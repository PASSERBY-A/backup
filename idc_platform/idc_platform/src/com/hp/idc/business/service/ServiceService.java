/*
 * @(#)ServiceService.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.Service;
import com.hp.idc.business.entity.ServiceResource;
import com.hp.idc.business.entity.ServiceResourcePK;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:38:57 PM Jul 20, 2011
 * 
 */

public interface ServiceService {

	public List<Service> queryResultList(Map<String, Object> paramMap);
	
	public Page<Service> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize);
	
	public Service queryService(long id);
	
	public void addService(Service service) throws ObjectExistException ;
	
	public void updateService(Service service) throws ObjectExistException;
	
	public boolean removeService(long id);
	
	public boolean removeServices(String ids);
	
	public void saveServiceResource(ServiceResource serviceResource);
	
	public ServiceResource queryServiceResource(ServiceResourcePK id);
	
	public void removeServiceResource(ServiceResourcePK id);
	
	public List<ServiceResource> queryServiceResourceByResModelId(String resModelId);
	
	public List<ServiceResource> queryServiceResourceList(Map<String,Object> paramMap);
	
	public Page<ServiceResource> queryServiceResourcePage(
			Map<String,Object> paramMap, int pageNo, int pageSize);
}
