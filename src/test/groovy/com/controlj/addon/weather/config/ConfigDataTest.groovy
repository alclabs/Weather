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
package com.controlj.addon.weather.config

import spock.lang.Specification
import com.controlj.green.addonsupport.access.SystemConnection
import com.controlj.green.addonsupport.access.SystemAccess
import com.controlj.green.addonsupport.access.DataStore
import com.controlj.green.addonsupport.access.WritableSystemAccess
import com.controlj.addon.weather.service.WeatherServices;

public class ConfigDataTest extends Specification
{
   SystemConnection mockSystemConnection(ByteArrayOutputStream os)
   {
      SystemConnection sysConn = Mock()
      sysConn.runWriteAction(_, _) >> { args ->
         DataStore store = Mock()
         store.getOutputStream() >> new BufferedOutputStream(os)

         WritableSystemAccess access = Mock()
         access.getSystemDataStore(_) >> store

         args[1].execute(access)
      }
      return sysConn
   }

   SystemConnection mockSystemConnection(String propData)
   {
      SystemConnection sysConn = Mock()
      sysConn.runReadAction(_) >> { args ->
         DataStore store = Mock()
         store.getInputStream() >> new BufferedInputStream(new StringBufferInputStream(propData))

         SystemAccess access = Mock()
         access.getSystemDataStore(_) >> store

         args[0].execute(access)
      }
      return sysConn
   }

   Properties readProperties(String propData)
   {
      Properties properties = new Properties()
      properties.load(new StringBufferInputStream(propData))
      return properties
   }

   String writeProperties(Map<String, String> props)
   {
      ByteArrayOutputStream os = new ByteArrayOutputStream()
      Properties properties = new Properties()
      properties.putAll(props);
      properties.save(os, "")
      return os.toString()
   }

   def "test save of a default config data"()
   {
      given:
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
         def data = new ConfigData(mockSystemConnection(outputStream));
         data.save()
         def props = readProperties(outputStream.toString())

      expect:
         props['version'] == '2'
         props['conditionsRefreshInMinutes'] == '60'
         props['forecastsRefreshInMinutes'] == '120'
         props['entryCount'] == '0'
   }

   def "test save of serviceConfigData"()
   {
      given:
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
         def data = new ConfigData(mockSystemConnection(outputStream));
         data.setServiceConfigData([testdata : 'testvalue', ('key with spaces') : 'value with spaces'])
         data.save()
         def props = readProperties(outputStream.toString())

      expect:
         props['servicedata.testdata'] == 'testvalue'
         props['servicedata.key with spaces'] == 'value with spaces'
   }

   def "test load of an empty config file"()
   {
      given:
         String propData = writeProperties([:])
         def data = new ConfigData(mockSystemConnection(propData));
         data.load()

      expect:
         data.conditionsRefreshInMinutes == 60
         data.forecastsRefreshInMinutes  == 120
         data.list == []
   }

   def "test load of serviceConfigData"()
   {
      given:
         String propData = writeProperties([('servicedata.testdata'): 'testvalue', ('servicedata.key with spaces') : 'value with spaces'])
         def data = new ConfigData(mockSystemConnection(propData));
         data.load()

      expect:
         data.serviceConfigData['testdata'] == 'testvalue'
         data.serviceConfigData['key with spaces'] == 'value with spaces'
   }

    def "test load of newer version"() {
        when:
            String propData = writeProperties(['version':'99'])
            def data = new ConfigData(mockSystemConnection(propData));
            data.load()
        then:
            thrown IOException
    }

    def "test load service"() {
        given:
            String propData = writeProperties(['version':'2', 'service':'wbug'])
            def data = new ConfigData(mockSystemConnection(propData));
            data.load()

        expect:
            data.weatherServiceEnum == WeatherServices.wbug
    }

    def "test store service"() {
        given:
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            def data = new ConfigData(mockSystemConnection(outputStream));
            data.service = WeatherServices.wbug
            data.save()
            def props = readProperties(outputStream.toString())

        expect:
            props['service'] == 'wbug'
    }


}