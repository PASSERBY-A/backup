package com.hp.idc.itsm.workflow;

import org.dom4j.Element;

import com.hp.idc.itsm.configure.FormManager;

/**
 * ��ʾ���̽ڵ㵽�ڵ��Ķ�����Ϣ
 * @author ÷԰
 *
 */
public class ActionInfo {	
	
	
	public static final String TYPE_ACCEPT = "acceptDeal";
	public static final String TYPE_AUTOFLOW = "autoFlow";
	
	/**
	 * �洢�����ڵ�ID
	 */
	protected String toNodeId;
	
	protected String toNodePath;
	
	/**
	 * �洢ִ�ж�������
	 */
	protected String actionName;
	
	//����ID	
	protected String actionId;
	
	//�����飬�ڷ�֧��ʼ�ڵ�����ȥ�Ķ������ɷ��飬��֮���Ƕ�ѡһ�ģ����ڵĶ����Ƕ����봦��ģ�
	//������������һ������Ķ�����������Ķ��������ܲ���
	protected String groupId;
	
	/**
	 * �洢����������
	 */
	protected String desc;
	
	/**
	 * �洢�Ƿ���������
	 */
	protected boolean mutiAssign; 
	
	/**
	 * �洢������صı�ID
	 */
	protected String formId;
	
	/**
	 * ת���������Ϊ�գ�ȡformId
	 */
	protected String transmitFormId;
	
	/**
	 * �洢���������Ľ��
	 */
	protected NodeInfo owner;
	
	/**
	 * չʾģ��·��
	 */
	protected String template;
	
	/**
	 * �ڵ�������Ϣ
	 */
	protected Element node;
	
	protected boolean displayAhead;
	
	//�����÷�ʽ��֧�ֵ���Զ��ҳ�棬ֵΪ:local/remote
	protected String formSelectMode = "";
	//Զ�˴���ҳ��
	protected String remoteDealPage = "";
	//Զ��ת��ҳ��
	protected String remoteViewPage = "";


	public boolean isDisplayAhead() {
		return displayAhead;
	}

	public void setDisplayAhead(boolean displayAhead) {
		this.displayAhead = displayAhead;
	}

	/**
	 * ��ȡ�����ı�oid
	 * @return ���ع����ı�oid
	 */
	public String getFormId() {
		return this.formId;
	}

	/**
	 * ���ù����ı�oid
	 * @param formId �����ı�oid
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * �����������̽ڵ��XML���ݴ����µ�ActionInfo����
	 * @param owner �������̽ڵ�
	 * @param node XML��������
	 * @return ���ɵ�ActionInfo����
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
	 * ��ȡ�Ƿ���������
	 * @return �����Ƿ���������
	 */
	public boolean isMutiAssign() {
		return this.mutiAssign;
	}

	/**
	 * �����Ƿ���������
	 * @param mutiAssign �Ƿ���������
	 */
	public void setMutiAssign(boolean mutiAssign) {
		this.mutiAssign = mutiAssign;
	}

	/**
	 * ��ȡ��������
	 * @return ���ض�������
	 */
	public String getActionName() {
		return this.actionName;
	}

	/**
	 * ���ö�������
	 * @param actionName ��������
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
	 * ��ȡĿ��ڵ�
	 * @return ����Ŀ��ڵ�
	 */
	public NodeInfo getToNode() {
		return this.owner.owner.getNode(this.toNodeId);
	}
	/**
	 * ��ȡĿ��ڵ�ID
	 * @return ����Ŀ��ڵ�ID
	 */
	public String getToNodeId() {
		return this.toNodeId;
	}

	/**
	 * ����Ŀ��ڵ�ID
	 * @param toNode Ŀ��ڵ�ID
	 */
	public void setToNodeId(String toNode) {
		this.toNodeId = toNode;
	}	
	
	
	/**
	 * ������ʵ��Ŀ��ڵ㡣�����toNode�������̽ڵ㣬���ȡ�����̵��׽ڵ�)
	 * @return
	 */
	public NodeInfo getSubToNode()  {
		return getToNode().getSubNode();
	}
	/**
	 * ������ʵ��Ŀ��ڵ�Id�������toNode�������̽ڵ㣬���ȡ�����̵��׽ڵ�Id)
	 * @return
	 */
	public String getSubToNodeId()  {
		return getSubToNode().getId();
	}

	
	/**
	 * ��ȡĿ��ڵ�ID
	 * @return ����Ŀ��ڵ�ID
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
	 * ����Ŀ��ڵ�Ľڵ�·������ͨ��taskData+nodeinfoҳ����õ�ʱ���������
	 * @param toNodePath
	 */
	public void setToNodePath(String toNodePath) {
		this.toNodePath = toNodePath;
	}
	
	/**
	 * ��ȡ����������
	 * @return ���ض���������
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * ���ö���������
	 * @param desc ����������
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
