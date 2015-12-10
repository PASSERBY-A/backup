package com.cmsz.cloudplatform.service;

import java.util.Map;


import com.cmsz.cloudplatform.model.ActiveUser;

public interface WorkOrderApprovalManager {
	
	/**审批通过*/
	int AGREE = 1;
	/**审批不通过*/
	int REJECT =0;
	public void approve(Map attributeItem,ActiveUser user,Long workOrderId, int approveResult, String approveDesc);
}
