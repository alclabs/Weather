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
package com.controlj.addon.weather;

import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.ConditionsSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores information about the most recent updates to weather data for each entry.
 */
public class RuntimeInformation
{
   private static final RuntimeInformation singleton = new RuntimeInformation();

   /**
    * Gets the RuntimeInformation singleton.
    */
   public static RuntimeInformation getSingleton() { return singleton; }

   private final Map<WeatherConfigEntry, RuntimeData> runtimeDataMap = new HashMap<WeatherConfigEntry, RuntimeData>();

   /**
    * Updates the runtime information for the given entry with new conditions data.
    */
   public void updateConditionsData(WeatherConfigEntry entry, ConditionsSource conditionsSource, String message)
   {
      getOrCreateRuntimeData(entry).setConditionsSource(conditionsSource, message);
   }

   /**
    * Returns the last updated conditions data for the given entry.
    */
   public ConditionsSource getLastConditionsData(WeatherConfigEntry entry)
   {
      return getOrCreateRuntimeData(entry).getConditionsSource();
   }

   public String getLastConditionsError(WeatherConfigEntry entry)
   {
      return getOrCreateRuntimeData(entry).getConditionsError();
   }

   /**
    * Updates the runtime information for the given entry with new forecast data.
    */
   public void updateForecastData(WeatherConfigEntry entry, ForecastSource[] forecastSources, String message)
   {
      getOrCreateRuntimeData(entry).setForecastSources(forecastSources, message);
   }

   /**
    * Returns the last updated forecast data for the given entry.
    */
   public ForecastSource[] getLastForecastData(WeatherConfigEntry entry)
   {
      return getOrCreateRuntimeData(entry).getForecastSources();
   }

   public String getLastForecastError(WeatherConfigEntry entry)
   {
      return getOrCreateRuntimeData(entry).getForecastError();
   }

   private synchronized RuntimeData getOrCreateRuntimeData(WeatherConfigEntry entry)
   {
      RuntimeData runtimeData = runtimeDataMap.get(entry);
      if (runtimeData == null)
      {
         runtimeData = new RuntimeData();
         runtimeDataMap.put(entry, runtimeData);
      }
      return runtimeData;
   }

   private static class RuntimeData
   {
      private ConditionsSource conditionsSource;
      private String conditionsError;
      private ForecastSource[] forecastSources;
      private String forecastError;

      public synchronized ConditionsSource getConditionsSource()
      {
         return conditionsSource;
      }

      public synchronized void setConditionsSource(ConditionsSource conditionsSource, String errorMsg)
      {
         this.conditionsSource = conditionsSource;
         conditionsError = errorMsg;
      }

      public synchronized String getConditionsError()
      {
         return conditionsError;
      }

      public synchronized ForecastSource[] getForecastSources()
      {
         return forecastSources;
      }

      public synchronized void setForecastSources(ForecastSource[] forecastSources, String errorMsg)
      {
         this.forecastSources = forecastSources;
         forecastError = errorMsg;
      }

      public synchronized String getForecastError()
      {
         return forecastError;
      }
   }
}