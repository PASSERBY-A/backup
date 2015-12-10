package com.hp.idc.resm.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 排序的列表，可以快速的进行查找
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <T>
 *            模板类的参数，值的类型
 * 
 */
public class UniqueIndexedList<T extends CacheableObject> extends
		IndexedList<String, T> {

	/**
	 * 数据存储
	 */
	private List<T> list = null;

	/**
	 * 比较接口
	 * 
	 */
	private class CompareHandler implements ICompareHandler<T> {
		public int compare(T a, T b) {
			return StringUtil.compareChinese(a.getPrimaryKey(),
					b.getPrimaryKey());
		}
	}

	/**
	 * 构造函数
	 */
	public UniqueIndexedList() {
		super(null, new ChineseStringCompareHandler());
		this.list = new ArrayList<T>(10);
	}

	/**
	 * 构造函数
	 * 
	 * @param initList
	 *            初始化的列表
	 */
	@SuppressWarnings("synthetic-access")
	public UniqueIndexedList(List<T> initList) {
		super(null, new ChineseStringCompareHandler());
		MaxHeap<T> m = new MaxHeap<T>();
		this.list = m.sort(initList, new CompareHandler());
	}

	@Override
	public T getAt(int index) throws IndexOutOfBoundsException {
		return this.list.get(index);
	}

	/**
	 * 按主键删除对象
	 * 
	 * @param id
	 *            主键
	 * @return 删除的对象
	 */
	public T remove(String id) {
		int index = 0;
		int start = 0;
		this.wlock.lock();
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			String compareKey = this.list.get(index).getPrimaryKey();
			r = this.compareHandler.compare(compareKey, id);
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				T ret = this.list.remove(index);
				this.wlock.unlock();
				return ret; // 找到了
			}
		}
		this.wlock.unlock();
		return null;
	}

	@Override
	public T remove(T value) {
		this.wlock.lock();
		int pos = getPosition(value);
		if (pos >= 0 && pos < this.list.size()) {
			String key = this.list.get(pos).getPrimaryKey();
			int r = this.compareHandler.compare(key, value.getPrimaryKey());
			if (r == 0) { // 相同
				T ret = this.list.remove(pos);
				this.wlock.unlock();
				return ret;
			}
		}
		this.wlock.unlock();
		return null;
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public void add(T value) throws Exception {
		this.wlock.lock();
		int pos = getPosition(value);
		if (pos >= 0 && pos < this.list.size()) {
			String key = this.list.get(pos).getPrimaryKey();
			int r = this.compareHandler.compare(key, value.getPrimaryKey());
			if (r == 0) { // 相同
				this.wlock.unlock();
				throw new Exception("主键重复");
			}
		}
		this.list.add(pos, value);
		this.wlock.unlock();
	}

	/**
	 * 添加对象，并返回原来有相同主键的值
	 * 
	 * @param value
	 *            对象
	 * @return 原来有相同主键的值，没有时返回null
	 */
	public T put(T value) {
		this.wlock.lock();
		int pos = getPosition(value);
		if (pos >= 0 && pos < this.list.size()) {
			String key = this.list.get(pos).getPrimaryKey();
			int r = this.compareHandler.compare(key, value.getPrimaryKey());
			if (r == 0) { // 相同
				T ret = this.list.set(pos, value);
				this.wlock.unlock();
				return ret;
			}
		}
		this.list.add(pos, value);
		this.wlock.unlock();
		return null;
	}

	/**
	 * 根据主键获取对象
	 * 
	 * @param id
	 *            主键
	 * @return 对象，找不到时返回null
	 */
	public T get(String id) {
		int index = 0;
		int start = 0;
		this.rlock.lock();
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			String compareKey = this.list.get(index).getPrimaryKey();
			r = this.compareHandler.compare(compareKey, id);
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				this.rlock.unlock();
				return this.list.get(index); // 找到了
			}
		}
		this.rlock.unlock();
		return null;
	}

	/**
	 * 获取一个对象在列表中应该放置的位置
	 * 
	 * @param value
	 *            对象
	 * @return 位置，从0开始
	 */
	private int getPosition(T value) {
		int index = 0;
		int start = 0;
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			String compareKey = this.list.get(index).getPrimaryKey();
			r = this.compareHandler.compare(compareKey, value.getPrimaryKey());
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				return index; // 找到了
			}
		}
		if (r < 0) // 没找到时，根据比较结果位置修正
			return index + 1;
		return index;
	}

	@Override
	public List<T> getE(String key) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getL(String key) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getLE(String key) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getG(String key) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getGE(String key) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getGL(String k1, String k2) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getGEL(String k1, String k2) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getGLE(String k1, String k2) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public List<T> getGELE(String k1, String k2) throws Exception {
		throw new Exception("不支持的方法");
	}

	@Override
	public void clear() {
		this.wlock.lock();
		this.list.clear();
		this.wlock.unlock();
	}

	@Override
	public List<T> values() {
		this.rlock.lock();
		List<T> ret = new ArrayList<T>(this.list.size());
		ret.addAll(this.list);
		this.rlock.unlock();
		return ret;
	}
}
