package com.hp.idc.itsm.workflow;

import org.dom4j.Element;

import com.hp.idc.itsm.configure.FormManager;

/**
 * 表示流程节点到节点间的动作信息
 * @author 梅园
 *
 */
public class ActionInfo {	
	
	
	public static final String TYPE_ACCEPT = "acceptDeal";
	public static final String TYPE_AUTOFLOW = "autoFlow";
	
	/**
	 * 存储结束节点ID
	 */
	protected String toNodeId;
	
	protected String toNodePath;
	
	/**
	 * 存储执行动作名称
	 */
	protected String actionName;
	
	//动作ID	
	protected String actionId;
	
	//动作组，在分支开始节点连出去的动作，可分组，组之间是多选一的，组内的动作是都必须处理的，
	//即处理了其中一个组里的动作，其他组的动作将不能操作
	protected String groupId;
	
	/**
	 * 存储动作的描述
	 */
	protected String desc;
	
	/**
	 * 存储是否允许多分派
	 */
	protected boolean mutiAssign; 
	
	/**
	 * 存储动作相关的表单ID
	 */
	protected String formId;
	
	/**
	 * 转发表单，如果为空，取formId
	 */
	protected String transmitFormId;
	
	/**
	 * 存储动作所属的结点
	 */
	protected NodeInfo owner;
	
	/**
	 * 展示模板路径
	 */
	protected String template;
	
	/**
	 * 节点配置信息
	 */
	protected Element node;
	
	protected boolean displayAhead;
	
	//表单配置方式，支持调用远程页面，值为:local/remote
	protected String formSelectMode = "";
	//远端处理页面
	protected String remoteDealPage = "";
	//远端转单页面
	protected String remoteViewPage = "";


	public boolean isDisplayAhead() {
		return displayAhead;
	}

	public void setDisplayAhead(boolean displayAhead) {
		this.displayAhead = displayAhead;
	}

	/**
	 * 获取关联的表单oid
	 * @return 返回关联的表单oid
	 */
	public String getFormId() {
		return this.formId;
	}

	/**
	 * 设置关联的表单oid
	 * @param formId 关联的表单oid
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * 根据所属流程节点和XML数据创建新的ActionInfo对象
	 * @param owner 所属流程节点
	 * @param node XML配置数据
	 * @return 生成的ActionInfo对象
	 */
	static public ActionInfo parse(NodeInfo owner, Element node) {
		ActionInfo d = new ActionInfo();
		d.node = node;
		d.owner = owner;
		d.actionId = node.attributeValue("id");
		d.groupId = node.attributeValue("groupId");
		d.actionName = node.attributeValue("name");
		String fId_ = node.attributeValue("formId");
		if (fId_ == null)
			d.formId = "-1";
		else {
			if (d.owner.owner.forms.containsKey(fId_))
				d.formId = fId_;
			else{
				int formOid = 0;
				try {
					formOid = Integer.parseInt(fId_);
					if (FormManager.getFormByOid(formOid) == null)
						formOid = 0;
				}catch(Exception e){
					e.printStackTrace();
				}
				d.formId = formOid+"";
				node.addAttribute("formId", d.formId);
			}
		}

		String ftId_ = node.attributeValue("transmitFormId");
		if (ftId_ == null)
			d.transmitFormId = "-1";
		else {
			if (d.owner.owner.forms.containsKey(ftId_))
				d.transmitFormId = ftId_;
			else{
				int formOid = -1;
				try {
					formOid = Integer.parseInt(ftId_);
					if (FormManager.getFormByOid(formOid) == null)
						formOid = -1;
				}catch(Exception e){
					e.printStackTrace();
				}
				d.transmitFormId = formOid+"";
			}
		}
		
		d.desc = node.attributeValue("desc");
		if (d.desc == null)
			d.desc = "";
		d.toNodeId = node.attributeValue("to");
		d.mutiAssign = "true".equals(node.attributeValue("mutiAssign"));
		d.displayAhead = "true".equals(node.attributeValue("displayAhead"));
		String formSelectMode = node.attributeValue("selectFormMode");
		if (formSelectMode==null || formSelectMode.equals(""))
			formSelectMode = "local";
		d.formSelectMode = formSelectMode;
		
		Element n = (Element)node.selectSingleNode("./template");
		if (n != null) {
			d.template = n.getText();
			if (d.template == null){
				d.template = "/default/nodeAction.html";
			}
		} else
			d.template = "/default/nodeAction.html";
		
		n = (Element)node.selectSingleNode("./remotePage/deal");
		if (n != null) {
			d.remoteDealPage = n.getText();
		}
		
		n = (Element)node.selectSingleNode("./remotePage/view");
		if (n != null) {
			d.remoteViewPage = n.getText();
		}
		
		owner.getActions().add(d);
		return d;
	}
	
	public String getTemplate() {
		return template;
	}
	
	public String getAttribute(String attr){
		if (node==null)
			return null;
		return node.attributeValue(attr);
	}

	/**
	 * 获取是否允许多分配
	 * @return 返回是否允许多分配
	 */
	public boolean isMutiAssign() {
		return this.mutiAssign;
	}

	/**
	 * 设置是否允许多分配
	 * @param mutiAssign 是否允许多分配
	 */
	public void setMutiAssign(boolean mutiAssign) {
		this.mutiAssign = mutiAssign;
	}

	/**
	 * 获取动作名称
	 * @return 返回动作名称
	 */
	public String getActionName() {
		return this.actionName;
	}

	/**
	 * 设置动作名称
	 * @param actionName 动作名称
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the actionId
	 */
	public String getActionId() {
		return actionId == null?"":actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId == null?"":groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * 获取目标节点
	 * @return 返回目标节点
	 */
	public NodeInfo getToNode() {
		return this.owner.owner.getNode(this.toNodeId);
	}
	/**
	 * 获取目标节点ID
	 * @return 返回目标节点ID
	 */
	public String getToNodeId() {
		return this.toNodeId;
	}

	/**
	 * 设置目标节点ID
	 * @param toNode 目标节点ID
	 */
	public void setToNodeId(String toNode) {
		this.toNodeId = toNode;
	}	
	
	
	/**
	 * 返回真实的目标节点。（如果toNode是子流程节点，则获取子流程的首节点)
	 * @return
	 */
	public NodeInfo getSubToNode()  {
		return getToNode().getSubNode();
	}
	/**
	 * 返回真实的目标节点Id。（如果toNode是子流程节点，则获取子流程的首节点Id)
	 * @return
	 */
	public String getSubToNodeId()  {
		return getSubToNode().getId();
	}

	
	/**
	 * 获取目标节点ID
	 * @return 返回目标节点ID
	 */
	public String getToNodePath() {
//		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.owner.owner.getWorkflowOid());
//		WorkflowData wfData = wfInfo.getVersion(this.owner.owner.getVersionId());
//		NodeInfo ni_ = wfData.getNode(this.toNodeId);
//		StringBuffer subNodePath = new StringBuffer();
//		ni_.getSubNodePath(subNodePath);
//		return subNodePath.toString();
		return this.toNodePath == null?"":this.toNodePath;
	}

	/**
	 * 设置目标节点的节点路径，是通过taskData+nodeinfo页面调用的时候算出来的
	 * @param toNodePath
	 */
	public void setToNodePath(String toNodePath) {
		this.toNodePath = toNodePath;
	}
	
	/**
	 * 获取动作的描述
	 * @return 返回动作的描述
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * 设置动作的描述
	 * @param desc 动作的描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTransmitFormId() {
		return transmitFormId;
	}

	public void setTransmitFormId(String transmitFormId) {
		this.transmitFormId = transmitFormId;
	}
	
	public String getFormSelectMode() {
		return (formSelectMode == null || formSelectMode.equals(""))?"local":formSelectMode;
	}

	public void setFormSelectMode(String formSelectMode) {
		this.formSelectMode = formSelectMode;
	}

	public String getRemoteDealPage() {
		return remoteDealPage;
	}

	public void setRemoteDealPage(String remoteDealPage) {
		this.remoteDealPage = remoteDealPage;
	}
	
	
	public String getRemoteViewPage() {
		return remoteViewPage;
	}

	public void setRemoteViewPage(String remoteViewPage) {
		this.remoteViewPage = remoteViewPage;
	}

}
