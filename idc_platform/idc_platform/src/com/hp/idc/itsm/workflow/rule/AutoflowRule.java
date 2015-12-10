/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.idc.itsm.impl.ITSMTaskManagerImpl;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.util.ExpressionCalculate;
import com.hp.idc.itsm.workflow.ActionInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * �Զ���ת����
 * <pre>
 * ���ܽ��ܣ���������һ���ڵ��ϵͳ�ɸ��������Զ���ת��ָ���ڵ�
 * </pre>
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class AutoflowRule extends AbstractRule {

	/**
	 * �Զ���ת����,��������
	 */
	public static final int AUTOFLOW_TYPE_UNCONDITIONAL = 0;
	
	/**
	 * �Զ���ת����,��������
	 */
	public static final int AUTOFLOW_TYPE_CONDITIONAL = 1;
	
	//Ŀ��ڵ�ӵ���
	private String autoFlowToUser = "";
	//Ŀ��ڵ�
	private String autoFlowToNode = "";
	//�Զ���ת����
	private int autoFlowType;
	//�������ʽ
	private String conditionExpression = "";

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.AbstractRule#parse(org.dom4j.Element)
	 */
	public void parse(JSONObject json) throws JSONException {
		super.parse(json);
		if(json.has("autoflowToUser"))
			autoFlowToUser = json.getString("autoflowToUser");
		if(json.has("autoflowToNode"))
			autoFlowToNode = json.getString("autoflowToNode");
		if(json.has("autoflowType"))
			autoFlowType = json.getInt("autoflowType");
		if(json.has("conditionExp"))
			conditionExpression = json.getString("conditionExp");	
	}
	
	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.IWorkflowRule#execute(com.hp.idc.itsm.task.TaskUpdateInfo)
	 */
	public Object execute(TaskUpdateInfo updateInfo,NodeInfo node, Object ... params) throws Exception {
		TaskData td = updateInfo.getTaskData();
		try {
			TaskInfo ti = updateInfo.getTaskInfo();
			WorkflowData wd = node.getOwner();

			NodeInfo autoToNodeInfo = node.getOwner().getNode(this.autoFlowToNode);

			if (this.autoFlowToNode.equals("") || autoToNodeInfo == null
					|| this.autoFlowToUser.equals(""))
				return null;

			// ��ʼƥ���滻getFV("title")���ֱ���
			String regx = "(getFV\\(\"([a-zA-Z0-9_]+)\"\\))";
			Pattern p = Pattern.compile(regx);

			// �������������Զ���ת���������ʽ
			String conditionExp = this.conditionExpression;
			if (this.autoFlowType == AutoflowRule.AUTOFLOW_TYPE_CONDITIONAL
					&& !conditionExp.equals("")) {
				Matcher m = p.matcher(conditionExp);
				while (m.find()) {
					String fieldName = m.group(2);
					String ret1 = m.group(1);
					ret1 = ret1.replaceAll("\\(", "[(]");
					ret1 = ret1.replaceAll("\\)", "[)]");
					String fieldValue = td.getAttribute(fieldName);
					conditionExp = conditionExp.replaceAll(ret1, fieldValue);
				}
				// ������ʽֵΪfalse���򷵻�,�������Զ���ת
				if (!ExpressionCalculate.calculateBoolean(null, conditionExp))
					return null;
			}

			//��ʼ�жϣ���ת�Ƿ�Ϸ�
			NodeInfo b1 = node.getBranchBeginNode();
			NodeInfo b2 = autoToNodeInfo.getBranchBeginNode();
			boolean canJump = false;
			if (b1 == null && b2 == null)
				canJump = true;
			else if (b1 == null) {
				if (autoToNodeInfo.getId().equals(b2.getId()))// ��ת��Ŀ��ڵ���Ƿ�֧��ʼ�ڵ�
					canJump = true;
			} else if (b2 == null) {
				;
			} else {
				// System.out.println("��֧�ڲ���ת�жϣ�"+autoToNodeInfo.getId().equals(b2.getId()));
				if (b1.getId().equals(b2.getId()))
					canJump = true;
				else if (autoToNodeInfo.getId().equals(b2.getId())) {
					// Դ�ڵ��Ƿ�֧��ʼ�ڵ㣬Ŀ��ڵ���Դ�ڵ�ķ�֧�ڲ���һ����ʼ�ڵ�
					// ����������ͼ���������հ�Ȧ����һ�𣬻����м���N����ͨ�ڵ㡣
					// System.out.println("��ʼ�ڵ�����");
					List<ActionInfo> acs = b1.getActions();
					Stack<NodeInfo>  s = new Stack<NodeInfo> ();
					Stack<NodeInfo> sb = new Stack<NodeInfo>();
					for (int i = 0; i < acs.size(); i++) {
						ActionInfo aInfo = acs.get(i);
						NodeInfo nInfo = aInfo.getToNode();
						s.push(nInfo);
					}
					// System.out.println("doAutoflow:------��ʼ�ڵ������ж�:");

					while (s.size() > 0) {
						NodeInfo n = s.pop();
						// System.out.println("doAutoflow:------����ڵ������ж�:"+n.getId());
						if (n.getId().equals(b2.getId())) {
							if (sb.size() == 0)
								canJump = true;
							break;
						}
						if (n.getType() == NodeInfo.TYPE_BRANCH_BEGIN)
							sb.push(n);
						if (n.getType() == NodeInfo.TYPE_BRANCH_END)
							sb.pop();
						List<ActionInfo> actions = n.getActions();
						for (int i = 0; i < actions.size(); i++) {
							ActionInfo aInfo = actions.get(i);
							s.push(aInfo.getToNode());
						}
					}
				}
			}
			if (!canJump) {
				throw new Exception("��֧�����֧�ڲ����໥��ת");
			}

			//��ʼ����Ŀ������˵ı��ʽ
			String autoExeUser = this.autoFlowToUser;
			Matcher m = p.matcher(autoExeUser);
			while (m.find()) {
				String fieldName = m.group(2);
				String ret1 = m.group(1);
				ret1 = ret1.replaceAll("\\(", "[(]");
				ret1 = ret1.replaceAll("\\)", "[)]");
				String fieldValue = td.getAttribute(fieldName);
				autoExeUser = autoExeUser.replaceAll(ret1, "," + fieldValue
						+ ",");
			}
			// ȥ�ո�
			autoExeUser = autoExeUser.replaceAll(" ", "");
			// �滻����ܳ��� ",," ��һ������
			while (autoExeUser.indexOf(",,") != -1)
				autoExeUser = autoExeUser.replaceAll(",,", ",");
			// ȥ��β","
			if (autoExeUser.startsWith(","))
				autoExeUser = autoExeUser.substring(1);
			if (autoExeUser.endsWith(","))
				autoExeUser = autoExeUser
						.substring(0, autoExeUser.length() - 1);

			if (autoExeUser.equals("") || autoExeUser.equals(",")) {
				System.out.println("�Զ���תִ����Ϊ�գ���������ת");
				return null;
			}

			if (node.getType() != NodeInfo.TYPE_BRANCH_BEGIN) {
				if (autoExeUser.indexOf(",") != -1)
					autoExeUser = autoExeUser.substring(0, autoExeUser
							.indexOf(","));
			}
			
			td.setNewAdded(false);
			TaskUpdateInfo tuInfo = new TaskUpdateInfo();
			tuInfo.setType(TaskUpdateInfo.TYPE_NORMAL);
			tuInfo.setMap(new HashMap<String,String>());
			tuInfo.setOperName("root");
			tuInfo.setTaskInfo(ti);
			tuInfo.setTaskData(td);
			tuInfo.setToNode(autoToNodeInfo);
			String toNodePath = td.getNodePath();
			if (toNodePath.indexOf("/") == -1)
				toNodePath = autoToNodeInfo.getId();
			else {
				toNodePath = toNodePath.substring(0, toNodePath
						.lastIndexOf("/") + 1)
						+ autoToNodeInfo.getId();
			}
			tuInfo.setToNodePath(toNodePath);
			tuInfo.setUsers(autoExeUser.split(","));
			tuInfo.setWorkflow(wd);

			String[] users = autoExeUser.split(",");
			if (node.getType() == NodeInfo.TYPE_BRANCH_BEGIN) {
				td.setStatus(TaskData.STATUS_CLOSE);
			}
			/**
			 * ֱ�Ӹ��ݶԿ��ˣ��½��ڵ㣬ʵ���ƽ���ת
			 */
			for (int i = 0; i < users.length; i++){
				if (users[i] == null || users[i].equals(""))
					continue;
				autoToNodeInfo.onUpdate(tuInfo);
				TaskData nextTD = new TaskData(ti,td,autoToNodeInfo,ActionInfo.TYPE_AUTOFLOW,users[i],"system");
				nextTD.setNewAdded(true);
				nextTD.setNodeDesc(autoToNodeInfo.getCaption());
				
				tuInfo.setTaskData(nextTD);
				autoToNodeInfo.onUpdateEnd(tuInfo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param autoFlowToUser the autoFlowToUser to set
	 */
	public void setAutoFlowToUser(String autoFlowToUser) {
		this.autoFlowToUser = autoFlowToUser;
	}

	/**
	 * @return the autoFlowToUser
	 */
	public String getAutoFlowToUser() {
		return autoFlowToUser;
	}

	public String getAutoFlowToNode() {
		return autoFlowToNode;
	}

	public void setAutoFlowToNode(String autoFlowToNode) {
		this.autoFlowToNode = autoFlowToNode;
	}

	public int getAutoFlowType() {
		return autoFlowType;
	}

	public void setAutoFlowType(int autoFlowType) {
		this.autoFlowType = autoFlowType;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}


}
