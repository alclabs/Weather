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
import java.util.HashMap;
import java.util.Map;

public class ConditionsSourceAdapter extends ConditionsSource {
    private final LiveWeather liveWeather;
    private final boolean isMetric;

    public ConditionsSourceAdapter(LiveWeather liveWeather, boolean isMetric) {
        this.liveWeather = liveWeather;
        this.isMetric = isMetric;
    }

    @Override protected boolean isMetric() {
        return isMetric;
    }

    @Override public String getAverageWindDirection() {
         return liveWeather.getAvgWindDirection();
    }

    @Override public Float getAverageWindDegrees() {
        return directionToBearing(getAverageWindDirection());
    }

    @Override public Float getAverageWindSpeed() {
        return toFloatNullSafe(liveWeather.getAvgWindSpeed());
    }

    @Override public String getAverageWindSpeedUnits() {
        return liveWeather.getAvgWindSpeedUnits();
    }

    @Override public Float getFeelsLike() {
        return toFloatNullSafe(liveWeather.getFeelsLike());
    }

    @Override public String getFeelsLikeUnits() {
        return liveWeather.getFeelsLikeUnits();
    }

    @Override public Float getTemperature() {
        return toFloatNullSafe(liveWeather.getTemperature());
    }

    @Override public String getTemperatureUnits() {
        return liveWeather.getTemperatureUnits();
    }

    @Override public Float getHumidity() {
        return toFloatNullSafe(liveWeather.getHumidity());
    }

    @Override public String getHumidityUnits() {
        return liveWeather.getHumidityUnits();
    }

    @Override public Float getPressure() {
        return toFloatNullSafe(liveWeather.getPressure());
    }

    @Override public String getPressureUnits() {
        return liveWeather.getPressureUnits();
    }

    @Override public String getCurrentCondition() {
        return liveWeather.getCurrentCondition();
    }

    @Override public Float getDewPoint() {
        return toFloatNullSafe(liveWeather.getDewPoint());
    }

    @Override public String getDewPointUnits() {
        return liveWeather.getDewPointUnits();
    }

    @Override public Float getWindSpeed() {
        return toFloatNullSafe(liveWeather.getWindSpeed());
    }

    @Override public String getWindSpeedUnits() {
        return liveWeather.getWindSpeedUnits();
    }

    @Override public String getWindDirection() {
        return liveWeather.getWindDirection();
    }

    @Override public Float getWindDegrees() {
        return directionToBearing(getWindDirection());
    }

    @Override public Float getRainRate() {
        return toFloatNullSafe(liveWeather.getRainRate());
    }

    @Override public String getRainRateUnits() {
        return liveWeather.getRainRateUnits();
    }

    @Override public Float getRainToday() {
        return toFloatNullSafe(liveWeather.getRainToday());
    }

    @Override public String getRainTodayUnits() {
        return liveWeather.getRainTodayUnits();
    }

    @Override public Float getRainMonth() {
        return toFloatNullSafe(liveWeather.getRainMonth());
    }

    @Override public String getRainMonthUnits() {
        return liveWeather.getRainMonthUnits();
    }

    @Override public Float getRainYear() {
        return toFloatNullSafe(liveWeather.getRainYear());
    }

    @Override public String getRainYearUnits() {
        return liveWeather.getRainYearUnits();
    }

    @Override public Float getLightPercent() {
        return toFloatNullSafe(liveWeather.getLight());
    }

    @Override public Float getWetBulb() {
        return toFloatNullSafe(liveWeather.getWetBulb());
    }

    @Override public String getWetBulbUnits() {
        return liveWeather.getWetBulbUnits();
    }

    @Override public Date getObservationTime() {
        return toDateNullSafe(liveWeather.getObservationTime());
    }

    @Override public WeatherIcon getIcon() {
        URL iconURL = liveWeather.getCurrentConditionIconURL();
        return new WeatherIconMapper().mapIconURL(iconURL.getPath());
    }

    @Override public String getSourceURL() {
        return liveWeather.getWeatherBugSiteURL().toExternalForm();
    }

    private Float toFloatNullSafe(BigDecimal value) {
        return value != null ? value.floatValue() : null;
    }

    private Date toDateNullSafe(Timestamp value) {
        return value != null ? new Date(value.getTime()) : null;
    }

    // this map is used to help convert cardinal directions (N, NE, NNE) to bearings (degrees).  It
    // maps each cardinal direction to a simple integer between 0 and 15, where each integer represents
    // 22.5 degrees of bearing.
    private static final Map<String, Integer> directionToBearingMap = new HashMap<String, Integer>();
    static {
        directionToBearingMap.put("N",   0);
        directionToBearingMap.put("NNE", 1);
        directionToBearingMap.put("NE",  2);
        directionToBearingMap.put("ENE", 3);
        directionToBearingMap.put("E",   4);
        directionToBearingMap.put("ESE", 5);
        directionToBearingMap.put("SE",  6);
        directionToBearingMap.put("SSE", 7);
        directionToBearingMap.put("S",   8);
        directionToBearingMap.put("SSW", 9);
        directionToBearingMap.put("SW",  10);
        directionToBearingMap.put("WSW", 11);
        directionToBearingMap.put("W",   12);
        directionToBearingMap.put("WNW", 13);
        directionToBearingMap.put("NW",  14);
        directionToBearingMap.put("NNW", 15);
    }

    private Float directionToBearing(String direction) {
        Integer bearingInt = directionToBearingMap.get(direction);
        return bearingInt == null ? null : bearingInt * 22.5f;
    }
}