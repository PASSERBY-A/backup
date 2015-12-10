package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.model.RelationDefine;

/**
 * ������ϵ���建��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RelationDefineCache extends CacheBase<RelationDefine> {

	@Override
	protected RelationDefine createNewObject(ResultSet rs) throws Exception {
		RelationDefine r = new RelationDefine();
		if (rs != null)
			r.readFromResultSet(rs);
		return r;
	}

	@Override
	public String getCacheName() {
		return "������ϵ����";
	}
}
