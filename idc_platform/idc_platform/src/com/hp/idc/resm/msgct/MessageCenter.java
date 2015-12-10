/*
 * @(#)MessageCenter.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.resm.msgct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.Message;

import org.apache.log4j.Logger;

import com.hp.idc.msgct.IDCMessageListener;
import com.hp.idc.msgct.IMessageCenter;
import com.hp.idc.msgct.MessageConstants;
import com.hp.idc.msgct.MessagePublishProperty;

/**
 * 实现消息的监听类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 9:13:13 AM Jul 26, 2011
 * 
 */

public class MessageCenter implements IMessageCenter {
	
	/**
	 * log4j日志
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 存储所有消息监听器的Map	
	 */
	private static Map<String,List<IDCMessageListener>> listenerContainerMap = new HashMap<String, List<IDCMessageListener>>();

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#addListener(java.lang.String, boolean, com.hp.idc.msgct.IDCMessageListener, java.lang.String)
	 */
	@Override
	public void addListener(String dest, boolean topicMode,
			IDCMessageListener listener, String consumerId) throws Exception {
		addListener(dest, topicMode, listener, consumerId, "");
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#addListener(java.lang.String, boolean, com.hp.idc.msgct.IDCMessageListener, java.lang.String, java.lang.String)
	 */
	@Override
	public void addListener(String dest, boolean topicMode,
			IDCMessageListener listener, String consumerId,
			String messageSelector) throws Exception {
		if (listener == null) {
			logger.warn("添加监听失败,传入对象为空");
			return;
		}
		String destKey = (topicMode?MessageConstants.TOPIC_MODE:MessageConstants.QUEUE_MODE) + "." + dest;
		synchronized (listenerContainerMap) {
			List<IDCMessageListener> ll = listenerContainerMap.get(destKey);
			if (ll == null) {
				ll = new ArrayList<IDCMessageListener>();
				listenerContainerMap.put(destKey, ll);
			}
			if (!existListener(listener, ll)) {
				ll.add(listener);
			} else {
				logger.warn("添加监听失败,ID已存在("+listener.getClass().getName()+")");
			}
		}
		if(topicMode)
			logger.info("成功新增对" + destKey + "的消息监听,消费者ID：" + consumerId);
		else
			logger.info("成功新增对" + destKey + "的消息监听");
	}

	private boolean existListener(IDCMessageListener listener, List<IDCMessageListener> ll){
		for (IDCMessageListener idcMessageListener : ll) {
			if (idcMessageListener.getClass().getName().equals(listener.getClass().getName())) {
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#existListener(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public boolean existListener(String dest, boolean topicMode,
			String consumerId) throws Exception {
		String destKey = (topicMode?MessageConstants.TOPIC_MODE:MessageConstants.QUEUE_MODE) + "." + dest;
		return listenerContainerMap.get(destKey) != null;
		
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#publishObjectMessage(java.lang.String, boolean, java.io.Serializable)
	 */
	@Override
	public void publishObjectMessage(String dest, boolean topicMode,
			Serializable messageBody) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#publishObjectMessage(java.lang.String, boolean, java.io.Serializable, com.hp.idc.msgct.MessagePublishProperty, java.util.Properties)
	 */
	@Override
	public void publishObjectMessage(String dest, boolean topicMode,
			Serializable messageBody, MessagePublishProperty mpp, Properties mp)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#publishTextMessage(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public void publishTextMessage(String dest, boolean topicMode,
			String messageBody) throws Exception {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#publishTextMessage(java.lang.String, boolean, java.lang.String, com.hp.idc.msgct.MessagePublishProperty, java.util.Properties)
	 */
	@Override
	public void publishTextMessage(String dest, boolean topicMode,
			String messageBody, MessagePublishProperty mpp, Properties mp)
			throws Exception {
		// TODO Auto-generated method stub
		String destKey = (topicMode?MessageConstants.TOPIC_MODE:MessageConstants.QUEUE_MODE) + "." + dest;
		List<IDCMessageListener> ll = listenerContainerMap.get(destKey);
		if (ll == null || ll.size() == 0) {
			logger.warn("无监听存在");
			return;
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("type", mp.getProperty("type"));
		m.put("id", messageBody);
		for(IDCMessageListener _l : ll){
			_l.onMessage(m);
		}
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#receiveMessage(java.lang.String, boolean, long)
	 */
	@Override
	public Message receiveMessage(String dest, boolean topicMode,
			long receiveTimeout) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#receiveSelectedMessage(java.lang.String, boolean, long, java.lang.String)
	 */
	@Override
	public Message receiveSelectedMessage(String dest, boolean topicMode,
			long receiveTimeout, String messageSelector) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.msgct.IMessageCenter#removeListener(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public void removeListener(String dest, boolean topicMode, String consumerId)
			throws Exception {
		// TODO Auto-generated method stub
		String destKey = (topicMode?MessageConstants.TOPIC_MODE:MessageConstants.QUEUE_MODE) + "." + dest;
		synchronized (listenerContainerMap) {
			listenerContainerMap.remove(destKey);
		}
	}

}
