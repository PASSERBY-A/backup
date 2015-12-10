package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.THbaDao;
import com.cmsz.cloudplatform.model.THba;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("tHbaDao")
public class THbaDaoImpl extends GenericDaoImpl<THba, Long>
		implements THbaDao {
	public THbaDaoImpl() {
		super(THba.class);
	}

	public THbaDaoImpl(Class<THba> clazz) {
		super(clazz);
	}

	@Override
	public int removeHbaBySerialNumber(String sn) {
		SQLQuery query = this.getSession().createSQLQuery("DELETE FROM T_HBA t WHERE t.fk_host_sn = ?");
		query.setString(0,
										sn);

		return query.executeUpdate();
	}

	@Override
	public List<THba> listBySN(String sn) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT *  FROM T_HBA t WHERE t.fk_host_sn = ?");
		query.setString(0,
										sn);
		query.addEntity(THba.class);
		
		return query.list();
	}

}
