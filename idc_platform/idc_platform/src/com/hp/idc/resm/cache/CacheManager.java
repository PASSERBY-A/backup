package com.hp.idc.resm.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存管理
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CacheManager {

	/**
	 * 所有缓存
	 */
	private static Map<String, CacheBase<? extends CacheableObject>> caches = new HashMap<String, CacheBase<? extends CacheableObject>>();

	/**
	 * 获取所有缓存
	 * @return 所有缓存列表
	 */
	public static List<CacheBase<? extends CacheableObject>> getAllCaches() {
		List<CacheBase<? extends CacheableObject>> list = new ArrayList<CacheBase<? extends CacheableObject>>();
		list.addAll(caches.values());
		return list;
	}
	
	/**
	 * 获取指定名称的缓存
	 * @param name 缓存名称
	 * @return 缓存对象
	 */
	public static CacheBase<? extends CacheableObject> getCacheByName(String name) {
		return caches.get(name);
	}
	
	/**
	 * 注册缓存
	 * 
	 * @param c
	 *            缓存类
	 * @throws Exception
	 *             缓存名称重复
	 */
	public static void registerCache(CacheBase<? extends CacheableObject> c)
			throws Exception {
		if (caches.put(c.getCacheName(), c) != null) {
			throw new Exception("缓存名称" + c.getCacheName() + "重复:"+caches.keySet().toString());
		}
	}

	/**
	 * 删除缓存
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param object
	 *            对象
	 */
	public static void removeCache(String cacheName, CacheableObject object) {
		@SuppressWarnings("unchecked")
		CacheBase<CacheableObject> c = (CacheBase<CacheableObject>) caches
				.get(cacheName);
		if (c != null)
			c.remove(object, false);
	}

	/**
	 * 增加缓存
	 * 
	 * @param cacheName
	 *            缓存名称
	 * @param object
	 *            对象
	 */
	public static void addCache(String cacheName, CacheableObject object) {
		@SuppressWarnings("unchecked")
		CacheBase<CacheableObject> c = (CacheBase<CacheableObject>) caches
				.get(cacheName);
		if (c != null) {
			c.add(object, false);
		}
	}
}
