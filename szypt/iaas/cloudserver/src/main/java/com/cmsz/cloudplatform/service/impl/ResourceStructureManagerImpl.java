package com.cmsz.cloudplatform.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.DimResourceDao;
import com.cmsz.cloudplatform.model.DimResource;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.model.vo.ResourceStructureVO;
import com.cmsz.cloudplatform.service.ResourceStructureManager;
import com.cmsz.cloudplatform.utils.StringUtils;

@Service(value = "resourceStructureManager")
public class ResourceStructureManagerImpl implements ResourceStructureManager {

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	@Autowired
	private DimResourceDao dimResourceDao;

	@SuppressWarnings("unchecked")
	@Override
	public ListResponse<ResourceStructureVO> getResource(Map<String, Object[]> params) {
		if (params == null) {
			return null;
		}

		ListResponse<ResourceStructureVO> response = new ListResponse<ResourceStructureVO>();
		List<ResourceStructureVO> resourceStructureVOList = new ArrayList<ResourceStructureVO>();
		
		params.put("command", new Object[] { LIST_VIRTUAL_MACHINES });

		DimResource exampleEntity = new DimResource();
		List<DimResource> dimResourceList = (List<DimResource>) dimResourceDao.findByExample(exampleEntity);

		Response resp = genericCloudServerManager.get(params);

		JSONObject json = JSONObject.fromObject(resp.getResponseString());
		if (json != null) {
			JSONObject jsonObj = (JSONObject) json.get(LIST_VIRTUAL_MACHINES_RESPONSE);

			if (jsonObj != null) {
				List<JSONObject> jsonObjList = (List<JSONObject>) jsonObj.get(VIRTUAL_MACHINE);

				if (CollectionUtils.isNotEmpty(jsonObjList)) {
					for (JSONObject jsobj : jsonObjList) {
						ResourceStructureVO vo = new ResourceStructureVO();
						vo.setVmId(StringUtils.getJsonString(jsobj, ID));
						vo.setVmName(StringUtils.getJsonString(jsobj, NAME));

						vo.setHostId(StringUtils.getJsonString(jsobj, HOST_ID));
						vo.setHost(StringUtils.getJsonString(jsobj, HOST_NAME));

						initResourceStructure(vo, "4", dimResourceList, jsobj);

						initResourceStructure(vo, "3", dimResourceList, jsobj);

						vo.setZoneId(StringUtils.getJsonString(jsobj, ZONE_ID));
						vo.setZone(StringUtils.getJsonString(jsobj, ZONE_NAME));

						initResourceStructure(vo, "1", dimResourceList, jsobj);
						
						resourceStructureVOList.add(vo);
					}
				}
			}
		}

		Collections.sort(resourceStructureVOList);
		response.setResponses(resourceStructureVOList);
		
		return response;
	}

	private void initResourceStructure(ResourceStructureVO vo, String preType, List<DimResource> dimResourceList, JSONObject jsobj) {
		if (dimResourceList == null) {
			return;
		}

		String resourceId = null;
		String type = null;
		if ("4".equals(preType)) {
			type = "5";
			resourceId = StringUtils.getJsonString(jsobj, HOST_ID);
		} else if ("3".equals(preType)) {
			type = "4";
			resourceId = StringUtils.getJsonString(jsobj, CLUSTER_ID);
		} else if ("1".equals(preType)) {
			type = "2";
			resourceId = StringUtils.getJsonString(jsobj, ZONE_ID);
		}

		for (DimResource dimResource : dimResourceList) {
			if (dimResource != null && type.equals(dimResource.getType()) && resourceId != null && resourceId.equals(dimResource.getResourceId())) {

				String preResourceId = dimResource.getPreResourceId();
				for (DimResource preDimResource : dimResourceList) {
					if (preDimResource != null && preType.equals(preDimResource.getType()) && preResourceId != null
							&& preResourceId.equals(preDimResource.getResourceId())) {
						if ("4".equals(preType)) {
							vo.setClusterId(preDimResource.getResourceId());
							jsobj.put(CLUSTER_ID, preDimResource.getResourceId());
							vo.setCluster(preDimResource.getName());
						} else if ("3".equals(preType)) {
							vo.setPodId(preDimResource.getResourceId());
							vo.setPod(preDimResource.getName());
						} else if ("1".equals(preType)) {
							vo.setResourcePoolId(preDimResource.getResourceId());
							vo.setResourcePool(preDimResource.getName());
						}
						return;
					}
				}
			}
		}
	}

	protected static final String LIST_VIRTUAL_MACHINES = "listVirtualMachines";

	protected static final String ZONE_ID = "zoneid";
	protected static final String POD_ID = "podid";
	protected static final String CLUSTER_ID = "clusterid";
	protected static final String HOST_ID = "hostid";

	protected static final String LIST_VIRTUAL_MACHINES_RESPONSE = "listvirtualmachinesresponse";
	protected static final String VIRTUAL_MACHINE = "virtualmachine";

	protected static final String ID = "id";
	protected static final String NAME = "name";
	protected static final String ZONE_NAME = "zonename";
	protected static final String HOST_NAME = "hostname";
}
