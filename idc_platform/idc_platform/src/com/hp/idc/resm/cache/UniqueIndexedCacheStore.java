package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.util.UniqueIndexedList;

/**
 * 使用了Hash表的缓存库的模板类。<br/>
 * Hash表使用主键->对象的映射，用来存储所有的缓存对象。
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            模板类参数，表示缓存对象类型
 */
public class UniqueIndexedCacheStore<T extends CacheableObject> extends
		CacheStore<T> {

	/**
	 * 主键->对象的映射，用来存储所有的缓存对象。
	 */
	// public ConcurrentMap<String, T> data = new ConcurrentHashMap<String,
	// T>();
	public UniqueIndexedList<T> data = null;

	@Override
	public void initData(List<T> arr) {
		this.data = new UniqueIndexedList<T>(arr);
	}
	
	@Override
	public T get(String key) {
		return this.data.get(key);
	}

	@Override
	public T put(T obj) {
		return this.data.put(obj);
	}

	@Override
	public T remove(String key) {
		return this.data.remove(key);
	}

	@Override
	public void clear() {
		this.data.clear();
	}

	@Override
	public List<T> values() {
		return this.data.values();
	}

	@Override
	public int size() {
		return this.data.size();
	}

}
