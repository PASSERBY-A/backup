package com.hp.idc.resm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 最大堆算法排序
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 * @param <T>
 *            模板类的参数，对象的类型
 */
public class MaxHeap<T> {

	/**
	 * 对列表进行排序（正序）
	 * 
	 * @param list
	 *            对象列表
	 * @param compare
	 *            比较的方法
	 * @return 排序后的列表
	 */
	public List<T> sort(List<T> list, ICompareHandler<T> compare) {
		return sort(list, compare, false);
	}

	/**
	 * 
	 * 对列表进行排序
	 * 
	 * @param list
	 *            对象列表
	 * @param compare
	 *            比较的方法
	 * @param desc
	 *            true=反序，false=正序
	 * @return 排序后的列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> sort(List<T> list, ICompareHandler<T> compare, boolean desc) {
		int currentSize = 0;
		Object heap[] = new Object[list.size() + 1];
		List<T> ret = new ArrayList<T>(list.size());
		for (int x = 0; x < list.size(); x++) {
			// 为 x 寻找应插入位置
			// i 从新的叶节点开始，并沿着树上升
			int i = ++currentSize;
			while (i != 1 && compare.compare(list.get(x), (T) heap[i / 2]) > 0) {
				// 不能够把x 放入h e a p [ i ]
				heap[i] = heap[i / 2]; // 将元素下移
				i /= 2; // 移向父节点
			}
			heap[i] = list.get(x);
		}

		while (currentSize > 0) {
			T x = (T) heap[1]; // 最大元素
			ret.add(x);
			// 重构堆
			T y = (T) heap[currentSize--]; // 最后一个元素
			// 从根开始，为 y 寻找合适的位置
			int i = 1, // 堆的当前节点
			ci = 2; // i的孩子
			while (ci <= currentSize) {
				// heap[ci] 应是i的较大的孩子
				if (ci < currentSize
						&& compare.compare((T) heap[ci], (T) heap[ci + 1]) < 0)
					ci++;
				// 能把y 放入h e a p [ i ]吗?
				if (compare.compare(y, (T) heap[ci]) >= 0)
					break; // 能
				// 不能
				heap[i] = heap[ci]; // 将孩子上移
				i = ci; // 下移一层
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
