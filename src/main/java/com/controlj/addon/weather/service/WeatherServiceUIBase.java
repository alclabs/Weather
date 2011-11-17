package com.controlj.addon.weather.service;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.noaa.WeatherServiceImpl;
import com.controlj.addon.weather.util.ResponseWriter;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public abstract class WeatherServiceUIBase implements WeatherServiceUI {
    //todo - override getServiceOptionHTML and generate refresh rates UI

    @Override
    public String getServiceConfigHTML() {
        return "    <p>pick service here</p>\n" +
                "    <h2>Refresh Rates</h2>\n" +
                "    <div class=\"indent\">\n" +
                "        <div class=\"nobr\">\n" +
                "            <label>Current Conditions:</label>\n" +
                "            <input type=\"text\" id=\"conditionrefresh\" name=\"conditionrefresh\" size=\"4\" value=\"\"/>\n" +
                "            minutes\n" +
                "        </div>\n" +
                "        <div class=\"nobr\">\n" +
                "            <label>Forecast:</label>\n" +
                "            <input type=\"text\" id=\"forecastrefresh\" name=\"forecastrefresh\" size=\"4\" value=\"\"/>\n" +
                "            minutes\n" +
                "        </div>\n" +
                "    </div>";
    }

    @Override
    public void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {

        String conditionRateString = req.getParameter("conditionrefresh");
        String forecastRateString = req.getParameter("forecastrefresh");

        if (conditionRateString != null) {
            try {
                configData.setConditionsRefreshInMinutes(Integer.parseInt(conditionRateString));
            } catch (NumberFormatException e) {
                writer.addValidationError("conditionrefresh", "\"" + conditionRateString + "\" is not a valid number");
            }
        } else {
            writer.addValidationError("conditionrefresh", "condition rate not specified");
        }

        if (forecastRateString!= null) {
            try {
                configData.setForecastsRefreshInMinutes(Integer.parseInt(forecastRateString));
            } catch (NumberFormatException e) {
                writer.addValidationError("forecastrefresh", "\"" + forecastRateString + "\" is not a valid number");
            }
        } else {
            writer.addValidationError("forecastrefresh", "forecast rate not specified");
        }
    }

    protected void updateConfigField(String field, ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        configData.getServiceConfigData().put(field, req.getParameter(field));
    }

    protected void updateIntegerConfigField(String field, ConfigData configData, ResponseWriter writer, HttpServletRequest req, int min, int max) {
        String stringVal = req.getParameter(field);
        try {
            int val = Integer.parseInt(stringVal);
            if (val < min) {
                writer.addValidationError(field, "\""+stringVal+"\" is less than the minimum of "+min);
            } else if (val > max) {
                writer.addValidationError(field, "\""+stringVal+"\" is greater than the maximum of "+max);
            } else {
                configData.getServiceConfigData().put(field, stringVal);
            }
        } catch (NumberFormatException e) {
            writer.addValidationError(field, "\""+stringVal+"\" is not an integer");
        }
    }
}
