package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 资源模型远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteModelService extends RemoteServiceBasic<IModelService>
		implements IModelService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/ModelService";
	}

	public List<Model> getAllModels(int userId) {
		return getRemoteService().getAllModels(userId);
	}

	public Model getModelById(String id) {
		return getRemoteService().getModelById(id);
	}

	public Model getParentModelById(String id) {
		return getRemoteService().getParentModelById(id);
	}

	public List<ModelAttribute> getModelAttributesByModelId(String id) {
		return getRemoteService().getModelAttributesByModelId(id);
	}

	public List<Model> getChildModelsById(String id, int userId) {
		return getRemoteService().getChildModelsById(id, userId);
	}

	public List<Model> getChildModelsById(String id, boolean recursive, int userId) {
		return getRemoteService().getChildModelsById(id, recursive, userId);
	}

	public ModelAttribute getModelAttribute(String modeId, String attrId) {
		return getRemoteService().getModelAttribute(modeId, attrId);
	}
	
	public List<Model> filterByUser(List<Model> list, int userId, int type) {
		return getRemoteService().filterByUser(list, userId, type);
	}

	/**
	 * @param args
	 *            args
	 */
	public static void main(String[] args) {
		ResmConfig config = new ResmConfig();
		config.setServer("127.0.0.1:10096");
		RemoteModelService r = new RemoteModelService();
		List<Model> m = r.getAllModels(1);
		for (Model mm : m)
			mm.dump();

	}

}
