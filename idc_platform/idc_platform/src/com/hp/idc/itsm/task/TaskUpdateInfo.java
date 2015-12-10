package com.hp.idc.itsm.task;

import java.util.Map;

import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;

/**
 * ��ʾ������ת��Ϣ
 * @author ÷԰
 *
 */
public class TaskUpdateInfo {
	/**
	 * ��ת��ʽ:����
	 */
	public static int TYPE_NORMAL = 0;

	/**
	 * ��ת��ʽ:����
	 */
	public static int TYPE_ROLLBACK = 1;

	/**
	 * ��ת��ʽ:������Ϣ
	 */
	public static int TYPE_MESSAGE = 2;

	/**
	 * ��ת��ʽ:����
	 */
	public static int TYPE_ACCEPT = 3;
	
	/**
	 * ��ת��ʽ:��������ͣ��������ݣ������̲���ת
	 */
	public static int TYPE_SAVE_FOR_EDIT = 4;
	
	public static int TYPE_FORCE_CLOSE = 5;//ǿ�ƹر�
	
	public static int TYPE_OVERTIME = 6;//��ʱ
	
	public static int TYPE_UPDATE_FIELD = 7;//ֱ�Ӹ���ĳ���ֶ�
	
	public static int TYPE_READ = 8;//��֪
	
	public static int TYPE_DYNAMIC_BRENCH = 9;
	
	/**
	 * �������¹������Ƕ��ڵ��������Ķ���ݸ��֧�������ύ
	 * ʹ�û�������֧��ʼ�ڵ㴦��תʱ���ɰ�ÿһ����֧��������Ϊ�ݸ壬���һ���ύ
	 */
	public static int TYPE_UPDATE_BATCH = 8;
	

	/**
	 * �洢��ת��ʽ
	 */
	protected int type = TYPE_NORMAL;
	
	/**
	 * �洢����������
	 */
	protected TaskInfo taskInfo;
	
	/**
	 * �洢�������ڵ����
	 */
	protected TaskData taskData;
	
	/**
	 * �洢������̶���
	 */
	protected WorkflowData workflow;
	
	/**
	 * �洢�ύ�����ݱ�
	 */
	protected Map<String,String> map;
	
	/**
	 * �洢�ύ��Ŀ��ڵ�
	 */
	protected NodeInfo toNode;
	
	/**
	 * �洢��ִ�еĶ���ID
	 */
	protected String action;
	
	/**
	 * �洢�ύ���û�
	 */
	protected String[] users;
	
	/**
	 * �洢������
	 */
	protected String operName;
	
	protected String toNodePath;

	public String getToNodePath() {
		return toNodePath;
	}

	public void setToNodePath(String toNodePath) {
		this.toNodePath = toNodePath;
	}

	/**
	 * ��ȡ�ύ�����ݱ�
	 * @return �����ύ�����ݱ�
	 */
	public Map<String,String> getMap() {
		return this.map;
	}
	
	/**
	 * �����ύ�����ݱ�
	 * @param map �ύ�����ݱ�
	 */
	public void setMap(Map<String,String> map) {
		this.map = map;
	}
	
	/**
	 * ��ȡ������ 
	 * @return ���ز�����
	 */
	public String getOperName() {
		return this.operName;
	}
	
	/**
	 * ���ò����� 
	 * @param operName ������ 
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}
	
	/**
	 * ��ȡ�������ڵ����
	 * @return �����������ڵ����
	 */
	public TaskData getTaskData() {
		return this.taskData;
	}
	
	/**
	 * �����������ڵ����
	 * @param taskData �������ڵ����
	 */
	public void setTaskData(TaskData taskData) {
		this.taskData = taskData;
	}
	
	/**
	 * ��ȡ���������� 
	 * @return �������������� 
	 */
	public TaskInfo getTaskInfo() {
		return this.taskInfo;
	}
	
	/**
	 * �������������� 
	 * @param taskInfo ���������� 
	 */
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	/**
	 * ��ȡ�ύ��Ŀ��ڵ�
	 * @return �����ύ��Ŀ��ڵ�
	 */
	public NodeInfo getToNode() {
		return this.toNode;
	}
	
	/**
	 * �����ύ��Ŀ��ڵ�
	 * @param toNode �ύ��Ŀ��ڵ�
	 */
	public void setToNode(NodeInfo toNode) {
		this.toNode = toNode;
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * ��ȡ�ύ���û�
	 * @return �����ύ���û�
	 */
	public String[] getUsers() {
		return this.users;
	}
	
	/**
	 * �����ύ���û�
	 * @param users �ύ���û�
	 */
	public void setUsers(String[] users) {
		this.users = users;
	}
	
	/**
	 * ��ȡ������̶���
	 * @return ����������̶���
	 */
	public WorkflowData getWorkflow() {
		return this.workflow;
	}
	
	/**
	 * ����������̶���
	 * @param workflow ������̶���
	 */
	public void setWorkflow(WorkflowData workflow) {
		this.workflow = workflow;
	}
	
	/**
	 * ��ȡ��ת��ʽ
	 * @see #TYPE_NORMAL
	 * @see #TYPE_ROLLBACK
	 * @return ������ת��ʽ
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * ������ת��ʽ
	 * @see #TYPE_NORMAL
	 * @see #TYPE_ROLLBACK
	 * @param type ��ת��ʽ 
	 */
	public void setType(int type) {
		this.type = type;
	}
}
