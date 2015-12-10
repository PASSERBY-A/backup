package com.cmsz.cloudplatform.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.HpvmDao;
import com.cmsz.cloudplatform.model.Hpvm;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("hpvmDao")
public class HpvmDaoImpl extends GenericDaoImpl<Hpvm, Long> implements HpvmDao {

	public HpvmDaoImpl(Class<Hpvm> persistentClass) {
		super(persistentClass);
	}

	public HpvmDaoImpl() {
		super(Hpvm.class);
	}

	
}
