/*
 * @(#)IDCMessageListener.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.msgct;

import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

/**
 * ��Ϣ�����࣬ʵ��MessageListener�ӿڣ�����onMessage�е��쳣����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:35:47 PM 2011
 * 
 */

public abstract class IDCMessageListener implements MessageListener {

	/**
	 * log4j��־
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * ʵ�ʴ�����Ϣ��ִ�ж���
	 * @param m				���յ�����Ϣ
	 * @throws Exception    ����������п����׵��쳣
	 */
	public abstract void onMessageDo(Message m) throws Exception;
	
	/**
	 * ʵ�ʴ�����Ϣ��ִ�ж���
	 * @param m				���յ�����Ϣ
	 * @throws Exception    ����������п����׵��쳣
	 */
	public abstract void onMessageDo(Map<String, Object> m) throws Exception;
	
	/**
	 * ���onMessageDo�������׳����쳣���д���,�÷���������Ҫ�����쳣
	 * @param e				onMessageDo�������׳����쳣
	 */
	public abstract void handleExcetpiton(Exception e);	
	
	/**
	 * ��������Ϣʱ��ִ�ж������÷���һ�㲻����д
	 * @param m				���յ�����Ϣ
	 */
	public final void onMessage(Message m) {
		try{
			onMessageDo(m);
		}catch(Exception e){
			try{
				handleExcetpiton(e);
			}catch(Exception e2){
				logger.error("handleExcetpiton�������쳣", e2);
			}
		}
	}
	
	/**
	 * ��������Ϣʱ��ִ�ж������÷���һ�㲻����д
	 * (�ṩ������Ҫjms���õķ�ʽ)
	 * @param m				���յ�����Ϣ
	 */
	public final void onMessage(Map<String, Object> m){
		try{
			onMessageDo(m);
		}catch(Exception e){
			try{
				handleExcetpiton(e);
			}catch(Exception e2){
				logger.error("handleExcetpiton�������쳣", e2);
			}
		}
	}
}
