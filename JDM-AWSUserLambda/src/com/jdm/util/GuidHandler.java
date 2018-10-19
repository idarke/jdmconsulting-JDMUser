package com.jdm.util;


public class GuidHandler
{
   
   /**
    * The GUID is in this format:
    *    username_machineID~projectname
    * The machineID turned out to not be unique and changed when the deployment
    * changed to a new location. So convert the GUID to replace the machineID 
    * with a dummy value.   
    * 
    * @param iotSaveBean
    */
   public static String convertToNewGuid(String oldguid)
   {
      int index1 = oldguid.indexOf("_");
      int index2 = oldguid.indexOf("~");
      if (index1 == -1 || index2 == -1)  return oldguid;
      return oldguid.substring(0,index1+1)+"xxx"+oldguid.substring(index2);
   }
}
