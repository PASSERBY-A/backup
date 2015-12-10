package com.hp.idc.itsm.task;

/**
 * 表示任务中的信息
 * @author 梅园
 * 
 */
public class TaskMessageInfo {
	/**
	 * 信息时间
	 */
	protected String date;

	/**
	 * 信息内容
	 */
	protected String content;

	/**
	 * 操作人
	 */
	protected String operName;

	/**
	 * 获取信息时间
	 * @return 返回信息时间
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * 设置信息时间
	 * @param date 信息时间
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 获取信息内容
	 * @return 返回信息内容
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * 设置信息内容
	 * @param content 信息内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取操作人
	 * @return 返回操作人
	 */
	public String getOperName() {
		return this.operName;
	}

	/**
	 * 设置操作人
	 * @param operName 操作人
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}

}
