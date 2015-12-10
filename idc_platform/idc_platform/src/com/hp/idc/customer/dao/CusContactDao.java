package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusContact;
import com.hp.idc.customer.entity.CusInfo;

public interface CusContactDao extends GenericDao<CusContact, Long>{
	
	/**
	 * ��ҳ��ѯ�ͻ���ϵ����Ϣ��Dao
	 * 
	 * queryCustomerContact
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusContact> queryCustomerContact(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
