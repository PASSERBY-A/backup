package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.security.BasicPermission;

/**
 * ����Ȩ�޶��建��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class BasicPermissionCache extends CacheBase<BasicPermission> {

	@SuppressWarnings("unchecked")
	@Override
	protected BasicPermission createNewObject(ResultSet rs) throws Exception {
		BasicPermission r;
		if (rs != null) {
			String className = rs.getString("classname");
			Class<? extends BasicPermission> cls = BasicPermission.class;
			if (className != null && className.length() > 0)
				cls = (Class<? extends BasicPermission>) Class.forName(className);
			r = cls.newInstance();

			r.readFromResultSet(rs);
		} else {
			r = new BasicPermission();
		}
		return r;
	}
	
	@Override
	public String getCacheName() {
		return "����Ȩ�޶���";
	}
	
}
