package com.hp.idc.resm.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;

/**
 * 资源管理对象更新接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceUpdateService {

	/**
	 * 同步资源至明细表中
	 * 
	 * @param id
	 *            资源id
	 */
	public void syncResource(int id);

	/**
	 * 更新资源
	 * 
	 * @param ro
	 *            资源对象
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void updateResource(ResourceObject ro, int userId) throws Exception;

	/**
	 * 更新资源对象
	 * 
	 * @param id
	 *            资源对象id
	 * @param list
	 *            属性列表
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void updateResource(int id, List<AttributeBase> list, int userId)
			throws Exception;

	/**
	 * 添加资源对象
	 * 
	 * @param modelId
	 *            资源模型id
	 * @param list
	 *            属性列表
	 * @param userId
	 *            操作的用户id
	 * @return 资源id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public int addResource(String modelId, List<AttributeBase> list, int userId)
			throws Exception;

	/**
	 * 添加资源对象
	 * 
	 * @param modelId
	 *            资源模型id
	 * @param attributes
	 *            属性，属性id->属性值
	 * @param userId
	 *            操作的用户id
	 * @return 资源id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public int addResource(String modelId, Map<String, String> attributes,
			int userId) throws Exception;

	/**
	 * 删除资源，资源删除不会从数据库中移除记录，只是将enabled字段打上标记。
	 * 
	 * @param id
	 *            资源id
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void deleteResource(int id, int userId) throws Exception;

	/**
	 * 更新资源状态
	 * 
	 * @param id
	 *            资源id
	 * @param status
	 *            资源新的状态
	 * @param custormerId
	 * 				客户编号
	 * @param orderId
	 * 			订单编号
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 */
	public void updateResource(int id, int userId, Map<String,String> attributes)
			throws Exception;
}
