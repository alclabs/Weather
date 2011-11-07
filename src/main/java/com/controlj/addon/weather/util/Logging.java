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
package com.controlj.addon.weather.util;

import com.controlj.green.addonsupport.AddOnInfo;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;

public class Logging
{
   public static final PrintWriter LOGGER = determineLogger();

   private static PrintWriter determineLogger()
   {
      try
      {
         // if we are running on a newer version of the add-on API (1.1 or later) then
         // we can get a logger...
         Method method = AddOnInfo.class.getDeclaredMethod("getDateStampLogger");
         AddOnInfo addOnInfo = AddOnInfo.getAddOnInfo();
         Writer writer = (Writer) method.invoke(addOnInfo);
         return new PrintWriter(writer);
      }
      catch (Throwable e)
      {
         // otherwise, just write to System.out (it's the best we can do)...
         return new PrintWriter(System.out);
      }
   }

   public static void println(String msg)
   {
      LOGGER.println(msg);
   }

   public static void println(Throwable throwable)
   {
      throwable.printStackTrace(LOGGER);
   }

   public static void println(String msg, Throwable throwable)
   {
      LOGGER.println(msg);
      throwable.printStackTrace(LOGGER);
   }
}

