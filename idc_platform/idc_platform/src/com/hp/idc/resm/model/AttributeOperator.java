/**
 * 
 */
package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * ���ԱȽ϶���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class AttributeOperator implements Serializable {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 1403605430772697706L;
	
	/**
	 * ������
	 */
	private String op;
	
	/**
	 * ����
	 */
	private String opName;

	/**
	 * ģ��
	 */
	private String template;
	
	/**
	 * ���캯��
	 * @param op ������
	 * @param opName ����
	 * @param template ģ��
	 */
	public AttributeOperator(final String op, final String opName, final String template) {
		setOp(op);
		setOpName(opName);
		setTemplate(template);
	}

	/**
	 * ��ȡ������
	 * @return ������
	 * @see #op
	 */
	public String getOp() {
		return this.op;
	}

	/**
	 * ���ò�����
	 * @param op ������
	 * @see #op
	 */
	public void setOp(String op) {
		this.op = op;
	}

	/**
	 * ��ȡ����
	 * @return ����
	 * @see #opName
	 */
	public String getOpName() {
		return this.opName;
	}

	/**
	 * ��������
	 * @param opName ����
	 * @see #opName
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * ��ȡģ��
	 * @return ģ��
	 * @see #template
	 */
	public String getTemplate() {
		return this.template;
	}

	/**
	 * ����ģ��
	 * @param template ģ��
	 * @see #template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	/**
	 * �Ƿ���Ҫ�Ƚ϶���
	 * @return true=��Ҫ��false=����Ҫ
	 */
	public boolean isNeedOpValue() {
		return this.template.indexOf("$1") != -1;
	}

	
}
