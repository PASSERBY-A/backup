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
 * 资源模型服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedModelService implements IModelService {

	/**
	 * 模型访问权限
	 */
	public static final int PERM_ACCESS = 0;
	
	/**
	 * 创建模型下的资源对象权限
	 */
	public static final int PERM_CREATE_OBJECT = 1;

	/**
	 * 资源模型缓存
	 */
	private ModelCache modelCache = null;

	/**
	 * 资源模型属性的缓存
	 */
	private ModelAttributeCache modelAttributeCache = null;

	/**
	 * 搜索代码定义的缓存
	 */
	private SearchCodeMappingCache searchCodeMappingCache = null;

	/**
	 * 构造函数，初始化缓存
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
	 * 获取资源模型缓存对象
	 * 
	 * @return 资源模型缓存对象
	 */
	public ModelCache getCache() {
		return this.modelCache;
	}

	/**
	 * 获取模型属性的缓存对象
	 * 
	 * @return 模型属性的缓存对象
	 */
	public ModelAttributeCache getAttributeCache() {
		return this.modelAttributeCache;
	}
	
	/**
	 * 获取搜索代码定义的缓存
	 * 
	 * @return 搜索代码定义的缓存
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
	 * 获取模型树(给Flex使用)
	 * 
	 * @return 树结构XML文本
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
	 * 获取模型可继承属性，此方法通过递归获取所有层级的可继承属性
	 * 
	 * @param id
	 *            模型id
	 * 
	 * @return 可继承属性的列表
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
			if (f) { // 发现相同的
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
