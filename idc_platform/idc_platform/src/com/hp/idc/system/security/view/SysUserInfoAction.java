package com.hp.idc.system.security.view;

import java.util.*;


import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Required;


import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.common.core.view.NoLogin;
import com.hp.idc.system.security.entity.SysUser;
import com.hp.idc.system.security.service.SercurityService;



public class SysUserInfoAction extends AbstractAction implements NoLogin{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1466590947593166026L;

	
	private SysUser sysUser;
	
	private List<SysUser> resultList;

	@Resource
	private SercurityService sercurityService;

	private String loginName;
	private String actualName;
	private String password;
	
	public String preQueryUser(){
		return SUCCESS;
	}
	public String querySysUser(){
		Map<String, Object> paramMap=new HashMap<String, Object>();
		LinkedHashMap<String, String> sortMap=new LinkedHashMap<String, String>();
		Page<SysUser> page=sercurityService.querySysUserList(paramMap, sortMap, start/limit+1, limit);
		List<SysUser> list=page.getResult();
		jsonObject=new JSONObject();
		jsonArray=new JSONArray();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"groups","roles"});
		for(SysUser user:list){
			JSONObject json=JSONObject.fromObject(user,jsonConfig);
			jsonArray.add(json);
		}
		jsonObject.put(SysUserInfoAction.JSON_RESULT, jsonArray);
		jsonObject.put(SysUserInfoAction.JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;
	}
	
	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public List<SysUser> getResultList() {
		return resultList;
	}

	public void setResultList(List<SysUser> resultList) {
		this.resultList = resultList;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getActualName() {
		return actualName;
	}

	public void setActualName(String actualName) {
		this.actualName = actualName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Required
	public SercurityService getSercurityService() {
		return sercurityService;
	}
	public void setSercurityService(SercurityService sercurityService) {
		this.sercurityService = sercurityService;
	}

}