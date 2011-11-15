/*
 * Copyright (c) 2011 Automated Logic Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.controlj.addon.weather.servlets;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceUI;
import com.controlj.addon.weather.util.Logging;
import com.controlj.addon.weather.util.ResponseWriter;
import org.json.JSONArray;
import org.json.JSONException;
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
    private static final String ACTION_POSTRATES = "postrates";
    private static final String ACTION_ADDDIALOG = "adddialog";


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String action = req.getParameter(ACTION_PARAM_NAME);

        if (ACTION_POSTRATES.equals(action)) {
            updateRates(req, resp);
        } else {
            String msg = "Unknown action \""+action+"\" specified for controller";
            resp.sendError(500, msg);
            Logging.println("ERROR:"+msg);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ACTION_PARAM_NAME);
        if (ACTION_INIT.equals(action)) {
            retrieveAll(req, resp);
        } else if (ACTION_ADDDIALOG.equals(action)) {
            getAddDialog(req, resp);
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
        ResponseWriter errorWriter = new ResponseWriter(resp);

        String conditionRateString = req.getParameter("condition_rate");
        String forecastRateString = req.getParameter("forecast_rate");

        int conditionRate = 0;
        int forecastRate = 0;
        if (conditionRateString != null) {
            try {
                conditionRate = Integer.parseInt(conditionRateString);
            } catch (NumberFormatException e) {
                errorWriter.addValidationError("condition_rate", "\"" + conditionRateString + "\" is not a valid number");
            }
        } else {
            errorWriter.addValidationError("condition_rate", "condition rate not specified");
        }

        if (forecastRateString!= null) {
            try {
                forecastRate = Integer.parseInt(forecastRateString);
            } catch (NumberFormatException e) {
                errorWriter.addValidationError("forecast_rate", "\"" + forecastRateString + "\" is not a valid number");
            }
        } else {
            errorWriter.addValidationError("forecast_rate", "forecast rate not specified");
        }

        // if there was an error
        if (errorWriter.hasErrors()) {
            errorWriter.write();
        } else {
            configData.setConditionsRefreshInMinutes(conditionRate);
            configData.setForecastsRefreshInMinutes(forecastRate);
            configData.save();

            retrieveAll(req, resp);
        }
    }

    private void getAddDialog(HttpServletRequest req, HttpServletResponse resp) {
        ConfigData configData = getConfigData(req);

        try {
            WeatherServiceUI wsui = configData.getWeatherService();
            resp.setContentType("text/html");
            wsui.writeAddDialog(resp.getWriter());
        } catch (WeatherServiceException e) {
            // todo - handle with error that shows up in ui
        } catch (IOException e) {
            // Can't write response, just log
            Logging.println("Can't write response from servlet", e);
        }
    }

    private void retrieveAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ConfigData configData = getConfigData(req);
        ResponseWriter writer = new ResponseWriter(resp);

        writer.putInteger("conditionrefresh", configData.getConditionsRefreshInMinutes());
        writer.putInteger("forecastrefresh", configData.getForecastsRefreshInMinutes());
        writeLocations(writer, configData);

        writer.write();
    }

    private void writeLocations(ResponseWriter writer, ConfigData configData) {
        List<WeatherConfigEntry> locations = configData.getList();

        for (WeatherConfigEntry location : locations) {
            HashMap<String, Object> next = new HashMap<String, Object>(3);
            next.put("path", location.getCpPath());
            //next.put("zip", location.getZipCode()); // TODO: fix me!
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
