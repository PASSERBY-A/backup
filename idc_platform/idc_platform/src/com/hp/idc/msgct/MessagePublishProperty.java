/*
 * @(#)MessagePublishProperty.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.msgct;

import javax.jms.Message;

/**
 * ��Ϣ����ʱ�Ĳ�������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 1:38:30 PM 2011
 * 
 */

public class MessagePublishProperty {
	
	/**
	 * ��Ϣ����Чʱ�䣨���룩��Ĭ����������
	 */
	private long timeToLive = Message.DEFAULT_TIME_TO_LIVE;
	
	/**
	 * ��Ϣ�Ƿ�־ã�Ĭ�ϳ־�
	 */
	private boolean deliveryPersistent = true;
	
	/**
	 * �Ƿ�����첽���ͣ�Ĭ���첽
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
