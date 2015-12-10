package com.cmsz.cloudplatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.PaasObjDao;
import com.cmsz.cloudplatform.model.PaasDestoryObj;
import com.cmsz.cloudplatform.service.PaasDestoryObjManager;
import com.hp.core.service.impl.GenericManagerImpl;
@Service(value="paasDestoryObjManager")
public class PaasDestoryObjManagerImpl extends GenericManagerImpl<PaasDestoryObj, Long>
		implements PaasDestoryObjManager {
	
	private PaasObjDao paasObjDao;
	
	public PaasDestoryObjManagerImpl() {
		super();
	}
	
	
	public PaasDestoryObjManagerImpl(PaasObjDao paasObjDao) {
		super(paasObjDao);
		this.paasObjDao = paasObjDao;
	}
	
	@Autowired
	public void setPaasObjDao(PaasObjDao paasObjDao) {
		this.paasObjDao = paasObjDao;
		this.dao = paasObjDao;
	}
}
