package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusService;
import com.hp.idc.customer.entity.CusServicePK;

public interface CusServiceDao extends GenericDao<CusService, CusServicePK>{
	/**
	 * 分页查询客户消费记录表Dao
	 * 
	 * queryCustomerService
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusService> queryCustomerService(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
