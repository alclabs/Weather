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
package com.controlj.addon.weather.wbug;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.*;
import com.controlj.addon.weather.util.Logging;
import com.controlj.addon.weather.util.ResponseWriter;
import com.controlj.addon.weather.wbug.service.Location;
import com.controlj.addon.weather.wbug.service.Station;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 *
 */
public class WeatherServiceUIImpl extends WeatherServiceUIBase {
    private static final String PARAM_LOCATION = "location";
    private static final String PARAM_FINDCITY = "findcity";
    private static final String PARAM_FINDSTATION = "findstation";

    private static final String FIELD_CITY ="zip";
    private static final String FIELD_STATION = "station_name";
    private static final String FIELD_KEY = "key";
    private static final String FIELD_NAME = "name";


    private final LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
    private List<String> fieldList = new ArrayList<String>();

    public WeatherServiceUIImpl() {
        fields.put(FIELD_CITY, "City");
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
        String zip = entry.getServiceEntryData().get(FIELD_CITY);
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
        String cityKey = req.getParameter("locationlist");
        String stationKey = req.getParameter("stationlist");
        String cityName = req.getParameter("cityname");
        if (path == null || path.length() == 0) {
            writer.addError("Error: Missing location path");
        } else if (cityKey == null || cityKey.length()==0) {
            writer.addError("Error: Missing City.  Please press \"Find City\" to pick a city when adding a location to update");
        } else {
            try
            {
                WeatherServiceImpl ws = (WeatherServiceImpl) configData.getWeatherService();

                StationSource stationSource = ws.resolveConfigurationToStation(
                        isLocationKeyZipCode(cityKey),
                        getLocationKeyCode(cityKey),
                        stationKey);
                String stationName = stationSource.getName();
                Map<String,String> data = new HashMap<String, String>();

                data.put(FIELD_CITY, cityName);
                data.put(FIELD_STATION, stationName);

                configData.add(new WeatherConfigEntry(path, stationSource, data));
            } catch (WeatherServiceException e) {
                writer.addError("Can't get weather service");
                Logging.println("Can't get weather service in noaa.WeatherServiceUIImpl");
            } catch (InvalidConfigurationDataException e) {
                writer.addError("Can't find station");
                Logging.println("Can't find station", e);
            } catch (IllegalStateException e) {
                writer.addError("An entry for " + path + " has already been configured");
                Logging.println("An entry for " + path + " has already been configured", e);
            }
        }
    }

    //@Override
    public void dialogAction(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        String dialogaction = req.getParameter("dialogaction");
        if (PARAM_FINDCITY.equals(dialogaction)) {
            findCityAction(configData, writer, req);
        } else if (PARAM_FINDSTATION.equals(dialogaction)) {
            findStationAction(configData, writer, req);
        }
    }

    private void findCityAction(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        try {
            WeatherServiceImpl ws = (WeatherServiceImpl) configData.getWeatherService();
            String searchString = req.getParameter(PARAM_LOCATION);
            if (searchString == null || searchString.length() == 0) {
                writer.addError("Error: missing City or Zip");
            } else {
                Location[] locations = ws.findLocations(searchString);
                for (Location location : locations) {
                    Map<String,Object> data = new HashMap<String, Object>();
                    data.put(FIELD_KEY, getLocationKey(location));
                    data.put(FIELD_NAME, getLocationDescription(location));
                    writer.appendToArray("list", data);
                }
            }
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
            Logging.println("Error getting weather service:" + e.getMessage(), e);
        }
    }

    private void findStationAction(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        try {
            WeatherServiceImpl ws = (WeatherServiceImpl) configData.getWeatherService();
            String key = req.getParameter(PARAM_LOCATION);
            Station[] stations = ws.findStations(isLocationKeyZipCode(key), getLocationKeyCode(key));

            for (Station station: stations) {
                Map<String,Object> data = new HashMap<String, Object>();
                data.put(FIELD_KEY, station.getId());
                data.put(FIELD_NAME, getStationDescription(station));
                writer.appendToArray("list", data);
            }
        } catch (WeatherServiceException e) {
            writer.addError("Error getting weather service:" + e.getMessage());
            Logging.println("Error getting weather service:" + e.getMessage(), e);
        }
    }

    private String getStationDescription(Station station) {
        StringBuilder result = new StringBuilder();
        result.append(station.getName());
        BigDecimal distance = station.getDistance();
        String distanceUnits = station.getUnit();
        if (distance!= null && distanceUnits!= null) {
            result.append(" (");
            result.append(distance.toPlainString());
            result.append(" ");
            result.append(distanceUnits);
            result.append(")");
        }
        return result.toString();
    }

    private String getLocationDescription(Location location) {
        StringBuilder result = new StringBuilder();
        result.append(location.getCityName());
        result.append(", ");
        if (location.getCityType() == Location.US_CITY_TYPE) {  // in US
            result.append(location.getStateName());
            result.append(" (");
            result.append(location.getZipCode());
            result.append(")");
        } else {
            result.append(location.getCountryName());
        }
        return result.toString();
    }

    private String getLocationKey(Location location) {
        StringBuilder result = new StringBuilder();
        if (location.getCityType() == Location.US_CITY_TYPE) {  // in US
            result.append("Z");
            result.append(location.getZipCode());
        } else {
            result.append("C");
            result.append(location.getCityCode());
        }
        return result.toString();
    }

    private boolean isLocationKeyZipCode(String key) throws WeatherServiceException {
        if (key.length() < 1) {
            throw new WeatherServiceException("Illegal location key '"+key+"'");
        }
        return key.charAt(0) == 'Z';
    }

    private String getLocationKeyCode(String key) throws WeatherServiceException {
        if (key.length() < 2) {
            throw new WeatherServiceException("Illegal location key '"+key+"'");
        }
        return key.substring(1);
    }
}
