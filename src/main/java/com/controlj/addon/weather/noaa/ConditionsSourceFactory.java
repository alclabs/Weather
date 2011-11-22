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

import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.data.WeatherIcon;
import org.dom4j.Document;
import org.dom4j.Node;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConditionsSourceFactory {
    private static final SimpleDateFormat rfc8222Format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    ConditionsSource createSourceFromDocument(Document document, boolean isMetric) {
        ConditionsSourceImpl weatherSource = new ConditionsSourceImpl();
        weatherSource.setTemperature(extractFloat(document, isMetric ? "temp_c" : "temp_f"));
        weatherSource.setHumidity(extractFloat(document, "relative_humidity"));
        weatherSource.setPressure(extractFloat(document, isMetric ? "pressure_mb" : "pressure_in"));
        weatherSource.setCurrentCondition(extractString(document, "weather"));
        weatherSource.setDewPoint(extractFloat(document, isMetric ? "dewpoint_c" : "dewpoint_f"));
        weatherSource.setWindSpeed(extractFloat(document, isMetric ? "wind_kt" : "wind_mph"));
        weatherSource.setWindDirection(extractString(document, "wind_dir"));
        weatherSource.setWindDegrees(extractFloat(document, "wind_degrees"));
        weatherSource.setObservationTime(extractObservationTime(document));
        weatherSource.setIcon(mapIcon(extractString(document, "icon_url_name")));
        return weatherSource;
    }

    private String extractString(Document document, String element) {
        Node node = document.selectSingleNode("/current_observation/" + element);
        return node == null ? null : node.getText();
    }

    private Float extractFloat(Document document, String element) {
        Node node = document.selectSingleNode("/current_observation/" + element);
        return node == null ? null : toFloat(node.getText());
    }

    private Integer toInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Float toFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Date extractObservationTime(Document document) {
        Node node = document.selectSingleNode("/current_observation/observation_time_rfc822");
        try {
            return rfc8222Format.parse(node.getText());
        } catch (ParseException e) {
            return null;
        }
    }

    private WeatherIcon mapIcon(String iconName) {
        return new WeatherIconMapper().mapIconURL(iconName);
    }
}