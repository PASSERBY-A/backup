/*
 * @(#)BusindessAction.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.view;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.hp.idc.business.entity.Product;
import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.entity.ProductCatalogDtlPK;
import com.hp.idc.business.entity.Service;
import com.hp.idc.business.entity.ServiceResource;
import com.hp.idc.business.entity.ServiceResourcePK;
import com.hp.idc.business.service.ProductCatalogService;
import com.hp.idc.business.service.ProductService;
import com.hp.idc.business.service.ServiceService;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.service.SysBaseTypeService;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.common.exception.ObjectExistException;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:34:22 AM Jul 18, 2011
 * 
 */

public class BusindessAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	private ProductCatalogService productCatalogService;
	
	private ProductService productService;
	
	private ServiceService serviceService;

	private String id;
	
	private String ids;
	
	private String name;
	
	private String resName;
	
	private Integer resType;
	
	private Long catalogId;
	
	private Long productId;
	
	private Long serviceId;
	
	private String resModelId;
	
	private ProductCatalog productCatalog;
	
	private ProductCatalogDtl productCatalogDtl;
	
	private Product product;
	
	private Service service;
	
	private ServiceResource serviceResource;
	
	private String effectDate;
	
	private String expireDate;
	
	public String queryProductCatalog(){
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(id!=null&&!"".equals(id)) queryMap.put("id",Long.parseLong(id));
		if(name!=null&&!"".equals(name)) queryMap.put("name", name);
		Page<ProductCatalog> page=productCatalogService.queryProductCatalogPage(queryMap, start/20+1, limit);
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.setExcludes(new String[]{"createDate","updateDate","catalogDtls",
				"effectDate","expireDate"});
		if(page.getResult()!=null){
			 for(ProductCatalog cata:page.getResult()){
				 JSONObject json=JSONObject.fromObject(cata,jsonConfig);
				 if(cata.getCreateDate()!=null)json.put("createDate", df.format(cata.getCreateDate()));
				 if(cata.getUpdateDate()!=null)json.put("updateDate", df.format(cata.getUpdateDate()));
				 if(cata.getCreator()!=null)json.put("creator", PersonManager.getPersonById(cata.getCreator()).getName());
				 jsonArray.add(json);
			 }
		}
		jsonObject.put(JSON_RESULT, jsonArray);
		jsonObject.put(JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;
	}
	
	public String queryProductCatalogDtl(){
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(catalogId!=null&&catalogId>0){ 
			ProductCatalog catalog=new ProductCatalog();
			catalog.setId(catalogId);
			queryMap.put("catalog",catalog);
		}
		if(id!=null&&!"".equals(id)) queryMap.put("id",Long.parseLong(id));
		if(name!=null&&!"".equals(name)) queryMap.put("name",name);
		Page<ProductCatalogDtl> page=productCatalogService.queryProductCatalogDtlPage(queryMap, start/20+1, limit);
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.setExcludes(new String[]{"createDate","updateDate",
				"catalog","product"});
		if(page.getResult()!=null){
			 for(ProductCatalogDtl cata:page.getResult()){
				 JSONObject json=JSONObject.fromObject(cata,jsonConfig);
				 json.put("productId", cata.getId().getProduct().getId());
				 json.put("productName", cata.getId().getProduct().getName());
				 json.put("status",cata.getId().getProduct().getStatus());
				 json.put("defaultQuanity", cata.getDefaultQuanity());
				 jsonArray.add(json);
			 }
		}
		jsonObject.put(JSON_RESULT, jsonArray);
		jsonObject.put(JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;
	}

	public String queryProduct(){
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(id!=null&&!"".equals(id)) queryMap.put("id",Long.parseLong(id));
		if(name!=null&&!"".equals(name)) queryMap.put("name", name);
		Page<Product> page=productService.queryResultPage(queryMap, start/20+1, limit);
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.setExcludes(new String[]{"createDate","updateDate","effectDate","expireDate",
				"catalog","services","catalogDtls"});
		if(page.getResult()!=null){
			 for(Product product:page.getResult()){
				 JSONObject json=JSONObject.fromObject(product,jsonConfig);
				 if(product.getCreateDate()!=null)json.put("createDate", df.format(product.getCreateDate()));
				 if(product.getEffectDate()!=null)json.put("effectDate", df.format(product.getEffectDate()));
				 if(product.getExpireDate()!=null)json.put("expireDate", df.format(product.getExpireDate()));
				 if(product.getCreator()!=null)json.put("creator", PersonManager.getPersonById(product.getCreator()).getName());
				 jsonArray.add(json);
			 }
		}
		jsonObject.put(JSON_RESULT, jsonArray);
		jsonObject.put(JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;
	}
	
	public String queryService(){
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(id!=null&&!"".equals(id)) queryMap.put("id",Long.parseLong(id));
		if(name!=null&&!"".equals(name)) queryMap.put("name", name);
		Page<Service> page=serviceService.queryResultPage(queryMap, start/20+1, limit);
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.setExcludes(new String[]{"createDate","updateDate","effectDate","expireDate",
				"catalog","products"});
		if(page.getResult()!=null){
			 for(Service service:page.getResult()){
				 JSONObject json=JSONObject.fromObject(service,jsonConfig);
				 if(service.getCreateDate()!=null)json.put("createDate", df.format(service.getCreateDate()));
				 if(service.getEffectDate()!=null)json.put("effectDate", df.format(service.getEffectDate()));
				 if(service.getExpireDate()!=null)json.put("expireDate", df.format(service.getExpireDate()));
				 jsonArray.add(json);
			 }
		}
		jsonObject.put(JSON_RESULT, jsonArray);
		jsonObject.put(JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;
	}
	
	public String queryServiceResource(){
		Map<String, Object> queryMap=new HashMap<String, Object>();
		if(id!=null&&!"".equals(id)) queryMap.put("serviceId",Long.parseLong(id));
		if(resName!=null&&!"".equals(resName)) queryMap.put("resName", resName);
		if(resType!=null&&resType>0) queryMap.put("resType", resType);
		
		Page<ServiceResource> page=serviceService.queryServiceResourcePage(queryMap, start/20+1, limit);
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		if(page.getResult()!=null){
			 for(ServiceResource sr:page.getResult()){
				 JSONObject json=new JSONObject();
				 json.put("serviceId", sr.getId().getService().getId());
				 json.put("resModelId", sr.getId().getResModelId());
				 json.put("amount", sr.getAmount());
				 json.put("remark", sr.getRemark());
				 json.put("resName", sr.getResModel().getName());
				 json.put("resType",  sr.getResModel().getType());
				 jsonArray.add(json);
			 }
		}
		jsonObject.put(JSON_RESULT, jsonArray);
		jsonObject.put(JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;
	}
	
	public String queryResource(){
		
		List<Model> list=ServiceManager.getModelService().getAllModels(0);
		//int end= (start+limit)<list.size()?(start+limit):list.size();
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		for(Model e:list){
			JSONObject json=new JSONObject();
			json.put("id", e.getId());
			json.put("name", e.getName());
			json.put("type", e.getType());
			jsonArray.add(json);
		}
		jsonObject.put(JSON_TOTAL_COUNT, list.size());
		jsonObject.put(JSON_RESULT, jsonArray);
		System.out.println(jsonObject.toString());
		return SUCCESS;
	}
	
	public String detailProductCatalog(){
		if(id!=null){
			productCatalog= productCatalogService.queryProductCatalog(Long.parseLong(id));
			if(productCatalog.getDescription()!=null){
				productCatalog.setDescription(productCatalog.getDescription().replaceAll("\r\n", "<br>"));
			}
		}
		return SUCCESS;
	}
	
	public String detailProduct(){
		if(id!=null){
			product=productService.queryProduct(Long.parseLong(id));
			if(product.getDescription()!=null){
				product.setDescription(product.getDescription().replaceAll("\r\n", "<br>"));
			}
		}
		return SUCCESS;
	}
	
	public String detailService(){
		if(id!=null){
			service = serviceService.queryService(Long.parseLong(id));
			if(service.getDescription()!=null){
				service.setDescription(service.getDescription().replaceAll("\r\n", "<br>"));
			}
		}
		return SUCCESS;
	}

	public String addProductCatalog(){
		boolean successFlg=true;
		String msg="";
		try{
			productCatalog.setCreator(getLoginUserId());
			productCatalogService.addProductCatalog(productCatalog);
			msg="保存产品目录成功";
		}catch(ObjectExistException e){
			successFlg=false;
			msg=e.getMessage();
		}catch(Exception e){
			successFlg=false;
			msg="保存产品目录失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String updateProductCatalog(){
		ProductCatalog cata=null;
		boolean successFlg=true;
		String msg="";
		try{
			if(productCatalog.getId()>0){
				cata=productCatalogService.queryProductCatalog(productCatalog.getId());
				cata.setUpdater(getLoginUserId());
				cata.setName(productCatalog.getName());
				cata.setDescription(productCatalog.getDescription());
				cata.setStatus(productCatalog.getStatus());
			}
			else
				throw new ObjectExistException("该产品目录不存在");
			productCatalogService.updateProductCatalog(cata);
			msg="保存产品目录成功";
		}catch(ObjectExistException e){
			successFlg=false;
			msg=e.getMessage();
		}catch(Exception e){
			successFlg=false;
			msg="保存产品目录失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String saveProductCatalogDtl(){
		try{
			productCatalogService.updateProductCatalogDtl(productCatalogDtl);
			setJSONResponse(true, "为产品目录添加基础产品成功！");
			
		}catch(Exception e){
			e.printStackTrace();
			setJSONResponse(false, "为产品目录添加基础产品成功失败！");
		}
		return SUCCESS;	
	}
	
	public String addProduct(){
		boolean successFlg=true;
		String msg="";
		try{
			if(effectDate!=null)
				product.setEffectDate(DateTimeUtil.parseDate(effectDate,"yyyy-MM-dd"));
			else 
				product.setEffectDate(null);
			if(expireDate!=null)
				product.setExpireDate(DateTimeUtil.parseDate(expireDate,"yyyy-MM-dd"));
			else
				product.setExpireDate(null);
			product.setCreator(getLoginUserId());
			productService.addProduct(product);
			msg="保存产品信息成功";
		}catch(ObjectExistException e){
			e.printStackTrace();
			successFlg=false;
			msg=e.getMessage();
		}catch(Exception e){
			e.printStackTrace();
			successFlg=false;
			msg="保存产品信息失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String updateProduct(){
		Product pro=null;
		boolean successFlg=true;
		String msg="";
		try{
			if(product.getId()>0){
				pro=productService.queryProduct(product.getId());
				pro.setName(product.getName());
				pro.setDescription(product.getDescription());
				pro.setSubParam(product.getSubParam());
				pro.setStatus(product.getStatus());
				if(effectDate!=null)
					pro.setEffectDate(DateTimeUtil.parseDate(effectDate,"yyyy-MM-dd"));
				else 
					pro.setEffectDate(null);
				if(expireDate!=null)
					pro.setExpireDate(DateTimeUtil.parseDate(expireDate,"yyyy-MM-dd"));
				else
					pro.setExpireDate(null);
			}
			else{
				throw new ObjectExistException("该产品不存在");
			}
			productService.updateProduct(pro);
			msg="保存产品信息成功";
		}catch(ObjectExistException e){
			e.printStackTrace();
			successFlg=false;
			msg=e.getMessage();
		}catch(Exception e){
			e.printStackTrace();
			successFlg=false;
			msg="保存产品信息失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String addService(){
		boolean successFlg=true;
		String msg="";
		try{
			if(effectDate!=null)
				service.setEffectDate(DateTimeUtil.parseDate(effectDate,"yyyy-MM-dd"));
			else 
				service.setEffectDate(null);
			if(expireDate!=null)
				service.setExpireDate(DateTimeUtil.parseDate(expireDate,"yyyy-MM-dd"));
			else
				service.setExpireDate(null);
			serviceService.addService(service);
			msg="保存服务信息成功";
		}catch(ObjectExistException e){
			successFlg=false;
			msg=e.getMessage();
		}catch(Exception e){
			successFlg=false;
			msg="保存服务信息失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String updateService(){
		Service ser=null;
		boolean successFlg=true;
		String msg="";
		try{
			if(service.getId()>0){
				ser=serviceService.queryService(service.getId());
				ser.setName(service.getName());
				ser.setDescription(service.getDescription());
				ser.setType(service.getType());
				ser.setServiceValue(service.getServiceValue());
				ser.setStatus(service.getStatus());
				if(effectDate!=null)
					ser.setEffectDate(DateTimeUtil.parseDate(effectDate,"yyyy-MM-dd"));
				else 
					ser.setEffectDate(null);
				if(expireDate!=null)
					ser.setExpireDate(DateTimeUtil.parseDate(expireDate,"yyyy-MM-dd"));
				else
					ser.setExpireDate(null);
			}
			else{
				throw new ObjectExistException("该服务不存在");
			}
			serviceService.updateService(ser);
			msg="保存服务信息成功";
		}catch(ObjectExistException e){
			successFlg=false;
			msg=e.getMessage();
		}catch(Exception e){
			successFlg=false;
			msg="保存服务信息失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String saveServiceResource(){
		try{
			serviceService.saveServiceResource(serviceResource);
			setJSONResponse(true, "为业务添加资源模型成功！");
			
		}catch(Exception e){
			e.printStackTrace();
			setJSONResponse(false, "为业务添加资源模型失败！");
		}
		return SUCCESS;	
	}
	
	public String removeProductCatalog(){
		boolean successFlg=true;
		String msg="";
		try{
			productCatalogService.removeProductCatalog(ids);
			msg="删除产品目录成功";
		}catch(Exception e){
			successFlg=false;
			msg="删除产品目录失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String removeProductCatalogDtl(){
		boolean successFlg=true;
		String msg="";
		try{
			String[] idArr= ids.split(",");
			for(String productId:idArr){
				if(productId!=null&&catalogId!=null){
					Product product=new Product();
					product.setId(Long.parseLong(productId));
					ProductCatalog catalog=new ProductCatalog();
					catalog.setId(catalogId);
					ProductCatalogDtlPK pk=new ProductCatalogDtlPK();
					pk.setProduct(product);
					pk.setCatalog(catalog);
					productCatalogService.removeProductCatalogDtl(pk);
				}
			}
			msg="从产品目录移除产品成功";
		}catch(Exception e){
			successFlg=false;
			msg="从产品目录移除产品失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}
	
	public String removeServiceResource(){
		System.out.println("ids---->"+ids);
		System.out.println("serviceId---->"+serviceId);
		try{
			String[] idArr= ids.split(",");
			for(String resModelId:idArr ){
				if(serviceId!=null&&resModelId!=null&&!"".equals(resModelId)){
					Service service=new Service();
					service.setId(serviceId);
					ServiceResourcePK pk=new ServiceResourcePK();
					pk.setService(service);
					pk.setResModelId(resModelId);
					serviceService.removeServiceResource(pk);
				}
			}
			setJSONResponse(true, "从业务删除资源模型成功！");
		}catch(Exception e){
			e.printStackTrace();
			setJSONResponse(false, "从业务删除资源模型失败！");
		}
		return SUCCESS;
	}
	
	public String removeProduct(){
		boolean successFlg=true;
		String msg="";
		try{
			boolean ret= productService.removeProducts(ids);
			if(ret){
				successFlg=true;
				msg="删除产品信息成功";
			}else{
				successFlg=false;
				msg="选择的产品中有一项或多项被产品目录引用，不能直接删除，请检查后重新操作。";
			}
		}catch(Exception e){
			successFlg=false;
			msg="删除产品信息失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}

	public String removeService(){
		boolean successFlg=true;
		String msg="";
		try{
			boolean ret= serviceService.removeServices(ids);
			if(ret){
				successFlg=true;
				msg="删除服务信息成功";
			}else{
				successFlg=false;
				msg="选择的服务中有一项或多项被产品引用，不能直接删除，请检查后重新操作。";
			}
		}catch(Exception e){
			successFlg=false;
			msg="删除服务信息失败";
		}finally{
			this.setJSONResponse(successFlg, msg);
		}
		return SUCCESS;
	}

	public String queryProductService(){
		if(id!=null){
			product=productService.queryProduct(Long.parseLong(id));
			jsonObject=new JSONObject();
			jsonArray=new JSONArray();
			JsonConfig jsonConfig=new JsonConfig();
			jsonConfig.setExcludes(new String[]{"createDate","updateDate","effectDate","expireDate",
					"catalog","products"});
			for(Service service:product.getServices()){
				 JSONObject json=JSONObject.fromObject(service,jsonConfig);
				 json.put("createDate", df.format(service.getCreateDate()));
				 jsonArray.add(json);
			}
			jsonObject.put(JSON_RESULT, jsonArray);
		}
		return SUCCESS;
	}
	
	public String addProductService(){
		try{
			product=productService.queryProduct(productId);
			service=serviceService.queryService(Long.parseLong(id));
			product.addService(service);
			productService.updateProduct(product);
			setJSONResponse(true,"添加服务到基础产品成功！");
		}catch(Exception e){
			e.printStackTrace();
			setJSONResponse(false,"添加服务到基础产品失败！");
		}
		return SUCCESS;
	}
	public String removeProductService(){
		try{
			product=productService.queryProduct(productId);
			String[] idArr= ids.split(",");
			for(String id:idArr){
				service=serviceService.queryService(Long.parseLong(id));
				product.removeService(service);
			}
			productService.updateProduct(product);
			setJSONResponse(true,"从基础产品删除服务成功！");
		}catch(Exception e){
			e.printStackTrace();
			setJSONResponse(false,"从基础产品删除服务失败！");
		}
		return SUCCESS;
	}
	
	public ProductCatalogService getProductCatalogService() {
		return productCatalogService;
	}

	public void setProductCatalogService(ProductCatalogService productCatalogService) {
		this.productCatalogService = productCatalogService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ServiceService getServiceService() {
		return serviceService;
	}

	public void setServiceService(ServiceService serviceService) {
		this.serviceService = serviceService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public ProductCatalog getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(ProductCatalog productCatalog) {
		this.productCatalog = productCatalog;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ProductCatalogDtl getProductCatalogDtl() {
		return productCatalogDtl;
	}

	public void setProductCatalogDtl(ProductCatalogDtl productCatalogDtl) {
		this.productCatalogDtl = productCatalogDtl;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Integer getResType() {
		return resType;
	}

	public void setResType(Integer resType) {
		this.resType = resType;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getResModelId() {
		return resModelId;
	}

	public void setResModelId(String resModelId) {
		this.resModelId = resModelId;
	}

	public ServiceResource getServiceResource() {
		return serviceResource;
	}

	public void setServiceResource(ServiceResource serviceResource) {
		this.serviceResource = serviceResource;
	}

	public String getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	
}
