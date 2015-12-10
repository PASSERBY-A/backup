package com.hp.idc.common.core.view;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.idc.common.core.entity.SysBaseType;
import com.hp.idc.common.core.service.SysBaseTypeService;

public class CodeManageAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SysBaseTypeService sysBaseTypeService;
	private long codeType;
	
	public String getCodeListByType(){
		List<SysBaseType> list=sysBaseTypeService.getSysBaseTypeByCodeType(codeType);
		jsonObject=new JSONObject();
		jsonArray =new JSONArray();
		for(SysBaseType st:list){
			JSONObject json=JSONObject.fromObject(st);
			jsonArray.add(json);
		}
		jsonObject.put(JSON_RESULT, jsonArray);
		jsonObject.put(JSON_TOTAL_COUNT, list.size());
		return SUCCESS;
	}

	public SysBaseTypeService getSysBaseTypeService() {
		return sysBaseTypeService;
	}

	public void setSysBaseTypeService(SysBaseTypeService sysBaseTypeService) {
		this.sysBaseTypeService = sysBaseTypeService;
	}

	public long getCodeType() {
		return codeType;
	}

	public void setCodeType(long codeType) {
		this.codeType = codeType;
	}

}
