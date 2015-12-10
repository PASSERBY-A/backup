/*
 * @(#)ProductCatalogServiceImpl.java
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

import com.hp.idc.business.dao.ProductCatalogDao;
import com.hp.idc.business.dao.ProductCatalogDtlDao;
import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.entity.ProductCatalogDtlPK;
import com.hp.idc.business.service.ProductCatalogService;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.exception.ObjectExistException;

/**
 * 产品目录服务提供类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:39:05 AM Jul 18, 2011
 * 
 */

@Service("productCatalogService")
public class ProductCatalogServiceImpl implements ProductCatalogService {

	@Resource
	private ProductCatalogDao productCatalogDao;

	@Resource
	private ProductCatalogDtlDao productCatalogDtlDao;
	
	@Override
	public void addProductCatalog(ProductCatalog cata) throws ObjectExistException {
		cata.setCreateDate(new Date());
		cata.setUpdateDate(new Date());
		productCatalogDao.addProductCatalog(cata);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.business.service.ProductCatalogService#addProductCatalog(com
	 * .hp.idc.business.entity.ProductCatalog)
	 */
	@Override
	public void updateProductCatalog(ProductCatalog cata) throws ObjectExistException {
		cata.setUpdateDate(new Date());
		productCatalogDao.updateProductCatalog(cata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.business.service.ProductCatalogService#addProductCatalogDtl
	 * (com.hp.idc.business.entity.ProductCatalogDtl)
	 */
	@Override
	public void updateProductCatalogDtl(ProductCatalogDtl cataDtl) {
		productCatalogDtlDao.save(cataDtl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.business.service.ProductCatalogService#queryAllProductCatalog
	 * ()
	 */
	@Override
	public List<ProductCatalog> queryProductCatalog(Map<String, Object> m) {
		return productCatalogDao.queryResultList(m);
	}

	
	@Override
	public Page<ProductCatalog> queryProductCatalogPage(Map<String, Object> paramMap, int pageNo , int pageSize) {
		return productCatalogDao.queryResultPage(paramMap, pageNo, pageSize);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.business.service.ProductCatalogService#queryProductCatalog
	 * (int)
	 */
	@Override
	public ProductCatalog queryProductCatalog(long catalogId) {
		return productCatalogDao.get(catalogId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.business.service.ProductCatalogService#queryProductCatalogAllDtl
	 * (int)
	 */
	@Override
	public List<ProductCatalogDtl> queryProductCatalogAllDtl(long catalogId) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("catalogId", catalogId);
		
		return productCatalogDtlDao.queryResultList(m);
	}
	
	@Override
	public Page<ProductCatalogDtl> queryProductCatalogDtlPage(Map<String, Object> paramMap, int pageNo , int pageSize){
		return productCatalogDtlDao.queryResultPage(paramMap, pageNo, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.business.service.ProductCatalogService#queryProductCatalogDtl
	 * (int)
	 */
	@Override
	public ProductCatalogDtl queryProductCatalogDtl(long dtlId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ProductCatalog> queryPageProductCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void removeProductCatalog(long id) {
		HashMap<String,Object> paramMap=new HashMap<String,Object>();
		ProductCatalog cata=new ProductCatalog();
		cata.setId(id);
		paramMap.put("catalog", cata);
		List<ProductCatalogDtl> list=productCatalogDtlDao.queryResultList(paramMap);
		for(ProductCatalogDtl dtl:list){
			productCatalogDtlDao.remove(dtl);
		}
		productCatalogDao.remove(id);
	}
	
	@Override
	public void removeProductCatalog(String ids){
		System.out.println("11111");
		String[] idStrArr= ids.split(",");
		try{
		for(String idStr : idStrArr){
			removeProductCatalog(Long.parseLong(idStr));
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void removeProductCatalogDtl(ProductCatalogDtlPK id) {
		productCatalogDtlDao.remove(id);
	}
	
	
	public ProductCatalogDao getProductCatalogDao() {
		return productCatalogDao;
	}

	public void setProductCatalogDao(ProductCatalogDao productCatalogDao) {
		this.productCatalogDao = productCatalogDao;
	}
	
	public void setProductCatalogDtlDao(ProductCatalogDtlDao productCatalogDtlDao) {
		this.productCatalogDtlDao = productCatalogDtlDao;
	}

	public ProductCatalogDtlDao getProductCatalogDtlDao() {
		return productCatalogDtlDao;
	}
}
