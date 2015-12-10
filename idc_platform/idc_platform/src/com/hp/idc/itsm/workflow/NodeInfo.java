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
 * ��ʾ���̽ڵ���Ϣ
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class NodeInfo {
	/**
	 * �ڵ�����:��ʾһ��ڵ�
	 */
	public static int TYPE_NODE = 0;

	/**
	 * �ڵ�����:��ʾ��֧��ʼ
	 */
	public static int TYPE_BRANCH_BEGIN = 1;

	/**
	 * �ڵ�����:��ʾ��֧����
	 */
	public static int TYPE_BRANCH_END = 2;

	/**
	 * �ڵ����ͣ������̽ڵ�
	 */
	public static int TYPE_SUB_WF = 3;

	/**
	 * �洢�ڵ�ID
	 */
	protected String id;

	/**
	 * �洢�ڵ�����
	 */
	protected String caption;

	/**
	 * �洢�����ı�ID
	 */
	protected String formId = -1 + "";

	/**
	 * ת���������Ϊ�գ�ȡ����ActionInfo��transmitFormId
	 */
	protected String transmitFormId;
	//ת��ģ��
	protected String transmitTemplate;

	/**
	 * �洢�ڵ�����
	 */
	protected int type;
	
	private boolean enableSMS = true;

	/**
	 * �洢�ڵ��Ƿ���Ի���
	 */
	protected boolean rollbackable;

	/**
	 * �洢�ڵ��Ƿ���Ա༭/ת��
	 */
	protected boolean forwardable;
	
	/**
	 * ����������������˽ڵ�ʱ���Ƿ���������֮����˽���Ԥ����
	 * ������д��Ϣ�󱣴�Ϊ�ݸ壬�����˴���˹���ʱ���ɿ����Ѿ���д�Ĵ�����Ϣ��
	 */
	protected boolean shareDeal;

	/**
	 * �洢�ڵ��������̶���
	 */
	protected WorkflowData owner;

	/**
	 * �洢�ڵ������еĶ����б�
	 */
	protected List<ActionInfo> actions = new ArrayList<ActionInfo>();

	/**
	 * �洢��̬���������
	 */
	private String className = "";

	/**
	 * �ڵ��ϵ����д������
	 */
	protected Map<String, AbstractRule> ruleList = new HashMap<String, AbstractRule>();


	/**
	 * ������id ���˽ڵ�Ϊ�����̽ڵ�ʱ��Ч
	 */
	protected String subflow = "";
	// �����̰汾
	protected String subflowVer = "";

	/**
	 * չʾģ��
	 */
	protected String template = "";

//	/**
//	 * �Զ������֪ͨ����List<String[noteTo,noteMessage]>
//	 */
//	protected List noticeList = new ArrayList();

	/**
	 * the task queue on this node: user1:5;user2:9;user3:10
	 */
	public String queue = "";
	/**
	 * ����Ŀ��ڵ�id���Ҷ�������
	 * 
	 * @param toNodeId Ŀ��ڵ�id
	 * @return �ҵ���ActionInfo����
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
	 * �����������̺�XML���ݴ����µ�NodeInfo����
	 * 
	 * @param owner
	 *            ��������
	 * @param node
	 *            XML��������
	 * @return ���ɵ�NodeInfo����
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
		// 2008-11-18���˸��£�this.autoFlowExeUser��ԭ���洢��text���棬��Ϊ�����ڵ�<executeUser/>�洢
		// * Ϊ�˱��ְ汾���ݣ������ж�����<executeUser/>�ڵ㣬���û�У���ȥ��n.getText();
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
		// if ("1".equals(e_.attributeValue("type")))//1:�������ģ�0:��������
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

		// ��������
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
	 * ��ȡ����Ŀ���
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
	 * ��ȡ��̬���������
	 * 
	 * @return ���ض�̬���������
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * ���ö�̬���������
	 * 
	 * @param className
	 *            ��̬���������
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * ��ȡ�ڵ�����
	 * 
	 * @return ���ؽڵ�����
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * ���ýڵ�����
	 * 
	 * @param caption
	 *            �ڵ�����
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * ��ȡ�����ı�ID
	 * 
	 * @return ���ع����ı�ID
	 */
	public String getFormId() {
		return this.formId;
	}

	/**
	 * ���ù����ı�ID
	 * 
	 * @param formId
	 *            �����ı�ID
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * ��ȡ�ڵ�ID
	 * 
	 * @return ���ؽڵ�ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ���ýڵ�ID
	 * 
	 * @param id
	 *            �ڵ�ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * �ڽڵ����ʱ���ô˺���
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
	 * �ڵ������ϴ����ݿ�֮ǰ
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

		//���͵������
//		System.out.println("==|||==:"+isEnableSMS()+"//��"+(this.owner.getOwner().isEnableSMS() && isEnableSMS()));
		if (this.owner.getOwner().isEnableSMS() && isEnableSMS()) {
			AbstractRule smsRule = getRule(SMSRule.class.getName());
			smsRule.execute(tuInfo, this, SMSRule.SMS_TYPE_ARRIVE);
		}
	}

	/**
	 * ���ڵ���ʾʱ���ô˺���
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
	 * ������ʱʱִ�еĺ���
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
		// ���ͳ�ʱ����
		if (this.owner.getOwner().isEnableSMS() && this.isEnableSMS()) {
			AbstractRule smsRule = getRule(SMSRule.class.getName());
			smsRule.execute(tuInfo, this, SMSRule.SMS_TYPE_OVERTIME);
		}

	}

	/**
	 * ����ʱִ��
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

		// ���ͻ��˶���
		if (this.owner.getOwner().isEnableSMS() && this.isEnableSMS()) {
			AbstractRule smsRule = getRule(SMSRule.class.getName());
			smsRule.execute(tuInfo, this, SMSRule.SMS_TYPE_ROOLBACK);
		}
	}
	
	/**
	 * ʵʱɨ��ִ�еķ���
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
	 * ��ȡ�ڵ�����
	 * 
	 * @see #TYPE_NODE
	 * @see #TYPE_BRANCH_BEGIN
	 * @see #TYPE_BRANCH_END
	 * @see #TYPE_SUB_WF
	 * @return ���ؽڵ�����
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * ���ýڵ�����
	 * 
	 * @see #TYPE_NODE
	 * @see #TYPE_BRANCH_BEGIN
	 * @see #TYPE_BRANCH_END
	 * @param type
	 *            �ڵ�����
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
	 * ��ȡ�ڵ������еĶ����б�
	 * 
	 * @return ���ؽڵ������еĶ����б�
	 */
	public List<ActionInfo> getActions() {
		return this.actions;
	}
	
	/**
	 * ��ȡͬ��Ķ����������ڷ�֧��ʼ
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
	 * ��ȡ�ڵ��������̶���
	 * 
	 * @return ���ؽڵ��������̶���
	 */
	public WorkflowData getOwner() {
		return this.owner;
	}

	public NodeInfo getBranchBeginNode() {
		NodeInfo rootNode = this.owner.getRootNode();
		// ������ջ
		Stack nodes = new Stack();
		// ��ʼ�ڵ��ջ
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
				if (visit.get(n.getId()) == null)// ������ʹ��Ľڵ㲻�ٷ���
					nodes.push(n);
			}
		}
		return null;
	}

	/**
	 * ��ȡ�ڵ��Ƿ���Ի���
	 * 
	 * @return ���ؽڵ��Ƿ���Ի���
	 */
	public boolean isRollbackable() {
		return this.rollbackable;
	}

	/**
	 * ���ýڵ��Ƿ���Ի���
	 * 
	 * @param canBack
	 *            �ڵ��Ƿ���Ի���
	 */
	public void setRollbackable(boolean canBack) {
		this.rollbackable = canBack;
	}

	/**
	 * ��ȡ�ڵ��Ƿ���Ա༭/ת��
	 * 
	 * @return ���ؽڵ��Ƿ���Ա༭/ת��
	 */
	public boolean isForwardable() {
		return this.forwardable;
	}

	/**
	 * ���ýڵ��Ƿ���Ա༭/ת��
	 * 
	 * @param forwardable
	 *            �ڵ��Ƿ���Ա༭/ת��
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
	 * ��ȡ��ʵ�ڵ�<br>
	 * 
	 * �����ǰ�ڵ��������̽ڵ㣬��Ҫ���������̵��׽ڵ㣬���ݹ�Ż������Ľڵ㡣
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
	 * �����ؽڵ���Ĭ�ϵĹ�������<br>
	 * 
	 * <pre>
	 * 	sms:���ŷ��͹���
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
