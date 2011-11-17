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
@Mixin(NOAAUtilities)

class StationSourceFactoryTest extends Specification {
    def service;

    def "Find Closest Weather Station ID"() {
        setup:
            Document stations = getTestDocument(STATIONS)
            service = new StationSourceFactory(Mock(Document));

        expect:
            service.findClosestWeatherStationID(stations, latitude, longitude) == station

        where:
        latitude | longitude | station
        34.02    | -84.62    | "KRYY"      // 30144 -> McCullum Field
        33.745   | -84.390   | "KATL"      // Downtown atlanta -> Hartsfield
        29.670   | -95.241   | "KHOU"      // Southeast Houston -> Houston Hobby
    }

    def "Find Station"() {
        setup:
            Document stations = getTestDocument(STATIONS)
            service = new StationSourceFactory(Mock(Document));

        expect:
            service.getStationSource(stations, id).getName()== name

        where:
        id     | name
        "KRYY" | "Marietta, Cobb County-McCollum Field Airport"
        "KATL" | "Hartsfield-Jackson/Atlanta International Airport"
        "KTMH" | "Twenty Mile Hill"
    }

}
