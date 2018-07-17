package com.jdm.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jdm.beans.InputPayloadBean;
import com.jdm.beans.IotSaveBean;
import com.jdm.common.Common;
import com.jdm.dao.IotSaveDao;
import com.jdm.exception.JDMException;

public class IotHandler implements IHandler
{
   @Override
   public Object handlePayload(InputPayloadBean inputPayloadBean)
   {
      Common.context.getLogger().log("handlePayload guid=" + inputPayloadBean.getGuid());
      IotSaveBean iotSaveBean = new IotSaveBean();
      iotSaveBean.setGuid(inputPayloadBean.getGuid());
      iotSaveBean.setJson(inputPayloadBean.getParams());
      
      switch (inputPayloadBean.getAction())
      {
         case "SAVEIOT":
            return saveIot(iotSaveBean);  
         case "GETIOT":
            return getIot(iotSaveBean);              
         case "DELIOT":
            return deleteIot(iotSaveBean);    
         case "GETPROJECTS":
            return getProjectIds(iotSaveBean, true); 
         case "GETOTHERPROJECTS":
            return getProjectIds(iotSaveBean, false);             
         case "GETALLPROJECTS":
            return getAllProjects();             
         default:
            throw new JDMException("1077","IoT action " + inputPayloadBean.getAction() + " not implemented");
      }
   }
   
   private Object saveIot(IotSaveBean iotSaveBean)
   {
      Common.context.getLogger().log("saveIot guid=" + iotSaveBean.getGuid());
      try
      {
         IotSaveDao dao = new IotSaveDao();
         IotSaveBean oldbean = dao.getIotSaveBean(iotSaveBean.getGuid());
         if (oldbean == null)
         {
            dao.insert(iotSaveBean);
         }
         else
         {
            dao.update(iotSaveBean);
         }
      }
      catch (Exception e)
      {
         throw new JDMException("1008","Error saving iotsave" + e);
      }
      return "Success";
   }

   private Object deleteIot(IotSaveBean iotSaveBean)
   {
      Common.context.getLogger().log("deleteIot guid=" + iotSaveBean.getGuid());
      try
      {
         new IotSaveDao().delete(iotSaveBean);
      }
      catch (Exception e)
      {
         throw new JDMException("1009","Error deleting iotsave" + e);
      }
      return "Success";
   }
   
   private Object getIot(IotSaveBean iotSaveBean)
   {
      Common.context.getLogger().log("getIot guid=" + iotSaveBean.getGuid());
      try
      {
         iotSaveBean = new IotSaveDao().getIotSaveBean(iotSaveBean.getGuid());
      }
      catch (Exception e)
      {
         throw new JDMException("1009","Error deleting iotsave" + e);
      }
      if (iotSaveBean == null || iotSaveBean.getJson() == null)
      {
         throw new JDMException("1019","Saved project is not found");
      }
      return iotSaveBean.getJson();
   }
   
   private Object getProjectIds(IotSaveBean iotSaveBean, boolean includeMe)
   {
      Common.context.getLogger().log("getIot getProjectIds=" + iotSaveBean.getGuid());
      String[] tokens = iotSaveBean.getGuid().split("~");
      String guid = tokens[0];
      List<String> projectIds = new ArrayList<String>();
      try
      {
         List<IotSaveBean> results = new IotSaveDao().getAll();
         for (IotSaveBean iotb : results)
         {
            String[] temptokens = iotb.getGuid().split("~");
            if (temptokens.length == 2)
            {
               if (includeMe && temptokens[0].equals(guid))
               {
                  projectIds.add(temptokens[1]);
               }
               else if (!includeMe && !temptokens[0].equals(guid))
               {
                  projectIds.add(temptokens[1]);
               }               
            }
         }
      }
      catch (Exception e)
      {
         throw new JDMException("1019","Error getting project ids" + e);
      }
      Gson gson = new Gson();
      return gson.toJson(projectIds);
   }
   
   private Object getAllProjects()
   {
      Common.context.getLogger().log("getIot getAllProjectIds");
      List<String> projectIds = new ArrayList<String>();
      try
      {
         List<IotSaveBean> results = new IotSaveDao().getAll();
         for (IotSaveBean iotb : results)
         {
            projectIds.add(iotb.getGuid());
         }
      }
      catch (Exception e)
      {
         throw new JDMException("1119","Error getting all project ids" + e);
      }
      Gson gson = new Gson();
      return gson.toJson(projectIds);
   }   
}
