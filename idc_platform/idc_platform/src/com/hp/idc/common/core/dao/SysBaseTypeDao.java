package com.hp.idc.common.core.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.entity.SysBaseType;
import com.hp.idc.common.core.entity.SysBaseTypePK;

public interface SysBaseTypeDao extends GenericDao<SysBaseType, SysBaseTypePK> {

	public List<SysBaseType> queryResultList(
			Map<String, Object> paramMap, LinkedHashMap<String, String> sortMap);
	
	public Page<SysBaseType> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
