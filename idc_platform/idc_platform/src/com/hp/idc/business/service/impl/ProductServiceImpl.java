/*
 * @(#)ProductServiceImpl.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hp.idc.business.dao.ProductCatalogDtlDao;
import com.hp.idc.business.dao.ProductDao;
import com.hp.idc.business.entity.Product;
import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.service.ProductService;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:39:19 AM Jul 18, 2011
 * 
 */

@Service("productService")
public class ProductServiceImpl implements ProductService{
	
	@Resource
	private ProductDao productDao;
	
	@Resource
	private ProductCatalogDtlDao productCatalogDtlDao; 

	@Override
	public Product queryProduct(long id) {
		Product product= productDao.get(id);
		product.getServices().size();
		return product;
	}

	@Override
	public List<Product> queryResultList(Map<String, Object> paramMap) {
		return productDao.queryResultList(paramMap);
	}

	@Override
	public Page<Product> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize) {
		return productDao.queryResultPage(paramMap, pageNo, pageSize);
	}
	@Override
	public void addProduct(Product product) throws ObjectExistException{
		product.setCreateDate(new Date());
		productDao.addProduct(product);
	}
	
	@Override
	public void updateProduct(Product product) throws ObjectExistException {
		productDao.updateProduct(product);
	}
	
	public void saveProductPersist(Product p){
		productDao.saveProductPersist(p);
	}
	

	
	@Override
	public boolean removeProduct(long id) {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id", id);
		List<ProductCatalogDtl> list=productCatalogDtlDao.queryResultList(paramMap);
		if(list.size()>0){
			return false;
		}
//		for(ProductCatalogDtl dtl:list){
//			productCatalogDtlDao.remove(dtl);
//		}
		productDao.remove(id);
		return true;
	}
	
	@Override
	public boolean removeProducts(String ids) {
		String[] idStrArr= ids.split(",");
		boolean ret=true;
		for(String idStr : idStrArr){
			boolean result=removeProduct(Long.parseLong(idStr));
			ret= ret&&result;
		}
		return ret;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ProductDao getProductDao() {
		return productDao;
	}
	
	public ProductCatalogDtlDao getProductCatalogDtlDao() {
		return productCatalogDtlDao;
	}

	public void setProductCatalogDtlDao(ProductCatalogDtlDao productCatalogDtlDao) {
		this.productCatalogDtlDao = productCatalogDtlDao;
	}
}
