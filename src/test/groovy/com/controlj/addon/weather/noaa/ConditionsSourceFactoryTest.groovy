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
import com.controlj.addon.weather.data.WeatherIcon

@Mixin(NOAAUtilities)
class ConditionsSourceFactoryTest extends Specification
{
   ConditionsSourceFactory factory = new ConditionsSourceFactory();

   def "test temperature"()
   {
      when:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      then:
         ((double)source.getTemperature()) closeTo(71.0, 0.001);

      when:
         def metricSource = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), true)
      then:
         ((double)metricSource.getTemperature()) closeTo(21.9, 0.001);
   }

   def "test humidity"()
   {
      given:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      expect:
         ((double)source.getHumidity()) closeTo(26.0, 0.001);
   }

   def "test pressure"()
   {
      when:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      then:
         ((double)source.getPressure()) closeTo(30.21, 0.001);

      when:
         def metricSource = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), true)
      then:
         ((double)metricSource.getPressure()) closeTo(1022.8, 0.001);
   }

   def "test current condition"()
   {
      given:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      expect:
         source.getCurrentCondition() == "Fair"
   }

   def "test dew point"()
   {
      when:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      then:
         ((double)source.getDewPoint()) closeTo(34.3, 0.001);

      when:
         def metricSource = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), true)
      then:
         ((double)metricSource.getDewPoint()) closeTo(1.3, 0.001);
   }

   def "test wind speed"()
   {
      when:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      then:
         ((double)source.getWindSpeed()) closeTo(11.5, 0.001);

      when:
         def metricSource = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), true)
      then:
         ((double)metricSource.getWindSpeed()) closeTo(10, 0.001);
   }

   def "test wind direction"()
   {
      given:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      expect:
         source.getWindDirection() == "East"
   }

   def "test wind degrees"()
   {
      given:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      expect:
         source.getWindDegrees() == 90
   }

   def "test observation time"()
   {
      given:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      expect:
         source.observationTime == new Date(111, 10, 1, 15, 55, 00);
   }

   def "test icon"()
   {
      given:
         def source = factory.createSourceFromDocument(getTestDocument(GOODCURRENTOBS), false)
      expect:
         source.icon == WeatherIcon.ClearSky
   }
}
