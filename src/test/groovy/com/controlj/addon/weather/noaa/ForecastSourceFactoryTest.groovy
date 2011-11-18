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
import com.controlj.addon.weather.data.WeatherIcon
import static com.controlj.addon.weather.data.WeatherIcon.*

/**
 * 
 */
@Mixin(NOAAUtilities)
class ForecastSourceFactoryTest extends Specification {
    ForecastSourceFactory factory = new ForecastSourceFactory( getTestDocument(GOODFORECAST))

    def "get forecast dates good"() {
        when:
            List<Date> dates = factory.dates
        then:
            dates.size() == 7
            dates[0].year == 111
            dates[0].month == 9
            dates[0].date ==31
            dates[6].date == 6
    }

    def "get forecast dates bad"() {
        setup:
          factory = new ForecastSourceFactory( getTestDocument(BADDATEFORECAST))

        when:
            List<Date> dates = factory.dates
        then:
            dates.size() == 7
            dates[0].year == 111
            dates[0].month == 9
            dates[0].date ==31
            dates[2] == null    // this one is malformed
            dates[3].date == 3
    }

    def "get forecast highs"() {
        when:
            List<Float> highs = factory.highs
        then:
            highs.size() == 7
            highs == [61, 67, 65, 61, 63, 67, 68].collect { new Float(it) }
    }

    def "get forecast lows"() {
        when:
            List<Float> lows = factory.lows
        then:
            lows.size() == 7
            lows == [32, 37, 40, 47, 45, 46, null].collect { it!=null?new Float(it):null }
    }

    def "get forecast descriptions"() {
        when:
            List<String> descriptions = factory.descriptions
        then:
            descriptions.size() == 7
            descriptions ==
                    ["Mostly Sunny", "Mostly Sunny", "Mostly Sunny", "Slight Chance Rain Showers",
                     "Partly Sunny", "Partly Sunny", "Fire from the Heavens"]
    }

    def "get forecast descriptions handles missing desc"() {
        when:
           factory = new ForecastSourceFactory( getTestDocument(MISSINGDESCFORECAST))
           List<String> descriptions = factory.descriptions
        then:
            descriptions.size() == 7
            descriptions ==
                    ["Becoming Sunny", "Mostly Sunny", "Partly Sunny", "Partly Sunny",
                     "Partly Sunny", "Chance Rain", null]
    }

    def "get icons"() {
        when:
            List<WeatherIcon> icons = factory.icons
        then:
            icons.size() == 7
            icons == [PartlyCloudy, PartlyCloudy, PartlyCloudy, Storms, PartlyCloudy, PartlyCloudy, PartlyCloudy]
    }

}
