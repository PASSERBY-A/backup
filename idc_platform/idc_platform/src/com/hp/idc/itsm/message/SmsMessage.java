package com.hp.idc.itsm.message;

import java.util.Date;

/**
 * 表示短信内容
 * @author 梅园
 *
 */
public class SmsMessage {
	/**
	 * 存储短信oid
	 */
	protected int oid;
	
	/**
	 * 存储发送人手机号
	 */
	protected String sender = "";
	
	/**
	 * 存储接收人手机号
	 */
	protected String receiver;
	
	/**
	 * 存储短信内容
	 */
	protected String content;
	
	/**
	 * 存活期限,毫秒.从当前时间算起.默认为24小时!
	 */
	protected long liveTime = 24 * 60 * 60 * 1000;
	
	/**
	 * 定时发送的时间
	 */
	protected Date atTime = null;

	/**
	 * 获取短信内容
	 * @return 返回短信内容
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * 设置储短信内容
	 * @param content 储短信内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取oid
	 * @return 返回oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * 设置oid
	 * @param oid oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * 获取接收人手机号
	 * @return 返回接收人手机号
	 */
	public String getReceiver() {
		return this.receiver;
	}

	/**
	 * 设置接收人手机号
	 * @param receiver 接收人手机号
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * 获取发送人手机号
	 * @return 返回发送人手机号
	 */
	public String getSender() {
		return this.sender;
	}

	/**
	 * 设置发送人手机号
	 * @param sender 发送人手机号
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public long getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(long liveTime) {
		this.liveTime = liveTime;
	}

	public Date getAtTime() {
		return atTime;
	}

	public void setAtTime(Date atTime) {
		this.atTime = atTime;
	}

	/**
	 * 重载toString()方法
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getReceiver() + ":" + this.getContent();
	}
}
