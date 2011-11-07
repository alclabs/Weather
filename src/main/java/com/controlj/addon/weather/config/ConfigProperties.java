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

import java.util.Properties;

/**
 * A helper for loading/saving configuration data that provides support for
 * typed properties with defaults.  This just makes the save/load code in
 * ConfigData easier.
 */
public class ConfigProperties
{
   private final Properties properties;

   public ConfigProperties(Properties properties)
   {
      this.properties = properties;
   }

   public String getStringProperty(String key, String defaultValue)
   {
      return properties.getProperty(key, defaultValue);
   }

   public int getIntProperty(String key, int defaultValue)
   {
      String value = properties.getProperty(key);
      if (value == null)
         return defaultValue;

      try
      {
         return Integer.parseInt(value);
      }
      catch (NumberFormatException e)
      {
         return defaultValue;
      }
   }

   public float getFloatProperty(String key, float defaultValue)
   {
      String value = properties.getProperty(key);
      if (value == null)
         return defaultValue;

      try
      {
         return Float.parseFloat(value);
      }
      catch (NumberFormatException e)
      {
         return defaultValue;
      }
   }

   public boolean getBooleanProperty(String key, boolean defaultValue)
   {
      String value = properties.getProperty(key);
      if (value == null)
         return defaultValue;

      return Boolean.parseBoolean(value);
   }

   public void setStringProperty(String key, String value)
   {
      properties.setProperty(key, value);
   }

   public void setIntProperty(String key, int value)
   {
      properties.setProperty(key, String.valueOf(value));
   }

   public void setFloatProperty(String key, float value)
   {
      properties.setProperty(key, String.valueOf(value));
   }

   public void setBooleanProperty(String key, boolean value)
   {
      properties.setProperty(key, String.valueOf(value));
   }
}

