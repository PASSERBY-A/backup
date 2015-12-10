package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * 资源模型更新服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IModelUpdateService {
	/**
	 * 添加 / 更新模型
	 * 
	 * @param model
	 *            资源模型对象
	 * @param attributes
	 *            资源模型属性
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 *             操作发生异常时产生
	 */
	public void updateModel(Model model, List<ModelAttribute> attributes,
			int userId) throws Exception;

	/**
	 * 删除模型
	 * 
	 * @param modelId
	 *            资源模型id
	 * @param userId
	 *            操作的用户id
	 * @throws Exception
	 *             操作发生异常时产生
	 */
	public void removeModel(String modelId, int userId) throws Exception;
}
