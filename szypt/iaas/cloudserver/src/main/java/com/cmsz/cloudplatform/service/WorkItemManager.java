package com.cmsz.cloudplatform.service;

import javax.servlet.http.HttpServletRequest;

import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.request.SaveOrderRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.hp.core.service.GenericManager;

public interface WorkItemManager extends GenericManager<WorkItem,Long>{
	
	/**
	 * @author zhouwenb
	 * @return 
	 */
	
	public EntityResponse<WorkItem> saveItem(SaveOrderRequest req,Long id,HttpServletRequest request);
/*	public void saveWorkItem(long workOrderId, int step, String attributeName,
			String attributeValue);*/
}
