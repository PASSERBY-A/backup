package com.cmsz.cloudplatform.web.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.ActiveUser;
import com.cmsz.cloudplatform.model.Log;
import com.cmsz.cloudplatform.model.request.ListLogRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.LogManager;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.Page;

public class LogAction extends BaseAction {

	private static final long serialVersionUID = 4450577432337012813L;
	@Autowired
	private LogManager logManager =  null;
	public LogAction() {
		super();
	}
	
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}

	public String list(){
		/*Date start = requestParams.get("start")==null?null : DateUtil.parseDateString(DateUtil.GMT_TIMEZONE, (String)requestParams.get("start")[0]);
		Date end = requestParams.get("end")==null?null : DateUtil.parseDateString(DateUtil.GMT_TIMEZONE, (String)requestParams.get("end")[0]);
		String module = requestParams.get("module")==null?null:(String)requestParams.get("module")[0];
		String operation = requestParams.get("operation")==null?null:(String)requestParams.get("operation")[0];
		String username = requestParams.get("username")==null?null:(String)requestParams.get("username")[0];
		int pageNo = requestParams.get("page")==null?null:Integer.parseInt(requestParams.get("page")[0].toString());
		int pageSize = requestParams.get("pagesize")==null?null:Integer.parseInt(requestParams.get("pagesize")[0].toString());
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		Page p = new Page();
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);*/
		
		
		
		ListLogRequest request = (ListLogRequest) this.wrapRequest(new ListLogRequest());
		//设置角色
		request.setRole(this.activeUser.getRole());
		
		request.setUserName(this.activeUser.getUserName());
		
		
		ListResponse<Log> response = logManager.list(request);
		
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("logs", response.getResponses());
		this.renderText(jsonPage.toString());
		return NONE;
	}
}
