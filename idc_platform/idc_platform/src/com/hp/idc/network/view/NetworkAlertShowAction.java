package com.hp.idc.network.view;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.network.entity.NetworkAlertShowEntity;
import com.hp.idc.network.service.NetworkService;

/**
 * 网络告警状态查询接口
 * 
 * @author Wang Rui
 *
 */
public class NetworkAlertShowAction extends AbstractAction {

	private static final long serialVersionUID = -8252756547802711728L;

	@Resource
	private NetworkService networkService;

	/**
	 * 监控器名
	 */
	private String monitor_name;
	
	/**
	 * 告警级别
	 */
	private String severity;
	
	/**
	 * 告警状态:Close
	 */
	private String eventStatus;
	
	/**
	 * 告警确认者
	 */
	private String eventConfirmer;
	
	/**
	 * 告警时间信息
	 */
	private Date timed;
	
	/**
	 * 监控组名称
	 */
	private String group_name;
	

	private List<NetworkAlertShowEntity> networkAlertShowEntityList;

	public NetworkService getNetworkService() {
		return networkService;
	}

	public void setNetworkService(NetworkService networkService) {
		this.networkService = networkService;
	}

	public String IntNetworkAlertShow() {
		return SUCCESS;
	}
    
	/**
	 * 查询告警信息
	 * 
	 * @return SUCCESS
	 */
	public String search() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		if (monitor_name != null && !"".equals(monitor_name)) {
			paramMap.put("monitorName", monitor_name);
		}
		if (severity != null && !"".equals(severity)) {
			paramMap.put("severity", severity);
		}
		if (eventStatus != null && !"".equals(eventStatus)) {
			paramMap.put("eventStatus", eventStatus);
		}
		if (eventConfirmer != null && !"".equals(eventConfirmer)) {
			paramMap.put("eventConfirmer", eventConfirmer);
		}
		Page<NetworkAlertShowEntity> page = networkService.queryAlertResult(
				paramMap, sortMap, start / limit + 1, limit);
		networkAlertShowEntityList = page.getResult();
		jsonObject = new JSONObject();
		jsonArray = new JSONArray();
		JsonConfig jc = new JsonConfig();
		jc.setExcludes(new String[] { "timed" });
		for (NetworkAlertShowEntity networkAlertShowEntity : networkAlertShowEntityList) {
			JSONObject json = JSONObject.fromObject(networkAlertShowEntity, jc);
			json.put("timed", networkAlertShowEntity.getTimed().toString());
			jsonArray.add(json);
		}
		jsonObject.put(NetworkLogShowAction.JSON_RESULT, jsonArray);
		jsonObject.put(NetworkLogShowAction.JSON_TOTAL_COUNT,
				page.getTotalSize());
		return SUCCESS;
	}
	
	/**
	 * 更新告警事件状态
	 * 
	 * @return SUCCESS
	 */
	public String update() {
		NetworkAlertShowEntity networkAlertShowEntity = new NetworkAlertShowEntity();
		networkAlertShowEntity.setTimed(timed);
		networkAlertShowEntity.setMonitorName(monitor_name);
		networkAlertShowEntity.setGroupName(group_name);
		networkAlertShowEntity.setEventConfirmer(eventConfirmer);
		networkAlertShowEntity.setEventStatus(eventStatus);
		if (networkAlertShowEntity != null
				&& networkAlertShowEntity.getMonitorName() != null) {
			try{
			 networkService.saveNetworkAlertShowEntity(networkAlertShowEntity);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		jsonObject = new JSONObject();
		jsonArray = new JSONArray();
		return SUCCESS;
	}

	public String getMonitor_name() {
		return monitor_name;
	}

	public List<NetworkAlertShowEntity> getNetworkAlertShowEntityList() {
		return networkAlertShowEntityList;
	}

	public void setMonitor_name(String monitor_name) {
		this.monitor_name = monitor_name;
	}

	public void setNetworkAlertShowEntityList(
			List<NetworkAlertShowEntity> networkAlertShowEntityList) {
		this.networkAlertShowEntityList = networkAlertShowEntityList;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventConfirmer() {
		return eventConfirmer;
	}

	public void setEventConfirmer(String eventConfirmer) {
		this.eventConfirmer = eventConfirmer;
	}


	public Date getTimed() {
		return timed;
	}

	public void setTimed(Date timed) {
		this.timed = timed;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
