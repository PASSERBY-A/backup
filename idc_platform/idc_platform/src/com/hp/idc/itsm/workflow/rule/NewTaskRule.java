/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.idc.itsm.impl.ITSMTaskManagerImpl;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.task.TaskWaitingInfo;
import com.hp.idc.itsm.util.ExpressionCalculate;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 创建新工单功能规则
 * <pre>
 * 功能描述：当一个流程到达某一步后，根据所配规则自动触发一个流程工单
 * 执行人为指定的一个人员字段值，如果人员字段值，或者流程OID
 * 两者任一个为空，则不触发
 * <pre>
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class NewTaskRule extends AbstractRule{

	//触发流程OID
	private int referToWFOid;
	
	//执行人的读取字段
	private String referToUser;
	
	//把工单OID带到新工单里的字段名
	private String referFromTaskOidField;
	
	//传递原工单的字段列表，逗号分隔多个，如果为“all”，则传递所有字段
	private String referFromFields;
	
	//条件表达式,如果为空，则直接触发，否则，判断条件是否成立
	private String conditionExp;
	
	//是否等待新触发的工单的完成
	private boolean doWait;
	
	//等待结束标识（新触发的工单状态），新工单到达哪一步时，原来工单就开始继续执行
	private String waitStatus;

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.AbstractRule#parse(org.dom4j.Element)
	 */
	public void parse(JSONObject json) throws JSONException {
		super.parse(json);
		if (json.has("wfOid"))
			referToWFOid = json.getInt("wfOid");

		if (json.has("exeUser"))
			referToUser = json.getString("exeUser");

		if (json.has("taskOidField"))
			referFromTaskOidField = json.getString("taskOidField");
		
		if (json.has("fields"))
			referFromFields = json.getString("fields");
		
		if (json.has("conditionExp"))
			conditionExp = json.getString("conditionExp");
		
		if (json.has("wait"))
			doWait = json.getBoolean("wait");
		
		if (json.has("waitStatus"))
			waitStatus = json.getString("waitStatus");
	}
	
	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.IWorkflowRule#execute(com.hp.idc.itsm.task.TaskUpdateInfo, com.hp.idc.itsm.workflow.NodeInfo, java.lang.Object[])
	 */
	public Object execute(TaskUpdateInfo updateInfo, NodeInfo node, Object... params) throws Exception {
		
		TaskInfo ti = updateInfo.getTaskInfo();
		TaskData td = updateInfo.getTaskData();
		
		// 匹配替换getFV("title")这种变量
		String regx = "(getFV\\(\"([a-zA-Z0-9_]+)\"\\))";
		Pattern p = Pattern.compile(regx);
		
		//开始处理条件触发
		String conditionExp = this.conditionExp;
		if (conditionExp!=null && !conditionExp.equals("")) {

			Matcher m = p.matcher(conditionExp);
			while (m.find()) {
				String fieldName = m.group(2);
				String ret1 = m.group(1);
				ret1 = ret1.replaceAll("\\(", "[(]");
				ret1 = ret1.replaceAll("\\)", "[)]");
				String fieldValue = td.getAttribute(fieldName);
				conditionExp = conditionExp.replaceAll(ret1, fieldValue);
			}
			// 如果表达式值为false，则返回
			if (!ExpressionCalculate.calculateBoolean(null, conditionExp))
				return null;
		}
		
		//开始处理处理人表达式
		String executeUser = this.referToUser;
		if (executeUser != null && !executeUser.equals("")) {
			Matcher m = p.matcher(executeUser);
			while (m.find()) {
				String fieldName = m.group(2);
				String ret1 = m.group(1);
				ret1 = ret1.replaceAll("\\(", "[(]");
				ret1 = ret1.replaceAll("\\)", "[)]");
				String fieldValue = td.getAttribute(fieldName);
				executeUser = executeUser.replaceAll(ret1, "," + fieldValue
						+ ",");
			}
			// 去空格
			executeUser = executeUser.replaceAll(" ", "");
			// 替换后可能出现 ",," 连一起的情况
			while (executeUser.indexOf(",,") != -1)
				executeUser = executeUser.replaceAll(",,", ",");
			// 去首尾","
			if (executeUser.startsWith(","))
				executeUser = executeUser.substring(1);
			if (executeUser.endsWith(","))
				executeUser = executeUser.substring(0, executeUser.length() - 1);
	
			if (executeUser.equals("") || executeUser.equals(",")) {
				logger.error("自动跳转执行人为空，不处理新建");
				return null;
			}
		}

		//开始处理字段值携带
		Map<String,String> m = new HashMap<String,String>();
		m.put(referFromTaskOidField, ti.getOid()+"");//把工单OID带到新工单里
		if (referFromFields!=null && !referFromFields.equals("")) {
			if (referFromFields.equalsIgnoreCase("all"))
				m.putAll(td.getAllFieldData());
			else {
				String[] fields = referFromFields.split(",");
				for (String field : fields){
					String value = td.getAttribute(field);
					if (value !=null && !value.equals(""))
						m.put(field, value);
				}
			}
			
		}
		m.put("_sys_linkTaskStr", "ITSM_"+ti.getWfOid()+"_"+ti.getWfVer()+"_"+ti.getOid());
		
		//开始创建工单
		WorkflowInfo wfi_ = WorkflowManager.getWorkflowByOid(referToWFOid);
		if (wfi_ != null){
			ITSMTaskManagerImpl taskManager = new ITSMTaskManagerImpl();
			int referTaskOid = taskManager.updateTask(-1,referToWFOid,m,-1,"","",executeUser,ti.getUser(),0);
			
			//开始双向关联工单
			String linktask = ti.getLinkedTaskStr();
			if (linktask!=null && !linktask.equals(""))
				linktask += ",";
			linktask += "ITSM_"+referToWFOid+"_"+wfi_.getCurrentVersionId()+"_"+referTaskOid;
			ti.setLinkedTaskStr(linktask);
			
			//开始处理等待状态
			if(this.doWait) {
				TaskWaitingInfo wInfo = new TaskWaitingInfo();
				wInfo.setWfOid(referToWFOid);
				wInfo.setWfVer(wfi_.getCurrentVersionId());
				wInfo.setWaiting_task_oid(referTaskOid);
				wInfo.setWaiting_end_status(this.waitStatus);
				td.setWaitStatus(wInfo);
			}
		} else
			logger.error("触发流程不存在，请查实");
		
		return null;
	}

	/**
	 * @return the referToWFOid
	 */
	public int getReferToWFOid() {
		return referToWFOid;
	}

	/**
	 * @param referToWFOid the referToWFOid to set
	 */
	public void setReferToWFOid(int referToWFOid) {
		this.referToWFOid = referToWFOid;
	}

	/**
	 * @return the referToField
	 */
	public String getReferToUser() {
		return referToUser;
	}

	/**
	 * @param referToField the referToField to set
	 */
	public void setReferToUser(String referToUser) {
		this.referToUser = referToUser;
	}

	/**
	 * @return the referFromTaskOidField
	 */
	public String getReferFromTaskOidField() {
		return referFromTaskOidField;
	}

	/**
	 * @param referFromTaskOidField the referFromTaskOidField to set
	 */
	public void setReferFromTaskOidField(String referFromTaskOidField) {
		this.referFromTaskOidField = referFromTaskOidField;
	}

	/**
	 * @return the referFromFields
	 */
	public String getReferFromFields() {
		return referFromFields;
	}

	/**
	 * @param referFromFields the referFromFields to set
	 */
	public void setReferFromFields(String referFromFields) {
		this.referFromFields = referFromFields;
	}

	/**
	 * @return the conditionExp
	 */
	public String getConditionExp() {
		return conditionExp;
	}

	/**
	 * @param conditionExp the conditionExp to set
	 */
	public void setConditionExp(String conditionExp) {
		this.conditionExp = conditionExp;
	}

	/**
	 * @return the doWaite
	 */
	public boolean isDoWait() {
		return doWait;
	}

	/**
	 * @param doWaite the doWaite to set
	 */
	public void setDoWait(boolean doWait) {
		this.doWait = doWait;
	}

	/**
	 * @return the waiteStatus
	 */
	public String getWaitStatus() {
		return waitStatus;
	}

	/**
	 * @param waiteStatus the waiteStatus to set
	 */
	public void setWaitStatus(String waitStatus) {
		this.waitStatus = waitStatus;
	}

}
