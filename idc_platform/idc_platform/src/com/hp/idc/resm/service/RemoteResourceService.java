package com.hp.idc.resm.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.ui.PageInfo;
import com.hp.idc.resm.ui.PageQueryInfo;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 资源对象远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteResourceService extends RemoteServiceBasic<IResourceService>
		implements IResourceService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/ResourceService";
	}

	public ResourceObject getResourceById(int id) {
		return getRemoteService().getResourceById(id);
	}

	public List<ResourceObject> getResourcesByModelId(String id, int userId) {
		return getRemoteService().getResourcesByModelId(id, userId);
	}

	public List<ResourceObject> getResourcesByModelId(String id,
			boolean includeChilds, int userId) {
		return getRemoteService().getResourcesByModelId(id, includeChilds, userId);
	}

	public List<ResourceObject> findResource(String expr, int userId) throws Exception {
		return getRemoteService().findResource(expr, userId);
	}

	public List<ResourceObject> getAllResources(int userId) {
		return getRemoteService().getAllResources(userId);
	}

	public ResourceObject getResourceByAttribute(String attrId, String value)
			throws Exception {
		return getRemoteService().getResourceByAttribute(attrId, value);
	}

	public List<AttributeBase> getResourceAttributes(int id) {
		return getRemoteService().getResourceAttributes(id);
	}
	
	public PageInfo<ResourceObject> listResource(String modelId, String str,
			PageQueryInfo queryInfo, int userId) {
		return getRemoteService().listResource(modelId, str, queryInfo, userId);
	}
	
	public List<ResourceObject> getResourcesById(String id) {
		return getRemoteService().getResourcesById(id);
	}

	public List<ResourceObject> filterByUser(List<ResourceObject> list,
			int userId) {
		return getRemoteService().filterByUser(list, userId);
	}

	public int[] filterByUser(int[] idArray, int userId) {
		return getRemoteService().filterByUser(idArray, userId);
	}

	public boolean hasAccessPermission(int id, int userId) {
		return getRemoteService().hasAccessPermission(id, userId);
	}

	/**
	 * @param args
	 *            args
	 */
	public static void main(String[] args)  {
		ResmConfig config = new ResmConfig();
		//config.setServer("192.168.4.114:10096");
		config.setServer("127.0.0.1:10096");
		RemoteResourceService r = new RemoteResourceService();
		ResourceObject obj = r.getResourceById(1111);
		obj.dump();
	}

	@Override
	public PageInfo<ResourceObject> listResource(String modelId,
			boolean recursion, String str, PageQueryInfo queryInfo, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResourceObject> findResource(List<ResourceObject> ll,
			String expr, int userId) throws Exception {
		return getRemoteService().findResource(ll, expr, userId);
	}

	@Override
	public PageInfo<ResourceObject> listResource(String modelId,
			boolean recursion, Map<String, Object> paramMap,
			PageQueryInfo queryInfo, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResourceObject> getResourceByAttribute(String modelId,String exp, int userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
