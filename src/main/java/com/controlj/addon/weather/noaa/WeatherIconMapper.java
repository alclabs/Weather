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
package com.controlj.addon.weather.noaa;

import com.controlj.addon.weather.data.WeatherIcon;
import com.controlj.addon.weather.util.Logging;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to map icons from the NWS xml feeds to our icon concepts
 */
public class WeatherIconMapper
{
   private static final Pattern ICON_URL_PATTERN = Pattern.compile("(?:.*/)?([a-zA-Z_]+)[0-9]*.*");

   /**
    * Given an icon URL, returns the associated WeatherIcon.  If none can be found, returns Unknown.
    */
   public WeatherIcon mapIconURL(String iconUrl)
   {
      if (iconUrl == null || iconUrl.isEmpty())
         return WeatherIcon.Unknown;

      Matcher matcher = ICON_URL_PATTERN.matcher(iconUrl);
      if (matcher.matches())
      {
         Map<String, WeatherIcon> map = makeIconMap();
         WeatherIcon icon = map.get(matcher.group(1));
         if (icon != null)
            return icon;

         Logging.println("Cannot find icon for simple name "+matcher.group(1)+", which was extracted from URL "+iconUrl);
         return WeatherIcon.Unknown;
      }

      Logging.println("Cannot match URL "+iconUrl);
      return WeatherIcon.Unknown;
   }

   private Map<String, WeatherIcon> makeIconMap()
   {
      Map<String, WeatherIcon> map = new HashMap<String, WeatherIcon>();

      // This was built using the source of the page located at http://www.crh.noaa.gov/riw/?n=forecast_icons

      // Clear Sky
      map.put("skc", WeatherIcon.ClearSky);
      map.put("nskc", WeatherIcon.ClearSky);
      map.put("m_skc", WeatherIcon.ClearSky);
      map.put("m_nskc", WeatherIcon.ClearSky);

      // Few Clouds
      map.put("few", WeatherIcon.FewClouds);
      map.put("nfew", WeatherIcon.FewClouds);
      map.put("m_few", WeatherIcon.FewClouds);
      map.put("m_nfew", WeatherIcon.FewClouds);

      // Partly Cloudy
      map.put("sct", WeatherIcon.PartlyCloudy);
      map.put("nsct", WeatherIcon.PartlyCloudy);
      map.put("m_sct", WeatherIcon.PartlyCloudy);
      map.put("m_nsct", WeatherIcon.PartlyCloudy);

      // Mostly Cloudy
      map.put("bkn", WeatherIcon.MostlyCloudy);
      map.put("nbkn", WeatherIcon.MostlyCloudy);
      map.put("m_bkn", WeatherIcon.MostlyCloudy);
      map.put("m_nbkn", WeatherIcon.MostlyCloudy);

      // Cloudy Sky
      map.put("ovc", WeatherIcon.CloudySky);
      map.put("novc", WeatherIcon.CloudySky);
      map.put("m_ovc", WeatherIcon.CloudySky);
      map.put("m_novc", WeatherIcon.CloudySky);

      // Fog
      map.put("fg", WeatherIcon.Fog);
      map.put("nfg", WeatherIcon.Fog);
      map.put("m_fg", WeatherIcon.Fog);
      map.put("m_nfg", WeatherIcon.Fog);

      // Clouds And Fog
      map.put("sctfg", WeatherIcon.CloudsAndFog);
      map.put("nbknfg", WeatherIcon.CloudsAndFog);
      map.put("m_sctfg", WeatherIcon.CloudsAndFog);
      map.put("m_nbknfg", WeatherIcon.CloudsAndFog);

      // Dust
      map.put("du", WeatherIcon.Dust);
      map.put("ndu", WeatherIcon.Dust);
      map.put("m_du", WeatherIcon.Dust);
      map.put("m_ndu", WeatherIcon.Dust);

      // Smoke
      map.put("fu", WeatherIcon.Smoke);
      map.put("nfu", WeatherIcon.Smoke);
      map.put("m_fu", WeatherIcon.Smoke);
      map.put("m_nfu", WeatherIcon.Smoke);

      // Windy
      map.put("wind", WeatherIcon.Windy);
      map.put("nwind", WeatherIcon.Windy);
      map.put("m_wind", WeatherIcon.Windy);
      map.put("m_nwind", WeatherIcon.Windy);

      // Cold
      map.put("cold", WeatherIcon.Cold);
      map.put("m_cold", WeatherIcon.Cold);

      // Hot
      map.put("hot", WeatherIcon.Hot);
      map.put("m_hot", WeatherIcon.Hot);

      // Rain
      map.put("ra", WeatherIcon.Rain);
      map.put("nra", WeatherIcon.Rain);
      map.put("m_ra", WeatherIcon.Rain);
      map.put("m_nra", WeatherIcon.Rain);

      // Rain Showers
      map.put("shra", WeatherIcon.RainShowers);
      map.put("nshra", WeatherIcon.RainShowers);
      map.put("m_shra", WeatherIcon.RainShowers);
      map.put("m_nshra", WeatherIcon.RainShowers);

      // Isolated Showers
      map.put("hi_shwrs", WeatherIcon.IsolatedShowers);
      map.put("hi_nshwrs", WeatherIcon.IsolatedShowers);
      map.put("m_hi_shwrs", WeatherIcon.IsolatedShowers);
      map.put("m_hi_nshwrs", WeatherIcon.IsolatedShowers);

      // Thunderstorms
      map.put("tsra", WeatherIcon.Thunderstorms);
      map.put("ntsra", WeatherIcon.Thunderstorms);
      map.put("m_tsra", WeatherIcon.Thunderstorms);
      map.put("m_ntsra", WeatherIcon.Thunderstorms);

      // Isolated Thunder
      map.put("hi_tsra", WeatherIcon.IsolatedThunder);
      map.put("hi_ntsra", WeatherIcon.IsolatedThunder);
      map.put("m_hi_tsra", WeatherIcon.IsolatedThunder);
      map.put("m_hi_ntsra", WeatherIcon.IsolatedThunder);

      // Scattered Thunderstorms
      map.put("scttsra", WeatherIcon.ScatteredThunderstorms);
      map.put("nscttsra", WeatherIcon.ScatteredThunderstorms);
      map.put("m_scttsra", WeatherIcon.ScatteredThunderstorms);
      map.put("m_nscttsra", WeatherIcon.ScatteredThunderstorms);

      // Freezing Rain
      map.put("fzra", WeatherIcon.FreezingRain);
      map.put("nfzra", WeatherIcon.FreezingRain);
      map.put("m_fzra", WeatherIcon.FreezingRain);
      map.put("m_nfzra", WeatherIcon.FreezingRain);

      // Rain Snow Mix
      map.put("rasn", WeatherIcon.RainSnowMix);
      map.put("nrasn", WeatherIcon.RainSnowMix);
      map.put("m_rasn", WeatherIcon.RainSnowMix);
      map.put("m_nrasn", WeatherIcon.RainSnowMix);

      // Snow
      map.put("sn", WeatherIcon.Snow);
      map.put("nsn", WeatherIcon.Snow);
      map.put("m_sn", WeatherIcon.Snow);
      map.put("m_nsn", WeatherIcon.Snow);

      // Ice Pellets
      map.put("ip", WeatherIcon.IcePellets);
      map.put("nip", WeatherIcon.IcePellets);
      map.put("m_ip", WeatherIcon.IcePellets);
      map.put("m_nip", WeatherIcon.IcePellets);

      // Rain Ice Pellet Mix
      map.put("raip", WeatherIcon.RainIcePelletMix);
      map.put("nraip", WeatherIcon.RainIcePelletMix);
      map.put("m_raip", WeatherIcon.RainIcePelletMix);
      map.put("m_nraip", WeatherIcon.RainIcePelletMix);

      // Wintry Mix
      map.put("mix", WeatherIcon.WintryMix);
      map.put("nmix", WeatherIcon.WintryMix);
      map.put("m_mix", WeatherIcon.WintryMix);
      map.put("m_nmix", WeatherIcon.WintryMix);

      // Blizzard
      map.put("blizzard", WeatherIcon.Blizzard);
      map.put("m_blizzard", WeatherIcon.Blizzard);


      // Tornadoes
      map.put("tor", WeatherIcon.Tornadoes);
      map.put("ntor", WeatherIcon.Tornadoes);
      map.put("m_tor", WeatherIcon.Tornadoes);
      map.put("m_ntor", WeatherIcon.Tornadoes);

      // Hurricane
      map.put("hurr", WeatherIcon.Hurricane);
      map.put("m_hurr", WeatherIcon.Hurricane);

      // Tropical Storm
      map.put("tropstorm", WeatherIcon.TropicalStorm);
      map.put("m_wave", WeatherIcon.TropicalStorm);

      return map;
   }
}