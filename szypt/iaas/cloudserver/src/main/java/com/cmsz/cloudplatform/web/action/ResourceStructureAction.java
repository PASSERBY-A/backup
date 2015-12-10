package com.cmsz.cloudplatform.web.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.ResourceStructureVO;
import com.cmsz.cloudplatform.service.ResourceStructureManager;
import com.cmsz.cloudplatform.web.core.BaseAction;

public class ResourceStructureAction extends BaseAction {

	private static final long serialVersionUID = 477008290078985032L;
	@Autowired
	private ResourceStructureManager resourceStructureManager;

	public String getResource() {
		ListResponse<ResourceStructureVO> response = resourceStructureManager.getResource(requestParams);
		writeResponse(response);
		return NONE;
	}
}
