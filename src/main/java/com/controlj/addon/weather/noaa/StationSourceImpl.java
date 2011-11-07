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

package com.controlj.addon.weather.noaa;

import org.dom4j.Node;
import org.dom4j.XPath;

/**
 *
 */
public class StationSourceImpl {
    private static  XPath latXpath;
    private static XPath longXpath;
    private static XPath idXpath;
    private static XPath nameXpath;

    private Node stationNode;

    public StationSourceImpl(Node stationNode) {
        this.stationNode = stationNode;
        initXPath();
    }

    public Float getLatitude() {
        return getFloatValue(latXpath);
    }

    public Float getLongitude() {
        return getFloatValue(longXpath);
    }

    public String getName() {
        return getStringValue(nameXpath);
    }

    public String getId() {
        return getStringValue(idXpath);
    }

    public float getDistanceTo(float latitude, float longitude, boolean isMetric) {
        return DistanceUtility.greatCircleDistanceDegrees(isMetric ? DistanceUtility.KILOMETERS : DistanceUtility.MILES, latitude, longitude, getLatitude().floatValue(), getLongitude().floatValue());
    }

    private void initXPath() {
        if (latXpath == null) {
            latXpath = stationNode.createXPath("latitude");
            longXpath = stationNode.createXPath("longitude");
            idXpath = stationNode.createXPath("station_id");
            nameXpath = stationNode.createXPath("station_name");
        }
    }

    private Float getFloatValue(XPath xpath) {
        Float result = null;
        Node node = xpath.selectSingleNode(stationNode);
        if (node != null) {
            try {
                result = new Float(node.getText());
            } catch (NumberFormatException e) { } // ignore and return null
        }
        return result;
    }

    private String getStringValue(XPath xpath) {
        String result = null;
        Node node = xpath.selectSingleNode(stationNode);
        if (node != null) {
            result = node.getText();
        }
        return result;
    }
}