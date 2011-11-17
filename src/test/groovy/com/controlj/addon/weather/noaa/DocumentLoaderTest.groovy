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

@Mixin(NOAAUtilities)
class DocumentLoaderTest extends Specification {

    def loader = new DocumentLoader();
    String todayString = loader.dateFormat.format(new Date())

    def "Future Data URI"() {
        when:
        URI uri = loader.getForecastURI(34.02f, -84.62f, 5, false)

        then:
        uri.path  == "/xml/sample_products/browser_interface/ndfdBrowserClientByDay.php"
        uri.query == "lat=34.02&lon=-84.62&startDate=${todayString}&numDays=5&format=24+hourly&Unit=e"
        uri.host  == "graphical.weather.gov"

        when:
        uri = loader.getForecastURI(-34.02f, 84.62f, 7, true)
        then:
        uri.query == "lat=-34.02&lon=84.62&startDate=${todayString}&numDays=7&format=24+hourly&Unit=m"
    }

    def "Current Observations URI"() {
        given:
        URI uri = loader.getCurrentObsURI("KXSS")

        expect:
        uri.path  == "/xml/current_obs/KXSS.xml"
        uri.query == null
        uri.host  == "weather.gov"
    }

    def "Zip Code URI"() {
        given:
        URI uri = loader.getZipcodeURI("30144")

        expect:
        uri.path  == "/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php"
        uri.query == "listZipCodeList=30144"
        uri.host  == "weather.gov"
    }
}
