package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.TEquipmentenrollDao;
import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("tEquipmentenrollDao")
public class TEquipmentenrollDaoImpl extends GenericDaoImpl<TEquipmentenroll, Long> implements TEquipmentenrollDao {
	public TEquipmentenrollDaoImpl() {
		super(TEquipmentenroll.class);
	}

	public TEquipmentenrollDaoImpl(Class<TEquipmentenroll> clazz) {
		super(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TEquipmentenroll> getAllAvailable() {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_EQUIPMENTENROLL t WHERE t.AVAILABLE = 1 order by t.target_type");
		query.addEntity(TEquipmentenroll.class);
		return (List<TEquipmentenroll>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TEquipmentenroll> getList(TEquipmentenroll tEquipmentenroll) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TEquipmentenroll.class);
		if (tEquipmentenroll != null) {
			if (tEquipmentenroll.getIp() != null) {
				detachedCriteria.add(Restrictions.eq("ip", tEquipmentenroll.getIp()));
			}
			if (tEquipmentenroll.getTargetType() != null) {
				detachedCriteria.add(Restrictions.eq("targetType", tEquipmentenroll.getTargetType()));
			}
		}

		return (List<TEquipmentenroll>) this.findByCriteria(detachedCriteria);
	}

}
