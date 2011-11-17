package com.controlj.addon.weather.service;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.util.ResponseWriter;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public abstract class WeatherServiceUIBase implements WeatherServiceUI {
    //todo - override getServiceOptionHTML and generate refresh rates UI

    @Override
    public void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {

        String conditionRateString = req.getParameter("condition_rate");
        String forecastRateString = req.getParameter("forecast_rate");

        if (conditionRateString != null) {
            try {
                configData.setConditionsRefreshInMinutes(Integer.parseInt(conditionRateString));
            } catch (NumberFormatException e) {
                writer.addValidationError("condition_rate", "\"" + conditionRateString + "\" is not a valid number");
            }
        } else {
            writer.addValidationError("condition_rate", "condition rate not specified");
        }

        if (forecastRateString!= null) {
            try {
                configData.setForecastsRefreshInMinutes(Integer.parseInt(forecastRateString));
            } catch (NumberFormatException e) {
                writer.addValidationError("forecast_rate", "\"" + forecastRateString + "\" is not a valid number");
            }
        } else {
            writer.addValidationError("forecast_rate", "forecast rate not specified");
        }
    }
}
