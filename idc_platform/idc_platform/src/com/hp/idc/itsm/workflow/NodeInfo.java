package com.hp.idc.itsm.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Element;

import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.workflow.rule.AbstractRule;
import com.hp.idc.itsm.workflow.rule.DynamicCodeRule;
import com.hp.idc.itsm.workflow.rule.SMSRule;
import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 表示流程节点信息
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class NodeInfo {
	/**
	 * 节点类型:表示一般节点
	 */
	public static int TYPE_NODE = 0;

	/**
	 * 节点类型:表示分支开始
	 */
	public static int TYPE_BRANCH_BEGIN = 1;

	/**
	 * 节点类型:表示分支结束
	 */
	public static int TYPE_BRANCH_END = 2;

	/**
	 * 节点类型：子流程节点
	 */
	public static int TYPE_SUB_WF = 3;

	/**
	 * 存储节点ID
	 */
	protected String id;

	/**
	 * 存储节点描述
	 */
	protected String caption;

	/**
	 * 存储关联的表单ID
	 */
	protected String formId = -1 + "";

	/**
	 * 转发表单，如果为空，取来向ActionInfo的transmitFormId
	 */
	protected String transmitFormId;
	//转发模板
	protected String transmitTemplate;

	/**
	 * 存储节点类型
	 */
	protected int type;
	
	private boolean enableSMS = true;

	/**
	 * 存储节点是否可以回退
	 */
	protected boolean rollbackable;

	/**
	 * 存储节点是否可以编辑/转发
	 */
	protected boolean forwardable;
	
	/**
	 * 共享处理，当工单到达此节点时，是否允许处理人之外的人进行预处理
	 * 即：填写信息后保存为草稿，处理人处理此工单时即可看到已经填写的处理信息。
	 */
	protected boolean shareDeal;

	/**
	 * 存储节点所属流程对象
	 */
	protected WorkflowData owner;

	/**
	 * 存储节点下所有的动作列表
	 */
	protected List<ActionInfo> actions = new ArrayList<ActionInfo>();

	/**
	 * 存储动态代码的类名
	 */
	private String className = "";

	/**
	 * 节点上的所有处理规则
	 */
	protected Map<String, AbstractRule> ruleList = new HashMap<String, AbstractRule>();


	/**
	 * 子流程id 当此节点为子流程节点时有效
	 */
	protected String subflow = "";
	// 子流程版本
	protected String subflowVer = "";

	/**
	 * 展示模板
	 */
	protected String template = "";

//	/**
//	 * 自定义短信通知内容List<String[noteTo,noteMessage]>
//	 */
//	protected List noticeList = new ArrayList();

	/**
	 * the task queue on this node: user1:5;user2:9;user3:10
	 */
	public String queue = "";
	/**
	 * 根据目标节点id查找动作对象
	 * 
	 * @param toNodeId 目标节点id
	 * @return 找到的ActionInfo对象
	 */
	public ActionInfo getAction(String toNodeId) {
		for (int i = 0; i < this.actions.size(); i++) {
			ActionInfo el = (ActionInfo) this.actions.get(i);
			if (el.getToNodeId().equals(toNodeId))
				return el;
		}
		return null;
	}

	/**
	 * 根据所属流程和XML数据创建新的NodeInfo对象
	 * 
	 * @param owner
	 *            所属流程
	 * @param node
	 *            XML配置数据
	 * @return 生成的NodeInfo对象
	 */
	public void parse(WorkflowData owner, Element node) {
		this.owner = owner;
		this.id = node.attributeValue("id");
		this.caption = node.attributeValue("name");
		this.formId = node.attributeValue("formId");
		this.enableSMS = "false".equals(node.attributeValue("enableSMS"))?false:true;
		this.queue = node.attributeValue("queue");
		
		if (!this.owner.forms.containsKey(this.formId)) {
			int formOid = 0;
			try {
				formOid = Integer.parseInt(this.formId);
				if (FormManager.getFormByOid(formOid) == null)
					formOid = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.formId = formOid + "";
			node.addAttribute("formId", this.formId);
		}

		String transFormId_ = node.attributeValue("transmitFormId");
		int formOid = -1;
		if (transFormId_ != null) {
			try {
				formOid = Integer.parseInt(transFormId_);
				if (FormManager.getFormByOid(formOid) == null)
					formOid = -1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.transmitFormId = formOid + "";

		// this.smsRemindDuration =
		// StringUtil.parseInt(node.attributeValue("smsRemindDuration"), 0);
		if ("false".equals(node.attributeValue("rollbackable")))
			this.rollbackable = false;
		else
			this.rollbackable = true;
		
		if ("false".equals(node.attributeValue("forwardable")))
			this.forwardable = false;
		else
			this.forwardable = true;
		
		if("true".equals(node.attributeValue("shareDeal")))
			this.shareDeal = true;
		else
			this.shareDeal = false;
		String type = node.attributeValue("type");
		if (type == null)
			type = "";
		if (type.equals("branchBegin"))
			this.type = NodeInfo.TYPE_BRANCH_BEGIN;
		else if (type.equals("branchEnd"))
			this.type = NodeInfo.TYPE_BRANCH_END;
		else if (type.equals("subflow"))
			this.type = NodeInfo.TYPE_SUB_WF;
		else
			// if (type.equals("normal"))
			this.type = NodeInfo.TYPE_NODE;

		this.subflow = node.attributeValue("subflow");
		this.subflowVer = node.attributeValue("subflowVer");
		//		
		Element n = (Element) node.selectSingleNode("./overtime");
		// if (n != null) {
		// String str = n.getText();
		// String fields[] = str.split(",");
		// for (int i = 0; i < fields.length; i++) {
		// FieldInfo fInfo = FieldManager.getFieldById("ITSM",fields[i],false);
		// if (fInfo != null)
		// this.overtimeCheckFields.add(fInfo);
		// }
		// }

		n = (Element) node.selectSingleNode("./script");
		if (n != null)
			this.className = n.attributeValue("className");
		if (this.className == null)
			this.className = "";
		List l = node.selectNodes("./actions/action");
		for (int i = 0; i < l.size(); i++) {
			Element el = (Element) l.get(i);
			ActionInfo.parse(this, el);
		}
		// n = (Element)node.selectSingleNode("./autoflow");
		// if (n != null) {
		// if ("true".equals(n.attributeValue("enable")))
		// this.autoFlow = true;
		// else
		// this.autoFlow = false;
		//			
		// this.autoFlowToNode = n.attributeValue("toNode");
		// if (this.autoFlowToNode == null)
		// this.autoFlowToNode = "";
		// /**
		// *
		// 2008-11-18做了更新，this.autoFlowExeUser由原来存储在text里面，改为单独节点<executeUser/>存储
		// * 为了保持版本兼容，首先判断有无<executeUser/>节点，如果没有，再去读n.getText();
		// */
		// Element e_ = (Element)n.selectSingleNode("executeUser");
		// if (e_!=null){
		// this.autoFlowExeUser = e_.getText();
		// } else
		// this.autoFlowExeUser = n.getText();
		// if (this.autoFlowExeUser == null){
		// this.autoFlowExeUser = "";
		// }
		// e_ = (Element)n.selectSingleNode("autoType");
		// if (e_ != null) {
		// if ("1".equals(e_.attributeValue("type")))//1:有条件的，0:无条件的
		// this.hasCondition = true;
		// else
		// this.hasCondition = false;
		// this.conditionExpression = e_.getText();
		// if (this.conditionExpression == null)
		// this.conditionExpression = "";
		// }
		//			
		// }
		n = (Element) node.selectSingleNode("./template");
		if (n != null) {
			this.template = n.getText();
			if (this.template == null) {
				this.template = "/default/nodeViewForm.html";
			}
		} else
			this.template = "/default/nodeViewForm.html";
		
		n = (Element) node.selectSingleNode("./transmitTemplate");
		if (n != null) {
			this.transmitTemplate = n.getText();
			if (this.transmitTemplate == null) {
				this.transmitTemplate = "/default/nodeAction.html";
			}
		} else
			this.transmitTemplate = "/default/nodeAction.html";

		// 解析规则
		Element ruleEl = (Element) node.selectSingleNode("./rule");
		if (ruleEl != null) {
			try {
				String ruleStr = ruleEl.getText();
				JSONArray ruleArray = new JSONArray(ruleStr);
				for (int i = 0; i < ruleArray.length(); i++) {
					JSONObject ruleJSON = ruleArray.getJSONObject(i);
					try {
						String ruleType = ruleJSON.getString("type");
						AbstractRule rule = (AbstractRule) Class.forName(ruleType).newInstance();
						rule.parse(ruleJSON);
						ruleList.put(ruleType, rule);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		if (ruleList.get(SMSRule.class.getName()) == null)
			ruleList.put(SMSRule.class.getName(), new SMSRule());
		owner.getNodes().add(this);
	}

	/**
	 * 获取本类的拷贝
	 * 
	 * @return
	 */
	public NodeInfo getClone() {
		NodeInfo ret = new NodeInfo();
		ret.setId(this.id);
		ret.setCaption(this.caption);
		ret.setFormId(this.formId);
		ret.setTransmitFormId(this.transmitFormId);
		ret.setType(this.type);
		ret.setRollbackable(this.rollbackable);
		ret.setForwardable(this.forwardable);
		ret.owner = this.owner;
		ret.actions = this.actions;
		ret.className = this.className;
		// ret.overtimeCheckFields = this.overtimeCheckFields;
		// ret.smsRemindDuration = this.smsRemindDuration;
		ret.subflow = this.subflow;
		ret.subflowVer = this.subflowVer;
		// ret.autoFlow = this.autoFlow;
		// ret.autoFlowExeUser = this.autoFlowExeUser;
		// ret.autoFlowToNode = this.autoFlowToNode;
		// ret.hasCondition = this.hasCondition;
		// ret.conditionExpression = this.conditionExpression;
		ret.template = this.template;
//		ret.noticeList = this.noticeList;
		ret.ruleList = this.ruleList;
		ret.queue = this.queue;
		return ret;
	}

	/**
	 * 获取动态代码的类名
	 * 
	 * @return 返回动态代码的类名
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * 设置动态代码的类名
	 * 
	 * @param className
	 *            动态代码的类名
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 获取节点描述
	 * 
	 * @return 返回节点描述
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * 设置节点描述
	 * 
	 * @param caption
	 *            节点描述
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 获取关联的表单ID
	 * 
	 * @return 返回关联的表单ID
	 */
	public String getFormId() {
		return this.formId;
	}

	/**
	 * 设置关联的表单ID
	 * 
	 * @param formId
	 *            关联的表单ID
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * 获取节点ID
	 * 
	 * @return 返回节点ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置节点ID
	 * 
	 * @param id
	 *            节点ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 在节点更新时调用此函数
	 * 
	 * @param tuInfo
	 * @throws Exception
	 */
	public void onUpdate(TaskUpdateInfo tuInfo) throws Exception {
		for (AbstractRule rule : ruleList.values()) {
			if (rule instanceof DynamicCodeRule) {
				rule.execute(tuInfo, this, DynamicCodeRule.METHOD_NAME_ONUPDATE);
			} else if (rule.getRunPosition() != null
					&& rule.getRunPosition().equals(AbstractRule.RULE_RUN_POSITION_UPDATE))
				rule.execute(tuInfo, this);
		}
	}

	/**
	 * 节点更新完毕存数据库之前
	 * 
	 * @param tuInfo
	 * @throws Exception
	 */
	public void onUpdateEnd(TaskUpdateInfo tuInfo) throws Exception {
		for (AbstractRule rule : ruleList.values()) {
			if (rule instanceof DynamicCodeRule) {
				rule.execute(tuInfo, this, DynamicCodeRule.METHOD_NAME_ONUPDATEEND);
			} else if (rule.getRunPosition() != null
					&& rule.getRunPosition().equals(AbstractRule.RULE_RUN_POSITION_UPDATE_END))
				rule.execute(tuInfo, this);
		}

		//发送到达短信
//		System.out.println("==|||==:"+isEnableSMS()+"//："+(this.owner.getOwner().isEnableSMS() && isEnableSMS()));
		if (this.owner.getOwner().isEnableSMS() && isEnableSMS()) {
			AbstractRule smsRule = getRule(SMSRule.class.getName());
			smsRule.execute(tuInfo, this, SMSRule.SMS_TYPE_ARRIVE);
		}
	}

	/**
	 * 当节点显示时调用此函数
	 * 
	 * @param info
	 * @param data
	 * @param operName
	 * @throws Exception
	 */
	public void onEnter(TaskUpdateInfo tuInfo) throws Exception {
		for (AbstractRule rule : ruleList.values()) {
			if (rule instanceof DynamicCodeRule) {
				rule.execute(tuInfo, this, DynamicCodeRule.METHOD_NAME_ONENTER);
			} else if (rule.getRunPosition() != null
					&& rule.getRunPosition().equals(AbstractRule.RULE_RUN_POSITION_ENTER))
				rule.execute(tuInfo, this);
		}
	}

	/**
	 * 工单超时时执行的函数
	 * 
	 * @param td
	 * @throws Exception
	 */
	public void onOvertime(TaskUpdateInfo tuInfo) throws Exception {
		for (AbstractRule rule : ruleList.values()) {
			if (rule instanceof DynamicCodeRule) {
				rule.execute(tuInfo, this, DynamicCodeRule.METHOD_NAME_ONOVERTIME);
			} else if (rule.getRunPosition() != null
					&& rule.getRunPosition().equals(AbstractRule.RULE_RUN_POSITION_OVERTIME))
				rule.execute(tuInfo, this);
		}
		// 发送超时短信
		if (this.owner.getOwner().isEnableSMS() && this.isEnableSMS()) {
			AbstractRule smsRule = getRule(SMSRule.class.getName());
			smsRule.execute(tuInfo, this, SMSRule.SMS_TYPE_OVERTIME);
		}

	}

	/**
	 * 回退时执行
	 * 
	 * @throws Exception
	 */
	public void onRollback(TaskUpdateInfo tuInfo) throws Exception {
		for (AbstractRule rule : ruleList.values()) {
			if (rule instanceof DynamicCodeRule) {
				rule.execute(tuInfo, this, DynamicCodeRule.METHOD_NAME_ONROLLBACK);
			} else if (rule.getRunPosition() != null
					&& rule.getRunPosition().equals(AbstractRule.RULE_RUN_POSITION_ROLLBACK))
				rule.execute(tuInfo, this);
		}

		// 发送回退短信
		if (this.owner.getOwner().isEnableSMS() && this.isEnableSMS()) {
			AbstractRule smsRule = getRule(SMSRule.class.getName());
			smsRule.execute(tuInfo, this, SMSRule.SMS_TYPE_ROOLBACK);
		}
	}
	
	/**
	 * 实时扫描执行的方法
	 * @param tuInfo
	 * @throws Exception
	 */
	public void onRealtime(TaskUpdateInfo tuInfo) throws Exception {
		for (AbstractRule rule : ruleList.values()) {
			if (rule instanceof DynamicCodeRule) {
				rule.execute(tuInfo, this, DynamicCodeRule.METHOD_NAME_ONREALTIME);
			} else if (rule.getRunPosition() != null
					&& rule.getRunPosition().equals(AbstractRule.RULE_RUN_POSITION_REALTIME))
				rule.execute(tuInfo, this);
		}
	}


	/**
	 * 获取节点类型
	 * 
	 * @see #TYPE_NODE
	 * @see #TYPE_BRANCH_BEGIN
	 * @see #TYPE_BRANCH_END
	 * @see #TYPE_SUB_WF
	 * @return 返回节点类型
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * 设置节点类型
	 * 
	 * @see #TYPE_NODE
	 * @see #TYPE_BRANCH_BEGIN
	 * @see #TYPE_BRANCH_END
	 * @param type
	 *            节点类型
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the enableSMS
	 */
	public boolean isEnableSMS() {
		return enableSMS;
	}

	/**
	 * @param enableSMS the enableSMS to set
	 */
	public void setEnableSMS(boolean enableSMS) {
		this.enableSMS = enableSMS;
	}

	/**
	 * 获取节点下所有的动作列表
	 * 
	 * @return 返回节点下所有的动作列表
	 */
	public List<ActionInfo> getActions() {
		return this.actions;
	}
	
	/**
	 * 获取同组的动作，适用于分支开始
	 * @param groupId
	 * @return
	 */
	public List<ActionInfo> getActions(String groupId) {
		List<ActionInfo> ret = new ArrayList<ActionInfo> ();
		if (groupId == null)
			groupId = "";
		if (this.actions!=null) {
			for (int i = 0; i < this.actions.size(); i++) {
				ActionInfo ai = this.actions.get(i);
				if (ai.getGroupId().equals(groupId))
					ret.add(ai);
			}
		}
		return ret;
	}

	/**
	 * 获取节点所属流程对象
	 * 
	 * @return 返回节点所属流程对象
	 */
	public WorkflowData getOwner() {
		return this.owner;
	}

	public NodeInfo getBranchBeginNode() {
		NodeInfo rootNode = this.owner.getRootNode();
		// 动作堆栈
		Stack nodes = new Stack();
		// 开始节点堆栈
		Stack bns = new Stack();
		nodes.push(rootNode);
		Map visit = new HashMap();

		while (nodes.size() > 0) {
			NodeInfo nInfo = (NodeInfo) nodes.pop();
			if (nInfo.getId().equals(this.id)) {
				if (nInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN)
					return nInfo;
				if (bns.size() == 0)
					return null;
				else
					return (NodeInfo) bns.peek();
			}
			if (nInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN) {
				bns.push(nInfo);
			}
			if (nInfo.getType() == NodeInfo.TYPE_BRANCH_END)
				bns.pop();
			visit.put(nInfo.getId(), "");
			List actions = nInfo.getActions();
			for (int i = 0; i < actions.size(); i++) {
				ActionInfo aInfo = (ActionInfo) actions.get(i);
				NodeInfo n = aInfo.getToNode();
				if (visit.get(n.getId()) == null)// 如果访问过的节点不再访问
					nodes.push(n);
			}
		}
		return null;
	}

	/**
	 * 获取节点是否可以回退
	 * 
	 * @return 返回节点是否可以回退
	 */
	public boolean isRollbackable() {
		return this.rollbackable;
	}

	/**
	 * 设置节点是否可以回退
	 * 
	 * @param canBack
	 *            节点是否可以回退
	 */
	public void setRollbackable(boolean canBack) {
		this.rollbackable = canBack;
	}

	/**
	 * 获取节点是否可以编辑/转发
	 * 
	 * @return 返回节点是否可以编辑/转发
	 */
	public boolean isForwardable() {
		return this.forwardable;
	}

	/**
	 * 设置节点是否可以编辑/转发
	 * 
	 * @param forwardable
	 *            节点是否可以编辑/转发
	 */
	public void setForwardable(boolean forwardable) {
		this.forwardable = forwardable;
	}


	/**
	 * @return the shareDeal
	 */
	public boolean isShareDeal() {
		return shareDeal;
	}

	/**
	 * @param shareDeal the shareDeal to set
	 */
	public void setShareDeal(boolean shareDeal) {
		this.shareDeal = shareDeal;
	}

	public String getTemplate() {
		return template;
	}

	public String getTransmitFormId() {
		return transmitFormId;
	}

	public void setTransmitFormId(String transmitFormId) {
		this.transmitFormId = transmitFormId;
	}

	/**
	 * @return the transmitTemplate
	 */
	public String getTransmitTemplate() {
		return transmitTemplate;
	}

	/**
	 * @param transmitTemplate the transmitTemplate to set
	 */
	public void setTransmitTemplate(String transmitTemplate) {
		this.transmitTemplate = transmitTemplate;
	}

	public int getSubflowVer() {
		if (subflowVer == null || subflowVer.equals(""))
			subflowVer = "-1";
		return Integer.parseInt(subflowVer);
	}

	public void setSubflowVer(String subflowVer) {
		this.subflowVer = subflowVer;
	}

	public int getSubflow() {
		if (subflow == null || subflow.equals(""))
			subflow = "-1";
		return Integer.parseInt(subflow);
	}

	public void setSubflow(String subflow) {
		this.subflow = subflow;
	}

	/**
	 * 获取真实节点<br>
	 * 
	 * 如果当前节点是子流程节点，则要返回子流程的首节点，层层递归放回最里层的节点。
	 * 
	 * @return
	 */
	public NodeInfo getSubNode() {
		if (this.getType() == NodeInfo.TYPE_SUB_WF) {
			WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.getSubflow());
			if (wfInfo != null) {
				WorkflowData wfData = wfInfo.getVersion(this.getSubflowVer());
				if (wfData != null && wfData.getRootNode() != null)
					return wfData.getRootNode().getSubNode();
			}
		}
		return this;
	}

	public void getSubNodePath(StringBuffer nodePath) {
		if (nodePath == null)
			nodePath = new StringBuffer();
		if (nodePath.length() > 0)
			nodePath.append("/");
		nodePath.append(this.getId());
		if (this.getType() == NodeInfo.TYPE_SUB_WF) {
			WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.getSubflow());
			WorkflowData wfData = wfInfo.getVersion(this.getSubflowVer());
			wfData.getRootNode().getSubNodePath(nodePath);
		}
	}

	/**
	 * 仅返回节点上默认的规则配置<br>
	 * 
	 * <pre>
	 * 	sms:短信发送规则
	 * </pre>
	 * 
	 * @param ruleName
	 * @return
	 */
	public AbstractRule getRule(String className) {
		return ruleList.get(className);
	}

	/**
	 * return this node queue
	 * @return
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * set thie node queue
	 * @param queue
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}
}
