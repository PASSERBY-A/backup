package com.cmsz.cloudplatform.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.ServiceConstants;
import com.cmsz.cloudplatform.model.ActiveUser;
import com.cmsz.cloudplatform.model.DimResource;
import com.cmsz.cloudplatform.model.ResourcePool;
import com.cmsz.cloudplatform.service.DimResourceManager;
import com.cmsz.cloudplatform.service.ResourcePoolManager;
import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.cmsz.cloudplatform.model.request.DimResourceTreeRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;

public class ResourcePoolAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5364087239920275888L;
	
	@Autowired
	private ResourcePoolManager resourcePoolManager;
	
	@Autowired
	private DimResourceManager dimResourceManager;
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	
	
	public void setResourcePoolManager(ResourcePoolManager resourcePoolManager) {
		this.resourcePoolManager = resourcePoolManager;
	}

	public String listSubResource(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String response = requestParams.get("response")==null ? null : (String) requestParams.get("response")[0];
		Map<String,Object[]> param = new HashMap();
		param.putAll(this.requestParams);
		param.put("command", new Object[]{"listZones"});
		String resultStr = resourcePoolManager.listSubResource(param, resourcePoolId);
		Response responseObj = new Response();
		responseObj.setResponseString(resultStr);
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(response);
		writeResponse(responseObj);
		return NONE;
	}
	
	/**
	 * 查询可用的ZONE
	 * @return
	 */
	public String listAvailableResource(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String response = requestParams.get("response")==null ? null : (String) requestParams.get("response")[0];
		Map<String,Object[]> param = new HashMap();
		param.putAll(this.requestParams);
		param.put("command", new Object[]{"listZones"});
		String resultStr = resourcePoolManager.listAvailableResource(param, resourcePoolId);
		Response responseObj = new Response();
		responseObj.setResponseString(resultStr);
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(response);
		writeResponse(responseObj);
		return NONE;
	}

	public String list(){
		List<ResourcePool> pools = resourcePoolManager.getAll();
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		/*Map m = new HashMap();
		m.put("root", pools)*/
		Response response = this.transformResponse(pools, responseType, HttpStatus.SC_OK);
		writeResponse(response);
		return NONE;
	}
	
	public String configZone(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String[] zoneIds = this.request.getParameterValues("zoneId");//requestParams.get("zoneId")==null ? null : (String[]) requestParams.get("zoneId");
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		ActiveUser user = (ActiveUser) session.get(ServiceConstants.ACTIVE_USER);
		resourcePoolManager.configZone(resourcePoolId, user.getLoginId(), zoneIds);
		Response response = this.transformResponse("success", responseType, HttpStatus.SC_OK);
		this.writeResponse(response);
		return NONE;
	}
	
	
	public String computeResource(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		
		Map<String,Object[]> param = new HashMap();
		param.putAll(this.requestParams);
		//param.put("command", new Object[]{"listZones"});
		
		String resultStr = resourcePoolManager.computeResource(param, resourcePoolId);
		
		Response responseObj = new Response();
		responseObj.setResponseString(resultStr);
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(responseType);
		writeResponse(responseObj);
		return NONE;
	}
	
	public String removeRelation(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String zoneId = requestParams.get("zoneId")==null ? null : (String) requestParams.get("zoneId")[0];
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		resourcePoolManager.removeRelation(resourcePoolId, zoneId);
		Response response = this.transformResponse("success", responseType, HttpStatus.SC_OK);
		this.writeResponse(response);
		return NONE;
	}
	
	public String getRelResourcePoolByZoneId(){
		String zoneId = requestParams.get("zoneId")==null ? null : (String) requestParams.get("zoneId")[0];
		Response response = this.transformResponse(resourcePoolManager.getRelResourcePoolByZoneId(zoneId), "json", HttpStatus.SC_OK);
		this.writeResponse(response);
		return NONE;
	}
	public String firstPage(){
		int hostCount =resourcePoolManager.getHostCount("5");
		request.setAttribute("hostCount", hostCount);
		//TODO..
		int vmCount =100;
		request.setAttribute("vmCount", vmCount);
		return "success";
	}
	
	
	
	// 0级,池资源统计信息
	public void rootClusterResource() {
		System.out.println("xx");
	}
	
	/**
	 * 主界面,资源池列表json格式数据
	 */
	public void listPoolNodes() { 
		DimResourceTreeRequest request = (DimResourceTreeRequest) this.wrapRequest(new DimResourceTreeRequest());
		ListResponse<DimResource> response = dimResourceManager.getDimResourceTree(request);
		 List<DimResource> poolNodes = new ArrayList<DimResource>();
		if(response != null){
			 List<DimResource> listDimResource = response.getResponses();
			 if(listDimResource != null && listDimResource.size() > 0){
				 for(DimResource dimResource:listDimResource){
					 if("1".equalsIgnoreCase(dimResource.getType())){
						 poolNodes.add(dimResource);
					 }
				 }
			 }
		}
		JSONObject json = new JSONObject();
		json.put("poolNodes", JSONArray.fromObject(poolNodes));
		this.renderText(json.toString());
	}
	
	
	public String firstResource() { 
		String zoneId = requestParams.get("zoneId")==null ? null : (String) requestParams.get("zoneId")[0];
		String title = requestParams.get("title")==null ? null : (String) requestParams.get("title")[0];
		requestParams.put("response",new String[]{"json"});
		requestParams.put("type",new String[]{"routing"});
		request.setAttribute("zoneId", zoneId);
		request.setAttribute("title", title);
		Map map = new HashMap();
		map.putAll(requestParams);
		map.put("command", new String[]{"listHosts"});
		Response listHostsResponse = genericCloudServerManager.get(map);
		
		if(listHostsResponse!=null && StringUtils.isNotBlank(listHostsResponse.getResponseString())){
			JSONObject listHostsJson = JSONObject.fromObject(listHostsResponse.getResponseString());
			if(listHostsJson.getJSONObject("listhostsresponse").containsKey("count")){
				JSONArray hostsArray = listHostsJson.getJSONObject("listhostsresponse").getInt("count")>0?listHostsJson.getJSONObject("listhostsresponse").getJSONArray("host"): new JSONArray();
				request.setAttribute("hostList", hostsArray);
//				JSONObject jsonObj = new JSONObject();
//				jsonObj.put("hostList",hostsArray);
//				this.renderText(hostsArray.toString());
			}
		}
		
		return SUCCESS;
	}
		
		
	public String secondResource(){
		//http://localhost:8080/client/api.action?command=listVirtualMachines&
		//response=json&sessionkey=rFkFVZxHpcGfuxcx7b5UFiJM%2FOQ%3D&listAll=true&page=1&pagesize=20&hostid=72c19138-8e1f-429a-8455-f898b5f8c80e&_=1397462801580
		String hostid=requestParams.get("hostid")==null ? null : (String) requestParams.get("hostid")[0];
		String title = requestParams.get("title")==null ? null : (String) requestParams.get("title")[0];
		String otitle = requestParams.get("otitle")==null ? null : (String) requestParams.get("otitle")[0];
		requestParams.put("response",new String[]{"json"});
		requestParams.put("listAll",new String[]{"true"});
		request.setAttribute("hostid", hostid);
		request.setAttribute("title", title);
		request.setAttribute("otitle", otitle);
		
		Map map = new HashMap();
		map.putAll(requestParams);
		map.put("command", new String[]{"listVirtualMachines"});
		Response listvirtualmachinesresponse = genericCloudServerManager.get(map);
		
		if(listvirtualmachinesresponse!=null && StringUtils.isNotBlank(listvirtualmachinesresponse.getResponseString())){
			JSONObject listHostsJson = JSONObject.fromObject(listvirtualmachinesresponse.getResponseString());
			if(listHostsJson.getJSONObject("listvirtualmachinesresponse").containsKey("count")){
				JSONArray vmArray = listHostsJson.getJSONObject("listvirtualmachinesresponse").getInt("count")>0?listHostsJson.getJSONObject("listvirtualmachinesresponse").getJSONArray("virtualmachine"): new JSONArray();
				request.setAttribute("list", vmArray);
			}
		}
		return SUCCESS;
	}

}
