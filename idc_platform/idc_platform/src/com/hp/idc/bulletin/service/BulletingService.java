package com.hp.idc.bulletin.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.bulletin.entity.BulletinInfo;
import com.hp.idc.common.core.bo.Page;



public interface BulletingService {

	

	public void addBulletinInfo(BulletinInfo bulletinInfo) ;
	
	public void updateBulletinInfo(BulletinInfo bulletinInfo);
	
	public Page<BulletinInfo> getBulletinList (Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap,int pageNo, int pageSize);
	  
	public BulletinInfo getBulletinInfo(Long bulletinId);
	  
	public void deleteBulletinInfoBySql(Long bulletinId);
}
