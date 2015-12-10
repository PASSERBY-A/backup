/**
 * 
 */
package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * ��������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public abstract class AttributeConst implements Serializable {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 8565473583011587480L;
	
	/**
	 * id
	 */
	private String id;
	
	/**
	 * ����
	 */
	private String name;
	
	/**
	 * ���캯��
	 * @param id id
	 * @param name ����
	 */
	public AttributeConst(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * ��ȡ
	 * @return id
	 * @see #id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ����id
	 * @param id id
	 * @see #id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡ����
	 * @return ����
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ��������
	 * @param name ����
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * ��ȡ����ֵ
	 * @return ����ֵ
	 */
	public abstract String getValue();

}
