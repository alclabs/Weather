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

/**
 * An enumeration of all available weather services.  This allows the UI to enumeration them and let
 * the user select one.
 */
public enum WeatherServices
{
   nws("National Weather Service", 15, 60),
   wbug("WeatherBug.com", 15, 60);

   private final String displayName;
   private final int minConditionsRefresh;
   private final int minForecastsRefresh;

   WeatherServices(String displayName, int minConditionsRefresh, int minForecastsRefresh)
   {
      this.displayName = displayName;
      this.minConditionsRefresh = minConditionsRefresh;
      this.minForecastsRefresh = minForecastsRefresh;
   }

   public String getDisplayName()
   {
      return displayName;
   }

   public int getMinConditionsRefresh()
   {
      return minConditionsRefresh;
   }

   public int getMinForecastsRefresh()
   {
      return minForecastsRefresh;
   }
}

