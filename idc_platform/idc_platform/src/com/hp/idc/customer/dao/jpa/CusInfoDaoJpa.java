package com.hp.idc.customer.dao.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.customer.dao.CusInfoDao;
import com.hp.idc.customer.entity.CusInfo;


@Repository("cusInfoDao")
public class CusInfoDaoJpa extends GenericDaoJpa<CusInfo, Long> implements
		CusInfoDao {
	
	public CusInfoDaoJpa() {
		super(CusInfo.class);
	}

	/**
	 * 分页查询客户基本信息表
	 */
	@Override
	public Page<CusInfo> queryCustomerInfo(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from CusInfo o  where 1=1");
		countSb.append("select count(o.id) from CusInfo o where 1=1");

		for (String key : paramMap.keySet()) {

			if (paramMap.get(key) != null) {
				// 集团编号
				if (key.equals("id")) {
					sb.append(" and o.id = :id ");
					countSb.append(" and o.id = :id ");
					paramMap.put(key, paramMap.get(key));
				}
				// 客户名称
				if (key.equals("name")) {
					sb.append(" and o.name like :name ");
					countSb.append(" and o.name like :name ");
					paramMap.put(key, "%"+paramMap.get(key)+"%");
				}
				// 客户类别
				if (key.equals("typeId")) {
					sb.append(" and o.typeId = :typeId ");
					countSb.append(" and o.typeId = :typeId ");
					paramMap.put(key, paramMap.get(key));
				}
				// 大客户经理
				if (key.equals("managerName")) {
					sb.append(" and o.managerName like :managerName ");
					countSb.append(" and o.managerName like :managerName ");
					paramMap.put(key, "%"+paramMap.get(key)+"%");
				}
				// 电话
				if (key.equals("phoneNo")) {
					sb.append(" and o.phoneNo = :phoneNo ");
					countSb.append(" and o.phoneNo = :phoneNo ");
					paramMap.put(key, paramMap.get(key));
				}
				// email
				if (key.equals("email")) {
					sb.append(" and o.email like :email ");
					countSb.append(" and o.email like :email ");
					paramMap.put(key, "%"+paramMap.get(key)+"%");
				}

			}
		}
		return this.queryResultPage(sb.toString(), countSb.toString(),
				paramMap, sortMap, pageNo, pageSize);
	}
}
