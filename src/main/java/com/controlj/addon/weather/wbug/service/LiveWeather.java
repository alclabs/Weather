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

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;

/**
 * The live weather for a specific location.
 */
public class LiveWeather {

	/** The temperature at the auxiliary probe for the station. */
    private BigDecimal auxTemp;

    /** The rate of change in temperature at the auxiliary probe. */
    private BigDecimal auxTempRate;

    /** The auxiliary rate of change unit of measurement. */
    private String auxTempRateUnits;

    /** The auxiliary temperature unit of measurement. */
    private String auxTempUnits;

    /** The average cardinal direction of the wind so far today. */
    private String avgWindDirection;

    /** The average speed of the wind so far today. */
    private BigDecimal avgWindSpeed;

    /** The wind speed (average) unit of measurement. */
    private String avgWindSpeedUnits;

    /** The city code. */
    private int cityCode;

    /** The city state. */
    private String cityState;

    /** The country name. */
    private String country;

    /** The current weather condition. */
    private String currentCondition;

    /** The URL of the icon associated with the current condition. */
    private URL currentConditionIconURL;

    /** The temperature to which the air must be cooled to condense. */
    private BigDecimal dewPoint;

    /** The dew-point unit of measurement. */
    private String dewPointUnits;

    /** The elevation above sea level for this station. */
    private int elevation;

    /** The elevation unit of measurement. */
    private String elevationUnits;

    /** The Wind Chill (cold temps) or Heat Index (hot temps) temperature. */
    private BigDecimal feelsLike;

    /** The feels-Like unit of measurement. */
    private String feelsLikeUnits;

    /** The cardinal direction of strongest wind gust recently recorded. */
    private String gustDirection;

    /** The speed of strongest wind gust recently recorded. */
    private BigDecimal gustSpeed;

    /** The gust speed unit of measurement. */
    private String gustSpeedUnits;

    /** The time of strongest wind gust recently recorded. */
    private Timestamp gustTime;

    /** The highest relative humidity measured today. */
    private BigDecimal highestHumidity;

    /** The relative humidity (high) unit of measurement. */
    private String highestHumidityUnits;

    /** The highest barometric pressure measured today. */
    private BigDecimal highestPressure;

    /** The barometric pressure (high) unit of measurement. */
    private String highestPressureUnits;

    /** The highest temperature measured today. */
    private BigDecimal highestTemperature;

    /** The temperature unit (high) of measurement. */
    private String highestTemperatureUnits;

    /** The current relative humidity. */
    private BigDecimal humidity;

    /** The rate of change in relative humidity. */
    private BigDecimal humidityRate;

    /** The current relative humidity. */
    private String humidityUnits;

    /** The indoor temperature at the facility that hosts the station. */
    private BigDecimal indoorTemperature;

    /** The rate of change in indoor temperature at the station's host facility. */
    private BigDecimal indoorTemperatureRate;

    /** The indoor temperature rate of change unit of measurement. */
    private String indoorTemperatureRateUnits;

    /** The indoor temperature unit of measurement. */
    private String indoorTemperatureUnits;

    /** The latitude. */
    private BigDecimal latitude;

    /** The relative percentage of daylight currently at the station's location. */
    private BigDecimal light;

    /** The rate of change in light at the station's location. */
    private BigDecimal lightRate;

    /** The longitude. */
    private BigDecimal longitude;

    /** The lowest relative humidity measured today. */
    private BigDecimal lowestHumidity;

    /** The relative humidity (low) unit of measurement. */
    private String lowestHumidityUnits;

    /** The lowest barometric pressure measured today. */
    private BigDecimal lowestPressure;

    /** The barometric pressure (low) unit of measurement. */
    private String lowestPressureUnits;

    /** The lowest temperature measured today. */
    private BigDecimal lowestTemperature;

    /** The temperature unit (low) of measurement. */
    private String lowestTemperatureUnits;

    /** The maximum rate at which rain has fallen today. */
    private BigDecimal maxRainRate;

    /** The rain rate (max) unit of measurement. */
    private String maxRainRateUnits;

    /** The phase of the moon. */
    private String moonPhase;

    /** The moon phase image URL. */
    private URL moonPhaseImageURL;

    /** The observation time. */
    private Timestamp observationTime;

    /** The current barometric pressure. */
    private BigDecimal pressure;

    /** The rate of change in barometric pressure. */
    private BigDecimal pressureRate;

    /** The barometric pressure rate of change unit of measurement. */
    private String pressureRateUnits;

    /** The barometric pressure (current) unit of measurement. */
    private String pressureUnits;

    /** The amount of rainfall so far this month. */
    private BigDecimal rainMonth;

    /** The month rainfall unit of measurement. */
    private String rainMonthUnits;

    /** The current rate at which rain is falling. */
    private BigDecimal rainRate;

    /** The rain rate (current) unit of measurement. */
    private String rainRateUnits;

    /** The amount of rainfall so far today. */
    private BigDecimal rainToday;

    /** The rainfall unit of measurement. */
    private String rainTodayUnits;

    /** The amount of rainfall so far this year. */
    private BigDecimal rainYear;

    /** The year rainfall unit of measurement. */
    private String rainYearUnits;

    /** The site URL. */
    private URL siteURL;

    /** The station identifier. */
    private String stationId;

    /** The station name. */
    private String stationName;

    /** The time of last/next sunrise. */
    private Timestamp sunriseTime;

    /** The time of last/next sunset. */
    private Timestamp sunsetTime;

    /** The current temperature. */
    private BigDecimal temperature;

    /** The rate of change in temperature. */
    private BigDecimal temperatureRate;

    /** The temperature rate of change unit of measurement. */
    private String temperatureRateUnits;

    /** The temperature unit (current) of measurement. */
    private String temperatureUnits;

    /** The WeatherBug web site URL associated with this live weather. */
    private URL weatherBugSiteURL;

    /** The temperature at which no evaporation occurs and temperature stops dropping. */
    private BigDecimal wetBulb;

    /** The wet-bulb unit of measurement. */
    private String wetBulbUnits;

    /** The current cardinal direction of the wind. */
    private String windDirection;

    /** The current wind speed. */
    private BigDecimal windSpeed;

    /** The wind speed (current) unit of measurement. */
    private String windSpeedUnits;

    /** The ZIP code. */
    private int zipCode;

    public LiveWeather() {
    	super();
    }

    /**
     * Constructs a new live weather.
     * 
     * @param weather
     *            the &lt;aws:weather&gt; XML element.
     */
    public LiveWeather(Element weather) {
        this.observationTime = WeatherBugDataUtils.getTimestamp(weather, "aws:ob/aws:ob-date");
        this.stationId = WeatherBugDataUtils.getString(weather, "aws:ob/aws:station-id");
        this.stationName = WeatherBugDataUtils.getString(weather, "aws:ob/aws:station");
        this.cityState = WeatherBugDataUtils.getString(weather, "aws:ob/aws:city-state");
        this.cityCode = WeatherBugDataUtils.getInt(weather, "aws:ob/aws:city-state/@citycode", -1);
        this.zipCode = WeatherBugDataUtils.getInt(weather, "aws:ob/aws:city-state/@zipcode", -1);
        this.country = WeatherBugDataUtils.getString(weather, "aws:ob/aws:country");
        this.latitude = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:latitude", null);
        this.longitude = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:longitude", null);
        this.siteURL = WeatherBugDataUtils.getURL(weather, "aws:ob/aws:site-url");
        this.auxTemp = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:aux-temp", null);
        this.auxTempUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:aux-temp/@units");
        this.auxTempRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:aux-temp-rate", null);
        this.auxTempRateUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:aux-temp-rate/@units");
        this.currentCondition = WeatherBugDataUtils.getString(weather, "aws:ob/aws:current-condition");
        this.currentConditionIconURL = WeatherBugDataUtils.getURL(weather, "aws:ob/aws:current-condition/@icon");
        this.dewPoint = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:dew-point", null);
        this.dewPointUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:dew-point/@units");
        this.elevation = WeatherBugDataUtils.getInt(weather, "aws:ob/aws:elevation", -1);
        this.elevationUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:elevation/@units");
        this.feelsLike = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:feels-like", null);
        this.feelsLikeUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:feels-like/@units");
        this.gustTime = WeatherBugDataUtils.getTimestamp(weather, "aws:ob/aws:gust-time");
        this.gustDirection = WeatherBugDataUtils.getString(weather, "aws:ob/aws:gust-direction");
        this.gustSpeed = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:gust-speed", null);
        this.gustSpeedUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:gust-speed/@units");
        this.humidity = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:humidity", null);
        this.humidityUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:humidity/@units");
        this.highestHumidity = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:humidity-high", null);
        this.highestHumidityUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:humidity-high/@units");
        this.lowestHumidity = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:humidity-low", null);
        this.lowestHumidityUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:humidity-low/@units");
        this.humidityRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:humidity-rate", null);
        this.indoorTemperature = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:indoor-temp", null);
        this.indoorTemperatureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:indoor-temp/@units");
        this.indoorTemperatureRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:indoor-temp-rate", null);
        this.indoorTemperatureRateUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:indoor-temp-rate/@units");
        this.light = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:light", null);
        this.lightRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:light-rate", null);
        this.moonPhase = WeatherBugDataUtils.getString(weather, "aws:ob/aws:moon-phase");
        this.moonPhaseImageURL = WeatherBugDataUtils.getURL(weather, "aws:ob/aws:moon-phase/@moon-phase-img");
        this.pressure = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:pressure", null);
        this.pressureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:pressure/@units");
        this.highestPressure = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:pressure-high", null);
        this.highestPressureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:pressure-high/@units");
        this.lowestPressure = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:pressure-low", null);
        this.lowestPressureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:pressure-low/@units");
        this.pressureRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:pressure-rate", null);
        this.pressureRateUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:pressure-low/@units");
        this.rainMonth = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:rain-month", null);
        this.rainMonthUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:rain-month/@units");
        this.rainRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:rain-rate", null);
        this.rainRateUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:rain-rate/@units");
        this.maxRainRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:rain-rate-max", null);
        this.maxRainRateUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:rain-rate-max/@units");
        this.rainToday = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:rain-today", null);
        this.rainTodayUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:rain-today/@units");
        this.rainYear = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:rain-year", null);
        this.rainYearUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:rain-year/@units");
        this.temperature = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:temp", null);
        this.temperatureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:temp/@units");
        this.highestTemperature = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:temp-high", null);
        this.highestTemperatureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:temp-high/@units");
        this.lowestTemperature = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:temp-low", null);
        this.lowestTemperatureUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:temp-low/@units");
        this.temperatureRate = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:temp-rate", null);
        this.temperatureRateUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:temp-low/@units");
        this.sunriseTime = WeatherBugDataUtils.getTimestamp(weather, "aws:ob/aws:sunrise");
        this.sunsetTime = WeatherBugDataUtils.getTimestamp(weather, "aws:ob/aws:sunset");
        this.wetBulb = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:wet-bulb", null);
        this.wetBulbUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:wet-bulb/@units");
        this.windSpeed = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:wind-speed", null);
        this.windSpeedUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:wind-speed/@units");
        this.avgWindSpeed = WeatherBugDataUtils.getBigDecimal(weather, "aws:ob/aws:wind-speed-avg", null);
        this.avgWindSpeedUnits = WeatherBugDataUtils.getUnits(weather, "aws:ob/aws:wind-speed-avg/@units");
        this.windDirection = WeatherBugDataUtils.getString(weather, "aws:ob/aws:wind-direction");
        this.avgWindDirection = WeatherBugDataUtils.getString(weather, "aws:ob/aws:wind-direction-avg");
        this.weatherBugSiteURL = WeatherBugDataUtils.getURL(weather, "aws:WebURL");
    }

    /**
     * Returns the temperature at the auxiliary probe for the station..
     * 
     * @return the temperature at the auxiliary probe for the station.
     */
    public BigDecimal getAuxTemp() {
        return auxTemp;
    }

    /**
     * Returns the rate of change in temperature at the auxiliary probe.
     * 
     * @return the rate of change in temperature at the auxiliary probe.
     */
    public BigDecimal getAuxTempRate() {
        return auxTempRate;
    }

    /**
     * Returns the auxiliary rate of change unit of measurement.
     * 
     * @return the auxiliary rate of change unit of measurement.
     */
    public String getAuxTempRateUnits() {
        return auxTempRateUnits;
    }

    /**
     * Returns the auxiliary temperature unit of measurement.
     * 
     * @return the auxiliary temperature unit of measurement.
     */
    public String getAuxTempUnits() {
        return auxTempUnits;
    }

    /**
     * Returns the average cardinal direction of the wind so far today.
     * 
     * @return the average cardinal direction of the wind so far today.
     */
    public String getAvgWindDirection() {
        return avgWindDirection;
    }

    /**
     * Returns the average speed of the wind so far today.
     * 
     * @return the average speed of the wind so far today.
     */
    public BigDecimal getAvgWindSpeed() {
        return avgWindSpeed;
    }

    /**
     * Returns the wind speed (average) unit of measurement.
     * 
     * @return the wind speed (average) unit of measurement.
     */
    public String getAvgWindSpeedUnits() {
        return avgWindSpeedUnits;
    }

    /**
     * Returns the city code.
     * 
     * @return the city code.
     */
    public int getCityCode() {
        return cityCode;
    }

    /**
     * Returns the city state.
     * 
     * @return the city state.
     */
    public String getCityState() {
        return cityState;
    }

    /**
     * Returns the country.
     * 
     * @return the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the current weather condition.
     * 
     * @return the current weather condition.
     */
    public String getCurrentCondition() {
        return currentCondition;
    }

    /**
     * Returns the URL of the icon associated with the current condition.
     * 
     * @return the URL of the icon associated with the current condition.
     */
    public URL getCurrentConditionIconURL() {
        return currentConditionIconURL;
    }

    /**
     * Returns the temperature to which the air must be cooled to condense.
     * 
     * @return the temperature to which the air must be cooled to condense.
     */
    public BigDecimal getDewPoint() {
        return dewPoint;
    }

    /**
     * Returns the dew-point unit of measurement.
     * 
     * @return the dew-point unit of measurement.
     */
    public String getDewPointUnits() {
        return dewPointUnits;
    }

    /**
     * Returns the elevation above sea level for this station.
     * 
     * @return the elevation above sea level for this station.
     */
    public int getElevation() {
        return elevation;
    }

    /**
     * Returns the elevation unit of measurement.
     * 
     * @return the elevation unit of measurement.
     */
    public String getElevationUnits() {
        return elevationUnits;
    }

    /**
     * Returns the Wind Chill (cold temps) or Heat Index (hot temps) temperature.
     * 
     * @return the Wind Chill (cold temps) or Heat Index (hot temps) temperature.
     */
    public BigDecimal getFeelsLike() {
        return feelsLike;
    }

    /**
     * Returns the feels-Like unit of measurement.
     * 
     * @return the feels-Like unit of measurement.
     */
    public String getFeelsLikeUnits() {
        return feelsLikeUnits;
    }

    /**
     * Returns the cardinal direction of strongest wind gust recently recorded.
     * 
     * @return the cardinal direction of strongest wind gust recently recorded.
     */
    public String getGustDirection() {
        return gustDirection;
    }

    /**
     * Returns the speed of strongest wind gust recently recorded.
     * 
     * @return the speed of strongest wind gust recently recorded.
     */
    public BigDecimal getGustSpeed() {
        return gustSpeed;
    }

    /**
     * Returns the gust speed unit of measurement.
     * 
     * @return the gust speed unit of measurement.
     */
    public String getGustSpeedUnits() {
        return gustSpeedUnits;
    }

    /**
     * Returns the time of strongest wind gust recently recorded.
     * 
     * @return the time of strongest wind gust recently recorded.
     */
    public Timestamp getGustTime() {
        return gustTime;
    }

    /**
     * Returns the highest relative humidity measured today.
     * 
     * @return the highest relative humidity measured today.
     */
    public BigDecimal getHighestHumidity() {
        return highestHumidity;
    }

    /**
     * Returns the relative humidity (high) unit of measurement.
     * 
     * @return the relative humidity (high) unit of measurement.
     */
    public String getHighestHumidityUnits() {
        return highestHumidityUnits;
    }

    /**
     * Returns the highest barometric pressure measured today.
     * 
     * @return the highest barometric pressure measured today.
     */
    public BigDecimal getHighestPressure() {
        return highestPressure;
    }

    /**
     * Returns the barometric pressure (high) unit of measurement.
     * 
     * @return the barometric pressure (high) unit of measurement.
     */
    public String getHighestPressureUnits() {
        return highestPressureUnits;
    }

    /**
     * Returns the highest temperature measured today.
     * 
     * @return the highest temperature measured today.
     */
    public BigDecimal getHighestTemperature() {
        return highestTemperature;
    }

    /**
     * Returns the temperature unit (high) of measurement.
     * 
     * @return the temperature unit (high) of measurement.
     */
    public String getHighestTemperatureUnits() {
        return highestTemperatureUnits;
    }

    /**
     * Returns the current relative humidity.
     * 
     * @return the current relative humidity.
     */
    public BigDecimal getHumidity() {
        return humidity;
    }

    /**
     * Returns the rate of change in relative humidity.
     * 
     * @return the rate of change in relative humidity.
     */
    public BigDecimal getHumidityRate() {
        return humidityRate;
    }

    /**
     * Returns the current relative humidity.
     * 
     * @return the current relative humidity.
     */
    public String getHumidityUnits() {
        return humidityUnits;
    }

    /**
     * Returns the indoor temperature at the facility that hosts the station.
     * 
     * @return the indoor temperature at the facility that hosts the station.
     */
    public BigDecimal getIndoorTemperature() {
        return indoorTemperature;
    }

    /**
     * Returns the rate of change in indoor temperature at the station's host facility.
     * 
     * @return the rate of change in indoor temperature at the station's host facility.
     */
    public BigDecimal getIndoorTemperatureRate() {
        return indoorTemperatureRate;
    }

    /**
     * Returns the indoor temperature rate of change unit of measurement.
     * 
     * @return the indoor temperature rate of change unit of measurement.
     */
    public String getIndoorTemperatureRateUnits() {
        return indoorTemperatureRateUnits;
    }

    /**
     * Returns the indoor temperature unit of measurement.
     * 
     * @return the indoor temperature unit of measurement.
     */
    public String getIndoorTemperatureUnits() {
        return indoorTemperatureUnits;
    }

    /**
     * Returns the latitude.
     * 
     * @return the latitude.
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Returns the relative percentage of daylight currently at the station's location.
     * 
     * @return the relative percentage of daylight currently at the station's location.
     */
    public BigDecimal getLight() {
        return light;
    }

    /**
     * Returns the rate of change in light at the station's location.
     * 
     * @return the rate of change in light at the station's location.
     */
    public BigDecimal getLightRate() {
        return lightRate;
    }

    /**
     * Returns the longitude.
     * 
     * @return the longitude.
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Returns the lowest relative humidity measured today.
     * 
     * @return the lowest relative humidity measured today.
     */
    public BigDecimal getLowestHumidity() {
        return lowestHumidity;
    }

    /**
     * Returns the relative humidity (low) unit of measurement.
     * 
     * @return the relative humidity (low) unit of measurement.
     */
    public String getLowestHumidityUnits() {
        return lowestHumidityUnits;
    }

    /**
     * Returns the lowest barometric pressure measured today.
     * 
     * @return the lowest barometric pressure measured today.
     */
    public BigDecimal getLowestPressure() {
        return lowestPressure;
    }

    /**
     * Returns the barometric pressure (low) unit of measurement.
     * 
     * @return the barometric pressure (low) unit of measurement.
     */
    public String getLowestPressureUnits() {
        return lowestPressureUnits;
    }

    /**
     * Returns the lowest temperature measured today.
     * 
     * @return the lowest temperature measured today.
     */
    public BigDecimal getLowestTemperature() {
        return lowestTemperature;
    }

    /**
     * Returns the temperature unit (low) of measurement.
     * 
     * @return the temperature unit (low) of measurement.
     */
    public String getLowestTemperatureUnits() {
        return lowestTemperatureUnits;
    }

    /**
     * Returns the maximum rate at which rain has fallen today.
     * 
     * @return the maximum rate at which rain has fallen today.
     */
    public BigDecimal getMaxRainRate() {
        return maxRainRate;
    }

    /**
     * Returns the rain rate (max) unit of measurement.
     * 
     * @return the rain rate (max) unit of measurement.
     */
    public String getMaxRainRateUnits() {
        return maxRainRateUnits;
    }

    /**
     * Returns the phase of the moon.
     * 
     * @return the phase of the moon.
     */
    public String getMoonPhase() {
        return moonPhase;
    }

    /**
     * Returns the moon phase image URL.
     * 
     * @return the moon phase image URL.
     */
    public URL getMoonPhaseImageURL() {
        return moonPhaseImageURL;
    }

    /**
     * Returns the observation time.
     * 
     * @return the observation time.
     */
    public Timestamp getObservationTime() {
        return observationTime;
    }

    /**
     * Returns the current barometric pressure.
     * 
     * @return the current barometric pressure.
     */
    public BigDecimal getPressure() {
        return pressure;
    }

    /**
     * Returns the rate of change in barometric pressure.
     * 
     * @return the rate of change in barometric pressure.
     */
    public BigDecimal getPressureRate() {
        return pressureRate;
    }

    /**
     * Returns the barometric pressure rate of change unit of measurement.
     * 
     * @return the barometric pressure rate of change unit of measurement.
     */
    public String getPressureRateUnits() {
        return pressureRateUnits;
    }

    /**
     * Returns the barometric pressure (current) unit of measurement.
     * 
     * @return the barometric pressure (current) unit of measuremen.
     */
    public String getPressureUnits() {
        return pressureUnits;
    }

    /**
     * Returns the amount of rainfall so far this month.
     * 
     * @return the amount of rainfall so far this month.
     */
    public BigDecimal getRainMonth() {
        return rainMonth;
    }

    /**
     * Returns the month rainfall unit of measurement.
     * 
     * @return the month rainfall unit of measurement.
     */
    public String getRainMonthUnits() {
        return rainMonthUnits;
    }

    /**
     * Returns the current rate at which rain is falling.
     * 
     * @return the current rate at which rain is falling.
     */
    public BigDecimal getRainRate() {
        return rainRate;
    }

    /**
     * Returns the rain rate (current) unit of measurement.
     * 
     * @return the rain rate (current) unit of measurement.
     */
    public String getRainRateUnits() {
        return rainRateUnits;
    }

    /**
     * Returns the amount of rainfall so far today.
     * 
     * @return the amount of rainfall so far today.
     */
    public BigDecimal getRainToday() {
        return rainToday;
    }

    /**
     * Returns the rainfall unit of measurement.
     * 
     * @return the rainfall unit of measurement.
     */
    public String getRainTodayUnits() {
        return rainTodayUnits;
    }

    /**
     * Returns the amount of rainfall so far this year.
     * 
     * @return the amount of rainfall so far this year.
     */
    public BigDecimal getRainYear() {
        return rainYear;
    }

    /**
     * Returns the year rainfall unit of measurement.
     * 
     * @return the year rainfall unit of measurement.
     */
    public String getRainYearUnits() {
        return rainYearUnits;
    }

    /**
     * Returns the site URL.
     * 
     * @return the site URL.
     */
    public URL getSiteURL() {
        return siteURL;
    }

    /**
     * Returns the station identifier.
     * 
     * @return the station identifier.
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * Returns the station name.
     * 
     * @return the station name.
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Returns the time of last/next sunrise.
     * 
     * @return the time of last/next sunrise.
     */
    public Timestamp getSunriseTime() {
        return sunriseTime;
    }

    /**
     * Returns the time of last/next sunset.
     * 
     * @return the time of last/next sunset.
     */
    public Timestamp getSunsetTime() {
        return sunsetTime;
    }

    /**
     * Returns the current temperature.
     * 
     * @return the current temperature.
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * Returns the rate of change in temperature.
     * 
     * @return the rate of change in temperature.
     */
    public BigDecimal getTemperatureRate() {
        return temperatureRate;
    }

    /**
     * Returns the temperature rate of change unit of measurement.
     * 
     * @return the temperature rate of change unit of measurement.
     */
    public String getTemperatureRateUnits() {
        return temperatureRateUnits;
    }

    /**
     * Returns the temperature unit (current) of measurement.
     * 
     * @return the temperature unit (current) of measurement.
     */
    public String getTemperatureUnits() {
        return temperatureUnits;
    }

    /**
     * Returns the WeathrBug Web site URL associated with this live weather.
     * 
     * @return the Web site URL.
     */
    public URL getWeatherBugSiteURL() {
        return weatherBugSiteURL;
    }

    /**
     * Returns the temperature at which no evaporation occurs and temperature stops dropping.
     * 
     * @return the temperature at which no evaporation occurs and temperature stops dropping.
     */
    public BigDecimal getWetBulb() {
        return wetBulb;
    }

    /**
     * Returns the wet-bulb unit of measurement.
     * 
     * @return the wet-bulb unit of measurement.
     */
    public String getWetBulbUnits() {
        return wetBulbUnits;
    }

    /**
     * Returns the current cardinal direction of the wind.
     * 
     * @return the current cardinal direction of the wind.
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * Returns the current wind speed.
     * 
     * @return the current wind speed.
     */
    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    /**
     * Returns the wind speed (current) unit of measurement.
     * 
     * @return the wind speed (current) unit of measurement.
     */
    public String getWindSpeedUnits() {
        return windSpeedUnits;
    }

    /**
     * Returns the ZIP code.
     * 
     * @return the ZIP code.
     */
    public int getZipCode() {
        return zipCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
