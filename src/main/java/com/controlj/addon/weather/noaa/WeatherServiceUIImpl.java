package com.controlj.addon.weather.noaa;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.InvalidConfigurationDataException;
import com.controlj.addon.weather.service.WeatherServiceException;
import com.controlj.addon.weather.service.WeatherServiceUIBase;
import com.controlj.addon.weather.util.Logging;
import com.controlj.addon.weather.util.ResponseWriter;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

/**
 *
 */
public class WeatherServiceUIImpl extends WeatherServiceUIBase {
    private static final String FIELD_ZIP ="zip";
    private static final String FIELD_STATION = "station_name";

    private final LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
    private List<String> fieldList = new ArrayList<String>();

    public WeatherServiceUIImpl() {
        fields.put(FIELD_ZIP, "Zip Code");
        fields.put(FIELD_STATION, "Station Name");
        fieldList = new ArrayList<String>(fields.keySet());
    }

    @Override @NotNull
    public List<String> getServiceEntryFields() {
        return fieldList;
    }

    @Override @NotNull
    public String getServiceEntryHeaderName(String fieldName) {
        String result = fields.get(fieldName);
        if (result == null) {
            throw new IllegalArgumentException("Unknown field '"+fieldName+"'");
        }
        return result;
    }

    @Override
    public String getAddDialogHTML() {
        StringWriter result = new StringWriter();
        copyHTMLTemplate(WeatherServiceUIImpl.class, "adddialog.html", result);
        return result.getBuffer().toString();
    }

    @Override
    public String getServiceConfigHTML() {
        StringWriter result = new StringWriter();
        result.append(super.getServiceConfigHTML());
        copyHTMLTemplate(WeatherServiceUIImpl.class, "serviceconfig.html", result);
        return result.getBuffer().toString();
    }

    @Override
    public String getEntryDisplayName(WeatherConfigEntry entry) {
        String zip = entry.getServiceEntryData().get(FIELD_ZIP);
        return zip;
    }

    @Override
    public void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        // super class handles the refresh rates
        super.updateConfiguration(configData, writer, req);

        updateConfigField(WeatherServiceImpl.CONFIG_KEY_UNITS, configData, writer, req);
        updateIntegerConfigField(WeatherServiceImpl.CONFIG_KEY_MAGICNUMBER, configData, writer, req, 0, 50);
    }

    @Override
    public void addRow(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        String path = req.getParameter("path"); // todo - move to base class
        String zip = req.getParameter(FIELD_ZIP);
        try
        {
            StationSource stationSource = configData.getWeatherService().resolveConfigurationToStation(zip);
            String stationName = stationSource.getName();
            Map<String,String> data = new HashMap<String, String>();
            data.put(FIELD_ZIP, zip);
            data.put(FIELD_STATION, stationName);

            configData.add(new WeatherConfigEntry(path, stationSource, data));
        } catch (WeatherServiceException e) {
            writer.addError("Can't get weather service");
            Logging.println("Can't get weather service in noaa.WeatherServiceUIImpl");
        } catch (InvalidConfigurationDataException e) {
            writer.addError("Can't find station for zip code '"+zip+"'");
            Logging.println("Can't find station for zip code '" + zip + "'", e);
        }

    }
}
