package com.jdm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Zipper
{
   private Zipper() {}
   
   public static byte[] zip(final String jsonMsg) throws IOException
   {
      final ByteArrayOutputStream bos2 = new ByteArrayOutputStream(jsonMsg.length());
      final GZIPOutputStream zipOutputStream = new GZIPOutputStream(bos2);
      zipOutputStream.write(jsonMsg.getBytes("UTF-8"));
      zipOutputStream.flush(); 
      zipOutputStream.close();
      return bos2.toByteArray();
   }
   
   public static String unzip(final byte[] zipped) throws IOException
   {
      final ByteArrayInputStream bos2 = new ByteArrayInputStream(zipped);
      final GZIPInputStream zipInputStream = new GZIPInputStream(bos2);
      final StringBuilder result = new StringBuilder();
      final byte tByte [] = new byte [1024];
      while (true)
      {
          final int iLength = zipInputStream.read (tByte, 0, 1024); 
          if (iLength < 0) break;
          result.append (new String (tByte, 0, iLength));
      }
      zipInputStream.close();
      return result.toString();
   }
}
