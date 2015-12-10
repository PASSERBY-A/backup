package com.hp.idc.resm.util.checker;

/**
 * 较验器接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IChecker {

	/**
	 * 较验值是否合法
	 * 
	 * @param value
	 *            数值
	 * @return 非法的原因。如果返回null，则表示较验通过
	 */
	public String isValid(String attrName, String value);
}
