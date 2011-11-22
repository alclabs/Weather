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

import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.WeatherIcon;
import com.controlj.addon.weather.wbug.service.Forecast;
import com.controlj.addon.weather.wbug.service.Forecasts;

import java.math.BigDecimal;
import java.util.Date;

public class ForecastSourceAdapter extends ForecastSource {
    private final Forecasts forecasts;
    private final Forecast forecast;

    public ForecastSourceAdapter(Forecasts forecasts, Forecast forecast) {
        this.forecasts = forecasts;
        this.forecast = forecast;
    }

    @Override public String getTitle() {
        return forecast.getTitle();
    }

    @Override public Float getHighestTemperature() {
        return toFloatNullSafe(forecast.getHighestTemperature());
    }

    @Override public Float getLowestTemperature() {
        return toFloatNullSafe(forecast.getLowestTemperature());
    }

    @Override public String getPrediction() {
        return forecast.getShortPrediction();
    }

    @Override public String getVerbosePrediction() {
        return forecast.getPrediction();
    }

    @Override public WeatherIcon getIcon() {
        return new WeatherIconMapper().mapIconURL(forecast.getIconName());
    }

    @Override public String getSourceURL() {
        return forecasts.getWeatherBugSiteURL().toExternalForm();
    }

    private Float toFloatNullSafe(BigDecimal value) {
        return value != null ? value.floatValue() : null;
    }
}