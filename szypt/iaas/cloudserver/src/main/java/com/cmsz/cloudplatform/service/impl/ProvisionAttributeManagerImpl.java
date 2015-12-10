package com.cmsz.cloudplatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.ProvisionAttributeDao;
import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.service.ProvisionAttributeManager;
import com.hp.core.service.impl.GenericManagerImpl;

@Service(value = "provisionAttributeManager")
public class ProvisionAttributeManagerImpl extends
		GenericManagerImpl<ProvisionAttribute, Long> implements
		ProvisionAttributeManager {
	private ProvisionAttributeDao provisionAttributeDao;

	@Autowired
	public void setProvisionAttributeDao(
			ProvisionAttributeDao provisionAttributeDao) {
		this.provisionAttributeDao = provisionAttributeDao;
		this.dao = provisionAttributeDao;
	}

	public ProvisionAttributeManagerImpl() {
		super();
	}

	public ProvisionAttributeManagerImpl(
			ProvisionAttributeDao provisionAttributeDao) {
		super(provisionAttributeDao);
		this.dao = provisionAttributeDao;
	}

}
