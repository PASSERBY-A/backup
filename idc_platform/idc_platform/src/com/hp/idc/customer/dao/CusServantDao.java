package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusServantPK;

public interface CusServantDao extends GenericDao<CusServant, CusServantPK>{
	/**
	 * 分页查询客户订购信息表Dao
	 * 
	 * queryCustomerServant
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusServant> queryCustomerServant(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * 
	 * 查询客户订购信息
	 * 
	 * @param id 主订购编号
	 * @param serviceId 服务标识
	 * @return 客户订购信息
	 */
	public CusServant findCustomerServant(long id, long serviceId);
}
