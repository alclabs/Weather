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
public class ScheduledWeatherLookup implements ServletContextListener {
    private static final AtomicReference<ScheduledWeatherLookup> ref = new AtomicReference<ScheduledWeatherLookup>();
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> conditionsUpdateFuture;
    private ScheduledFuture<?> forecastsUpdateFuture;

    /**
     * Starts the scheduled update of weather information when the context starts.
     */
    //@Override
    public synchronized void contextInitialized(ServletContextEvent sce) {
        ref.set(this);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        ConfigData configData = ConfigDataFactory.loadConfigData();
        int conditionsRefresh = configData.getConditionsRefreshInMinutes();
        int forecastsRefresh = configData.getForecastsRefreshInMinutes();
        rescheduleUpdates(1, conditionsRefresh, forecastsRefresh);
    }

    /**
     * Stops the scheduled update of weather information when the context is destroyed.  This happens when
     * the server is shutting down, or if this add-on is disabled.
     */
    //@Override
    public synchronized void contextDestroyed(ServletContextEvent sce) {
        scheduledExecutorService.shutdownNow();
    }

    public static void rescheduleUpdates(int conditionsRefresh, int forecastsRefresh) {
        ref.get().rescheduleUpdates(1, conditionsRefresh, forecastsRefresh);
    }

    /**
     * Stops any running updates and reschedules them based on the current saved configuration data.
     * The updates have an initial delay as specified.
     */
    private synchronized void rescheduleUpdates(int initialDelay, int conditionsRefresh, int forecastsRefresh) {
        if (conditionsUpdateFuture != null)
            conditionsUpdateFuture.cancel(false);
        if (forecastsUpdateFuture != null)
            forecastsUpdateFuture.cancel(false);

        Logging.println("Starting scheduled update of weather information:");

        Logging.println("    current conditions updated at a fixed rate of every " + conditionsRefresh + " minutes");
        conditionsUpdateFuture = scheduledExecutorService.scheduleAtFixedRate(new ConditionsUpdate(), initialDelay * 60, conditionsRefresh * 60, TimeUnit.SECONDS);

        Logging.println("    forecasts updated at a fixed rate of every " + forecastsRefresh + " minutes");
        forecastsUpdateFuture = scheduledExecutorService.scheduleAtFixedRate(new ForecastsUpdate(), initialDelay * 60, forecastsRefresh * 60, TimeUnit.SECONDS);
    }

    private static class ConditionsUpdate implements Runnable {
        //@Override
        public void run() {
            try {
                ConfigData configData = ConfigDataFactory.loadConfigData();
                WeatherLookup weatherLookup = new WeatherLookup(configData);
                for (WeatherConfigEntry entry : configData.getList()) {
                    try {
                        EquipmentHandler handler = new EquipmentHandler(configData.getSystemConn(), entry.getCpPath());
                        if (handler.hasFieldsToWrite())
                            weatherLookup.lookupConditionsData(entry, true);
                    } catch (Exception e) {
                        if (e.getMessage().equals("Missing station content"))
                            Logging.println("Conditions data missing station content for entry " + entry);
                        else
                            Logging.println("Error writing conditions data for entry " + entry, e);
                    }

                    Thread.sleep(2000);
                }
            } catch (InterruptedException ignored) {
                // we must be being rescheduled, so just return from the run()
            }
        }
    }

    private static class ForecastsUpdate implements Runnable {
        //@Override
        public void run() {
            try {
                ConfigData configData = ConfigDataFactory.loadConfigData();
                WeatherLookup weatherLookup = new WeatherLookup(configData);
                for (WeatherConfigEntry entry : configData.getList()) {
                    try {
                        EquipmentHandler handler = new EquipmentHandler(configData.getSystemConn(), entry.getCpPath());
                        if (handler.hasFieldsToWrite())
                            weatherLookup.lookupForecastsData(entry, true);
                    } catch (Exception e) {
                        Logging.println("Error writing forecasts data for entry " + entry, e);
                    }

                    Thread.sleep(2000);
                }
            } catch (InterruptedException ignored) {
                // we must be being rescheduled, so just return from the run()
            }
        }
    }
}