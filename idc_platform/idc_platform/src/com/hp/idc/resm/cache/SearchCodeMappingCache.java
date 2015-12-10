package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.model.SearchCodeMapping;

/**
 * 搜索代码定义缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class SearchCodeMappingCache extends CacheBase<SearchCodeMapping> {

	@Override
	protected SearchCodeMapping createNewObject(ResultSet rs) throws Exception {
		SearchCodeMapping c = new SearchCodeMapping();
		if (rs != null)
			c.readFromResultSet(rs);
		return c;
	}

	@Override
	public String getCacheName() {
		return "搜索代码定义";
	}
	
	@Override
	protected String getInitSql() {
		return "select modelid, prefix from resm_searchcode where modelid is not null and prefix is not null";
	}
}
