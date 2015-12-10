package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.ResourcePoolDao;
import com.cmsz.cloudplatform.model.ResourcePool;
import com.hp.core.dao.hibernate.GenericDaoImpl;
@Repository("resourcePoolDao")
public class ResourcePoolDaoImpl extends GenericDaoImpl<ResourcePool, Integer> implements ResourcePoolDao  {

	public ResourcePoolDaoImpl(Class<ResourcePool> persistentClass) {
		super(persistentClass);
	}
	
	public ResourcePoolDaoImpl(){
		super(ResourcePool.class);
	}

	@Override
	public String[] listSubResource(String resourcePoolId) {
		String sql = "SELECT T.ZONE_ID FROM T_ResourcePool_Zone_Relations T WHERE T.RESOURCEPOOL_ID = ?"; 
		Query query = this.getSession().createSQLQuery(sql);
		query.setString(0, resourcePoolId);
		List<String> list = query.list();
		if(list!=null){
			String[] zones = new String[list.size()];
			list.toArray(zones);
			return zones;
		}else{
			return null;
		}
	}

	@Override
	public String getResourcePool(String zoneId) {
		String sql = "SELECT T.Resourcepool_Id FROM T_ResourcePool_Zone_Relations T WHERE T.Zone_Id = :zoneId"; 
		Query query = this.getSession().createSQLQuery(sql);
		query.setString("zoneId", zoneId);
		List<String> list = query.list();
		if(list!=null&& list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void configZone(String resourcePoolId, String zoneId, String username) {
		String sql = "INSERT INTO T_ResourcePool_Zone_Relations(ID,Resourcepool_Id,Zone_Id,Created_By,modified_by) VALUES(T_ResourcePool_Zone_Rel_SEQ.Nextval,?,?,?,?)";		
		Query query =  this.getSession().createSQLQuery(sql);
		query.setString(0,resourcePoolId);
		query.setString(1,zoneId);
		query.setString(2,username);
		query.setString(3,username);
		query.executeUpdate();
	}
	
	public void removeZone(String resourcePoolId,String zoneId){
		String sql = "DELETE  T_RESOURCEPOOL_ZONE_RELATIONS T WHERE T.RESOURCEPOOL_ID = ? AND T.ZONE_ID = ?";
		Query query =  this.getSession().createSQLQuery(sql);
		query.setString(0,resourcePoolId);
		query.setString(1,zoneId);
		query.executeUpdate();
	}

	@Override
	public int getHostCount(String type) {
		String sql = "SELECT count(0) FROM t_dim_resource T WHERE T.type=:type";
		Query query = this.getSession().createSQLQuery(sql);
		query.setString("type", type);
		int hostCount = ((Number)query.uniqueResult()).intValue();
		return hostCount;
	}
	

}

