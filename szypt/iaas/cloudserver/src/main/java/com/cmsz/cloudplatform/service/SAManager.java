package com.cmsz.cloudplatform.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;

public interface SAManager {
	public Map<String,String> getOrderTypeSpecificParams();
    public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder);
    public void jobSucceedDeal(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject resultJSONObj,Map<String,String> woSpecificParams);
    public  void putUserCredentialParams(Map<String,Object[]> cloudStackParams,WorkOrder workOrder);
}
