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

package com.controlj.addon.weather.servlets;

import com.controlj.addon.weather.EquipmentWriteException;
import com.controlj.addon.weather.WeatherLookup;
import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.data.FieldType;
import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.util.Logging;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 */
public class PrimUpdate extends PrimitiveServletBase {
    private static final String footer1 = "window.parent.setTimeout(\"updateWeatherData(\\\"";
    private static final String footer2 = "\\\")\", 60000)\n" +
                    "</script>";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String location = request.getParameter("loc");
        if (location != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("<script>");
            ConfigData config = getConfigData();
            WeatherConfigEntry entry = config.getEntryForCpPath(location);
            if (entry != null) {
                try {
                    updatePrimitives(builder, config, entry);
                }  catch (Throwable th) {
                    Logging.println("Error when initially writing primitives", th);
                }
            }
            builder.append(footer1);
            builder.append(location);
            builder.append(footer2);
            out.print(builder.toString());
            //System.out.println("Update:");
            //System.out.println(builder.toString());
        } else {
            Logging.println("Primitive update called without location");
        }
    }

    private void updatePrimitives(final StringBuilder builder, ConfigData config, WeatherConfigEntry entry) throws EquipmentWriteException, WeatherServiceException {
        WeatherLookup weatherLookup = new WeatherLookup(config);
        StationSource stationData = entry.getStationSource();
        ConditionsSource conditionData = weatherLookup.lookupConditionsData(entry, false);
        ForecastSource[] forecastSources = weatherLookup.lookupForecastsData(entry, false);

        try {
            iterateFields(conditionData, stationData, forecastSources, new FieldHandler() {
                //@Override
                public void handleField(FieldType type, String fieldName, Object value) {
                    updatePrimitive(builder, type, fieldName, value);
                    builder.append("\n");
                }
            });
        } catch (Throwable th) {
            Logging.println("Error while iterating over fields", th);
        }
    }

    private void updatePrimitive(StringBuilder builder, FieldType type, String fieldName, Object value) {
        builder.append("window.parent.updateWeatherValue(\"");
        builder.append(fieldName);
        builder.append("\", ");
        if (value != null) {
            builder.append("\"");
            builder.append(StringEscapeUtils.escapeJavaScript(formatData(type, value)));
            builder.append("\"");
        } else {
            builder.append("null");
        }
        builder.append(")");
    }
}
