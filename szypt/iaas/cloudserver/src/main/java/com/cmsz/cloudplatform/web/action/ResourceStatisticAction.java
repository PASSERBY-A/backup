package com.cmsz.cloudplatform.web.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.DimResource;
import com.cmsz.cloudplatform.model.request.DimResourceTreeRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.DimResourceManager;
import com.cmsz.cloudplatform.web.core.BaseAction;

public class ResourceStatisticAction extends BaseAction {

	@Autowired
	private DimResourceManager dimResourceManager;

	public String listDimResourceTree() {
		DimResourceTreeRequest request = (DimResourceTreeRequest) this.wrapRequest(new DimResourceTreeRequest());
		ListResponse<DimResource> response = dimResourceManager.getDimResourceTree(request);
		
		writeResponse(response);
		return NONE;
	}
}
