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
 * An enumeration of the fields available from a {@link ConditionsSource}.  Using this enumeration allows code to
 * find fields by name, or iterator through all fields without resorting to reflection.
 */
public enum ConditionsField {
    /**
     * Field for {@link ConditionsSource#getUpdateTime()}
     */
    updateTime(DateType),

    /**
     * Field for {@link ConditionsSource#getUpdateTime()}.getTime()/600000
     */
    updateStamp(IntegerType),

    /**
     * Field for {@link ConditionsSource#getAverageWindDirection()}
     */
    avgWindDirection(StringType),

    /**
     * Field for {@link ConditionsSource#getAverageWindDegrees()}
     */
    avgWindDegrees(FloatType),

    /**
     * Field for {@link ConditionsSource#getAverageWindSpeed()}
     */
    avgWindSpeed(FloatType),

    /**
     * Field for {@link ConditionsSource#getFeelsLike()}
     */
    feelsLike(FloatType),

    /**
     * Field for {@link ConditionsSource#getTemperature()}
     */
    temperature(FloatType),

    /**
     * Field for {@link ConditionsSource#getHumidity()}
     */
    humidity(FloatType),

    /**
     * Field for {@link ConditionsSource#getPressure()}
     */
    pressure(FloatType),

    /**
     * Field for {@link ConditionsSource#getCurrentCondition()}
     */
    currentCondition(StringType),

    /**
     * Field for {@link ConditionsSource#getDewPoint()}
     */
    dewPoint(FloatType),

    /**
     * Field for {@link ConditionsSource#getWindSpeed()}
     */
    windSpeed(FloatType),

    /**
     * Field for {@link ConditionsSource#getWindDirection()}
     */
    windDirection(StringType),

    /**
     * Field for {@link ConditionsSource#getWindDegrees()}
     */
    windDegrees(FloatType),

    /**
     * Field for {@link ConditionsSource#getRainRate()}
     */
    rainRate(FloatType),

    /**
     * Field for {@link ConditionsSource#getRainToday()}
     */
    rainToday(FloatType),

    /**
     * Field for {@link ConditionsSource#getObservationTime()}
     */
    observationTime(DateType),

    /**
     * Field for {@link ConditionsSource#getObservationTime()}.getTime()/600000
     */
    observationStamp(IntegerType),

    /**
     * Field for {@link ConditionsSource#getWetBulb()}
     */
    wetBulb(FloatType),

    /**
     * Field for {@link ConditionsSource#getIcon()}.getDisplayName()
     */
    iconName(StringType),

    /**
     * Field for {@link ConditionsSource#getIcon()}.getValue()
     */
    iconValue(IntegerType),

    /**
     * Field for {@link ConditionsSource#getSourceURL()}
     */
    sourceURL(StringType);

    /**
     * Finds a field given the {link #name} String for the field.  NOTE: this method does not accept the result of
     * {@link #getName} and will return null for that name.
     *
     * @param name The simple name of the field.
     * @return The field.
     */
    public static ConditionsField find(String name) {
        for (ConditionsField field : values())
            if (field.name().equalsIgnoreCase(name))
                return field;
        return null;
    }

    private final FieldType type;

    private ConditionsField(FieldType type) {
        this.type = type;
    }

    /**
     * Returns an enumeration constant that describes the return type of the {@link #getValue} method.
     */
    public FieldType getType() {
        return type;
    }

    /**
     * Returns the value of this field from the given source. Returns null if the value is null or the source does not
     * support this field.  Use {@link #isSupported} to distinguish between these null results (if you care).
     *
     * @param source the source of the data to return.
     * @return the value of this field from the source.  The type of the value can be determined using {@link #getType}.
     */
    public Object getValue(ConditionsSource source) {
        try {
            return getRawValue(source);
        } catch (UnsupportedOperationException e) {
            // source doesn't support this field
            return null;
        }
    }

    public String getUnits(ConditionsSource source) {
        return getRawUnits(source);
    }

    /**
     * Returns true if this source supports this field, false if the source throws an UnsupportedOperationException
     * when retrieving the value of the field.
     */
    public boolean isSupported(ConditionsSource source) {
        try {
            getRawValue(source);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    /**
     * Returns the full name of this field
     */
    public String getName() {
        return "wc_" + name();
    }

    private Object getRawValue(ConditionsSource source) {
        switch (this) {
            case updateTime:       return source.getUpdateTime();
            case updateStamp:      return (int) (source.getUpdateTime().getTime() / 600000);
            case avgWindDirection: return source.getAverageWindDirection();
            case avgWindDegrees:   return source.getAverageWindDegrees();
            case avgWindSpeed:     return source.getAverageWindSpeed();
            case feelsLike:        return source.getFeelsLike();
            case temperature:      return source.getTemperature();
            case humidity:         return source.getHumidity();
            case pressure:         return source.getPressure();
            case currentCondition: return source.getCurrentCondition();
            case dewPoint:         return source.getDewPoint();
            case windSpeed:        return source.getWindSpeed();
            case windDirection:    return source.getWindDirection();
            case windDegrees:      return source.getWindDegrees();
            case rainRate:         return source.getRainRate();
            case rainToday:        return source.getRainToday();
            case wetBulb:          return source.getWetBulb();
            case observationTime:  return source.getObservationTime();
            case observationStamp: return (int) (source.getObservationTime().getTime() / 600000);
            case iconName:         return source.getIcon().getDisplayName();
            case iconValue:        return source.getIcon().getValue();
            case sourceURL:        return source.getSourceURL();
        }

        Logging.println("WeatherField.getRawValue() doesn't have a case for: " + this);
        throw new UnsupportedOperationException("WeatherField.getRawValue() doesn't have a case for: " + this);
    }

    private String getRawUnits(ConditionsSource source) {
        switch (this) {
            case avgWindDegrees:   return source.getAverageWindDegreesUnits();
            case avgWindSpeed:     return source.getAverageWindSpeedUnits();
            case feelsLike:        return source.getFeelsLikeUnits();
            case temperature:      return source.getTemperatureUnits();
            case humidity:         return source.getHumidityUnits();
            case pressure:         return source.getPressureUnits();
            case dewPoint:         return source.getDewPointUnits();
            case windSpeed:        return source.getWindSpeedUnits();
            case windDegrees:      return source.getWindDegreesUnits();
            case rainRate:         return source.getRainRateUnits();
            case rainToday:        return source.getRainTodayUnits();
            case wetBulb:          return source.getWetBulbUnits();
            case updateStamp:
            case observationStamp: return "10 minute intervals";
            default: return "";
        }
    }
}