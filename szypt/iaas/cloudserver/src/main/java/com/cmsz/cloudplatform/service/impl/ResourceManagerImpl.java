package com.cmsz.cloudplatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.ResourceDao;
import com.cmsz.cloudplatform.model.Resource;
import com.cmsz.cloudplatform.service.ResourceManager;
import com.hp.core.service.impl.GenericManagerImpl;

@Service(value="resourceManager")
public class ResourceManagerImpl  extends GenericManagerImpl<Resource,Long> implements ResourceManager {
	private ResourceDao resourceDao;
	
	public ResourceManagerImpl(){
		super(); 
	}
	
	public ResourceManagerImpl(ResourceDao resourceDao){
		super(resourceDao);
		this.resourceDao = resourceDao;
	}
	
	@Autowired
	public void setPropertyDao(ResourceDao resourceDao) {
		this.dao = resourceDao;
		this.resourceDao = resourceDao;
	}

}
