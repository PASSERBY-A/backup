package com.cmsz.cloudplatform.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.cmsz.cloudplatform.model.ActiveUser;
import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.utils.Compare;
import com.cmsz.cloudplatform.utils.ServiceOptionUtil;

public abstract class AbstractSAManager implements SAManager {
	
	protected String ignoreFields = "applyreason";
	private WorkOrderManager orderManager=null;
	@Autowired
	private WorkItemManager itemManager=null;
	public WorkItemManager getItemManager() {
		return itemManager;
	}
	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}
	public WorkOrderManager getOrderManager() {
		return orderManager;
	}
	@Autowired
	public void setOrderManager(WorkOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	private ProvisionAttributeManager provisionAttributeManager=null;
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	public ProvisionAttributeManager getProvisionAttributeManager() {
		return provisionAttributeManager;
	}
	@Autowired
	public void setProvisionAttributeManager(
			ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
	
		this.genericCloudServerManager = genericCloudServerManager;
	}

	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
		
		return null;
	};

	public List<WorkItem> getCapObjWorkItem(long workOrderId){return null;}
	
	
	/*private Response loginCloudStack() {
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		param.put("command", new Object[] { "login" });
		param.put("username",
				new Object[] { ServiceOptionUtil.obtainCloudStackUsername() });
		param.put("password",
				new Object[] { ServiceOptionUtil.obtainCloudStackPassword() });
		param.put("response", new Object[] { "json" });
		Response loginResponse = genericCloudServerManager.post(param);
		return loginResponse;

	}*/
	public void putUserCredentialParams(Map<String,Object[]> cloudStackParams,WorkOrder workOrder){
		this.putUserCredentialParams(cloudStackParams, workOrder.getApplierId());
	}
	
/*	 protected void putUserCredentialParams(Map<String,Object[]> cloudStackParams,String userId){
		Response loginResponse = loginCloudStack();

		Map<String, Object[]> param = new HashMap<String, Object[]>();

		System.out.println("here is the response " + loginResponse.toString());
	
		JSONObject jo = JSONObject
				.fromObject(loginResponse.getResponseString());
		String sessionkey = "";
		try {
			jo = JSONObject.fromObject(jo.getString("loginresponse"));
			sessionkey = jo.getString("sessionkey");
		} catch (JSONException e) {
			
			return;
		}

		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		listUsersParams.put("id", new Object[] { userId });

		System.out.println("userid is "+userId);
		Response listUsersResponse = genericCloudServerManager.get(
				listUsersParams, false);
		System.out.println("listUsersResponse is "+listUsersResponse.toString());
		String apikey = "";
		String secretkey = "";
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			jo = JSONObject.fromObject(listUsersResponse.getResponseString());
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			apikey = jo.getString("apikey");
			secretkey = jo.getString("secretkey");
		}
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		cloudStackParams.put("sessionkey", new Object[] { sessionkey });
		cloudStackParams.put("response", new Object[] { "json" });
		
	};*/
	protected void putUserCredentialParams(Map<String,Object[]> cloudStackParams,String userId){
		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		//listUsersParams.put("id", new Object[] {userId});
		listUsersParams.put("accounttype", new Object[]{String.valueOf(ActiveUser.ADMIN.intValue())});//帐户类型
		Response listUsersResponse = genericCloudServerManager.get(
				listUsersParams, false);
		System.out.println("userid is "+userId);
		System.out.println("response is "+listUsersResponse.toString());
		String apikey = "";
		String secretkey = "";
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			JSONObject jo = JSONObject.fromObject(listUsersResponse
					.getResponseString());
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			apikey = jo.getString("apikey");
			secretkey = jo.getString("secretkey");
		}
		cloudStackParams= new HashMap<String, Object[]>();
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		cloudStackParams.put("response", new Object[] { "json" });
	}
    public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder){
    	
    	List<WorkItem> workItemList=workOrder.getWorkItems();
		Collections.sort(workItemList, new Compare());  
		//将工单的开通属性放入MAP中。
		for(WorkItem wi:workItemList){
			ProvisionAttribute pa=new ProvisionAttribute();
			pa.setWorkOrderType(workOrder.getWorkOrderType());
			List paList=provisionAttributeManager.findByExample(pa);
			Iterator iterPa=paList.iterator();
			while(iterPa.hasNext()){
				ProvisionAttribute pa1=(ProvisionAttribute)iterPa.next();
				if(pa1.getAttributeName().equalsIgnoreCase(wi.getAttributeName())){
					String attributeName=wi.getAttributeName();
					String attributeValue=wi.getAttributeValue();
					if(null!=attributeValue&&!"".equalsIgnoreCase(attributeValue.trim())){
						attributeName=attributeName.trim();
						attributeValue=attributeValue.trim();
						cloudStackParams.put(attributeName, new Object[]{attributeValue});
					
					}
					
				}
			}
         }
		
		return cloudStackParams;
	};
	  public void jobSucceedDeal(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject resultJSONObj,Map<String,String> woSpecificParams){

			JSONObject jobresultJO=resultJSONObj.getJSONObject("jobresult");
			String jobResultresponseHead=woSpecificParams.get("jobResultresponseHead");
			JSONObject resultHeadJO=jobresultJO.getJSONObject(jobResultresponseHead);
			String id=resultHeadJO.getString("id");
			workOrder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);//成功
			orderManager.save(workOrder);
			saveWorkItem(workOrder.getId(),3,"id",id);
			
		
}
	  public void saveWorkItem(long workOrderId,int step,String attributeName,String attributeValue){
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
			
		}
}
