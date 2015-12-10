package com.hp.idc.portal.message;

import java.util.List;


/**
 * �ṩ�ⲿ������Ϣ���ѽӿ�
 * @author chengqp
 */

public interface IMessageManager {
	/**
	 * ��ģ�������Ϣ���ѵĽӿڷ���
	 * @param log
	 * @return
	 */
	public abstract int addMessage(Message message);
	
	/**
	 * ��ģ�����������Ϣ���ѵĽӿڷ���
	 * @param messages
	 * @return
	 */
	public abstract int addMessageList(List<Message> messages);
}
