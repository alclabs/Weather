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

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.service.WeatherService;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.util.Logging;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is responsible for retrieving weather data on a recurring periodic basic and
 * updating the associated control program and/or storing that data for later use on graphics
 * pages.  This class implements ServletContextListener so that it can be started up when the
 * web server starts.
 */
public class ScheduledWeatherLookup implements ServletContextListener
{
   private static AtomicReference<ScheduledWeatherLookup> ref = new AtomicReference<ScheduledWeatherLookup>();
   private ScheduledExecutorService scheduledExecutorService;
   private ScheduledFuture<?> conditionsUpdateFuture;
   private ScheduledFuture<?> forecastsUpdateFuture;

   /**
    * Starts the scheduled update of weather information when the context starts.
    */
   @Override public synchronized void contextInitialized(ServletContextEvent sce)
   {
      ref.set(this);
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      rescheduleUpdates(1);
   }

   /**
    * Stops the scheduled update of weather information when the context is destroyed.  This happens when
    * the server is shutting down, or if this add-on is disabled.
    */
   @Override public synchronized void contextDestroyed(ServletContextEvent sce)
   {
      scheduledExecutorService.shutdownNow();
   }

   /**
    * Stops any running updates and reschedules them based on the current saved configuration data.
    * The updates have an initial delay as specified.
    */
   private synchronized void rescheduleUpdates(int initialDelay)
   {
      if (conditionsUpdateFuture != null)
         conditionsUpdateFuture.cancel(false);
      if (forecastsUpdateFuture != null)
         forecastsUpdateFuture.cancel(false);

      Logging.println("Starting scheduled update of weather information:");

      int conditionsRefresh = ConfigDataFactory.loadConfigData().getConditionsRefreshInMinutes();
      Logging.println("    current conditions updated at a fixed rate of every "+conditionsRefresh+" minutes");
      conditionsUpdateFuture = scheduledExecutorService.scheduleAtFixedRate(new ConditionsUpdate(), initialDelay, conditionsRefresh, TimeUnit.MINUTES);

      int forecastsRefresh = ConfigDataFactory.loadConfigData().getForecastsRefreshInMinutes();
      Logging.println("    forecasts updated at a fixed rate of every "+forecastsRefresh+" minutes");
      forecastsUpdateFuture = scheduledExecutorService.scheduleAtFixedRate(new ForecastsUpdate(), initialDelay, forecastsRefresh, TimeUnit.MINUTES);
   }

   /**
    * Stops any running updates and reschedules them based on the current saved configuration data.  Used when
    * the UI changes the refresh rate.
    */
   public static void rescheduleUpdates()
   {
      ref.get().rescheduleUpdates(0);
   }

   /**
    * Reads weather conditions data from the WeatherService, inserts it into the associated control program
    * for the given entry (if any) and saves this data for later use by a view page.
    * @param configData the config data.  Used to get the selected weather service.
    * @param entry the config entry for which this update is occurring.
    * @return the conditions data read from the weather service.
    * @throws WeatherServiceException if the data could not be read from the weather service.
    */
   public static ConditionsSource updateConditionsData(ConfigData configData, WeatherConfigEntry entry) throws WeatherServiceException
   {
      ConditionsSource conditionsSource = null;
      try
      {
         WeatherService weatherService = configData.getWeatherService();
         conditionsSource = weatherService.getConditionsSource(entry.getStationSource(), entry.isMetric());

         String errorMessage = null;
         try
         {
            EquipmentHandler equipmentHandler = new EquipmentHandler(configData.getSystemConn(), "ABSPATH:1:"+entry.getCpPath());
            equipmentHandler.writeConditionsData(conditionsSource);
         }
         catch (EquipmentWriteException e)
         {
            Logging.println("Error writing current conditions to CP "+entry.getCpPath(), e);
            errorMessage = "Error writing data";
         }

         RuntimeInformation.getSingleton().updateConditionsData(entry, conditionsSource, errorMessage);
         return conditionsSource;
      }
      catch (WeatherServiceException e)
      {
         Logging.println("Error reading forecast data", e);
         RuntimeInformation.getSingleton().updateConditionsData(entry, null, "Error reading data");
         throw e;
      }
   }

   /**
    * Reads weather forecast data from the WeatherService, inserts it into the associated control program
    * for the given entry (if any) and saves this data for later use by a view page.
    * @param configData the config data.  Used to get the selected weather service.
    * @param entry the config entry for which this update is occurring.
    * @return the forecast data read from the weather service.
    * @throws WeatherServiceException if the data could not be read from the weather service.
    */
   public static ForecastSource[] updateForecastsData(ConfigData configData, WeatherConfigEntry entry) throws WeatherServiceException
   {
      try
      {
         WeatherService weatherService = configData.getWeatherService();
         ForecastSource[] forecastSources = weatherService.getForecastSources(entry.getZipCode(), 7, entry.isMetric());

         String errorMessage = null;
         try
         {
            EquipmentHandler equipmentHandler = new EquipmentHandler(configData.getSystemConn(), "ABSPATH:1:"+entry.getCpPath());
            equipmentHandler.writeForecastData(forecastSources);

            // periodically push this just so that it's kept up to date in the module (in case the equipment is reset to definition
            // defaults or recreated or something)
            equipmentHandler.writeStationData(entry.getStationSource());
         }
         catch (EquipmentWriteException e)
         {
            Logging.println("Error writing forecast data to CP "+entry.getCpPath(), e);
            errorMessage = "Error writing data";
         }

         RuntimeInformation.getSingleton().updateForecastData(entry, forecastSources, errorMessage);
         return forecastSources;
      }
      catch (WeatherServiceException e)
      {
         Logging.println("Error reading forecast data", e);
         RuntimeInformation.getSingleton().updateForecastData(entry, null, "Error reading data");
         throw e;
      }
   }

   private static class ConditionsUpdate implements Runnable
   {
      @Override public void run()
      {
         ConfigData configData = ConfigDataFactory.loadConfigData();
         for (WeatherConfigEntry entry : configData.getList())
         {
            try
            {
               updateConditionsData(configData, entry);
            }
            catch (Exception e)
            {
               Logging.println("Error writing conditions data for entry "+entry, e);
            }
         }
      }
   }

   private static class ForecastsUpdate implements Runnable
   {
      @Override public void run()
      {
         ConfigData configData = ConfigDataFactory.loadConfigData();
         for (WeatherConfigEntry entry : configData.getList())
         {
            try
            {
               updateForecastsData(configData, entry);
            }
            catch (Exception e)
            {
               Logging.println("Error writing forecasts data for entry "+entry, e);
            }
         }
      }
   }
}