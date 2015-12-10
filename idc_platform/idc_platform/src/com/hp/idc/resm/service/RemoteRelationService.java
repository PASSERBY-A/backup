package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.model.RelationDefine;
import com.hp.idc.resm.resource.ResourceRelation;
import com.hp.idc.resm.util.ResmConfig;

/**
 * 关联关系远程服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RemoteRelationService extends RemoteServiceBasic<IRelationService>
		implements IRelationService {

	@Override
	protected String getRemoteServiceURL() {
		return "http://" + ResmConfig.server + "/hessian/RelationService";
	}

	public RelationDefine getRelationDefineById(String id) {
		return getRemoteService().getRelationDefineById(id);
	}

	public List<RelationDefine> getAllRelationDefines() {
		return getRemoteService().getAllRelationDefines();
	}

	public ModelRelation getModelRelationById(int id) {
		return getRemoteService().getModelRelationById(id);
	}

	public List<ModelRelation> getAllModelRelations() {
		return getRemoteService().getAllModelRelations();
	}

	public List<ResourceRelation> getRelationsByResourceId(int id) {
		return getRemoteService().getRelationsByResourceId(id);
	}

	public List<ModelRelation> getModelRelationsByModelId(String id) {
		return getRemoteService().getModelRelationsByModelId(id);
	}

}
