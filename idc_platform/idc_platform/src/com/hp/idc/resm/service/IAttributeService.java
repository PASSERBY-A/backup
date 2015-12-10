package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.AttributeType;
import com.hp.idc.resm.model.Code;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * 资源属性服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IAttributeService {

	/**
	 * 根据id获取属性对象
	 * 
	 * @param id
	 *            属性id
	 * @return 属性对象
	 */
	public AttributeDefine getAttributeById(String id);

	/**
	 * 获取所有属性
	 * 
	 * @return 所有属性列表
	 */
	public List<AttributeDefine> getAllAttributes();

	/**
	 * 获取所有维度
	 * 
	 * @return 所有维度属性列表
	 */
	public List<AttributeDefine> getAllDimensions();

	/**
	 * 获取模型下的维度
	 * 
	 * @param modelId
	 *            模型id
	 * @return 维度属性列表
	 */
	public List<ModelAttribute> getDimensionsByModelId(String modelId);

	/**
	 * 获取所有的属性类型
	 * 
	 * @return 所有的属性类型列表
	 */
	public List<AttributeType> getAllAttributeTypes();

	/**
	 * 根据id获取资源属性类型
	 * 
	 * @param id
	 *            资源属性类型id
	 * @return 资源属性类型
	 */
	public AttributeType getAttributeTypeById(String id);

	/**
	 * 获取除掉parentModelId包含的属性
	 * 
	 * @param parentModelId
	 *            模型ID
	 * @return 属性列表
	 */
	public List<AttributeDefine> getAttributesForAdd(String parentModelId);
	
	/**
	 * 获取所有代码
	 * @return 代码列表
	 */
	public List<Code> getAllCodes();

}
