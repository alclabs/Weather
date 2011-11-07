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

package com.controlj.addon.weather.noaa;

import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.WeatherIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 *
 */
public class ForecastSourceImpl implements ForecastSource {
    private final Date updateTime;
    private final String day;
    private final Float high, low, probPrecip;
    private final String weatherDescription;
    private final WeatherIcon icon;

    public ForecastSourceImpl(@Nullable Float high, @Nullable Float low, @Nullable Float probPrecip, @NotNull String day, @Nullable String weatherDescription, WeatherIcon icon) {
        this.updateTime = new Date();
        this.high = high;
        this.low = low;
        this.probPrecip = probPrecip;
        this.day = day;
        this.weatherDescription = weatherDescription;
        this.icon = icon;
    }

    @Override public Date getUpdateTime() {
       return updateTime;
    }

    @Override public String getTitle() {
        return day;
    }

    @Override @Nullable
    public Float getHighestTemperature() {
        return high;
    }

    @Override @Nullable
    public Float getLowestTemperature() {
        return low;
    }

    @Override @Nullable
    public String getPrediction() {
        return weatherDescription;
    }

    @Override
    public Float getProbPrecipitation() {
        return probPrecip;
    }

   @Override public WeatherIcon getIcon()
   {
      return icon;
   }
}