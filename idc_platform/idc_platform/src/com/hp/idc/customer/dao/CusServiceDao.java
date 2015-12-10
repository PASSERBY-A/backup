package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusService;
import com.hp.idc.customer.entity.CusServicePK;

public interface CusServiceDao extends GenericDao<CusService, CusServicePK>{
	/**
	 * ��ҳ��ѯ�ͻ����Ѽ�¼��Dao
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
