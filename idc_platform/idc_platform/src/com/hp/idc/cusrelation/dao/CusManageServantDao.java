package com.hp.idc.cusrelation.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.cusrelation.entity.CusManageServant;
import com.hp.idc.cusrelation.entity.CusManageServantPK;



public interface CusManageServantDao extends GenericDao<CusManageServant, CusManageServantPK>{
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
	public Page<CusManageServant> queryCustomerServant(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * 
	 * ��ѯ�ͻ�������Ϣ
	 * 
	 * @param id ���������
	 * @param serviceId �����ʶ
	 * @return �ͻ�������Ϣ
	 */
	public CusManageServant findCustomerServant(long id, long serviceId);
}
