package com.hp.idc.resm.cache;

import java.sql.ResultSet;

import com.hp.idc.resm.ui.Icon;

/**
 * Í¼±ê»º´æ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class IconCache extends CacheBase<Icon> {

	@Override
	protected Icon createNewObject(ResultSet rs) throws Exception {
		Icon i = new Icon();
		if (rs != null)
			i.readFromResultSet(rs);
		return i;
	}

	@Override
	public String getCacheName() {
		return "Í¼±ê";
	}
}
