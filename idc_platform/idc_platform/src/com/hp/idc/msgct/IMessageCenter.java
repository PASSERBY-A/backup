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
 * 消息中心接口类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:34:38 PM 2011
 * 
 */

public interface IMessageCenter {

	/**
	 * 增加对一类消息的异步监听
	 * @param dest      		消息目标
	 * @param topicMode  		消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param listener  		监听器
	 * @param consumerId        消息接收者Id，主题模式时必须设置，队列模式时可以为null<br>
	 * PS:	consumerId不能包含点号(.),否则消息中心会解析错误
	 */
	public void addListener(String dest, boolean topicMode, IDCMessageListener listener, String consumerId) throws Exception;
	
	/**
	 * 增加对一类消息的异步监听，只监听满足条件的消息
	 * @param dest      		消息目标
	 * @param topicMode  		消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param listener  		监听器
	 * @param consumerId        消息接收者Id，主题模式时必须设置，队列模式时可以为null<br>
	 * PS:	consumerId不能包含点号(.),否则消息中心会解析错误
	 * @param messageSelector	消息过滤器，根据Message Headers和Message Properties对消息进行过滤，表达式定义采用SQL92的子集	
	 */
	public void addListener(String dest, boolean topicMode, IDCMessageListener listener, String consumerId, String messageSelector) throws Exception;
	
	/**
	 * 判断该类消息是否已经存在监听器，该方法只是判断本应用中是否已经存在监听，无法获取到其他应用中的监听
	 * @param dest				消息目标
	 * @param topicMode  		消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param consumerId        消息接收者Id，主题模式时必须设置，队列模式时可以为null
	 * @return 					是否存在
	 */
	public boolean existListener(String dest, boolean topicMode, String consumerId) throws Exception;
	
	/**
	 * 删除消息目标对应的监听器 ，该方法也只是删除本应用中的监听
	 * @param dest 				消息目标
	 * @param topicMode  		消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param consumerId        消息接收者Id，主题模式时必须设置，队列模式时可以为null
	 */
	public void removeListener(String dest, boolean topicMode, String consumerId) throws Exception;
	
	/**
	 * 同步接收某消息目标上的消息<br>
	 * PS:不建议用此方法同步接收主题模式的消息，因为非持久订阅的话，无法获取消费者不在线时的消息；<br>
	 * 如果一定要用，对一个主题第一次接收的时候返回为空，后面再接收才能返回消息
	 * @param dest				消息目标
	 * @param topicMode			消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param receiveTimeout    接收超时时长（毫秒）
	 * @return
	 * @throws Exception
	 */
	public Message receiveMessage(String dest, boolean topicMode, long receiveTimeout) throws Exception;
	
	/**
	 * 同步接收某消息目标上满足条件的消息<br>
	 * PS:不建议用此方法同步接收主题模式的消息，因为非持久订阅的话，无法获取消费者不在线时的消息；<br>
	 * 如果一定要用，对一个主题第一次接收的时候返回为空，后面再接收才能返回消息
	 * @param dest				消息目标
	 * @param topicMode			消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param receiveTimeout    接收超时时长（毫秒）
	 * @param messageSelector	消息过滤器，根据Message Headers和Message Properties对消息进行过滤，表达式定义采用SQL92的子集
	 * @return
	 * @throws Exception
	 */
	public Message receiveSelectedMessage(String dest, boolean topicMode, long receiveTimeout, String messageSelector) throws Exception;
	
	/**
	 * 根据默认设置，发送文本消息
	 * @param dest				消息目标
	 * @param topicMode			消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param messageBody		消息内容
	 * @throws Exception		发送出错时的异常
	 */
	public void publishTextMessage(String dest, boolean topicMode, String messageBody) throws Exception;
	
	/**
	 * 根据自定义设置，发送文本消息
	 * @param dest				消息目标
	 * @param topicMode			消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param messageBody		消息内容
	 * @param mpp				发送选项	
	 * @param mp				消息的自定义属性，可用于消息过滤
	 * @throws Exception		发送出错时的异常
	 */
	public void publishTextMessage(String dest, boolean topicMode, String messageBody, MessagePublishProperty mpp, Properties mp) throws Exception;	
	
	/**
	 * 根据默认设置，发送可序列化对象消息
	 * @param dest				消息目标
	 * @param topicMode			消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param messageBody		消息内容
	 * @throws Exception		发送出错时的异常
	 */
	public void publishObjectMessage(String dest, boolean topicMode, Serializable messageBody) throws Exception;
	
	/**
	 * 根据自定义设置，发送可序列化对象消息
	 * @param dest				消息目标
	 * @param topicMode			消息目标是否主题模式，true:主题模式；false:队列模式
	 * @param messageBody		消息内容
	 * @param mpp				发送选项	
	 * @param mp				消息的自定义属性，可用于消息过滤
	 * @throws Exception		发送出错时的异常
	 */
	public void publishObjectMessage(String dest, boolean topicMode, Serializable messageBody, MessagePublishProperty mpp, Properties mp) throws Exception;
}
