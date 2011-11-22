package com.controlj.addon.weather.service;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.util.ResponseWriter;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface WeatherServiceUI {
    @NotNull List<String> getServiceEntryFields();
    @NotNull String getServiceEntryHeaderName(String fieldName);

    String getAddDialogHTML();
    String getServiceConfigHTML();
    String getEntryDisplayName(WeatherConfigEntry entry);
    void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req);
    void addRow(ConfigData configData, ResponseWriter writer, HttpServletRequest req);

    /**
     * Some UI's may provide for extra interactivity on the dialog.  The controller will
     * forward those requests here.
     */
    void dialogAction(ConfigData configData, ResponseWriter writer, HttpServletRequest req);
}
