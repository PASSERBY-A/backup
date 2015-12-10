package com.hp.idc.itsm.task;

/**
 * 表示任务回退信息<br>
 * 注:因为一个回退动作可能会导致多个节点的回退,所以会有dataId和actureDataId两个记录
 * @author 梅园
 * 
 */
public class TaskRollbackInfo {

	/**
	 * 存储从哪一个节点回退的(操作节点)
	 */
	protected int actureDataId;

	/**
	 * 存储从哪一个节点回退的
	 */
	protected int dataId;

	/**
	 * 回退时间
	 */
	protected String date;

	/**
	 * 回退原因
	 */
	protected String reason;

	/**
	 * 获取从哪一个节点回退的
	 * @return 返回从哪一个节点回退的
	 */
	public int getDataId() {
		return this.dataId;
	}

	/**
	 * 设置从哪一个节点回退的
	 * @param dataId 从哪一个节点回退的
	 */
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	/**
	 * 获取回退时间
	 * @return 返回回退时间
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * 设置回退时间
	 * @param date 回退时间
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 获取回退原因
	 * @return 返回回退原因
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * 设置回退原因
	 * @param reason 回退原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 获取从哪一个节点回退的(操作节点)
	 * @return 返回从哪一个节点回退的(操作节点)
	 */
	public int getActureDataId() {
		return this.actureDataId;
	}

	/**
	 * 设置从哪一个节点回退的(操作节点)
	 * @param actureDataId 从哪一个节点回退的(操作节点)
	 */
	public void setActureDataId(int actureDataId) {
		this.actureDataId = actureDataId;
	}
}
