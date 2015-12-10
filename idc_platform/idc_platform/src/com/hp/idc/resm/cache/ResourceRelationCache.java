package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.List;

import com.hp.idc.resm.resource.ResourceRelation;

/**
 * 资源模型关系缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceRelationCache extends CacheBase<ResourceRelation> {

	@Override
	protected ResourceRelation createNewObject(ResultSet rs) throws Exception {

		ResourceRelation r = new ResourceRelation();
		if (rs != null)
			r.readFromResultSet(rs);
		return r;

	}

	@Override
	public String getCacheName() {
		return "资源关联关系";
	}

	@Override
	protected CacheStore<ResourceRelation> createStore() {
		return new ResourceRelationCacheStore();
	}

	/**
	 * 获取资源对象的关联关系
	 * 
	 * @param id
	 *            资源对象id
	 * @return 资源对象的关联关系
	 */
	public List<ResourceRelation> getRelationsByResourceId(int id) {
		ResourceRelationCacheStore store = (ResourceRelationCacheStore) this.cacheStore;
		return store.getRelationsByResourceId(id);
	}
}
