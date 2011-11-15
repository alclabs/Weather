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

import com.controlj.addon.weather.service.WeatherServiceException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentLoader
{
   private static final String WEATHER_STATIONS_URL = "http://www.weather.gov/xml/current_obs/index.xml";

   private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private final AtomicReference<Document> weatherStationsRef = new AtomicReference<Document>();

   public Document loadZipcodeDoc(String zipCode) throws WeatherServiceException
   {
      return retrieveInfo(getZipcodeURI(zipCode));
   }

   public Document getCurrentObs(String stationID) throws WeatherServiceException
   {
      return retrieveInfo(getCurrentObsURI(stationID));
   }

   public Document getForecast(float latitude, float longitude, int numDays, boolean isMetric) throws WeatherServiceException
   {
       return retrieveInfo(getForecastURI(latitude, longitude, numDays, isMetric));
   }

   public Document getWeatherStationsDoc() throws WeatherServiceException {
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

   private URI getZipcodeURI(String zipCode) {
      List<NameValuePair> qparams = new ArrayList<NameValuePair>();
      qparams.add(new BasicNameValuePair("listZipCodeList", zipCode));

      try {
          return URIUtils.createURI("http", "weather.gov", -1, "/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php", URLEncodedUtils.format(qparams, "UTF-8"), null);
      } catch (URISyntaxException e) {
          throw new RuntimeException("Internal error constructing URI", e);
      }
   }

   private Document retrieveInfo(URI uri) throws WeatherServiceException
      {
       SAXReader reader = new SAXReader();
       try {
           Document result = reader.read(uri.toURL());
           return result;
       } catch (Exception e) {
           throw new WeatherServiceException(e);
       }
   }

   private URI getCurrentObsURI(String stationID) {
       try {
           return URIUtils.createURI("http", "weather.gov", -1, "/xml/current_obs/"+stationID+".xml", null, null);
       } catch (URISyntaxException e) {
           throw new RuntimeException("Internal error constructing URI", e);
       }
   }

   private URI getForecastURI(float latitude, float longitude, int numDays, boolean isMetric) {
      List<NameValuePair> qparams = new ArrayList<NameValuePair>();
      qparams.add(new BasicNameValuePair("lat", Float.toString(latitude)));
      qparams.add(new BasicNameValuePair("lon", Float.toString(longitude)));
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
}