/*
 * @(#)IMessageCenter.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.msgct;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Message;

/**
 * ��Ϣ���Ľӿ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:34:38 PM 2011
 * 
 */

public interface IMessageCenter {

	/**
	 * ���Ӷ�һ����Ϣ���첽����
	 * @param dest      		��ϢĿ��
	 * @param topicMode  		��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param listener  		������
	 * @param consumerId        ��Ϣ������Id������ģʽʱ�������ã�����ģʽʱ����Ϊnull<br>
	 * PS:	consumerId���ܰ������(.),������Ϣ���Ļ��������
	 */
	public void addListener(String dest, boolean topicMode, IDCMessageListener listener, String consumerId) throws Exception;
	
	/**
	 * ���Ӷ�һ����Ϣ���첽������ֻ����������������Ϣ
	 * @param dest      		��ϢĿ��
	 * @param topicMode  		��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param listener  		������
	 * @param consumerId        ��Ϣ������Id������ģʽʱ�������ã�����ģʽʱ����Ϊnull<br>
	 * PS:	consumerId���ܰ������(.),������Ϣ���Ļ��������
	 * @param messageSelector	��Ϣ������������Message Headers��Message Properties����Ϣ���й��ˣ����ʽ�������SQL92���Ӽ�	
	 */
	public void addListener(String dest, boolean topicMode, IDCMessageListener listener, String consumerId, String messageSelector) throws Exception;
	
	/**
	 * �жϸ�����Ϣ�Ƿ��Ѿ����ڼ��������÷���ֻ���жϱ�Ӧ�����Ƿ��Ѿ����ڼ������޷���ȡ������Ӧ���еļ���
	 * @param dest				��ϢĿ��
	 * @param topicMode  		��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param consumerId        ��Ϣ������Id������ģʽʱ�������ã�����ģʽʱ����Ϊnull
	 * @return 					�Ƿ����
	 */
	public boolean existListener(String dest, boolean topicMode, String consumerId) throws Exception;
	
	/**
	 * ɾ����ϢĿ���Ӧ�ļ����� ���÷���Ҳֻ��ɾ����Ӧ���еļ���
	 * @param dest 				��ϢĿ��
	 * @param topicMode  		��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param consumerId        ��Ϣ������Id������ģʽʱ�������ã�����ģʽʱ����Ϊnull
	 */
	public void removeListener(String dest, boolean topicMode, String consumerId) throws Exception;
	
	/**
	 * ͬ������ĳ��ϢĿ���ϵ���Ϣ<br>
	 * PS:�������ô˷���ͬ����������ģʽ����Ϣ����Ϊ�ǳ־ö��ĵĻ����޷���ȡ�����߲�����ʱ����Ϣ��<br>
	 * ���һ��Ҫ�ã���һ�������һ�ν��յ�ʱ�򷵻�Ϊ�գ������ٽ��ղ��ܷ�����Ϣ
	 * @param dest				��ϢĿ��
	 * @param topicMode			��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param receiveTimeout    ���ճ�ʱʱ�������룩
	 * @return
	 * @throws Exception
	 */
	public Message receiveMessage(String dest, boolean topicMode, long receiveTimeout) throws Exception;
	
	/**
	 * ͬ������ĳ��ϢĿ����������������Ϣ<br>
	 * PS:�������ô˷���ͬ����������ģʽ����Ϣ����Ϊ�ǳ־ö��ĵĻ����޷���ȡ�����߲�����ʱ����Ϣ��<br>
	 * ���һ��Ҫ�ã���һ�������һ�ν��յ�ʱ�򷵻�Ϊ�գ������ٽ��ղ��ܷ�����Ϣ
	 * @param dest				��ϢĿ��
	 * @param topicMode			��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param receiveTimeout    ���ճ�ʱʱ�������룩
	 * @param messageSelector	��Ϣ������������Message Headers��Message Properties����Ϣ���й��ˣ����ʽ�������SQL92���Ӽ�
	 * @return
	 * @throws Exception
	 */
	public Message receiveSelectedMessage(String dest, boolean topicMode, long receiveTimeout, String messageSelector) throws Exception;
	
	/**
	 * ����Ĭ�����ã������ı���Ϣ
	 * @param dest				��ϢĿ��
	 * @param topicMode			��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param messageBody		��Ϣ����
	 * @throws Exception		���ͳ���ʱ���쳣
	 */
	public void publishTextMessage(String dest, boolean topicMode, String messageBody) throws Exception;
	
	/**
	 * �����Զ������ã������ı���Ϣ
	 * @param dest				��ϢĿ��
	 * @param topicMode			��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param messageBody		��Ϣ����
	 * @param mpp				����ѡ��	
	 * @param mp				��Ϣ���Զ������ԣ���������Ϣ����
	 * @throws Exception		���ͳ���ʱ���쳣
	 */
	public void publishTextMessage(String dest, boolean topicMode, String messageBody, MessagePublishProperty mpp, Properties mp) throws Exception;	
	
	/**
	 * ����Ĭ�����ã����Ϳ����л�������Ϣ
	 * @param dest				��ϢĿ��
	 * @param topicMode			��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param messageBody		��Ϣ����
	 * @throws Exception		���ͳ���ʱ���쳣
	 */
	public void publishObjectMessage(String dest, boolean topicMode, Serializable messageBody) throws Exception;
	
	/**
	 * �����Զ������ã����Ϳ����л�������Ϣ
	 * @param dest				��ϢĿ��
	 * @param topicMode			��ϢĿ���Ƿ�����ģʽ��true:����ģʽ��false:����ģʽ
	 * @param messageBody		��Ϣ����
	 * @param mpp				����ѡ��	
	 * @param mp				��Ϣ���Զ������ԣ���������Ϣ����
	 * @throws Exception		���ͳ���ʱ���쳣
	 */
	public void publishObjectMessage(String dest, boolean topicMode, Serializable messageBody, MessagePublishProperty mpp, Properties mp) throws Exception;
}
