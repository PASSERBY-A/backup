package com.cmsz.cloudplatform.service;

import com.cmsz.cloudplatform.model.DimResource;
import com.cmsz.cloudplatform.model.request.DimResourceTreeRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.hp.core.service.GenericManager;

public interface DimResourceManager extends GenericManager<DimResource, Long> {
	/**
	 * 同步数据  资源表
	 */
	void synchronizeData();
	
	/**
	 * 
	 * @param drtr
	 * @return
	 */
	public ListResponse<DimResource> getDimResourceTree(DimResourceTreeRequest drtr);
}
