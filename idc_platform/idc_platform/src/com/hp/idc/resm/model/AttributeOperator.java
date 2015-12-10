/**
 * 
 */
package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * 属性比较定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class AttributeOperator implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1403605430772697706L;
	
	/**
	 * 操作符
	 */
	private String op;
	
	/**
	 * 名称
	 */
	private String opName;

	/**
	 * 模板
	 */
	private String template;
	
	/**
	 * 构造函数
	 * @param op 操作符
	 * @param opName 名称
	 * @param template 模板
	 */
	public AttributeOperator(final String op, final String opName, final String template) {
		setOp(op);
		setOpName(opName);
		setTemplate(template);
	}

	/**
	 * 获取操作符
	 * @return 操作符
	 * @see #op
	 */
	public String getOp() {
		return this.op;
	}

	/**
	 * 设置操作符
	 * @param op 操作符
	 * @see #op
	 */
	public void setOp(String op) {
		this.op = op;
	}

	/**
	 * 获取名称
	 * @return 名称
	 * @see #opName
	 */
	public String getOpName() {
		return this.opName;
	}

	/**
	 * 设置名称
	 * @param opName 名称
	 * @see #opName
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * 获取模板
	 * @return 模板
	 * @see #template
	 */
	public String getTemplate() {
		return this.template;
	}

	/**
	 * 设置模板
	 * @param template 模板
	 * @see #template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	/**
	 * 是否需要比较对象
	 * @return true=需要，false=不需要
	 */
	public boolean isNeedOpValue() {
		return this.template.indexOf("$1") != -1;
	}

	
}
