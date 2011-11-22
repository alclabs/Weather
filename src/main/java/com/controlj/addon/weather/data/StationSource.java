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

/**
 * The data source for Station information.  Instances of this are returned by the WeatherService or
 * reconstituted from the configuration data store.
 */
public class StationSource {
    private String id;
    private String name;
    private Float latitude;
    private Float longitude;

    /**
     * Returns the station id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the station id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the station name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the station name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the latitude of the station.
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the station.
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude of the station.
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the station.
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}