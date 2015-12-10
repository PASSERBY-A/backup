package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.model.RelationDefine;

/**
 * 关联关系定义缓存
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
		return "关联关系定义";
	}
}
