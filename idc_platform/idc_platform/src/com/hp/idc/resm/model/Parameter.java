package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * ��������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Parameter implements Serializable {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 3132018643426193833L;

	/**
	 * ����ID
	 */
	private String id;

	/**
	 * ��������
	 */
	private String name;

	/**
	 * ��������
	 */
	private String remark;

	/**
	 * ���캯������ʼ��Parameter����
	 * 
	 * @param id
	 *            ����id
	 * @param name
	 *            ��������
	 * @param remark
	 *            ����˵��
	 */
	public Parameter(String id, String name, String remark) {
		this.id = id;
		this.name = name;
		this.remark = remark;
	}

	/**
	 * ��ȡ����id
	 * 
	 * @return ����id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ���ò���id
	 * 
	 * @param id
	 *            ����id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���ò�������
	 * 
	 * @param name
	 *            ��������
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * ���ò�������
	 * 
	 * @param remark
	 *            ��������
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
