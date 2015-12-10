/*
 * @(#)BusinessService.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.entity.ProductCatalogDtlPK;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:21:49 AM Jul 18, 2011
 * 
 */

public interface ProductCatalogService {
	

	
	
	public List<ProductCatalog> queryProductCatalog(Map<String, Object> m);
	
	public Page<ProductCatalog> queryPageProductCatalog();
	
	public Page<ProductCatalog> queryProductCatalogPage(Map<String, Object> m, int pageNo , int pageSize);
	
	public ProductCatalog queryProductCatalog(long catalogId);
	
	public ProductCatalogDtl queryProductCatalogDtl(long dtlId);
	
	public List<ProductCatalogDtl> queryProductCatalogAllDtl(long catalogId);
	
	public Page<ProductCatalogDtl> queryProductCatalogDtlPage(Map<String, Object> m, int pageNo , int pageSize);
	
	public void addProductCatalog(ProductCatalog cata) throws ObjectExistException;
	
	public void updateProductCatalog(ProductCatalog cata)throws ObjectExistException;
	
	public void removeProductCatalog(long id);
	
	public void removeProductCatalog(String ids);
	
	public void updateProductCatalogDtl(ProductCatalogDtl cataDtl) ;
	
	public void removeProductCatalogDtl(ProductCatalogDtlPK id);
}
