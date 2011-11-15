package com.controlj.addon.weather.servlets;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.util.Logging;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class AjaxController extends HttpServlet {
    private static final String ACTION_PARAM_NAME = "action";
    private static final String ACTION_INIT = "init";
    private static final String ACTION_POSTRATES = "postrates";


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String action = req.getParameter(ACTION_PARAM_NAME);
        if (ACTION_INIT.equals(action)) {
            retrieveAll(req, resp);
        } else if (ACTION_POSTRATES.equals(action)) {
            updateRates(req, resp);
        } else {
            String msg = "Unknown action \""+action+"\" specified for controller";
            resp.sendError(500, msg);
            Logging.println("ERROR:"+msg);
        }
    }

    private ConfigData getConfigData(HttpServletRequest request)
    {
       ConfigData data = (ConfigData) request.getAttribute("config_data");
       if (data == null)
          data = ConfigDataFactory.loadConfigData();
       return data;
    }

    private void updateRates(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ConfigData configData = getConfigData(req);
        JSONObject result = new JSONObject();

        String conditionRateString = req.getParameter("condition_rate");
        String forecastRateString = req.getParameter("forecast_rate");

        int conditionRate = 0;
        int forecastRate = 0;
        if (conditionRateString != null) {
            try {
                conditionRate = Integer.parseInt(conditionRateString);
            } catch (NumberFormatException e) {
                try {
                    result.put("errortype","validation");
                    result.put("field", "condition_rate");
                } catch (JSONException e1) { } // ignore
            }
        }
        if (forecastRateString!= null) {
            try {
                forecastRate = Integer.parseInt(forecastRateString);
            } catch (NumberFormatException e) {
                try {
                    result.put("errortype","validation");
                    result.put("field", "forecast_rate");
                } catch (JSONException e1) { } // ignore
            }
        }

        // if there was an error
        if (result.keys().hasNext()) {
            resp.sendError(403, result.toString());
        } else {
            configData.setConditionsRefreshInMinutes(conditionRate);
            configData.setForecastsRefreshInMinutes(forecastRate);
            configData.save();
            retrieveAll(req, resp);
        }
    }

    private void retrieveAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ConfigData configData = getConfigData(req);
        JSONObject result = new JSONObject();

        try {
            result.put("conditionrefresh", configData.getConditionsRefreshInMinutes());
            result.put("forecastrefresh", configData.getForecastsRefreshInMinutes());
            result.put("locations", listLocations(configData));
            result.write(resp.getWriter());
        } catch (JSONException e) {
            handleError(resp, e);
        }
    }

    private JSONArray listLocations(ConfigData configData) throws JSONException {
        List<WeatherConfigEntry> locations = configData.getList();
        final JSONArray results = new JSONArray();

        for (WeatherConfigEntry location : locations) {
            JSONObject next = new JSONObject();
            next.put("path", location.getCpPath());
            next.put("zip", location.getZipCode());
            next.put("update", location.getLastUpdate());
            results.put(next);
        }
        return results;
    }

    private void handleError(HttpServletResponse resp, Throwable th) throws IOException {
        resp.sendError(500, th.getMessage());
        Logging.println(th);
    }
}
