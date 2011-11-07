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

import org.dom4j.io.SAXReader
import org.dom4j.Document

class NOAAUtilities {
    public static final String GOODFORECAST = "goodforecast.xml"
    public static final String BADDATEFORECAST = "baddateforecast.xml"
    public static final String MISSINGDESCFORECAST = "missingdescforecast.xml"
    public static final String STATIONS        = "index.xml"
    public static final String ACWORTH_STATION = "/wx_station_index/station[station_id = 'KRYY']"
    public static final String GOODCURRENTOBS = "goodcurrentobs.xml"

    def Document getTestDocument(def filename) {
        InputStream is = NOAAUtilities.class.getResourceAsStream(filename);
        SAXReader reader = new SAXReader();
        return reader.read(is);
    }

    def getTestNode(def filename, def xpath) {
        return getTestDocument(filename).selectSingleNode(xpath)
    }

}
