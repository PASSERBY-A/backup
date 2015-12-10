package com.cmsz.cloudplatform.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.AbstractSAManager;
import com.cmsz.cloudplatform.service.ResourcePoolPermissionManager;
import com.cmsz.cloudplatform.service.SAAsynManager;
import com.cmsz.cloudplatform.service.WorkItemManager;
import com.cmsz.cloudplatform.service.WorkOrderManager;
import com.cmsz.cloudplatform.utils.Compare;
import com.cmsz.cloudplatform.utils.ServiceOptionUtil;

@Service(value="saNewProjectImpl")
public class SANewProjectImpl extends AbstractSAManager {
	
	static String PRODUCTIONPOOL="R_01";
	static String TESTINGPOOL="R_02";
	static String ALLPOOL="all";
	
	static String RESOURCEPOOLPERMISSIONPERMIT="1";
	static String RESOURCEPOOLPERMISSIONREJECT="0";
	static int OWNERTYPEPROJECT=3;

	

	@Autowired
	private ResourcePoolPermissionManager resourcePoolPermissionManager;
	@Autowired

	public ResourcePoolPermissionManager getResourcePoolPermissionManager() {
		return resourcePoolPermissionManager;
	}
	public void setResourcePoolPermissionManager(
			ResourcePoolPermissionManager resourcePoolPermissionManager) {
		this.resourcePoolPermissionManager = resourcePoolPermissionManager;
	}
	private WorkOrderManager orderManager=null;
	public WorkOrderManager getOrderManager() {
		return orderManager;
	}
	@Autowired
	public void setOrderManager(WorkOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public WorkItemManager getItemManager() {
		return itemManager;
	}
	@Autowired
	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
	private WorkItemManager itemManager=null;
	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
				Map<String,String> woSpecificParams =new LinkedHashMap();
				woSpecificParams.put("commandName", "createProject");
				woSpecificParams.put("responseHead", "createprojectresponse");
				woSpecificParams.put("jobResultresponseHead", "project");
				return woSpecificParams; 
			}
	
	public void saveResourcePoolPermission(Map<String, Object[]> cloudStackParams,String projectid,String projectname,WorkOrder workOrder){
		ResourcePoolPermission p=new ResourcePoolPermission();
		p.setOwnerId(projectid);
		p.setOwnerName(projectname);
		p.setOwnerType(this.OWNERTYPEPROJECT);
		String resourcePoolId="";
		WorkItem wiRP=new WorkItem();
		wiRP.setWorkOrderId(workOrder.getId());
		wiRP.setAttributeName("resourcePoolId");
		
		List<WorkItem> wiRPl=(List<WorkItem>) itemManager.findByExample(wiRP);
		Collections.sort(wiRPl, new Compare());  
		cloudStackParams =this.refreshCloudStackParams(cloudStackParams);
		for(WorkItem wi:wiRPl){
			resourcePoolId=wi.getAttributeValue();
		}
		
		if(this.PRODUCTIONPOOL.equalsIgnoreCase(resourcePoolId)){
			p.setProductionPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONPERMIT));
			p.setTestingPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONREJECT));	
		}
		else if(this.TESTINGPOOL.equalsIgnoreCase(resourcePoolId)){
			p.setProductionPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONREJECT));
			p.setTestingPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONPERMIT));	
		}
		else if(this.ALLPOOL.equalsIgnoreCase(resourcePoolId)){
			p.setProductionPool(Integer.parseInt(RESOURCEPOOLPERMISSIONPERMIT));
			p.setTestingPool(Integer.parseInt(RESOURCEPOOLPERMISSIONPERMIT));	
			
		}
		p.setCreatedBy("admin");
		p.setCreatedOn(new Date());
		
		resourcePoolPermissionManager.save(p);
	
		
		
	}
	
	public void updateResourceLimit(Map<String, Object[]> cloudStackParams,String projectid,WorkOrder workOrder){
		
		LinkedHashMap<String,String> map=this.getCapabilityMap();
		for(String capabilityName:map.keySet()){
			WorkItem wiExample=new WorkItem();
			wiExample.setWorkOrderId(workOrder.getId());
			wiExample.setAttributeName(capabilityName);
			//wiExample.setStep(2);
			
			List<WorkItem> wil=(List<WorkItem>) itemManager.findByExample(wiExample);
			Collections.sort(wil, new Compare());  
			cloudStackParams =this.refreshCloudStackParams(cloudStackParams);
			for(WorkItem wi:wil){
				String attributeValue=wi.getAttributeValue();
				cloudStackParams.put("max", new Object[]{attributeValue});
			}
		
			cloudStackParams.put("command", new Object[]{"updateResourceLimit"});
			cloudStackParams.put("resourcetype", new Object[]{map.get(capabilityName)});
			cloudStackParams.put("projectid", new Object[]{projectid});
			
			Response updateLimitResponse = genericCloudServerManager
					.get(cloudStackParams);
			JSONObject updateLimitResponseJSONObj = JSONObject
					.fromObject(updateLimitResponse.getResponseString());
			
			
			String updateresourcelimitresponseStr=updateLimitResponseJSONObj.getString("updateresourcelimitresponse");
			if(updateresourcelimitresponseStr.indexOf("errorcode")>-1){
				JSONObject updateresourcelimitresponseObj=JSONObject.fromObject(updateresourcelimitresponseStr);
				String errorcode =updateresourcelimitresponseObj.getString("errorcode");
				String errortext =updateresourcelimitresponseObj.getString("errortext");
				workOrder.setStatus(WorkOrder.STATUS_PROVISONFAIL);//处理失败
				orderManager.save(workOrder);
				saveWorkItem(workOrder.getId(),3,"errorcode",errorcode);
				saveWorkItem(workOrder.getId(),3,"errortext",errortext);
				
				this.refreshCloudStackParams(cloudStackParams);
				cloudStackParams.put("command", new Object[]{"deleteProject"});
				cloudStackParams.put("id", new Object[]{projectid});
				Response deleteProjectResponse = genericCloudServerManager
						.get(cloudStackParams);
				
				JSONObject deleteProjectResponseSSS = JSONObject
						.fromObject(deleteProjectResponse.getResponseString());
				break;
	
			}
		}
	}
/*		public void saveWorkItem(long workOrderId,int step,String attributeName,String attributeValue){
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
		public LinkedHashMap<String, String> getCapabilityMap(){
			LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
			map.put("instance", "0");
			map.put("ip", "1");
			map.put("volume", "2");
			map.put("snapshot", "3");
			map.put("template", "4");
			map.put("network", "6");
			map.put("vpc", "7");
			map.put("cpu", "8");
			map.put("memory", "9");
			map.put("primaryStorage", "10");
			map.put("secondaryStorage", "11");
			
			return map;
			
		}
		  public void jobSucceedDeal(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject resultJSONObj,Map<String,String> woSpecificParams){


				System.out.println("command succeeded");
				JSONObject jobresultJO=resultJSONObj.getJSONObject("jobresult");
				String jobResultresponseHead=woSpecificParams.get("jobResultresponseHead");

				JSONObject resultHeadJO=jobresultJO.getJSONObject(jobResultresponseHead);
				String id=resultHeadJO.getString("id");
				String projectname=resultHeadJO.getString("name");
				this.saveResourcePoolPermission(cloudStackParams, id, projectname, workOrder);
				workOrder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);//成功
				orderManager.save(workOrder);
				saveWorkItem(workOrder.getId(),3,"id",id);
				this.updateResourceLimit(cloudStackParams, id,  workOrder);
				
			
				
			
	}
			public void putUserCredentialParams(Map<String,Object[]> cloudStackParams,WorkOrder workOrder){
				
				this.putUserCredentialParams(cloudStackParams,ServiceOptionUtil.obtainCloudStackUserId());
				cloudStackParams.put("domainid", new Object[]{workOrder.getDomainId()});
				cloudStackParams.put("account", new Object[]{workOrder.getAccount()});
			}
}
