package com.hp.idc.itsm.task.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.idc.itsm.task.Event;
import com.hp.idc.itsm.task.TaskFactory;

/**
 * 工单数据输出接口类
 * @author FluteD
 *
 */
public abstract class TaskListener {
	
	/**
	 * 默认的监听器分类
	 */
	public final static String CATEGORY_DEFAULT = "ITSM";

	/**
	 * 监听器的ID
	 */
    private String id = getClass().getName();
    
    
    /**
     * 是否抛出异常，如果是，则主程序不会因为此监听发生异常而终止下面的处理
     */
    private boolean throwException = false;
    
	protected static TaskFactory taskFactory;
	
	protected Log logger = LogFactory.getLog(getClass());

	public void init() {
		taskFactory.addListener(this);
	}
	
	/**
	 * 工单输出接口函数
	 */
	public abstract void handleEvent(Event event) throws Exception;

	/**
	 * 验证使用本接口处理输出
	 * @return
	 */
	public abstract boolean accept(Event event);
	
	public String getId() {
		return id;
	}
	

	public TaskFactory getTaskFactory() {
		return taskFactory;
	}

	public void setTaskFactory(TaskFactory taskFactory) {
		this.taskFactory = taskFactory;
	}

	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}

	public boolean isThrowException() {
		return throwException;
	}
}