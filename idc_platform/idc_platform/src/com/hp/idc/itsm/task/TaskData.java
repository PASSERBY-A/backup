package com.hp.idc.itsm.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Element;
import org.dom4j.Node;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.IObjectWithAttribute;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.security.LocalgroupInfo;
import com.hp.idc.itsm.security.LocalgroupManager;
import com.hp.idc.itsm.security.OrganizationManager;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.security.WorkgroupManager;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.workflow.ActionInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

public class TaskData implements IObjectWithAttribute {
	/*
	 * <node id="node1" dataId="1" status="0" parentId="-1" rollback="true"
	 * actureParentId="-1" createBy="root" lastUpdate="20070101000000"
	 * assignTo="root" assignType="0"> <fields> <field id="3">23</field> <field
	 * id="7">sfdf</field> <field id="6">on</field> <field id="1">12</field>
	 * </fields> </node>
	 */
	public static int STATUS_OPEN = 0;

	public static int STATUS_CLOSE = 1;

	public static int STATUS_EDIT = 2;// 保存待编辑状态，流程不流转

	public static int STATUS_WAITING = 3;// 等待其他工单的处理

	/**
	 * 分支开始节点的预关闭状态,分支开始节点处，在所有分支都结束前，处于此状态。分支全部正常 结束后，变为STATUS_CLOSE
	 */
	public static int STATUS_PRE_CLOSE = 4;

	/**
	 * 强制关闭，在分支里，分支开始处人员可对分支内部进行强制关闭操作
	 */
	public static int STATUS_FORCE_CLOSE = 5;

	// -----------Assign_type--------
	public static int ASSIGN_PERSON = 0; // 分配到人

	public static int ASSIGN_WORKGROUP = 1; // 分配到工作组

	public static int ASSIGN_ORGANIZATION = 2; // 分配到组织

	public static int ASSIGN_LOCALGROUP = 3; // 分配到角色

	private TaskInfo owner;

	private Element xmlNode;

	private String nodeId;//节点ID
	
	private String actId;//执行的哪个动作ID

	private String subNodeId = "";

	private int dataId;

	private TaskData parent;

	private TaskData actureParent;

	private List<TaskData> childs = new ArrayList<TaskData>();

	private Date lastUpdate;

	private Date createTime;

	private String createBy;

	// 原定处理人
	private String assignTo;

	// 带路径的处理人（哪个组下哪个人）
	// 当选择不属于任何一个组的人员的时候，assignTo=assignPath
	private String assignPath;

	// 协作者，当此属性有了值后，同assignTo一样也可以处理此工单。多个人员中间以逗号“,”分割
	private String assistant;

	// 阅知人，或者叫抄送人，如果值不为空，则会受到一条待阅工单，只能填写阅知信息。多个人员中间以逗号“,”分割
	private String readUser;

	private int assignType = ASSIGN_PERSON;

	// 处理人，拥有超级权限（流程里面的配置）的人，可以处理别人的工单
	private String dealedBy;

	private String nodeDesc;

	private Map<String, String> values = new HashMap<String, String>();

	private boolean rollback;

	private int status = STATUS_OPEN;

	private boolean branchEnd = false;

	private boolean branchBegin = false;

	// 标识taskData是否是新加的
	private boolean newAdded = false;

	// 标识taskData是否分记录存放
	private boolean newBranch = false;

	private List<TaskRollbackInfo> rollbacks = new ArrayList<TaskRollbackInfo>();

	private List<TaskMessageInfo> messages = new ArrayList<TaskMessageInfo>();

	private Map<String, TaskMessageInfo> readMessages = new HashMap<String, TaskMessageInfo>();

	protected boolean overtime = false;

	protected long smsRemindTime = 0;

	/**
	 * 真实的任务OID，有分支、子流程的时候，是另开记录存储的，xml节点数据全部存在主记录里面， 分支记录不存xml节点数据
	 */
	protected int actually_task_oid = -1;

	protected TaskWaitingInfo waitInfo;

	/**
	 * 真实的流程Oid，当有子流程的时候，存储真实流程id
	 */
	protected int actuallyWorkflowOid = -1;
	protected int actuallyWorkflowVer = -1;
	
	/**
	 * task wait, when user has the max task num that he set, 
	 * the other task will wait
	 */
	protected boolean wait = false;
	
	/**
	 * the count of sending the sms about this taskData
	 */
	protected int smsRemindCount = 0;

	public TaskData(TaskInfo owner, TaskData parent) {
		if (parent != null)
			parent.addChild(this);
		this.owner = owner;
		Element el = owner.getXmlDoc().getRootElement();
		xmlNode = el.addElement("node");
		this.setParent(parent);
	}

	public TaskData(TaskInfo owner, TaskData parent, Element xmlNode) throws ParseException {
		if (parent != null)
			parent.addChild(this);
		this.owner = owner;
		this.xmlNode = xmlNode;
		this.parent = parent;
		List l = xmlNode.selectNodes("./fields/field");
		for (int i = 0; i < l.size(); i++) {
			Element n = (Element) l.get(i);
			values.put(n.attributeValue("id"), n.getText());
			owner.addValue(n.attributeValue("id"), n.getText());
		}

		// 如果真实的任务OID为空，默认为是主记录的OID
		String ataskOid = xmlNode.attributeValue("actTaskOid");
		if (ataskOid == null || ataskOid.equals("") || ataskOid.equals("-1"))
			this.actually_task_oid = -1;
		else
			this.actually_task_oid = Integer.parseInt(ataskOid);

		String awfOid = xmlNode.attributeValue("actuallyWorkflowOid");
		if (awfOid != null && !awfOid.equals("") && !awfOid.equals("-1"))
			this.actuallyWorkflowOid = Integer.parseInt(awfOid);

		String awfVer = xmlNode.attributeValue("actuallyWorkflowVer");
		if (awfVer != null && !awfVer.equals("") && !awfVer.equals("-1"))
			this.actuallyWorkflowVer = Integer.parseInt(awfVer);

		this.subNodeId = xmlNode.attributeValue("subNodeId");
		this.nodeId = xmlNode.attributeValue("id");
		this.actId = xmlNode.attributeValue("actId");
		this.subNodeId = xmlNode.attributeValue("subNodeId");
		this.dataId = Integer.parseInt(xmlNode.attributeValue("dataId"));
		this.lastUpdate = DateTimeUtil.parseDate(xmlNode.attributeValue("lastUpdate"));
		String str = xmlNode.attributeValue("createTime");
		if (str == null)
			this.createTime = this.lastUpdate;
		else
			this.createTime = DateTimeUtil.parseDate(str);
		this.assignType = StringUtil.parseInt(xmlNode.attributeValue("assignType"), ASSIGN_PERSON);
		this.createBy = xmlNode.attributeValue("createBy");

		this.assignPath = xmlNode.attributeValue("assignTo");
		int pos = assignPath.lastIndexOf("/");
		if (pos == -1) {
			this.assignTo = assignPath;
		} else {
			this.assignTo = assignPath.substring(pos + 1);
		}

		this.assistant = xmlNode.attributeValue("assistant");
		this.readUser = xmlNode.attributeValue("readUser");
		this.dealedBy = xmlNode.attributeValue("dealedBy");
		this.smsRemindTime = StringUtil.parseLong(xmlNode.attributeValue("smsRemindTime"), 0);
		this.nodeDesc = xmlNode.attributeValue("nodeDesc");
		this.rollback = "true".equals(xmlNode.attributeValue("rollback"));
		this.overtime = "true".equals(xmlNode.attributeValue("overtime"));
		this.status = Integer.parseInt(xmlNode.attributeValue("status"));
		this.actureParent = owner.getTaskData(Integer.parseInt(xmlNode.attributeValue("actureParentId")));
		this.branchEnd = "true".equals(xmlNode.attributeValue("branchEnd"));
		this.branchBegin = "true".equals(xmlNode.attributeValue("branchBegin"));
		this.wait = "true".equals(xmlNode.attributeValue("wait"));
		this.smsRemindCount = StringUtil.parseInt(xmlNode.attributeValue("smsRemindCount"), 0);
		
		// 等待信息
		Element waiteNode = (Element) xmlNode.selectSingleNode("./waiteInfo");
		if (waiteNode != null) {
			this.waitInfo = new TaskWaitingInfo();

			this.waitInfo.setWaiting_task_oid(Integer.parseInt(waiteNode.attributeValue("waiteTaskOid")));
			this.waitInfo.setWaiting_end_status(waiteNode.attributeValue("waiteStatus"));
			this.waitInfo.setWfOid(Integer.parseInt(waiteNode.attributeValue("wfOid")));
			this.waitInfo.setWfVer(Integer.parseInt(waiteNode.attributeValue("wfVer")));

		}
		l = xmlNode.selectNodes("./rollback");
		for (int i = 0; i < l.size(); i++) {
			Element n = (Element) l.get(i);
			int dataId = Integer.parseInt(n.attributeValue("dataId"));
			String time = n.attributeValue("time");
			String reason = n.getText();
			int actureDataId = -1;
			String adidStr_ = n.attributeValue("actureDataId");
			if (adidStr_ == null || adidStr_.equals(""))
				actureDataId = dataId;
			else
				actureDataId = Integer.parseInt(adidStr_);
			addRollbackMsgToCache(dataId, actureDataId, reason, time);
		}

		l = xmlNode.selectNodes("./message");
		for (int i = 0; i < l.size(); i++) {
			Element n = (Element) l.get(i);
			String operName = n.attributeValue("operName");
			String time = n.attributeValue("time");
			String msg = n.getText();
			addMsgToCache(msg, operName, time);
		}

		// 读取阅知信息
		l = xmlNode.selectNodes("./readMessage");
		for (int i = 0; i < l.size(); i++) {
			Element n = (Element) l.get(i);
			String operName = n.attributeValue("operName");
			String time = n.attributeValue("time");
			String msg = n.getText();
			addReadMsgToCache(msg, operName, time);
		}
	}

	public TaskData(TaskInfo owner, TaskData parent, NodeInfo node, String actionId, String assignTo,
			String operName) {
		initTask(owner, parent, node,actionId, assignTo, "", operName);
	}

	public TaskData(TaskInfo owner, TaskData parent, NodeInfo node, String actionId, String assignTo,
			String assistant, String operName) {
		initTask(owner, parent, node,actionId, assignTo, assistant, operName);
	}

	private void initTask(TaskInfo owner, TaskData parent, NodeInfo node, String actionId, String assignTo,
			String assistant, String operName) {
		this.owner = owner;
		Element el = owner.getXmlDoc().getRootElement();
		xmlNode = el.addElement("node");
		if (node.getType() == NodeInfo.TYPE_BRANCH_BEGIN)
			setBranchBegin(true);

		if (parent != null) {
			parent.addChild(this);
			parent.setDealedBy(operName);
		}
		this.setNodeId(node.getId());
		this.setActId(actionId);
		this.setNewAdded(true);
		this.setDataId(owner.getNewId());
		this.setLastUpdate(new Date());
		this.setParent(parent);
		this.setCreateBy(operName);
		this.setCreateTime(new Date());
		this.setAssignTo(assignTo);
		this.setAssistant(assistant);
		this.setActureParent(parent);
		this.setRollback(false);
		this.setStatus(STATUS_OPEN);
	}

	public int getActually_task_oid() {
		return actually_task_oid;
	}

	public void setActually_task_oid(int actually_task_oid) {
		this.actually_task_oid = actually_task_oid;
		xmlNode.addAttribute("actTaskOid", "" + actually_task_oid);
	}

	public String getOrigin() {
		if (this.owner != null)
			return this.owner.getOrigin();
		return null;
	}

	/**
	 * 获取smsRemindTime
	 * 
	 * @return 返回smsRemindTime
	 */
	public long getSmsRemindTime() {
		if (smsRemindTime <= 0)
			return this.getCreateTime().getTime();
		return smsRemindTime;
	}

	/**
	 * 设置smsRemindTime
	 * 
	 * @param smsRemindTime
	 *            smsRemindTime
	 */
	public void setSmsRemindTime(long smsRemindTime) {
		this.smsRemindTime = smsRemindTime;
		xmlNode.addAttribute("smsRemindTime", "" + smsRemindTime);
	}
	
	/**
	 * get the smsRemindCount
	 * @return
	 */
	public int getSmsRemindCount() {
		return smsRemindCount;
	}

	/**
	 * set the smsRemindCount
	 * @param smsRemindCount
	 */
	public void setSmsRemindCount(int smsRemindCount) {
		this.smsRemindCount = smsRemindCount;
		xmlNode.addAttribute("smsRemindCount", "" + smsRemindCount);
	}

	public boolean isOvertime() {
		return this.overtime;
	}

	public void setOvertime() {
		this.overtime = true;
		xmlNode.addAttribute("overtime", "true");
	}

	public List<TaskRollbackInfo> getRollbacks() {
		return rollbacks;
	}

	public List<TaskMessageInfo> getMessages() {
		return messages;
	}

	public Map<String, TaskMessageInfo> getReadMessages() {
		return readMessages;
	}

	public boolean isRollback() {
		return rollback;
	}

	public boolean isEditing() {
		if (status == STATUS_EDIT)
			return true;
		else
			return false;
	}

	public void setRollback(boolean rollback) {
		this.rollback = rollback;
		if (rollback)
			xmlNode.addAttribute("rollback", "true");
		else
			xmlNode.addAttribute("rollback", "false");
	}

	public boolean isBranchEnd() {
		return branchEnd;
	}

	public void setBranchEnd(boolean branchEnd) {
		this.branchEnd = branchEnd;
		if (branchEnd)
			xmlNode.addAttribute("branchEnd", "true");
		else
			xmlNode.addAttribute("branchEnd", "false");
	}

	/**
	 * 设置字段
	 * 
	 * @param val
	 */
	public void setValue(Map<String, String> val) {
		this.values = val;
	}

	/**
	 * 添加字段值，保存入values（Map），同时更新xml对象
	 * 
	 * @param id
	 * @param val
	 */
	public void setValue(String id, String val) {
		values.put(id, val);
		Element el = (Element) xmlNode.selectSingleNode("./fields");
		if (el == null)
			el = xmlNode.addElement("fields");
		Element el2 = (Element) el.selectSingleNode("./field[@id='" + id + "']");
		if (el2 == null) {
			el2 = el.addElement("field");
			el2.addAttribute("id", id);
		}
		el2.setText(val);
	}

	/**
	 * 只加value值入Map，不更新xml对象，（为了ovsd接口格式化数据的速度）
	 * 
	 * @param id
	 * @param val
	 */
	public void setValueVirtual(String id, String val) {
		values.put(id, val);
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getNodeDesc() {
		return nodeDesc == null ? "" : nodeDesc;
	}

	/**
	 * 获取本节点的字段值
	 * 
	 * @param id
	 * @return
	 */
	public String getValue(String id) {
		Object ret = values.get(id);
		return ret == null ? "" : (String) ret;
	}

	/**
	 * 返回当前数据节点的所有表单字段值
	 * 
	 * @return
	 */
	public Map<String, String> getValues() {
		return values;
	}

	/**
	 * 获取历史最近一个分支下的各个分支同字段的所有值 分支外的不获取，本身是分支开始的不算
	 * 
	 * @param id
	 *            字段id
	 * @return List<String>
	 */
	public List<String> getAllBranchValue(String id) {
		List<String> retList = new ArrayList<String>();
		TaskData rd = this;
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.owner.getWfOid());
		WorkflowData wfData = wfInfo.getVersion(this.owner.getWfVer());
		NodeInfo node = wfData.getNode(rd.getNodeId());
		if (node.getType() == NodeInfo.TYPE_BRANCH_BEGIN) {
			if (rd.getParent() != null) {
				rd = rd.getParent();
				node = wfData.getNode(rd.getNodeId());
			} else
				return retList;
		}
		// 取最近的一个历史分支开始节点，如果没有分支返回根节点
		while (node.getType() != NodeInfo.TYPE_BRANCH_BEGIN) {
			if (rd.getParent() != null)
				rd = rd.getParent();
			else
				break;
			node = wfData.getNode(rd.getNodeId());
		}
		Stack<TaskData> s = new Stack<TaskData>();
		s.push(rd);
		while (s.size() > 0) {
			TaskData td = (TaskData) s.pop();
			String v = td.getValue(id);
			if (v != null && !v.equals(""))
				retList.add(v);
			// 如果达到分支结束则返回，分支外的不获取
			if (td.isBranchEnd())
				continue;
			List<TaskData> l = td.getChilds();
			for (int i = 0; i < l.size(); i++)
				s.push(l.get(i));
		}
		return retList;
	}
	

	/**
	 * 获取节点的字段值，如果本节点没有，则，递归查询历史的父节点
	 */
	public String getAttribute(String id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (id.equalsIgnoreCase(Consts.FLD_EXECUTE_USER)) {
			return PersonManager.getLocalId(getOrigin(), getAssignTo());
		}
		if (id.equalsIgnoreCase(Consts.FLD_EXECUTE_GROUP_USER)) {
			if (assignType == ASSIGN_PERSON) {
				// System.out.println("/" + getAssignTo() + "/");
				return "/" + PersonManager.getLocalId(getOrigin(), getAssignTo()) + "/";
			}
			List l = new ArrayList();
			if (assignType == ASSIGN_WORKGROUP) {
				WorkgroupInfoInterface info = WorkgroupManager.getWorkgroupById(this.getOwner().getOrigin(),
						this.assignTo);
				if (info != null)
					l = info.getPersons(true);
			} else if (assignType == ASSIGN_ORGANIZATION) {
				OrganizationInfoInterface info = OrganizationManager.getOrganizationById(this.getOwner()
						.getOrigin(), this.assignTo);
				if (info != null)
					l = info.getPersons(true);
			} else if (assignType == ASSIGN_LOCALGROUP) {
				LocalgroupInfo lgInfo = LocalgroupManager.getGroupById(this.assignTo);
				if (lgInfo != null)
					l = LocalgroupManager.getPersonsOfLocalgroup(lgInfo.getId(), false);
			}
			StringBuffer sb = new StringBuffer();
			sb.append("/");
			for (int i = 0; i < l.size(); i++) {
				String pId = ((PersonInfoInterface) l.get(i)).getId();
				sb.append(PersonManager.getLocalId(getOrigin(), pId));
				sb.append("/");
			}
			// System.out.println(sb.toString());
			return sb.toString();
		}
		if (id.equalsIgnoreCase("overtime"))
			return this.overtime ? "true" : "false";
		if (id.equalsIgnoreCase("origin"))
			return owner.getOrigin();
		if (id.equalsIgnoreCase("workflow_oid") || id.equalsIgnoreCase("task_wf_oid"))
			return owner.getWfOid() + "";
		if (id.equalsIgnoreCase("workflow") || id.equalsIgnoreCase("workflow_name")
				|| id.equalsIgnoreCase("task_wf_name"))
			return WorkflowManager.getWorkflowByOid(owner.getWfOid()).getName();
		if (id.equalsIgnoreCase("workflow_node"))
			return this.getNodeDesc();
		if (id.equalsIgnoreCase("task_relations"))
			return owner.getRelations();
		if (id.equalsIgnoreCase("create_time") || id.equalsIgnoreCase("task_create_time"))
			return sdf.format(new java.util.Date(owner.getCreateTime()));
		if (id.equalsIgnoreCase("create_by") || id.equalsIgnoreCase("task_create_by"))
			return owner.getCreatedBy();
		if (id.equalsIgnoreCase("oid") || id.equalsIgnoreCase("task_oid"))
			return owner.getOid() + "";
		if (id.equalsIgnoreCase("dataId"))
			return dataId + "";
		if (id.equalsIgnoreCase("status") || id.equalsIgnoreCase("task_status")) {
			if (owner.getStatus() == TaskInfo.STATUS_CLOSE)
				return "关闭";
			if (owner.getStatus() == TaskInfo.STATUS_FORCE_CLOSE)
				return "强制关闭";
			return nodeDesc;
		}
		if (id.equalsIgnoreCase("update_time"))
			return sdf.format(getLastUpdate());
		Object obj = values.get(id);
		if (obj == null) {
			if (actureParent == null)
				return "";
			return actureParent.getAttribute(id);
		}
		return obj.toString();
	}

	public void addChild(TaskData c) {
		childs.add(c);
	}

	public List getChilds() {
		return childs;
	}

	/**
	 * 获取指定nodeID的子节点，用于保存草稿不派单，给下次编辑使用。
	 * 
	 * @param nodeId
	 * @return
	 */
	public TaskData getChildEditing(String nodeId) {
		for (int i = 0; i < childs.size(); i++) {
			TaskData c = (TaskData) childs.get(i);
			if (c.getStatus() == TaskData.STATUS_EDIT && c.getNodeId().equals(nodeId))
				return c;
		}
		return null;
	}

	/**
	 * 获取所有编辑状态的子节点
	 * 
	 * @return
	 */
	public List<TaskData> getChildEditing() {
		List<TaskData> ret = new ArrayList<TaskData>();
		for (int i = 0; i < childs.size(); i++) {
			TaskData c = (TaskData) childs.get(i);
			if (c.getStatus() == TaskData.STATUS_EDIT) {
				ret.add(c);
			}
		}
		return ret;
	}

	public void removeChildEditing(String nodeId) {
		for (int i = 0; i < childs.size(); i++) {
			TaskData c = (TaskData) childs.get(i);
			if (c.getStatus() == TaskData.STATUS_EDIT) {
				if (nodeId == null || c.getNodeId().equals(nodeId)) {
					childs.remove(i);
					Element el = owner.getXmlDoc().getRootElement();
					el.remove(c.getXmlNode());
					i--;
				}
			}
		}
	}

	/**
	 * 移除指定的子节点
	 * 
	 * @param dataId
	 */
	public void removeChild(int dataId) {
		for (int i = 0; i < childs.size(); i++) {
			TaskData c = (TaskData) childs.get(i);
			if (c.getDataId() == dataId) {
				childs.remove(i);
				Element el = owner.getXmlDoc().getRootElement();
				el.remove(c.getXmlNode());
				if (childs.size() == 0)
					setStatus(TaskData.STATUS_OPEN);
				break;
			}
		}
	}

	public int getDataId() {
		return dataId;
	}

	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @return the actId
	 */
	public String getActId() {
		return actId == null?"":actId;
	}

	/**
	 * @param actId the actId to set
	 */
	public void setActId(String actId) {
		this.actId = actId;
		xmlNode.addAttribute("actId", actId);
	}

	public String getNodePath() {
		if (this.subNodeId != null && this.subNodeId.length() > 0)
			return this.nodeId + "/" + this.subNodeId;
		else
			return this.nodeId;

	}

	public TaskInfo getOwner() {
		return owner;
	}

	public Node getXmlNode() {
		return xmlNode;
	}

	public TaskData getParent() {
		return parent;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
		xmlNode.addAttribute("dataId", "" + dataId);
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
		xmlNode.addAttribute("lastUpdate", DateTimeUtil.formatDate(lastUpdate));
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
		xmlNode.addAttribute("id", nodeId);
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
		xmlNode.addAttribute("nodeDesc", nodeDesc);
	}

	public void setParent(TaskData parent) {
		this.parent = parent;
		if (parent == null)
			xmlNode.addAttribute("parentId", "-1");
		else
			xmlNode.addAttribute("parentId", "" + parent.getDataId());
	}

	/**
	 * @deprecated
	 * @param createBy
	 */
	public void setCreateByBy(String createBy) {
		this.createBy = createBy;
		xmlNode.addAttribute("createBy", createBy);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
		xmlNode.addAttribute("createBy", createBy);
	}

	/**
	 * 取不带路径的执行人串 如：root
	 * 
	 * @return
	 */
	public String getAssignTo() {
		return assignTo;
	}

	/**
	 * 获取带路径的执行人串 如：/group1/subgroup1/root
	 * 
	 * @return
	 */
	public String getAssignPath() {
		return assignPath;
	}

	/**
	 * 把传过来的assignTo当作是路径来看待，路径中最后一个/后的串就是执行人 如果路径中不包含/，则整个串就是执行人
	 * 
	 * @param assignPath
	 *            执行人路径
	 */
	public void setAssignTo(String assignPath) {
		this.assignPath = assignPath;
		int pos = assignPath.lastIndexOf("/");
		if (pos == -1) {
			this.assignTo = assignPath;
		} else {
			this.assignTo = assignPath.substring(pos + 1);
		}
		this.owner.addRelation(this.assignTo);
		// CIInfo info = CIManager.getCIById(assignTo);
		CIInfo info = PersonManager.getPersonById(getOrigin(), assignTo);
		xmlNode.addAttribute("assignTo", assignPath);

		// 保存草稿时，处理人不放进历史处理人串中，故把此语句放在创建此Taskdata的外层
		// owner.addRelation(assignTo);
		if (info != null) {
			this.setAssignType(ASSIGN_PERSON);
		} else {
			info = WorkgroupManager.getWorkgroupById(getOrigin(), assignTo);
			if (info != null)
				this.setAssignType(ASSIGN_WORKGROUP);
			else {
				info = OrganizationManager.getOrganizationById(getOrigin(), assignTo);
				if (info != null)
					this.setAssignType(ASSIGN_ORGANIZATION);
				else {
					info = LocalgroupManager.getGroupById(assignTo);
					if (info != null)
						this.setAssignType(ASSIGN_LOCALGROUP);
				}
			}
		}
		// if (info != null) {
		// if (info instanceof PersonInfo) {
		// this.setAssignType(ASSIGN_PERSON);
		// } else if (info instanceof WorkgroupInfo)
		// this.setAssignType(ASSIGN_WORKGROUP);
		// else if (info instanceof OrganizationInfo)
		// this.setAssignType(ASSIGN_ORGANIZATION);
		// }
	}

	public TaskData getActureParent() {
		return actureParent;
	}

	public void setActureParent(TaskData actureParent) {
		this.actureParent = actureParent;
		if (actureParent == null)
			xmlNode.addAttribute("actureParentId", "-1");
		else
			xmlNode.addAttribute("actureParentId", "" + actureParent.getDataId());
	}

	public int getStatus() {
		return status;
	}

	public String getTitle() {
		return getAttribute("title");
	}

	public void setStatus(int status) {
		this.status = status;
		xmlNode.addAttribute("status", "" + status);
	}

	/**
	 * 获取本节点开始的所有没有子节点的节点
	 * @param l
	 */
	public void getLastData(List l) {
		int n = 0;
		for (int j = 0; j < childs.size(); j++) {
			TaskData d = (TaskData) childs.get(j);
			if (d.isRollback())
				continue;
			n++;
			// 放在外部过滤处理
			// if (d.getChilds().size()==0){
			// if (d.isBranchEnd()) // 跳过已处理完成的分支结束结点
			// continue;
			// }
			d.getLastData(l);
		}
		if (n == 0) {
			l.add(this);
			return;
		}
	}

	public boolean isForwardable() {
		NodeInfo node = getWorkflowNode();
		if (node == null)
			return false;
		return node.isForwardable();
	}

	protected NodeInfo getWorkflowNode() {
		int wfOid = this.getOwner().getWfOid();
		int wfVer = this.getOwner().getWfVer();
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		if (wfInfo == null)
			return null;
		WorkflowData wfData = wfInfo.getVersion(wfVer);
		if (wfData == null)
			return null;
		return wfData.getNode(this.getNodeId());
	}

	public boolean isRollbackable() {
		if (this.parent == null)
			return false;
		NodeInfo node = getWorkflowNode();
		if (node == null)
			return false;
		if (node == null || !node.isRollbackable())
			return false;
		for (int j = 0; j < childs.size(); j++) {
			TaskData d = (TaskData) childs.get(j);
			if (d.isRollback() == false)
				return false;
		}
		return true;
	}

	// 取分支节点的动作，还需要做的动作(包含已做但mutiassign为true的)
	// 如果节点走遍了，那么返回 true, jsp 上要加上关闭不再分配的按钮
	public boolean getNextAction(List list, WorkflowData workflow) {
		list.clear();
		NodeInfo fromNode = workflow.getNode(getNodeId());
		if (fromNode.getType() != NodeInfo.TYPE_BRANCH_BEGIN)
			return false;
		String groupId = "";
		List actions = null;
		if (childs.size()>0){
			TaskData d_ =null;
			for (int j = 0; j < childs.size(); j++) {
				TaskData d = (TaskData) childs.get(j);
				if (d.rollback == true || d.status == STATUS_EDIT)
					continue;
				d_ = d;
				break;
			}
			if (d_ == null)
				actions = fromNode.getActions();
			else {
				ActionInfo ai_ = fromNode.getAction(d_.getNodeId());
				if (ai_!=null)
					groupId = ai_.getGroupId();
				actions = fromNode.getActions(groupId);
			}
		} else {
			actions = fromNode.getActions();
		}
		boolean ret = true;
		for (int i = 0; i < actions.size(); i++) {
			ActionInfo action = (ActionInfo) actions.get(i);
			boolean found = false;
			for (int j = 0; j < childs.size(); j++) {
				TaskData d = (TaskData) childs.get(j);
				if (d.rollback == true || d.status == STATUS_EDIT)
					continue;
				if (action.getToNodeId().equals(d.getNodeId())) {
					found = true;
					break;
				}
			}
			if (found == false)
				ret = false;
			if (action.isMutiAssign() || found == false)
				list.add(action);
		}
		return ret;
	}
	
	/**
	 * 判断分支开始数据节点是否可以预关闭
	 * @param workflow
	 * @return
	 */
	public boolean branchBeginPreClose(WorkflowData workflow){
		NodeInfo fromNode = workflow.getNode(getNodeId());
		if (fromNode.getType() != NodeInfo.TYPE_BRANCH_BEGIN)
			return false;
		if (childs.size() >= 1) {
			TaskData d = (TaskData) childs.get(0);
			ActionInfo ai_ = fromNode.getAction(d.getNodeId());
			String groupId = "";
			if (ai_!=null)
				groupId = ai_.getGroupId();
			List actions = fromNode.getActions(groupId);
			if (actions.size() == 1) {
				ActionInfo action = (ActionInfo) actions.get(0);
				if (action.isMutiAssign() == false)
					return false;
				else
					return true;
			} else
				return true;
			
		}
		return false;
	}
	

	/**
	 * 递归找子流程里的当前处理节点下的动作，支持子流程里再次嵌套子流程
	 * 
	 * @param wfData
	 * @param nodePath
	 *            当前TaskData所属流程的节点路径，假如，流程A里有节点anode
	 *            关联子流程B，流程B里有节点bnode关联流程C。如果此TaskData属于流程C的节点cnode，则
	 *            nodePath="anode/bnode/cnode"
	 * @param retList
	 * @return
	 */
	public boolean getSubflowActions(WorkflowData wfData, String nodePath, Stack subNodePath, List retList) {
		boolean ret = false;

		nodePath = nodePath == null ? "" : nodePath;
		String nodeIdStr = "";
		String nodeIdSub = "";
		if (nodePath.indexOf("/") == -1)
			nodeIdStr = nodePath;
		else {
			nodeIdStr = nodePath.substring(0, nodePath.indexOf("/"));
			nodeIdSub = nodePath.substring(nodePath.indexOf("/") + 1);
		}
		NodeInfo nodeInfo = wfData.getNode(nodeIdStr);
		if (nodeInfo == null)
			nodeInfo = wfData.getRootNode();

		if (subNodePath == null)
			subNodePath = new Stack();
		subNodePath.push(nodeInfo.getId());
		if (nodeInfo.getType() == NodeInfo.TYPE_SUB_WF) {
			WorkflowInfo subwfInfo = WorkflowManager.getWorkflowByOid(nodeInfo.getSubflow());
			WorkflowData subwfData = subwfInfo.getVersion(nodeInfo.getSubflowVer());
			ret = getSubflowActions(subwfData, nodeIdSub, subNodePath, retList);
			if (retList.size() == 0) {
				retList.addAll(nodeInfo.getActions());
				subNodePath.pop();
			}
		} else if (nodeInfo.getType() == NodeInfo.TYPE_BRANCH_BEGIN) {
			ret = getNextAction(retList, wfData);
		} else {
			retList.addAll(nodeInfo.getActions());
		}
		for (int i = 0; i < retList.size(); i++) {
			ActionInfo ai_ = (ActionInfo) retList.get(i);
			subNodePath.pop();
			subNodePath.push(ai_.getToNodeId());
			// NodeInfo ni_ = ai_.getToNode();
			// if (ni_.getType() == NodeInfo.TYPE_SUB_WF) {
			// subNodePath.push(ai_.getSubToNodeId());
			// ai_.setToNodePath(getStringFromStack('/',subNodePath));
			// subNodePath.pop();
			// } else
			ai_.setToNodePath(getStringFromStack('/', subNodePath));
		}
		return ret;
	}

	private String getStringFromStack(char separator, Stack s) {
		if (s == null)
			return "";
		String retStr = "";
		for (int i = 0; i < s.size(); i++) {
			if (i > 0)
				retStr += separator;
			retStr += s.get(i);
		}
		return retStr;
	}

	public void addMessage(String msg, String operName) {
		if (operName == null || operName.equals(""))
			return;
		Element el = xmlNode.addElement("message");
		String time = DateTimeUtil.formatDate(System.currentTimeMillis());
		el.addAttribute("time", time);
		el.addAttribute("operName", operName);
		if (msg == null)
			msg = "";
		el.setText(msg);
		addMsgToCache(msg, operName, time);
	}

	private void addMsgToCache(String msg, String operName, String time) {
		TaskMessageInfo info = new TaskMessageInfo();
		info.setOperName(operName);
		info.setDate(time);
		info.setContent(msg);
		messages.add(info);
	}

	public void addRollbackMessage(int actureDataId, int dataId, String msg) {
		Element el = xmlNode.addElement("rollback");
		String time = DateTimeUtil.formatDate(System.currentTimeMillis());
		el.addAttribute("time", time);
		el.addAttribute("dataId", "" + dataId);
		el.addAttribute("actureDataId", "" + actureDataId);
		if (msg == null)
			msg = "";
		el.setText(msg);
		addRollbackMsgToCache(dataId, actureDataId, msg, time);
	}

	private void addRollbackMsgToCache(int dataId, int actureDataId, String reason, String time) {
		TaskRollbackInfo info = new TaskRollbackInfo();
		info.setDataId(dataId);
		info.setDate(time);
		info.setReason(reason);
		info.setActureDataId(actureDataId);
		rollbacks.add(info);
	}

	/**
	 * 增加阅知信息
	 */
	public void addReadMessage(String msg, String operName) {
		if (operName == null || operName.equals(""))
			return;
		Element el = xmlNode.addElement("readMessage");
		String time = DateTimeUtil.formatDate(System.currentTimeMillis());
		el.addAttribute("time", time);
		el.addAttribute("operName", operName);
		if (msg == null)
			msg = "";
		el.setText(msg);
		addReadMsgToCache(msg, operName, time);
	}

	private void addReadMsgToCache(String msg, String operName, String time) {
		TaskMessageInfo info = new TaskMessageInfo();
		info.setOperName(operName);
		info.setDate(time);
		info.setContent(msg);
		readMessages.put(operName, info);
	}

	/**
	 * 获取分支结束的数据节点（TaskData）
	 * 
	 * @param l
	 *            存放获取到的数据节点
	 * @param workflow
	 * @param level
	 */
	public void getBranchEnd(List<TaskData> l, WorkflowData workflow, int level) {
		NodeInfo node = workflow.getNode(getNodeId());
		if (node.getType() == NodeInfo.TYPE_BRANCH_BEGIN)
			level++;
		if (node.getType() == NodeInfo.TYPE_BRANCH_END) {
			level--;
			if (this.isRollback() == false && level == 0) {
				l.add(this);
				return;
			}
		}
		for (int j = 0; j < childs.size(); j++) {
			TaskData d = (TaskData) childs.get(j);
			d.getBranchEnd(l, workflow, level);
		}
	}

	/**
	 * 获取分支开始的数据节点TaskData
	 * 
	 * @param workflow
	 * @return
	 * @throws Exception
	 */
	public TaskData getBranchBegin(WorkflowData workflow) throws Exception {
		TaskData b = this;
		int adj = 1;
		for (;;) {
			b = b.getParent();
			if (b == null)
				return null;
			int t = workflow.getNode(b.getNodeId()).getType();
			if (t == NodeInfo.TYPE_BRANCH_END)
				adj++;
			else if (t == NodeInfo.TYPE_BRANCH_BEGIN) {
				adj--;
				if (adj == 0)
					break;
			}
		}
		return b;
	}

	/**
	 * 回退处理方法
	 * 
	 * @param msg
	 * @param workflow
	 * @param operName
	 * @throws Exception
	 */
	public void rollback(String msg, WorkflowData workflow, String operName) throws Exception {
		setRollback(true);
		setStatus(TaskData.STATUS_CLOSE);

		List<TaskData> l = new ArrayList<TaskData>();
		TaskData parent = getParent();
		NodeInfo node = workflow.getNode(parent.getNodeId());

		// 如果父节点是分支结束节点，则查找所有分支
		if (node.getType() == NodeInfo.TYPE_BRANCH_END) {
			TaskData b = TaskManager.getBranchBegin(parent, workflow);
			b.getBranchEnd(l, workflow, 0);
		} else {
			l.add(parent);
		}

		// 所有分支均做回退处理
		for (int i = 0; i < l.size(); i++) {
			TaskData d = (TaskData) l.get(i);
			d.setBranchEnd(false);
			NodeInfo node_ = workflow.getNode(d.getNodeId());
			d.addRollbackMessage(this.getDataId(), this.getDataId(), msg);
			d.setStatus(TaskData.STATUS_OPEN);

			// 如果是分支结束节点，则继续回退一层，因为分支结束节点是程序自动处理的。
			if (node_.getType() == NodeInfo.TYPE_BRANCH_END) {
				d.rollback(msg, workflow, operName);
			}

		}

		// 执行当前节点的onRoolBack方法。
		NodeInfo currentNode = workflow.getNode(this.nodeId);
		TaskUpdateInfo tuInfo = new TaskUpdateInfo();
		tuInfo.setTaskData(this);
		tuInfo.setToNode(currentNode);
		tuInfo.setToNodePath(currentNode.getId());
		tuInfo.setOperName(operName);
		tuInfo.setWorkflow(workflow);
		tuInfo.setMap(new HashMap<String,String>());
		tuInfo.setTaskInfo(this.owner);
		currentNode.onRollback(tuInfo);
	}

	/**
	 * 获取assignType
	 * 
	 * @return 返回assignType
	 */
	public int getAssignType() {
		return assignType;
	}

	/**
	 * 设置assignType
	 * 
	 * @param assignType
	 *            assignType
	 */
	public void setAssignType(int assignType) {
		this.assignType = assignType;
		xmlNode.addAttribute("assignType", "" + assignType);
	}

	/**
	 * 获取此任务分支的所有历史填写的字段信息
	 * 
	 * @return
	 */
	public Map<String, String> getAllFieldData() {
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.putAll(this.values);
		if (this.parent != null) {
			Map<String, String> pMap = this.parent.getAllFieldData();
			// 以新值为准
			for (String pk : pMap.keySet()) {
				if (!retMap.containsKey(pk)) {
					retMap.put(pk, pMap.get(pk));
				}
			}
		}
		return retMap;
	}

	/**
	 * 返回所有的信息,包括{此任务分支的所有历史填写的字段信息,以及非字段信息}
	 * 
	 * @return
	 */
	public Map<String, String> getAllData() {
		Map<String, String> retMap = getAllFieldData();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		retMap.put("OVERTIME", this.overtime ? "true" : "false");
		retMap.put("TASK_WF_OID", owner.getWfOid() + "");
		retMap.put("TASK_WF_VER", owner.getWfVer() + "");
		retMap.put("TASK_WF_NAME", WorkflowManager.getWorkflowByOid(owner.getWfOid()).getName());
		// retMap.put("TASK_NODE_ID", getNodeId());
		// retMap.put("NODE_DESC",getNodeDesc());
		retMap.put("TASK_RELATIONS", owner.getRelations());
		retMap.put("TASK_CREATE_BY", owner.getCreatedBy());
		retMap.put("TASK_CREATE_TIME", sdf.format(new java.util.Date(owner.getCreateTime())));
		retMap.put("TASK_UPDATE_TIME", sdf.format(getLastUpdate()));
		retMap.put("TITLE", this.getAttribute("title"));
		retMap.put("TASK_OID", owner.getOid() + "");
		retMap.put("TASK_DATA_ID", dataId + "");
		retMap.put("TASK_ORIGIN", this.owner.getOrigin());
		retMap.put("TASK_DATA_STATUS", this.getStatus()+"");		

		String task_user = owner.getUsers();
		if (task_user.length() > 2) {
			task_user = task_user.substring(1, task_user.length() - 1);
			task_user = task_user.replaceAll("/", ",");
		}
		task_user = task_user.replaceAll("/", "-");
		retMap.put("TASK_USERS", task_user);

		if (owner.getStatus() == TaskInfo.STATUS_CLOSE) {
			retMap.put(Consts.FLD_EXECUTE_USER.toUpperCase(), "-");
			retMap.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(), "-");
			retMap.put("TASK_STATUS", "关闭");
		} else if (owner.getStatus() == TaskInfo.STATUS_FORCE_CLOSE) {
			retMap.put(Consts.FLD_EXECUTE_USER.toUpperCase(), "-");
			retMap.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(), "-");
			retMap.put("TASK_STATUS", "强制关闭");
		} else {
			if (this.assignType == TaskData.ASSIGN_PERSON) {
				String operator = PersonManager.getLocalId(this.getOrigin(), this.assignTo);
				if (this.assistant != null && !this.assistant.equals(""))
					operator += "," + this.assistant;
				retMap.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(), operator);
				retMap.put(Consts.FLD_EXECUTE_USER.toUpperCase(), operator);
			} else {
				String operator = this.assignTo;
				if (this.assistant != null && !this.assistant.equals(""))
					operator += "," + this.assistant;
				retMap.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(), operator);
				retMap.put(Consts.FLD_EXECUTE_USER.toUpperCase(), operator);
			}
			retMap.put("TASK_STATUS", nodeDesc);
			retMap.put("TASK_WAIT", String.valueOf(this.isWait()));
		}
		return retMap;
	}

	/**
	 * 获取非回退的子节点
	 * 
	 * @return List[TaskData,...]
	 */
	public List getActivateChilds() {
		List retList = new ArrayList();
		for (int i = 0; i < this.childs.size(); i++) {
			TaskData d = (TaskData) this.childs.get(i);
			if (d.rollback)
				continue;
			if (d.getStatus() == TaskData.STATUS_EDIT)
				continue;
			retList.add(d);
		}
		return retList;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
		xmlNode.addAttribute("createTime", DateTimeUtil.formatDate(createTime));
	}

	/**
	 * 判断当前用户是否有处理本节点的权限<br/>
	 * <br/>
	 * 1、判断是否是当前节点的处理人<br/>
	 * 2、判断是否是代理人<br/>
	 * 3、是否是超级处理权限<br/>
	 * 4、流程是否允许本组处理<br/>
	 * 5、是否是协助处理人<br/>
	 * 
	 * @param operName
	 * @return
	 */
	public boolean canDealThisNode(String operName) {
		if (this.status != TaskData.STATUS_OPEN && this.status != TaskData.STATUS_PRE_CLOSE)
			return false;
		if (operName == null || operName.equals(""))
			return false;
		String operetor = PersonManager.getLocalId(this.getOrigin(), this.assignTo);
		if (operetor.equals(operName))
			return true;
		if (PersonManager.isFactor(operetor, operName))
			return true;
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.owner.getWfOid());
		String editRule = wfInfo.getAnalyzedEditRule();
		if (editRule != null && !editRule.equals("") && editRule.indexOf("/" + operName + "/") != -1)
			return true;

		if (wfInfo.isAllowLocalGroup()) {
			String ps = getPersonsOfLocalGroup();
			if (ps.indexOf("/" + operName + "/") != -1)
				return true;
		}

		if (this.assistant != null) {
			if (this.assistant.equals(operName) || this.assistant.startsWith(operName + ",")
					|| this.assistant.endsWith("," + operName)
					|| this.assistant.indexOf("," + operName + ",") != -1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断对当前节点是否有阅知权限
	 * 
	 * @param operName
	 * @return
	 */
	public boolean canReadThisNode(String operName) {
		if (operName == null || operName.equals(""))
			return false;
		if (readMessages.get(operName) != null)
			return false;
		if (this.readUser != null) {
			if (this.readUser.equals(operName) || this.readUser.startsWith(operName + ",")
					|| this.readUser.endsWith("," + operName)
					|| this.readUser.indexOf("," + operName + ",") != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回本工单执行人所在组的人员（根据assignPath来判断）
	 * 
	 * @return
	 */
	public String getPersonsOfLocalGroup() {
		String ret = "";
		if (assignType == ASSIGN_WORKGROUP || assignType == ASSIGN_ORGANIZATION) {
			ret = this.getAttribute(Consts.FLD_EXECUTE_GROUP_USER);
		} else {
			int pos = this.assignPath.lastIndexOf("/");
			if (pos != -1) {
				// 取最后一级的goupId
				String groupPath = assignPath.substring(0, pos);
				int pos_ = groupPath.lastIndexOf("/");
				String subGroup = "";
				if (pos_ == -1)
					subGroup = groupPath;
				else {
					subGroup = groupPath.substring(pos_ + 1);
				}
				if (!subGroup.equals("")) {
					WorkgroupInfoInterface workgroupInfo = WorkgroupManager.getWorkgroupById(getOrigin(),
							subGroup);
					if (workgroupInfo != null) {
						List p = workgroupInfo.getPersons();
						for (int i = 0; i < p.size(); i++) {
							PersonInfoInterface pInfo = (PersonInfoInterface) p.get(i);
							ret += "/" + pInfo.getId();
						}
					} else {
						OrganizationInfoInterface organization = OrganizationManager.getOrganizationById(
								getOrigin(), subGroup);
						if (organization != null) {
							List p = organization.getPersons();
							for (int i = 0; i < p.size(); i++) {
								PersonInfoInterface pInfo = (PersonInfoInterface) p.get(i);
								ret += "/" + pInfo.getId();
							}
						}
					}
					ret += "/";
				}
			}
		}
		return ret;
	}

	public String getDealedBy() {
		return dealedBy;
	}

	public void setDealedBy(String dealedBy) {
		this.dealedBy = dealedBy;
		xmlNode.addAttribute("dealedBy", dealedBy);
	}

	public String getSubNodeId() {
		return subNodeId == null ? "" : subNodeId;
	}

	public void setSubNodeId(String subNodeId) {
		this.subNodeId = subNodeId;
		xmlNode.addAttribute("subNodeId", subNodeId);
	}

	public int getActuallyWorkflowOid() {
		return actuallyWorkflowOid;
	}

	public void setActuallyWorkflowOid(int actuallyWorkflowOid) {
		this.actuallyWorkflowOid = actuallyWorkflowOid;
		xmlNode.addAttribute("actuallyWorkflowOid", actuallyWorkflowOid + "");
	}

	public int getActuallyWorkflowVer() {
		return actuallyWorkflowVer;
	}

	public void setActuallyWorkflowVer(int actuallyWorkflowVer) {
		this.actuallyWorkflowVer = actuallyWorkflowVer;
		xmlNode.addAttribute("actuallyWorkflowVer", actuallyWorkflowVer + "");
	}

	/**
	 * 获取工单的协作者，如果多个，以逗号“,”分割
	 * 
	 * @return
	 */
	public String getAssistant() {
		return assistant;
	}

	/**
	 * 设置工单的协作人，如果多个，以逗号“,”分割
	 * 
	 * @param assistant
	 */
	public void setAssistant(String assistant) {
		this.assistant = assistant;
		xmlNode.addAttribute("assistant", assistant);
	}

	/**
	 * @return the readUser
	 */
	public String getReadUser() {
		return readUser;
	}

	/**
	 * @param readUser
	 *            the readUser to set
	 */
	public void setReadUser(String readUser) {
		this.readUser = readUser;
		xmlNode.addAttribute("readUser", readUser);
	}

	public boolean isBranchBegin() {
		return branchBegin;
	}

	public void setBranchBegin(boolean branchBegin) {
		this.branchBegin = branchBegin;
		if (branchBegin)
			xmlNode.addAttribute("branchBegin", "true");
		else
			xmlNode.addAttribute("branchBegin", "false");
	}

	public void setNewAdded(boolean newAdded) {
		this.newAdded = newAdded;
	}

	public boolean isNewAdded() {
		return newAdded;
	}

	public void setNewBranch(boolean newBranch) {
		this.newBranch = newBranch;
	}

	public boolean isNewBranch() {
		return newBranch;
	}

	public TaskWaitingInfo getWaitInfo() {
		return waitInfo;
	}

	public void setWaitStatus(TaskWaitingInfo waitInfo) {
		this.waitInfo = waitInfo;
		this.setStatus(TaskData.STATUS_WAITING);

		Element el = (Element) xmlNode.selectSingleNode("./waiteNode");
		if (el == null) {
			el = xmlNode.addElement("waiteNode");
		}
		el.addAttribute("waiteTaskOid", waitInfo.getWaiting_task_oid() + "");
		el.addAttribute("waiteStatus", waitInfo.getWaiting_end_status());
		el.addAttribute("wfOid", waitInfo.getWfOid() + "");
		el.addAttribute("wfVer", waitInfo.getWfVer() + "");
	}
	
	public void setWait(boolean wait) {
		this.wait = wait;
		xmlNode.addAttribute("wait", String.valueOf(wait));	
	}
	
	public boolean isWait(){
		return this.wait;
	}
}
