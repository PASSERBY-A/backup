package com.hp.idc.resm.cache;

import java.util.List;

/**
 * 提供缓存的库的模板类，此类不能实例化
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            模板类的参数，表示缓存的对象
 */
public abstract class CacheStore<T extends CacheableObject> {

	/**
	 * 在控制台输出调试信息
	 */
	public void dump() {
		System.out.println(getClass().getName() + "没有实现dump()方法。");
	}

	/**
	 * 初始化数据
	 * @param arr 数据
	 */
	public abstract void initData(List<T> arr);
	
	/**
	 * 初始化索引
	 */
	public void initIndex() {
		// 在派生类中实现
	}

	/**
	 * 根据主键从缓存中读取一个对象
	 * 
	 * @param key
	 *            主键
	 * @return 有相同主键的对象，找不到时返回null
	 */
	public abstract T get(String key);

	/**
	 * 将一个对象添加对缓存中
	 * 
	 * @param obj
	 *            缓存对象
	 * @return 原来具有相同主键的对象
	 */
	public abstract T put(T obj);

	/**
	 * 将一个对象从对缓存中删除
	 * 
	 * @param key
	 *            主键
	 * @return 删除的对象
	 */
	public abstract T remove(String key);

	/**
	 * 清空缓存
	 */
	public abstract void clear();

	/**
	 * 返回缓存中所有对象的列表
	 * 
	 * @return 缓存中所有对象的列表
	 */
	public abstract List<T> values();

	/**
	 * 返回缓存中所有对象的数量
	 * 
	 * @return 缓存中所有对象的数量
	 */
	public abstract int size();

}
