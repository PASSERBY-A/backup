package com.hp.idc.resm.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用了List的缓存库的模板类。<br/>
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            模板类参数，表示缓存对象类型
 */
public class ListCacheStore<T extends CacheableObject> extends CacheStore<T> {

	/**
	 * 存储所有缓存对象的列表
	 */
	public List<T> data = new ArrayList<T>();
	
	@Override
	public void initData(List<T> arr) {
		this.data.addAll(arr);
	}

	/**
	 * 获取指定主键在列表中的索引位置
	 * 
	 * @param key
	 *            主键
	 * @return 索引位置，从0开始，找不到时返回-1
	 */
	private int indexOf(String key) {
		for (int i = 0; i < this.data.size(); i++) {
			if (this.data.get(i).getPrimaryKey().equals(key))
				return i;
		}
		return -1;
	}

	@Override
	public T get(String key) {
		int pos = indexOf(key);
		if (pos == -1)
			return null;
		return this.data.get(pos);
	}

	@Override
	public T put(T obj) {
		T ret = null;
		int pos = indexOf(obj.getPrimaryKey());
		if (pos != -1)
			ret = this.data.remove(pos);
		this.data.add(obj);
		return ret;
	}

	@Override
	public T remove(String key) {
		int pos = indexOf(key);
		if (pos != -1)
			return this.data.remove(pos);
		return null;
	}

	@Override
	public void clear() {
		this.data.clear();
	}

	@Override
	public List<T> values() {
		List<T> list = new ArrayList<T>();
		list.addAll(this.data);
		return list;
	}

	@Override
	public int size() {
		return this.data.size();
	}

}
