package com.jdm.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TestZipper
{
   @Test
   public void testZipping() throws IOException
   {
      String test = "This is a test";
      byte[] zipped = Zipper.zip(test);
      
      String restored = Zipper.unzip(zipped);
      assertEquals(test,restored);
   }
   
   @Test
   public void testLargeZipping() throws IOException
   {
      StringBuilder test = new StringBuilder();
      for (int i=0; i<10000; i++)
      {
         test.append("This is a test");
      }
      byte[] zipped = Zipper.zip(test.toString());
      
      String restored = Zipper.unzip(zipped);
      assertEquals(test.toString(),restored);
   }
}
