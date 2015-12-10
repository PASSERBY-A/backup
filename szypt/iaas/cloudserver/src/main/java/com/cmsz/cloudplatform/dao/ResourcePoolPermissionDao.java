package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.hp.core.dao.GenericDao;

public interface ResourcePoolPermissionDao extends GenericDao<ResourcePoolPermission, Long> {
	
	public List<ResourcePoolPermission> getResourcePoolPermissions(Integer ownerType, List<String> ownerIds);
	
	public ResourcePoolPermission getPool(Integer type,String value);
}
