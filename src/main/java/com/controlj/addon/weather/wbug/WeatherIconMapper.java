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

package com.controlj.addon.weather.wbug;

import com.controlj.addon.weather.data.WeatherIcon;
import com.controlj.addon.weather.util.Logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to map icons from the NWS xml feeds to our icon concepts
 */
public class WeatherIconMapper
{
   private static final Pattern ICON_URL_PATTERN = Pattern.compile("(?:.*/)?[a-zA-Z_]+([0-9]*).*");

   /**
    * Given an icon URL, returns the associated WeatherIcon.  If none can be found, returns Unknown.
    */
   public WeatherIcon mapIconURL(String iconUrl)
   {
      if (iconUrl == null || iconUrl.length() == 0)
         return WeatherIcon.Unknown;

      Matcher matcher = ICON_URL_PATTERN.matcher(iconUrl);
      if (matcher.matches() && matcher.group(1).length() > 0)
      {
          int iconNum = 0;
          try {
              iconNum = Integer.parseInt(matcher.group(1));
              if (iconNum >= 0 && iconNum < iconMap.length)
                 return iconMap[iconNum];
          } catch (NumberFormatException e) {
              Logging.println("Error parsing icon number from " + matcher.group(1) + ", which was extracted from URL "+iconUrl);
              return WeatherIcon.Unknown;
          }

          Logging.println("Cannot find icon number "+iconNum+", which was extracted from URL "+iconUrl);
          return WeatherIcon.Unknown;
      }

      Logging.println("Cannot match URL "+iconUrl);
      return WeatherIcon.Unknown;
   }

   private static final WeatherIcon[] iconMap = new WeatherIcon[] {
     /* cond000.gif	Clear */                        WeatherIcon.ClearSky,
     /* cond001.gif	Cloudy */                       WeatherIcon.CloudySky,
     /* cond002.gif	Partly Cloudy */                WeatherIcon.PartlyCloudy,
     /* cond003.gif	Partly Cloudy */                WeatherIcon.PartlyCloudy,
     /* cond004.gif	Partly Sunny */                 WeatherIcon.PartlyCloudy,
     /* cond005.gif	Rain */                         WeatherIcon.Rain,
     /* cond006.gif	Thunderstorms */                WeatherIcon.Thunderstorms,
     /* cond007.gif	Sunny */                        WeatherIcon.ClearSky,
     /* cond008.gif	Snow */                         WeatherIcon.Snow,
     /* cond009.gif	Flurries */                     WeatherIcon.Snow,
     /* cond010.gif	Unknown */                      WeatherIcon.Unknown,
     /* cond011.gif	Chance of Snow */               WeatherIcon.Snow,
     /* cond012.gif	Snow */                         WeatherIcon.Snow,
     /* cond013.gif	Cloudy */                       WeatherIcon.CloudySky,
     /* cond014.gif	Rain */                         WeatherIcon.Rain,
     /* cond015.gif	Chance of Rain */               WeatherIcon.Rain,
     /* cond016.gif	Partly Cloudy */                WeatherIcon.PartlyCloudy,
     /* cond017.gif	Fair */                         WeatherIcon.ClearSky,
     /* cond018.gif	Thunderstorms */                WeatherIcon.Thunderstorms,
     /* cond019.gif	Chance of Flurry */             WeatherIcon.Snow,
     /* cond020.gif	Chance of Rain */               WeatherIcon.Rain,
     /* cond021.gif	Chance of Sleet */              WeatherIcon.IcePellets,
     /* cond022.gif	Chance of Storms */             WeatherIcon.Storms,
     /* cond023.gif	Hazy */                         WeatherIcon.BadVisibility,
     /* cond024.gif	Mostly Cloudy */                WeatherIcon.MostlyCloudy,
     /* cond025.gif	Sleet */                        WeatherIcon.IcePellets,
     /* cond026.gif	Mostly Sunny */                 WeatherIcon.PartlyCloudy,
     /* cond027.gif	Chance of Flurry */             WeatherIcon.Snow,
     /* cond028.gif	Chance of Sleet  */             WeatherIcon.IcePellets,
     /* cond029.gif	Chance of Snow */               WeatherIcon.Snow,
     /* cond030.gif	Chance of Storms */             WeatherIcon.Thunderstorms,
     /* cond031.gif	Clear */                        WeatherIcon.ClearSky,
     /* cond032.gif	Flurries */                     WeatherIcon.Snow,
     /* cond033.gif	Hazy */                         WeatherIcon.BadVisibility,
     /* cond034.gif	Mostly Cloudy  */               WeatherIcon.MostlyCloudy,
     /* cond035.gif	Fair */                         WeatherIcon.ClearSky,
     /* cond036.gif	Sleet */                        WeatherIcon.IcePellets,
     /* cond037.gif	Unknown */                      WeatherIcon.Unknown,
     /* cond038.gif	Chance of Rain Showers */       WeatherIcon.Rain,
     /* cond039.gif	Chance of Snow Showers */       WeatherIcon.Rain,
     /* cond040.gif	Snow Showers */                 WeatherIcon.Snow,
     /* cond041.gif	Rain Showers */                 WeatherIcon.RainShowers,
     /* cond042.gif	Chance of Rain Showers */       WeatherIcon.RainShowers,
     /* cond043.gif	Chance of Snow Showers */       WeatherIcon.Snow,
     /* cond044.gif	Snow Showers */                 WeatherIcon.Snow,
     /* cond045.gif	Rain Showers */                 WeatherIcon.RainShowers,
     /* cond046.gif	Freezing Rain */                WeatherIcon.FreezingRain,
     /* cond047.gif	Freezing Rain */                WeatherIcon.FreezingRain,
     /* cond048.gif	Chance Freezing Rain */         WeatherIcon.FreezingRain,
     /* cond049.gif	Chance Freezing Rain */         WeatherIcon.FreezingRain,
     /* cond050.gif	Windy */                        WeatherIcon.Windy,
     /* cond051.gif	Fog */                          WeatherIcon.Fog,
     /* cond052.gif	Scattered Showers */            WeatherIcon.Storms,
     /* cond053.gif	Scattered Thunderstorms */      WeatherIcon.Thunderstorms,
     /* cond054.gif	Light Snow */                   WeatherIcon.Snow,
     /* cond055.gif	Chance of Light Snow */         WeatherIcon.Snow,
     /* cond056.gif	Frozen Mix */                   WeatherIcon.WintryMix,
     /* cond057.gif	Chance of Frozen Mix */         WeatherIcon.WintryMix,
     /* cond058.gif	Drizzle */                      WeatherIcon.Rain,
     /* cond059.gif	Chance of Drizzle */            WeatherIcon.Rain,
     /* cond060.gif	Freezing Drizzle */             WeatherIcon.FreezingRain,
     /* cond061.gif	Chance of Freezing Drizzle */   WeatherIcon.FreezingRain,
     /* cond062.gif	Heavy Snow */                   WeatherIcon.Snow,
     /* cond063.gif	Heavy Rain */                   WeatherIcon.Snow,
     /* cond064.gif	Hot and Humid */                WeatherIcon.Hot,
     /* cond065.gif	Very Hot */                     WeatherIcon.Hot,
     /* cond066.gif	Increasing Clouds */            WeatherIcon.CloudySky,
     /* cond067.gif	Clearing */                     WeatherIcon.ClearSky,
     /* cond068.gif	Mostly Cloudy */                WeatherIcon.MostlyCloudy,
     /* cond069.gif	Very Cold */                    WeatherIcon.Cold,
     /* cond070.gif	Mostly Clear */                 WeatherIcon.PartlyCloudy,
     /* cond071.gif	Increasing Clouds */            WeatherIcon.CloudySky,
     /* cond072.gif	Clearing */                     WeatherIcon.ClearSky,
     /* cond073.gif	Mostly Cloudy */                WeatherIcon.MostlyCloudy,
     /* cond074.gif	Very Cold */                    WeatherIcon.Cold,
     /* cond075.gif	Warm and Humid */               WeatherIcon.Hot,
     /* cond076.gif	Nowcast */                      WeatherIcon.Unknown,
     /* cond077.gif	Headline */                     WeatherIcon.Unknown,
     /* cond078.gif	30% Chance of Snow */           WeatherIcon.Snow,
     /* cond079.gif	40% Chance of Snow */           WeatherIcon.Snow,
     /* cond080.gif	50% Chance of Snow */           WeatherIcon.Snow,
     /* cond081.gif	30% Chance of Rain */           WeatherIcon.Rain,
     /* cond082.gif	40% Chance of Rain */           WeatherIcon.Rain,
     /* cond083.gif	50% Chance of Rain */           WeatherIcon.Rain,
     /* cond084.gif	30% Chance of Flurry */         WeatherIcon.Snow,
     /* cond085.gif	40% Chance of Flurry */         WeatherIcon.Snow,
     /* cond086.gif	50% Chance of Flurry */         WeatherIcon.Snow,
     /* cond087.gif	30% Chance of Rain */           WeatherIcon.Rain,
     /* cond088.gif	40% Chance of Rain */           WeatherIcon.Rain,
     /* cond089.gif	50% Chance of Rain */           WeatherIcon.Rain,
     /* cond090.gif	30% Chance of Sleet */          WeatherIcon.IcePellets,
     /* cond091.gif	40% Chance of Sleet */          WeatherIcon.IcePellets,
     /* cond092.gif	50% Chance of Sleet */          WeatherIcon.IcePellets,
     /* cond093.gif	30% Chance of Storms */         WeatherIcon.Storms,
     /* cond094.gif	40% Chance of Storms */         WeatherIcon.Storms,
     /* cond095.gif	50% Chance of Storms */         WeatherIcon.Storms,
     /* cond096.gif	30% Chance of Flurry */         WeatherIcon.Snow,
     /* cond097.gif	40% Chance of Flurry */         WeatherIcon.Snow,
     /* cond098.gif	50% Chance of Flurry */         WeatherIcon.Snow,
     /* cond099.gif	30% Chance of Sleet */          WeatherIcon.IcePellets,
     /* cond100.gif	40% Chance of Sleet */          WeatherIcon.IcePellets,
     /* cond101.gif	50% Chance of Sleet */          WeatherIcon.IcePellets,
     /* cond102.gif	30% Chance of Snow */           WeatherIcon.Snow,
     /* cond103.gif	40% Chance of Snow */           WeatherIcon.Snow,
     /* cond104.gif	50% Chance of Snow */           WeatherIcon.Snow,
     /* cond105.gif	30% Chance of Storms */         WeatherIcon.Storms,
     /* cond106.gif	40% Chance of Storms */         WeatherIcon.Storms,
     /* cond107.gif	50% Chance of Storms */         WeatherIcon.Storms,
     /* cond108.gif	30% Chance Rain Shower */       WeatherIcon.RainShowers,
     /* cond109.gif	40% Chance Rain Shower */       WeatherIcon.RainShowers,
     /* cond110.gif	50% Chance Rain Shower */       WeatherIcon.RainShowers,
     /* cond111.gif	30% Chance Snow Shower */       WeatherIcon.Snow,
     /* cond112.gif	40% Chance Snow Shower */       WeatherIcon.Snow,
     /* cond113.gif	50% Chance Snow Shower */       WeatherIcon.Snow,
     /* cond114.gif	30% Chance Rain Shower */       WeatherIcon.RainShowers,
     /* cond115.gif	40% Chance Rain Shower */       WeatherIcon.RainShowers,
     /* cond116.gif	50% Chance Rain Shower */       WeatherIcon.RainShowers,
     /* cond117.gif	30% Chance Snow Shower */       WeatherIcon.Snow,
     /* cond118.gif	40% Chance Snow Shower */       WeatherIcon.Snow,
     /* cond119.gif	50% Chance Snow Shower */       WeatherIcon.Snow,
     /* cond120.gif	30% Chance Freezing Rain */     WeatherIcon.FreezingRain,
     /* cond121.gif	40% Chance Freezing Rain */     WeatherIcon.FreezingRain,
     /* cond122.gif	50% Chance Freezing Rain */     WeatherIcon.FreezingRain,
     /* cond123.gif	30% Chance Freezing Rain */     WeatherIcon.FreezingRain,
     /* cond124.gif	40% Chance Freezing Rain */     WeatherIcon.FreezingRain,
     /* cond125.gif	50% Chance Freezing Rain */     WeatherIcon.FreezingRain,
     /* cond126.gif	30% Chance of Light Snow */     WeatherIcon.Snow,
     /* cond127.gif	40% Chance of Light Snow */     WeatherIcon.Snow,
     /* cond128.gif	50% Chance of Light Snow */     WeatherIcon.Snow,
     /* cond129.gif	30% Chance of Frozen Mix */     WeatherIcon.WintryMix,
     /* cond130.gif	40% Chance of Frozen Mix */     WeatherIcon.WintryMix,
     /* cond131.gif	50% Chance of Frozen Mix */     WeatherIcon.WintryMix,
     /* cond132.gif	30% Chance of Drizzle */        WeatherIcon.Rain,
     /* cond133.gif	40% Chance of Drizzle */        WeatherIcon.Rain,
     /* cond134.gif	50% Chance of Drizzle */        WeatherIcon.Rain,
     /* cond135.gif	30% Chance Freezing Drizzle */  WeatherIcon.FreezingRain,
     /* cond136.gif	40% Chance Freezing Drizzle */  WeatherIcon.FreezingRain,
     /* cond137.gif	50% Chance Freezing Drizzle */  WeatherIcon.FreezingRain,
     /* cond138.gif	Chance of Snow */               WeatherIcon.Snow,
     /* cond139.gif	Chance of Rain */               WeatherIcon.Rain,
     /* cond140.gif	Chance of Flurry */             WeatherIcon.Snow,
     /* cond141.gif	Chance of Rain */               WeatherIcon.Rain,
     /* cond142.gif	Chance of Sleet */              WeatherIcon.IcePellets,
     /* cond143.gif	Chance of Storms */             WeatherIcon.Storms,
     /* cond144.gif	Chance of Flurry */             WeatherIcon.Snow,
     /* cond145.gif	Chance of Sleet */              WeatherIcon.IcePellets,
     /* cond146.gif	Chance of Snow */               WeatherIcon.Snow,
     /* cond147.gif	Chance of Storms */             WeatherIcon.Storms,
     /* cond148.gif	Chance Rain Shower */           WeatherIcon.RainShowers,
     /* cond149.gif	Chance Snow Shower */           WeatherIcon.Snow,
     /* cond150.gif	Chance Rain Shower */           WeatherIcon.RainShowers,
     /* cond151.gif	Chance Snow Shower */           WeatherIcon.Snow,
     /* cond152.gif	Chance Freezing Rain */         WeatherIcon.FreezingRain,
     /* cond153.gif	Chance Freezing Rain */         WeatherIcon.FreezingRain,
     /* cond154.gif	Chance of Light Snow */         WeatherIcon.Snow,
     /* cond155.gif	Chance of Frozen Mix */         WeatherIcon.WintryMix,
     /* cond156.gif	Chance of Drizzle */            WeatherIcon.FreezingRain,
     /* cond157.gif	Chance Freezing Drizzle */      WeatherIcon.FreezingRain,
     /* cond158.gif	windy */                        WeatherIcon.Windy,
     /* cond159.gif	Foggy */                        WeatherIcon.Fog,
     /* cond160.gif	Light Snow */                   WeatherIcon.Snow,
     /* cond161.gif	Frozen Mix */                   WeatherIcon.WintryMix,
     /* cond162.gif	Drizzle */                      WeatherIcon.Rain,
     /* cond163.gif	Heavy Rain */                   WeatherIcon.RainShowers,
     /* cond164.gif	Chance of Frozen Mix */         WeatherIcon.WintryMix,
     /* cond165.gif	Chance of Drizzle */            WeatherIcon.Rain,
     /* cond166.gif	Chance of Frozen Drizzle */     WeatherIcon.FreezingRain,
     /* cond167.gif	30% Chance Drizzle */           WeatherIcon.Rain,
     /* cond168.gif	30% Chance Frozen Drizzle */    WeatherIcon.FreezingRain,
     /* cond169.gif	30% Chance Frozen Mix */        WeatherIcon.WintryMix,
     /* cond170.gif	40% Chance Drizzle */           WeatherIcon.Rain,
     /* cond171.gif	40% Chance Fozen Drizzle */     WeatherIcon.FreezingRain,
     /* cond172.gif	40% Chance Frozen Mix */        WeatherIcon.WintryMix,
     /* cond173.gif	40% Chance Drizzle */           WeatherIcon.Rain,
     /* cond174.gif	40% Chance Frozen Drizzle */    WeatherIcon.FreezingRain,
     /* cond175.gif	40% Chance Frozen Mix */        WeatherIcon.WintryMix,
     /* cond176.gif	Chance of Light Snow */         WeatherIcon.Snow
  };
}