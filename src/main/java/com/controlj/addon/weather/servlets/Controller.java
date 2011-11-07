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
package com.controlj.addon.weather.servlets;

import com.controlj.addon.weather.ScheduledWeatherLookup;
import com.controlj.addon.weather.config.ConfigData;
import com.controlj.addon.weather.config.ConfigDataFactory;
import com.controlj.addon.weather.config.WeatherConfigEntry;
import com.controlj.addon.weather.data.StationSource;
import com.controlj.addon.weather.service.InvalidConfigurationDataException;
import com.controlj.addon.weather.service.WeatherServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**<!=========================================================================>
   <p>
   Controller servlet.  Determines what to do based on &quot;action&quot;
   parameter, performs the action and forwards to the correct view.  All the
   view jsp files are hidden under WEB-INF so they can't be accessed without
   this controller.
   </p>
   <p>
   Many of the action methods add their results to the request so they can
   be accessed in the page they forward to.
   </p>

   @author sappling
<!==========================================================================>*/
public class Controller extends HttpServlet
{
   private static final String ACTION_PARAM_NAME = "action";
   private static final String ACTION_APPLY = "apply";
   private static final String ACTION_ADD = "add";
   private static final String ACTION_DELETE = "delete";
   private static final String ACTION_SHOW = "show";

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      doPost(request, response);
   }

   /**<!===== doPost =========================================================>
    Forward request to the right action method.  By default, it just
    displays the config page.
    <!      Name             Description>
    @param  request          .
    @param  response         .
    @throws javax.servlet.ServletException .
    @throws java.io.IOException      .
    @author sappling
    <!=======================================================================>*/
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      String action = request.getParameter(ACTION_PARAM_NAME);
      if (ACTION_APPLY.equalsIgnoreCase(action))
         doApply(request, response);
      else if (ACTION_ADD.equalsIgnoreCase(action))
         doAddEntry(request, response);
      else if (ACTION_DELETE.equalsIgnoreCase(action))
         doDeleteEntry(request, response);
      else if (ACTION_SHOW.equalsIgnoreCase(action))
         doShowEntry(request, response);
      else
         doView(request, response);
   }

   private ConfigData getConfigData(HttpServletRequest request)
   {
      ConfigData data = (ConfigData) request.getAttribute("config_data");
      if (data == null)
         data = ConfigDataFactory.loadConfigData();
      return data;
   }

   private void doApply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      ConfigData configData = getConfigData(request);
      String conditionsRateString = request.getParameter("conditions_rate");
      String forecastsRateString = request.getParameter("forecasts_rate");

      try
      {
         configData.setConditionsRefreshInMinutes(Integer.parseInt(conditionsRateString));
         configData.setForecastsRefreshInMinutes(Integer.parseInt(forecastsRateString));
         configData.save();

         ScheduledWeatherLookup.rescheduleUpdates();
      }
      catch (NumberFormatException e)
      {
         e.printStackTrace();
      }
      forwardToViewConfig(request, response);
   }

   private void doView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      request.setAttribute("config_data", getConfigData(request));
      request.getRequestDispatcher("config.jsp").forward(request, response);
   }

   /**<!===== doAddEntry =====================================================>
    Adds a new entry to the SanityCheckConfig.
    <!      Name             Description>
    @param  request          .
    @param  response         .
    @throws javax.servlet.ServletException .
    @throws java.io.IOException      .
    @author sappling
    <!=======================================================================>*/
   private void doAddEntry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      ConfigData configData = getConfigData(request);

      String cpPath = request.getParameter("cppath");
      String zipCode = request.getParameter("zipcode");
      String units = request.getParameter("units");

      if (configData.getEntryForCpPath(cpPath) != null)
         throw new ServletException("An entry with that control program path already exists");

      StationSource stationSource;
      try
      {
         stationSource = configData.getWeatherService().resolveConfigurationToStation(zipCode);
      }
      catch (InvalidConfigurationDataException e)
      {
         throw new ServletException("Unable to resolve zip code", e);
      }
      catch (WeatherServiceException e)
      {
         throw new ServletException("Error resolving zip code", e);
      }

      WeatherConfigEntry entry = new WeatherConfigEntry(cpPath, zipCode, units.equals("metric"), stationSource);
      configData.add(entry);
      try
      {
         configData.save();
      }
      catch (IOException ioe)
      {
         throw new ServletException("Error saving configuration data to disk", ioe);
      }
      try
      {
         ScheduledWeatherLookup.updateConditionsData(configData, entry);
         ScheduledWeatherLookup.updateForecastsData(configData, entry);
      }
      catch (Exception e)
      {
         throw new ServletException("Error updating weather data for new entry", e);
      }

      forwardToViewConfig(request, response);
   }

   /**<!===== doDeleteEntry ==================================================>
    Removes an entry from the SanityCheckConfig.
    <!      Name             Description>
    @param  request          .
    @param  response         .
    @throws javax.servlet.ServletException .
    @throws java.io.IOException      .
    @author sappling
    <!=======================================================================>*/
   private void doDeleteEntry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      ConfigData configData = getConfigData(request);

      String itemString = request.getParameter("item");
      try
      {
         int index = Integer.parseInt(itemString);
         configData.delete(index);
         configData.save();
      }
      catch (NumberFormatException e)
      {
         log("Error deleting configuration data, item not an int", e);
      }
      catch (IOException ioe)
      {
         throw new ServletException("Error saving configuration data to disk", ioe);
      }
      forwardToViewConfig(request, response);
   }

   private void doShowEntry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      ConfigData configData = getConfigData(request);

      String itemString = request.getParameter("item");
      try
      {
         int index = Integer.parseInt(itemString);
         WeatherConfigEntry entry = configData.getList().get(index);
         request.setAttribute("weather_station", entry.getStationSource());
         request.setAttribute("weather_conditions", ScheduledWeatherLookup.updateConditionsData(configData, entry));
         request.setAttribute("weather_forecast", ScheduledWeatherLookup.updateForecastsData(configData, entry));
         request.setAttribute("weather_zip", entry.getZipCode());
      }
      catch (NumberFormatException e)
      {
         throw new ServletException("Error testing configuration data, item not an int", e);
      }
      catch (WeatherServiceException e)
      {
         throw new ServletException("Error testing configuration data", e);
      }
      doView(request, response);
   }

   private void forwardToViewConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      response.sendRedirect(response.encodeRedirectURL("controller"));
   }
}