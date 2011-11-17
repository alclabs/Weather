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
import com.controlj.addon.weather.util.HTTPHelper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentLoader
{
   private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private final AtomicReference<Document> weatherStationsRef = new AtomicReference<Document>();

   public Document loadZipcodeDoc(String zipCode) throws WeatherServiceException
   {
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("listZipCodeList", zipCode);
       try {
           return new HTTPHelper().readDocument("http", "weather.gov", -1, "/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php", params);
       } catch (Exception e) {
           throw new WeatherServiceException(e);
       }
   }

   public Document getCurrentObs(String stationID) throws WeatherServiceException
   {
       try {
           return new HTTPHelper().readDocument("http", "weather.gov", -1, "/xml/current_obs/"+stationID+".xml", null);
       } catch (Exception e) {
           throw new WeatherServiceException(e);
       }
   }

   public Document getForecast(float latitude, float longitude, int numDays, boolean isMetric) throws WeatherServiceException
   {
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("lat", Float.toString(latitude));
       params.put("lon", Float.toString(longitude));
       params.put("startDate", dateFormat.format(new Date()));
       params.put("numDays", Integer.toString(numDays));
       params.put("format", "24 hourly");
       params.put("Unit", isMetric ? "m":"e");
       try {
           return new HTTPHelper().readDocument("http", "graphical.weather.gov", -1, "/xml/sample_products/browser_interface/ndfdBrowserClientByDay.php", params);
       } catch (Exception e) {
           throw new WeatherServiceException(e);
       }
   }

   public Document getWeatherStationsDoc() throws WeatherServiceException {
      Document weatherStations = weatherStationsRef.get();
      if (weatherStations == null) {
           try {
               weatherStations = new HTTPHelper().readDocument("http", "www.weather.gov", -1, "/xml/current_obs/index.xml", null);
               weatherStationsRef.set(weatherStations);
           } catch (Exception e) {
               throw new WeatherServiceException("Internal error constructing URI", e);
           }
       }
       return weatherStations;
   }
}