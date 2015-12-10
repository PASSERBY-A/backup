package com.hp.idc.resm.util;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * ���������б����ڿ��ټ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <K>
 *            ģ����Ĳ���������������
 * @param <T>
 *            ģ����Ĳ�����ֵ������
 * 
 */
public abstract class IndexedList<K, T extends CacheableObject> {

	/**
	 * ��ȡ��ֵ�ķ���
	 */
	protected ICompareKeyGetter<K, T> keyGetter = null;

	/**
	 * �Ƚϼ�ֵ�ķ���
	 */
	protected ICompareHandler<K> compareHandler = null;

	/**
	 * ��д��
	 */
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * ����
	 */
	protected ReadLock rlock = this.lock.readLock();

	/**
	 * д��
	 */
	protected WriteLock wlock = this.lock.writeLock();

	/**
	 * ���캯��
	 * 
	 * @param keyGetter
	 *            ��ȡ��ֵ�ķ���
	 * @param compareHandler
	 *            �Ƚϼ�ֵ�ķ���
	 */
	public IndexedList(ICompareKeyGetter<K, T> keyGetter,
			ICompareHandler<K> compareHandler) {
		this.keyGetter = keyGetter;
		this.compareHandler = compareHandler;
	}

	/**
	 * ��ȡָ��λ�õ�����
	 * 
	 * @param index
	 *            λ�ã���0��ʼ
	 * @return ָ��λ�õ�����
	 * @throws IndexOutOfBoundsException
	 *             ����λ��Խ��
	 */
	public abstract T getAt(int index) throws IndexOutOfBoundsException;

	/**
	 * ɾ��һ������
	 * 
	 * @param value
	 *            ɾ���Ķ���
	 * @return ɾ���Ķ����Ҳ���ʱ����null
	 */
	public abstract T remove(T value);

	/**
	 * ��ȡ�б�Ĵ�С
	 * 
	 * @return �б�Ĵ�С
	 */
	public abstract int size();

	/**
	 * ��������ӵ��б���
	 * 
	 * @param value
	 *            Ҫ��ӵĶ���
	 * @throws Exception
	 *             ���쳣ʱ����
	 * 
	 */
	public abstract void add(T value) throws Exception;

	/**
	 * ��������ȡ�б�=key
	 * 
	 * @param key
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getE(K key) throws Exception;

	/**
	 * ��������ȡ�б�<key
	 * 
	 * @param key
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getL(K key) throws Exception;

	/**
	 * ��������ȡ�б�<=key
	 * 
	 * @param key
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getLE(K key) throws Exception;

	/**
	 * ��������ȡ�б�>key
	 * 
	 * @param key
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getG(K key) throws Exception;

	/**
	 * ��������ȡ�б�>=key
	 * 
	 * @param key
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getGE(K key) throws Exception;

	/**
	 * ��������ȡ�б�>k1 && <k2
	 * 
	 * @param k1
	 *            �Ƚϵļ�ֵ
	 * @param k2
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getGL(K k1, K k2) throws Exception;

	/**
	 * ��������ȡ�б�>=k1 && <k2
	 * 
	 * @param k1
	 *            �Ƚϵļ�ֵ
	 * @param k2
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getGEL(K k1, K k2) throws Exception;

	/**
	 * ��������ȡ�б�>k1 && <=k2
	 * 
	 * @param k1
	 *            �Ƚϵļ�ֵ
	 * @param k2
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getGLE(K k1, K k2) throws Exception;

	/**
	 * ��������ȡ�б�>=k1 && <=k2
	 * 
	 * @param k1
	 *            �Ƚϵļ�ֵ
	 * @param k2
	 *            �Ƚϵļ�ֵ
	 * @return �����������б�
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public abstract List<T> getGELE(K k1, K k2) throws Exception;

	/**
	 * �����������
	 */
	public abstract void clear();

	/**
	 * �������ж�����б�
	 * 
	 * @return ���ж�����б�
	 */
	public abstract List<T> values();
}
