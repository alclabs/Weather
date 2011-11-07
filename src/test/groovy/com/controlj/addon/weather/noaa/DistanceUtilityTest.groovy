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

/**
 * 
 */
class DistanceUtilityTest extends Specification {
    def "Great Circle in Degrees"() {
        expect:
        //Math.abs(DistanceUtility.greatCircleDistanceDegrees(DistanceUtility.MILES, lat1, long1, lat2, long2) - distance) < (distance * 0.001)
        ((double)DistanceUtility.greatCircleDistanceDegrees(DistanceUtility.MILES, lat1, long1, lat2, long2)) closeTo(distance, distance * 0.001)

        where:
        lat1  | long1  | lat2  | long2  | distance
        36.12 | -86.67 | 33.94 | -118.4 | 1794
        53.2  | -1.1   | 52.1  | 2      | 150.496
    }



}
