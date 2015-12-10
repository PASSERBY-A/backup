package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.resource.ResourceRelation;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 关联关系更新远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteRelationUpdateService extends
		RemoteServiceBasic<IRelationUpdateService> implements
		IRelationUpdateService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/RelationUpdateService";
	}

	public void updateResourceRelation(int id,
			List<ResourceRelation> relationList, int userId) throws Exception {
		getRemoteService().updateResourceRelation(id, relationList, userId);
	}

	public void addModelRelation(ModelRelation relation, int userId)
			throws Exception {
		getRemoteService().addModelRelation(relation, userId);
	}

	public void removeModelRelation(int id, int userId) throws Exception {
		getRemoteService().removeModelRelation(id, userId);
	}

	public void addResourceRelation(ResourceRelation relation, int userId)
			throws Exception {
		getRemoteService().addResourceRelation(relation, userId);
	}
}
