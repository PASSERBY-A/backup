package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.ui.Module;

/**
 * ģ�黺��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModuleCache extends CacheBase<Module> {

	@Override
	protected Module createNewObject(ResultSet rs) throws Exception {
		Module m = new Module();
		if (rs != null)
			m.readFromResultSet(rs);
		return m;
	}

	@Override
	public String getCacheName() {
		return "ģ�鶨��";
	}

	@Override
	protected CacheStore<Module> createStore() {
		return new ListCacheStore<Module>();
	}

}
