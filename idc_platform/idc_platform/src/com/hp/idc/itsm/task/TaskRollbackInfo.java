package com.hp.idc.itsm.task;

/**
 * ��ʾ���������Ϣ<br>
 * ע:��Ϊһ�����˶������ܻᵼ�¶���ڵ�Ļ���,���Ի���dataId��actureDataId������¼
 * @author ÷԰
 * 
 */
public class TaskRollbackInfo {

	/**
	 * �洢����һ���ڵ���˵�(�����ڵ�)
	 */
	protected int actureDataId;

	/**
	 * �洢����һ���ڵ���˵�
	 */
	protected int dataId;

	/**
	 * ����ʱ��
	 */
	protected String date;

	/**
	 * ����ԭ��
	 */
	protected String reason;

	/**
	 * ��ȡ����һ���ڵ���˵�
	 * @return ���ش���һ���ڵ���˵�
	 */
	public int getDataId() {
		return this.dataId;
	}

	/**
	 * ���ô���һ���ڵ���˵�
	 * @param dataId ����һ���ڵ���˵�
	 */
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	/**
	 * ��ȡ����ʱ��
	 * @return ���ػ���ʱ��
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * ���û���ʱ��
	 * @param date ����ʱ��
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * ��ȡ����ԭ��
	 * @return ���ػ���ԭ��
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * ���û���ԭ��
	 * @param reason ����ԭ��
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * ��ȡ����һ���ڵ���˵�(�����ڵ�)
	 * @return ���ش���һ���ڵ���˵�(�����ڵ�)
	 */
	public int getActureDataId() {
		return this.actureDataId;
	}

	/**
	 * ���ô���һ���ڵ���˵�(�����ڵ�)
	 * @param actureDataId ����һ���ڵ���˵�(�����ڵ�)
	 */
	public void setActureDataId(int actureDataId) {
		this.actureDataId = actureDataId;
	}
}
