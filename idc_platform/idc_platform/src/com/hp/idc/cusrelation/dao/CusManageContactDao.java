package com.hp.idc.cusrelation.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.cusrelation.entity.CusManageContact;


public interface CusManageContactDao extends GenericDao<CusManageContact, Long>{
	
	/**
	 * 分页查询客户联系人信息表Dao
	 * 
	 * queryCustomerContact
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusManageContact> queryCustomerContact(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
