package com.hp.idc.resm.util;

/**
 * 字符串的比较接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <T> 模板类参数，表示比较的类型
 * 
 */
public class DefaultCompareHandler<T extends Comparable<T>> implements
		ICompareHandler<T> {

	public int compare(T a, T b) {
		if (a == null) {
			if (b == null)
				return 0;
			return -1;
		} else if (b == null)
			return 1;
		return a.compareTo(b);
	}
}
