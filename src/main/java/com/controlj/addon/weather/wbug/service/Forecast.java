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

/** A local forecast. */
public class Forecast {

    /** The forecast description. */
    private String description;

    /** The expected highest temperature. */
    private BigDecimal highestTemperature;

    /** The units of the expected highest temperature. */
    private String highestTemperatureUnits;

    /** The name of the forecast icon. */
    private String iconName;

    /** The absolute URL of the remote forecast icon. */
    private URL imageURL;

    /** Indicates whether the image represents either night or day. */
    private boolean isNightImage;

    /** The expected lowest temperature. */
    private BigDecimal lowestTemperature;

    /** The units of the expected lowest temperature. */
    private String lowestTemperatureUnits;

    /** The full predication text. */
    private String prediction;

    /** The short prediction text. */
    private String shortPrediction;

    /** The title (the name of a day). */
    private String title;

    /**
     * Constructs a new forecast.
     * 
     * @param forecast
     *            the &lt;aws:weather&gt; XML element.
     */
    public Forecast(Element forecast) {
        this.title = WeatherBugDataUtils.getString(forecast, "aws:title");
        this.shortPrediction = WeatherBugDataUtils.getString(forecast, "aws:short-prediction");
        this.isNightImage = (WeatherBugDataUtils.getInt(forecast, "aws:image/@isNight", 0) != 0);
        this.iconName = WeatherBugDataUtils.getString(forecast, "aws:image/@icon");
        this.imageURL = WeatherBugDataUtils.getURL(forecast, "aws:image");
        this.description = WeatherBugDataUtils.getString(forecast, "aws:description");
        this.prediction = WeatherBugDataUtils.fixDegrees(WeatherBugDataUtils.getString(forecast, "aws:prediction"));
        this.highestTemperature = WeatherBugDataUtils.getBigDecimal(forecast, "aws:high", null);
        this.highestTemperatureUnits = WeatherBugDataUtils.getUnits(forecast, "aws:high/@units");
        this.lowestTemperature = WeatherBugDataUtils.getBigDecimal(forecast, "aws:low", null);
        this.lowestTemperatureUnits = WeatherBugDataUtils.getUnits(forecast, "aws:low/@units");
    }

    /**
     * Returns the description.
     * 
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the expected highest temperature.
     * 
     * @return the expected highest temperature.
     */
    public BigDecimal getHighestTemperature() {
        return highestTemperature;
    }

    /**
     * Returns the units of the expected highest temperature.
     * 
     * @return the units of the expected highest temperature.
     */
    public String getHighestTemperatureUnits() {
        return highestTemperatureUnits;
    }

    /**
     * Returns the name of the forecast icon.
     * 
     * @return the name of the forecast icon.
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * Returns the absolute URL of the forecast icon.
     * 
     * @return the absolute URL of the forecast icon.
     */
    public URL getImageURL() {
        return imageURL;
    }

    /**
     * Returns the expected lowest temperature.
     * 
     * @return the expected lowest temperature.
     */
    public BigDecimal getLowestTemperature() {
        return lowestTemperature;
    }

    /**
     * Returns the units of the expected lowest temperature.
     * 
     * @return the units of the expected lowest temperature.
     */
    public String getLowestTemperatureUnits() {
        return lowestTemperatureUnits;
    }

    /**
     * Returns the full prediction text.
     * 
     * @return the full prediction text.
     */
    public String getPrediction() {
        return prediction;
    }

    /**
     * Returns the short prediction text.
     * 
     * @return the short prediction text.
     */
    public String getShortPrediction() {
        return shortPrediction;
    }

    /**
     * Returns the title (the name of a day).
     * 
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Indicates whether the image represents either night or day.
     * 
     * @return <code>true</code> if the forecast icon represents night.
     */
    public boolean isNightImage() {
        return isNightImage;
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
