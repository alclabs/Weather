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
import org.dom4j.Document;

import java.io.PrintWriter;
import java.util.Map;

/**
 *
 */
public class WeatherServiceImpl implements WeatherService {
    private final DocumentLoader documentLoader = new DocumentLoader();

   @Override
   public StationSource resolveConfigurationToStation(String zipCode) throws InvalidConfigurationDataException, WeatherServiceException
   {
       StationSourceFactory factory = new StationSourceFactory(documentLoader.loadZipcodeDoc(zipCode));
       return factory.findClosestWeatherStation(documentLoader.getWeatherStationsDoc());
   }

   @Override
   public ConditionsSource getConditionsSource(Map<String, String> configData, StationSource station, Map<String, String> entryData) throws WeatherServiceException {
      boolean isMetric = false;  //TODO: fix me!
      Document currentObs = documentLoader.getCurrentObs(station.getId());
      ConditionsSourceFactory wsFactory = new ConditionsSourceFactory();
      return wsFactory.createSourceFromDocument(currentObs, isMetric);
   }

   @Override
   public ForecastSource[] getForecastSources(Map<String, String> configData, StationSource station, Map<String, String> entryData) throws WeatherServiceException {
       int requestedDays = 7;  //TODO: fix me!
       boolean isMetric = false;
       ForecastSource[] result = new ForecastSource[requestedDays];
       Document forecast = documentLoader.getForecast(station.getLatitude(), station.getLongitude(), requestedDays, isMetric);
       ForecastSourceFactory fsFactory = new ForecastSourceFactory(forecast);
       int returnedDays = fsFactory.getNumberForecastedDays();
       int minDays = Math.min(returnedDays, requestedDays);

       for (int i=0; i<minDays; i++) {
           result[i] = fsFactory.getForecast(i);
       }
       return result;
   }

    @Override
    public void writeAddDialog(PrintWriter writer) {
        writer.print("<form>\n" +
                "        <table>\n" +
                "            <tr>\n" +
                "                <td nowrap=\"true\">Location Path:&nbsp;</td>\n" +
                "                <td><input type=\"text\" name=\"newpath\" id=\"newpath\" value=\"\"/></td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>Zip Code:&nbsp;</td>\n" +
                "                <td><input type=\"text\" name=\"newzip\" id=\"newzip\" value=\"\"/></td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "    </form>");
    }
}