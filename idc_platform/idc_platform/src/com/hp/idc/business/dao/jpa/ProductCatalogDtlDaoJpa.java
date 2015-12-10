/*
 * @(#)ProductCatalogDtlDaoJpa.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao.jpa;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.business.dao.ProductCatalogDtlDao;
import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.entity.ProductCatalogDtlPK;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.common.util.StringUtil;


/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 4:47:11 PM Jul 21, 2011
 * 
 */

@Repository("productCatalogDtlDao")
public class ProductCatalogDtlDaoJpa extends
		GenericDaoJpa<ProductCatalogDtl, ProductCatalogDtlPK> implements ProductCatalogDtlDao {

	public ProductCatalogDtlDaoJpa() {
		super(ProductCatalogDtl.class);
	}
	private String where;
	private String sqlMaker(Map<String, Object> paramMap){
		StringBuffer whereBuffer=new StringBuffer();
		for(String key : paramMap.keySet()){
			
			if("catalog".equals(key)){
				whereBuffer.append(" and o.id.catalog=:catalog");
			}
			if("name".equals(key)){
				whereBuffer.append(" and o.id.product.name like :name escape '^'");
				String name=(String)paramMap.get(key);
				name=StringUtil.escapeLikeSql(name, "^");
				paramMap.put(key, "%"+name+"%");
			}
			if("id".equals(key)){
				whereBuffer.append(" and o.id.product.id =:id");
			}
		}
		return whereBuffer.toString();
	}
	
	@Override
	public List<ProductCatalogDtl> queryResultList(Map<String, Object> paramMap) {
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		sortMap.put("o.id", ORDER_ASC);
		return super.queryResultList(new JsqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from ProductCatalogDtl o left join fetch o.id.product p  where 1=1");
				sb.append(sqlMaker(paramMap));
				return sb.toString();
			}}, paramMap, sortMap);
			
	}

	@Override
	public Page<ProductCatalogDtl> queryResultPage(
			Map<String, Object> paramMap, int pageNo, int pageSize) {
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		sortMap.put("o.id", ORDER_ASC);
		where=sqlMaker(paramMap);
		return this.queryResultPage(new SqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from ProductCatalogDtl o left join fetch o.id.product p where 1=1");
				sb.append(where);
				return sb.toString();
			}

			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id.product.id) from ProductCatalogDtl o left join o.id.product p where 1=1");
				sb.append(where);
				return sb.toString();
			}
		}, paramMap, sortMap, pageNo, pageSize);
	}

}
