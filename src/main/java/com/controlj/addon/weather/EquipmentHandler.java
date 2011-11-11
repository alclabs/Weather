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
package com.controlj.addon.weather;

import com.controlj.addon.weather.data.*;
import com.controlj.addon.weather.util.Logging;
import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.access.aspect.PresentValue;
import com.controlj.green.addonsupport.access.util.Acceptors;
import com.controlj.green.addonsupport.access.value.FloatValue;
import com.controlj.green.addonsupport.access.value.InvalidValueException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for inserting weather data into control programs.  If the
 * lookupString given in the constructor is not an equipment, other calls to this class
 * will be ignored.
 *
 * Only numeric field values are written into the field devices.  String and Date type fields
 * are ignored.
 *
 * Control programs are written to using a "FieldAccess", so any changes get automatically
 * downloaded to the field device.
 */
public class EquipmentHandler
{
   private static final Pattern WEATHER_STATION_PATTERN  = Pattern.compile("ws_(.+)");
   private static final Pattern WEATHER_CONDITIONS_PATTERN = Pattern.compile("wc_(.+)");
   private static final Pattern WEATHER_FORECAST_PATTERN = Pattern.compile("wf(\\d+)_(.+)");

   private final SystemConnection systemConnection;
   private final Collection<PresentValue> presentValues;

   public EquipmentHandler(SystemConnection systemConn, final String lookupString) throws EquipmentWriteException
   {
      systemConnection = systemConn;
      try
      {
         presentValues = systemConnection.runReadAction(new ReadActionResult<Collection<PresentValue>>()
         {
            @Override public Collection<PresentValue> execute(@NotNull SystemAccess systemAccess) throws Exception
            {
               Location location = systemAccess.getTree(SystemTree.Geographic).resolve(lookupString);
               if (location.getType() == LocationType.Equipment)
                  return location.find(PresentValue.class, Acceptors.acceptAll());
               else
                  return Collections.emptyList();
            }
         });
      }
      catch (Exception e)
      {
         throw new EquipmentWriteException(e);
      }
   }

   public void writeStationData(final StationSource stationSource) throws EquipmentWriteException
   {
      if (presentValues.isEmpty()) //short circuit if no data to update
         return;

      try
      {
         systemConnection.runWriteAction(FieldAccessFactory.newFieldAccess(), "Updating weather station data", new WriteAction()
         {
            @Override public void execute(@NotNull WritableSystemAccess systemAccess) throws Exception
            {
               systemAccess.getSystemDataStore("not_real").getOutputStream();  // workaround for 4.1 SP1b bug
               for (PresentValue presentValue : presentValues)
               {
                  String referenceName = presentValue.getLocation().getReferenceName();
                  Float value = getValueIfStation(referenceName, stationSource);
                  if (value != null)
                     setValue(presentValue, value);
               }
            }
         });
      }
      catch (Exception e)
      {
         throw new EquipmentWriteException(e);
      }
   }

   public void writeConditionsData(final ConditionsSource conditionsSource)throws EquipmentWriteException
   {
      if (presentValues.isEmpty()) //short circuit if no data to update
         return;

      try
      {
         systemConnection.runWriteAction(FieldAccessFactory.newFieldAccess(), "Updating current weather data", new WriteAction()
         {
            @Override public void execute(@NotNull WritableSystemAccess systemAccess) throws Exception
            {
               systemAccess.getSystemDataStore("not_real").getOutputStream();   // workaround for 4.1 SP1b bug
               for (PresentValue presentValue : presentValues)
               {
                  String referenceName = presentValue.getLocation().getReferenceName();
                  Float value = getValueIfConditions(referenceName, conditionsSource);
                  if (value != null)
                     setValue(presentValue, value);
               }
            }
         });
      }
      catch (Exception e)
      {
         throw new EquipmentWriteException(e);
      }
   }

   public void writeForecastData(final ForecastSource[] forecastSources)throws EquipmentWriteException
   {
      if (presentValues.isEmpty()) //short circuit if no data to update
         return;

      try
      {
         systemConnection.runWriteAction(FieldAccessFactory.newFieldAccess(), "Updating weather forecast data", new WriteAction()
         {
            @Override public void execute(@NotNull WritableSystemAccess systemAccess) throws Exception
            {
               systemAccess.getSystemDataStore("not_real").getOutputStream();   // workaround for 4.1 SP1b bug
               for (PresentValue presentValue : presentValues)
               {
                  String referenceName = presentValue.getLocation().getReferenceName();
                  Float value = getValueIfForecast(referenceName, forecastSources);
                  if (value != null)
                     setValue(presentValue, value);
               }
            }
         });
      }
      catch (Exception e)
      {
         throw new EquipmentWriteException(e);
      }
   }

   private Float getValueIfStation(String referenceName, StationSource stationSource)
   {
      Matcher matcher = WEATHER_STATION_PATTERN.matcher(referenceName);
      if (matcher.matches())
      {
         String fieldName = matcher.group(1);
         StationField field = StationField.find(fieldName);
         if (field != null)
            return sanitizeValue(field.getValue(stationSource), field.getType());
      }
      return null;
   }

   private Float getValueIfConditions(String referenceName, ConditionsSource conditionsSource)
   {
      Matcher matcher = WEATHER_CONDITIONS_PATTERN.matcher(referenceName);
      if (matcher.matches())
      {
         String fieldName = matcher.group(1);
         ConditionsField field = ConditionsField.find(fieldName);
         if (field != null)
            return sanitizeValue(field.getValue(conditionsSource), field.getType());
      }
      return null;
   }

   private Float getValueIfForecast(String referenceName, ForecastSource[] forecastSources)
   {
      Matcher matcher = WEATHER_FORECAST_PATTERN.matcher(referenceName);
      if (matcher.matches())
      {
         int day = Integer.parseInt(matcher.group(1));
         String fieldName = matcher.group(2);
         ForecastField field = ForecastField.find(fieldName);
         if (field != null && day < forecastSources.length)
            return sanitizeValue(field.getValue(forecastSources[day]), field.getType());
      }
      return null;
   }

   private Float sanitizeValue(Object value, FieldType type)
   {
      if (value == null)
         return null;
      switch(type)
      {
         case FloatType:   return (Float)value;
         case IntegerType: return new Float((Integer)value);
         case StringType:
         case DateType:    return null; // strings and dates don't get written to the field
      }
      return null; // if it's a type we don't recognize, ignore it
   }

   private void setValue(PresentValue pv, Float value)
   {
      try
      {
         ((FloatValue)pv.getValue()).set(value);
      }
      catch (InvalidValueException e)
      {
         Logging.println("Error writing weather data (" + value + ") into present value (" + pv.getLocation() + ')', e);
      }
   }
}