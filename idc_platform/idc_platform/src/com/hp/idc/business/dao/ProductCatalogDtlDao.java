/*
 * @(#)ProductCatalogDtl.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.entity.ProductCatalogDtlPK;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 4:45:11 PM Jul 21, 2011
 * 
 */

public interface ProductCatalogDtlDao extends GenericDao<ProductCatalogDtl, ProductCatalogDtlPK>{

public List<ProductCatalogDtl> queryResultList(Map<String, Object> paramMap);
	
	public Page<ProductCatalogDtl> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize);
	
}
