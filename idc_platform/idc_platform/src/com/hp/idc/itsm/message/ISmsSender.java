package com.hp.idc.itsm.message;

/**
 * ��ʾ���Ͷ��ŵĽӿ�<br>
 * ����������ͨ������MessageManager.SmsSender������ʵ�ֶ��ŵķ���<br>
 * �磺SMPPSmsSenderΪ���ӿڵ�ʵ��<br>
 * ��ô������䣺MessageManager.SmsSender = new SMPPSmsSender()����
 * @author ÷԰
 *
 */
public interface ISmsSender {
	/**
	 * ���Ͷ���
	 * @param sms ��������
	 * @return �ɹ�����null, ���򷵻ش�����Ϣ
	 */
	public String send(SmsMessage sms);
}
