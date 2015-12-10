package com.hp.idc.resm.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 排序的列表，可以快速的进行查找
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <K>
 *            模板类的参数，主键的类型
 * @param <T>
 *            模板类的参数，值的类型
 * 
 */
public class SortedArrayList<K, T extends CacheableObject> extends
		IndexedList<K, T> {

	/**
	 * 数据存储
	 */
	private List<Pair<K, T>> list = null;

	/**
	 * 比较接口
	 * 
	 */
	class PairCompareHandler implements ICompareHandler<Pair<K, T>> {
		public int compare(Pair<K, T> a, Pair<K, T> b) {
			int r = SortedArrayList.this.compareHandler.compare(a.key, b.key);
			if (r == 0)
				r = StringUtil.compareChinese(a.value.getPrimaryKey(),
						b.value.getPrimaryKey());
			return r;
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param keyGetter
	 *            获取键值的方法
	 * @param compareHandler
	 *            比较键值的方法
	 */
	public SortedArrayList(ICompareKeyGetter<K, T> keyGetter,
			ICompareHandler<K> compareHandler) {
		super(keyGetter, compareHandler);
		this.list = new ArrayList<Pair<K, T>>(10);
	}

	/**
	 * 构造函数
	 * 
	 * @param keyGetter
	 *            获取键值的方法
	 * @param compareHandler
	 *            比较键值的方法
	 * @param initList
	 *            初始化的列表
	 */
	public SortedArrayList(ICompareKeyGetter<K, T> keyGetter,
			ICompareHandler<K> compareHandler, List<T> initList) {
		super(keyGetter, compareHandler);
		initData(initList);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param keyGetter
	 *            获取键值的方法
	 * @param compareHandler
	 *            比较键值的方法
	 * @param initList
	 *            初始化的列表
	 */
	public SortedArrayList(ICompareKeyGetter<K, T> keyGetter,
			ICompareHandler<K> compareHandler, T[] initList) {
		super(keyGetter, compareHandler);
		initData(initList);
	}

	/**
	 * 初始化数据
	 * @param initList 初始化的列表
	 */
	public void initData(List<T> initList) {
		MaxHeap<Pair<K, T>> m = new MaxHeap<Pair<K, T>>();
		List<Pair<K, T>> list0 = new ArrayList<Pair<K, T>>(initList.size());
		for (T value : initList) {
			Pair<K, T> p = new Pair<K, T>();
			p.key = this.keyGetter.getCompareKey(value);
			p.value = value;
			list0.add(p);
		}
		this.list = m.sort(list0, new PairCompareHandler());
	}

	/**
	 * 初始化数据
	 * @param initList 初始化的列表
	 */
	public void initData(T[] initList) {
		MaxHeap<Pair<K, T>> m = new MaxHeap<Pair<K, T>>();
		List<Pair<K, T>> list0 = new ArrayList<Pair<K, T>>(initList.length);
		for (T value : initList) {
			Pair<K, T> p = new Pair<K, T>();
			p.key = this.keyGetter.getCompareKey(value);
			p.value = value;
			list0.add(p);
		}
		this.list = m.sort(list0, new PairCompareHandler());
	}

	@Override
	public T getAt(int index) throws IndexOutOfBoundsException {
		return this.list.get(index).value;
	}

	@Override
	public T remove(T value) {
		this.wlock.lock();
		K key = this.keyGetter.getCompareKey(value);
		int pos = getPosition(key, value);
		if (pos >= 0 && pos < this.list.size()) {
			Pair<K, T> p = this.list.get(pos);
			int r = this.compareHandler.compare(p.key, key);
			if (r == 0 && value != null) { // 键值相等，进行对象比较
				r = StringUtil.compare(p.value.getPrimaryKey(),
						value.getPrimaryKey());
			}
			if (r == 0) { // 找到了
				this.list.remove(pos);
				this.wlock.unlock();
				return p.value;
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
	public void add(T value) {
		Pair<K, T> p = new Pair<K, T>();
		p.key = this.keyGetter.getCompareKey(value);
		p.value = value;
		this.wlock.lock();
		int pos = getPosition(p.key, value);
		this.list.add(pos, p);
		this.wlock.unlock();
	}

	/**
	 * 获取一个对象在列表中应该放置的位置
	 * 
	 * @param key
	 *            对象键值
	 * @param value
	 *            对象
	 * @return 位置，从0开始
	 */
	private int getPosition(K key, T value) {
		int index = 0;
		int start = 0;
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			K compareKey = this.list.get(index).key;
			r = this.compareHandler.compare(compareKey, key);
			if (r == 0 && value != null) { // 键值相等，进行对象比较
				r = StringUtil.compareChinese(
						this.list.get(index).value.getPrimaryKey(),
						value.getPrimaryKey());
			}
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

	/**
	 * 查找时的起始和终止位置
	 * 
	 * @author 梅园
	 * 
	 */
	class Range {
		/**
		 * 开始位置
		 */
		public int start;

		/**
		 * 结束位置
		 */
		public int end;
	}

	/**
	 * 获取一个对象的主键在列表中的位置
	 * 
	 * @param key
	 *            对象键值
	 * @return 位置信息
	 */
	private Range getRange(K key) {
		int index = 0;
		int start = 0;
		int end = this.list.size() - 1;

		int r = 0;
		while (start <= end) {
			index = (start + end) / 2;
			K compareKey = this.list.get(index).key;
			r = this.compareHandler.compare(compareKey, key);
			if (r < 0) {
				start = index + 1;
			} else if (r > 0) {
				end = index - 1;
			} else if (r == 0) {
				Range range = new Range();
				range.start = index;
				range.end = index;
				while (range.start > 0
						&& this.compareHandler.compare(
								this.list.get(range.start - 1).key, key) == 0) {
					range.start--;
				}
				while (range.end < this.list.size() - 1
						&& this.compareHandler.compare(
								this.list.get(range.end + 1).key, key) == 0) {
					range.end++;
				}
				return range;
			}
		}
		return null;
	}

	/**
	 * 按条件获取位置：<key
	 * 
	 * @param key
	 *            比较对象键值
	 * @return 位置索引
	 */
	private int getPositionL(K key) {
		int pos = getPosition(key, null);
//		System.out.println("frist pos=" + pos);
		while (pos >= 0) {
			K compareKey = this.list.get(pos).key;
			int r = this.compareHandler.compare(compareKey, key);
			if (r < 0)
				break;
			System.out.println("pos--");
			pos--;
		}
		return pos;
	}

	/**
	 * 按条件获取位置：<=key
	 * 
	 * @param key
	 *            比较对象键值
	 * @return 位置索引
	 */
	private int getPositionLE(K key) {
		int pos = getPosition(key, null);
//		System.out.println("frist pos=" + pos);
		while (pos < this.list.size()) {
			K compareKey = this.list.get(pos).key;
			int r = this.compareHandler.compare(compareKey, key);
			if (r > 0)
				break;
			System.out.println("pos++");
			pos++;
		}
		return pos - 1;
	}

	/**
	 * 按条件获取位置：>key
	 * 
	 * @param key
	 *            比较对象键值
	 * @return 位置索引
	 */
	private int getPositionG(K key) {
		return getPositionLE(key) + 1;
	}

	/**
	 * 按条件获取位置：>=key
	 * 
	 * @param key
	 *            比较对象键值
	 * @return 位置索引
	 */
	private int getPositionGE(K key) {
		return getPositionL(key) + 1;
	}

	@Override
	public List<T> getE(K key) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		Range range = getRange(key);
		if (range != null) {
			for (int i = range.start; i <= range.end; i++)
				ret.add(this.list.get(i).value);
		}
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getL(K key) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int pos = getPositionL(key);
		for (int i = 0; i <= pos; i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getLE(K key) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int pos = getPositionLE(key);
		for (int i = 0; i <= pos; i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getG(K key) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int pos = getPositionG(key);
		for (int i = pos; i < this.list.size(); i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getGE(K key) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int pos = getPositionGE(key);
		for (int i = pos; i < this.list.size(); i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getGL(K k1, K k2) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int p1 = getPositionG(k1);
		int p2 = getPositionL(k2);
		for (int i = p1; i <= p2; i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getGEL(K k1, K k2) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int p1 = getPositionGE(k1);
		int p2 = getPositionL(k2);
		for (int i = p1; i <= p2; i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
	}

	@Override
	public List<T> getGLE(K k1, K k2) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int p1 = getPositionG(k1);
		int p2 = getPositionLE(k2);
		for (int i = p1; i <= p2; i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;

	}

	@Override
	public List<T> getGELE(K k1, K k2) {
		List<T> ret = new ArrayList<T>();
		this.rlock.lock();
		int p1 = getPositionGE(k1);
		int p2 = getPositionLE(k2);
		for (int i = p1; i <= p2; i++)
			ret.add(this.list.get(i).value);
		this.rlock.unlock();
		return ret;
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
		for (Pair<K, T> p : this.list) {
			ret.add(p.value);
		}
		this.rlock.unlock();
		return ret;
	}
}
