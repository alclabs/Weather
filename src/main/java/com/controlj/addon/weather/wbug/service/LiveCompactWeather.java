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

package com.controlj.addon.weather.wbug.service;

import java.math.BigDecimal;
import java.net.URL;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * The live compact weather for a specific location.
 */
public class LiveCompactWeather {

    /** The city name. */
    private String city;

    /** The city code. */
    private int cityCode;

    /** The country name. */
    private String country;

    /** The current weather condition. */
    private String currentCondition;

    /** The URL of the icon associated with the current condition. */
    private URL currentConditionIconURL;

    /** The cardinal direction of strongest wind gust recently recorded. */
    private String gustDirection;

    /** The speed of strongest wind gust recently recorded. */
    private BigDecimal gustSpeed;

    /** The gust speed unit of measurement. */
    private String gustSpeedUnits;

    /** The latitude. */
    private BigDecimal latitude;

    /** The longitude. */
    private BigDecimal longitude;

    /** The amount of rainfall so far today. */
    private BigDecimal rainToday;

    /** The rainfall unit of measurement. */
    private String rainTodayUnits;

    /** The state name. */
    private String state;

    /** The station identifier. */
    private String stationId;

    /** The station name. */
    private String stationName;

    /** The current temperature. */
    private BigDecimal temperature;

    /** The temperature unit (current) of measurement. */
    private String temperatureUnits;

    /** The current cardinal direction of the wind. */
    private String windDirection;

    /** The current wind speed. */
    private BigDecimal windSpeed;

    /** The wind speed (current) unit of measurement. */
    private String windSpeedUnits;

    /** The ZIP code. */
    private int zipCode;

    /**
     * Constructs a new live compact weather.
     * 
     * @param weather
     *            the &lt;aws:weather&gt; XML element.
     */
    public LiveCompactWeather(Element weather) {
        this.stationId = WeatherBugDataUtils.getString(weather, "aws:station/@id");
        this.stationName = WeatherBugDataUtils.getString(weather, "aws:station/@name");
        this.state = WeatherBugDataUtils.getString(weather, "aws:station/@state");
        this.city = WeatherBugDataUtils.getString(weather, "aws:station/@city");
        this.zipCode = WeatherBugDataUtils.getInt(weather, "aws:station/@zipcode", -1);
        this.cityCode = WeatherBugDataUtils.getInt(weather, "aws:station/@citycode", -1);
        this.country = WeatherBugDataUtils.getString(weather, "aws:station/@country");
        this.latitude = WeatherBugDataUtils.getBigDecimal(weather, "aws:station/@latitude", null);
        this.longitude = WeatherBugDataUtils.getBigDecimal(weather, "aws:station/@longitude", null);
        this.currentCondition = WeatherBugDataUtils.getString(weather, "aws:current-condition");
        this.currentConditionIconURL = WeatherBugDataUtils.getURL(weather, "aws:current-condition/@icon");
        this.temperature = WeatherBugDataUtils.getBigDecimal(weather, "aws:temp", null);
        this.temperatureUnits = WeatherBugDataUtils.getUnits(weather, "aws:temp/@units");
        this.rainToday = WeatherBugDataUtils.getBigDecimal(weather, "aws:rain-today", null);
        this.rainTodayUnits = WeatherBugDataUtils.getUnits(weather, "aws:rain-today/@units");
        this.windSpeed = WeatherBugDataUtils.getBigDecimal(weather, "aws:wind-speed", null);
        this.windSpeedUnits = WeatherBugDataUtils.getUnits(weather, "aws:wind-speed/@units");
        this.windDirection = WeatherBugDataUtils.getString(weather, "aws:wind-direction");
        this.gustSpeed = WeatherBugDataUtils.getBigDecimal(weather, "aws:gust-speed", null);
        this.gustSpeedUnits = WeatherBugDataUtils.getUnits(weather, "aws:gust-speed/@units");
        this.gustDirection = WeatherBugDataUtils.getString(weather, "aws:gust-direction");
    }

    /**
     * Returns the city name.
     * 
     * @return the city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the city code.
     * 
     * @return the city code.
     */
    public int getCityCode() {
        return cityCode;
    }

    /**
     * Returns the country name.
     * 
     * @return the country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the current weather condition.
     * 
     * @return the current weather condition.
     */
    public String getCurrentCondition() {
        return currentCondition;
    }

    /**
     * Returns the URL of the icon associated with the current condition.
     * 
     * @return the URL of the icon associated with the current condition.
     */
    public URL getCurrentConditionIconURL() {
        return currentConditionIconURL;
    }

    /**
     * Returns the cardinal direction of strongest wind gust recently recorded.
     * 
     * @return the cardinal direction of strongest wind gust recently recorded.
     */
    public String getGustDirection() {
        return gustDirection;
    }

    /**
     * Returns the speed of strongest wind gust recently recorded.
     * 
     * @return the speed of strongest wind gust recently recorded.
     */
    public BigDecimal getGustSpeed() {
        return gustSpeed;
    }

    /**
     * Returns the gust speed unit of measurement.
     * 
     * @return the gust speed unit of measurement.
     */
    public String getGustSpeedUnits() {
        return gustSpeedUnits;
    }

    /**
     * Returns the latitude.
     * 
     * @return the latitude.
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude.
     * 
     * @return the longitude.
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Returns the amount of rainfall so far today.
     * 
     * @return the amount of rainfall so far today.
     */
    public BigDecimal getRainToday() {
        return rainToday;
    }

    /**
     * Returns the rainfall unit of measurement.
     * 
     * @return the rainfall unit of measurement.
     */
    public String getRainTodayUnits() {
        return rainTodayUnits;
    }

    /**
     * Returns the city state.
     * 
     * @return the city state.
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the station identifier.
     * 
     * @return the station identifier.
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * Returns the station name.
     * 
     * @return the station name.
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Returns the current temperature.
     * 
     * @return the current temperature.
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * Returns the rate of change in temperature.
     * 
     * @return the rate of change in temperature.
     */
    public String getTemperatureUnits() {
        return temperatureUnits;
    }

    /**
     * Returns the current cardinal direction of the wind.
     * 
     * @return the current cardinal direction of the wind.
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * Returns the current wind speed.
     * 
     * @return the current wind speed.
     */
    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    /**
     * Returns the wind speed (current) unit of measurement.
     * 
     * @return the wind speed (current) unit of measurement.
     */
    public String getWindSpeedUnits() {
        return windSpeedUnits;
    }

    /**
     * Returns the ZIP code.
     * 
     * @return the ZIP code.
     */
    public int getZipCode() {
        return zipCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
