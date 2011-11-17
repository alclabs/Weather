package com.controlj.addon.weather.service;

import org.jetbrains.annotations.NotNull;

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
    //void parseAddRequest
}
