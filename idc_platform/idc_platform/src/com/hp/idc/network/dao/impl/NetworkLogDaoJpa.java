package com.hp.idc.network.dao.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.common.util.StringUtil;
import com.hp.idc.network.dao.NetworkLogDao;
import com.hp.idc.network.entity.NetworkLogShowEntity;
/**
 * 网络历史状态查询DAO实现
 * 
 * @author Wang Rui
 * @version 1.0,
 * 
 */
@Repository("networkLogDao")
public class NetworkLogDaoJpa extends GenericDaoJpa<NetworkLogShowEntity, String>
  implements NetworkLogDao{

	public NetworkLogDaoJpa() {
		super(NetworkLogShowEntity.class);
	}
	
	/**
	 *  网络历史状态查询
	 */
	public Page<NetworkLogShowEntity> queryResult(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		String jSql = "";
		String countSql = "";
		StringBuffer jSqlsb = new StringBuffer();
		StringBuffer countSqlsb = new StringBuffer();
		jSqlsb.append("select o from NetworkLogShowEntity o where 1=1 and o.groupname !=  '__Health__'");
		countSqlsb
				.append("select Count(o.monitorid) from NetworkLogShowEntity o where 1=1 and o.groupname !=  '__Health__'");
		for (String key : paramMap.keySet()) {
			if (paramMap.get(key) != null) {
				if (key.equals("monitorname")) {
					jSqlsb.append(" and o.monitorname like :monitorname escape '^'");
					countSqlsb.append(" and o.monitorname like :monitorname escape '^'");
					paramMap.put(key, "%" + StringUtil.escapeLikeSql((String) paramMap.get(key), "^") + "%");
				}
			}
		}
		jSql = jSqlsb.toString();
		countSql = countSqlsb.toString();
		return this.queryResultPage(jSql, countSql, paramMap, sortMap, pageNo,
				pageSize);
	}
}
