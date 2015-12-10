/*
 * @(#)ProductService.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.Product;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:33:40 AM Jul 18, 2011
 * 
 */

public interface ProductService {

	public List<Product> queryResultList(Map<String, Object> paramMap);
	
	public Page<Product> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize);
	
	public Product queryProduct(long id);
	
	public void addProduct(Product product) throws ObjectExistException;
	
	public void updateProduct(Product product)throws ObjectExistException;
	
	public boolean removeProduct(long id);
	
	public boolean removeProducts(String ids);
	
	public void saveProductPersist(Product p);
	
}
