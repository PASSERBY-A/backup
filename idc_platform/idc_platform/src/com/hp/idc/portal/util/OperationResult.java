package com.hp.idc.portal.util;


/**
 * ��ʾ�����ķ��ؽ�������Ի�ȡ�����ĳɹ�/ʧ��״̬�Լ���ص���ϸ����
 * @author ÷԰
 *
 */
public class OperationResult {
	/**
	 * �洢�����Ƿ�ɹ�
	 */
	protected boolean success = true;
	
	/**
	 * �洢�����������ϸ��Ϣ��Ĭ��Ϊ�ɹ�����Ϣ
	 */
	protected String message = "���Ĳ������ύ�ɹ�";

	/** 
	 * ��ȡ�����������ϸ��Ϣ
	 * @return ���ز����������ϸ��Ϣ
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * ���ò����������ϸ��Ϣ
	 * @param message �����������ϸ��Ϣ
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/** 
	 * ��ȡ�����Ƿ�ɹ�
	 * @return ���ز����Ƿ�ɹ�
	 */
	public boolean isSuccess() {
		return this.success;
	}
	
	/**
	 * ���ò�������Ƿ�ɹ�
	 * @param success ��������Ƿ�ɹ�
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
