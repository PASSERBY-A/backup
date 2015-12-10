package com.hp.idc.network.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.network.entity.NetworkAlertShowEntity;
import com.hp.idc.network.entity.NetworkLogShowEntity;

/**
 * ÍøÂç¸æ¾¯½Ó¿Ú
 * 
 * @author Wang Rui
 *
 */
public interface NetworkService {
	
	public Page<NetworkLogShowEntity> queryLogResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public Page<NetworkAlertShowEntity> queryAlertResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
	
	public NetworkAlertShowEntity saveNetworkAlertShowEntity(
			NetworkAlertShowEntity networkAlertShowEntity);
}
