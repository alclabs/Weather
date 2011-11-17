package com.controlj.addon.weather.service;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.util.ResponseWriter;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 */
public interface WeatherServiceUI {
    @NotNull List<String> getServiceEntryFields();
    @NotNull String getServiceEntryHeaderName(String fieldName);

    String getAddDialogHTML();
    String getServiceOptionHTML();
    void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req);
}
