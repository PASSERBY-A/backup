package com.hp.idc.customer.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.customer.entity.CusBussiness;
import com.hp.idc.customer.entity.CusBussinessPK;

public interface CusBussinessDao extends GenericDao<CusBussiness, CusBussinessPK>{
	/**
	 * ��ҳ��ѯboss IDCҵ����Ϣ��Dao
	 * 
	 * queryCustomerBussiness
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusBussiness> queryCustomerBussiness(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * 
	 * ��ѯBOSS IDCҵ����Ϣ
	 * 
	 * @param orderId  ������
	 * @param orderDetailId ������ϸ��
	 * @param doneCode
	 * @return BOSS IDCҵ����Ϣ
	 */
	public CusBussiness findCustomerBussiness(long orderId, long orderDetailId, long doneCode);

	/**
	 * 
	 * ��ѯBOSS IDCҵ����Ϣ
	 * 
	 * @return BOSS IDCҵ����Ϣ
	 */
	public List<CusBussiness> queryCustomerBussinessList(long doneCode);
}
