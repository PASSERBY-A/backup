package com.hp.idc.itsm.message;

import java.util.Date;

/**
 * ��ʾ��������
 * @author ÷԰
 *
 */
public class SmsMessage {
	/**
	 * �洢����oid
	 */
	protected int oid;
	
	/**
	 * �洢�������ֻ���
	 */
	protected String sender = "";
	
	/**
	 * �洢�������ֻ���
	 */
	protected String receiver;
	
	/**
	 * �洢��������
	 */
	protected String content;
	
	/**
	 * �������,����.�ӵ�ǰʱ������.Ĭ��Ϊ24Сʱ!
	 */
	protected long liveTime = 24 * 60 * 60 * 1000;
	
	/**
	 * ��ʱ���͵�ʱ��
	 */
	protected Date atTime = null;

	/**
	 * ��ȡ��������
	 * @return ���ض�������
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * ���ô���������
	 * @param content ����������
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * ��ȡoid
	 * @return ����oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * ����oid
	 * @param oid oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * ��ȡ�������ֻ���
	 * @return ���ؽ������ֻ���
	 */
	public String getReceiver() {
		return this.receiver;
	}

	/**
	 * ���ý������ֻ���
	 * @param receiver �������ֻ���
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * ��ȡ�������ֻ���
	 * @return ���ط������ֻ���
	 */
	public String getSender() {
		return this.sender;
	}

	/**
	 * ���÷������ֻ���
	 * @param sender �������ֻ���
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public long getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(long liveTime) {
		this.liveTime = liveTime;
	}

	public Date getAtTime() {
		return atTime;
	}

	public void setAtTime(Date atTime) {
		this.atTime = atTime;
	}

	/**
	 * ����toString()����
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getReceiver() + ":" + this.getContent();
	}
}
