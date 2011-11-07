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
package com.controlj.addon.weather.data;

import java.util.Date;

/**
 * The data source for Weather information.  Instances of this are returned by the WeatherService.
 */
public interface ConditionsSource
{
   /**
    * Returns when this source was read from the service.
    *
    * @return when this source was read from the service.
    */
   Date getUpdateTime();

   /**
    * Returns the current temperature.
    *
    * @return the current temperature.
    */
   Float getTemperature();

   /**
    * Returns the current relative humidity.
    *
    * @return the current relative humidity.
    */
   Float getHumidity();

   /**
    * Returns the current barometric pressure.
    *
    * @return the current barometric pressure.
    */
   Float getPressure();

   /**
    * Returns the current weather condition.
    *
    * @return the current weather condition.
    */
   String getCurrentCondition();

   /**
    * Returns the temperature to which the air must be cooled to condense.
    *
    * @return the temperature to which the air must be cooled to condense.
    */
   Float getDewPoint();

   /**
    * Returns the current wind speed.
    *
    * @return the current wind speed.
    */
   Float getWindSpeed();

   /**
    * Returns the current cardinal direction of the wind.
    *
    * @return the current cardinal direction of the wind.
    */
   String getWindDirection();

   /**
    * Returns the current cardinal direction of the wind.
    *
    * @return the current cardinal direction of the wind.
    */
   Integer getWindDegrees();

   /**
    * Returns the observation time.
    *
    * @return the observation time.
    */
   Date getObservationTime();

   /**
    * Returns the enumeration of the icon.
    *
    * @return the enumeration of the icon.
    */
   WeatherIcon getIcon();
}