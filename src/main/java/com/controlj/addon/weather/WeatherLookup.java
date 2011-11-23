/*=============================================================================
                    AUTOMATED LOGIC CORPORATION
            Copyright (c) 1999 - 2011 All Rights Reserved
     This document contains confidential/proprietary information.
===============================================================================

   @(#)WeatherLookup

   Author(s) jmurph
   $Log: $    
=============================================================================*/
package com.controlj.addon.weather;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.service.WeatherService;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.util.Logging;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WeatherLookup {
    private final ConfigData configData;

    public WeatherLookup(ConfigData configData) {
        this.configData = configData;
    }

    /**
     * Reads weather conditions data from the WeatherService, inserts it into the associated control program
     * for the given entry (if any) and saves this data for later use by a view page.
     *
     * @param entry the config entry for which this update is occurring.
     * @param force if true, will lookup data even if the latest data has not yet expired.
     * @return the conditions data read from the weather service.
     * @throws WeatherServiceException if the data could not be read from the weather service.
     */
    public ConditionsSource lookupConditionsData(WeatherConfigEntry entry, boolean force) throws WeatherServiceException {
        RuntimeInformation rti = RuntimeInformation.getSingleton();
        if (!force) {
            ConditionsSource conditionsData = rti.getLastConditionsData(entry);
            if (conditionsData != null && conditionsData.getUpdateTime().after(getConditionDataExpirary()))
                return conditionsData;
        }

        ConditionsSource conditionsSource = null;
        try {
            WeatherService weatherService = configData.getWeatherService();
            Logging.println("Reading conditions data for "+entry.getCpPath());
            conditionsSource = weatherService.getConditionsSource(configData.getServiceConfigData(),
                    entry.getStationSource(), entry.getServiceEntryData());

            String errorMessage = null;
            try {
                EquipmentHandler equipmentHandler = new EquipmentHandler(configData.getSystemConn(), entry.getCpPath());
                equipmentHandler.writeConditionsData(conditionsSource);
            } catch (EquipmentWriteException e) {
                Logging.println("Error writing current conditions to CP " + entry.getCpPath(), e);
                errorMessage = "Error writing data";
            }

            RuntimeInformation.getSingleton().updateConditionsData(entry, conditionsSource, errorMessage);
            return conditionsSource;
        } catch (WeatherServiceException e) {
            Logging.println("Error reading forecast data", e);
            RuntimeInformation.getSingleton().updateConditionsData(entry, null, "Error reading data");
            throw e;
        }
    }

    /**
     * Reads weather forecast data from the WeatherService, inserts it into the associated control program
     * for the given entry (if any) and saves this data for later use by a view page.
     *
     * @param entry the config entry for which this update is occurring.
     * @param force if true, will lookup data even if the latest data has not yet expired.
     * @return the forecast data read from the weather service.
     * @throws WeatherServiceException if the data could not be read from the weather service.
     */
    public ForecastSource[] lookupForecastsData(WeatherConfigEntry entry, boolean force) throws WeatherServiceException {
        RuntimeInformation rti = RuntimeInformation.getSingleton();
        if (!force) {
            ForecastSource[] forecastData = rti.getLastForecastData(entry);
            if (forecastData != null && forecastData[0].getUpdateTime().after(getForecastDataExpirary()))
                return forecastData;
        }

        try {
            WeatherService weatherService = configData.getWeatherService();
            Logging.println("Reading forecast data for "+entry.getCpPath());
            ForecastSource[] forecastSources = weatherService.getForecastSources(configData.getServiceConfigData(),
                    entry.getStationSource(), entry.getServiceEntryData());

            String errorMessage = null;
            try {
                EquipmentHandler equipmentHandler = new EquipmentHandler(configData.getSystemConn(), entry.getCpPath());
                equipmentHandler.writeForecastData(forecastSources);

                // periodically push this just so that it's kept up to date in the module (in case the equipment is reset to definition
                // defaults or recreated or something)
                equipmentHandler.writeStationData(entry.getStationSource());
            } catch (EquipmentWriteException e) {
                Logging.println("Error writing forecast data to CP " + entry.getCpPath(), e);
                errorMessage = "Error writing data";
            }

            RuntimeInformation.getSingleton().updateForecastData(entry, forecastSources, errorMessage);
            return forecastSources;
        } catch (WeatherServiceException e) {
            Logging.println("Error reading forecast data", e);
            RuntimeInformation.getSingleton().updateForecastData(entry, null, "Error reading data");
            throw e;
        }
    }

    private Date getConditionDataExpirary() {
        return getDataExpirary(configData.getConditionsRefreshInMinutes());
    }

    private Date getForecastDataExpirary() {
        return getDataExpirary(configData.getForecastsRefreshInMinutes());
    }

    private Date getDataExpirary(int refreshInMinutes) {
        long refresh = TimeUnit.MILLISECONDS.convert(refreshInMinutes, TimeUnit.MINUTES);
        return new Date(System.currentTimeMillis() - refresh);
    }
}