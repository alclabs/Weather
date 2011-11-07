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
package com.controlj.addon.weather.data;

import com.controlj.addon.weather.util.Logging;

import static com.controlj.addon.weather.data.FieldType.*;

/**
 * An enumeration of the fields available from a {@link ForecastSource}.  Using this enumeration allows code to
 * find fields by name, or iterator through all fields without resorting to reflection.
 */
public enum ForecastField
{
   /**
    * Field for {@link ForecastSource#getUpdateTime()}
    */
   updateTime(DateType),

   /**
    * Field for {@link ConditionsSource#getUpdateTime()}.getTime()/600000
    */
   updateStamp(IntegerType),

   /**
    * Field for {@link ForecastSource#getTitle()}
    */
   title(StringType),

   /**
    * Field for {@link ForecastSource#getHighestTemperature()}
    */
   highestTemperature(FloatType),

   /**
    * Field for {@link ForecastSource#getLowestTemperature()}
    */
   lowestTemperature(FloatType),

   /**
    * Field for {@link ForecastSource#getProbPrecipitation()}
    */
   probPrecipitation(FloatType),

   /**
    * Field for {@link ForecastSource#getPrediction()}
    */
   prediction(StringType),

   /**
    * Field for {@link ConditionsSource#getIcon()}.getDisplayName()
    */
   iconName(StringType),

   /**
    * Field for {@link ConditionsSource#getIcon()}.getValue()
    */
   iconValue(IntegerType);

   /**
    * Finds a field given the {link #name} String for the field.  NOTE: this method does not accept the result of
    * {@link #getName} and will return null for that name.
    * @param name The simple name of the field.
    * @return The field.
    */
   public static ForecastField find(String name)
   {
      for (ForecastField field : values())
         if (field.name().equalsIgnoreCase(name))
            return field;
      return null;
   }

   private final FieldType type;

   private ForecastField(FieldType type)
   {
      this.type = type;
   }

   /**
    * Returns an enumeration constant that describes the return type of the {@link #getValue} method.
    */
   public FieldType getType()
   {
      return type;
   }

   /**
    * Returns the value of this field from the given source. Returns null if the value is null or the source does not
    * support this field.  Use {@link #isSupported} to distinguish between these null results (if you care).
    * @param source the source of the data to return.
    * @return the value of this field from the source.  The type of the value can be determined using {@link #getType}.
    */
   public Object getValue(ForecastSource source)
   {
      try
      {
         return getRawValue(source);
      }
      catch (UnsupportedOperationException e)
      {
         // source doesn't support this field
         return null;
      }
   }

   /**
    * Returns true if this source supports this field, false if the source throws an UnsupportedOperationException
    * when retrieving the value of the field.
    */
   public boolean isSupported(ForecastSource source)
   {
      try
      {
         getRawValue(source);
         return true;
      }
      catch (UnsupportedOperationException e)
      {
         return false;
      }
   }

   /**
    * Returns the full name of this field, with the given placeholder character being substituted for for
    * day number.
    */
   public String getName(char placeholder) { return "wf"+placeholder+'_'+name(); }

   /**
    * Returns the full name of this field for the given day number.
    */
   public String getName(int dayNum) { return "wf"+dayNum+'_'+name(); }

   private Object getRawValue(ForecastSource source)
   {
      switch (this)
      {
         case updateTime:         return source.getUpdateTime();
         case updateStamp:        return (int)(source.getUpdateTime().getTime()/600000);
         case title:              return source.getTitle();
         case highestTemperature: return source.getHighestTemperature();
         case lowestTemperature:  return source.getLowestTemperature();
         case prediction:         return source.getPrediction();
         case probPrecipitation:  return source.getProbPrecipitation();
         case iconName:           return source.getIcon().getDisplayName();
         case iconValue:          return source.getIcon().getValue();
      }
      Logging.println("ForecastField.getRawValue() doesn't have a case for: "+this);
      throw new UnsupportedOperationException("ForecastField.getRawValue() doesn't have a case for: "+this);
   }
}