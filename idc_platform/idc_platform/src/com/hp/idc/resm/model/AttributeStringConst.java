/**
 * 
 */
package com.hp.idc.resm.model;

/**
 * �ַ�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeStringConst extends AttributeConst {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 7475277196331707634L;

	/**
	 * ����ֵ
	 */
	private String value;
	
	/**
	 * ���캯��
	 * @param id id
	 * @param name ����
	 * @param value ����ֵ
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
	 * ���ó���ֵ
	 * 
	 * @param value
	 *            ����ֵ
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
