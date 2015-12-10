/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.SMSTemplate;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.security.FactorInfo;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class SMSRule extends AbstractRule {

	// 工单到达时给处理人短信
	public static final String SMS_TYPE_ARRIVE = "arrive";

	// 工单到达时通知相关人短信
	public static final String SMS_TYPE_REFER = "refer";

	// 工单被代处理时给远处理人短信
	public static final String SMS_TYPE_DEALED = "dealed";

	// 工单被回退时短信
	public static final String SMS_TYPE_ROOLBACK = "rollback";

	// 工单超时时短信
	public static final String SMS_TYPE_OVERTIME = "overtime";

	// 工单循环提醒短信
	public static final String SMS_TYPE_REMIND_AUTO = "remindAuto";

	/**
	 * 
	 * 自定义短信通知内容
	 * 
	 * <pre>
	 * key=SMS_TYPE_*
	 * value=String[noteTo,noteMessageTemplate]
	 * </pre>
	 */
	private Map<String, String[]> noticeList = new HashMap<String, String[]>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.itsm.workflow.rule.AbstractRule#parse(org.dom4j.Element
	 * )
	 */
	public void parse(JSONObject json) throws JSONException {
		super.parse(json);
		
		
		//TODO 

	}

	/*
	 * params[0]:短信到达类型，取SMSRule.SMS_TYPE_*之一
	 * 
	 * @see
	 * com.hp.idc.itsm.workflow.rule.IWorkflowRule#execute(com.linkage
	 * .toptea.itsm.task.TaskUpdateInfo,
	 * com.hp.idc.itsm.workflow.NodeInfo, java.lang.Object[])
	 */
	public Object execute(TaskUpdateInfo updateInfo, NodeInfo node, Object... params) throws Exception {
		if (params.length == 0) {
			logger.error("未知的短信发送内容");
			return null;
		}

		if (params[0].equals(SMS_TYPE_ARRIVE))
			doArrive(updateInfo.getTaskData(), updateInfo.getOperName(),updateInfo.getWorkflow());
		else if (params[0].equals(SMS_TYPE_DEALED))
			doDealed(updateInfo.getTaskData(), updateInfo.getOperName(),updateInfo.getWorkflow());
		else if (params[0].equals(SMS_TYPE_ROOLBACK))
			doRollback(updateInfo.getTaskData(), updateInfo.getOperName(),updateInfo.getWorkflow());
		else if (params[0].equals(SMS_TYPE_OVERTIME)) {
			doOvertime(updateInfo.getTaskData(),updateInfo.getWorkflow());
		} else if (params[0].equals(SMS_TYPE_REMIND_AUTO)) {
			doRemindAuto(updateInfo.getTaskData(),updateInfo.getWorkflow());
		}
		return null;
	}

	/**
	 * @param taskData
	 */
	private void doRemindAuto(TaskData task, WorkflowData wfData) {
		String n = DateTimeUtil
				.toChinese((System.currentTimeMillis() - task.getCreateTime().getTime()) / 60000 * 60);
		String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_REMIND_AUTO, task, wfData);
		String smsReceiver = smsTemplate[0];
		String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { n,
				task.getOwner().getOid() + "", task.getTitle(), task.getNodeDesc() },task);

		sendSMS(smsReceiver, smsContent);
	}

	/**
	 * @param taskData
	 * @param operName
	 */
	private void doRollback(TaskData taskData, String operName, WorkflowData wfData) {
		String operName_zh = operName;
		PersonInfoInterface pi = PersonManager.getPersonById(operName);
		if (pi != null)
			operName_zh = pi.getName();
		//回退时短信的接受人要取上一步节点的处理人
		String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_ROOLBACK, taskData.getParent(),wfData);
		String smsReceiver = smsTemplate[0];
		String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { operName_zh,
				taskData.getOwner().getOid() + "", taskData.getTitle(), taskData.getNodeDesc() },taskData);

		sendSMS(smsReceiver, smsContent);

	}

	/**
	 * 工单被代处理时
	 * 
	 * @param fromData
	 *            上一个节点
	 */
	private void doDealed(TaskData fromData, String operName, WorkflowData wfData) {
		String operName_zh = operName;
		PersonInfo pi = (PersonInfo) PersonManager.getPersonById(operName);
		if (pi != null)
			operName_zh = pi.getName();
		String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_DEALED, fromData, wfData);
		String smsReceiver = smsTemplate[0];
		String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { operName_zh,
				fromData.getOwner().getOid() + "", fromData.getTitle(), fromData.getNodeDesc() },fromData);

		sendSMS(smsReceiver, smsContent);
	}

	/**
	 * 处理工单超后的首次短信
	 */
	private void doOvertime(TaskData task, WorkflowData wfData) {
		String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_OVERTIME, task, wfData);
		String smsReceiver = smsTemplate[0];
		String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { "",
				task.getOwner().getOid() + "", task.getTitle(), task.getNodeDesc() },task);

		sendSMS(smsReceiver, smsContent);
	}

	/**
	 * 处理工单到达时的短信
	 */
	private void doArrive(TaskData task, String operName, WorkflowData wfData) {
		String operName_zh = operName;
		PersonInfo pi = PersonManager.getPersonById(operName);
		if (pi != null)
			operName_zh = pi.getName();
		if (!task.getAssignTo().equals(operName)) {

			String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_ARRIVE, task, wfData);
			String smsReceiver = smsTemplate[0];
			String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { operName_zh,
					task.getOwner().getOid() + "", task.getTitle(), task.getNodeDesc() },task);

			sendSMS(smsReceiver, smsContent);
			
			//给代理人发短信
			FactorInfo fi = Cache.Factors.get(operName);
			if (fi!=null && fi.isSendSMS()) {
				String factor = fi.getFactors();
				if (factor!=null && !factor.equals(""))
					sendSMS(factor, smsContent);
			}
			
		}
		//给阅知人发送短信
		String readUser = task.getReadUser();
		if (readUser!=null && !readUser.equals("")){
			String smsContent = "有工单需要您批阅（ID:"+task.getOwner().getOid()+",TITLE:"+task.getTitle()+"）";
			sendSMS(readUser, smsContent);
		}
		
		// 发送已代处理短信
		TaskData fromData = task.getParent();
		if (fromData != null && !fromData.getAssignTo().equals(operName)) {
			String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_DEALED, task, wfData);
			String smsReceiver = smsTemplate[0];

			String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { operName_zh,
					fromData.getOwner().getOid() + "", fromData.getTitle(), fromData.getNodeDesc() },task);
			sendSMS(smsReceiver, smsContent);
		}

	}

	private String[] getSmsTemplate(String type, TaskData td, WorkflowData wfData) {
		String[] ret = noticeList.get(type);
		SMSTemplate smsTemplate = wfData.getOwner().getSmsTemplate();
		
		if (ret == null) {
			ret = new String[2];
			if (type == SMS_TYPE_ARRIVE) {
				if(smsTemplate == null || smsTemplate.getSmsNew()==null || smsTemplate.getSmsNew().equals(""))
					ret[1] = Consts.SMS_TEMPLATE.getSmsNew();
				else
					ret[1] = smsTemplate.getSmsNew();
			} else if (type == SMS_TYPE_ROOLBACK) {
				if(smsTemplate == null || smsTemplate.getSmsRollback()==null || smsTemplate.getSmsRollback().equals(""))
					ret[1] = Consts.SMS_TEMPLATE.getSmsRollback();
				else
					ret[1] = smsTemplate.getSmsRollback();
			} else if (type == SMS_TYPE_DEALED) {
				if(smsTemplate == null || smsTemplate.getSmsDealed()==null || smsTemplate.getSmsDealed().equals(""))
					ret[1] = Consts.SMS_TEMPLATE.getSmsDealed();
				else
					ret[1] = smsTemplate.getSmsDealed();
			} else if (type == SMS_TYPE_OVERTIME) {
				if(smsTemplate == null || smsTemplate.getSmsOverdue()==null || smsTemplate.getSmsOverdue().equals(""))
					ret[1] = Consts.SMS_TEMPLATE.getSmsOverdue();
				else
					ret[1] = smsTemplate.getSmsOverdue();
			} else if (type == SMS_TYPE_REMIND_AUTO) {
				if(smsTemplate == null || smsTemplate.getSmsOvertime()==null || smsTemplate.getSmsOvertime().equals(""))
					ret[1] = Consts.SMS_TEMPLATE.getSmsOvertime();
				else
					ret[1] = smsTemplate.getSmsOvertime();
			}
		}
		String smsReceiver = MessageManager.getInjectedReceiver(ret[0], td);
		//判断是否包含工单处理人
		if (smsReceiver.equals(td.getAssignTo()) || smsReceiver.startsWith(td.getAssignTo() + ",")
				|| smsReceiver.endsWith("," + td.getAssignTo())
				|| smsReceiver.indexOf("," + td.getAssignTo() + ",") != -1)
			;
		
		//*****add by zhonganjing for assign to the workgroup or orgnaziatio ***/
		else if(td.getAssignType() == TaskData.ASSIGN_ORGANIZATION){
			List<PersonInfo> l = PersonManager.getPersonsByOrganizationId("ITSM", td.getAssignTo(), false);
			for (PersonInfo p : l) {
				smsReceiver += "," + p.getId();
			}
		} else if(td.getAssignType() == TaskData.ASSIGN_WORKGROUP) {
			List<PersonInfo> l = PersonManager.getPersonsByWorkgoupId("ITSM", td.getAssignTo(), false);
			for (PersonInfo p : l) {
				smsReceiver += "," + p.getId();
			}
		} 
		//**********end****/
		else {
			smsReceiver += "," + td.getAssignTo();
		}
		ret[0] = smsReceiver;
		return ret;
	}

	private void sendSMS(String smsReceiver, String smsContent) {
		try {
			String[] receivers = smsReceiver.split(",");
			for (String receiver : receivers) {
				if (receiver == null || receiver.equals(""))
					continue;
				MessageManager.sendSms(smsContent, receiver, "system", new Date(), "system");
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

}
