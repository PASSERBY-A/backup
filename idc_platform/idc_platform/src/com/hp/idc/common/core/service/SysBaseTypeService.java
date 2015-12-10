package com.hp.idc.common.core.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.entity.SysBaseType;

public interface SysBaseTypeService {

	public void addSysBaseType(SysBaseType code);
	
	public void updateSysBaseType(SysBaseType code);
	
	public void removeSysBaseType(long codeId,long codeType);
	
	public SysBaseType getSysBaseType(long codeId,long codeType);
	
	public List<SysBaseType> getSysBaseTypeByCodeType(long codeType);
	
	public Page<SysBaseType> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap,
			int pageNo, int pageSize);
}
