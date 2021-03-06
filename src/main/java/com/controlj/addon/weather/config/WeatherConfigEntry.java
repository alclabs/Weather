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

import com.controlj.addon.weather.RuntimeInformation;
import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.servlets.PrimitiveServletBase;

import java.util.Collections;
import java.util.Map;

/**
 * A control program/zip code entry in the configuration data.  Each entry also stores whether
 * it should use metric values and the "config key" that is determined by the WeatherService when
 * this entry is created.
 */
public class WeatherConfigEntry
{
   private final String cpPath;
   private final StationSource stationSource;
   private final Map<String, String> serviceEntryData;

   public WeatherConfigEntry(String cpPath, StationSource stationSource, Map<String, String> serviceEntryData)
   {
      this.cpPath = cpPath.trim();
      this.stationSource = stationSource;
      this.serviceEntryData = Collections.unmodifiableMap(serviceEntryData);
   }

   public String getCpPath()
   {
      return cpPath;
   }

   public Map<String, String> getServiceEntryData()
   {
      return serviceEntryData;
   }

   public StationSource getStationSource()
   {
      return stationSource;
   }

   public String getLastUpdate() // doesn't belong here, but I don't know how else to get it on the page.  Remove when we switch to using AJAX
   {
      RuntimeInformation rti = RuntimeInformation.getSingleton();
      String error = rti.getLastConditionsError(this);
      if (error != null)
         return error;

      ConditionsSource lastConditionsData = rti.getLastConditionsData(this);
      if (lastConditionsData != null)
         return PrimitiveServletBase.timeFormat.format(lastConditionsData.getUpdateTime());

      return "";
   }

   @Override public String toString()
   {
      return "WeatherConfigEntry{" +
            "cpPath='" + cpPath + '\'' +
            ", stationId='" + stationSource.getId() + '\'' +
            ", serviceConfigData=" + serviceEntryData +
            '}';
   }

   @Override public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      WeatherConfigEntry that = (WeatherConfigEntry) o;

      return !(cpPath != null ? !cpPath.equals(that.cpPath) : that.cpPath != null);
   }

   @Override public int hashCode()
   {
      return cpPath != null ? cpPath.hashCode() : 0;
   }
}