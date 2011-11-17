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

import com.controlj.addon.weather.data.ForecastSource;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.data.ConditionsSource;
import com.controlj.addon.weather.service.InvalidConfigurationDataException;
import com.controlj.addon.weather.service.WeatherService;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceUI;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class WeatherServiceImpl implements WeatherService {
    private static final String WEATHER_STATIONS_URL = "http://www.weather.gov/xml/current_obs/index.xml";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final AtomicReference<Document> weatherStationsRef = new AtomicReference<Document>();
    private WeatherServiceUI ui = new WeatherServiceUIImpl();


   @Override
   public StationSource resolveConfigurationToStation(String zipCode) throws InvalidConfigurationDataException, WeatherServiceException
   {
       Document forecast = getForecastByZip(zipCode, 2, false);
       float latitude = getLatitudeFromForecast(forecast);
       float longitude = getLongitudeFromForecast(forecast);
       StationSourceFactory factory = new StationSourceFactory(getWeatherStationsDoc());
       String closestWeatherStationID = factory.findClosestWeatherStationID(latitude, longitude);
       return factory.getStationSource(closestWeatherStationID);
   }

   @Override public ConditionsSource getConditionsSource(StationSource station, boolean isMetric) throws WeatherServiceException {
      Document currentObs = getCurrentObs(station.getId());
      ConditionsSourceFactory wsFactory = new ConditionsSourceFactory();
      return wsFactory.createSourceFromDocument(currentObs, isMetric);
   }

   public Document getCurrentObs(String stationID) throws WeatherServiceException
   {
      URI uri = getCurrentObsURI(stationID);
      return retrieveInfo(uri);
   }

   private URI getCurrentObsURI(String stationID) {
       try {
           return URIUtils.createURI("http", "weather.gov", -1, "/xml/current_obs/"+stationID+".xml", null, null);
       } catch (URISyntaxException e) {
           throw new RuntimeException("Internal error constructing URI", e);
       }
   }

   @Override
   public ForecastSource[] getForecastSources(String zipCode, int requestedDays, boolean isMetric) throws WeatherServiceException {
       ForecastSource[] result = new ForecastSource[requestedDays];
       Document forecast = getForecastByZip(zipCode, requestedDays, isMetric);
       ForecastSourceFactory fsFactory = new ForecastSourceFactory(forecast);
       int returnedDays = fsFactory.getNumberForecastedDays();
       int minDays = Math.min(returnedDays, requestedDays);

       for (int i=0; i<minDays; i++) {
           result[i] = fsFactory.getForecast(i);
       }
       return result;
   }

    @Override
    public WeatherServiceUI getUI() {
        return ui;
    }

    public Document getForecastByZip(String zipCode, int numDays, boolean isMetric) throws WeatherServiceException {
        URI uri = getForecastURI(zipCode, numDays, isMetric);
        return retrieveInfo(uri);
    }


    private URI getForecastURI(String zipCode, int numDays, boolean isMetric) {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("zipCodeList", zipCode));
        qparams.add(new BasicNameValuePair("startDate", dateFormat.format(new Date())));
        qparams.add(new BasicNameValuePair("numDays", Integer.toString(numDays)));
        qparams.add(new BasicNameValuePair("format", "24 hourly"));
        qparams.add(new BasicNameValuePair("Unit", isMetric ? "m":"e"));

        try {
            return URIUtils.createURI("http", "graphical.weather.gov", -1, "/xml/sample_products/browser_interface/ndfdBrowserClientByDay.php", URLEncodedUtils.format(qparams, "UTF-8"), null);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Internal error constructing URI", e);
        }
    }

    private Document retrieveInfo(URI uri) throws WeatherServiceException {
        SAXReader reader = new SAXReader();
        try {
            Document result = reader.read(uri.toURL());
            return result;
        } catch (Exception e) {
            throw new WeatherServiceException(e);
        }
    }

    private float getLatitudeFromForecast(Document forecast) throws WeatherServiceException {
        return getLatLong(forecast, "latitude");
    }

    private float getLongitudeFromForecast(Document forecast) throws WeatherServiceException {
        return getLatLong(forecast, "longitude");
    }

    private float getLatLong(Document forecast, String latlong) throws WeatherServiceException {
        Node node = forecast.selectSingleNode("/dwml/data/location/point/@"+latlong);
        if (node == null) {
            throw new WeatherServiceException("Can't find " + latlong + " from zip code");
        }
        try {
            return Float.parseFloat(node.getText());
        } catch (NumberFormatException e) {
            throw new WeatherServiceException("Error parsing number for " + latlong+
                    " trying to parse \""+node.getText()+"\"", e);
        }
    }

    private Document getWeatherStationsDoc() throws WeatherServiceException {
       Document weatherStations = weatherStationsRef.get();
       if (weatherStations == null) {
            try {
                weatherStations = retrieveInfo(new URI(WEATHER_STATIONS_URL));
                weatherStationsRef.set(weatherStations);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Internal error constructing URI", e);
            }
        }
        return weatherStations;
    }

    private String findClosestWeatherStationID(Document weatherStations, float latitude, float longitude) {
        XPath idXpath = weatherStations.createXPath("station_id");
        float minDistance = Float.MAX_VALUE;
        String minId= null;

        List stations = weatherStations.selectNodes("/wx_station_index//station");
        for (Object station : stations) {
            try {
                StationSourceImpl stationSource = new StationSourceImpl((Node) station);
                float nextDistance = stationSource.getDistanceTo(latitude,  longitude, false);
                if (nextDistance < minDistance) {
                    Node id = idXpath.selectSingleNode(station);
                    if (id != null) {
                        minId = id.getText();
                        minDistance = nextDistance;
                    }
                }
            } catch (NumberFormatException e) {} // skip this one if malformed
        }
        return minId;
    }

}
