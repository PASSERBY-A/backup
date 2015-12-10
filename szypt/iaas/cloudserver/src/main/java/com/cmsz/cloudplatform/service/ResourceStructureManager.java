package com.cmsz.cloudplatform.service;

import java.util.Map;

import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.ResourceStructureVO;

public interface ResourceStructureManager {

	/**
	 * 
	 * @param request
	 * @return
	 */
	public ListResponse<ResourceStructureVO> getResource(Map<String, Object[]> params);
}
