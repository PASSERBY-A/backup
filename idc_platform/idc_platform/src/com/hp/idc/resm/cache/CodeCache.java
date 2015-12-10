package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.List;

import com.hp.idc.resm.model.Code;

/**
 * ��Դ���붨�建��
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
		return "���붨��";
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
	 * ��ȡ�����б�
	 * 
	 * @param codeId
	 *            ����id
	 * @return �����б�
	 */
	public List<Code> getCodeList(String codeId) {
		CodeCacheStore s = (CodeCacheStore)this.cacheStore;
		return s.getCodeList(codeId);
	}
}
