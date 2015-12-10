package com.cmsz.cloudplatform.service.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.request.ListWorkOrderRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.ProvisionAttributeManager;
import com.cmsz.cloudplatform.service.ResourcePoolPermissionManager;
import com.cmsz.cloudplatform.service.SAManager;
import com.cmsz.cloudplatform.service.WorkItemManager;
import com.cmsz.cloudplatform.service.WorkOrderManager;
import com.cmsz.cloudplatform.utils.Compare;
@Service(value="saAsyncHelper")

public class SAAsyncHelper {


	static String JOBSTATUS_PENDING="0";
	static String JOBSTATUS_SUCCEED="1";
	static String JOBSTATUS_FAIL="2";
	
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private SAManager saNewProjectImpl;
	@Autowired
	private SAManager saNewVolumeImpl;
	@Autowired
	private SAManager saDeployVMImpl;
	@Autowired
    private SAManager saScaleVMImpl;
	@Autowired
	private WorkItemManager itemManager;
	@Autowired
	private WorkOrderManager orderManager;
	
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	public SAManager getSaNewProjectImpl() {
		return saNewProjectImpl;
	}

	public void setSaNewProjectImpl(SAManager saNewProjectImpl) {
		this.saNewProjectImpl = saNewProjectImpl;
	}

	public SAManager getSaNewVolumeImpl() {
		return saNewVolumeImpl;
	}

	public void setSaNewVolumeImpl(SAManager saNewVolumeImpl) {
		this.saNewVolumeImpl = saNewVolumeImpl;
	}

	public SAManager getSaDeployVMImpl() {
		return saDeployVMImpl;
	}

	public void setSaDeployVMImpl(SAManager saDeployVMImpl) {
		this.saDeployVMImpl = saDeployVMImpl;
	}

	public SAManager getSaScaleVMImpl() {
		return saScaleVMImpl;
	}

	public void setSaScaleVMImpl(SAManager saScaleVMImpl) {
		this.saScaleVMImpl = saScaleVMImpl;
	}

	public WorkItemManager getItemManager() {
		return itemManager;
	}

	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public WorkOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(WorkOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	/*public void saveWorkItem(long workOrderId,int step,String attributeName,String attributeValue){
		WorkItem wi=new WorkItem();
		wi.setWorkOrderId(workOrderId);
		wi.setStep(3);
		wi.setAttributeName(attributeName);
		wi.setAttributeValue(attributeValue);
		wi.setDescript("inserted by service active module");
		wi.setCreatedBy("admin");
		wi.setCreatedOn(new Date());
		wi.setModifiedBy("admin");
		wi.setModifiedOn(new Date());
		itemManager.save(wi);
		
	}*/
	
	public LinkedHashMap<String,Object[]> refreshCloudStackParams(Map<String, Object[]> cloudStackParams){
		 Object[] apikey=cloudStackParams.get("apikey");
		 Object[] secretkey=cloudStackParams.get("secretkey");
		 Object[] sessionkey=cloudStackParams.get("sessionkey");
		 Object[] response=cloudStackParams.get("response");
		 LinkedHashMap<String,Object[]>  cloudStackParams2=new  LinkedHashMap<String,Object[]>();
		cloudStackParams2.put("apikey", apikey);
		cloudStackParams2.put("secretkey",secretkey);
		cloudStackParams2.put("sessionkey", sessionkey);
		cloudStackParams2.put("response", response);
		return cloudStackParams2;
	}
	
	
	public void active(Map<String,Object[]> cloudStackParams){
	
		List<Integer> typeList=new ArrayList<Integer>();
		typeList.add(WorkOrder.TYPE_NEWPROJECTAPPLICATION);
		typeList.add(WorkOrder.TYPE_DEPLOYVIRTUALMACHINE);
		typeList.add(WorkOrder.TYPE_SCALEVIRTUALMACHINE);
		typeList.add(WorkOrder.TYPE_NEWVOLUME);
	
		List<Integer> statusList=new ArrayList<Integer>();
		statusList.add(WorkOrder.STATUS_APPROVED);


		List<WorkOrder> workOrderList=orderManager.listWorkOrderByTypeAndStatus(typeList, statusList);
		

		for(WorkOrder workOrder:workOrderList){
		SAManager saManager=this.getSAManager(workOrder.getWorkOrderType());
		Map<String,String> woSpecificParams =saManager.getOrderTypeSpecificParams();

		String commandName=woSpecificParams.get("commandName");
		String responseHead=woSpecificParams.get("responseHead");
		
		cloudStackParams=this.refreshCloudStackParams(cloudStackParams);
		saManager.putUserCredentialParams(cloudStackParams, workOrder);
		
		saManager.getProvisionAttributes(cloudStackParams, workOrder);
		cloudStackParams.put("command", new Object[]{commandName});
		Response response = genericCloudServerManager
				.get(cloudStackParams);
		JSONObject JSONObj = JSONObject
				.fromObject(response.getResponseString());
		String commandresponseStr=JSONObj.getString(responseHead);
		
		if(commandresponseStr.indexOf("errorcode")>-1){
			
			
			for(String key:cloudStackParams.keySet()){
				System.out.println("llllllllllllllllllllllll :"+key+" value is "+cloudStackParams.get(key)[0]);
			}
			
			JSONObject createprojectresponseObj=JSONObject.fromObject(commandresponseStr);
			String errorcode =createprojectresponseObj.getString("errorcode");
			String errortext =createprojectresponseObj.getString("errortext");
			workOrder.setStatus(WorkOrder.STATUS_PROVISONFAIL);//处理失败
			workOrder.setErrorCode(errorcode);
			workOrder.setErrorText(errortext);
			orderManager.save(workOrder);
		

		}else{
			JSONObject commandresponseObj=JSONObject.fromObject(commandresponseStr);
			this.jobDeal(cloudStackParams,woSpecificParams, workOrder, commandresponseObj,saManager);
			}
		}
	}
	
	public void jobDeal(Map<String, Object[]> cloudStackParams,Map<String,String> woSpecificParams,WorkOrder workOrder,JSONObject commandresponseObj,SAManager saManager ){
		cloudStackParams=this.refreshCloudStackParams(cloudStackParams);
		String jobid =commandresponseObj.getString("jobid");
		cloudStackParams.put("command", new Object[]{"queryAsyncJobResult"});
		cloudStackParams.put("jobId", new Object[]{jobid});

		String jobstatus=this.JOBSTATUS_PENDING;
		JSONObject resultJSONObj=null;
				while(jobstatus.equalsIgnoreCase(this.JOBSTATUS_PENDING)){
					try{
						System.out.println("sleeping now");
						Thread.sleep(3000);
					}catch(Exception e){e.printStackTrace();}
					Response queryasyncjobresult2 = genericCloudServerManager
							.get(cloudStackParams);
					JSONObject queryasyncjobresultJSONObj2 = JSONObject
							.fromObject(queryasyncjobresult2.getResponseString());
					
					String queryasynjobresultStr2=queryasyncjobresultJSONObj2.toString();
						   JSONObject queryasynjobresultJSONObj2=JSONObject.fromObject(queryasynjobresultStr2);
							String resultStr2=queryasynjobresultJSONObj2.getString("queryasyncjobresultresponse");
							 resultJSONObj=JSONObject.fromObject(resultStr2);
							jobstatus=resultJSONObj.getString("jobstatus");
				}
				
				if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_SUCCEED)){
					saManager.jobSucceedDeal(cloudStackParams, workOrder, resultJSONObj, woSpecificParams);
				}else if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_FAIL)){
					String jobresultcode=resultJSONObj.getString("jobresultcode");
					String jobresult=resultJSONObj.getString("jobresult");
					workOrder.setStatus(WorkOrder.STATUS_PROVISONFAIL);//处理失败
					workOrder.setErrorCode(jobresultcode);
					workOrder.setErrorText(jobresult);
					orderManager.save(workOrder);
				
				}
				
	}
	
public SAManager getSAManager(int workOrderType){
	if(workOrderType==WorkOrder.TYPE_NEWPROJECTAPPLICATION) return saNewProjectImpl;
	else if(workOrderType==WorkOrder.TYPE_DEPLOYVIRTUALMACHINE) return saDeployVMImpl;
	else if(workOrderType==WorkOrder.TYPE_SCALEVIRTUALMACHINE) return saScaleVMImpl;
	else if(workOrderType==WorkOrder.TYPE_NEWVOLUME) return saNewVolumeImpl;
	else return null;
}


}
