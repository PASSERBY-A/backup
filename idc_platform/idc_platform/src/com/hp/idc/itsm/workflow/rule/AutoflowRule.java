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
 * 自动流转规则
 * <pre>
 * 功能介绍：工单到达一个节点后，系统可根据配置自动流转到指定节点
 * </pre>
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class AutoflowRule extends AbstractRule {

	/**
	 * 自动流转类型,无条件的
	 */
	public static final int AUTOFLOW_TYPE_UNCONDITIONAL = 0;
	
	/**
	 * 自动流转类型,有条件的
	 */
	public static final int AUTOFLOW_TYPE_CONDITIONAL = 1;
	
	//目标节点接单人
	private String autoFlowToUser = "";
	//目标节点
	private String autoFlowToNode = "";
	//自动流转类型
	private int autoFlowType;
	//条件表达式
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

			// 开始匹配替换getFV("title")这种变量
			String regx = "(getFV\\(\"([a-zA-Z0-9_]+)\"\\))";
			Pattern p = Pattern.compile(regx);

			// 处理有条件的自动流转的条件表达式
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
				// 如果表达式值为false，则返回,不进行自动流转
				if (!ExpressionCalculate.calculateBoolean(null, conditionExp))
					return null;
			}

			//开始判断，跳转是否合法
			NodeInfo b1 = node.getBranchBeginNode();
			NodeInfo b2 = autoToNodeInfo.getBranchBeginNode();
			boolean canJump = false;
			if (b1 == null && b2 == null)
				canJump = true;
			else if (b1 == null) {
				if (autoToNodeInfo.getId().equals(b2.getId()))// 跳转的目标节点就是分支开始节点
					canJump = true;
			} else if (b2 == null) {
				;
			} else {
				// System.out.println("分支内部跳转判断："+autoToNodeInfo.getId().equals(b2.getId()));
				if (b1.getId().equals(b2.getId()))
					canJump = true;
				else if (autoToNodeInfo.getId().equals(b2.getId())) {
					// 源节点是分支开始节点，目标节点是源节点的分支内部的一个开始节点
					// 表现在流程图上是两个空白圈连在一起，或者中间有N个普通节点。
					// System.out.println("开始节点相连");
					List<ActionInfo> acs = b1.getActions();
					Stack<NodeInfo>  s = new Stack<NodeInfo> ();
					Stack<NodeInfo> sb = new Stack<NodeInfo>();
					for (int i = 0; i < acs.size(); i++) {
						ActionInfo aInfo = acs.get(i);
						NodeInfo nInfo = aInfo.getToNode();
						s.push(nInfo);
					}
					// System.out.println("doAutoflow:------开始节点相连判断:");

					while (s.size() > 0) {
						NodeInfo n = s.pop();
						// System.out.println("doAutoflow:------进入节点相连判断:"+n.getId());
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
				throw new Exception("分支外与分支内不能相互跳转");
			}

			//开始处理目标操作人的表达式
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
			// 去空格
			autoExeUser = autoExeUser.replaceAll(" ", "");
			// 替换后可能出现 ",," 连一起的情况
			while (autoExeUser.indexOf(",,") != -1)
				autoExeUser = autoExeUser.replaceAll(",,", ",");
			// 去首尾","
			if (autoExeUser.startsWith(","))
				autoExeUser = autoExeUser.substring(1);
			if (autoExeUser.endsWith(","))
				autoExeUser = autoExeUser
						.substring(0, autoExeUser.length() - 1);

			if (autoExeUser.equals("") || autoExeUser.equals(",")) {
				System.out.println("自动跳转执行人为空，不处理跳转");
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
			 * 直接根据对口人，新建节点，实现推进流转
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
