package com.hp.idc.itsm.task.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.idc.itsm.task.Event;
import com.hp.idc.itsm.task.TaskFactory;

/**
 * ������������ӿ���
 * @author FluteD
 *
 */
public abstract class TaskListener {
	
	/**
	 * Ĭ�ϵļ���������
	 */
	public final static String CATEGORY_DEFAULT = "ITSM";

	/**
	 * ��������ID
	 */
    private String id = getClass().getName();
    
    
    /**
     * �Ƿ��׳��쳣������ǣ��������򲻻���Ϊ�˼��������쳣����ֹ����Ĵ���
     */
    private boolean throwException = false;
    
	protected static TaskFactory taskFactory;
	
	protected Log logger = LogFactory.getLog(getClass());

	public void init() {
		taskFactory.addListener(this);
	}
	
	/**
	 * ��������ӿں���
	 */
	public abstract void handleEvent(Event event) throws Exception;

	/**
	 * ��֤ʹ�ñ��ӿڴ������
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