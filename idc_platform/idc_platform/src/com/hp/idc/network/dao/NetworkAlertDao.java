package com.hp.idc.network.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.network.entity.NetworkAlertShowEntity;

/**
 * ������ʷ�澯��ѯ�ӿ�
 * 
 * @author Wang Rui
 *
 */
public interface NetworkAlertDao extends GenericDao<NetworkAlertShowEntity, String> {
	
	/**
	 * ������ʷ�澯��ѯ
	 */
	public Page<NetworkAlertShowEntity> queryResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * ���¸澯״̬��Close
	 */
	public NetworkAlertShowEntity saveNetworkAlertShowEntity(NetworkAlertShowEntity networkAlertShowEntity);
}
