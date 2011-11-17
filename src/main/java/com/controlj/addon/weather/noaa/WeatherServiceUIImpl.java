package com.controlj.addon.weather.noaa;

import com.controlj.addon.weather.service.WeatherServiceUI;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.util.*;

/**
 *
 */
public class WeatherServiceUIImpl implements WeatherServiceUI {
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
    public String getServiceOptionHTML() {
        return "Units:\n" +
               "<input type=\"radio\" name=\"units\" value=\"imperial\" checked/>US Customary&nbsp;&nbsp;&nbsp;\n" +
               "<input type=\"radio\" name=\"units\" value=\"metric\" />Metric&nbsp;&nbsp;<br/>" +
               "<label>Magic Number:&nbsp;</label><input type=\"text\" name=\"magicnumber\"/>";
    }
}
