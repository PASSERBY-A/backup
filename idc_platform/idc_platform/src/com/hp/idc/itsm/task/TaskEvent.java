package com.hp.idc.itsm.task;


public class TaskEvent extends Event{

	private static final long serialVersionUID = 3681516202265502384L;

	
	//�����ύ�����ĸ����ݽڵ㿪ʼ��,
	//���統ǰ���ڽڵ�A���ύ����B����startDataΪ�ڵ�A���ڵ�taskData���ݽڵ��Id
	protected int startDataId;
	
	//�Ƿ����½�����
	protected boolean newTask;


	//�������� �μ�TaskUpdateInfo
	protected int operType;
	
	public TaskEvent(Object source) {
		super(source);
	}
	
	/**
	 * 
	 * @param source
	 * @param ti ��������
	 * @param startDataId �����ύ�����ĸ����ݽڵ㿪ʼ��
	 * @param newTask �Ƿ����½�����
	 * @param operUser ������
	 * @param operType �������� �μ�TaskUpdateInfo
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
