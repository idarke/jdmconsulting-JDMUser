package com.jdm.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGuidHandler
{
   @Test
   public void testConversion()
   {
      String test1 = "Louis_45061646453736670339699537363900160024~Ian-YardFlashV2b";
      String expected = "Louis_xxx~Ian-YardFlashV2b";
      String result = GuidHandler.convertToNewGuid(test1);
      assertEquals(expected, result);
   }
   
   @Test
   public void testNoConversion()
   {
      String test1 = "Louis45061646453736670339699537363900160024~Ian-YardFlashV2b";
      String result = GuidHandler.convertToNewGuid(test1);
      assertEquals(test1, result);
   }
}
