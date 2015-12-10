package com.hp.idc.itsm.common;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.itsm.message.ISmsSender;
import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.message.SmsMessage;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskEvent;
import com.hp.idc.itsm.task.TaskFactory;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskManager;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.task.TaskWaitingInfo;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;
import com.hp.idc.itsm.workflow.rule.AbstractRule;
import com.hp.idc.itsm.workflow.rule.OvertimeCheckRule;

/**
 * �������ʹ�õ�bean
 * 
 * @author ÷԰
 * 
 */
public class ItsmBean {
	
	private static Logger logger = Logger.getLogger(ItsmBean.class);

	/**
	 * �ڲ��߳�
	 */
	private Thread dog = null;

	/**
	 * ָʾ�߳��Ƿ�������
	 */
	private boolean isRunning = false;
	
	private TaskFactory taskFactory;
	
	/**
	 * ���ŷ��Ϳ���:0 ->�رն��ŷ���;1->�򿪶��ŷ���. Ĭ�Ϲر�
	 */
	private String isSendSms = "0";

	/**
	 * ����
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		if(this.isSendSms.equals("0"))
			return;
		this.isRunning = true;
		this.dog = new Thread() {
			public void run() {
				onStart();
			}
		};
		this.dog.setDaemon(true);
		this.dog.start();
	}

	/**
	 * ������ֹͣʱֹͣ�߳�
	 * 
	 */
	public void destroy() {
		this.isRunning = false;
		// ��1���ӣ����߳��Զ�ֹͣ
		for (int i = 0; i < 100 && this.dog != null; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		Thread t = this.dog;
		if (this.dog != null)
			t.interrupt();
		this.dog = null;
	}

	/**
	 * �洢���������ʱ��
	 */
	protected long lastSmsCheckTime = -1;
	
	/**
	 * �����Ų�����
	 * 
	 */
	protected void checkSms() {
		
		long t = System.currentTimeMillis();
		if (this.lastSmsCheckTime == -1)
			this.lastSmsCheckTime = t;
		if (t - this.lastSmsCheckTime < 180000) // 1����
			return;
		logger.debug("��ʼ������");
		ISmsSender smsSender = MessageManager.SmsSender;
		if (smsSender == null)
			return;
		try {
			List<SmsMessage> l = MessageManager.getSmsMessage(20, "root");
			for (int i = 0; i < l.size(); i++) {
				if (!this.isRunning)
					return;
				SmsMessage sms = (SmsMessage) l.get(i);
				String str = null;
				if (sms.getReceiver() == null
						|| sms.getReceiver().length() == 0)
					str = "û�н����˺�����Ϣ";
				else if (sms.getContent() == null
						|| sms.getContent().length() == 0)
					str = "û��������Ϣ";
				else
					str = smsSender.send(sms);
				if (str == null)
					MessageManager.updateMessage(sms.getOid(),
							MessageManager.TYPE_SMS, true, "OK", "root");
				else
					MessageManager.updateMessage(sms.getOid(),
							MessageManager.TYPE_SMS, false, str, "root");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.debug("�����Ž���(" + (System.currentTimeMillis() - t)+ "ms)");
		this.lastSmsCheckTime = System.currentTimeMillis();
	}

	/**
	 * �洢���������ʱ��
	 */
	protected long lastTaskCheckTime = -1;

	/**
	 * ��鹤��
	 * 
	 */
	protected void checkTask() {
		long t = System.currentTimeMillis();
		if (this.lastTaskCheckTime == -1)
			this.lastTaskCheckTime = t;
		if (t - this.lastTaskCheckTime < 60000) // 1����
			return;
		logger.debug("��ʼ��鹤����ʱ/��������");
		try {
			List<?> l = TaskManager.getAllOpenedTaskInfos(-1);
			for (int i = 0; i < l.size(); i++) {
				TaskInfo info = (TaskInfo) l.get(i);
				List<?> l1 = info.getTaskData();
				boolean update = false;
				for (int j = 0; j < l1.size(); j++) {
					TaskData task = (TaskData) l1.get(j);
					//Ԥ�رյķ�֧��ʼ�ڵ㲻����ʱ�ж�
					if (task.getStatus() == TaskData.STATUS_PRE_CLOSE)
						continue;
					update |= checkOverTime(task);
					if (task.getStatus() == TaskData.STATUS_WAITING){
						update |= checkWaitEnd(task);
					}
				}
				if (update) {
					ItsmUtil.debugPrint("���¹��� " + info.getOid());
					TaskEvent event = new TaskEvent(this,info,0,false, "system",TaskUpdateInfo.TYPE_NORMAL);
					taskFactory.publishEvent(event);
//					TaskManager.updateTaskInfo(info, "root");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.debug("��鹤����ʱ/�������ѽ���(" + (System.currentTimeMillis() - t)
				+ ")");
		this.lastTaskCheckTime = System.currentTimeMillis();
	}

	/**
	 * ���ָ���Ĺ����Ƿ�ʱ<br>
	 * ���ָ���Ĺ����Ƿ��ж�������
	 * 
	 * @param task
	 * @return ��Ҫ����ʱ����true, ���򷵻�false
	 */
	boolean checkOverTime(TaskData task) {
		boolean update = false;
		try {
			TaskInfo info = task.getOwner();
			WorkflowInfo workflowInfo = WorkflowManager.getWorkflowByOid(info
					.getWfOid());
			if (workflowInfo == null)
				return false;
			WorkflowData workflow = workflowInfo.getVersion(info.getWfVer());
			if (workflow == null)
				return false;
			NodeInfo node = workflow.getNode(task.getNodeId());
			if (node == null)
				return false;
			
			TaskUpdateInfo tuInfo = new TaskUpdateInfo();
			tuInfo.setTaskInfo(info);
			tuInfo.setTaskData(task);
			tuInfo.setWorkflow(workflow);
			tuInfo.setMap(new HashMap<String,String>());
			tuInfo.setOperName("system");
			AbstractRule overtimeCheck = node.getRule(OvertimeCheckRule.class.getName());
			if (overtimeCheck != null) {
				Boolean ret = (Boolean)overtimeCheck.execute(tuInfo, node);
				update = ret.booleanValue();
			}
			node.onRealtime(tuInfo);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return update;
	}
	
	/**
	 * ��鹤�����ȴ�״̬�������ı�־
	 * @param task
	 * @return
	 */
	boolean checkWaitEnd(TaskData task) {
		boolean update = false;
		if (task.getStatus() == TaskData.STATUS_WAITING){
			TaskWaitingInfo tai = task.getWaitInfo();
			if (tai.waiteEnd()) {
				update = true;
				task.setStatus(TaskData.STATUS_OPEN);
			}
		}
		return update;
	}

	/**
	 * �����߳���ں���
	 * 
	 */
	protected void onStart() {
//		Cache cache = new Cache();
//
//		try {
//			cache.init();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}

		while (this.isRunning) {
			try {
				for (int i = 0; i < 10 && this.isRunning; i++)
					Thread.sleep(100);
				if (!this.isRunning)
					break;
				checkSms();
				//�رչ�����ʱ��������
				//checkTask();
			} catch (InterruptedException e) {
				break;
			}
		}
		this.dog = null;
	}

	public void setTaskFactory(TaskFactory taskFactory) {
		this.taskFactory = taskFactory;
	}

	public TaskFactory getTaskFactory() {
		return taskFactory;
	}

	public void setIsSendSms(String isSendSms) {
		this.isSendSms = isSendSms;
	}

	public String getIsSendSms() {
		return isSendSms;
	}

}
