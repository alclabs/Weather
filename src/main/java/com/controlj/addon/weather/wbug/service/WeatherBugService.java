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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import com.controlj.addon.weather.util.HTTPHelper;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;

/**
 * The WeatherBug service.
 */
public class WeatherBugService {

    /** HTTP connection default time-out (in milliseconds). */
    public static final int DEFAULT_TIMEOUT = 20000;

    /**
     * The alphanumeric license key issued by WeatherBug.
     */
    private String aCode;

    /** The current time-out (default = DEFAULT_TIMEOUT). */
    private int timeout = DEFAULT_TIMEOUT;

    /* static initializer */
    static {
        Map nsURIs = DocumentFactory.getInstance().getXPathNamespaceURIs();
        if (nsURIs == null) {
            nsURIs = new HashMap();
        }
        nsURIs.put("aws", "http://www.aws.com/aws");
        DocumentFactory.getInstance().setXPathNamespaceURIs(nsURIs);
    }

    /**
     * Constructs a new WeatherBug service.
     * 
     * @param aCode
     *            the alphanumeric license key issued by WeatherBug.
     */
    public WeatherBugService(String aCode) {
        this.aCode = aCode;
    }

    /**
     * Sets the timeout.
     * 
     * @param timeout
     *            the timeout.
     * @see #DEFAULT_TIMEOUT
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns the set of locations matching a specific name. The soundex system allows searching for a city based on phonetics rather
     * than spelling.
     * 
     * @param searchString
     *            the search string.
     * @return the set of matching locations.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Location[] getLocationList(String searchString) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("SearchString", searchString);
        Document doc = execute("getLocationsXML", params);
        List locations = WeatherBugDataUtils.bind(doc, "aws:locations/aws:location", Location.class);
        return (Location[]) locations.toArray(new Location[0]);
    }

    /**
     * For a given ZIP code returns the weather stations in the area.
     * 
     * @param zipCode
     *            the ZIP code to match.
     * @return the set of weather stations in the area.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Station[] getStationListByUSZipCode(String zipCode) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("zipCode", zipCode);
        Document doc = execute("getStationsXML", params);
        List stations = WeatherBugDataUtils.bind(doc, "aws:stations/aws:station", Station.class);
        return (Station[]) stations.toArray(new Station[0]);
    }

    /**
     * For a given city code returns the weather stations in the area.
     * 
     * @param cityCode
     *            the city code to match.
     * @return the set of weather stations in the area.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Station[] getStationListByCityCode(int cityCode) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("cityCode", Integer.toString(cityCode));
        Document doc = execute("getStationsXML", params);
        List stations = WeatherBugDataUtils.bind(doc, "aws:stations/aws:station", Station.class);
        return (Station[]) stations.toArray(new Station[0]);
    }

    /**
     * For a given latitude and longitude returns the weather stations in the area.
     * 
     * @param latitude
     *            the latitude.
     * @param longitude
     *            the longitude.
     * @return the set of weather stations in the area.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Station[] getStationListByLatLong(double latitude, double longitude) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("lat", WeatherBugDataUtils.formatNumber(latitude, "0.00"));
        params.put("long", WeatherBugDataUtils.formatNumber(longitude, "0.00"));
        Document doc = execute("getStationsXML", params);
        List stations = WeatherBugDataUtils.bind(doc, "/aws:weather/aws:stations/aws:station", Station.class);
        return (Station[]) stations.toArray(new Station[0]);
    }

    /**
     * Gets the live weather based on a station.
     * 
     * @param stationId
     *            the station identifier.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveWeather getLiveWeatherByStationID(String stationId, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("stationid", stationId);
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveWeatherRSS", params);
        return (LiveWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveWeather.class);

    }

    /**
     * Gets the live weather based on a U.S. ZIP code.
     * 
     * @param zipCode
     *            the ZIP code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveWeather getLiveWeatherByUSZipCode(String zipCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("zipCode", zipCode);
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveWeatherRSS", params);
        return (LiveWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveWeather.class);
    }

    /**
     * Gets the live weather for a city located outside of the U.S.
     * 
     * @param cityCode
     *            the city code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveWeather getLiveWeatherByCityCode(int cityCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("cityCode", Integer.toString(cityCode));
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveWeatherRSS", params);
        return (LiveWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveWeather.class);
    }

    /**
     * Gets the live compact weather based on a station.
     * 
     * @param stationId
     *            the station identifier.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live compact weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveCompactWeather getLiveCompactWeatherByStationID(String stationId, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("stationid", stationId);
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveCompactWeatherRSS", params);
        return (LiveCompactWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveCompactWeather.class);
    }

    /**
     * Gets the live compact weather based on a U.S. ZIP code.
     * 
     * @param zipCode
     *            the ZIP code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live compact weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveCompactWeather getLiveCompactWeatherByUSZipCode(String zipCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("zipCode", zipCode);
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveCompactWeatherRSS", params);
        return (LiveCompactWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveCompactWeather.class);
    }

    /**
     * Gets the live compact weather for a city located outside of the U.S.
     * 
     * @param cityCode
     *            the city code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live compact weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveCompactWeather getLiveCompactWeatherByCityCode(int cityCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("cityCode", Integer.toString(cityCode));
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveCompactWeatherRSS", params);
        return (LiveCompactWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveCompactWeather.class);
    }

    /**
     * For a given latitude and longitude returns the live compact weather.
     * 
     * @param latitude
     *            the latitude.
     * @param longitude
     *            the longitude.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the live compact weather.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public LiveCompactWeather getLiveCompactWeatherByLatLong(double latitude, double longitude, int unitType)
            throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("lat", WeatherBugDataUtils.formatNumber(latitude, "0.00"));
        params.put("long", WeatherBugDataUtils.formatNumber(longitude, "0.00"));
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getLiveCompactWeatherRSS", params);
        return (LiveCompactWeather) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", LiveCompactWeather.class);
    }

    /**
     * Gets the forecast based on a U.S. ZIP code.
     * 
     * @param zipCode
     *            the ZIP code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the forecast.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Forecasts getForecastByUSZipCode(String zipCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("zipCode", zipCode);
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getForecastRSS", params);
        return (Forecasts) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", Forecasts.class);
    }

    /**
     * Gets the forecast for a city located outside of the U.S.
     * 
     * @param cityCode
     *            the city code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the forecast.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Forecasts getForecastByCityCode(int cityCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("cityCode", Integer.toString(cityCode));
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getForecastRSS", params);
        return (Forecasts) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", Forecasts.class);
    }

    /**
     * For a given latitude and longitude returns the forecast.
     * 
     * @param latitude
     *            the latitude.
     * @param longitude
     *            the longitude.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the forecast.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Forecasts getForecastByLatLong(double latitude, double longitude, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("lat", WeatherBugDataUtils.formatNumber(latitude, "0.00"));
        params.put("long", WeatherBugDataUtils.formatNumber(longitude, "0.00"));
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getForecastRSS", params);
        return (Forecasts) WeatherBugDataUtils.bindSingle(doc, "/rss/channel/aws:weather", Forecasts.class);
    }

    /**
     * Gets the weather alerts based on a U.S. ZIP code.
     * 
     * @param zipCode
     *            the ZIP code to match.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the weather alerts.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    public Alert[] getAlerts(String zipCode, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("zipCode", zipCode);
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getAlertsRSS", params);
        List alerts = WeatherBugDataUtils.bind(doc, "/rss/channel/aws:weather/aws:alerts/aws:alert", Alert.class);
        return (Alert[]) alerts.toArray(new Alert[0]);
    }

    /**
     * Gets the weather alerts based on a latitude and longitude.
     * 
     * @param latitude
     *            the latitude.
     * @param longitude
     *            the longitude.
     * @param unitType
     *            the unit type: <code>0</code> for U.S. customary units or <code>1</code> for Metric system units
     * @return the weather alerts.
     * @throws WeatherBugServiceException
     *             if an error occurred processing the service response.
     */
    //http://api.wxbug.net/getAlertsRSS.aspx?ACode=&lat=26&long=-80
    public Alert[] getAlertsByLatLong(double latitude, double longitude, int unitType) throws WeatherBugServiceException {
        Map params = new HashMap();
        params.put("lat", WeatherBugDataUtils.formatNumber(latitude, "0.00"));
        params.put("long", WeatherBugDataUtils.formatNumber(longitude, "0.00"));
        params.put("UnitType", Integer.toString(unitType));
        Document doc = execute("getAlertsRSS", params);
        List alerts = WeatherBugDataUtils.bind(doc, "/rss/channel/aws:weather/aws:alerts/aws:alert", Alert.class);
        return (Alert[]) alerts.toArray(new Alert[0]);
    }

    /**
     * Executes a REST method.
     * 
     * @param methodName
     *            the name of the method.
     * @param params
     *            the set of parameters being appended to the secure request.
     * @return the resulting XML document.
     * @throws WeatherBugServiceException
     *             if an error occurred executing the method.
     */
    private Document execute(String methodName, Map params) throws WeatherBugServiceException {
        params.put("ACode", aCode);
        try {
            return new HTTPHelper().readDocument("http", aCode + ".api.wxbug.net", -1, methodName + ".aspx", params);
        } catch (Exception e) {
            throw new WeatherBugServiceException(e);
        }
    }

    /**
     * Encodes a URL parameter using the UTF-8 encoding.
     * 
     * @param param
     *            the parameter being encoded.
     * @return the encoded parameter.
     * @throws UnsupportedEncodingException
     *             if the parameter cannot be encoded.
     */
    private String encodeURLParameter(String param) throws UnsupportedEncodingException {
        String encodedPath = URLEncoder.encode(param, "UTF8");
        encodedPath = encodedPath.replaceAll("\\+", "%20");
        return encodedPath;
    }
}