package com.jdm;

import java.lang.reflect.InvocationTargetException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.jdm.beans.InputPayloadBean;
import com.jdm.beans.LambdaProxyResponse;
import com.jdm.common.Common;
import com.jdm.common.PropertiesHelper;
import com.jdm.exception.JDMException;
import com.jdm.handler.HandlerMapper;
import com.jdm.handler.IHandler;

public class JDMUserLambdaHandler implements RequestHandler<Object, Object> {

   @Override
   public Object handleRequest(Object input, Context context) {
      Common.context = context;
      PropertiesHelper.getProperties("db");
      PropertiesHelper.getSqlProperties("jdm");
      context.getLogger().log("JDMUserLambdaHandler input=" + input);
      
      Gson gson = new Gson();
      InputPayloadBean inputPayloadBean = gson.fromJson(input.toString(),InputPayloadBean.class);
      try
      {
         IHandler handler = HandlerMapper.getHandler(inputPayloadBean);
         context.getLogger().log("Invoking handler " + handler.getClass().getName());
         String bodyJson = gson.toJson(handler.handlePayload(inputPayloadBean));
         LambdaProxyResponse lbr = new LambdaProxyResponse();
         lbr.setBody(bodyJson);
         context.getLogger().log("Returning " + gson.toJson(lbr));
         return lbr;
      }
      catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
      {
         throw new JDMException("1001","Error instantiating or running handler: " + e);
      }
   }


}
