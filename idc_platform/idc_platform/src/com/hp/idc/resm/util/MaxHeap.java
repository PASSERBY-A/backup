package com.hp.idc.resm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * �����㷨����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            ģ����Ĳ��������������
 */
public class MaxHeap<T> {

	/**
	 * ���б������������
	 * 
	 * @param list
	 *            �����б�
	 * @param compare
	 *            �Ƚϵķ���
	 * @return �������б�
	 */
	public List<T> sort(List<T> list, ICompareHandler<T> compare) {
		return sort(list, compare, false);
	}

	/**
	 * 
	 * ���б��������
	 * 
	 * @param list
	 *            �����б�
	 * @param compare
	 *            �Ƚϵķ���
	 * @param desc
	 *            true=����false=����
	 * @return �������б�
	 */
	@SuppressWarnings("unchecked")
	public List<T> sort(List<T> list, ICompareHandler<T> compare, boolean desc) {
		int currentSize = 0;
		Object heap[] = new Object[list.size() + 1];
		List<T> ret = new ArrayList<T>(list.size());
		for (int x = 0; x < list.size(); x++) {
			// Ϊ x Ѱ��Ӧ����λ��
			// i ���µ�Ҷ�ڵ㿪ʼ��������������
			int i = ++currentSize;
			while (i != 1 && compare.compare(list.get(x), (T) heap[i / 2]) > 0) {
				// ���ܹ���x ����h e a p [ i ]
				heap[i] = heap[i / 2]; // ��Ԫ������
				i /= 2; // ���򸸽ڵ�
			}
			heap[i] = list.get(x);
		}

		while (currentSize > 0) {
			T x = (T) heap[1]; // ���Ԫ��
			ret.add(x);
			// �ع���
			T y = (T) heap[currentSize--]; // ���һ��Ԫ��
			// �Ӹ���ʼ��Ϊ y Ѱ�Һ��ʵ�λ��
			int i = 1, // �ѵĵ�ǰ�ڵ�
			ci = 2; // i�ĺ���
			while (ci <= currentSize) {
				// heap[ci] Ӧ��i�Ľϴ�ĺ���
				if (ci < currentSize
						&& compare.compare((T) heap[ci], (T) heap[ci + 1]) < 0)
					ci++;
				// �ܰ�y ����h e a p [ i ]��?
				if (compare.compare(y, (T) heap[ci]) >= 0)
					break; // ��
				// ����
				heap[i] = heap[ci]; // ����������
				i = ci; // ����һ��
				ci *= 2;
			}
			heap[i] = y;
		}
		if (!desc) {
			List<T> ret2 = new ArrayList<T>(ret.size());
			for (int i = ret.size() - 1; i >= 0; i--)
				ret2.add(ret.get(i));
			ret = ret2;
		}
		return ret;
	}
}
