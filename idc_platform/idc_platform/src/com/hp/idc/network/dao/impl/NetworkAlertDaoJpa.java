package com.hp.idc.network.dao.impl;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.common.util.StringUtil;
import com.hp.idc.network.dao.NetworkAlertDao;
import com.hp.idc.network.entity.NetworkAlertShowEntity;

/**
 * 网络历史告警查询DAO实现
 * 
 * @author Wang Rui
 * @version 1.0,
 * 
 */
@Repository("networkAlertDao")
public class NetworkAlertDaoJpa extends
		GenericDaoJpa<NetworkAlertShowEntity, String> implements
		NetworkAlertDao {

	public NetworkAlertDaoJpa() {
		super(NetworkAlertShowEntity.class);
	}

	/**
	 * 告警查询
	 */
	public Page<NetworkAlertShowEntity> queryResult(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		String jSql = "";
		String countSql = "";
		StringBuffer jSqlsb = new StringBuffer();
		StringBuffer countSqlsb = new StringBuffer();
		jSqlsb.append("select o from NetworkAlertShowEntity o where 1=1");
		countSqlsb
				.append("select Count(o.state) from NetworkAlertShowEntity o where 1=1");
		for (String key : paramMap.keySet()) {
			if (paramMap.get(key) != null) {
				if (key.equals("monitorName")) {
					jSqlsb.append(" and o.monitorName like :monitorName escape '^'");
					countSqlsb.append(" and o.monitorName like :monitorName escape '^'");
					paramMap.put(key, "%" + StringUtil.escapeLikeSql((String) paramMap.get(key), "^") + "%");
				}
				if (key.equals("severity")) {
					jSqlsb.append(" and o.severity like :severity escape '^'");
					countSqlsb.append(" and o.severity like :severity escape '^'");
					paramMap.put(key, "%" + StringUtil.escapeLikeSql((String) paramMap.get(key), "^") + "%");
				}
				if (key.equals("eventStatus")) {
					jSqlsb.append(" and o.eventStatus like :eventStatus escape '^'");
					countSqlsb.append(" and o.eventStatus like :eventStatus escape '^'");
					paramMap.put(key, "%" + StringUtil.escapeLikeSql((String) paramMap.get(key), "^") + "%");
				}
				if (key.equals("eventConfirmer")) {
					jSqlsb.append(" and o.eventConfirmer like :eventConfirmer");
					countSqlsb.append(" and o.eventConfirmer like :eventConfirmer");
					paramMap.put(key, "%" + StringUtil.escapeLikeSql((String) paramMap.get(key), "^") + "%");
				}
			}
		}
		jSql = jSqlsb.toString();
		countSql = countSqlsb.toString();
		return this.queryResultPage(jSql, countSql, paramMap, sortMap, pageNo,
				pageSize);
	}

	/**
	 * 更新告警状态到Close
	 */
	public NetworkAlertShowEntity saveNetworkAlertShowEntity(
			NetworkAlertShowEntity networkAlertShowEntity) {
		EntityManager em = getEntityManager();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String jSql = "select o from NetworkAlertShowEntity o where o.timed = to_date('"
					+ sdf.format(networkAlertShowEntity.getTimed())
					+ "', 'yyyy-MM-dd HH24:mi:ss') and monitorName='"
					+ networkAlertShowEntity.getMonitorName()
					+ "' and groupName='"
					+ networkAlertShowEntity.getGroupName() + "'";
			List<NetworkAlertShowEntity> sqList = em.createQuery(jSql,
					NetworkAlertShowEntity.class).getResultList();
			if (sqList != null && sqList.size() == 1) {
				NetworkAlertShowEntity insertEntity = sqList.get(0);
				insertEntity.setEventStatus(networkAlertShowEntity.getEventStatus());
				insertEntity.setEventConfirmer(networkAlertShowEntity
						.getEventConfirmer());
				this.save(insertEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return networkAlertShowEntity;
	}

}
