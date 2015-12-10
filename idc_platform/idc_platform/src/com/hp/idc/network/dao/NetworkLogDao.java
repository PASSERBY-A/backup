package com.hp.idc.network.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.network.entity.NetworkLogShowEntity;

/**
 * 网络历史状态查询接口
 * 
 * @author Wang Rui
 *
 */
public interface NetworkLogDao extends GenericDao<NetworkLogShowEntity, String> {
	
	
	/**
	 * 网络历史状态查询
	 */
	public Page<NetworkLogShowEntity> queryResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

}
