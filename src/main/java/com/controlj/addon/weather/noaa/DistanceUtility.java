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

/**
 *
 */
public class DistanceUtility {
    public static final float MILES = 3959;  // mean radius of the earth
    public static final float KILOMETERS = 6371;

    public static float greatCircleDistanceDegrees(float radius, float lat1, float long1, float lat2, float long2) {
        return greatCircleDistanceRadians(radius,
                radiansFromDegrees(lat1),
                radiansFromDegrees(long1),
                radiansFromDegrees(lat2),
                radiansFromDegrees(long2) );
    }

    public static float greatCircleDistanceRadians(float radius, float lat1, float long1, float lat2, float long2) {
        float longDelta = long2 - long1;

        float a = (float) Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(longDelta));

        return radius * a;

    }

    public static float radiansFromDegrees(float deg) {
        return (float) (deg * Math.PI / 180);
    }
}
