package com.hp.idc.bulletin.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.hp.idc.bulletin.dao.BulletinInfoDao;
import com.hp.idc.bulletin.entity.BulletinInfo;
import com.hp.idc.bulletin.service.BulletingService;
import com.hp.idc.common.core.bo.Page;


@Service("bulletingService")
public class BulletinServiceImpl implements BulletingService {

	@Resource
	private BulletinInfoDao bulletinInfoDao;


	public void deleteBulletinInfoBySql(Long bulletinId) {

		bulletinInfoDao.remove(bulletinId);
	}
	


	public Page<BulletinInfo> getBulletinList(Map<String,Object> paramMap,LinkedHashMap<String,String> sortMap,int pageNo, int pageSize) {
		return bulletinInfoDao.getBulletinList(paramMap, sortMap,pageNo, pageSize);
	}

	public BulletinInfo getBulletinInfo(Long bulletinId) {

		return bulletinInfoDao.get(bulletinId);
	}


	public void addBulletinInfo(BulletinInfo bulletinInfo) {

		bulletinInfo.setCreatedDate(new Date());

		bulletinInfoDao.save(bulletinInfo);
	}
	
	public void updateBulletinInfo(BulletinInfo bulletinInfo) {
		bulletinInfoDao.save(bulletinInfo);
	}

	@Required
	public BulletinInfoDao getBulletinInfoDao() {
		return bulletinInfoDao;
	}

	public void setBulletinInfoDao(BulletinInfoDao bulletinInfoDao) {
		this.bulletinInfoDao = bulletinInfoDao;
	}


}
