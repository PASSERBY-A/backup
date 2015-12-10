package com.hp.idc.itsm.common;

import java.util.HashMap;
import java.util.Map;

public class ExtendMap{

	private Map map1 = new HashMap();
	
	public ExtendMap(){
		
	}
	
	public void put(Object key1,Object key2,Object value){
		if (key1 == null || key2 == null)
			return;
		Map mapTemp = null;
		if (map1.get(key1)!=null) {
			mapTemp = (Map)map1.get(key1);
			mapTemp.put(key2, value);
		} else {
			mapTemp = new HashMap();
			mapTemp.put(key2, value);
			map1.put(key1, mapTemp);
		}
	}
	
	/**
	 * 批量put数据，
	 * @param map 必须是Map[key,Map[]]的结构，否则会忽略
	 */
	public void putAll(Map map){
		Object[] key = map.keySet().toArray();
		for (int i = 0; i < key.length; i++) {
			Object value = map.get(key[i]);
			if (value instanceof Map){
				map1.put(key[i], value);
			}
		}
	}
	
	/**
	 * 获取具体的单个值
	 * @param key1
	 * @param key2
	 * @return
	 */
	public Object get(Object key1,Object key2){
		Object value = null;
		if (map1.get(key1)!=null){
			Map map = (Map)map1.get(key1);
			value = map.get(key2);
		}
		return value;
	}
	
	/**
	 * 获取第一维的值
	 * @param key1
	 * @return Map[key2,value]
	 */
	public Map get(Object key1){
		return (Map)map1.get(key1);
	}
	
	/**
	 * 获取第二维的值
	 * @param key2
	 * @return Map[key1,value]
	 */
	public Map getByKey2(Object key2){
		Map retMap = new HashMap();
		Object[] key = map1.keySet().toArray();
		for (int i = 0; i < key.length; i++) {
			Map map = (Map)map1.get(key[i]);
			if (map.get(key2)!=null){
				retMap.put(key[i], map.get(key2));
			}
		}
		return retMap;
	}
}
