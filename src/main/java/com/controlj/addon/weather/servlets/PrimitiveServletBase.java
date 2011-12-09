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

package com.controlj.addon.weather.servlets;

import com.controlj.addon.weather.EquipmentWriteException;
import com.controlj.addon.weather.RuntimeInformation;
import com.controlj.addon.weather.ScheduledWeatherLookup;
import com.controlj.addon.weather.WeatherLookup;
import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.*;
import com.controlj.addon.weather.service.WeatherServiceException;

import javax.servlet.http.HttpServlet;
import java.text.SimpleDateFormat;


public class PrimitiveServletBase extends HttpServlet {
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yy hh:mm aa");

    protected ConfigData getConfigData() {
        return ConfigDataFactory.loadConfigData();
    }

    protected void iterateFields(ConditionsSource conditionData, StationSource stationData, ForecastSource[] forecastSources, FieldHandler handler) throws EquipmentWriteException, WeatherServiceException {
        FieldType fieldType;

        for (ConditionsField field : ConditionsField.values()) {
            String fieldName = field.getName();
            if (field.isSupported(conditionData)) {
                Object value = field.getValue(conditionData);
                fieldType = field.getType();
                if (fieldType != null) {
                    handler.handleField(fieldType, fieldName, value);
                }
            }
        }

        ConfigData configData = getConfigData();
        for (StationField field : StationField.values()) {
            String fieldName = field.getName();
            if (field.isSupported(stationData, configData)) {
                Object value = field.getValue(stationData, configData);
                fieldType = field.getType();
                if (fieldType != null) {
                    handler.handleField(fieldType, fieldName, value);
                }
            }
        }

        int sourceCount = 0;
        for (ForecastSource forecastData : forecastSources) {
            for (ForecastField field : ForecastField.values()) {
                String fieldName = field.getName(sourceCount);
                if (field.isSupported(forecastData)) {
                    Object value = field.getValue(forecastData);
                    fieldType = field.getType();
                    if (fieldType != null) {
                        handler.handleField(fieldType, fieldName, value);
                    }
                }
            }
            sourceCount++;
        }
    }

    protected static String formatData(FieldType type, Object value) {
        String result = null;
        if (type == FieldType.DateType) {
            result = timeFormat.format(value);
        } else {
            result = value.toString();
        }
        return result;
    }

    protected interface FieldHandler {
        void handleField(FieldType type, String fieldName, Object value);
    }
}
