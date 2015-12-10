package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.util.UniqueIndexedList;

/**
 * ʹ����Hash��Ļ�����ģ���ࡣ<br/>
 * Hash��ʹ������->�����ӳ�䣬�����洢���еĻ������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            ģ�����������ʾ�����������
 */
public class UniqueIndexedCacheStore<T extends CacheableObject> extends
		CacheStore<T> {

	/**
	 * ����->�����ӳ�䣬�����洢���еĻ������
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
