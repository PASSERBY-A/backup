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
	 * 分页查询boss IDC业务信息表Dao
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
	 * 查询BOSS IDC业务信息
	 * 
	 * @param orderId  定单号
	 * @param orderDetailId 定单明细号
	 * @param doneCode
	 * @return BOSS IDC业务信息
	 */
	public CusBussiness findCustomerBussiness(long orderId, long orderDetailId, long doneCode);

	/**
	 * 
	 * 查询BOSS IDC业务信息
	 * 
	 * @return BOSS IDC业务信息
	 */
	public List<CusBussiness> queryCustomerBussinessList(long doneCode);
}
