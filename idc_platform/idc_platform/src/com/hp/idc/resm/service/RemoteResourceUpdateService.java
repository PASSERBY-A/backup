package com.hp.idc.resm.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 资源对象更新远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteResourceUpdateService extends
		RemoteServiceBasic<IResourceUpdateService> implements
		IResourceUpdateService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/ResourceUpdateService";
	}

	public void updateResource(ResourceObject ro, int userId) throws Exception {
		getRemoteService().updateResource(ro, userId);
	}

	public void syncResource(int id) {
		getRemoteService().syncResource(id);
	}

	public void deleteResource(int id, int userId) throws Exception {
		getRemoteService().deleteResource(id, userId);
	}

	public void updateResource(int id, List<AttributeBase> list, int userId)
			throws Exception {
		getRemoteService().updateResource(id, list, userId);
	}

	public int addResource(String modelId, List<AttributeBase> list, int userId)
			throws Exception {
		return getRemoteService().addResource(modelId, list, userId);
	}

	public int addResource(String modelId, Map<String, String> attributes,
			int userId) throws Exception {
		return getRemoteService().addResource(modelId, attributes, userId);
	}

	@Override
	public void updateResource(int id, int userId, Map<String, String> attributes)
			throws Exception {
		getRemoteService().updateResource(id, userId, attributes);
	}

}
