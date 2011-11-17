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
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AjaxController extends HttpServlet {
    private static final String ACTION_PARAM_NAME = "action";
    private static final String ACTION_INIT = "init";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_POSTCONFIG = "postconfig";

    private static final String JSON_DATA = "data";


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

    private void updateRates(ConfigData configData, ResponseWriter writer, HttpServletRequest req) throws IOException {
        try {
            WeatherServiceUI ui = configData.getWeatherService().getUI();
            ui.updateConfiguration(configData, writer, req);
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
        }

        // if there are no errors
        if (!writer.hasErrors()) {
            configData.save();
            retrieveData(configData, writer);
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
        writer.putStringChild(JSON_DATA, "conditionrefresh", Integer.toString(configData.getConditionsRefreshInMinutes()));
        writer.putStringChild(JSON_DATA, "forecastrefresh", Integer.toString(configData.getForecastsRefreshInMinutes()));
        Map<String,String> data = configData.getServiceConfigData();
        for (String key : data.keySet()) {
            writer.putStringChild(JSON_DATA, key, data.get(key));
        }
        writeLocations(writer, configData);
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
