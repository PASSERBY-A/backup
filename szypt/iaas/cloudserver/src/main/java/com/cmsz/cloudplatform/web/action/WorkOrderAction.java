package com.cmsz.cloudplatform.web.action;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.request.ListWorkOrderRequest;
import com.cmsz.cloudplatform.model.request.SaveOrderRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.LogManager;
import com.cmsz.cloudplatform.service.ProvisionAttributeManager;
import com.cmsz.cloudplatform.service.ProvisionAttributeValueManager;
import com.cmsz.cloudplatform.service.WorkItemManager;
import com.cmsz.cloudplatform.service.WorkOrderManager;
import com.cmsz.cloudplatform.service.impl.AttributeValueManagerFactory;
import com.cmsz.cloudplatform.utils.LogConstants;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.Page;

public class WorkOrderAction extends BaseAction{

	/**
	 * @author zhouwenb
	 * 提交工单
	 */
	@Autowired
	private LogManager logManager;
	
	private static final long serialVersionUID = 1032232232L;
	
	private WorkOrderManager orderManager=null;
	
	private WorkOrder order=null;

	public WorkOrderAction(){
		super();
	}
	
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

	public WorkOrder getOrder() {
		return order;
	}

	public void setOrder(WorkOrder order) {
		this.order = order;
	}
	
	public String save(){
		String msg = "SUCCESS";
		SaveOrderRequest request = (SaveOrderRequest) this.wrapRequest(new SaveOrderRequest());
		EntityResponse<WorkOrder> entityResponse = this.orderManager.saveOrder(request,this.activeUser,this.request);
		//EntityResponse<WorkItem> entityResponse2 = this.itemManager.saveItem(request,entityResponse.getEntity().getId(),this.request);
		Response response = this.transformResponse(msg, "json", HttpStatus.SC_OK);
		logManager.log(this.request.getRemoteHost(), LogConstants.SERVICE_VM, LogConstants.CREATE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.SUCCESS, msg);
		writeResponse(response);
		
		return NONE;
	}
	public String list(){
		ListWorkOrderRequest request = (ListWorkOrderRequest) this.wrapRequest(new ListWorkOrderRequest());
		
		ListResponse<WorkOrder> response = orderManager.listWorkOrder(request);
		
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("workOrders", response.getResponses());
		this.renderText(jsonPage.toString());
		return NONE;
	}
	private ProvisionAttributeManager provisionAttributeManager;
	
	@Autowired
	public void setProvisionAttributeManager(
			ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}
	
	
	public String getAttribute(){
		String workordertype = this.requestParams.containsKey("workordertype")?(String)requestParams.get("workordertype")[0]:"-1";
		ProvisionAttribute  pa = new ProvisionAttribute();
		pa.setWorkOrderType(Integer.parseInt(workordertype));
		List<ProvisionAttribute> pas = (List<ProvisionAttribute>) provisionAttributeManager.findByExample(pa);
		JSONObject jsonObj = new JSONObject();
		Collections.sort(pas);
		jsonObj.put("fields",pas);
		this.renderText(jsonObj.toString());
		return NONE;
	}
	
	public String getExtval(){
		String workordertype = this.requestParams.containsKey("workordertype")?(String)requestParams.get("workordertype")[0]:"-1";
		String attributeName = this.requestParams.containsKey("attributename")?(String)requestParams.get("attributename")[0]:"";
		ProvisionAttributeValueManager attributeValManager = AttributeValueManagerFactory.createManager(Integer.parseInt(workordertype));
		String sessionkey = requestParams.containsKey("sessionkey")?(String)requestParams.get("sessionkey")[0]:"";
		Map<String,Object> result = attributeValManager.getExtval(attributeName, sessionkey, this.requestParams);
		JSONObject jsonObj = null;
		JSONArray array = new JSONArray();
		if(result!=null && result.size()>0){
			Iterator<Entry<String,Object>> iterator =  result.entrySet().iterator();
			while(iterator.hasNext()){
				jsonObj = new JSONObject();
				Entry<String,Object> keyValue = iterator.next();
				jsonObj.put("id", keyValue.getKey());
				if(keyValue.getValue() instanceof String){
					jsonObj.put("description",keyValue.getValue());
				}else{
					if(keyValue.getValue() instanceof JSONObject){
						if(workordertype.equalsIgnoreCase(String.valueOf(WorkOrder.TYPE_NEWVOLUME))){
							jsonObj.put("description", ((JSONObject)keyValue.getValue()).getString("displaytext"));
							jsonObj.put("iscustomized", ((JSONObject)keyValue.getValue()).getBoolean("iscustomized")?1:0);
						}else if(workordertype.equalsIgnoreCase(String.valueOf(WorkOrder.TYPE_DEPLOYVIRTUALMACHINE))){
							jsonObj.put("description", ((JSONObject)keyValue.getValue()).getString("displaytext"));
							jsonObj.put("iscustomized", ((JSONObject)keyValue.getValue()).getBoolean("iscustomized")?1:0);
						}
					}
				}
				
				array.add(jsonObj);
			}
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("keyValues", array);
		this.renderText(resultJson.toString());
		return NONE;
	}
	
	
	public String checkStatus(){
		int key=Integer.parseInt(request.getParameter("workordertype"));
		WorkOrder order=new WorkOrder();
		if(key==2){
			order=this.orderManager.checkStatus(2,"projectid",request.getParameter("projectid"));
		}else if(key==3){
			order=this.orderManager.checkStatus(3,"domainId",request.getParameter("domainId"));
		}else if(key==4){
			order=this.orderManager.checkStatus(4,"account",request.getParameter("account"));
		}else if(key==6){
			order=this.orderManager.checkStatus(6,"vmid",request.getParameter("vmid"));
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("status", order.getStatus());
		this.renderText(resultJson.toString());
		return NONE;
	}
}
