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
import com.controlj.addon.weather.data.WeatherIcon;
import com.controlj.addon.weather.wbug.service.LiveWeather;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

public class ConditionsSourceAdapter implements ConditionsSource
{
   private final LiveWeather liveWeather;
   private final Date updateTime;

   public ConditionsSourceAdapter(LiveWeather liveWeather)
   {
      this.liveWeather = liveWeather;
      updateTime = new Date();
   }

   @Override public Date getUpdateTime()
   {
      return updateTime;
   }

   @Override public Float getTemperature()
   {
      return toFloatNullSafe(liveWeather.getTemperature());
   }

   @Override public Float getHumidity()
   {
      return toFloatNullSafe(liveWeather.getHumidity());
   }

   @Override public Float getPressure()
   {
      return toFloatNullSafe(liveWeather.getPressure());
   }

   @Override public String getCurrentCondition()
   {
      return liveWeather.getCurrentCondition();
   }

   @Override public Float getDewPoint()
   {
      return toFloatNullSafe(liveWeather.getDewPoint());
   }

   @Override public Float getWindSpeed()
   {
      return toFloatNullSafe(liveWeather.getWindSpeed());
   }

   @Override public String getWindDirection()
   {
      return liveWeather.getWindDirection();
   }

   @Override public Integer getWindDegrees()
   {
      throw new UnsupportedOperationException();
   }

   @Override public Date getObservationTime()
   {
      return toDateNullSafe(liveWeather.getObservationTime());
   }

   @Override public WeatherIcon getIcon()
   {
      URL iconURL = liveWeather.getCurrentConditionIconURL();
      return new WeatherIconMapper().mapIconURL(iconURL.getPath());
   }

   private Float toFloatNullSafe(BigDecimal value)
   {
      return value != null ? value.floatValue() : null;
   }

   private Date toDateNullSafe(Timestamp value)
   {
      return value != null ? new Date(value.getTime()) : null;
   }
}

