package com.hp.idc.cusrelation.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.cusrelation.entity.CusManageServant;
import com.hp.idc.cusrelation.entity.CusManageServantPK;



public interface CusManageServantDao extends GenericDao<CusManageServant, CusManageServantPK>{
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
	public Page<CusManageServant> queryCustomerServant(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * 
	 * 查询客户订购信息
	 * 
	 * @param id 主订购编号
	 * @param serviceId 服务标识
	 * @return 客户订购信息
	 */
	public CusManageServant findCustomerServant(long id, long serviceId);
}
