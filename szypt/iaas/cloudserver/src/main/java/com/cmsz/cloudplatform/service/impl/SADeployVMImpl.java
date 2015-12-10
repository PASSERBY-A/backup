package com.cmsz.cloudplatform.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.service.AbstractSAManager;
import com.cmsz.cloudplatform.utils.Compare;

@Service(value = "saDeployVMImpl")
public class SADeployVMImpl extends AbstractSAManager {
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	@Override
	public Map<String, String> getOrderTypeSpecificParams() {
		Map<String, String> woSpecificParams = new LinkedHashMap();
		woSpecificParams.put("commandName", "deployVirtualMachine");
		woSpecificParams.put("responseHead", "deployvirtualmachineresponse");
		woSpecificParams.put("jobResultresponseHead", "virtualmachine");
		return woSpecificParams;
	}

/*	public Map<String, Object[]> getProvisionAttributes(
			Map<String, Object[]> cloudStackParams, WorkOrder workOrder) {
		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		listUsersParams.put("id", new Object[] { workOrder.getApplierId() });

		Response listUsersResponse = genericCloudServerManager.get(
				listUsersParams, false);

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
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		cloudStackParams.put("response", new Object[] { "json" });
		super.getProvisionAttributes(cloudStackParams, workOrder);
		return cloudStackParams;
	};*/
	
	public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder){
    	
		 List<WorkItem> workItemList=workOrder.getWorkItems();
			Collections.sort(workItemList, new Compare());  
			//将工单的开通属性放入MAP中。
			for(WorkItem wi:workItemList){
				ProvisionAttribute pa=new ProvisionAttribute();
				pa.setWorkOrderType(workOrder.getWorkOrderType());
				List paList=getProvisionAttributeManager().findByExample(pa);
				Iterator iterPa=paList.iterator();
				while(iterPa.hasNext()){
					ProvisionAttribute pa1=(ProvisionAttribute)iterPa.next();
					if(pa1.getAttributeName().equalsIgnoreCase(ignoreFields)){
						continue;
					}
					String attributeName=wi.getAttributeName();
					if(pa1.getAttributeName().equalsIgnoreCase(wi.getAttributeName())){
						String attributeValue=wi.getAttributeValue();
						if(null!=attributeValue&&!"".equalsIgnoreCase(attributeValue.trim())){
							attributeName=attributeName.trim();
							attributeValue=attributeValue.trim();
							cloudStackParams.put(attributeName, new Object[]{attributeValue});
						}
						
					}
				}
	         }
			//System.out.println("projectid = -1 表示是默认视图."+cloudStackParams.get("projectid")[0].toString());
			//projectid = -1 表示是默认视图
			if(!cloudStackParams.containsKey("projectid") || "-1".equals(cloudStackParams.get("projectid")[0].toString())){
				//添加account和domainid参数，如果使用account参数时，domainid必须同时使用
				cloudStackParams.put("account", new Object[]{workOrder.getAccount()});
				cloudStackParams.put("domainid", new Object[]{workOrder.getDomainId()});
				//cloudStackParams.remove("projectid");
			}
			
			//删除diskofferingid
			if(cloudStackParams.containsKey("diskSize") && cloudStackParams.get("diskSize").length>0 && cloudStackParams.get("diskSize")[0]!=null
					&& StringUtils.isNotBlank(cloudStackParams.get("diskSize")[0].toString())){
				cloudStackParams.put("size", cloudStackParams.get("diskSize"));
				cloudStackParams.remove("diskSize");
			}
			return cloudStackParams;
		};

}
