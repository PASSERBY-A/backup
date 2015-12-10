package com.cmsz.cloudplatform.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.service.AbstractSAManager;
import com.cmsz.cloudplatform.utils.Compare;


@Service(value="saScaleVMImpl")
public class SAScaleVMImpl extends AbstractSAManager {
	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
		Map<String,String> woSpecificParams =new LinkedHashMap();
		woSpecificParams.put("commandName", "scaleVirtualMachine");
		woSpecificParams.put("responseHead", "scalevirtualmachineresponse");
		woSpecificParams.put("jobResultresponseHead", "virtualmachine");
				return woSpecificParams; 
			}
	
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
					String attributeName=wi.getAttributeName();
					
					if(attributeName.equalsIgnoreCase("vmid")){
						attributeName = "id";
					}
					if(attributeName.equalsIgnoreCase(ignoreFields)){
						continue;
					}
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
			
			return cloudStackParams;
		};
}