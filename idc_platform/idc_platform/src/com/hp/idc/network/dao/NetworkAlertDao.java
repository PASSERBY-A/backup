package com.hp.idc.network.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.network.entity.NetworkAlertShowEntity;

/**
 * 网络历史告警查询接口
 * 
 * @author Wang Rui
 *
 */
public interface NetworkAlertDao extends GenericDao<NetworkAlertShowEntity, String> {
	
	/**
	 * 网络历史告警查询
	 */
	public Page<NetworkAlertShowEntity> queryResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * 更新告警状态到Close
	 */
	public NetworkAlertShowEntity saveNetworkAlertShowEntity(NetworkAlertShowEntity networkAlertShowEntity);
}
