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
package com.controlj.addon.weather.service;

import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.data.ConditionsSource;

import java.util.Map;

/**
 * A WeatherService is used to look up weather information given some configuration data.
 */
public interface WeatherService
{
   /**
    * Takes the given configuration data (for now, a zip code) and does whatever the service
    * needs in order to later get the various Weather/Station/Forecast sources using that
    * configuration data.  It should that resolved information and given back a String "key"
    * that will later be used when getting the Weather/Station/Forecast sources.
    *
    * @param zipCode the zip code supplied by the user -- might not be valid.
    * @return a key to the resolved information.
    */
   public StationSource resolveConfigurationToStation(String zipCode) throws InvalidConfigurationDataException, WeatherServiceException;

   /**
    * Retrieves the current weather conditions using the given configuration information.
    * indicated by <code>key</code>.
    *
    * @param configData configuration settings for the whole weather service.
    * @param entryData configuration for the entry for which to retrieve conditions.
    * @return the current weather conditions.
    */
   public ConditionsSource getConditionsSource(Map<String, String> configData, StationSource stationSource, Map<String, String> entryData) throws WeatherServiceException;

   /**
    * Retrieves the weather forecast information using the given configuration information.
    * indicated by <code>key</code>.  The number of days of forecast information that is
    * desired is given by <code>forecastDays</code>.  If the service cannot provide that many
    * days, it shall return as many days as it can provide.  The result is an array of forecasts
    * where index 0 is 1 day out, index 1 is 2 days out, etc..
    *
    * @param configData configuration settings for the whole weather service.
    * @param entryData configuration for the entry for which to retrieve conditions.
    * @return the forecast information.
    */
   public ForecastSource[] getForecastSources(Map<String, String> configData, StationSource stationSource, Map<String, String> entryData) throws WeatherServiceException;

    /**
    * Retrieves the object used to manage the interaction with the WeatherService's UI.
    * @return the WeatherServiceUI object
    */
   public WeatherServiceUI getUI();
    
}
