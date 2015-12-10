package com.hp.idc.cusrelation.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.cusrelation.entity.CusManageContact;
import com.hp.idc.cusrelation.entity.CusManageInfo;
import com.hp.idc.cusrelation.entity.CusManageServant;

/**
 * 
 * 
 * @function 客户关系管理业务逻辑层接口
 * @author Fancy
 * @version 1.0, 2:30:46 PM Jul 29, 2011
 * 
 */
public interface CusRelationManageService {

	public Page<CusManageInfo> queryCustomerInfo(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public CusManageInfo queryCusInfoById(long customerid);

	public Page<CusManageContact> queryCustomerContact(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public Page<CusManageServant> queryCustomerServant(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

}
