package com.hp.idc.customer.dao.jpa;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.customer.dao.CusBussinessDao;
import com.hp.idc.customer.entity.CusBussiness;
import com.hp.idc.customer.entity.CusBussinessPK;

@Repository("cusBussinessDao")
public class CusBussinessDaoJpa extends GenericDaoJpa<CusBussiness, CusBussinessPK>
		implements CusBussinessDao {
	public CusBussinessDaoJpa() {
		super(CusBussiness.class);
	}

	/**
	 * 分页查询boss IDC业务信息表
	 */
	@Override
	public Page<CusBussiness> queryCustomerBussiness(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from CusBussiness o  where 1=1");
		countSb.append("select count(o.customerId) from CusBussiness o where 1=1");

		for (String key : paramMap.keySet()) {

			if (paramMap.get(key) != null) {
				// 集团编号
				if (key.equals("customerId")) {
					sb.append(" and o.customerId = :customerId ");
					countSb.append(" and o.customerId = :customerId ");
					paramMap.put(key, paramMap.get(key));
				}

			}
		}
		return this.queryResultPage(sb.toString(), countSb.toString(),
				paramMap, sortMap, pageNo, pageSize);
	}
	
	
	/**
	 * 
	 * 查询BOSS IDC业务信息
	 * 
	 * @return BOSS IDC业务信息
	 */
	public List<CusBussiness> queryCustomerBussinessList(long doneCode) {
		StringBuffer jSql = new StringBuffer();
		jSql.append("select o from CusBussiness o where 1=1 and o.id.doneCode=:doneCode");
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		paramMap.put("doneCode",doneCode);
		try {
			return  this.queryResultList(jSql.toString(), paramMap, sortMap);
		} catch (Exception e) {
			return new ArrayList<CusBussiness>();
		}
	}
	
	/**
	 * 
	 * 查询BOSS IDC业务信息
	 * 
	 * @param orderId  定单号
	 * @param orderDetailId 定单明细号
	 * @return BOSS IDC业务信息
	 */
	public CusBussiness findCustomerBussiness(long orderId, long orderDetailId,
			long doneCode) {
		CusBussinessPK id = new CusBussinessPK();
		id.setDoneCode(doneCode);
		id.setOrderDetailId(orderDetailId);
		id.setOrderId(orderId);
		try {
			return this.get(id);
		} catch (Exception e) {
			return new CusBussiness();
		}
	}
}
