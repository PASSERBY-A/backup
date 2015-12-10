package com.hp.idc.resm.util;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 带索引的列表，用于快速检索
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <K>
 *            模板类的参数，主键的类型
 * @param <T>
 *            模板类的参数，值的类型
 * 
 */
public abstract class IndexedList<K, T extends CacheableObject> {

	/**
	 * 获取键值的方法
	 */
	protected ICompareKeyGetter<K, T> keyGetter = null;

	/**
	 * 比较键值的方法
	 */
	protected ICompareHandler<K> compareHandler = null;

	/**
	 * 读写锁
	 */
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 读锁
	 */
	protected ReadLock rlock = this.lock.readLock();

	/**
	 * 写锁
	 */
	protected WriteLock wlock = this.lock.writeLock();

	/**
	 * 构造函数
	 * 
	 * @param keyGetter
	 *            获取键值的方法
	 * @param compareHandler
	 *            比较键值的方法
	 */
	public IndexedList(ICompareKeyGetter<K, T> keyGetter,
			ICompareHandler<K> compareHandler) {
		this.keyGetter = keyGetter;
		this.compareHandler = compareHandler;
	}

	/**
	 * 获取指定位置的数据
	 * 
	 * @param index
	 *            位置，从0开始
	 * @return 指定位置的数据
	 * @throws IndexOutOfBoundsException
	 *             索引位置越界
	 */
	public abstract T getAt(int index) throws IndexOutOfBoundsException;

	/**
	 * 删除一个对象
	 * 
	 * @param value
	 *            删除的对象
	 * @return 删除的对象，找不到时返回null
	 */
	public abstract T remove(T value);

	/**
	 * 获取列表的大小
	 * 
	 * @return 列表的大小
	 */
	public abstract int size();

	/**
	 * 将对象添加到列表中
	 * 
	 * @param value
	 *            要添加的对象
	 * @throws Exception
	 *             有异常时发生
	 * 
	 */
	public abstract void add(T value) throws Exception;

	/**
	 * 按条件获取列表：=key
	 * 
	 * @param key
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getE(K key) throws Exception;

	/**
	 * 按条件获取列表：<key
	 * 
	 * @param key
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getL(K key) throws Exception;

	/**
	 * 按条件获取列表：<=key
	 * 
	 * @param key
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getLE(K key) throws Exception;

	/**
	 * 按条件获取列表：>key
	 * 
	 * @param key
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getG(K key) throws Exception;

	/**
	 * 按条件获取列表：>=key
	 * 
	 * @param key
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getGE(K key) throws Exception;

	/**
	 * 按条件获取列表：>k1 && <k2
	 * 
	 * @param k1
	 *            比较的键值
	 * @param k2
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getGL(K k1, K k2) throws Exception;

	/**
	 * 按条件获取列表：>=k1 && <k2
	 * 
	 * @param k1
	 *            比较的键值
	 * @param k2
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getGEL(K k1, K k2) throws Exception;

	/**
	 * 按条件获取列表：>k1 && <=k2
	 * 
	 * @param k1
	 *            比较的键值
	 * @param k2
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getGLE(K k1, K k2) throws Exception;

	/**
	 * 按条件获取列表：>=k1 && <=k2
	 * 
	 * @param k1
	 *            比较的键值
	 * @param k2
	 *            比较的键值
	 * @return 满足条件的列表
	 * @throws Exception
	 *             有异常时发生
	 */
	public abstract List<T> getGELE(K k1, K k2) throws Exception;

	/**
	 * 清空所有数据
	 */
	public abstract void clear();

	/**
	 * 返回所有对象的列表
	 * 
	 * @return 所有对象的列表
	 */
	public abstract List<T> values();
}
