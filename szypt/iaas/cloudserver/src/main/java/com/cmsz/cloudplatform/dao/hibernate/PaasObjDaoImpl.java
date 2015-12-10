package com.cmsz.cloudplatform.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.PaasObjDao;
import com.cmsz.cloudplatform.model.PaasDestoryObj;
import com.hp.core.dao.hibernate.GenericDaoImpl;
@Repository("paasObjDao")
public class PaasObjDaoImpl extends GenericDaoImpl<PaasDestoryObj, Long> implements PaasObjDao {

	public PaasObjDaoImpl(Class<PaasDestoryObj> persistentClass) {
		super(persistentClass);
	}
	
	public PaasObjDaoImpl() {
		super(PaasDestoryObj.class);
	}

}
