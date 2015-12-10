package com.cmsz.cloudplatform.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.ResourceDao;
import com.cmsz.cloudplatform.dao.ResourcePoolDao;
import com.cmsz.cloudplatform.dao.THostDao;
import com.cmsz.cloudplatform.dto.VmSummary;
import com.cmsz.cloudplatform.model.Resource;
import com.cmsz.cloudplatform.model.ResourcePool;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.ResourcePoolManager;
import com.cmsz.cloudplatform.spi.VirtualMachineService;
import com.hp.core.service.impl.GenericManagerImpl;

@Service(value = "resourcePoolManager")
public class ResourcePoolManagerImpl extends
		GenericManagerImpl<ResourcePool, Integer> implements
		ResourcePoolManager {

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	private ResourcePoolDao resourcePoolDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private THostDao tHostDao;
	@Autowired
	private VirtualMachineService hpVirtualMachineService;

	@Autowired
	public void setResourcePoolDao(ResourcePoolDao resourcePoolDao) {
		this.resourcePoolDao = resourcePoolDao;
		this.dao = resourcePoolDao;
	}

	public ResourcePoolManagerImpl(ResourcePoolDao resourcePoolDao) {
		super(resourcePoolDao);
		this.resourcePoolDao = resourcePoolDao;
	}

	public ResourcePoolManagerImpl() {
		super();
	}

	@Override
	public void configZone(String resourcePoolId, String username,
			String... zoneId) {
		for (String zid : zoneId) {
			this.resourcePoolDao.configZone(resourcePoolId, zid, username);
			log.info("配置一级资源池 和 Zone 关联： 一级资源池ID=" + resourcePoolId
					+ " , Zone ID =" + zid + "  ,操作成功!");
		}
	}

	@Override
	public String listSubResource(Map<String, Object[]> cloudStackParam,
			String resourcePoolId) {
		Map<String, List> comuteResult = compute(cloudStackParam,
				resourcePoolId);
		Map map = new HashMap();
		map.put("zone", comuteResult.get("subResource"));
		map.put("count", comuteResult.get("subResource").size());
		JSONObject jo = new JSONObject();
		jo.put("listzonesresponse", JSONObject.fromObject(map).toString());
		System.out.println(jo.toString());
		return jo.toString();
	}

	public static void main(String[] args) {
		String abc = "{ 'listzonesresponse' : { 'count':2 ,'zone' : [  {'id':'bcdf98b8-33ba-48a9-b785-6be289c5ff10'},{'id':'bcdf98b8-33ba-48a9-b785-6be289c5ff10'}]} }";
		JSONObject JSONObj = JSONObject.fromObject(abc);
		System.out.println(JSONObj.containsKey("listzonesresponse"));
		if (JSONObj.containsKey("listzonesresponse")) {
			JSONObject JSONObj1 = JSONObject.fromObject(JSONObj.get(
					"listzonesresponse").toString());
			if (JSONObj1.containsKey("zone")) {
				JSONArray zoneArray = JSONArray.fromObject(JSONObj1.get("zone")
						.toString());
				
			
			 for(int i=0;i<zoneArray.size();++i){
				 JSONObject js = zoneArray.getJSONObject(i);
				// js.optString("id", "abc");
				 js.element("id", "cc"+i);
				 System.out.println(js.optString("id"));
				 
			}
				/*zoneArray.clear();
				zoneArray.add("abc");
				JSONObject j = new JSONObject();

				JSONObj = JSONObject.fromObject(JSONObj
						.get("listzonesresponse").toString());
				JSONObj.put("zone", zoneArray);
				System.out.println(JSONObj);*/
				
			}
		}

	}

	@Override
	public String listAvailableResource(Map<String, Object[]> cloudStackParam,
			String resourcePoolId) {
		Map<String, List> comuteResult = compute(cloudStackParam,
				resourcePoolId);
		Map map = new HashMap();
		map.put("zone", comuteResult.get("availableResource"));
		map.put("count", comuteResult.get("availableResource").size());
		JSONObject jo = new JSONObject();
		jo.put("listzonesresponse", JSONObject.fromObject(map).toString());
		System.out.println(jo.toString());
		return jo.toString();
	}

	private Map<String, List> compute(Map<String, Object[]> cloudStackParam,
			String resourcePoolId) {
		Response response = genericCloudServerManager
				.get(cloudStackParam);
		// 己经配置的ZONE ID
		String[] zoneIds = resourcePoolDao.listSubResource(resourcePoolId);
		String zoneId = "";
		List<Object> subResource = new ArrayList<Object>();
		List<Object> availableResource = new ArrayList<Object>();
		Map<String, List> map = new HashMap<String, List>();
		map.put("subResource", subResource);
		map.put("availableResource", availableResource);
		JSONObject JSONObj = JSONObject
				.fromObject(response.getResponseString());
		if (JSONObj.containsKey("listzonesresponse")) {
			JSONObj = JSONObject.fromObject(JSONObj.get("listzonesresponse")
					.toString());
			if (JSONObj.containsKey("zone")) {
				JSONArray zoneArray = JSONArray.fromObject(JSONObj.get("zone")
						.toString());
				for (int i = 0; i < zoneArray.size(); ++i) {
					zoneId = JSONObj.fromObject(zoneArray.get(i).toString())
							.get("id").toString();
					boolean remove = false;
					for (String zone : zoneIds) {
						if (zoneId.equals(zone)) {// 相同即关联配置过ZONE
							remove = true;
							break;
						}
					}
					if (remove) {
						subResource.add(zoneArray.get(i));
						// availableResource.add(zoneArray.get(i));
					} else {
						// 查询 zoneId 是否被关联一级资源池
						String rid = resourcePoolDao.getResourcePool(zoneId);
						if (StringUtils.isBlank(rid)) {
							availableResource.add(zoneArray.get(i));
						}
					}
				}
			}
		}
		return map;

	}

	@Override
	public String computeResource(Map<String, Object[]> cloudStackParam, String resourcePoolId) {
		if (cloudStackParam != null
				&& cloudStackParam.get("type") != null
				&& cloudStackParam.get("type").length > 0
				&& (WorkOrder.KEY_X86_PHYSICAL_RESOURCES_APPLICATION.equals(cloudStackParam.get("type")[0]) || WorkOrder.KEY_HP_MINICOMPUTER_RESOURCES_APPLICATION
						.equals(cloudStackParam.get("type")[0]))) {
			// X86物理资源和HPVM资源统计
			return computeResource(cloudStackParam.get("type")[0].toString(), resourcePoolId);
		}
		
		Map<String, Object[]> newMap = new HashMap<String, Object[]>();
		newMap.putAll(cloudStackParam);
		// 查询ZONES
		newMap.put("command", new Object[] { "listZones" });
		Map<String, List> resourceMap = compute(newMap, resourcePoolId);
		Map<String,JSONObject> finalMap = new HashMap<String,JSONObject>();
		List subResource = resourceMap.get("subResource");
		for (int i = 0; i < subResource.size(); ++i) {
			JSONObject j = (JSONObject) subResource.get(i);
			String zoneId = j.getString("id");
			cloudStackParam.put("command", new Object[] { "listCapacity" });
			cloudStackParam.put("zoneid", new Object[] { zoneId });
			Response response = genericCloudServerManager.get(cloudStackParam);
			if (response != null
					&& StringUtils.isNotBlank(response.getResponseString())) {
				JSONObject jsonObj = JSONObject.fromObject(response
						.getResponseString());
				if (jsonObj.containsKey("listcapacityresponse")) {
					jsonObj = JSONObject.fromObject(jsonObj.get(
							"listcapacityresponse").toString());
					if (jsonObj.containsKey("capacity")) {
						JSONArray array = jsonObj.getJSONArray("capacity");
						for(int k=0;k<array.size();++k){
							JSONObject job = array.getJSONObject(k);
							if(finalMap.containsKey(job.getString("type"))){
								JSONObject tempJson = finalMap.get(job.getString("type"));
								//System.out.println("己经有的 zoneid:"+tempJson.getString("zoneid")+",type:"+tempJson.getInt("type")+",capacityused:"+tempJson.getLong("capacityused")+",capacitytotal:"+tempJson.getLong("capacitytotal")+",percentused:"+tempJson.getString("percentused"));
								/*job.optLong("capacityused",tempJson.getLong("capacityused")+job.optLong("capacityused"));
								job.optLong("capacitytotal",tempJson.getLong("capacitytotal")+job.optLong("capacitytotal"));
								job.optString("percentused",""+Math.round((job.optLong("capacityused")*1.0D*10000/job.optLong("capacitytotal")))*0.01);*/
								job.element("capacityused",tempJson.getLong("capacityused")+job.optLong("capacityused"));
								job.element("capacitytotal",tempJson.getLong("capacitytotal")+job.optLong("capacitytotal"));
								job.element("percentused",""+Math.round((job.optLong("capacityused")*1.0D*10000/job.optLong("capacitytotal")))*0.01);
								log.info("合并之后 zoneid:"+job.getString("zoneid")+",type:"+job.getInt("type")+",capacityused:"+job.getDouble("capacityused")+",capacitytotal:"+job.getDouble("capacitytotal")+",percentused:"+job.getString("percentused"));
								finalMap.put(job.getString("type"), job);
							}else{
								finalMap.put(job.getString("type"), job);
							}
						}
					}
				}
			}
		}
		Map jsonMap = new HashMap();
		jsonMap.put("count",finalMap.size());
		jsonMap.put("capacity", finalMap.values());
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("listcapacityresponse", JSONObject.fromObject(jsonMap)
				.toString());
		return jsonStr.toString();
	}

	// X86物理资源和HPVM资源统计
	private String computeResource(String type, String resourcePoolId) {
		// {"listcapacityresponse":{"count":8,
		// "capacity":[{"type":3,"zoneid":"bcdf98b8-33ba-48a9-b785-6be289c5ff10","zonename":"vm-zone",
		// "capacityused":1365749268480,"capacitytotal":3220688601088,"percentused":"42.41"},
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		
		int totalUnit = 0;
		int totalCpu = 0;
		int totalMemory = 0;
		int totalHost = 0; // 总的主机数
		int hostType = WorkOrder.KEY_X86_PHYSICAL_RESOURCES_APPLICATION.equals(type) ? THost.TYPE_X86_RACK : THost.TYPE_HP;
		List<Object> hostTR = resourceDao.hostTotalResource(hostType, resourcePoolId);
		if (CollectionUtils.isNotEmpty(hostTR)) {
			Object[] hosts = (Object[]) hostTR.get(0);
			if (ArrayUtils.isNotEmpty(hosts) && hosts.length >= 3) {
				if(WorkOrder.KEY_X86_PHYSICAL_RESOURCES_APPLICATION.equals(type)) {
					// X86总台数
					totalUnit = ((BigDecimal) hosts[0]).intValue();
				}
				totalHost = ((BigDecimal) hosts[0]).intValue();
				totalCpu = ((BigDecimal) hosts[1]).intValue();
				totalMemory = ((BigDecimal) hosts[2]).intValue();
			}
		}
		
		int usedUnit = 0;
		int usedCpu = 0;
		int usedMemory = 0;
		int resourceType = WorkOrder.KEY_X86_PHYSICAL_RESOURCES_APPLICATION.equals(type) ? Resource.RESOURCE_TYPE_X86 : Resource.RESOURCE_TYPE_HPVM;
		List<Object> appTR = resourceDao.applicationTotalResource(resourceType, resourcePoolId);
		if (CollectionUtils.isNotEmpty(appTR)) {
			Object[] apps = (Object[]) appTR.get(0);
			if (ArrayUtils.isNotEmpty(apps) && apps.length >= 3) {
				usedUnit = ((BigDecimal) apps[0]).intValue();
				usedCpu = ((BigDecimal) apps[1]).intValue();
				usedMemory = ((BigDecimal) apps[2]).intValue();
			}
		}
		
		// hpvm 总数量
		if(WorkOrder.KEY_HP_MINICOMPUTER_RESOURCES_APPLICATION.equals(type)) {
			totalUnit = getHpvmCount(resourcePoolId);
//			if (CollectionUtils.isNotEmpty(hpvmTR)) {
//				Object[] hpvms = (Object[]) hpvmTR.get(0);
//				if (ArrayUtils.isNotEmpty(hpvms) && hpvms.length >= 3) {
//					totalUnit = ((BigDecimal) hpvms[0]).intValue();
//				}
//			}
		}

		JSONObject jsonUnit = new JSONObject();
		if(WorkOrder.KEY_HP_MINICOMPUTER_RESOURCES_APPLICATION.equals(type)) {
			jsonUnit.put("totalHost", totalHost);
		}
		jsonUnit.put("resourcePool", "unit");
		jsonUnit.put("total", totalUnit);
		jsonUnit.put("used", usedUnit);
		jsonUnit.put("unused", totalUnit != 0 || usedUnit == 0 ? totalUnit - usedUnit : "未知");
		jsonUnit.put("percentused", totalUnit != 0 ? new BigDecimal(100 * usedUnit).divide(new BigDecimal(totalUnit), 2, RoundingMode.HALF_UP) : usedUnit == 0 ? 0 : "未知");
		jsonList.add(jsonUnit);

		JSONObject jsonCpu = new JSONObject();
		jsonCpu.put("resourcePool", "cpu");
		jsonCpu.put("total", totalCpu);
		jsonCpu.put("used", usedCpu);
		jsonCpu.put("unused", totalCpu != 0 || usedCpu == 0 ? totalCpu - usedCpu : "未知");
		jsonCpu.put("percentused", totalCpu != 0 ? new BigDecimal(100 * usedCpu).divide(new BigDecimal(totalCpu), 2, RoundingMode.HALF_UP) :  usedCpu == 0 ? 0 : "未知");
		jsonList.add(jsonCpu);

		JSONObject jsonMemory = new JSONObject();
		jsonMemory.put("resourcePool", "memory");
		jsonMemory.put("total", totalMemory);
		jsonMemory.put("used", usedMemory);
		jsonMemory.put("unused", totalMemory != 0 || usedMemory == 0 ? totalMemory - usedMemory : "未知");
		jsonMemory.put("percentused",
				totalMemory != 0 ? new BigDecimal(100 * usedMemory).divide(new BigDecimal(totalMemory), 2, RoundingMode.HALF_UP) :  usedMemory == 0 ? 0 : "未知");
		jsonList.add(jsonMemory);

		JSONArray jsonArr = JSONArray.fromObject(jsonList);

		return jsonArr.toString();
	}
	
	@SuppressWarnings("unchecked")
	private int getHpvmCount(String resourcePoolId) {
		int count = 0;
		THost exampleEntity = new THost();
		exampleEntity.setResourcepoolid(resourcePoolId);
		exampleEntity.setType(THost.TYPE_HP);
		List<THost> tHostList = (List<THost>) tHostDao.findByExample(exampleEntity);
		if (CollectionUtils.isNotEmpty(tHostList)) {
			for (THost tHost : tHostList) {
				List<VmSummary> hpvmlist = hpVirtualMachineService.getVMList(tHost.getHostname());
				if (CollectionUtils.isNotEmpty(hpvmlist)) {
					count += hpvmlist.size();
				}
			}
		}
		return count;
	}

	public void removeRelation(String resourcePoolId,String zoneId){
		if(StringUtils.isNotBlank(resourcePoolId) && StringUtils.isNotBlank(zoneId)){
			resourcePoolDao.removeZone(resourcePoolId, zoneId);
		}else{
			log.error("解除一级资源池和ZONE参数为空");
		}
	}
	
	
	public String getRelResourcePoolByZoneId(String zoneId){
		// 查询 zoneId 是否被关联一级资源池
		String rid = resourcePoolDao.getResourcePool(zoneId);
		return rid;
	}

	public int getHostCount(String type) {
		return resourcePoolDao.getHostCount(type);
	}
}
