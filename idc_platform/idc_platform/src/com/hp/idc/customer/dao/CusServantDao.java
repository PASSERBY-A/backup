package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusServantPK;

public interface CusServantDao extends GenericDao<CusServant, CusServantPK>{
	/**
	 * ��ҳ��ѯ�ͻ�������Ϣ��Dao
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
	 * ��ѯ�ͻ�������Ϣ
	 * 
	 * @param id ���������
	 * @param serviceId �����ʶ
	 * @return �ͻ�������Ϣ
	 */
	public CusServant findCustomerServant(long id, long serviceId);
}
