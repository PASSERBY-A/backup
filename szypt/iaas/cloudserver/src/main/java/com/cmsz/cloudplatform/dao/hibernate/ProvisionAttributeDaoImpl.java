package com.cmsz.cloudplatform.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.ProvisionAttributeDao;
import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.hp.core.dao.hibernate.GenericDaoImpl;
@Repository("provisionAttributeDao")
public class ProvisionAttributeDaoImpl extends GenericDaoImpl<ProvisionAttribute, Long> implements
		ProvisionAttributeDao {
	public ProvisionAttributeDaoImpl(){
		super(ProvisionAttribute.class);
	}
	
	
	public ProvisionAttributeDaoImpl(Class<ProvisionAttribute> clazz) {
		super(clazz);
	}

}
