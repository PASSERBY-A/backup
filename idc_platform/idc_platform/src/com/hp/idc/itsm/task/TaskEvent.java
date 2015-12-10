package com.hp.idc.itsm.task;


public class TaskEvent extends Event{

	private static final long serialVersionUID = 3681516202265502384L;

	
	//本次提交，从哪个数据节点开始的,
	//比如当前处在节点A，提交表单到B，则startData为节点A所在的taskData数据节点的Id
	protected int startDataId;
	
	//是否是新建工单
	protected boolean newTask;


	//操作类型 参见TaskUpdateInfo
	protected int operType;
	
	public TaskEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * @param source
	 * @param ti 工单对象
	 * @param startDataId 本次提交，从哪个数据节点开始的
	 * @param newTask 是否是新建工单
	 * @param operUser 操作人
	 * @param operType 操作类型 参见TaskUpdateInfo
	 */
	public TaskEvent(Object source,TaskInfo ti,int startDataId,boolean newTask,String operUser,int operType) {
		super(source);
		this.taskInfo = ti;
		this.startDataId = startDataId;
		this.newTask = newTask;
		this.operUser = operUser;
		this.operType = operType;
	}

	public int getStartDataId() {
		return startDataId;
	}

	public void setStartDataId(int startDataId) {
		this.startDataId = startDataId;
	}

	public boolean isNewTask() {
		return newTask;
	}

	public void setNewTask(boolean newTask) {
		this.newTask = newTask;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

}
