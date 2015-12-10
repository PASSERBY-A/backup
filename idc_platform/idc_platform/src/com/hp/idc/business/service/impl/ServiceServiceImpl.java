/*
 * @(#)ServiceServiceImpl.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hp.idc.business.dao.ProductDao;
import com.hp.idc.business.dao.ServiceDao;
import com.hp.idc.business.dao.ServiceResourceDao;
import com.hp.idc.business.entity.Product;
import com.hp.idc.business.entity.Service;
import com.hp.idc.business.entity.ServiceResource;
import com.hp.idc.business.entity.ServiceResourcePK;
import com.hp.idc.business.service.ServiceService;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.exception.ObjectExistException;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:40:14 PM Jul 20, 2011
 * 
 */

@org.springframework.stereotype.Service("serviceService")
public class ServiceServiceImpl implements ServiceService {
	
	@Resource
	private ServiceDao serviceDao;
	
	@Resource
	private ProductDao productDao;
	
	@Resource
	private ServiceResourceDao serviceResourceDao;

	/* (non-Javadoc)
	 * @see com.hp.idc.business.service.ServiceService#queryResultList(java.util.Map)
	 */
	@Override
	public List<Service> queryResultList(Map<String, Object> paramMap) {
		return serviceDao.queryResultList(paramMap);
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.business.service.ServiceService#queryResultPage(java.util.Map, int, int)
	 */
	@Override
	public Page<Service> queryResultPage(Map<String, Object> paramMap,
			int pageNo, int pageSize) {
		return serviceDao.queryResultPage(paramMap, pageNo, pageSize);
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.business.service.ServiceService#queryService(long)
	 */
	@Override
	public Service queryService(long id) {
		return serviceDao.get(id);
	}
	@Override
	public void addService(Service service) throws ObjectExistException {
		service.setCreateDate(new Date());
		serviceDao.addService(service);
	}
	/* (non-Javadoc)
	 * @see com.hp.idc.business.service.ServiceService#updateService(com.hp.idc.business.entity.Service)
	 */
	@Override
	public void updateService(Service service) throws ObjectExistException {
		serviceDao.updateService(service);
	}



	@Override
	public boolean removeService(long id) {
		Service service = serviceDao.getService(id);
		if(service.getProducts().size()>0){
			return false;
		}
//		for(Product p:service.getProducts()){
//			p.getServices().size();
//			p.removeService(service);
//			productDao.save(p);
//		}
//		service.setProducts(new HashSet<Product>());
		HashMap<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("serviceId", id);
		List<ServiceResource> list=serviceResourceDao.queryResultList(paramMap, new LinkedHashMap<String,String>());
		for(ServiceResource sr:list){
			serviceResourceDao.remove(sr);
		}
		serviceDao.remove(id);
		return true;
	}
	
	@Override
	public boolean removeServices(String ids) {
		String[] idStrArr= ids.split(",");
		boolean ret=true;
		for(String idStr : idStrArr){
			boolean result=removeService(Long.parseLong(idStr));
			ret=ret&&result;
		}
		return ret;
	}
	
	@Override
	public ServiceResource queryServiceResource(ServiceResourcePK id){
		ServiceResource sr =serviceResourceDao.get(id);
		sr.setResModel(ServiceManager.getModelService().getModelById(id.getResModelId()));
		return sr;
	}
	
	@Override
	public void removeServiceResource(ServiceResourcePK id){
		serviceResourceDao.remove(id);
	}
	@Override
	public void saveServiceResource(ServiceResource serviceResource){
		serviceResourceDao.save(serviceResource);
	}
	@Override
	public List<ServiceResource> queryServiceResourceByResModelId(String resModelId){
		Map<String, Object> paramMap=new HashMap<String ,Object>();
		LinkedHashMap<String, String> sortMap=new LinkedHashMap<String ,String>();
		paramMap.put("resModelId", resModelId);
		return serviceResourceDao.queryResultList(paramMap, sortMap);
	}
	@Override
	public List<ServiceResource> queryServiceResourceList(Map<String,Object> paramMap){
		LinkedHashMap<String, String> sortMap=new LinkedHashMap<String ,String>();
		return serviceResourceDao.queryResultList(paramMap, sortMap);
	}
	@Override
	public Page<ServiceResource> queryServiceResourcePage(Map<String,Object> paramMap, int pageNo, int pageSize){
		LinkedHashMap<String, String> sortMap=new LinkedHashMap<String ,String>();
		Page<ServiceResource> page=serviceResourceDao.queryResultPage(paramMap, sortMap, pageNo, pageSize);
		return page;
	}
	
	
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ServiceResourceDao getServiceResourceDao() {
		return serviceResourceDao;
	}

	public void setServiceResourceDao(ServiceResourceDao serviceResourceDao) {
		this.serviceResourceDao = serviceResourceDao;
	}

}
