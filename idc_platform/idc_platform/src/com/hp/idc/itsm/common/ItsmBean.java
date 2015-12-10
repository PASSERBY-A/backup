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
 * 服务管理使用的bean
 * 
 * @author 梅园
 * 
 */
public class ItsmBean {
	
	private static Logger logger = Logger.getLogger(ItsmBean.class);

	/**
	 * 内部线程
	 */
	private Thread dog = null;

	/**
	 * 指示线程是否在运行
	 */
	private boolean isRunning = false;
	
	private TaskFactory taskFactory;
	
	/**
	 * 短信发送开关:0 ->关闭短信发送;1->打开短信发送. 默认关闭
	 */
	private String isSendSms = "0";

	/**
	 * 加载
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
	 * 服务器停止时停止线程
	 * 
	 */
	public void destroy() {
		this.isRunning = false;
		// 待1秒钟，让线程自动停止
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
	 * 存储任务最后检查时间
	 */
	protected long lastSmsCheckTime = -1;
	
	/**
	 * 检查短信并发送
	 * 
	 */
	protected void checkSms() {
		
		long t = System.currentTimeMillis();
		if (this.lastSmsCheckTime == -1)
			this.lastSmsCheckTime = t;
		if (t - this.lastSmsCheckTime < 180000) // 1分钟
			return;
		logger.debug("开始检查短信");
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
					str = "没有接收人号码信息";
				else if (sms.getContent() == null
						|| sms.getContent().length() == 0)
					str = "没有内容信息";
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
		logger.debug("检查短信结束(" + (System.currentTimeMillis() - t)+ "ms)");
		this.lastSmsCheckTime = System.currentTimeMillis();
	}

	/**
	 * 存储任务最后检查时间
	 */
	protected long lastTaskCheckTime = -1;

	/**
	 * 检查工单
	 * 
	 */
	protected void checkTask() {
		long t = System.currentTimeMillis();
		if (this.lastTaskCheckTime == -1)
			this.lastTaskCheckTime = t;
		if (t - this.lastTaskCheckTime < 60000) // 1分钟
			return;
		logger.debug("开始检查工单超时/短信提醒");
		try {
			List<?> l = TaskManager.getAllOpenedTaskInfos(-1);
			for (int i = 0; i < l.size(); i++) {
				TaskInfo info = (TaskInfo) l.get(i);
				List<?> l1 = info.getTaskData();
				boolean update = false;
				for (int j = 0; j < l1.size(); j++) {
					TaskData task = (TaskData) l1.get(j);
					//预关闭的分支开始节点不做超时判断
					if (task.getStatus() == TaskData.STATUS_PRE_CLOSE)
						continue;
					update |= checkOverTime(task);
					if (task.getStatus() == TaskData.STATUS_WAITING){
						update |= checkWaitEnd(task);
					}
				}
				if (update) {
					ItsmUtil.debugPrint("更新工单 " + info.getOid());
					TaskEvent event = new TaskEvent(this,info,0,false, "system",TaskUpdateInfo.TYPE_NORMAL);
					taskFactory.publishEvent(event);
//					TaskManager.updateTaskInfo(info, "root");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.debug("检查工单超时/短信提醒结束(" + (System.currentTimeMillis() - t)
				+ ")");
		this.lastTaskCheckTime = System.currentTimeMillis();
	}

	/**
	 * 检查指定的工单是否超时<br>
	 * 检查指定的工单是否有短信提醒
	 * 
	 * @param task
	 * @return 需要更新时返回true, 否则返回false
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
	 * 检查工单【等待状态】结束的标志
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
	 * 运行线程入口函数
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
				//关闭工单超时短信提醒
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
