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
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;

public class Logging
{
   public static final PrintWriter LOGGER = createLogger();

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

   public static void logDocument(String sourceType, String reason, Document document)
   {
      PrintWriter logger = createLogger(sourceType);

      try
      {
         logger.print(toXMLString(reason, document));
      }
      catch (IOException e)
      {
         println("Error writing document to separate "+sourceType+" log (logDocument reason: "+reason+')', e);
      }
   }

   private static String toXMLString(String reason, Document document) throws IOException
   {
      StringWriter stringWriter = new StringWriter();
      PrintWriter pw = new PrintWriter(stringWriter);
      pw.println(reason);
      pw.flush();

      XMLWriter writer = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
      writer.write(document);
      return stringWriter.toString();
   }

   private static PrintWriter createLogger()
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

   private static PrintWriter createLogger(String name)
   {
      try
      {
         // if we are running on a newer version of the add-on API (1.1 or later) then
         // we can get a logger...
         Method method = AddOnInfo.class.getDeclaredMethod("getDateStampLogger", String.class);
         AddOnInfo addOnInfo = AddOnInfo.getAddOnInfo();
         Writer writer = (Writer) method.invoke(addOnInfo, name);
         return new PrintWriter(writer);
      }
      catch (Throwable e)
      {
         // otherwise, just write to System.out (it's the best we can do)...
         return new PrintWriter(System.out);
      }
   }
}