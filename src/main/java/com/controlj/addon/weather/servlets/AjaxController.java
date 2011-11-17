package com.controlj.addon.weather.servlets;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceUI;
import com.controlj.addon.weather.util.Logging;
import com.controlj.addon.weather.util.ResponseWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class AjaxController extends HttpServlet {
    private static final String ACTION_PARAM_NAME = "action";
    private static final String ACTION_INIT = "init";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_POSTCONFIG = "postconfig";
    private static final String ACTION_ADDDIALOG = "adddialog";


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConfigData configData = getConfigData(req);
        ResponseWriter writer = new ResponseWriter(resp);

        String action = req.getParameter(ACTION_PARAM_NAME);

        if (ACTION_POSTCONFIG.equals(action)) {
            updateRates(configData, writer, req);
        } else {
            String message = "Unknown action \"" + action + "\" specified in post for controller";
            writer.addError(message);
            Logging.println("ERROR:"+message);
        }
        writer.write();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConfigData configData = getConfigData(req);
        ResponseWriter writer = new ResponseWriter(resp);

        String action = req.getParameter(ACTION_PARAM_NAME);
        if (ACTION_INIT.equals(action)) {
            retrieveData(configData, writer);
            retrieveUI(configData, writer);
        } else if (ACTION_UPDATE.equals(action)) {
            retrieveData(configData, writer);
        } else {
            String message = "Unknown action \"" + action + "\" specified for controller";
            writer.addError(message);
            Logging.println("ERROR:"+message);
        }
        writer.write();
    }

    private ConfigData getConfigData(HttpServletRequest request)
    {
       ConfigData data = (ConfigData) request.getAttribute("config_data");
       if (data == null)
          data = ConfigDataFactory.loadConfigData();
       return data;
    }

    private void updateRates(ConfigData config, ResponseWriter writer, HttpServletRequest req) throws IOException {

        String conditionRateString = req.getParameter("condition_rate");
        String forecastRateString = req.getParameter("forecast_rate");

        int conditionRate = 0;
        int forecastRate = 0;
        if (conditionRateString != null) {
            try {
                conditionRate = Integer.parseInt(conditionRateString);
            } catch (NumberFormatException e) {
                writer.addValidationError("condition_rate", "\"" + conditionRateString + "\" is not a valid number");
            }
        } else {
            writer.addValidationError("condition_rate", "condition rate not specified");
        }

        if (forecastRateString!= null) {
            try {
                forecastRate = Integer.parseInt(forecastRateString);
            } catch (NumberFormatException e) {
                writer.addValidationError("forecast_rate", "\"" + forecastRateString + "\" is not a valid number");
            }
        } else {
            writer.addValidationError("forecast_rate", "forecast rate not specified");
        }

        // if there was an error
        if (!writer.hasErrors()) {
            config.setConditionsRefreshInMinutes(conditionRate);
            config.setForecastsRefreshInMinutes(forecastRate);
            config.save();

            retrieveData(config, writer);
        }
    }

    private void retrieveUI(ConfigData configData, ResponseWriter writer) {
        try {
            WeatherServiceUI ui = configData.getWeatherService().getUI();
            writer.putString("adddialog", ui.getAddDialogHTML());
            writer.putString("serviceoptions", ui.getServiceOptionHTML());
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:"+e.getMessage());
        }
    }


    private void retrieveData(ConfigData configData, ResponseWriter writer) throws IOException {
        writer.putInteger("conditionrefresh", configData.getConditionsRefreshInMinutes());
        writer.putInteger("forecastrefresh", configData.getForecastsRefreshInMinutes());
        writeLocations(writer, configData);
    }

    private void writeLocations(ResponseWriter writer, ConfigData configData) {
        List<WeatherConfigEntry> locations = configData.getList();

        for (WeatherConfigEntry location : locations) {
            HashMap<String, Object> next = new HashMap<String, Object>(3);
            next.put("path", location.getCpPath());
            next.put("zip", location.getZipCode());
            next.put("update", location.getLastUpdate());

            writer.appendToArray("locations", next);
        }
    }

    private void handleError(HttpServletResponse resp, Throwable th) throws IOException {
        resp.sendError(500, th.getMessage());
        Logging.println(th);
    }

    private void addValidationError(JSONObject response, String field, String message) {
        JSONArray errors = null;
    }
}
