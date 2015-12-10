package com.hp.idc.resm.util.checker;

/**
 * ���ͽ�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class IntegerChecker implements IChecker {

	/**
	 * ��Сֵ
	 */
	private Integer minValue = null;

	/**
	 * ���ֵ
	 */
	private Integer maxValue = null;

	public String isValid(String attrName, String value) {
		int n = 0;
		try {
			n = Integer.parseInt(value);
		} catch (Exception e) {
			return "��"+attrName+"��ֵ��ʽ�Ƿ�,����д����ֵ";
		}
		if (this.minValue != null && this.minValue > n)
			return "��"+attrName+"����ͨ�����趨����Сֵ" + this.minValue;
		if (this.maxValue != null && this.maxValue < n)
			return "��"+attrName+"�����ܳ����趨�����ֵ" + this.maxValue;
		return null;
	}

}
