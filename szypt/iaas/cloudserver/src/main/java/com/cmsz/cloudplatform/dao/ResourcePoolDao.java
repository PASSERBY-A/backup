package com.cmsz.cloudplatform.dao;

import com.cmsz.cloudplatform.model.ResourcePool;
import com.hp.core.dao.GenericDao;

public interface ResourcePoolDao extends GenericDao<ResourcePool,Integer> {
	/**
	 * 查询配置的二级资源池ZONE ID
	 * @param resourcePoolId 一级资源池ID
	 * @return ZONE id 数组
	 */
	String[] listSubResource(String resourcePoolId);
	
	/**
	 * 通过Zone ID 查询一级资源池ID
	 * @param zoneId   ZONE id
	 * @return 关联的一级资源池ID
	 */
	String getResourcePool(String zoneId);
	
	/**
	 * 配置一级资源池与ZONE关联
	 * @param resourcePoolId
	 * @param zoneId
	 * @param username TODO
	 */
	void configZone(String resourcePoolId,String zoneId, String username);
	
	/**
	 * 删除关联ZONE
	 */
	void removeZone(String resourcePoolId,String zoneId);

	int getHostCount(String type);
}
