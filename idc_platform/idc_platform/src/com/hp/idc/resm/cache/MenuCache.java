package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.ui.Menu;

/**
 * 菜单缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class MenuCache extends CacheBase<Menu> {

	@Override
	protected Menu createNewObject(ResultSet rs) throws Exception {
		Menu m = new Menu();
		if (rs != null)
			m.readFromResultSet(rs);
		return m;
	}

	@Override
	public String getCacheName() {
		return "菜单定义";
	}

	@Override
	protected CacheStore<Menu> createStore() {
		return new ListCacheStore<Menu>();
	}

	@Override
	protected String getInitSql() {
		return "select * from resm_menu order by idx";
	}

	/**
	 * 获取指定模块的所有菜单列表
	 * 
	 * @param moduleId
	 *            指定模块的id
	 * @return 菜单列表
	 */
	public List<Menu> getMenus(int moduleId) {
		List<Menu> all = this.cacheStore.values();
		List<Menu> list = new ArrayList<Menu>();
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).getModuleId() == moduleId)
				list.add(all.get(i));
		}
		return list;
	}
}
