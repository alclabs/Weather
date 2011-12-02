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
package com.controlj.addon.weather.wbug;

import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.InvalidConfigurationDataException;
import com.controlj.addon.weather.service.WeatherService;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceUI;
import com.controlj.addon.weather.wbug.service.*;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherServiceImpl implements WeatherService
{
   static final String CONFIG_KEY_UNITS = "units";
   static final String CONFIG_VALUE_UNITS_IMPERIAL = "imperial";
   static final String CONFIG_VALUE_UNITS_METRIC = "metric";

   private static final AtomicReference<String> LICENSE_KEY = new AtomicReference<String>();
   private WeatherServiceUI ui = new WeatherServiceUIImpl();

   public StationSource resolveConfigurationToStation(boolean isZip, String cityZipCode, String stationCode) throws InvalidConfigurationDataException, WeatherServiceException
   {
      try
      {
         Station result = null;
         Station[] stations;
         if (isZip) {
             stations = getService().getStationListByUSZipCode(cityZipCode);
         } else {
             stations = getService().getStationListByCityCode(cityZipCode);
         }
         for (Station station : stations) {
            if (stationCode.equals(station.getId())) {
                 result = station;
                 break;
             }
         }

         if (result == null)
            throw new InvalidConfigurationDataException("Unknown station code "+stationCode);

         return new StationSourceAdapter(result);
      }
      catch (WeatherBugServiceException e)
      {
         throw new WeatherServiceException(e.getMessage(), e);
      }
   }

   @Override
   public ConditionsSource getConditionsSource(Map<String, String> configData, StationSource stationSource, Map<String, String> entryData) throws WeatherServiceException
   {
      boolean isMetric = CONFIG_VALUE_UNITS_METRIC.equals(configData.get(CONFIG_KEY_UNITS));
      try
      {
         LiveWeather liveWeather = getService().getLiveWeatherByStationID(stationSource.getId(), isMetric ? 1 : 0);
         if (liveWeather == null)
             throw new WeatherServiceException("Error getting live weather data");
         return new ConditionsSourceAdapter(liveWeather);
      }
      catch (WeatherBugServiceException e)
      {
         throw new WeatherServiceException(e.getMessage(), e);
      }
   }

   @Override
   public ForecastSource[] getForecastSources(Map<String, String> configData, StationSource stationSource, Map<String, String> entryData) throws WeatherServiceException
   {
      boolean isMetric = CONFIG_VALUE_UNITS_METRIC.equals(configData.get(CONFIG_KEY_UNITS));
      Forecasts forecasts;
      try
      {
         forecasts = getService().getForecastByLatLong(stationSource.getLatitude(), stationSource.getLongitude(), isMetric ? 1 : 0);
          if (forecasts == null)
              throw new WeatherServiceException("Error getting live weather data");
      }
      catch (WeatherBugServiceException e)
      {
         throw new WeatherServiceException(e.getMessage(), e);
      }

      Forecast[] forecastArray = forecasts.getForecasts();
      ForecastSource[] results = new ForecastSource[forecastArray.length];
      for (int i = 0; i < forecastArray.length; i++)
         results[i] = new ForecastSourceAdapter(forecasts, forecastArray[i]);

      return results;
   }

   public Location[] findLocations(String searchString) throws WeatherServiceException {
       try {
           return getService().getLocationList(searchString);
       } catch (WeatherBugServiceException e) {
           throw new WeatherServiceException(e.getMessage(), e);
       }
   }

   public Station[] findStations(boolean isZip, String cityZipCode) throws WeatherServiceException {
       try {
           if (isZip) {
               return getService().getStationListByUSZipCode(cityZipCode);
           } else {
               return getService().getStationListByCityCode(cityZipCode);
           }
       } catch (WeatherBugServiceException e) {
           throw new WeatherServiceException(e.getMessage(), e);
       }
   }

   @Override public Map<String, String> getDefaults()
   {
      return Collections.emptyMap();
   }

   @Override public WeatherServiceUI getUI()
   {
      return ui;
   }

   private WeatherBugService getService() throws WeatherServiceException
   {
      return new WeatherBugService(getKey());
   }

   private String getKey() throws WeatherServiceException
   {
      String key = LICENSE_KEY.get();
      if (key == null)
      {
         try
         {
            InputStream stream = getClass().getResourceAsStream("wbug-key.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            key = reader.readLine();
            LICENSE_KEY.set(key);
         }
         catch (IOException e)
         {
            throw new WeatherServiceException("Error reading WeatherBug license key", e);
         }
      }

      return key;
   }
}