package com.hp.idc.common.core.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.SysBaseTypeDao;
import com.hp.idc.common.core.entity.SysBaseType;
import com.hp.idc.common.core.entity.SysBaseTypePK;
import com.hp.idc.common.core.service.SysBaseTypeService;

@Service("sysBaseTypeService")
public class SysBaseTypeServiceImpl implements SysBaseTypeService {
	
	@Resource
	private SysBaseTypeDao sysBaseTypeDao;
	
	
	public void addSysBaseType(SysBaseType code){
		if(sysBaseTypeDao.exists(code.getId())){
		}
		sysBaseTypeDao.save(code);
	}
	
	public void updateSysBaseType(SysBaseType code){
		if(!sysBaseTypeDao.exists(code.getId())){
		}
		sysBaseTypeDao.save(code);
	}
	
	public void removeSysBaseType(long codeId,long codeType){
		SysBaseTypePK id=new SysBaseTypePK();
		id.setId(codeId);
		id.setType(codeType);
		sysBaseTypeDao.remove(id);
	}
	
	public SysBaseType getSysBaseType(long codeId,long codeType){
		SysBaseTypePK id=new SysBaseTypePK();
		id.setId(codeId);
		id.setType(codeType);
		return sysBaseTypeDao.get(id);
	}
	
	public List<SysBaseType> getSysBaseTypeByCodeType(long codeType){
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("type", codeType);
		return sysBaseTypeDao.queryResultList(paramMap, new LinkedHashMap<String, String> ());
	}
	
	public Page<SysBaseType> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap,
			int pageNo, int pageSize){
		return sysBaseTypeDao.queryResultPage(paramMap, sortMap, pageNo, pageSize);
	}

	public SysBaseTypeDao getSysBaseTypeDao() {
		return sysBaseTypeDao;
	}

	public void setSysBaseTypeDao(SysBaseTypeDao sysBaseTypeDao) {
		this.sysBaseTypeDao = sysBaseTypeDao;
	}
	
	
	

}
