package com.jdm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdm.beans.IotSaveBean;
import com.jdm.common.Common;
import com.jdm.common.PropertiesHelper;
import com.jdm.common.SqlHelper;
import com.jdm.exception.JDMException;

public class IotSaveDao
{
   public void insert( IotSaveBean iotSaveBean )
   {
      Connection conn = ConnectionFactory.getConnection(); 
      PreparedStatement ps = null;
      try
      {
         ps = conn.prepareStatement(PropertiesHelper.getProperty("INSERT_IOTSAVE"));
         ps.setString(1,iotSaveBean.getGuid());
         ps.setString(2,iotSaveBean.getJson());
         ps.execute();
      }
      catch (Exception e)
      {
         throw new JDMException("1105","Error inserting iotsave", e);
      }
      finally
      {
         SqlHelper.close(ps);
      }
   }
   
   public void update( IotSaveBean iotSaveBean )
   {
      Connection conn = ConnectionFactory.getConnection(); 
      PreparedStatement ps = null;
      try
      {
         ps = conn.prepareStatement(PropertiesHelper.getProperty("UPDATE_IOTSAVE"));
         ps.setString(1,iotSaveBean.getJson());
         ps.setString(2,iotSaveBean.getGuid());
         ps.execute();
      }
      catch (Exception e)
      {
         throw new JDMException("1106","Error updating iotsave", e);
      }
      finally
      {
         SqlHelper.close(ps);
      }
   }

   public void delete( IotSaveBean iotSaveBean )
   {
      Connection conn = ConnectionFactory.getConnection(); 
      PreparedStatement ps = null;
      try
      {
         ps = conn.prepareStatement(PropertiesHelper.getProperty("DELETE_IOTSAVE"));
         ps.setString(1,iotSaveBean.getGuid()); 
         ps.execute();
      }
      catch (Exception e)
      {
         throw new JDMException("1107","Error deleting iotsave", e);
      }
      finally
      {
         SqlHelper.close(ps);
      }
   }
   
   public IotSaveBean getIotSaveBean(String guid)
   {
      Common.context.getLogger().log("IotSaveDao getIotSaveBean");
      Connection conn = ConnectionFactory.getConnection(); 
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         ps = conn.prepareStatement(PropertiesHelper.getProperty("SELECT_IOTSAVE"));
         ps.setString(1,guid); 
         rs = ps.executeQuery();
         if (rs.next())
         {
            return buildIotSaveBean(rs);
         }
      }
      catch (Exception e)
      {
         throw new JDMException("1104","Error reading iotsave", e);
      }
      finally
      {
         SqlHelper.close(ps);
         SqlHelper.close(rs);
      }
      return null;
   }
   
   public List<IotSaveBean> getAll()
   {
      List<IotSaveBean> results = new ArrayList<IotSaveBean>();
      Common.context.getLogger().log("IotSaveDao getAll");
      Connection conn = ConnectionFactory.getConnection(); 
      PreparedStatement ps = null;
      ResultSet rs = null;
      try
      {
         ps = conn.prepareStatement(PropertiesHelper.getProperty("SELECT_IOTALL"));
         rs = ps.executeQuery();
         while (rs.next())
         {
            results.add(buildIotSaveBean(rs));
         }
      }
      catch (Exception e)
      {
         throw new JDMException("1114","Error reading iotsave", e);
      }
      finally
      {
         SqlHelper.close(ps);
         SqlHelper.close(rs);
      }
      return results;
   } 
   
   private IotSaveBean buildIotSaveBean( ResultSet rs ) throws SQLException
   {
      IotSaveBean iotSaveBean = new IotSaveBean();
      iotSaveBean.setGuid( rs.getString("guid") );
      iotSaveBean.setJson( rs.getString("json") );
      return iotSaveBean;
   }
   
}
