package com.hp.idc.resm.util;

/**
 * ����ļ������������ڱȽ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <K>
 *            ģ�������������ļ�����
 * @param <T>
 *            ģ������������������
 */
public interface ICompareKeyGetter<K, T> {
	/**
	 * �������ڶ���Ƚϵļ�ֵ
	 * 
	 * @param obj
	 *            ����
	 * @return ��ֵ
	 */
	public K getCompareKey(T obj);
}
