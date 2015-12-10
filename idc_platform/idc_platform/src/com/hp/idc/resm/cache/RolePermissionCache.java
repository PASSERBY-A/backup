package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.security.RolePermission;

/**
 * 角色权限缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RolePermissionCache extends CacheBase<RolePermission> {

	@Override
	protected RolePermission createNewObject(ResultSet rs) throws Exception {
		RolePermission c = new RolePermission();
		if (rs != null)
			c.readFromResultSet(rs);
		return c;
	}

	@Override
	public String getCacheName() {
		return "角色权限";
	}
	
	@Override
	protected CacheStore<RolePermission> createStore() {
		return new RolePermissionCacheStore();
	}
	
}
