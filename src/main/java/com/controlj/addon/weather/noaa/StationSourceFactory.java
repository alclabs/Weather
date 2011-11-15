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

import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.WeatherServiceException;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.XPath;

import java.util.List;

/**
 *
 */
public class StationSourceFactory {
    private Document latLongDoc;

    StationSourceFactory(Document document) {
        this.latLongDoc = document;
    }

   public StationSource findClosestWeatherStation(Document weatherStations) throws WeatherServiceException
   {
      String text = getLatLong();
      float latitude;
      float longitude;
      try
      {
        String[] strings = text.split(",", 2);
        latitude = Float.parseFloat(strings[0]);
        longitude = Float.parseFloat(strings[1]);
      }
      catch (NumberFormatException e)
      {
         throw new WeatherServiceException("Error parsing lat/lon from "+text, e);
      }

      String id = findClosestWeatherStationID(weatherStations, latitude, longitude);
      return getStationSource(weatherStations, id);
   }

   private String getLatLong() throws WeatherServiceException
      {
       Node node = latLongDoc.selectSingleNode("/dwml/latLonList");
       if (node == null) {
           throw new WeatherServiceException("Can't find latitude/longitude from zip code");
       }
       return node.getText();
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

    private StationSource getStationSource(Document weatherStations, String stationID) {
       StationSourceImpl impl = new StationSourceImpl(weatherStations.selectSingleNode("/wx_station_index/station[station_id = '" + stationID + "']"));
       StationSource source = new StationSource();
       source.setId(impl.getId());
       source.setName(impl.getName());
       source.setLatitude(impl.getLatitude());
       source.setLongitude(impl.getLongitude());
       return source;
    }
}
