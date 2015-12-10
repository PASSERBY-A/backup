package com.cmsz.cloudplatform.web.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.TemplateAccount;
import com.cmsz.cloudplatform.model.response.ErrorResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.TemplateAccountManager;
import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.web.core.BaseAction;

public class TemplateAccountAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4751146907746240132L;
	
	@Autowired
	private TemplateAccountManager templateAccountManager = null;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager ;
	
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	public void setTemplateAccountManager(
			TemplateAccountManager templateAccountManager) {
		this.templateAccountManager = templateAccountManager;
	}
	
	public String getTemplateAccount(){
		String templateId = requestParams.containsKey("templateId")?(String)this.requestParams.get("templateId")[0]:"";
		String id = requestParams.containsKey("id")?(String)this.requestParams.get("id")[0]:"";
		List<TemplateAccount>  templateAccounts=  templateAccountManager.getByTemplateIdOrAccoutId(templateId, id);
//		
//		this.genericCloudServerManager
		
		Response response = transformResponse(templateAccounts, null, 200);
		writeResponse(response);
		//Object to JSONString 
		//Write to response
		return NONE;
	}
	
	public String create(){
		String name = (String)requestParams.get("name")[0];
		String password = (String)requestParams.get("password")[0];
		String type = (String)requestParams.get("type")[0];
		String templateId = (String)requestParams.get("templateId")[0];
		String responseType = (String)requestParams.get("response")[0];
		TemplateAccount templateAccount = new TemplateAccount();
		templateAccount.setAccountName(name);
		templateAccount.setPassword(password);
		templateAccount.setAccountType(type);
		templateAccount.setCreatedBy(this.activeUser.getUserid());
		templateAccount.setCreatedOn(new Date());
		templateAccount.setTemplateId(templateId);
		templateAccount = templateAccountManager.save(templateAccount);
		if(templateAccount.getId()!=null){
			Response response = this.transformResponse(templateAccount,responseType, HttpStatus.SC_OK);
			writeResponse(response);
		}else{
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			errorResponse.setErrorText("Create template-account failed,please check data or contack administrator!");
			writeResponse(errorResponse);
		}
		return NONE;
	}
	
	public String remove(){
		String id = requestParams.containsKey("id")?(String) requestParams.get("id")[0]:"0";
		if(StringUtils.isNotBlank(id)){
			templateAccountManager.remove(Long.valueOf(id));
			/*Response response = new Response();
			response.setStatusCode(statusCode)
			writerResponse(sr);*/
		}
		return NONE;
	}
	
}
