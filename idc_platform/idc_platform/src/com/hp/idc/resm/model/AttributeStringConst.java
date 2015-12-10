/**
 * 
 */
package com.hp.idc.resm.model;

/**
 * 字符常量
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeStringConst extends AttributeConst {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 7475277196331707634L;

	/**
	 * 常量值
	 */
	private String value;
	
	/**
	 * 构造函数
	 * @param id id
	 * @param name 名称
	 * @param value 常量值
	 */
	public AttributeStringConst(String id, String name, String value) {
		super(id, name);
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * 设置常量值
	 * 
	 * @param value
	 *            常量值
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
