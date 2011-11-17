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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * A weather station.
 */
public class Station {

    /** The city where the station is located. */
    private String city;

    /** The 5 digits designation for non-U.S. cities. */
    private int cityCode;

    /** The country where the station is located. */
    private String country;

    /** The distance. */
    private BigDecimal distance;

    /** The unique station identifier. */
    private String id;

    /** The latitude. */
    private BigDecimal latitude;

    /** The longitude. */
    private BigDecimal longitude;

    /** The station name. */
    private String name;

    /** The state where the station is located. */
    private String state;

    /** The distance unit. */
    private String unit;

    /** The 5 digits designation for U.S. cities only. */
    private int zipCode;

    /**
     * Constructs a new station.
     * 
     * @param location
     *            the &lt;aws:location&gt; XML element.
     */
    public Station(Element location) {
        this.id = WeatherBugDataUtils.getString(location, "@id");
        this.name = WeatherBugDataUtils.getString(location, "@name");
        this.city = WeatherBugDataUtils.getString(location, "@city");
        this.state = WeatherBugDataUtils.getString(location, "@state");
        this.country = WeatherBugDataUtils.getString(location, "@country");
        this.zipCode = WeatherBugDataUtils.getInt(location, "@zipcode", -1);
        this.cityCode = WeatherBugDataUtils.getInt(location, "@citycode", -1);
        this.distance = WeatherBugDataUtils.getBigDecimal(location, "@distance", null);
        this.unit = WeatherBugDataUtils.getString(location, "@Unit");
        this.latitude = WeatherBugDataUtils.getBigDecimal(location, "@latitude", null);
        this.longitude = WeatherBugDataUtils.getBigDecimal(location, "@longitude", null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Station)) {
            return false;
        }
        return getId().equals(((Station) obj).getId());
    }

    /**
     * Returns the city where the station is located.
     * 
     * @return the city where the station is located.
     */
    public String getCity() {
        return city;
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
     * Returns the country where the station is located.
     * 
     * @return the country where the station is located.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the distance.
     * 
     * @return the distance.
     */
    public BigDecimal getDistance() {
        return distance;
    }

    /**
     * Returns the station identifier.
     * 
     * @return the station identifier.
     */
    public String getId() {
        return id;
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
     * Returns the station name.
     * 
     * @return the station name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the state where the station is located.
     * 
     * @return the state where the station is located.
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the distance unit.
     * 
     * @return the distance unit.
     */
    public String getUnit() {
        return unit;
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
        return getId().hashCode();
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
