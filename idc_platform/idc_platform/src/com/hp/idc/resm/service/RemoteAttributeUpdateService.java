package com.hp.idc.resm.service;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 资源属性更新远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteAttributeUpdateService extends
		RemoteServiceBasic<IAttributeUpdateService> implements
		IAttributeUpdateService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server
				+ "/hessian/AttributeUpdateService";
	}

	public void updateAttribute(AttributeDefine attr, int userId)
			throws Exception {
		getRemoteService().updateAttribute(attr, userId);
	}

	public void removeAttribute(String id, int userId) throws Exception {
		getRemoteService().removeAttribute(id, userId);
	}
}
