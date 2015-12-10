/**
 * 
 */
package com.hp.idc.resm.util;

/**
 * 存储键、值
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <K>
 *            模板类的参数，主键的类型
 * @param <T>
 *            模板类的参数，值的类型
 */
public class Pair<K, T> {
	/**
	 * 主键
	 */
	public K key;

	/**
	 * 值
	 */
	public T value;
}