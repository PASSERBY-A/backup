package com.hp.idc.resm.util;

/**
 * 对象的键生成器，用于比较
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <K>
 *            模板类参数，对象的键类型
 * @param <T>
 *            模板类参数，对象的类型
 */
public interface ICompareKeyGetter<K, T> {
	/**
	 * 生成用于对象比较的键值
	 * 
	 * @param obj
	 *            对象
	 * @return 键值
	 */
	public K getCompareKey(T obj);
}
