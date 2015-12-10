package com.cmsz.cloudplatform.web.action;

import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.request.ListTEquipmenteventRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.EquipmentEventManager;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.Page;

public class EquipmentEventAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5714235067822039049L;
	@Autowired
	private EquipmentEventManager equipmentEventManager;
	
	public String list(){
		
		ListTEquipmenteventRequest  request = (ListTEquipmenteventRequest ) this.wrapRequest(new ListTEquipmenteventRequest ());
		
		ListResponse<TEquipmentevent> response = equipmentEventManager.list(request);
		
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("messages", response.getResponses());
		this.renderText(jsonPage.toString());
		return NONE;
	}
	
	
	public String remove(){
		String id = request.getParameter("id");
		String msg = null;
		
		try {
			equipmentEventManager.remove(new Long(id));
			msg="删除消息成功";
			//logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.DELETE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.SUCCESS, msg);
		} catch (Exception e) {
			msg="删除消息错误,请稍后再试";
			e.printStackTrace();
			//logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.DELETE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.FAIL, msg);
		}
		Response response = this.transformResponse(msg, "json", HttpStatus.SC_OK);
		writeResponse(response);
		return NONE;
	}
}
