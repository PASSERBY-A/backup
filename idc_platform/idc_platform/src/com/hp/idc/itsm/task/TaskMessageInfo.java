package com.hp.idc.itsm.task;

/**
 * ��ʾ�����е���Ϣ
 * @author ÷԰
 * 
 */
public class TaskMessageInfo {
	/**
	 * ��Ϣʱ��
	 */
	protected String date;

	/**
	 * ��Ϣ����
	 */
	protected String content;

	/**
	 * ������
	 */
	protected String operName;

	/**
	 * ��ȡ��Ϣʱ��
	 * @return ������Ϣʱ��
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * ������Ϣʱ��
	 * @param date ��Ϣʱ��
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * ��ȡ��Ϣ����
	 * @return ������Ϣ����
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * ������Ϣ����
	 * @param content ��Ϣ����
	 */
	public void setContent(String content) {
		this.content = content;
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
