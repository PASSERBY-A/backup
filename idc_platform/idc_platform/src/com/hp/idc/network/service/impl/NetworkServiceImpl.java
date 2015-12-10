package com.hp.idc.network.service.impl;

import java.util.LinkedHashMap;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.network.dao.NetworkAlertDao;
import com.hp.idc.network.dao.NetworkLogDao;
import com.hp.idc.network.entity.NetworkAlertShowEntity;
import com.hp.idc.network.entity.NetworkLogShowEntity;
import com.hp.idc.network.service.NetworkService;
/**
 * ÍøÂç¸æ¾¯½Ó¿Ú
 * 
 * @author Wang Rui
 *
 */
@Service("networkService")
public class NetworkServiceImpl implements NetworkService{
	
	@Resource
	private NetworkLogDao networkLogDao;
	
	@Resource
	private NetworkAlertDao networkAlertDao;
	
	public Page<NetworkLogShowEntity> queryLogResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return networkLogDao.queryResult(paramMap, sortMap, pageNo, pageSize);
	}

	public Page<NetworkAlertShowEntity> queryAlertResult(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return networkAlertDao.queryResult(paramMap, sortMap, pageNo, pageSize);
	}
	
	public NetworkAlertShowEntity saveNetworkAlertShowEntity(
			NetworkAlertShowEntity networkAlertShowEntity) {
		return networkAlertDao
				.saveNetworkAlertShowEntity(networkAlertShowEntity);
	}
  
}
