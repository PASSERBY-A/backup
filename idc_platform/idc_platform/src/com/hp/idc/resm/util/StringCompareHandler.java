package com.hp.idc.resm.util;

/**
 * �ַ����ıȽϽӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class StringCompareHandler implements ICompareHandler<String> {

	public int compare(String a, String b) {
		return StringUtil.compare(a, b);
	}
}