package com.hp.idc.resm.util;

/**
 * �ַ����ıȽϽӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ChineseStringCompareHandler implements ICompareHandler<String> {

	public int compare(String a, String b) {
		return StringUtil.compareChinese(a, b);
	}
}
