package com.hp.idc.customer.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusBussiness;
import com.hp.idc.customer.entity.CusContact;
import com.hp.idc.customer.entity.CusInfo;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusService;

/**
 * 
 * 
 * @function 客户关系管理业务逻辑层接口
 * @author Fancy
 * @version 1.0, 2:30:46 PM Jul 29, 2011
 * 
 */
public interface CustomerManageService {

	public Page<CusInfo> queryCustomerInfo(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public CusInfo queryCusInfoById(long customerid);

	public Page<CusContact> queryCustomerContact(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public Page<CusAccount> queryCustomerAccount(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public Page<CusServant> queryCustomerServant(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public Page<CusService> queryCustomerService(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public Page<CusBussiness> queryCustomerBussiness(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
