package com.hp.idc.resm.util;

/**
 * �����ıȽϽӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class IntegerCompareHandler implements ICompareHandler<Integer> {

	public int compare(Integer a, Integer b) {
		if (a == null) {
			if (b == null)
				return 0;
			return -1;
		} else if (b == null)
			return 1;
		return a.compareTo(b);
	}
}
