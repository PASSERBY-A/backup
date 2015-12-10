package com.hp.idc.cusrelation.dao.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.cusrelation.dao.CusManageContactDao;
import com.hp.idc.cusrelation.entity.CusManageContact;


@Repository("cusManageContactDao")
public class CusManageContactDaoJpa extends GenericDaoJpa<CusManageContact, Long> implements
		CusManageContactDao {

	public CusManageContactDaoJpa() {
		super(CusManageContact.class);
	}

	/**
	 * 分页查询客户联系人信息表
	 */
	@Override
	public Page<CusManageContact> queryCustomerContact(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from CusManageContact o  where 1=1");
		countSb.append("select count(o.id) from CusManageContact o where 1=1");

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
}
