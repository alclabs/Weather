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

import javax.servlet.http.HttpServletRequest
import com.controlj.addon.weather.data.FieldType
import java.text.SimpleDateFormat;

class PrimDeclarationTest extends Specification {
    static final SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy hh:mm aa");

    def "Define Primitive"() {
        setup:
           StringBuilder builder = new StringBuilder();

        expect:
            PrimDeclaration.definePrimitive(builder, type, name, value)
            builder.toString() == "new ${primType}(\"${name}\", false, \"${resultingValue}\").setPageLocal(true);"

        where:
        type                  | name    |  value    | primType          | resultingValue
        FieldType.FloatType   | "fred"  | 42f       | "PrimitiveNumber" | "42.0"
        FieldType.StringType  | "fred"  | "hey"     | "PrimitiveString" | "hey"
        FieldType.StringType  | "fred"  | "Martha's"| "PrimitiveString" | "Martha\\'s"
        FieldType.DateType    | "fred"  | f.parse("10/2/11 12:34 PM") | "PrimitiveString" | "10\\/02\\/11 12:34 PM"
    }

    def "Get Location"() {
        setup:
            HttpServletRequest req = Mock()

        when:
            String loc = PrimDeclaration.getLocation(req)
        then:
            req.getParameter(_) >> "Administrator@161.145.175.61[lvl5] - #weather [Graphics:None:Test Fields:View]"
            loc == "#weather"
    }
}
