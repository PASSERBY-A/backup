package com.hp.idc.resm.util;

/**
 * 对象的比较接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            模板类参数，比较对象的类型
 */
public interface ICompareHandler<T> {
	/**
	 * 比较对象
	 * 
	 * @param a
	 *            对象a
	 * @param b
	 *            对象b
	 * @return a>b时返回正数，a=b时返回0，a<b时返回负数
	 */
	public int compare(T a, T b);
}
