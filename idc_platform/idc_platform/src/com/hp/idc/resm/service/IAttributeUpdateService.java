package com.hp.idc.resm.service;

import com.hp.idc.resm.model.AttributeDefine;

/**
 * 资源属性服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IAttributeUpdateService {

	/**
	 * 添加 / 更新资源属性对象
	 * 
	 * @param attr
	 *            资源属性
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 *             操作发生异常时产生
	 */
	public void updateAttribute(AttributeDefine attr, int userId)
			throws Exception;

	/**
	 * 禁用资源属性。已经关联模型的属性禁止进行删除。
	 * 
	 * @param id
	 *            资源属性id
	 * @param userId
	 *            操作用户id
	 * @throws Exception
	 *             操作发生异常或禁止删除时产生。
	 */
	public void removeAttribute(String id, int userId) throws Exception;
}
