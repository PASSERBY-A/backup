package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusContact;
import com.hp.idc.customer.entity.CusServicePK;

public interface CusAccountDao extends GenericDao<CusAccount, CusServicePK>{
	
	/**
	 * 分页查询客户账务费用信息表Dao
	 * 
	 * queryCustomerContact
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusAccount> queryCustomerAccount(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
