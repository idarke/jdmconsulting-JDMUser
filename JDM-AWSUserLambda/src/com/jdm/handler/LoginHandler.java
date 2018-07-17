package com.jdm.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.google.gson.Gson;
import com.jdm.beans.InputPayloadBean;
import com.jdm.beans.UserBean;
import com.jdm.common.Common;
import com.jdm.dao.UserDao;
import com.jdm.exception.JDMException;

public class LoginHandler implements IHandler
{
   @Override
   public Object handlePayload(InputPayloadBean inputPayloadBean)
   {
      switch (inputPayloadBean.getAction())
      {
         case "LOGIN":
            return doLogin(inputPayloadBean.getParams());  
         default:
            throw new JDMException("1005","Login action " + inputPayloadBean.getAction() + " not implemented");
      }
   }
   
   private Object doLogin(String params)
   {
      Common.context.getLogger().log("doLogin params=" + params);
      try
      {
         UserBean userBean = extractUserBean(params);
         UserDao dao = new UserDao();
         userBean = dao.validate(userBean);
         return userBean;
      }
      catch (UnsupportedEncodingException e)
      {
         throw new JDMException("1006","Error validating user " + params + ": " + e);
      }
   }
   
   private UserBean extractUserBean(String params) throws UnsupportedEncodingException
   {
      String decoded = URLDecoder.decode(params,"UTF-8");
      Gson gson = new Gson();
      UserBean userBean = gson.fromJson(decoded,UserBean.class);
      return userBean;
   }
   
}
