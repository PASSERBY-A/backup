package com.hp.idc.resm.util.checker;

/**
 * 整型较验器
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class IntegerChecker implements IChecker {

	/**
	 * 最小值
	 */
	private Integer minValue = null;

	/**
	 * 最大值
	 */
	private Integer maxValue = null;

	public String isValid(String attrName, String value) {
		int n = 0;
		try {
			n = Integer.parseInt(value);
		} catch (Exception e) {
			return "“"+attrName+"数值格式非法,请填写整数值";
		}
		if (this.minValue != null && this.minValue > n)
			return "“"+attrName+"”不通低于设定的最小值" + this.minValue;
		if (this.maxValue != null && this.maxValue < n)
			return "“"+attrName+"”不能超过设定的最大值" + this.maxValue;
		return null;
	}

}
