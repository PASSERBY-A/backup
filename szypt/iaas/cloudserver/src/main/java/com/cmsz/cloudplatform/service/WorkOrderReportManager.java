package com.cmsz.cloudplatform.service;

import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.request.ListWorkOrderReportRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.WorkOrderReportVO;
import com.hp.core.service.GenericManager;

public interface WorkOrderReportManager extends GenericManager<WorkOrder, Long> {

	/**
	 * 
	 * @param req
	 * @return
	 */
	public ListResponse<WorkOrderReportVO> getWorkOrderReportData(ListWorkOrderReportRequest req);
}
