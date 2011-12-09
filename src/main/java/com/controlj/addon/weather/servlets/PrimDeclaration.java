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
import com.controlj.addon.weather.data.*;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.util.Logging;
import com.controlj.green.addonsupport.AddOnInfo;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class PrimDeclaration extends PrimitiveServletBase {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    /*
    This servlet will always be called as a script soruce.  It will actually be called twice on a
    page.  The first call to this will not have any parameters passed.  The response will write the iframe
    and another script block.  This second script block will call the servelet again, but will also pass the
    page's title (which is used to get the location).
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String location = getLocation(request);
        AddOnInfo info = AddOnInfo.getAddOnInfo();
        String addonName = info.getName().toLowerCase();

        String footer = "function updateWeatherData(loc) {\n" +
            "  var dupif = document.getElementById(\"dataupdate\")\n" +
            "  if (dupif) { dupif.src = \"/"+addonName+"/dataupdate?loc=\"+escape(loc) }\n" +
            "}\n" +
            "function updateWeatherValue(id, val) {\n" +
            "var p = PrimitiveBase.findPrimitive(id)\n" +
            "p.setMasterValue(val)\n" +
            "}\n" +
            "updateWeatherData(\"";

        if (location == null) {
            writePrefixScript(out);
        } else {
            try {
                ConfigData config = getConfigData();
                WeatherConfigEntry entry = config.getEntryForCpPath(location);
                if (entry != null) {
                    StringBuilder builder = new StringBuilder();
                    definePrimitives(builder, config, entry);
                    builder.append(footer);
                    builder.append(location);
                    builder.append("\")");
                    out.print(builder.toString());
                    //System.out.println("Declare:");
                    //System.out.println(builder.toString());
                }
            } catch (Throwable th) {
                Logging.println("Error when initially writing primitives", th);
            }
        }

    }

    private void writePrefixScript(PrintWriter out) {
        out.println("document.write(\"<iframe style=\\\"visibility:hidden\\\" width=0 height=0 src=\\\"about:blank\\\" id=\\\"dataupdate\\\" name=\\\"dataupdate\\\"></iframe>\")");
        out.println("document.write(\"<script src=\\\"/weather/data?title=\"+escape(document.title)+\"\\\"></script>\")");
    }

    private static String getLocation(HttpServletRequest request) {
        String locString = request.getParameter("title");
        String result = null;
        if (locString != null) {
            // example: Administrator@161.145.175.61[lvl5] - #weather [Graphics:None:Test Fields:View]
            Pattern pattern = Pattern.compile(".+ - ([^ ]+) \\[.+\\]");
            Matcher matcher = pattern.matcher(locString);
            if (matcher.matches()) {
                try {
                    result = matcher.group(1);
                } catch (Exception e) {} // ignore matching exceptions
            }
        }
        return result;
    }


    private void definePrimitives(final StringBuilder builder, ConfigData config, WeatherConfigEntry entry) throws EquipmentWriteException, WeatherServiceException {
        WeatherLookup weatherLookup = new WeatherLookup(config);
        StationSource stationData = entry.getStationSource();
        ConditionsSource conditionData = weatherLookup.lookupConditionsData(entry, false);
        ForecastSource[] forecastSources = weatherLookup.lookupForecastsData(entry, false);

        try {
            iterateFields(conditionData, stationData, forecastSources, new FieldHandler() {
                //@Override
                public void handleField(FieldType type, String fieldName, Object value) {
                    definePrimitive(builder, type, fieldName, value);
                    builder.append("\n");
                }
            });
        } catch (Throwable th) {
            Logging.println("Error while iterating over fields", th);
        }
    }

    private static void definePrimitive(StringBuilder builder, FieldType type, String fieldName, Object value) {
        String primitiveType = null;

        switch (type) {
            case FloatType:
            case IntegerType:
                primitiveType = "PrimitiveNumber";
                break;

            case StringType:
            case DateType:
                primitiveType = "PrimitiveString";
                break;
        }

        builder.append("new ");
        builder.append(primitiveType);
        builder.append("(\"");
        builder.append(fieldName);
        builder.append("\", false, ");
        if (value == null) {
            builder.append("null");
        } else {
            builder.append("\"");
            builder.append(StringEscapeUtils.escapeJavaScript(formatData(type, value)));
            builder.append("\"");
        }
        builder.append(").setPageLocal(true);");
    }
}
