/*
 * @(#)ServiceDaoJpa.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.business.dao.ServiceDao;
import com.hp.idc.business.entity.Service;
import com.hp.idc.common.core.JsqlBuildByMap;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa.JsqlBuilder;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa.SqlBuilder;
import com.hp.idc.common.exception.ObjectExistException;
import com.hp.idc.common.util.StringUtil;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:29:01 PM Jul 20, 2011
 * 
 */

@Repository("serviceDao")
public class ServiceDaoJpa extends GenericDaoJpa<Service, Long> implements
		ServiceDao {

	public ServiceDaoJpa() {
		super(Service.class);
	}
	
	public Service getService(long id){
		Service service = getEntityManager().find(Service.class, id);
		service.getProducts().size();
		return service;
	}
	
	@Override
	public void addService(Service s) throws ObjectExistException{
		if(this.exists(s.getId())){
			throw new ObjectExistException("该服务已经存在！");
		}			
		this.getEntityManager().merge(s);
	}
	@Override
	public void updateService(Service s)throws ObjectExistException{
		if(!this.exists(s.getId())){
			throw new ObjectExistException("该服务不存在！");
		}			
		this.getEntityManager().merge(s);
	}
	private String where;
	public String sqlMaker(Map<String, Object> paramMap){
		StringBuffer whereSb=new StringBuffer();
		for(String key: paramMap.keySet()){
			if("id".equals(key)){
				whereSb.append(" and o.id =:id");
			}
			if("name".equals(key)){
				whereSb.append(" and o.name like :name escape '^'");
				String name=(String)paramMap.get(key);
				name=StringUtil.escapeLikeSql(name, "^");
				paramMap.put(key, "%"+name+"%");
			}
			if("serviceValue".equals(key)){
				whereSb.append(" and o.serviceValue = :serviceValue");
			}
			if("parentService".equals(key)){
				whereSb.append(" and o.parentService = :parentService");
			}
		}
		return whereSb.toString();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.idc.business.dao.ServiceDao#queryResultList(java.util.Map)
	 */
	@Override
	public List<Service> queryResultList(Map<String, Object> paramMap) {
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		sortMap.put("id", ORDER_ASC);
		return super.queryResultList(new JsqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from Service o where 1=1");
				sb.append(sqlMaker(paramMap));
				return sb.toString();
			}}, paramMap, sortMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.idc.business.dao.ServiceDao#queryResultPage(java.util.Map,
	 * int, int)
	 */
	@Override
	public Page<Service> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize) {
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		sortMap.put("id", ORDER_ASC);
		where=sqlMaker(paramMap);
		return this.queryResultPage(new SqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from Service o where 1=1");
				sb.append(where);
				return sb.toString();
			}

			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id) from Service o where 1=1");
				sb.append(where);
				return sb.toString();
			}
		}, paramMap, sortMap, pageNo, pageSize);
	}
	
	public List<Service> getDirectChildrenService(Service parentService){
		List<Service> children=new ArrayList<Service>();
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("parentService", parentService);
		children = this.queryResultList(paramMap);
		return children;
	}
	
	public List<Service> getAllChildrenService(Service parentService){
		List<Service> children=new ArrayList<Service>();
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("parentService", parentService);
		children = this.queryResultList(paramMap);
		for(Service service:children){
			List<Service> list=getAllChildrenService(service);
			if(list.size()>0){
				children.addAll(list);
			}
		}
		return children;
	}

}
