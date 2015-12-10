/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import java.sql.SQLException;
import java.util.Date;

import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 自定义短信规则
 * <pre>
 * 规则介绍，每个节点可以在系统的短信之外，发送额外的短信。
 * 在每个节点onUpdateEnd时执行
 * 不受NodeInfo.enableSMS属性控制
 * </pre>
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class CustomSMSRule  extends AbstractRule {

	//短信内容模板
	private String smsContent = "";
	
	//接受人
	private String smsReceiver = "";
	
	public void parse(JSONObject json) throws JSONException {
		super.parse(json);
		if (json.has("smsContent"))
			this.smsContent = json.getString("smsContent");
		if (json.has("smsReceiver")) {
			this.smsReceiver = json.getString("smsReceiver");

		}
	}
	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.IWorkflowRule#execute(com.hp.idc.itsm.task.TaskUpdateInfo, com.hp.idc.itsm.workflow.NodeInfo, java.lang.Object[])
	 */
	public Object execute(TaskUpdateInfo updateInfo, NodeInfo node, Object... params) throws Exception {
		TaskData td = updateInfo.getTaskData();
		String receiver = MessageManager.getInjectedReceiver(smsReceiver, td);
		String content = MessageManager.getInjectedMessage(smsContent, new String[]{}, td);
		
		try {
			String[] receivers = receiver.split(",");
			for (String rec_ : receivers) {
				if (rec_ == null || rec_.equals(""))
					continue;
				MessageManager.sendSms(content, rec_, "system", new Date(), "system");
			}
		} catch (SQLException e) {
			logger.error(e);
		}
		
		return null;
	}
	/**
	 * @return the smsContent
	 */
	public String getSmsContent() {
		return smsContent;
	}
	/**
	 * @param smsContent the smsContent to set
	 */
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return smsReceiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.smsReceiver = receiver;
	}

}
