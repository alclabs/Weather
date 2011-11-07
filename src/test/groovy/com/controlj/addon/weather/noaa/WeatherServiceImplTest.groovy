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

package com.controlj.addon.weather.noaa

import spock.lang.Specification

import org.dom4j.Document
import static org.hamcrest.Matchers.closeTo

@Mixin(NOAAUtilities)

class WeatherServiceImplTest extends Specification {

    def service = new WeatherServiceImpl();
    String todayString = WeatherServiceImpl.dateFormat.format(new Date())

    def "Future Data URI"() {
        when:
        URI uri = service.getForecastURI("30101", 5, false)

        then:
        uri.query == "zipCodeList=30101&startDate=${todayString}&numDays=5&format=24+hourly&Unit=e"
        uri.host  == "graphical.weather.gov"

        when:
        uri = service.getForecastURI("30144", 7, true)
        then:
        uri.query == "zipCodeList=30144&startDate=${todayString}&numDays=7&format=24+hourly&Unit=m"
    }

    def "Get Latitude and Longitude from Zip Code"() {
        setup:
            Document forecast = getTestDocument(GOODFORECAST)
        expect:
            ((double)service.getLatitudeFromForecast(forecast)) closeTo(34.02, 0.001)
            ((double)service.getLongitudeFromForecast(forecast)) closeTo(-84.62, 0.001)
    }
}
