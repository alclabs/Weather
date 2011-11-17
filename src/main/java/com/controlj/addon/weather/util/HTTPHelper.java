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
package com.controlj.addon.weather.util;

import org.apache.commons.lang.ObjectUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPHelper {

    public Document readDocument(String scheme, String host, int port, String path, Map<String, Object> params)
            throws URISyntaxException, MalformedURLException, DocumentException {
        URI uri = URIUtils.createURI(scheme, host, port, path, encodeParams(params), null);
        SAXReader reader = new SAXReader();
        return reader.read(uri.toURL());
    }

    private String encodeParams(Map<String, Object> params) {
        if (params == null)
            return null;

        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet())
            qparams.add(new BasicNameValuePair(entry.getKey(), ObjectUtils.toString(entry.getValue())));
        return URLEncodedUtils.format(qparams, "UTF-8");
    }

}

