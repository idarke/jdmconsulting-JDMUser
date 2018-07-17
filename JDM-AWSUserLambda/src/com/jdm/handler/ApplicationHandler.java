package com.jdm.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.google.gson.Gson;
import com.jdm.beans.ApplicationBean;
import com.jdm.beans.InputPayloadBean;
import com.jdm.common.Common;
import com.jdm.dao.ApplicationDao;
import com.jdm.exception.JDMException;

public class ApplicationHandler implements IHandler
{
   @Override
   public Object handlePayload(InputPayloadBean inputPayloadBean)
   {
      switch (inputPayloadBean.getAction())
      {
         case "GETALLAPPS":
            Common.context.getLogger().log("ApplicationHandler GETALLAPPS");
            return new ApplicationDao().getAllApps();
         case "GETAPP":
            return getApp(inputPayloadBean.getParams());             
         default:
            throw new JDMException("1002","Application action " + inputPayloadBean.getAction() + " not implemented");
      }
   }
   
   private Object getApp(String params)
   {
      Common.context.getLogger().log("getApp params=" + params);
      try
      {
         ApplicationBean appBean = extractApplicationBean(params);
         ApplicationDao dao = new ApplicationDao();
         appBean = dao.getApplication(appBean.getId());
         return appBean;
      }
      catch (UnsupportedEncodingException e)
      {
         throw new JDMException("1003","Error getting application " + params + ": " + e);
      }
   }
   
   private ApplicationBean extractApplicationBean(String params) throws UnsupportedEncodingException
   {
      String decoded = URLDecoder.decode(params,"UTF-8");
      Gson gson = new Gson();
      ApplicationBean appBean = gson.fromJson(decoded,ApplicationBean.class);
      return appBean;
   }
}
