package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 资源模型更新远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteModelUpdateService extends
		RemoteServiceBasic<IModelUpdateService> implements IModelUpdateService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/ModelUpdateService";
	}

	public void updateModel(Model model, List<ModelAttribute> attributes,
			int userId) throws Exception {
		getRemoteService().updateModel(model, attributes, userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.resm.service.IModelUpdateService#removeModel(java.lang
	 * .String, int)
	 */
	public void removeModel(String modelId, int userId) throws Exception {
		getRemoteService().removeModel(modelId, userId);
	}

}
