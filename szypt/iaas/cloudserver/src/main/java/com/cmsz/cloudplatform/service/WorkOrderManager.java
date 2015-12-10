package com.cmsz.cloudplatform.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cmsz.cloudplatform.model.ActiveUser;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.request.ListWorkOrderRequest;
import com.cmsz.cloudplatform.model.request.SaveOrderRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.hp.core.service.GenericManager;

public interface WorkOrderManager extends GenericManager<WorkOrder,Long>{
	
	/**
	 * @author zhouwenb
	 * @return 
	 */
	
	public EntityResponse<WorkOrder> saveOrder(SaveOrderRequest request,ActiveUser user,HttpServletRequest req);
	
	/**
	 * @author Li Manxin
	 * @param request
	 * @return
	 */
	ListResponse<WorkOrder> listWorkOrder(ListWorkOrderRequest request);
	
	/***
	 * 检查状态
	 */
	WorkOrder checkStatus(int key,String name,String value);

	public List<WorkOrder> listWorkOrderByTypeAndStatus(List<Integer> typeList,List<Integer> statusList);

}
