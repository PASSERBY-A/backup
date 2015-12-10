/*
 * @(#)ProductCatalogDao.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:06:19 AM Jul 18, 2011
 * 
 */

public interface ProductCatalogDao extends GenericDao<ProductCatalog, Long>{
	
	public void addProductCatalog(ProductCatalog p) throws ObjectExistException;
	
	public void updateProductCatalog(ProductCatalog p)throws ObjectExistException;
	
	public List<ProductCatalog> queryResultList(Map<String, Object> paramMap);
	
	public Page<ProductCatalog> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize);
}
