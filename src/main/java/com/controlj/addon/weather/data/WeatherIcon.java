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
package com.controlj.addon.weather.data;

public enum WeatherIcon
{
    Unknown(0, "Unknown"),
    ClearSky(1, "Clear Sky"),
    PartlyCloudy(2, "Partly Cloudy"),
    MostlyCloudy(3, "Mostly Cloudy"),
    CloudySky(4, "Cloudy Sky"),
    Fog(5, "Fog"),
    BadVisibility(6, "Bad Visibility"),
    Windy(7, "Windy"),
    Cold(8, "Cold"),
    Hot(9, "Hot"),
    Drizzle(10, "Drizzle"),
    Rain(11, "Rain"),
    RainShowers(12, "Rain Showers"),
    Storms(13, "Storms"),
    Thunderstorms(14, "Thunderstorms"),
    Flurries(15, "Flurries"),
    Snow(16, "Snow"),
    SnowShowers(17, "SnowShowers"),
    Blizzard(18, "Blizzard"),
    FreezingDrizzle(19, "Freezing Drizzle"),
    FreezingRain(20, "Freezing Rain"),
    IcePellets(21, "Ice Pellets"),
    WintryMix(22, "Wintry Mix");

    /*
   Unknown(0, "Unknown"),
   ClearSky(1, "Clear Sky"),
   FewClouds(2, "Few Clouds"),         // kill?
   PartlyCloudy(3, "Partly Cloudy"),
   MostlyCloudy(4, "Mostly Cloudy"),
   CloudySky(5, "Cloudy Sky"),
   Fog(6, "Fog"),                      // kill?
   CloudsAndFog(7, "Clouds And Fog"),  // kill?
   Dust(8, "Dust"),                    // kill?
   Smoke(9, "Smoke"),                  // kill?
   Windy(10, "Windy"),
   Cold(11, "Cold"),                   // ???
   Hot(12, "Hot"),                     // ???
   Rain(13, "Rain"),
   RainShowers(14, "Rain Showers"),
   IsolatedShowers(15, "Isolated Showers"),  // kill?
   Thunderstorms(16, "Thunderstorms"),
   IsolatedThunder(17, "Isolated Thunder"),  // kill?
   ScatteredThunderstorms(18, "Scattered Thunderstorms"),  // kill?
   FreezingRain(19, "Freezing Rain"),
   RainSnowMix(20, "Rain Snow Mix"),
   Snow(21, "Snow"),
   IcePellets(22, "Ice Pellets"),         // kill?
   RainIcePelletMix(23, "Rain Ice Pellet Mix"),    // kill?
   WintryMix(24, "Wintry Mix"),           // kill?
   Blizzard(25, "Blizzard"),              // kill?
   Tornadoes(26, "Tornadoes"),            // kill?
   Hurricane(27, "Hurricane"),            // kill?
   TropicalStorm(28, "Tropical Storm");   // kill?
   */

   private final int value;
   private final String displayName;

   WeatherIcon(int value, String displayName)
   {
      this.value = value;
      this.displayName = displayName;
   }

   public int getValue()
   {
      return value;
   }

   public String getDisplayName()
   {
      return displayName;
   }
}