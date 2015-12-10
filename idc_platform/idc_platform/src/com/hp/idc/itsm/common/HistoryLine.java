package com.hp.idc.itsm.common;

/**
 * ��ʾ��ʷ��¼��
 * @author ÷԰
 *
 */
public class HistoryLine {
	/**
	 * ����ʱ��
	 */
	protected String date;
	
	/**
	 * ��ʷֵ
	 */
	protected String oldValue;
	
	/**
	 * ����ֵ
	 */
	protected String newValue;
	
	/**
	 * �ֶ�id
	 */
	protected String id;
	
	/**
	 * ������
	 */
	protected String operName;

	/**
	 * ��ȡ����ʱ��
	 * @return ���ز���ʱ��
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * ���ò���ʱ��
	 * @param date ����ʱ��
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * ��ȡ�ֶ�id
	 * @return �����ֶ�id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * �����ֶ�id
	 * @param id �ֶ�id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡ����ֵ
	 * @return ���ظ���ֵ
	 */
	public String getNewValue() {
		return this.newValue;
	}

	/**
	 * ���ø���ֵ
	 * @param newValue ����ֵ
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/**
	 * ��ȡ��ʷֵ
	 * @return ������ʷֵ
	 */
	public String getOldValue() {
		return this.oldValue;
	}

	/**
	 * ������ʷֵ
	 * @param oldValue ��ʷֵ
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * ��ȡ������
	 * @return ���ز�����
	 */
	public String getOperName() {
		return this.operName;
	}

	/**
	 * ���ò�����
	 * @param operName ������
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}
}
