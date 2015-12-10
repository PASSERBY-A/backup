/*
 * @(#)MessagePublishProperty.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.msgct;

import javax.jms.Message;

/**
 * 消息发送时的参数定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:38:30 PM 2011
 * 
 */

public class MessagePublishProperty {
	
	/**
	 * 消息的有效时间（毫秒），默认永不过期
	 */
	private long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
	
	/**
	 * 消息是否持久，默认持久
	 */
	private boolean deliveryPersistent = true;
	
	/**
	 * 是否采用异步发送，默认异步
	 */
	private boolean useAsyncSend = true;

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public boolean isDeliveryPersistent() {
		return deliveryPersistent;
	}

	public void setDeliveryPersistent(boolean deliveryPersistent) {
		this.deliveryPersistent = deliveryPersistent;
	}

	public boolean isUseAsyncSend() {
		return useAsyncSend;
	}

	public void setUseAsyncSend(boolean useAsyncSend) {
		this.useAsyncSend = useAsyncSend;
	}	
	

}
