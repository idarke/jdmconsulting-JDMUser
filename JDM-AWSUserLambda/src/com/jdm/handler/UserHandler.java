package com.jdm.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.google.gson.Gson;
import com.jdm.beans.InputPayloadBean;
import com.jdm.beans.UserBean;
import com.jdm.common.Common;
import com.jdm.dao.UserDao;
import com.jdm.exception.JDMException;

public class UserHandler implements IHandler
{
   @Override
   public Object handlePayload(InputPayloadBean inputPayloadBean)
   {
      switch (inputPayloadBean.getAction())
      {
         case "GETALL":
            return new UserDao().getAllUsers();
         case "SAVE":
            return saveUser(inputPayloadBean.getParams());  
         case "DELETE":
            return deleteUser(inputPayloadBean.getParams());               
         default:
            throw new JDMException("1007","User action " + inputPayloadBean.getAction() + " not implemented");
      }
   }
   
   private Object saveUser(String params)
   {
      Common.context.getLogger().log("saveUser params=" + params);
      try
      {
         UserBean userBean = extractUserBean(params);
         UserDao dao = new UserDao();
         if (userBean.getId() == -1)
         {
            dao.insert(userBean);
         }
         else
         {
            dao.update(userBean);
         }
      }
      catch (UnsupportedEncodingException e)
      {
         throw new JDMException("1008","Error saving user" + e);
      }
      return "Success";
   }

   private UserBean extractUserBean(String params) throws UnsupportedEncodingException
   {
      String decoded = URLDecoder.decode(params,"UTF-8");
      Gson gson = new Gson();
      UserBean userBean = gson.fromJson(decoded,UserBean.class);
      return userBean;
   }
   
   private Object deleteUser(String params)
   {
      Common.context.getLogger().log("deleteUser params=" + params);
      try
      {
         UserBean userBean = extractUserBean(params);
         new UserDao().delete(userBean);
      }
      catch (UnsupportedEncodingException e)
      {
         throw new JDMException("1009","Error deleting user" + e);
      }
      return "Success";
   }
}
