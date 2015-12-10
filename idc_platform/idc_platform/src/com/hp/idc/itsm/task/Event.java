/**
 * 
 */
package com.hp.idc.itsm.task;

import java.util.EventObject;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class Event extends EventObject{

	protected TaskInfo taskInfo;

	
	//²Ù×÷ÈË
	protected String operUser;

	/**
	 * @param source
	 */
	public Event(Object source) {
		super(source);
	}
	
	public Event(Object source,TaskInfo ti,String operUser) {
		super(source);
		this.taskInfo = ti;
		this.operUser = operUser;
	}
	
	
	
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}
	
	
	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}


}
