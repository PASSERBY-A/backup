package com.hp.idc.resm.service;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.ModelAttributeCache;
import com.hp.idc.resm.cache.ModelCache;
import com.hp.idc.resm.cache.SearchCodeMappingCache;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.RoleUtil;

import flex.messaging.io.BeanProxy;

/**
 * ��Դģ�ͷ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedModelService implements IModelService {

	/**
	 * ģ�ͷ���Ȩ��
	 */
	public static final int PERM_ACCESS = 0;
	
	/**
	 * ����ģ���µ���Դ����Ȩ��
	 */
	public static final int PERM_CREATE_OBJECT = 1;

	/**
	 * ��Դģ�ͻ���
	 */
	private ModelCache modelCache = null;

	/**
	 * ��Դģ�����ԵĻ���
	 */
	private ModelAttributeCache modelAttributeCache = null;

	/**
	 * �������붨��Ļ���
	 */
	private SearchCodeMappingCache searchCodeMappingCache = null;

	/**
	 * ���캯������ʼ������
	 */
	public CachedModelService() {
		BeanProxy.addIgnoreProperty(Model.class, "childs");
		this.modelCache = new ModelCache();
		this.modelAttributeCache = new ModelAttributeCache();
		this.searchCodeMappingCache = new SearchCodeMappingCache();
		try {
			this.modelCache.initCache();
			this.modelAttributeCache.initCache();
			this.searchCodeMappingCache.initCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��Դģ�ͻ������
	 * 
	 * @return ��Դģ�ͻ������
	 */
	public ModelCache getCache() {
		return this.modelCache;
	}

	/**
	 * ��ȡģ�����ԵĻ������
	 * 
	 * @return ģ�����ԵĻ������
	 */
	public ModelAttributeCache getAttributeCache() {
		return this.modelAttributeCache;
	}
	
	/**
	 * ��ȡ�������붨��Ļ���
	 * 
	 * @return �������붨��Ļ���
	 */
	public SearchCodeMappingCache getSearchCodeMappingCache() {
		return this.searchCodeMappingCache;
	}

	public Model getModelById(String id) {
		return this.modelCache.get(id);
	}

	public List<Model> getAllModels(int userId) {
		return filterByUser(this.modelCache.getAll(), userId, PERM_ACCESS);
	}

	public List<Model> getChildModelsById(String id, int userId) {
		return getChildModelsById(id, false, userId);
	}

	public List<Model> getChildModelsById(String id, boolean recursive, int userId) {
		return filterByUser(this.modelCache.getChildModelsById(id, recursive), userId, PERM_ACCESS);
	}

	/**
	 * ��ȡģ����(��Flexʹ��)
	 * 
	 * @return ���ṹXML�ı�
	 */
	public String getModelTree() {
		return this.modelCache.getModelTree();
	}

	public Model getParentModelById(String id) {
		Model m = getModelById(id);
		if (m == null)
			return null;
		return this.modelCache.get(m.getParentId());
	}

	/**
	 * ��ȡģ�Ϳɼ̳����ԣ��˷���ͨ���ݹ��ȡ���в㼶�Ŀɼ̳�����
	 * 
	 * @param id
	 *            ģ��id
	 * 
	 * @return �ɼ̳����Ե��б�
	 */
	protected List<ModelAttribute> getInheritableAttributesByModelId(String id) {
		List<ModelAttribute> list = new ArrayList<ModelAttribute>();
		Model m = getModelById(id);
		if (m != null)
			list.addAll(getInheritableAttributesByModelId(m.getParentId()));
		list.addAll(this.modelAttributeCache.getInheritableAttributesById(id));
		return list;
	}

	public List<ModelAttribute> getModelAttributesByModelId(String id) {
		List<ModelAttribute> list = new ArrayList<ModelAttribute>();
		Model m = getModelById(id);
		if (m != null)
			list.addAll(getInheritableAttributesByModelId(m.getParentId()));
		list.addAll(this.modelAttributeCache.getAttributesById(id));
		for (int i = 0; i < list.size(); i++) {
			boolean f = false;
			String s = list.get(i).getAttrId();
			for (int j = i + 1; j < list.size(); j++) {
				if (s.equals(list.get(j).getAttrId())) {
					f = true;
					break;
				}
			}
			if (f) { // ������ͬ��
				list.remove(i);
				i--;
			}
		}
		return list;

	}

	public ModelAttribute getModelAttribute(String modeId, String attrId) {
		Model m = this.getModelById(modeId);
		if (m == null)
			return null;
		return m.getAttributeById(attrId);
	}

	public List<Model> filterByUser(List<Model> list, int userId, int type) {
		try {
			if (type == PERM_ACCESS)
				return RoleUtil.filterByUserPermission(userId, new String[] { "model_access" }, list);
			if (type == PERM_CREATE_OBJECT) {
				List<Model> l = RoleUtil.filterByUserPermission(userId, new String[] { "model_access" }, list);
				l = RoleUtil.filterByUserPermission(userId, new String[] { "model_createobject" }, l);
				return l;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Model>();
	}
}
