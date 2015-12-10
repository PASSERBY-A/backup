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

	// ��������ʱ�������˶���
	public static final String SMS_TYPE_ARRIVE = "arrive";

	// ��������ʱ֪ͨ����˶���
	public static final String SMS_TYPE_REFER = "refer";

	// ������������ʱ��Զ�����˶���
	public static final String SMS_TYPE_DEALED = "dealed";

	// ����������ʱ����
	public static final String SMS_TYPE_ROOLBACK = "rollback";

	// ������ʱʱ����
	public static final String SMS_TYPE_OVERTIME = "overtime";

	// ����ѭ�����Ѷ���
	public static final String SMS_TYPE_REMIND_AUTO = "remindAuto";

	/**
	 * 
	 * �Զ������֪ͨ����
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
	 * params[0]:���ŵ������ͣ�ȡSMSRule.SMS_TYPE_*֮һ
	 * 
	 * @see
	 * com.hp.idc.itsm.workflow.rule.IWorkflowRule#execute(com.linkage
	 * .toptea.itsm.task.TaskUpdateInfo,
	 * com.hp.idc.itsm.workflow.NodeInfo, java.lang.Object[])
	 */
	public Object execute(TaskUpdateInfo updateInfo, NodeInfo node, Object... params) throws Exception {
		if (params.length == 0) {
			logger.error("δ֪�Ķ��ŷ�������");
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
		//����ʱ���ŵĽ�����Ҫȡ��һ���ڵ�Ĵ�����
		String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_ROOLBACK, taskData.getParent(),wfData);
		String smsReceiver = smsTemplate[0];
		String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { operName_zh,
				taskData.getOwner().getOid() + "", taskData.getTitle(), taskData.getNodeDesc() },taskData);

		sendSMS(smsReceiver, smsContent);

	}

	/**
	 * ������������ʱ
	 * 
	 * @param fromData
	 *            ��һ���ڵ�
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
	 * ������������״ζ���
	 */
	private void doOvertime(TaskData task, WorkflowData wfData) {
		String[] smsTemplate = getSmsTemplate(SMSRule.SMS_TYPE_OVERTIME, task, wfData);
		String smsReceiver = smsTemplate[0];
		String smsContent = MessageManager.getInjectedMessage(smsTemplate[1], new String[] { "",
				task.getOwner().getOid() + "", task.getTitle(), task.getNodeDesc() },task);

		sendSMS(smsReceiver, smsContent);
	}

	/**
	 * ����������ʱ�Ķ���
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
			
			//�������˷�����
			FactorInfo fi = Cache.Factors.get(operName);
			if (fi!=null && fi.isSendSMS()) {
				String factor = fi.getFactors();
				if (factor!=null && !factor.equals(""))
					sendSMS(factor, smsContent);
			}
			
		}
		//����֪�˷��Ͷ���
		String readUser = task.getReadUser();
		if (readUser!=null && !readUser.equals("")){
			String smsContent = "�й�����Ҫ�����ģ�ID:"+task.getOwner().getOid()+",TITLE:"+task.getTitle()+"��";
			sendSMS(readUser, smsContent);
		}
		
		// �����Ѵ��������
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
		//�ж��Ƿ��������������
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
