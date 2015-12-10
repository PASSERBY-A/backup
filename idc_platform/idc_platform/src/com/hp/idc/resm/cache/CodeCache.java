package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.List;

import com.hp.idc.resm.model.Code;

/**
 * 资源代码定义缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CodeCache extends CacheBase<Code> {

	@Override
	protected Code createNewObject(ResultSet rs) throws Exception {
		Code c = new Code();
		if (rs != null)
			c.readFromResultSet(rs);
		return c;
	}

	@Override
	public String getCacheName() {
		return "代码定义";
	}
	
	@Override
	protected CacheStore<Code> createStore() {
		return new CodeCacheStore();
	}
	
	@Override
	protected String getInitSql() {
		return "select oid, nvl(itemorder,0) itemorder, id, name, nvl(parentoid,-1) parentoid, remark from resm_code order by id, itemorder, name";
	}
	
	/**
	 * 获取代码列表
	 * 
	 * @param codeId
	 *            代码id
	 * @return 代码列表
	 */
	public List<Code> getCodeList(String codeId) {
		CodeCacheStore s = (CodeCacheStore)this.cacheStore;
		return s.getCodeList(codeId);
	}
}
