package com.hp.idc.cusrelation.dao.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.cusrelation.dao.CusManageServantDao;
import com.hp.idc.cusrelation.entity.CusManageServant;
import com.hp.idc.cusrelation.entity.CusManageServantPK;

@Repository("cusManageServantDao")
public class CusManageServantDaoJpa extends
		GenericDaoJpa<CusManageServant, CusManageServantPK> implements
		CusManageServantDao {
	public CusManageServantDaoJpa() {
		super(CusManageServant.class);
	}

	/**
	 * ��ҳ��ѯ�ͻ�������Ϣ
	 */
	@Override
	public Page<CusManageServant> queryCustomerServant(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from CusManageServant o  where 1=1");
		countSb.append("select count(o.id.customerId) from CusManageServant o where 1=1");

		for (String key : paramMap.keySet()) {

			if (paramMap.get(key) != null) {
				// ���ű��
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

	/**
	 * 
	 * ��ѯ�ͻ�������Ϣ
	 * 
	 * @param id
	 *            ���������
	 * @param serviceId
	 *            �����ʶ
	 * @return �ͻ�������Ϣ
	 */
	public CusManageServant findCustomerServant(long id, long serviceId) {
		EntityManager em = getEntityManager();
		StringBuffer jSql = new StringBuffer();
		jSql.append("select o from CusManageServant o where 1=1 and and o.id=:id and o.id.serviceId=:serviceId");
		Query query = em.createQuery(jSql.toString());
		query.setParameter("id", id);
		query.setParameter("serviceId", serviceId);
		try {
			return (CusManageServant) query.getResultList().get(0);
		} catch (Exception e) {
			return new CusManageServant();
		}
	}
}
