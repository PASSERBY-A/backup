package com.hp.idc.resm.cache;



/**
 * 全局缓存，正在修改，准备去除这个类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class GlobalCache {
	/**
	 * 图标缓存
	 */
	public static IconCache iconCache = null;
	
	/**
	 * 模块缓存
	 */
	public static ModuleCache moduleCache = null;
	
	/**
	 * 菜单缓存
	 */
	public static MenuCache menuCache = null;

	
	/**
	 * 初始化
	 */
	public void init() {
		try {
			iconCache = new IconCache();
			iconCache.initCache();

			moduleCache = new ModuleCache();
			moduleCache.initCache();

			menuCache = new MenuCache();
			menuCache.initCache();
			

	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
