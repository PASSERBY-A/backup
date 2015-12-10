package com.hp.idc.resm.util;

/**
 * ����ıȽϽӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            ģ����������Ƚ϶��������
 */
public interface ICompareHandler<T> {
	/**
	 * �Ƚ϶���
	 * 
	 * @param a
	 *            ����a
	 * @param b
	 *            ����b
	 * @return a>bʱ����������a=bʱ����0��a<bʱ���ظ���
	 */
	public int compare(T a, T b);
}
