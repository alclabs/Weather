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

import com.controlj.addon.weather.ScheduledWeatherLookup;
import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.*;
import com.controlj.addon.weather.service.*;
import com.controlj.addon.weather.util.Logging;
import com.controlj.addon.weather.util.ResponseWriter;
import com.controlj.addon.weather.wbug.WeatherServiceImpl;
import com.controlj.addon.weather.wbug.service.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AjaxController extends HttpServlet {
    private static final String ACTION_PARAM_NAME = "action";
    private static final String ROW_PARAM_NAME = "rownum";
    private static final String PARAM_SERVICE = "service";
    private static final String PARAM_LOCATION = "location";

    private static final String ACTION_INIT = "init";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_SHOWDATA = "showdata";
    private static final String ACTION_POSTCONFIG = "postconfig";
    private static final String ACTION_DELETEROW = "deleterow";
    private static final String ACTION_ADDROW = "addrow";
    private static final String ACTION_CHANGESERVICE = "changeservice";
    private static final String ACTION_UI = "ui";   // ui specific action, forwarded to WeatheServiceUI

    private static final String JSON_DATA = "data";
    private static final String JSON_NAME = "name";
    private static final String JSON_CURRENT = "current";
    private static final String JSON_STATION = "station";
    private static final String JSON_FORECAST = "forecast";
    private static final String JSON_FORECAST_HEADERS = "forecastheaders";
    private static final String JSON_ICON     = "icon";
    private static final String JSON_FIELD    = "field";
    private static final String JSON_VALUE    = "value";


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean saveConfig = true;
        ConfigData configData = getConfigData(req);
        ResponseWriter writer = new ResponseWriter(resp);

        String action = req.getParameter(ACTION_PARAM_NAME);

        if (ACTION_POSTCONFIG.equals(action)) {
            configData = updateOrCreateConfiguration(configData, writer, req);
        } else if (ACTION_DELETEROW.equals(action)) {
            deleteRow(configData, writer, req);
        } else if (ACTION_ADDROW.equals(action)) {
            addRow(configData, writer, req);
        } else if (ACTION_CHANGESERVICE.equals(action)) {
            configData = changeService(writer, req);
            saveConfig = false;
        } else {
            String message = "Unknown action \"" + action + "\" specified in post for controller";
            writer.addError(message);
            Logging.println("ERROR:"+message);
        }

        // if there are no errors
        if (!writer.hasErrors()) {
            if (saveConfig)
                configData.save();
            retrieveData(configData, writer);
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
        } else if (ACTION_SHOWDATA.equals(action)) {
            showData(configData, writer, req);
        } else if (ACTION_UI.equals(action)) {
            uiAction(configData, writer, req);
        } else {
            String message = "Unknown action \"" + action + "\" specified for controller";
            writer.addError(message);
            Logging.println("ERROR:" + message);
        }
        writer.write();
    }

    private ConfigData changeService(ResponseWriter writer, HttpServletRequest req) throws IOException {
        WeatherServices serviceEnum = WeatherServiceUIBase.getSpecifiedService(req);
        ConfigData configData = ConfigDataFactory.create(serviceEnum);

        try {
            WeatherService weatherService = configData.getWeatherService();
            retrieveUI(weatherService.getUI(), writer);
            retrieveData(configData, writer);
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
            Logging.println("Error getting weather service:" + e.getMessage(), e);
        }
        return configData;
    }

    private void showData(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        String rowString = req.getParameter(ROW_PARAM_NAME);
        try {
            int rowNum = Integer.parseInt(rowString);
            WeatherConfigEntry entry = configData.getList().get(rowNum);

            // display name
            WeatherServiceUI ui = configData.getWeatherService().getUI();
            String displayName = ui.getEntryDisplayName(entry);
            writer.putString(JSON_NAME, displayName);

            // station data
            StationSource stationSource = entry.getStationSource();
            if (stationSource != null) {
                for (StationField field : StationField.values()) {
                    if (field.isSupported(stationSource)) {
                        Map<String, Object> row = new HashMap<String, Object>(2);
                        row.put(JSON_FIELD, field.getName());
                        row.put(JSON_VALUE, formatValue(field.getValue(stationSource)));
                        writer.appendToArray(JSON_STATION, row);
                    }
                }
            }

            // current conditions
            ConditionsSource conditionsSource = ScheduledWeatherLookup.updateConditionsData(configData, entry);
            if (conditionsSource != null) {
                for (ConditionsField field : ConditionsField.values()) {
                    if (field.isSupported(conditionsSource)) {
                        Map<String, Object> row = new HashMap<String, Object>(2);
                        row.put(JSON_FIELD, field.getName());
                        row.put(JSON_VALUE, formatValue(field.getValue(conditionsSource)));
                        writer.appendToArray(JSON_CURRENT, row);
                    }
                }
            }

            // forecast data
            ForecastSource[] forecastSources = ScheduledWeatherLookup.updateForecastsData(configData, entry);
            if (forecastSources != null) {
                writer.appendToArray(JSON_FORECAST_HEADERS, "Field");
                for (int i=0; i<forecastSources.length; i++) {
                    writer.appendToArray(JSON_FORECAST_HEADERS, "Day "+i+ " Value");
                }

                for (ForecastField field : ForecastField.values()) {
                    if (field.isSupported(forecastSources[0])) {
                        String row[] = new String[forecastSources.length + 1];
                        row[0] = field.getName('?');
                        int i=1;
                        for (ForecastSource forecastSource : forecastSources) {
                            row[i++] = formatValue(field.getValue(forecastSource));
                        }
                        writer.appendToArray(JSON_FORECAST, row);
                    }
                }
            }

            // icon data
            for (WeatherIcon icon : WeatherIcon.values()) {
                Map<String, Object> row = new HashMap<String, Object>(2);
                row.put(JSON_FIELD, icon.getDisplayName());
                row.put(JSON_VALUE, icon.getValue());
                writer.appendToArray(JSON_ICON, row);
            }

        } catch (NumberFormatException e) {
            writer.addError("Error deleting row.  Invalid row number '"+rowString+"'");
            Logging.println("Error deleting row.  Invalid row number '"+rowString+"'", e);
        } catch (WeatherServiceException e) {
                writer.addError("Error getting weather service:" + e.getMessage());
                Logging.println("Error getting weather service:" + e.getMessage(), e);
        }
    }

    private void uiAction(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        try {
            WeatherServiceUI ui = configData.getWeatherService().getUI();
            ui.dialogAction(configData, writer, req);

        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
            Logging.println("Error getting weather service:" + e.getMessage(), e);
        }

    }

    private String formatValue(Object value) {
        //todo - safer formatting
        if (value == null) {
            return "-";
        } else {
            return value instanceof Date ? PrimitiveServletBase.timeFormat.format(value) : value.toString();
        }
    }


    private void deleteRow(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        String rowString = req.getParameter(ROW_PARAM_NAME);
        try {
            int rowNum = Integer.parseInt(rowString);
            configData.delete(rowNum);
        } catch (NumberFormatException e) {
            writer.addError("Error deleting row.  Invalid row number '"+rowString+"'");
            Logging.println("Error deleting row.  Invalid row number '"+rowString+"'", e);
        }
    }

    private void addRow(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        try {
            WeatherServiceUI ui = configData.getWeatherService().getUI();
            ui.addRow(configData, writer, req);
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
            Logging.println("Error getting weather service:" + e.getMessage(), e);
        }

    }

    private ConfigData getConfigData(HttpServletRequest request)
    {
       ConfigData data = (ConfigData) request.getAttribute("config_data");
       if (data == null)
          data = ConfigDataFactory.loadConfigData();
       return data;
    }

    /*
     Typically this just updates the values in the provided configData.  However, when the weather
         service is changed, it has to create a new configData using the defaults from the
         new weather service.
     */
    private ConfigData updateOrCreateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req) throws IOException {
        WeatherServices serviceEnum = WeatherServiceUIBase.getSpecifiedService(req);

        if (!configData.getWeatherServiceEnum().equals(serviceEnum)) {
            configData = ConfigDataFactory.create(serviceEnum);
        }

        try {
            WeatherService ws = configData.getWeatherService();
            WeatherServiceUI ui = ws.getUI();
            ui.updateConfiguration(configData, writer, req);
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
            Logging.println("Error getting weather service:" + e.getMessage(), e);
        }
        return configData;
    }


    private void retrieveUI(ConfigData configData, ResponseWriter writer) {
        WeatherServiceUI ui = null;
        try {
            ui = configData.getWeatherService().getUI();
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
        }
        retrieveUI(ui, writer);
    }

    private void retrieveUI(WeatherServiceUI ui, ResponseWriter writer) {
        writer.putString("adddialog", ui.getAddDialogHTML());
        writer.putString("serviceconfig", ui.getServiceConfigHTML());

        // Get Weather Services
        for (WeatherServices service : WeatherServices.values()) {
            Map<String, Object> row = new HashMap<String, Object>(2);

            row.put("key", service.name());
            row.put("display", service.getDisplayName());
            writer.appendToArray("services", row);
        }

        // Get headers for entry
        List<String> serviceEntryFields = ui.getServiceEntryFields();

        writer.appendToArray("entryheaders", " ");
        writer.appendToArray("entryheaders", "Location Path");
        for (String serviceEntryField : serviceEntryFields) {
            writer.appendToArray("entryheaders", ui.getServiceEntryHeaderName(serviceEntryField));
        }
        writer.appendToArray("entryheaders", "Last Update");
    }


    private void retrieveData(ConfigData configData, ResponseWriter writer) throws IOException {
        WeatherServices service = configData.getWeatherServiceEnum();
        writer.putString("currentservice", service.name());
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
        WeatherServiceUI ui;
        try {
            ui = configData.getWeatherService().getUI();
            int entrySize = ui.getServiceEntryFields().size()+2; // add path and update time

            for (WeatherConfigEntry location : locations) {
                String[] next = new String[entrySize];

                next[0] = location.getCpPath();
                next[entrySize-1] = location.getLastUpdate();
                Map<String, String> serviceEntryData = location.getServiceEntryData();
                int i=1;
                for (String fieldName : ui.getServiceEntryFields()) {
                    next[i++] = serviceEntryData.get(fieldName);
                }
                //next.put("zip", location.getZipCode()); // TODO: fix me!

                writer.appendToArray("locations", next);
            }
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:"+e.getMessage());
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
