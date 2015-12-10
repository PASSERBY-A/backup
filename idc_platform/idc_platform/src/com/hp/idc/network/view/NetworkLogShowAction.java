package com.hp.idc.network.view;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.network.entity.NetworkLogShowEntity;
import com.hp.idc.network.service.NetworkService;

/**
 * 网络历史状态查询接口
 * 
 * @author Wang Rui
 *
 */
public class NetworkLogShowAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8252756547802711728L;
	
	@Resource
	private NetworkService networkService;
	
	/**
	 * 监控器名称
	 */
	private String monitorname;
	
	public NetworkService getNetworkService() {
		return networkService;
	}


	public List<NetworkLogShowEntity> getNetworkLogShowEntityList() {
		return networkLogShowEntityList;
	}

	public void setNetworkService(NetworkService networkService) {
		this.networkService = networkService;
	}

	public void setNetworkLogShowEntityList(
			List<NetworkLogShowEntity> networkLogShowEntityList) {
		this.networkLogShowEntityList = networkLogShowEntityList;
	}

	private List<NetworkLogShowEntity> networkLogShowEntityList; 
	
	/**
	 * 网络历史查询初始化页面
	 * @return SUCCESS
	 */
	public String IntNetworkLogShow(){
		return SUCCESS;
	}
	/**
	 * 网络历史状态翻页查询		
	 * @return SUCCESS
	 */
	public String search(){
		Map<String, Object> paramMap=new HashMap<String, Object>();
		LinkedHashMap<String, String> sortMap=new LinkedHashMap<String, String>();
        if(monitorname != null && !"".equals(monitorname)){
        	paramMap.put("monitorname", monitorname);
		}
		Page<NetworkLogShowEntity> page = networkService.queryLogResult(
				paramMap, sortMap, start / limit + 1, limit);
		networkLogShowEntityList = page.getResult();
		jsonObject = new JSONObject();
		jsonArray = new JSONArray();
		for (NetworkLogShowEntity networkLogShowEntity : networkLogShowEntityList) {
			JSONObject json = JSONObject.fromObject(networkLogShowEntity);
			jsonArray.add(json);
		}
		jsonObject.put(NetworkLogShowAction.JSON_RESULT, jsonArray);
		jsonObject.put(NetworkLogShowAction.JSON_TOTAL_COUNT, page.getTotalSize());
		return SUCCESS;		
	}


	public String getMonitorname() {
		return monitorname;
	}


	public void setMonitorname(String monitorname) {
		this.monitorname = monitorname;
	}
	

}
