package com.hp.idc.customer.dao.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.customer.dao.CusServantDao;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusServantPK;

@Repository("cusServantDao")
public class CusServantDaoJpa extends GenericDaoJpa<CusServant, CusServantPK> implements
		CusServantDao {
	public CusServantDaoJpa() {
		super(CusServant.class);
	}

	/**
	 * ��ҳ��ѯ�ͻ�������Ϣ
	 */
	@Override
	public Page<CusServant> queryCustomerServant(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from CusServant o  where 1=1");
		countSb.append("select count(o.customerId) from CusServant o where 1=1");

		for (String key : paramMap.keySet()) {

			if (paramMap.get(key) != null) {
				// ���ű��
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
	 * ��ѯ�ͻ�������Ϣ
	 * 
	 * @param id  ���������
	 * @param serviceId �����ʶ
	 * @return �ͻ�������Ϣ
	 */
	public CusServant findCustomerServant(long id, long serviceId) {
		EntityManager em = getEntityManager();
		StringBuffer jSql = new StringBuffer();
		jSql.append("select o from CusServant o where 1=1 and and o.id=:id and o.serviceId=:serviceId");
		Query query = em.createQuery(jSql.toString());
		query.setParameter("id", id);
		query.setParameter("serviceId", serviceId);
		try {
			return (CusServant) query.getResultList().get(0);
		} catch (Exception e) {
			return new CusServant();
		}
	}
}
