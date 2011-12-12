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

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.InvalidConfigurationDataException;
import com.controlj.addon.weather.service.WeatherService;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceUIBase;
import com.controlj.addon.weather.util.Logging;
import com.controlj.addon.weather.util.ResponseWriter;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 *
 */
public class WeatherServiceUIImpl extends WeatherServiceUIBase {
    private static final String FIELD_ZIP ="zip";
    private static final String FIELD_STATION = "station_name";

    private final LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
    private List<String> fieldList = new ArrayList<String>();

    public WeatherServiceUIImpl() {
        fields.put(FIELD_ZIP, "Zip Code");
        fields.put(FIELD_STATION, "Station Name");
        fieldList = new ArrayList<String>(fields.keySet());
    }

    //@Override @NotNull
    public List<String> getServiceEntryFields() {
        return fieldList;
    }

    //@Override @NotNull
    public String getServiceEntryHeaderName(String fieldName) {
        String result = fields.get(fieldName);
        if (result == null) {
            throw new IllegalArgumentException("Unknown field '"+fieldName+"'");
        }
        return result;
    }

    //@Override
    public String getAddDialogHTML() {
        StringWriter result = new StringWriter();
        copyHTMLTemplate(WeatherServiceUIImpl.class, "adddialog.html", result);
        return result.getBuffer().toString();
    }

    //@Override
    public String getServiceConfigHTML() {
        StringWriter result = new StringWriter();
        result.append(super.getServiceConfigHTML());
        copyHTMLTemplate(WeatherServiceUIImpl.class, "serviceconfig.html", result);
        return result.getBuffer().toString();
    }

    //@Override
    public String getEntryDisplayName(WeatherConfigEntry entry) {
        String zip = entry.getServiceEntryData().get(FIELD_ZIP);
        return zip;
    }

    //@Override
    public void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        // super class handles the refresh rates
        super.updateConfiguration(configData, writer, req);

        updateConfigField(WeatherServiceImpl.CONFIG_KEY_UNITS, configData, writer, req);
    }

    //@Override
    public void addRow(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        String path = req.getParameter("path");
        String zip = req.getParameter(FIELD_ZIP);

        if (path == null || path.length() == 0) {
            writer.addError("Error: Missing location path");
        } else if (zip == null || zip.length() == 0) {
            writer.addError("Error: Missing zip code");
        } else {
            try
            {
                WeatherServiceImpl ws = (WeatherServiceImpl) configData.getWeatherService();
                StationSource stationSource = ws.resolveConfigurationToStation(zip);
                String stationName = stationSource.getName();
                Map<String,String> data = new HashMap<String, String>();
                data.put(FIELD_ZIP, zip);
                data.put(FIELD_STATION, stationName);

                configData.add(new WeatherConfigEntry(path, stationSource, data));
            } catch (WeatherServiceException e) {
                writer.addError("Can't get weather service");
                Logging.println("Can't get weather service in noaa.WeatherServiceUIImpl");
            } catch (InvalidConfigurationDataException e) {
                writer.addError("Can't find station for zip code '"+zip+"'");
                Logging.println("Can't find station for zip code '"+zip+"'", e);
            } catch (IllegalStateException e) {
                writer.addError("An entry for " + path + " has already been configured");
                Logging.println("An entry for " + path + " has already been configured", e);
            }
        }

    }
}
