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
 * ��Դ���Է���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedAttributeService implements IAttributeService {

	/**
	 * �ֶλ���
	 */
	private CodeCache codeCache = null;

	/**
	 * �ֶλ���
	 */
	private AttributeDefineCache attributeDefineCache = null;

	/**
	 * ���캯������ʼ������
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
	 * ��ȡ��Դ���󻺴�
	 * 
	 * @return ��Դ���󻺴�
	 */
	public AttributeDefineCache getCache() {
		return this.attributeDefineCache;
	}
	
	/**
	 * ��ȡ���뻺��
	 * @return ���뻺��
	 */
	public CodeCache getCodeCache() {
		return this.codeCache;
	}

	/**
	 * ��ȡ���Զ���
	 * 
	 * @param id
	 *            ����id
	 * @return ���Զ���
	 */
	public AttributeDefine getAttributeById(String id) {
		return this.attributeDefineCache.get(id);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ���������б�
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
