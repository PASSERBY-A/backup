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
 * �����¹������ܹ���
 * <pre>
 * ������������һ�����̵���ĳһ���󣬸�����������Զ�����һ�����̹���
 * ִ����Ϊָ����һ����Ա�ֶ�ֵ�������Ա�ֶ�ֵ����������OID
 * ������һ��Ϊ�գ��򲻴���
 * <pre>
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class NewTaskRule extends AbstractRule{

	//��������OID
	private int referToWFOid;
	
	//ִ���˵Ķ�ȡ�ֶ�
	private String referToUser;
	
	//�ѹ���OID�����¹�������ֶ���
	private String referFromTaskOidField;
	
	//����ԭ�������ֶ��б����ŷָ���������Ϊ��all�����򴫵������ֶ�
	private String referFromFields;
	
	//�������ʽ,���Ϊ�գ���ֱ�Ӵ����������ж������Ƿ����
	private String conditionExp;
	
	//�Ƿ�ȴ��´����Ĺ��������
	private boolean doWait;
	
	//�ȴ�������ʶ���´����Ĺ���״̬�����¹���������һ��ʱ��ԭ�������Ϳ�ʼ����ִ��
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
		
		// ƥ���滻getFV("title")���ֱ���
		String regx = "(getFV\\(\"([a-zA-Z0-9_]+)\"\\))";
		Pattern p = Pattern.compile(regx);
		
		//��ʼ������������
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
			// ������ʽֵΪfalse���򷵻�
			if (!ExpressionCalculate.calculateBoolean(null, conditionExp))
				return null;
		}
		
		//��ʼ�������˱��ʽ
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
			// ȥ�ո�
			executeUser = executeUser.replaceAll(" ", "");
			// �滻����ܳ��� ",," ��һ������
			while (executeUser.indexOf(",,") != -1)
				executeUser = executeUser.replaceAll(",,", ",");
			// ȥ��β","
			if (executeUser.startsWith(","))
				executeUser = executeUser.substring(1);
			if (executeUser.endsWith(","))
				executeUser = executeUser.substring(0, executeUser.length() - 1);
	
			if (executeUser.equals("") || executeUser.equals(",")) {
				logger.error("�Զ���תִ����Ϊ�գ��������½�");
				return null;
			}
		}

		//��ʼ�����ֶ�ֵЯ��
		Map<String,String> m = new HashMap<String,String>();
		m.put(referFromTaskOidField, ti.getOid()+"");//�ѹ���OID�����¹�����
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
		
		//��ʼ��������
		WorkflowInfo wfi_ = WorkflowManager.getWorkflowByOid(referToWFOid);
		if (wfi_ != null){
			ITSMTaskManagerImpl taskManager = new ITSMTaskManagerImpl();
			int referTaskOid = taskManager.updateTask(-1,referToWFOid,m,-1,"","",executeUser,ti.getUser(),0);
			
			//��ʼ˫���������
			String linktask = ti.getLinkedTaskStr();
			if (linktask!=null && !linktask.equals(""))
				linktask += ",";
			linktask += "ITSM_"+referToWFOid+"_"+wfi_.getCurrentVersionId()+"_"+referTaskOid;
			ti.setLinkedTaskStr(linktask);
			
			//��ʼ����ȴ�״̬
			if(this.doWait) {
				TaskWaitingInfo wInfo = new TaskWaitingInfo();
				wInfo.setWfOid(referToWFOid);
				wInfo.setWfVer(wfi_.getCurrentVersionId());
				wInfo.setWaiting_task_oid(referTaskOid);
				wInfo.setWaiting_end_status(this.waitStatus);
				td.setWaitStatus(wInfo);
			}
		} else
			logger.error("�������̲����ڣ����ʵ");
		
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
