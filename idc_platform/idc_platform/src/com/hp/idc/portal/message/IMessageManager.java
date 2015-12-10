package com.hp.idc.portal.message;

import java.util.List;


/**
 * 提供外部插入消息提醒接口
 * @author chengqp
 */

public interface IMessageManager {
	/**
	 * 各模块添加消息提醒的接口方法
	 * @param log
	 * @return
	 */
	public abstract int addMessage(Message message);
	
	/**
	 * 各模块批量添加消息提醒的接口方法
	 * @param messages
	 * @return
	 */
	public abstract int addMessageList(List<Message> messages);
}
