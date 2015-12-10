package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.List;

import com.hp.idc.resm.resource.ResourceRelation;

/**
 * ��Դģ�͹�ϵ����
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
		return "��Դ������ϵ";
	}

	@Override
	protected CacheStore<ResourceRelation> createStore() {
		return new ResourceRelationCacheStore();
	}

	/**
	 * ��ȡ��Դ����Ĺ�����ϵ
	 * 
	 * @param id
	 *            ��Դ����id
	 * @return ��Դ����Ĺ�����ϵ
	 */
	public List<ResourceRelation> getRelationsByResourceId(int id) {
		ResourceRelationCacheStore store = (ResourceRelationCacheStore) this.cacheStore;
		return store.getRelationsByResourceId(id);
	}
}
