package com.hp.idc.resm.ui;

import java.util.List;

import com.hp.idc.resm.cache.GlobalCache;

/**
 * 用户界面服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class UiService {

	/**
	 * 获取用户有权限的功能模块列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户有权限的功能模块列表
	 */
	public List<Module> getModules(String userId) {
		
		List<Module> l = GlobalCache.moduleCache.getAll();
		return l;
	}

	/**
	 * 获取用户有权限的菜单列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param moduleId
	 *            模块ID
	 * @return 用户有权限的菜单列表
	 */
	public List<Menu> getMenus(String userId, int moduleId) {
		return GlobalCache.menuCache.getMenus(moduleId);
	}

}
