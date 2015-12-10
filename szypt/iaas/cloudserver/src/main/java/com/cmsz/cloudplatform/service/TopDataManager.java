package com.cmsz.cloudplatform.service;

import java.util.Map;

import com.cmsz.cloudplatform.model.DimResource;
import com.cmsz.cloudplatform.model.request.ListTopDataRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.TopDataVO;
import com.hp.core.service.GenericManager;

public interface TopDataManager extends GenericManager<DimResource, Long> {

	/**
	 * 
	 * @param request
	 * @param requestParams
	 * @return
	 */
	public ListResponse<TopDataVO> getTopData(ListTopDataRequest request, Map<String, Object[]> requestParams);
}
