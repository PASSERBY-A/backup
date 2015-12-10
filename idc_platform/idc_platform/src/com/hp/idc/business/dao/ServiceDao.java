/*
 * @(#)Service.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.Service;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:06:40 AM Jul 18, 2011
 * 
 */

public interface ServiceDao extends GenericDao<Service, Long> {
	
	public Service getService(long id);
	
	public void addService(Service s) throws ObjectExistException;
	
	public void updateService(Service s)throws ObjectExistException;
	
	public List<Service> queryResultList(Map<String, Object> paramMap);
	
	public Page<Service> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize);

	public List<Service> getDirectChildrenService(Service parentService);
	
	public List<Service> getAllChildrenService(Service parentService);
}
