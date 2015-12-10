package com.cmsz.cloudplatform.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.AbstractSAManager;
import com.cmsz.cloudplatform.service.ProvisionAttributeManager;
import com.cmsz.cloudplatform.utils.Compare;
@Service(value="saNewVolumeImpl")
public class SANewVolumeImpl extends AbstractSAManager {
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
				Map<String,String> woSpecificParams =new LinkedHashMap();
				woSpecificParams.put("commandName", "createVolume");
				woSpecificParams.put("responseHead", "createvolumeresponse");
				woSpecificParams.put("jobResultresponseHead", "volume");
				return woSpecificParams; 
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
				if(pa1.getAttributeName().equalsIgnoreCase(ignoreFields)){
					continue;
				}
				if(pa1.getAttributeName().equalsIgnoreCase(wi.getAttributeName())){
					String attributeName=wi.getAttributeName();
					String attributeValue=wi.getAttributeValue();
					if(null!=attributeValue&&!"".equalsIgnoreCase(attributeValue.trim())){
						attributeName=attributeName.trim();
						attributeValue=attributeValue.trim();
						if("virtualmachineid".equalsIgnoreCase(attributeName)){
					         this.putZoneId(cloudStackParams, attributeValue);
						}else {
							cloudStackParams.put(attributeName, new Object[]{attributeValue});
						}
					}
					
				}
			}
			
         }
		if(cloudStackParams.containsKey("iscustomized") && cloudStackParams.get("iscustomized").length==1 && cloudStackParams.get("iscustomized")[0].toString().equals("1")){
			cloudStackParams.put("size",cloudStackParams.get("diskSize"));
		}
		
		if(cloudStackParams.containsKey("diskSize")){
			cloudStackParams.remove("diskSize");
		}
		if(cloudStackParams.containsKey("iscustomized")){
			cloudStackParams.remove("iscustomized");
		}
		//System.out.println("projectid = -1 表示是默认视图."+cloudStackParams.get("projectid")[0].toString());
		if(!cloudStackParams.containsKey("projectid") || "-1".equals(cloudStackParams.get("projectid")[0].toString())){
			
		//	cloudStackParams.remove("projectid");
			//附加字段
			cloudStackParams.put("domainId", new Object[]{workOrder.getDomainId()});
			cloudStackParams.put("account", new Object[]{workOrder.getAccount()});
			//workOrder.getAccountId()
		}
		//cloudStackParams.put("userid", new Object[]{"a65e6a04-6e86-42c9-bfdb-9f9d77e56b3c"});
		
		//=========================================================添加卷需添加的用户登录开通才能到对应帐号下面去，下面是用当前用的sessionkey
		
		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		listUsersParams.put("account", new Object[] { workOrder.getAccount() });
		listUsersParams.put("domainid", new Object[] { workOrder.getDomainId()});

		Response listUsersResponse = genericCloudServerManager.get(
				listUsersParams, false);

		String apikey = "";
		String secretkey = "";
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			JSONObject jo = JSONObject.fromObject(listUsersResponse.getResponseString());
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			apikey = jo.getString("apikey");
			secretkey = jo.getString("secretkey");
		}
	
		//Map<String, Object[]> cloudStackParams = new HashMap<String, Object[]>();
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		//cloudStackParams.put("sessionkey", new Object[] { sessionkey });
		cloudStackParams.put("response", new Object[] { "json" });
		//=========================================================
		
		return cloudStackParams;
	};
	public void putZoneId(Map<String, Object[]> cloudStackParams,String vmId){
		 Object[] apikey=cloudStackParams.get("apikey");
		 Object[] secretkey=cloudStackParams.get("secretkey");
		 Object[] sessionkey=cloudStackParams.get("sessionkey");
		 Object[] response=cloudStackParams.get("response");
		 LinkedHashMap<String,Object[]>  newcloudStackParams=new  LinkedHashMap<String,Object[]>();
		 newcloudStackParams.put("apikey", apikey);
		 newcloudStackParams.put("secretkey",secretkey);
		 newcloudStackParams.put("sessionkey", sessionkey);
		newcloudStackParams.put("response", response);
		
		newcloudStackParams.put("command", new Object[]{"listVirtualMachines"});
		newcloudStackParams.put("id", new Object[]{vmId});
		
		Response listVirtualMachinesResponse = genericCloudServerManager
				.get(newcloudStackParams);
		JSONObject listVirtualMachinesResponseJSONObj = JSONObject
				.fromObject(listVirtualMachinesResponse.getResponseString());
		System.out.println("apikey"+apikey[0]);
		System.out.println("JSON IS "+listVirtualMachinesResponseJSONObj.toString());
		String zoneid = ""; 
		if(listVirtualMachinesResponseJSONObj.containsKey("listvirtualmachinesresponse")){
			listVirtualMachinesResponseJSONObj = listVirtualMachinesResponseJSONObj.getJSONObject("listvirtualmachinesresponse");
			if(listVirtualMachinesResponseJSONObj.containsKey("count") && listVirtualMachinesResponseJSONObj.getInt("count")>0){
				zoneid = listVirtualMachinesResponseJSONObj.getJSONArray("virtualmachine").getJSONObject(0).getString("zoneid");
			}
		}
		//String zoneid=listVirtualMachinesResponseJSONObj.getJSONObject("listvirtualmachinesresponse").getJSONArray("virtualmachine").getJSONObject(0).getString("zoneid");
		cloudStackParams.put("zoneId", new Object[]{zoneid});
		
		
	}
	
	
}
