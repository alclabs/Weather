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

package com.controlj.addon.weather.servlets

import spock.lang.Specification
import com.controlj.addon.weather.data.ConditionsSource
import com.controlj.addon.weather.data.StationSource
import com.controlj.addon.weather.data.ForecastSource
import com.controlj.addon.weather.servlets.PrimitiveServletBase.FieldHandler
import com.controlj.addon.weather.data.FieldType
import com.controlj.addon.weather.config.ConfigData
import com.controlj.green.addonsupport.access.SystemConnection

class PrimitiveServletBaseTest extends Specification {
    def "Iterate Fields Normal"() {
        //iterateFields(ConditionsSource conditionData, StationSource stationData, ForecastSource[] forecastSources, FieldHandler handler)
        setup:
            PrimitiveServletBase base = new PrimitiveServletBase()
            ConditionsSource conditionData = Mock()
            StationSource stationData = Mock()
            ForecastSource[] forecastData = new ForecastSource[2]
            for (int i=0; i<forecastData.length; i++) {
                forecastData[i] = Mock(ForecastSource.class)
            }
            ConfigData config = new ConfigData(Mock(SystemConnection));

            FieldHandler handler = Mock()

        when:
            base.iterateFields(conditionData, stationData, forecastData, config, handler)

        then:
            conditionData.getTemperature() >> 42.0f
            conditionData.getTemperatureUnits() >> "F"
            conditionData.getWindDirection() >> "Down"
            conditionData._ >> { throw new UnsupportedOperationException() }
            stationData.getName() >> "Wild Yonder"
            stationData._ >> { throw new UnsupportedOperationException() }
            forecastData[0].highestTemperature >> 101.0f
            forecastData[0].highestTemperatureUnits >> "F"
            forecastData[1].lowestTemperature >> 30.0f
            forecastData[1].lowestTemperatureUnits >> "F"
            forecastData[1].highestTemperature >> null
            forecastData[1].highestTemperatureUnits >> "F"
            for (int i=0; i<forecastData.length; i++) {
                forecastData[i]._ >> { throw new UnsupportedOperationException() }
            }
            3 * handler.handleField(FieldType.DateType, {it.endsWith("_updateTime")}, _)
            3 * handler.handleField(FieldType.StringType, {it.endsWith("_updateTimeUnit")}, "")
            3 * handler.handleField(FieldType.IntegerType, {it.endsWith("_updateStamp")}, _)
            3 * handler.handleField(FieldType.StringType, {it.endsWith("_updateStampUnit")}, "10 minute intervals")
            //1 * handler.handleField(FieldType.StringType, "wc_updateStampUnit", "10 minute intervals")
            1 * handler.handleField(FieldType.FloatType, "wc_temperature", 42.0f)
            1 * handler.handleField(FieldType.StringType, "wc_temperatureUnit", "F")
            1 * handler.handleField(FieldType.StringType, "wc_windDirection", "Down")
            1 * handler.handleField(FieldType.StringType, "wc_windDirectionUnit", "")
            1 * handler.handleField(FieldType.StringType, "ws_name", "Wild Yonder")
            1 * handler.handleField(FieldType.StringType, "ws_service", "nws")
            1 * handler.handleField(FieldType.FloatType, "wf0_highestTemperature", 101.0f)
            1 * handler.handleField(FieldType.StringType, "wf0_highestTemperatureUnit", "F")
            1 * handler.handleField(FieldType.FloatType, "wf1_highestTemperature", null)
            1 * handler.handleField(FieldType.StringType, "wf1_highestTemperatureUnit", "F")
            1 * handler.handleField(FieldType.FloatType, "wf1_lowestTemperature", 30.0f)
            1 * handler.handleField(FieldType.StringType, "wf1_lowestTemperatureUnit", "F")
            0 * handler._
    }
}
