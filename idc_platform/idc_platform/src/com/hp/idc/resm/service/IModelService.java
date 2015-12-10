package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * 资源模型服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IModelService {

	/**
	 * 获取所有模型
	 * 
	 * @param userId
	 *            操作用户id
	 * 
	 * @return 所有模型列表
	 */
	public List<Model> getAllModels(int userId);

	/**
	 * 根据id获取模型，不受权限控制
	 * 
	 * @param id
	 *            模型id
	 * @return 资源模型对象
	 */
	public Model getModelById(String id);

	/**
	 * 根据id获取父模型，不受权限控制
	 * 
	 * @param id
	 *            模型id
	 * @return 父模型对象
	 */
	public Model getParentModelById(String id);

	/**
	 * 获取子模型对象（一级）
	 * 
	 * @param id
	 *            模型id
	 * @param userId
	 *            操作用户id
	 * @return 模型子对象列表
	 */
	public List<Model> getChildModelsById(String id, int userId);

	/**
	 * 获取子模型对象（多级）
	 * 
	 * @param id
	 *            模型id
	 * @param recursive
	 *            递归查询所有子级
	 * @param userId
	 *            操作用户id
	 * @return 模型子对象列表
	 */
	public List<Model> getChildModelsById(String id, boolean recursive,
			int userId);

	/**
	 * 根据模型id获取模型的所有属性
	 * 
	 * @param id
	 *            模型id
	 * @return 模型的所有属性列表
	 */
	public List<ModelAttribute> getModelAttributesByModelId(String id);

	/**
	 * 获取模型的指定属性
	 * 
	 * @param modeId
	 *            模型id
	 * @param attrId
	 *            属性id
	 * @return 模型属性
	 */
	public ModelAttribute getModelAttribute(String modeId, String attrId);

	/**
	 * 按用户权限对列表中的模型对象进行过滤
	 * @param list 资模型对象列表
	 * @param userId 用户id
	 * @param type 权限类型
	 * @return 用户有访问授权的模型对象
	 * @see CachedModelService#PERM_ACCESS
	 * @see CachedModelService#PERM_CREATE_OBJECT
	 */
	public List<Model> filterByUser(List<Model> list, int userId, int type);
}
