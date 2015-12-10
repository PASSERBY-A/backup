package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.security.Role;

/**
 * ��ɫ���建��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RoleCache extends CacheBase<Role> {

	@Override
	protected Role createNewObject(ResultSet rs) throws Exception {
		Role c = new Role();
		if (rs != null)
			c.readFromResultSet(rs);
		return c;
	}

	@Override
	public String getCacheName() {
		return "��ɫ����";
	}
	
	@Override
	protected CacheStore<Role> createStore() {
		return new RoleCacheStore();
	}
}
