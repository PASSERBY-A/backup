package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.model.ModelRelation;

/**
 * ��Դģ�͹�ϵ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelRelationCache extends CacheBase<ModelRelation> {

	@Override
	protected ModelRelation createNewObject(ResultSet rs) throws Exception {
		ModelRelation m = new ModelRelation();
		if (rs != null)
			m.readFromResultSet(rs);
		return m;

	}

	@Override
	public String getCacheName() {
		return "ģ���ϵ����";
	}

	@Override
	protected CacheStore<ModelRelation> createStore() {
		return new ListCacheStore<ModelRelation>();
	}

}
