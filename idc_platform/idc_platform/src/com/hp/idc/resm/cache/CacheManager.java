package com.hp.idc.resm.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CacheManager {

	/**
	 * ���л���
	 */
	private static Map<String, CacheBase<? extends CacheableObject>> caches = new HashMap<String, CacheBase<? extends CacheableObject>>();

	/**
	 * ��ȡ���л���
	 * @return ���л����б�
	 */
	public static List<CacheBase<? extends CacheableObject>> getAllCaches() {
		List<CacheBase<? extends CacheableObject>> list = new ArrayList<CacheBase<? extends CacheableObject>>();
		list.addAll(caches.values());
		return list;
	}
	
	/**
	 * ��ȡָ�����ƵĻ���
	 * @param name ��������
	 * @return �������
	 */
	public static CacheBase<? extends CacheableObject> getCacheByName(String name) {
		return caches.get(name);
	}
	
	/**
	 * ע�Ỻ��
	 * 
	 * @param c
	 *            ������
	 * @throws Exception
	 *             ���������ظ�
	 */
	public static void registerCache(CacheBase<? extends CacheableObject> c)
			throws Exception {
		if (caches.put(c.getCacheName(), c) != null) {
			throw new Exception("��������" + c.getCacheName() + "�ظ�:"+caches.keySet().toString());
		}
	}

	/**
	 * ɾ������
	 * 
	 * @param cacheName
	 *            ��������
	 * @param object
	 *            ����
	 */
	public static void removeCache(String cacheName, CacheableObject object) {
		@SuppressWarnings("unchecked")
		CacheBase<CacheableObject> c = (CacheBase<CacheableObject>) caches
				.get(cacheName);
		if (c != null)
			c.remove(object, false);
	}

	/**
	 * ���ӻ���
	 * 
	 * @param cacheName
	 *            ��������
	 * @param object
	 *            ����
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
