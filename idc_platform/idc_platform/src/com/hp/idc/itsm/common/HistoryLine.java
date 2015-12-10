package com.hp.idc.itsm.common;

/**
 * 表示历史记录行
 * @author 梅园
 *
 */
public class HistoryLine {
	/**
	 * 操作时间
	 */
	protected String date;
	
	/**
	 * 历史值
	 */
	protected String oldValue;
	
	/**
	 * 更新值
	 */
	protected String newValue;
	
	/**
	 * 字段id
	 */
	protected String id;
	
	/**
	 * 操作人
	 */
	protected String operName;

	/**
	 * 获取操作时间
	 * @return 返回操作时间
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * 设置操作时间
	 * @param date 操作时间
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 获取字段id
	 * @return 返回字段id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置字段id
	 * @param id 字段id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取更新值
	 * @return 返回更新值
	 */
	public String getNewValue() {
		return this.newValue;
	}

	/**
	 * 设置更新值
	 * @param newValue 更新值
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/**
	 * 获取历史值
	 * @return 返回历史值
	 */
	public String getOldValue() {
		return this.oldValue;
	}

	/**
	 * 设置历史值
	 * @param oldValue 历史值
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
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
