<%@ page import="com.controlj.addon.weather.data.*" %>
<%@ page import="com.controlj.addon.weather.servlets.PrimitiveServletBase" %>
<%@ page import="com.controlj.addon.weather.config.ConfigData" %>
<%@ page import="com.controlj.addon.weather.config.WeatherConfigEntry" %>
<%@ page import="com.controlj.addon.weather.util.Logging" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright (c) 2010 Automated Logic Corporation
  ~
  ~ Permission is hereby granted, free of charge, to any person obtaining a copy
  ~ of this software and associated documentation files (the "Software"), to deal
  ~ in the Software without restriction, including without limitation the rights
  ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  ~ copies of the Software, and to permit persons to whom the Software is
  ~ furnished to do so, subject to the following conditions:
  ~
  ~ The above copyright notice and this permission notice shall be included in
  ~ all copies or substantial portions of the Software.
  ~
  ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  ~ THE SOFTWARE.
  --%>

<%!
    String formatValue(Object value) {
        return value instanceof Date ? PrimitiveServletBase.timeFormat.format(value) : value.toString();
    }
%>
<html>
  <head><title>Weather Settings</title></head>
  <style type="text/css">
      body {
          font-family:sans-serif; color:black; }
      a { color:black; }
      td { padding-left:1em; text-align:left; }
      th { padding-left:1em; text-align:left; }
      input { margin-right:1em; }
      .del { color:#A00000; text-align:center; }
      .test { color:#A00000; }
      .title { font-weight:bold; font-size:150%; }
      .subtitle { font-style:italic; padding-left:2em; }
      .boxheading { position:relative; left:20px; top:-.7em; background-color:white; padding-left:0.2em; padding-right:0.2em; }
      .right {text-align: right;}
  </style>
<body>
    <div>
        <span class="title">Weather</span>
        <span class="subtitle">powered by NOAA's National Weather Service</span>
    </div>
    <% if (Logging.is41) { %>
      <br/>
      <div>
          <p>
              Warning: This add-on does not download weather data into control programs when using WebCTRL 4.  In order
              to get values to download into a control program, you must be using WebCTRL 5 or later.
          </p>
      </div>
    <% } %>
    <br/>
    <%
        ConfigData configData = (ConfigData) request.getAttribute("config_data");
    %>
    <form action="controller" method="post">
        <table>
            <tr>
                <td style="white-space:nowrap; margin-top:20px; border:solid black 1px;">
                    <span class="boxheading">Refresh Rates:</span>
                    <table cellpadding="0" cellspacing="0" style="position:relative; top:-10px;">
                        <tr>
                            <td class="right">Current Conditions:</td>
                            <td><input type="text" name="conditions_rate" size="4" value="<%=configData.getConditionsRefreshInMinutes()%>"/>minutes</td>
                        </tr>
                        <tr>
                            <td class="right">Forecast:</td>
                            <td><input type="text" name="forecasts_rate" size="4" value="<%=configData.getForecastsRefreshInMinutes()%>"/>minutes</td>
                            <td>&nbsp;<input type="submit" name="action" value="Apply"/></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
    <br/>
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td style="border:solid black 1px; margin-right:100px; padding-right:25px; position:relative;" rowspan="2">
                <span class="boxheading">Locations to update:</span>
                <%
                    List<WeatherConfigEntry> entryList = configData.getList();
                    if (!entryList.isEmpty()) {
                %>
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <th>Delete</th>
                        <th>Location Path</th>
                        <th>Zip Code</th>
                        <th>Metric</th>
                        <th>Last Update</th>
                    </tr>
                    <%
                        int index = 0;
                        for (WeatherConfigEntry entry : entryList)
                        {
                    %>
                    <tr>
                        <td class="del"><a href="controller?action=delete&item=<%=index%>">X</a></td>
                        <td><%=entry.getCpPath()%></td>
                        <td></td>
                        <td></td>
                        <td><%=entry.getLastUpdate()%></td>
                        <td class="test"><a href="controller?action=show&item=<%=index%>">Show Data</a></td>
                    </tr>
                    <%     ++index;
                        } // end for (entryList) %>
                </table>
                <% } // end if (!entryList.isEmpty()) %>
                <form action="controller" method="post">
                <div style="white-space:nowrap; margin-top:20px;">
                    Location Path: <input type="text" name="cppath" size="25"/>
                    Zip Code: <input type="text" name="zipcode" size="10"/>
                    <input type="radio" name="units" value="imperial" checked/>US Customary&nbsp;&nbsp;&nbsp;
                    <input type="radio" name="units" value="metric" />Metric&nbsp;&nbsp;
                    <input type="submit" name="action" value="Add"/>
                </div>
                </form>
            </td>
        </tr>
    </table>
    <%
        String weatherZip = (String) request.getAttribute("weather_zip");
        if (weatherZip != null && weatherZip.length() > 0) {
    %>
    <div>
        <h3>Results of reading weather data for <%=weatherZip%></h3>
        <%
            StationSource stationSource = (StationSource) request.getAttribute("weather_station");
            if (stationSource != null) {
        %>
        <h4>Station Data</h4>
        <table>
            <tr>
                <th>Field</th>
                <th>Value</th>
            </tr>
            <%
                for (StationField field : StationField.values())
                {
                  if (field.isSupported(stationSource)) {%>
                  <tr>
                    <td><%=field.getName()%></td>
                    <% Object value = field.getValue(stationSource);
                       if (value != null) { %>
                         <td><%=formatValue(value)%></td>
                       <% } else { %>
                         <td>&mdash;</td>
                       <% } %>
                  </tr><%
                  }
                }
            %>
        </table>
        <% } // end if ((stationSource != null)

           ConditionsSource conditionsSource = (ConditionsSource) request.getAttribute("weather_conditions");
           if (conditionsSource != null) {
        %>
        <h4>Current Conditions (updateStamp is the number of 10 minute intervals between midnight Jan. 1, 1970 GMT and the update time)</h4>
        <table>
            <tr>
                <th>Field</th>
                <th>Value</th>
            </tr>
            <%
                for (ConditionsField field : ConditionsField.values())
                {
                  if (field.isSupported(conditionsSource)) {%>
                  <tr>
                    <td><%=field.getName()%></td>
                      <% Object value = field.getValue(conditionsSource);
                      if (value != null) { %>
                        <td><%= formatValue(value)%></td>
                      <% } else { %>
                        <td>&mdash;</td>
                      <% } %>
                  </tr><%
                  }
                }
            %>
        </table>
        <% } // end if ((conditionsSource != null)

           ForecastSource[] forecastSources = (ForecastSource[]) request.getAttribute("weather_forecast");
           if (forecastSources != null) {
        %>
        <h4>Forecast (replace the ? in the field with the day number to reference the value)</h4>
        <table>
            <tr>
                <th>Field</th>
                <%
                    for (int i = 0; i < forecastSources.length; i++)
                    { %>
                      <th>Day <%=i%> Value</th><%
                    }
                %>
            </tr>
            <%
                for (ForecastField field : ForecastField.values())
                {
                  if (forecastSources.length > 0 && field.isSupported(forecastSources[0])) {%>
                  <tr>
                    <td><%=field.getName('?')%></td>
                    <%
                        for (ForecastSource source : forecastSources)
                        {
                          Object value = field.getValue(source);
                          if (value != null) { %>
                            <td><%=formatValue(value)%></td>
                          <% } else { %>
                            <td>&mdash;</td>
                          <% }
                        }
                    %>
                  </tr><%
                  }
                }
            %>
        </table>
        <% } // end if ((forecastSources != null) %>
        <h4>Weather Icons (all possible icon names and values)</h4>
        <table>
            <tr>
                <th>Name</th>
                <th>Value</th>
                <th>Name</th>
                <th>Value</th>
                <th>Name</th>
                <th>Value</th>
            </tr>
            <%
                WeatherIcon[] icons = WeatherIcon.values();
                for (int i = 0; i < 10; i++)
                {
                  %>
                  <tr>
                    <td nowrap="true"><%=icons[i].getDisplayName()%></td>
                    <td><%=icons[i].getValue()%></td>
                    <td nowrap="true"><%=icons[i+10].getDisplayName()%></td>
                    <td><%=icons[i+10].getValue()%></td>
                  <% if (i+20 < icons.length) { %>
                    <td nowrap="true"><%=icons[i+20].getDisplayName()%></td>
                    <td><%=icons[i+20].getValue()%></td>
                  </tr><%
                  }
                }
            %>
        </table>
    </div>
    <% } // end if (weatherZip != null && weatherZip.length() > 0) %>
</body>
</html>
