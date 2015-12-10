package com.hp.idc.network.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.network.entity.NetworkLogShowEntity;

/**
 * ������ʷ״̬��ѯ�ӿ�
 * 
 * @author Wang Rui
 *
 */
public interface NetworkLogDao extends GenericDao<NetworkLogShowEntity, String> {
	
	
	/**
	 * ������ʷ״̬��ѯ
	 */
	public Page<NetworkLogShowEntity> queryResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

}
