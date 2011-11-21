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

import com.controlj.addon.weather.service.WeatherServices;
import com.controlj.addon.weather.util.Logging;
import com.controlj.green.addonsupport.AddOnInfo;
import java.io.IOException;

/**
 * Creates a {@link ConfigData} object by reading the configuration data from the datastore.
 * If there is no saved oonfiguration data (or the data cannot be read for some reason), an
 * empty ConfigData is returned.
 */
public class ConfigDataFactory
{
   public static ConfigData loadConfigData()
   {
      ConfigData data = new ConfigData(AddOnInfo.getAddOnInfo().getRootSystemConnection());
      try
      {
         data.load();
      }
      catch (IOException e)
      {
         Logging.println("Error reading configuration data.  Continuing as if no data.", e);
      }
      return data;
   }


   public static ConfigData create(WeatherServices serviceEnum) {
       return new ConfigData(AddOnInfo.getAddOnInfo().getRootSystemConnection(), serviceEnum);
   }
}

