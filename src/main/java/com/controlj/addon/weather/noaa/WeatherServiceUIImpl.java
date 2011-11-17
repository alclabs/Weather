package com.controlj.addon.weather.noaa;

import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.service.WeatherServiceUIBase;
import com.controlj.addon.weather.util.ResponseWriter;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 */
public class WeatherServiceUIImpl extends WeatherServiceUIBase {
    private final LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
    private List<String> fieldList = new ArrayList<String>();

    public WeatherServiceUIImpl() {
        fields.put("zip", "Zip Code");
        fields.put("station_name", "Station Name");
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
        return  "<form>\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td nowrap=\"true\">Location Path:&nbsp;</td>\n" +
                "            <td><input type=\"text\" name=\"newpath\" id=\"newpath\" value=\"\"/></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Zip Code:&nbsp;</td>\n" +
                "            <td><input type=\"text\" name=\"newzip\" id=\"newzip\" value=\"\"/></td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</form>";
    }

    @Override
    public String getServiceConfigHTML() {
        return super.getServiceConfigHTML() +
               "<h2>Service Options</h2>\n" +
               "<div class=\"indent\">" +
               "Units:\n" +
               "<input type=\"radio\" name=\"units\" value=\"imperial\" checked/>US Customary&nbsp;&nbsp;&nbsp;\n" +
               "<input type=\"radio\" name=\"units\" value=\"metric\" />Metric&nbsp;&nbsp;<br/>" +
               "<label>Magic Number:&nbsp;</label><input type=\"text\" name=\"magicnumber\"/>" +
               "</div>\n";
    }

    @Override
    public void updateConfiguration(ConfigData configData, ResponseWriter writer, HttpServletRequest req) {
        // super class handles the refresh rates
        super.updateConfiguration(configData, writer, req);

        updateConfigField(WeatherServiceImpl.CONFIG_KEY_UNITS, configData, writer, req);
        updateIntegerConfigField(WeatherServiceImpl.CONFIG_KEY_MAGICNUMBER, configData, writer, req, 0, 50);
    }
}
