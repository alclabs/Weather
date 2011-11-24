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
import com.controlj.addon.weather.data.WeatherIcon;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.util.Logging;
import org.dom4j.Document;
import org.dom4j.Node;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastSourceFactory {
    private static final SimpleDateFormat timeLayoutFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateDescriptionFormat = new SimpleDateFormat("EEEE");

    private boolean isMetric;
    private List<Date> dates;
    private List<Float> highs;
    private List<Float> lows;
    private List<Float> probPrecip;
    private List<String> descriptions;
    private List<WeatherIcon> icons;

    public ForecastSourceFactory(boolean isMetric, Document forecast) throws WeatherServiceException {
        this.isMetric = isMetric;
        dates = getForecastDates(forecast);
        highs = getForecastHighs(forecast);
        lows = getForecastLows(forecast);
        probPrecip = getProbPrecip(forecast);
        descriptions = getForecastDescriptions(forecast);
        icons = getIcons(forecast);

        int targetSize = dates.size();
        if (highs.size() != targetSize || lows.size() != targetSize || descriptions.size() != targetSize ||
            icons.size() != targetSize) {
            throw new WeatherServiceException("Not all forecast arrays are the same size");
        }
    }

    public ForecastSource getForecast(int day) {
        return new ForecastSourceImpl(isMetric, highs.get(day),
                                      lows.get(day),
                                      probPrecip.get(day),
                                      dateDescriptionFormat.format(dates.get(day)),
                                      descriptions.get(day), icons.get(day));
    }

    public int getNumberForecastedDays() {
        return dates.size();
    }

    private List<Date> getForecastDates(Document forecast) {
        boolean problem = false;
        ArrayList<Date> result = new ArrayList<Date>();
        Node node = forecast.selectSingleNode("/dwml/data/parameters/temperature[@type='maximum']/@time-layout");
        if (node != null) {
            String timeLayout = node.getText();
            if (timeLayout != null) {
                List list = forecast.selectNodes("/dwml/data/time-layout/layout-key[text()='"+timeLayout+"']//following-sibling::start-valid-time");
                for (Object o : list) {
                    try {
                        result.add(timeLayoutFormat.parse(((Node) o).getText()));
                    } catch (ParseException e) {
                        result.add(null);
                        Logging.println("Couldn't parse date in the forecast", e);
                        problem = true;
                    }
                }
                if (result.size() == 0) {
                    Logging.println("Didn't find any days in forecast");
                    problem = true;
                }
            }
        } else {
            Logging.println("Can't find max temperature in forecast");
            problem = true;
        }
        if (problem) {
            Logging.logDocument("forecast", "Problem parsing forecast dates", forecast);
        }
        return result;
    }

    private List<Float> getForecastHighs(Document forecast) {
        return getForecastTemps(forecast, "maximum");
    }

    private List<Float> getForecastLows(Document forecast) {
        return getForecastTemps(forecast, "minimum");
    }

    private List<Float> getForecastTemps(Document forecast, String type) {
        ArrayList<Float> result = new ArrayList<Float>();
        boolean problem = false;

        List list = forecast.selectNodes("/dwml/data/parameters/temperature[@type='"+type+"']//value");
        for (Object o : list) {
            try {
                result.add(new Float(((Node)o).getText()));
            } catch (NumberFormatException e) {
                result.add(null);
                String badText = ((Node)o).getText();
                if (badText!=null && badText.length()>0) {
                    Logging.println("Error parsing temperature in forecast of '"+badText+"'");
                    problem = true;
                }
            }
        }
        if (problem) {
            Logging.logDocument("forecast", "Problem parsing temperature", forecast);
        }
        return result;
    }

    /*
    Precipitation data is on a 12 hour cycle, so tere are 2 precipitation predictions for a day.
    We return the max of these for the 25 hour cycle.
     */
    private List<Float> getProbPrecip(Document forecast) {
        ArrayList<Float> result = new ArrayList<Float>();
        boolean problem = false;

        List list = forecast.selectNodes("/dwml/data/parameters/probability-of-precipitation//value");
        boolean morning = true;
        float accumulate = 0f;
        for (Object o : list) {
            try {
                float value = Float.parseFloat(((Node)o).getText());
                if (morning) {
                    accumulate = value;
                } else {
                    accumulate = Math.max(value, accumulate);
                    result.add(new Float(accumulate));
                }
                morning = !morning;
            } catch (NumberFormatException e) {
                result.add(null);
                String badText = ((Node)o).getText();
                if (badText!=null && badText.length()>0) {
                    Logging.println("Error parsing prob precip in forecast of '"+badText+"'");
                    problem = true;
                }
            }
        }
        if (problem) {
            Logging.logDocument("forecast", "Problem parsing prob precip", forecast);
        }
        return result;
    }

    private List<String> getForecastDescriptions(Document forecast) {
        ArrayList<String> result = new ArrayList<String>();

        List list = forecast.selectNodes("/dwml/data/parameters/weather/weather-conditions");
        for (Object o : list) {
           String value = ((Node) o).valueOf("@weather-summary");
           result.add(value.length() == 0 ? null : value);
        }
        return result;
    }

    private List<WeatherIcon> getIcons(Document forecast) {
        ArrayList<WeatherIcon> result = new ArrayList<WeatherIcon>();

        List list = forecast.selectNodes("/dwml/data/parameters/conditions-icon/icon-link");
        WeatherIconMapper iconMapper = new WeatherIconMapper();
        for (Object o : list) {
            result.add(iconMapper.mapIconURL(((Node)o).getText()));
        }
        return result;
    }
}
