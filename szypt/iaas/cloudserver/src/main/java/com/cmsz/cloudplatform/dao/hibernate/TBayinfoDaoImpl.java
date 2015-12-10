package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.TBayinfoDao;
import com.cmsz.cloudplatform.model.TBayinfo;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("tBayinfoDao")
public class TBayinfoDaoImpl extends GenericDaoImpl<TBayinfo, Long>
		implements TBayinfoDao {
	public TBayinfoDaoImpl() {
		super(TBayinfo.class);
	}

	public TBayinfoDaoImpl(Class<TBayinfo> clazz) {
		super(clazz);
	}

	@Override
	public List<TBayinfo> getAllByRackId(Long rackId) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_Bayinfo t WHERE t.fk_rack_id = ? order by t.bayIndex");
		query.addEntity(TBayinfo.class);
		query.setLong(0, rackId);
		
		return (List<TBayinfo>)query.list();
	}

}
