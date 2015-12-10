/*
 * @(#)ProductDaoJpa.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao.jpa;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.business.dao.ProductDao;
import com.hp.idc.business.entity.Product;
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
 * @version 1.0, 1:23:39 PM Jul 20, 2011
 * 
 */

@Repository("productDao")
public class ProductDaoJpa extends GenericDaoJpa<Product, Long> implements
		ProductDao {

	public ProductDaoJpa() {
		super(Product.class);
	}

	public void saveProductPersist(Product p){
		this.getEntityManager().persist(p);
	}
	@Override
	public void addProduct(Product p) throws ObjectExistException{
		if(this.exists(p.getId())){
			throw new ObjectExistException("该产品已经存在！");
		}else	
			this.getEntityManager().merge(p);
	}
	@Override
	public void updateProduct(Product p)throws ObjectExistException{
		if(!this.exists(p.getId())){
			throw new ObjectExistException("该产品不存在！");
		}else			
			this.getEntityManager().merge(p);
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
		}
		return whereSb.toString();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.idc.business.dao.ProductDao#queryResultList(java.util.Map)
	 */
	@Override
	public List<Product> queryResultList(Map<String, Object> paramMap) {
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		sortMap.put("id", ORDER_ASC);
		return super.queryResultList(new JsqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from Product o where 1=1");
				sb.append(sqlMaker(paramMap));
				return sb.toString();
			}}, paramMap, sortMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hp.idc.business.dao.ProductDao#queryResultPage(java.util.Map,
	 * int, int)
	 */
	@Override
	public Page<Product> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize) {
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		sortMap.put("id", ORDER_ASC);
		where=sqlMaker(paramMap);
		return this.queryResultPage(new SqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from Product o where 1=1");
				sb.append(where);
				return sb.toString();
			}

			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id) from Product o where 1=1");
				sb.append(where);
				return sb.toString();
			}
		}, paramMap, sortMap, pageNo, pageSize);
	}

}
