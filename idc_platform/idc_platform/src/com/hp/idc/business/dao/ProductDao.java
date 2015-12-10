/*
 * @(#)ProductDao.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.dao;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.Product;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:06:31 AM Jul 18, 2011
 * 
 */

public interface ProductDao extends GenericDao<Product, Long> {

	public void saveProductPersist(Product p);
	
	public void addProduct(Product p) throws ObjectExistException;
	
	public void updateProduct(Product p)throws ObjectExistException;
	
	public List<Product> queryResultList(Map<String, Object> paramMap);
	
	public Page<Product> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize);
}
