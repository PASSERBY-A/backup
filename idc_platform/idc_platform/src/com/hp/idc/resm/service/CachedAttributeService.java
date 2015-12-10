package com.hp.idc.resm.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.idc.resm.cache.AttributeDefineCache;
import com.hp.idc.resm.cache.CodeCache;
import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.AttributeType;
import com.hp.idc.resm.model.Code;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * 资源属性服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedAttributeService implements IAttributeService {

	/**
	 * 字段缓存
	 */
	private CodeCache codeCache = null;

	/**
	 * 字段缓存
	 */
	private AttributeDefineCache attributeDefineCache = null;

	/**
	 * 构造函数，初始化缓存
	 */
	public CachedAttributeService() {
		this.codeCache = new CodeCache();
		this.attributeDefineCache = new AttributeDefineCache();
		try {
			this.codeCache.initCache();
			this.attributeDefineCache.initCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取资源对象缓存
	 * 
	 * @return 资源对象缓存
	 */
	public AttributeDefineCache getCache() {
		return this.attributeDefineCache;
	}
	
	/**
	 * 获取代码缓存
	 * @return 代码缓存
	 */
	public CodeCache getCodeCache() {
		return this.codeCache;
	}

	/**
	 * 获取属性对象
	 * 
	 * @param id
	 *            属性id
	 * @return 属性对象
	 */
	public AttributeDefine getAttributeById(String id) {
		return this.attributeDefineCache.get(id);
	}

	/**
	 * 获取所有属性
	 * 
	 * @return 所有属性列表
	 */
	public List<AttributeDefine> getAllAttributes() {
		return this.attributeDefineCache.getAll();
	}

	@Override
	public List<AttributeType> getAllAttributeTypes() {
		List<AttributeType> list = new ArrayList<AttributeType>();
		for (AttributeType a : AttributeType.ALL_TYPES)
			list.add(a);
		return list;
	}

	@Override
	public AttributeType getAttributeTypeById(String id) {
		for (AttributeType a : AttributeType.ALL_TYPES) {
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}
	
	@Override
	public List<AttributeDefine> getAllDimensions() {
		List<AttributeDefine> list = this.attributeDefineCache.getAll();
		List<AttributeDefine> ret = new ArrayList<AttributeDefine>();
		for (AttributeDefine a : list) {
			if (a.isDimension())
				ret.add(a);
		}
		return ret;
	}
	
	@Override
	public List<ModelAttribute> getDimensionsByModelId(String modelId) {
		List<ModelAttribute> ret = new ArrayList<ModelAttribute>();
		Model m = ServiceManager.getModelService().getModelById(modelId);
		if (m != null) {
			List<ModelAttribute> attrList = m.getAttributes();
			for (ModelAttribute a : attrList) {
				if (a.getDefine().isDimension())
					ret.add(a);
			}
		}
		return ret;
	}

	@Override
	public List<AttributeDefine> getAttributesForAdd(String parentModelId) {
		List<ModelAttribute> l = ServiceManager.getModelService().getModelAttributesByModelId(parentModelId);
		List<AttributeDefine> l1 = getAllAttributes();
		
		for (Iterator<AttributeDefine> iterator = l1.iterator(); iterator.hasNext();) {
			AttributeDefine attributeDefine = iterator.next();			
			for (ModelAttribute modelAttribute : l) {
				if (!attributeDefine.isEnabled() || attributeDefine.getId().equals(modelAttribute.getAttrId())) {
					iterator.remove();
					break;
				}
			}
		}
		return l1;
	}
	
	@Override
	public List<Code> getAllCodes(){
		return this.codeCache.getAll();
	}
}
