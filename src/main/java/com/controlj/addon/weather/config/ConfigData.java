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
package com.controlj.addon.weather.config;

import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.WeatherService;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceFactory;
import com.controlj.addon.weather.service.WeatherServices;
import com.controlj.green.addonsupport.access.*;

import java.util.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Holds the configuration data for this add-on.  Get one of these from
 * the {@link ConfigDataFactory}.
 */
public class ConfigData
{
   private static final String DATASTORE_NAME = "WeatherConfig";
   private final AtomicReference<WeatherService> serviceRef = new AtomicReference<WeatherService>();
   private final List<WeatherConfigEntry> list = new ArrayList<WeatherConfigEntry>();
   private final SystemConnection systemConn;

   private WeatherServices service = WeatherServices.nws; // the default is NWS
   private int conditionsRefreshInMinutes = 60;
   private int forecastsRefreshInMinutes = 120;
   private Map<String, String> serviceConfigData = new HashMap<String,String>();

   ConfigData(SystemConnection systemConn)
   {
      this.systemConn = systemConn;
   }

   ConfigData(SystemConnection systemConn, WeatherServices service)
   {
      this(systemConn);
      this.service = service;
   }

   /**
    * Loads the configuration data from the datastore.  Called by {@link ConfigDataFactory}.
    * @throws IOException if the datastore cannot be read.
    */
   public void load() throws IOException
   {
      try
      {
         systemConn.runReadAction(new ReadAction()
         {
            public void execute(SystemAccess systemAccess) throws Exception
            {
               DataStore store = systemAccess.getSystemDataStore(DATASTORE_NAME);
               Properties properties = loadFile(store.getInputStream());
               extractConfig(new ConfigProperties(properties));
            }
         });
      }
      catch (ActionExecutionException e)
      {
         throw new IOException("Error reading from data store");
      }
      catch (SystemException e)
      {
         throw new IOException("Error reading from data store");
      }
   }

   private Properties loadFile(InputStream in) throws IOException
   {
      Properties properties = new Properties();
      properties.load(new BufferedInputStream(in));
      return properties;
   }

   private void extractConfig(ConfigProperties properties) throws IOException
   {
      int version = properties.getIntProperty("version", 2);
      if (version != 2)
         throw new IOException("Configuration data is an unsupported version (expected 2, was "+version+')');

      conditionsRefreshInMinutes = properties.getIntProperty("conditionsRefreshInMinutes", conditionsRefreshInMinutes);
      forecastsRefreshInMinutes = properties.getIntProperty("forecastsRefreshInMinutes", forecastsRefreshInMinutes);
      String serviceName = properties.getStringProperty("service", service.name());
      service = WeatherServices.valueOf(serviceName);
      serviceConfigData = properties.getMap("servicedata.");

      synchronized (list)
      {
         int entryCount = properties.getIntProperty("entryCount", 0);
         for (int i = 1; i <= entryCount; i++)
         {
            String cpPath = properties.getStringProperty("entry"+i+".cpPath", "");

            StationSource stationSource = new StationSource();
            stationSource.setId(properties.getStringProperty("entry"+i+".station.id", ""));
            stationSource.setName(properties.getStringProperty("entry" + i + ".station.name", ""));
            stationSource.setLatitude(properties.getFloatProperty("entry" + i + ".station.latitude", 0f));
            stationSource.setLongitude(properties.getFloatProperty("entry" + i + ".station.longitude", 0f));

            Map<String, String> map = properties.getMap("entry" + i + ".servicedata.");
            list.add(new WeatherConfigEntry(cpPath, stationSource, map));
         }
      }
   }

   /**
    * Returns the weather service selected for the config data.  Right now the service
    * is fixed, but this method (with some other changes) allows us to support user
    * selectable weather services.
    */
   public WeatherService getWeatherService() throws WeatherServiceException
   {
      WeatherService service = serviceRef.get();
      if (service == null)
      {
         WeatherService newService = new WeatherServiceFactory().getService(this.service);
         serviceRef.compareAndSet(null, newService);
      }
      return serviceRef.get(); // cannot be null now
   }


   public WeatherServices getWeatherServiceEnum() {
       return service;
   }

   /**
    * Returns the configuration data that is managed by the current weather service.  This class
    * takes care of persisting this data, but does not try to interpret it in any way.
    */
   public Map<String, String> getServiceConfigData()
   {
      return serviceConfigData;
   }

   /**
    * Sets the configuration data that is managed by the current weather service.  This class
    * takes care of persisting this data, but does not try to interpret it in any way.
    */
   public void setServiceConfigData(Map<String, String> serviceConfigData)
   {
      this.serviceConfigData = serviceConfigData;
   }

   /**
    * Returns the user configured refresh rate for reading the current conditions from the weather service
    * and (possibly) updating a control program.
    */
   public int getConditionsRefreshInMinutes()
   {
      return conditionsRefreshInMinutes;
   }

   public void setConditionsRefreshInMinutes(int conditionsRefreshInMinutes)
   {
      this.conditionsRefreshInMinutes = Math.max(conditionsRefreshInMinutes, service.getMinConditionsRefresh());
   }

   /**
    * Returns the user configured refresh rate for reading the forecast from the weather service
    * and (possibly) updating a control program
    */
   public int getForecastsRefreshInMinutes()
   {
       return forecastsRefreshInMinutes;
   }

   public void setForecastsRefreshInMinutes(int forecastsRefreshInMinutes)
   {
       this.forecastsRefreshInMinutes = Math.max(forecastsRefreshInMinutes, service.getMinForecastsRefresh());
   }

   public WeatherConfigEntry getEntryForCpPath(String path)
   {
       for (WeatherConfigEntry entry : list) {
           if (entry.getCpPath().equals(path))
           {
               return entry;
           }
       }
       return null;
   }

   public List<WeatherConfigEntry> getList()
   {
      return Collections.unmodifiableList(list);
   }

   public void add(WeatherConfigEntry entry)
   {
      synchronized (list)
      {
         list.add(entry);
      }
   }

   public void delete(int index)
   {
      synchronized (list)
      {
         list.remove(index);
      }
   }

   public void save() throws IOException
   {
      try
      {
         systemConn.runWriteAction("Writing defaults to system datastore", new WriteAction()
         {
            public void execute(WritableSystemAccess systemAccess) throws Exception
            {
               DataStore store = systemAccess.getSystemDataStore(DATASTORE_NAME);
               Properties properties = buildConfig();
               properties.store(store.getOutputStream(), "");
            }
         });
      }
      catch (Exception e)
      {
         throw new IOException("Error writing to data store", e);
      }
   }

   public SystemConnection getSystemConn()
   {
      return systemConn;
   }

   private Properties buildConfig() throws IOException
   {
      Properties props = new Properties();
      ConfigProperties properties = new ConfigProperties(props);

      properties.setIntProperty("version", 2);
      properties.setIntProperty("conditionsRefreshInMinutes", conditionsRefreshInMinutes);
      properties.setIntProperty("forecastsRefreshInMinutes", forecastsRefreshInMinutes);
      properties.setStringProperty("service", service.name());
      properties.setMap(serviceConfigData, "servicedata.");

      synchronized (list)
      {
         properties.setIntProperty("entryCount", list.size());
         int i = 1;
         for (WeatherConfigEntry entry : list)
         {
            properties.setStringProperty("entry" + i + ".cpPath", entry.getCpPath());

            StationSource stationSource = entry.getStationSource();
            properties.setStringProperty("entry"+i+".station.id", stationSource.getId());
            properties.setStringProperty("entry"+i+".station.name", stationSource.getName());
            properties.setFloatProperty("entry" + i + ".station.latitude", stationSource.getLatitude());
            properties.setFloatProperty("entry" + i + ".station.longitude", stationSource.getLongitude());

            properties.setMap(entry.getServiceEntryData(), "entry" + i + ".servicedata.");
            ++i;
         }
      }
      return props;
   }
}