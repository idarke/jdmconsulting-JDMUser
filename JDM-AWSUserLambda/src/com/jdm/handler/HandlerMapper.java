package com.jdm.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.jdm.beans.InputPayloadBean;
import com.jdm.exception.JDMException;

public class HandlerMapper
{
   private static Map<String,Class<?>> handlerMap = new HashMap<String,Class<?>>();
   static
   {
      handlerMap.put("USER", UserHandler.class);
      handlerMap.put("LOGIN", LoginHandler.class);
      handlerMap.put("APPS", ApplicationHandler.class);
      handlerMap.put("IOT", IotHandler.class);
   }
   
   public static IHandler getHandler(InputPayloadBean inputPayloadBean) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
   {
      Class<?> handlerClass = handlerMap.get(inputPayloadBean.getHandler());
      if (handlerClass == null)
      {
         throw new JDMException("1004","No handler defined for " + inputPayloadBean.getHandler());
      }
      return (IHandler) handlerClass.getConstructor().newInstance();
   }

}
