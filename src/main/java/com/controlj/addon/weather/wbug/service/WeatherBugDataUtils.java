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

package com.controlj.addon.weather.wbug.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.controlj.addon.weather.util.Logging;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * A set of utility methods to handle data returned from the WeatherBug API.
 */
public class WeatherBugDataUtils {

    /**
     * Array composed by the dom4j element class, reused by the <i>bind</i> method.
     */
    private static final Class[] ELEM_CLASS_ARRAY = new Class[] { Element.class };

    /** Private constructor (don't instantiate). */
    private WeatherBugDataUtils() {
    }

    /**
     * Extracts a string value from a XML element.
     * 
     * @param elem
     *            the element whose string value must be returned.
     * @param path
     *            the XPath to be used to locate the value.
     * @return the extracted string value.
     */
    public static String getString(Element elem, String path) {
        return elem.valueOf(path);
    }

    /**
     * Extracts a units string value from a XML element.
     * 
     * @param elem
     *            the element whose units string value must be returned.
     * @param path
     *            the XPath to be used to locate the value.
     * @return the extracted units string value.
     */
    public static String getUnits(Element elem, String path) {
        String units =  fixDegrees(elem.valueOf(path));
        if ("km".equals(units)) {
            units = "km/h";
        }
        return units;
    }

    /**
     * Fix the degrees representation, replacing the HTML entity <code>&amp;deg;</code> with the unicode character.
     * 
     * @param s
     *            the string to be fixed.
     * @return the input string with all the degree representation (if any) fixed.
     */
    public static String fixDegrees(String s) {
        return StringUtils.replace(s, "&deg;", "\u00B0");
    }

    /**
     * Extracts an integer value from a XML element.
     * 
     * @param elem
     *            the element whose integer value must be returned.
     * @param path
     *            the XPath to be used to locate the value.
     * @param defaultValue
     *            the default value to be returned if the string value located through <i>path</i> cannot be converted to an integer.
     * @return the extracted integer value.
     */
    public static int getInt(Element elem, String path, int defaultValue) {
        return NumberUtils.toInt(elem.valueOf(path), defaultValue);
    }

    /**
     * Extracts a big decimal value from a XML element.
     * 
     * @param elem
     *            the element whose decimal value must be returned.
     * @param path
     *            the XPath to be used to locate the value.
     * @param defaultValue
     *            the default value to be returned if the string value located through <i>path</i> cannot be converted to a big
     *            decimal.
     * @return the extracted integer value.
     */
    public static BigDecimal getBigDecimal(Element elem, String path, BigDecimal defaultValue) {
        try {
            return new BigDecimal(elem.valueOf(path));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Extracts a URL from a XML element.
     * 
     * @param elem
     *            the element from which the URL must be extracted.
     * @param path
     *            the XPath to be used to locate the value.
     * @return the extracted URL.
     */
    public static URL getURL(Element elem, String path) {
        try {
            return new URL(elem.valueOf(path));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Extracts a timestamp from a XML element.
     * 
     * @param elem
     *            the element from which the timestamp must be extracted.
     * @param path
     *            the XPath to be used to locate the value.
     * @return the extracted timestamp.
     */
    public static Timestamp getTimestamp(Element elem, String path) {
        Element timestampElem = (Element) elem.selectSingleNode(path);
        final String tz = WeatherBugDataUtils.getString(timestampElem, "aws:time-zone/@abbrv") != null
        	? WeatherBugDataUtils.getString(timestampElem, "aws:time-zone/@abbrv") : "CST";

        final GregorianCalendar cal = new GregorianCalendar( TimeZone.getTimeZone(tz) );

        cal.set(Calendar.YEAR, WeatherBugDataUtils.getInt(timestampElem, "aws:year/@number", -1) );
        cal.set(Calendar.MONTH, WeatherBugDataUtils.getInt(timestampElem, "aws:month/@number", -1) - 1); //zero based in GregorianCal
        cal.set(Calendar.DAY_OF_MONTH, WeatherBugDataUtils.getInt(timestampElem, "aws:day/@number", -1) );
        cal.set(Calendar.HOUR_OF_DAY, WeatherBugDataUtils.getInt(timestampElem, "aws:hour/@hour-24", -1) );
        cal.set(Calendar.MINUTE, WeatherBugDataUtils.getInt(timestampElem,"aws:minute/@number", 0) );
        cal.set(Calendar.SECOND, WeatherBugDataUtils.getInt(timestampElem, "aws:second/@number", 0));
        cal.set(Calendar.MILLISECOND, 0);
        
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * Navigates a XML document through an XPath and, for each encountered element, creates a specific WeatherBug data object (<i>Location</i>,
     * <i>Station</i>, and so on). Java reflection errors are silently ignored.
     * 
     * 
     * @param doc
     *            the XML document being accessed.
     * @param path
     *            the XPath used to find specific sub-elements.
     * @param dataClass
     *            the class of objects being instantiated (<i>Location</i>, <i>Station</i>, and so on).
     * @return a list of <i>dataClass</i> objects.
     */
    public static <T> List<T> bind(Document doc, String path, Class<T> dataClass) throws WeatherBugServiceException {
        List<T> objects = bind(doc.getRootElement(), path, dataClass);
        if (objects.isEmpty())
            extractError(doc);
        return objects;
    }

    /**
     * Navigates a XML document through an XPath and creates a specific WeatherBug data object (<i>Location</i>, <i>Station</i>, and
     * so on). Java reflection errors are silently ignored.
     * 
     * @param doc
     *            the XML document being accessed.
     * @param path
     *            the XPath used to find specific sub-elements.
     * @param dataClass
     *            the class of object being instantiated (<i>Location</i>, <i>Station</i>, and so on).
     * @return a <i>dataClass</i> object or <code>null</code>.
     */
    public static <T> T bindSingle(Document doc, String path, Class<T> dataClass) throws WeatherBugServiceException {
        List<T> objects = bind(doc, path, dataClass);
        return objects.get(0);
    }

    /**
     * Navigates a XML document through an XPath and, for each encountered element, creates a specific WeatherBug data object (<i>Location</i>,
     * <i>Station</i>, and so on). Java reflection errors are silently ignored.
     * 
     * @param elem
     *            the XML element being accessed.
     * @param path
     *            the XPath used to find specific sub-elements.
     * @param dataClass
     *            the class of objects being instantiated (<i>Location</i>, <i>Station</i>, and so on).
     * @return a list of <i>dataClass</i> objects.
     */
    public static <T> List<T> bind(Element elem, String path, Class<T> dataClass) {
        Constructor<T> constr;
        try {
            constr = dataClass.getConstructor(ELEM_CLASS_ARRAY);
        } catch (SecurityException e) {
            return Collections.emptyList();
        } catch (NoSuchMethodException e) {
            return Collections.emptyList();
        }
        List<T> resultList = new ArrayList<T>();
        for (Object o : elem.selectNodes(path)) {
            Element item = (Element) o;
            try {
                resultList.add(constr.newInstance(item));
            } catch (IllegalArgumentException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return resultList;
    }

    private static void extractError(Document doc) throws WeatherBugServiceException {
        Node h1 = doc.getRootElement().selectSingleNode("/h1");
        if (h1 != null)
            throw new WeatherBugServiceException(h1.getText());
        else {
            Logging.logDocument("unknown", "Unknown error", doc);
            throw new WeatherBugServiceException("Unknown error");
        }
    }

    /**
     * Formats a timestamp into a string.
     * 
     * @param timestamp
     *            the timestamp being formatted.
     * @param pattern
     *            the pattern describing the string format.
     * @return the formatted string.
     */
    public static String formatTimestamp(Timestamp timestamp, String pattern) {
        return new SimpleDateFormat(pattern).format(timestamp);
    }

    /**
     * Formats a number into a string.
     * 
     * @param d
     *            the decimal number being formatted.
     * @param pattern
     *            the pattern describing the string format.
     * @return the formatted string.
     */
    public static String formatNumber(double d, String pattern) {
        DecimalFormat format = new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.US));
        return format.format(d);
    }

}
