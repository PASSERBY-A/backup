package com.hp.idc.customer.dao.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.customer.dao.CusAccountDao;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusContact;
import com.hp.idc.customer.entity.CusServicePK;


@Repository("cusAccountDao")
public class CusAccountDaoJpa extends GenericDaoJpa<CusAccount, CusServicePK>
implements CusAccountDao{
	
	public CusAccountDaoJpa() {
		super(CusAccount.class);
	}

	/**
	 * 分页查询客户联系人信息表
	 */
	@Override
	public Page<CusAccount> queryCustomerAccount(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from CusAccount o  where 1=1");
		countSb.append("select count(o.id.customerId) from CusAccount o where 1=1");

		for (String key : paramMap.keySet()) {

			if (paramMap.get(key) != null) {
				// 集团编号
				if (key.equals("customerId")) {
					sb.append(" and o.id.customerId = :customerId ");
					countSb.append(" and o.id.customerId = :customerId ");
					paramMap.put(key, paramMap.get(key));
				}

			}
		}
		return this.queryResultPage(sb.toString(), countSb.toString(),
				paramMap, sortMap, pageNo, pageSize);
	}
}
