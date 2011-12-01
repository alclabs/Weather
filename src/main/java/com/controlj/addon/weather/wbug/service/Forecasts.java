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

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * A set of daily forecasts.
 */
public class Forecasts {

    /** The city the forecast is for. */
    private String city;

    /** The city code. */
    private int cityCode;

    /** The country where the city is located. */
    private String country;

    /** The collection of forecasts (usually seven). */
    private Forecast[] forecasts;

    /** The state where the country is located. */
    private String state;

    /** The WeatherBug forecast site URL. */
    private URL weatherBugSiteURL;

    /** The ZIP code. */
    private int zipCode;

    /** The zone. */
    private String zone;

    /**
     * Constructs a new collection of forecasts.
     * 
     * @param weather
     *            the &lt;aws:weather&gt; XML element.
     */
    public Forecasts(Element weather) {
        Element location = (Element) weather.selectSingleNode("aws:forecasts/aws:location");
        this.city = WeatherBugDataUtils.getString(location, "aws:city");
        this.state = WeatherBugDataUtils.getString(location, "aws:state");
        this.country = WeatherBugDataUtils.getString(location, "aws:country");
        this.zipCode = WeatherBugDataUtils.getInt(location, "aws:zip", -1);
        this.cityCode = WeatherBugDataUtils.getInt(location, "aws:citycode", -1);
        this.zone = WeatherBugDataUtils.getString(location, "aws:zone");
        this.weatherBugSiteURL = WeatherBugDataUtils.getURL(weather, "aws:WebURL");
        List<Forecast> forecastList = WeatherBugDataUtils.bind(weather, "aws:forecasts/aws:forecast", Forecast.class);
        forecasts = forecastList.toArray(new Forecast[forecastList.size()]);
    }

    /**
     * Returns the city the forecast is for.
     * 
     * @return the city the forecast is for.
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
     * Returns the country where the city is located.
     * 
     * @return the country where the city is located.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the collection of forecasts (usually seven).
     * 
     * @return the collection of forecasts.
     */
    public Forecast[] getForecasts() {
        return forecasts;
    }

    /**
     * Returns the state where the city is located.
     * 
     * @return the state where the city is located.
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the WeatherBug forecast site URL.
     * 
     * @return the forecast site URL.
     */
    public URL getWeatherBugSiteURL() {
        return weatherBugSiteURL;
    }

    /**
     * Returns the city ZIP code.
     * 
     * @return the city ZIP code.
     */
    public int getZipCode() {
        return zipCode;
    }

    /**
     * Returns the zone.
     * 
     * @return the zone.
     */
    public String getZone() {
        return zone;
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
