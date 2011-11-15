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

class ConfigPropertiesTest extends Specification
{
   def "test set/get string property"()
   {
      given:
         def props = new ConfigProperties(new Properties())

      when:
         props.setStringProperty('key', 'value')
      then:
         props.getStringProperty('key', '') == 'value'
         props.getStringProperty('notakey', 'defvalue') == 'defvalue'

      when:
         props.setStringProperty('key', null)
      then:
         thrown(NullPointerException)

      when:
         props.setStringProperty(null, 'value')
      then:
         thrown(NullPointerException)
   }

   def "test set/get int property"()
   {
      given:
         def props = new ConfigProperties(new Properties())

      when:
         props.setIntProperty('key', 15)
      then:
         props.getIntProperty('key', 0) == 15
         props.getIntProperty('notakey', -5) == -5

      when:
         props.setIntProperty(null, 0)
      then:
         thrown(NullPointerException)
   }

   def "test set/get float property"()
   {
      given:
         def props = new ConfigProperties(new Properties())

      when:
         props.setFloatProperty('key', 15f)
      then:
         props.getFloatProperty('key', 0f) == 15f
         props.getFloatProperty('notakey', -5f) == -5f

      when:
         props.setFloatProperty(null, 0f)
      then:
         thrown(NullPointerException)
   }

   @SuppressWarnings("GroovyPointlessBoolean")
   def "test set/get boolean property"()
   {
      given:
         def props = new ConfigProperties(new Properties())

      when:
         props.setBooleanProperty('key', true)
      then:
         props.getBooleanProperty('key', false) == true
         props.getBooleanProperty('notakey', true) == true

      when:
         props.setBooleanProperty(null, true)
      then:
         thrown(NullPointerException)
   }

   def "test set/get map"()
   {
      given:
         def props = new ConfigProperties(new Properties())
         def map = [testdata : 'testvalue', ('key with spaces') : 'value with spaces']

      when:
         props.setMap(map, 'keyprefix')
      then:
         props.getMap('keyprefix') == map

      when:
         props.setMap(null, 'keyprefix')
      then:
         thrown(NullPointerException)

      when:
         props.setMap(map, null)
      then:
         thrown(NullPointerException)

      when:
         props.getMap(null)
      then:
         thrown(NullPointerException)
   }
}
