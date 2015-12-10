package com.cmsz.cloudplatform.service;

import java.util.Map;

import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.cmsz.cloudplatform.model.request.ResourcePoolPermissionRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.ResourcePoolPermissionVO;
import com.hp.config.model.DbConfig;
import com.hp.core.service.GenericManager;

public interface ResourcePoolPermissionManager extends GenericManager<ResourcePoolPermission, Long> {

	/**
	 * 
	 * @return
	 */
	public ListResponse<DbConfig> getResourcePoolRelatedObjectType();

	/**
	 * save resource pool permission
	 * 
	 * @param request
	 * @return
	 */
	public EntityResponse<ResourcePoolPermission> saveResourcePoolPermission(ResourcePoolPermissionRequest request);

	/**
	 * get resource pool permission
	 * 
	 * @param request
	 * @param requestParams
	 * @return
	 */
	public ListResponse<ResourcePoolPermissionVO> getResourcePoolPermissions(ResourcePoolPermissionRequest request,
			Map<String, Object[]> requestParams);
	
	public ResourcePoolPermission getPool(Integer type,String value);
}
