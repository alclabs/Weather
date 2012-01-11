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
public abstract class ConditionsSource {
    private final Date updateTime;

    protected ConditionsSource() {
        updateTime = new Date();
    }

    protected abstract boolean isMetric();

    /**
     * Returns when this source was read from the service.
     */
    public final Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Returns the average cardinal direction of the wind so far today.
     */
    public String getAverageWindDirection() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the average direction of the wind so far today.
     */
    public Float getAverageWindDegrees() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the average direction of the wind so far today.
     */
    public String getAverageWindDegreesUnits() {
        return "\u00B0";
    }

    /**
     * Returns the average speed of the wind so far today.
     */
    public Float getAverageWindSpeed() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the average speed of the wind so far today.
     */
    public String getAverageWindSpeedUnits() {
        return "";
    }

    /**
     * Returns the wind chill or heat index temperature, whichever is relevant.
     */
    public Float getFeelsLike() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the wind chill or heat index temperature, whichever is relevant.
     */
    public String getFeelsLikeUnits() {
        return "";
    }

    /**
     * Returns the current temperature.
     */
    public Float getTemperature() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current temperature.
     */
    public String getTemperatureUnits() {
        return "";
    }

    /**
     * Returns the current temperature in Celsius, regardless of the normal
     * units.
     */
    public final Float getTemperatureInCelsius() {
        Float temperature = getTemperature();
        if (temperature == null || isMetric())
            return temperature;

        // temperature is in Fahrenheit, so convert to celsius
        return (temperature - 32f) * 5f / 9f;
    }

    public String getTemperatureInCelsiusUnits() {
        return "\u00B0C";
    }

    /**
     * Returns the current relative humidity.
     */
    public Float getHumidity() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current relative humidity.
     */
    public String getHumidityUnits() {
        return "";
    }

    /**
     * Returns the current barometric pressure.
     */
    public Float getPressure() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current barometric pressure.
     */
    public String getPressureUnits() {
        return "";
    }

    /**
     * Returns the current weather condition.
     */
    public String getCurrentCondition() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the temperature to which the air must be cooled to condense.
     */
    public Float getDewPoint() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the temperature to which the air must be cooled to condense.
     */
    public String getDewPointUnits() {
        return getTemperatureUnits();
    }

    /**
     * Returns the current wind speed.
     */
    public Float getWindSpeed() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current wind speed.
     */
    public String getWindSpeedUnits() {
        return "";
    }

    /**
     * Returns the current cardinal direction of the wind.
     */
    public String getWindDirection() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current cardinal direction of the wind.
     */
    public Float getWindDegrees() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current cardinal direction of the wind.
     */
    public String getWindDegreesUnits() {
        return "\u00B0";
    }

    /**
     * Returns the current rate at which rain is falling.
     */
    public Float getRainRate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current rate at which rain is falling.
     */
    public String getRainRateUnits() {
        return "";
    }

    /**
     * Returns the amount of rainfall so far today.
     */
    public Float getRainToday() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the amount of rainfall so far today.
     */
    public String getRainTodayUnits() {
        return "";
    }

    /**
     * Returns the amount of rainfall so far this month.
     */
    public Float getRainMonth() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the amount of rainfall so far this month.
     */
    public String getRainMonthUnits() {
        return "";
    }

    /**
     * Returns the amount of rainfall so far this year.
     */
    public Float getRainYear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the amount of rainfall so far this year.
     */
    public String getRainYearUnits() {
        return "";
    }

    /**
     * Returns the relative percentage of daylight.
     */
    public Float getLightPercent() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the relative percentage of daylight.
     */
    public String getLightPercentUnits() {
        return "%";
    }

    /**
     * Returns the temperature at which no evaporation occurs and temperature stops dropping.
     */
    public Float getWetBulb() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the temperature at which no evaporation occurs and temperature stops dropping.
     */
    public String getWetBulbUnits() {
        return getTemperatureUnits();
    }

    /**
     * Returns the observation time.
     */
    public Date getObservationTime() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the enumeration of the icon.
     */
    public WeatherIcon getIcon() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the weather services URL for this data source.
     */
    public String getSourceURL() {
        throw new UnsupportedOperationException();
    }
}