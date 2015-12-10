package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.ModelAttribute;

/**
 * 资源模型属性的缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelAttributeCache extends CacheBase<ModelAttribute> {

	/**
	 * 获取指定资源模型中，可继承的属性列表。<br/>
	 * 注：从父模型继承的属性并不包含在此列表中。
	 * 
	 * @param id
	 *            资源模型id
	 * @return 可继承的属性列表
	 */
	public List<ModelAttribute> getInheritableAttributesById(String id) {
		List<ModelAttribute> list = new ArrayList<ModelAttribute>();
		List<ModelAttribute> l0 = ((ModelAttributeCacheStore) this.cacheStore)
				.getListByModelId(id);
		for (ModelAttribute a : l0) {
			if (a.isInheritable())
				list.add(a);
		}
		return list;
	}

	/**
	 * 获取指定资源模型中的属性列表。<br/>
	 * 注：从父模型继承的属性并不包含在此列表中。
	 * 
	 * @param id
	 *            资源模型id
	 * @return 属性列表
	 */
	public List<ModelAttribute> getAttributesById(String id) {
		List<ModelAttribute> list = ((ModelAttributeCacheStore) this.cacheStore)
				.getListByModelId(id);
		return list;
	}

	@Override
	protected ModelAttribute createNewObject(ResultSet rs) throws Exception {
		ModelAttribute m = new ModelAttribute();
		if (rs != null)
			m.readFromResultSet(rs);
		return m;
	}

	@Override
	public String getCacheName() {
		return "模型属性";
	}

	@Override
	protected CacheStore<ModelAttribute> createStore() {
		return new ModelAttributeCacheStore();
	}
}
