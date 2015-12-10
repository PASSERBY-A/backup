/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 超时检测规则
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class OvertimeCheckRule extends AbstractRule {

	// 存储超时检测字段
	private List<FieldInfo> overtimeCheckFields = new ArrayList<FieldInfo>();

	// 短信提醒间隔,单位分钟,0或负数不提醒
	private int smsRemindDuration = 0;

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.AbstractRule#parse(org.dom4j.Element)
	 */
	public void parse(JSONObject json) throws JSONException {
		super.parse(json);
		if (json.has("smsRemindDuration"))
			this.smsRemindDuration = json.getInt("smsRemindDuration");
		if (json.has("overtimeCheckFields")) {
			String str = json.getString("overtimeCheckFields");
			String fields[] = str.split(",");
			for (int i = 0; i < fields.length; i++) {
				FieldInfo fInfo = FieldManager.getFieldById("ITSM",fields[i],false);
				if (fInfo != null)
					this.overtimeCheckFields.add(fInfo);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.itsm.workflow.rule.IWorkflowRule#execute(com.linkage
	 * .toptea.itsm.task.TaskUpdateInfo)
	 */
	public Object execute(TaskUpdateInfo updateInfo,NodeInfo node, Object ... params) throws Exception {
		TaskData task = updateInfo.getTaskData();
		TaskInfo info = updateInfo.getTaskInfo();
		Boolean ret = new Boolean(false);
		try {
			if (!task.isOvertime()) {
				// List l = node.getOvertimeCheckFields();
				for (int i = 0; i < overtimeCheckFields.size(); i++) {
					FieldInfo fInfo = overtimeCheckFields.get(i);
					String val = task.getAttribute(fInfo.getId());
					if (val == null || val.length() == 0)
						continue;
					long t = -1;
					long n = val.length();
					if (n == 14)
						t = DateTimeUtil.parseDate(val, "yyyyMMddHHmmss").getTime();
					else if (n == 19)
						t = DateTimeUtil.parseDate(val, "yyyy-MM-dd HH:mm:ss").getTime();
					else if (n == 10)
						t = DateTimeUtil.parseDate(val, "yyyy-MM-dd").getTime();
					else if (n == 8)
						t = DateTimeUtil.parseDate(val, "yyyyMMdd").getTime();
					if (t != -1 && t < System.currentTimeMillis()) {
						ItsmUtil.debugPrint("检测到工单" + info.getOid() + "." + task.getDataId() + "已超时");
						task.setOvertime();
						task.setSmsRemindTime(System.currentTimeMillis());
						task.setSmsRemindCount(task.getSmsRemindCount()+1);
						ret = new Boolean(true);
//						String fieldDesc = fInfo.getName() + ":" + val;

						try {
							node.onOvertime(updateInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
			} else {
				int dur = this.smsRemindDuration;//node.getSmsRemindDuration();
				if (dur > 0 && node.getOwner().getOwner().isEnableSMS()) {
					if (System.currentTimeMillis() - task.getSmsRemindTime() > dur * 60000) {
						task.setSmsRemindTime(System.currentTimeMillis());
						task.setSmsRemindCount(task.getSmsRemindCount()+1);
						ItsmUtil.debugPrint("发送工单" + info.getOid() + "." + task.getDataId() + "的短信提醒");
						ret = new Boolean(true);
						IWorkflowRule smsRule = node.getRule(SMSRule.class.getName());
						smsRule.execute(updateInfo, node, SMSRule.SMS_TYPE_REMIND_AUTO);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return ret;

	}

	/**
	 * @return the overtimeCheckFields
	 */
	public List<FieldInfo> getOvertimeCheckFields() {
		return overtimeCheckFields;
	}

	/**
	 * @param overtimeCheckFields
	 *            the overtimeCheckFields to set
	 */
	public void setOvertimeCheckFields(List<FieldInfo> overtimeCheckFields) {
		this.overtimeCheckFields = overtimeCheckFields;
	}

	public void addOvertimeCheckField(FieldInfo f) {
		this.overtimeCheckFields.add(f);
	}

	/**
	 * @return the smsRemindDuration
	 */
	public int getSmsRemindDuration() {
		return smsRemindDuration;
	}

	/**
	 * @param smsRemindDuration
	 *            the smsRemindDuration to set
	 */
	public void setSmsRemindDuration(int smsRemindDuration) {
		this.smsRemindDuration = smsRemindDuration;
	}

}
