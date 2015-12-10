/**
 * 
 */
package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.resource.ResourceRelation;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IRelationUpdateService {

	/**
	 * 更新资源的关联关系。
	 * 
	 * @param id
	 *            资源对象的id
	 * @param relationList
	 *            资源对象的全部关联关系。不在列表中的关联关系将被移除。
	 * @param userId
	 *            操作用户的id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void updateResourceRelation(int id,
			List<ResourceRelation> relationList, int userId) throws Exception;

	/**
	 * 新增资源的关联关系。
	 * 
	 * @param relation
	 *            资源对象的关联关系
	 * @param userId
	 *            操作用户的id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void addResourceRelation(
			ResourceRelation relation, int userId) throws Exception;
	
	/**
	 * 新增模型关联关系
	 * 
	 * @param relation
	 *            关联关系定义
	 * @param userId
	 *            操作用户的id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void addModelRelation(ModelRelation relation, int userId)
			throws Exception;

	/**
	 * 删除模型关联关系
	 * 
	 * @param id
	 *            关联关系id
	 * @param userId
	 *            操作用户的id
	 * @throws Exception
	 *             操作有异常时发生，或不允许删除关联关系（有对象已经建立了关联关系）
	 */
	public void removeModelRelation(int id, int userId) throws Exception;
}
