package com.hp.idc.resm.cache;

import java.util.List;

/**
 * �ṩ����Ŀ��ģ���࣬���಻��ʵ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            ģ����Ĳ�������ʾ����Ķ���
 */
public abstract class CacheStore<T extends CacheableObject> {

	/**
	 * �ڿ���̨���������Ϣ
	 */
	public void dump() {
		System.out.println(getClass().getName() + "û��ʵ��dump()������");
	}

	/**
	 * ��ʼ������
	 * @param arr ����
	 */
	public abstract void initData(List<T> arr);
	
	/**
	 * ��ʼ������
	 */
	public void initIndex() {
		// ����������ʵ��
	}

	/**
	 * ���������ӻ����ж�ȡһ������
	 * 
	 * @param key
	 *            ����
	 * @return ����ͬ�����Ķ����Ҳ���ʱ����null
	 */
	public abstract T get(String key);

	/**
	 * ��һ��������ӶԻ�����
	 * 
	 * @param obj
	 *            �������
	 * @return ԭ��������ͬ�����Ķ���
	 */
	public abstract T put(T obj);

	/**
	 * ��һ������ӶԻ�����ɾ��
	 * 
	 * @param key
	 *            ����
	 * @return ɾ���Ķ���
	 */
	public abstract T remove(String key);

	/**
	 * ��ջ���
	 */
	public abstract void clear();

	/**
	 * ���ػ��������ж�����б�
	 * 
	 * @return ���������ж�����б�
	 */
	public abstract List<T> values();

	/**
	 * ���ػ��������ж��������
	 * 
	 * @return ���������ж��������
	 */
	public abstract int size();

}
