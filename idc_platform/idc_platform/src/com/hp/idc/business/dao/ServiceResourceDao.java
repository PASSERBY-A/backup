package com.hp.idc.business.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.ServiceResource;
import com.hp.idc.business.entity.ServiceResourcePK;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;

public interface ServiceResourceDao extends
		GenericDao<ServiceResource, ServiceResourcePK> {
	
	public List<ServiceResource> queryResultList(Map<String, Object> paramMap,LinkedHashMap<String, String> sortMap);
	
	public Page<ServiceResource> queryResultPage(Map<String, Object> paramMap,LinkedHashMap<String, String> sortMap,
			int pageNo, int pageSize);

}
