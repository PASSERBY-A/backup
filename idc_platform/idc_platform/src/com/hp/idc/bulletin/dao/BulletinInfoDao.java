package com.hp.idc.bulletin.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.bulletin.entity.BulletinInfo;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;

public interface BulletinInfoDao extends GenericDao<BulletinInfo, Long> {
	
	public Page<BulletinInfo> getBulletinList(
			Map<String,Object> paramMap,LinkedHashMap<String,String> sortMap,int pageNo, int pageSize) ;

}
