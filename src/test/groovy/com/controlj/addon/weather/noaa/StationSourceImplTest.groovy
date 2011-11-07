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
import static org.hamcrest.Matchers.closeTo

@Mixin(NOAAUtilities)

/**
 * 
 */
class StationSourceImplTest extends Specification {
    def basicTest() {
        expect:
        StationSourceImpl source = new StationSourceImpl(getTestNode(STATIONS, "/wx_station_index/station[station_id = '${selectID}']"))
        source.id == selectID
        ((double)source.latitude) closeTo(latitude, 0.001)
        ((double)source.longitude) closeTo(longitude, 0.001)
        source.name == name

        where:
        selectID | latitude | longitude | name
        "KRYY"   | 34.013d  | -84.599d  | "Marietta, Cobb County-McCollum Field Airport"
        "KDHS"   | 44.21d   | -106.1d   | "Dead Horse"
    }
}
