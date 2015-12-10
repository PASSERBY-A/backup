package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.model.RelationDefine;
import com.hp.idc.resm.resource.ResourceRelation;

/**
 * 关联关系服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IRelationService {

	/**
	 * 获取关系定义
	 * 
	 * @param id
	 *            关系id
	 * @return 关系定义
	 */
	public RelationDefine getRelationDefineById(String id);

	/**
	 * 获取所有关系定义
	 * 
	 * @return 所有关系定义
	 */
	public List<RelationDefine> getAllRelationDefines();

	/**
	 * 获取模型关系
	 * 
	 * @param id
	 *            模型关系id
	 * @return 模型关系对象
	 */
	public ModelRelation getModelRelationById(int id);

	/**
	 * 获取指定模型的所有关系
	 * 
	 * @param id
	 *            模型id
	 * @return 模型关系列表
	 */
	public List<ModelRelation> getModelRelationsByModelId(String id);

	/**
	 * 获取所有模型关系
	 * 
	 * @return 所有模型关系
	 */
	public List<ModelRelation> getAllModelRelations();

	/**
	 * 获取资源对象的关联关系
	 * 
	 * @param id
	 *            资源对象id
	 * 
	 * @return 资源对象的关联关系
	 */
	public List<ResourceRelation> getRelationsByResourceId(int id);

}
