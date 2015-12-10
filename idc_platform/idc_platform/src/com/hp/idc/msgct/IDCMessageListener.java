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
 * 消息监听类，实现MessageListener接口，增加onMessage中的异常处理
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:35:47 PM 2011
 * 
 */

public abstract class IDCMessageListener implements MessageListener {

	/**
	 * log4j日志
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 实际处理消息的执行动作
	 * @param m				接收到的消息
	 * @throws Exception    处理过过程中可能抛的异常
	 */
	public abstract void onMessageDo(Message m) throws Exception;
	
	/**
	 * 实际处理消息的执行动作
	 * @param m				接收到的消息
	 * @throws Exception    处理过过程中可能抛的异常
	 */
	public abstract void onMessageDo(Map<String, Object> m) throws Exception;
	
	/**
	 * 针对onMessageDo方法中抛出的异常进行处理,该方法尽量不要再抛异常
	 * @param e				onMessageDo方法中抛出的异常
	 */
	public abstract void handleExcetpiton(Exception e);	
	
	/**
	 * 监听到消息时的执行动作，该方法一般不用重写
	 * @param m				接收到的消息
	 */
	public final void onMessage(Message m) {
		try{
			onMessageDo(m);
		}catch(Exception e){
			try{
				handleExcetpiton(e);
			}catch(Exception e2){
				logger.error("handleExcetpiton方法抛异常", e2);
			}
		}
	}
	
	/**
	 * 监听到消息时的执行动作，该方法一般不用重写
	 * (提供给不需要jms调用的方式)
	 * @param m				接收到的消息
	 */
	public final void onMessage(Map<String, Object> m){
		try{
			onMessageDo(m);
		}catch(Exception e){
			try{
				handleExcetpiton(e);
			}catch(Exception e2){
				logger.error("handleExcetpiton方法抛异常", e2);
			}
		}
	}
}
