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

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * A severe weather alert.
 */
public class Alert {

    /** The expiration time of the alert. */
    private Timestamp expiresTime;

    /** The unique identifier. */
    private String id;

    /** The message summary. */
    private String messageSummary;

    /** The timestamp when the alert has been posted. */
    private Timestamp postedTime;

    /** The title of the alert. */
    private String title;

    /** The type of the alert. */
    private String type;

    /**
     * Constructs a new alert.
     * 
     * @param alert
     *            the &lt;aws:alert&gt; XML element.
     */
    public Alert(Element alert) {
        this.id = WeatherBugDataUtils.getString(alert, "aws:id");
        this.type = WeatherBugDataUtils.getString(alert, "aws:type");
        this.title = WeatherBugDataUtils.getString(alert, "aws:title");
        this.postedTime = WeatherBugDataUtils.getTimestamp(alert, "aws:posted-date");
        this.expiresTime = WeatherBugDataUtils.getTimestamp(alert, "aws:expires-date");
        this.messageSummary = WeatherBugDataUtils.getString(alert, "aws:msg-summary");
    }

    /**
     * Returns the expiration time.
     * 
     * @return the expiration time.
     */
    public Timestamp getExpiresTime() {
        return expiresTime;
    }

    /**
     * Returns the unique identifier
     * 
     * @return the unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the message summary.
     * 
     * @return the message summary
     */
    public String getMessageSummary() {
        return messageSummary;
    }

    /**
     * Returns the timestamp when the alert has been posted.
     * 
     * @return the posting timestamp.
     */
    public Timestamp getPostedTime() {
        return postedTime;
    }

    /**
     * Returns the title of the alert.
     * 
     * @return the title of the alert.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the type of the alert.
     * 
     * @return the type of the alert.
     */
    public String getType() {
        return type;
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
