package com.controlj.addon.weather.metar;

import com.controlj.addon.weather.data.ConditionsSource;

/**
 *
 */
public class ConditionsSourceImpl extends ConditionsSource {
    private Float temperature;

    @Override public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }
}
