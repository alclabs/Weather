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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * A city located in U.S or outside of the U.S.
 */
public class Location {

    /** The city type identifying cities outside of the U.S. */
    public static final int NON_US_CITY_TYPE = 1;

    /** The city type identifying U.S. cities. */
    public static final int US_CITY_TYPE = 0;

    /** The 5 digits designation for non-U.S. cities. */
    private int cityCode;

    /** The city name. */
    private String cityName;

    /**
     * The city type: <code>US_CITY_TYPE</code> for city in the U.S. and <code>NON_US_CITY_TYPE</code> for city outside of the U.S.
     * 
     * @see #US_CITY_TYPE
     * @see #NON_US_CITY_TYPE
     */
    private int cityType;

    /** The country name. */
    private String countryName;

    /** The state name. */
    private String stateName;

    /** The 5 digits designation for U.S. cities only. */
    private int zipCode;

    /**
     * Constructs a new location.
     * 
     * @param location
     *            the &lt;aws:location&gt; XML element.
     */
    public Location(Element location) {
        this.cityName = WeatherBugDataUtils.getString(location, "@cityname");
        this.stateName = WeatherBugDataUtils.getString(location, "@statename");
        this.countryName = WeatherBugDataUtils.getString(location, "@countryname");
        this.zipCode = WeatherBugDataUtils.getInt(location, "@zipcode", -1);
        this.cityCode = WeatherBugDataUtils.getInt(location, "@citycode", -1);
        this.cityType = WeatherBugDataUtils.getInt(location, "@citytype", -1);
    }

    /**
     * Constructs a new location.
     * 
     * @param cityName
     *            the city name.
     * @param stateName
     *            the state name.
     * @param countryName
     *            the country name.
     * @param zipCode
     *            the ZIP code.
     * @param cityCode
     *            the city code.
     * @param cityType
     *            the city type.
     */
    public Location(String cityName, String stateName, String countryName, int zipCode, int cityCode, int cityType) {
        this.cityName = cityName;
        this.stateName = stateName;
        this.countryName = countryName;
        this.zipCode = zipCode;
        this.cityCode = cityCode;
        this.cityType = cityType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) {
            return false;
        }
        Location loc = (Location) obj;
        if (getCityType() == US_CITY_TYPE) {
            return getZipCode() == loc.getZipCode();
        } else {
            return getCityCode() == loc.getCityCode();
        }
    }

    /**
     * Returns the 5 digits designation for non-U.S. cities.
     * 
     * @return the city code.
     */
    public int getCityCode() {
        return cityCode;
    }

    /**
     * Returns the city name.
     * 
     * @return the city name.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Returns the city type: <code>US_CITY_TYPE</code> for city in the U.S. and <code>NON_US_CITY_TYPE</code> for city outside of
     * the U.S.
     * 
     * @see #US_CITY_TYPE
     * @see #NON_US_CITY_TYPE
     * 
     * @return the city type.
     */
    public int getCityType() {
        return cityType;
    }

    /**
     * Returns the country name.
     * 
     * @return the country name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Returns the state name.
     * 
     * @return the state name.
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Returns the zip code.
     * 
     * @return the zip code.
     */
    public int getZipCode() {
        return zipCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (getCityType() == US_CITY_TYPE) ? getZipCode() : getCityCode();
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
