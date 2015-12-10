package com.hp.idc.resm.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * ʹ����List�Ļ�����ģ���ࡣ<br/>
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            ģ�����������ʾ�����������
 */
public class ListCacheStore<T extends CacheableObject> extends CacheStore<T> {

	/**
	 * �洢���л��������б�
	 */
	public List<T> data = new ArrayList<T>();
	
	@Override
	public void initData(List<T> arr) {
		this.data.addAll(arr);
	}

	/**
	 * ��ȡָ���������б��е�����λ��
	 * 
	 * @param key
	 *            ����
	 * @return ����λ�ã���0��ʼ���Ҳ���ʱ����-1
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
