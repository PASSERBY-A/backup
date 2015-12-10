package com.hp.idc.itsm.task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.security.OrganizationInfo;
import com.hp.idc.itsm.security.OrganizationManager;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.WorkgroupInfo;
import com.hp.idc.itsm.security.WorkgroupManager;
import com.hp.idc.itsm.util.XmlUtil;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * 事件信息类
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class TaskInfo {
	
	/**
	 * ID
	 */
	private int oid = -1;
	
	/**
	 * 祖记录OID
	 */
	private int rootParent = -1;
	
	/**
	 * 父OID，有分支的时候使用,当是第一层分支的时候，等于rootParent
	 */
	private int parentOid = -1;

	//流程ID
	private int wfOid;
	//流程版本
	private int wfVer;
	
	//父流程id，有子流程的时候使用
	private int pwfOid = -1;
	//父流程版本，有子流程的时候使用
	private int pwfVer = -1;
	
	//状态
	private int status;

	public static int STATUS_OPEN = 0;
	public static int STATUS_CLOSE_TEMP = 3;
	public static int STATUS_CLOSE = 1;
	public static int STATUS_FORCE_CLOSE = 2;
	

	//创建时间
	private long createTime = -1;
	//创建人
	private String createdBy;
	
	//相关人员 /user1/user2/
	private String relations;
	
	//最后更新时间
	private long lastUpdate;
	//最后更新人
	private String lastUpdateBy;
	
	//是否是新建还未入库的
	private boolean newTaskInfo = false;

	
	//XML 数据
	private String xmlData;
	
	//XML 数据
	private Document xmlDoc;
	
	/**
	 * 工单来源:“ITSM”:服务管理，“OVSD”:ovsd...
	 */
	private String origin = "ITSM";
	
	private boolean showHisGraphics = true;
	
	private String historyDataStr = "";
	
	private TaskData rootData;
	
	private String forceCloseMessage = "";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	/**
	 * 单分支的执行人
	 */
	private String user = "";
	
	private Map<String,String> values = new HashMap<String,String>();
	
	/**
	 * 关联的工单串，工单来源_流程OID_工单OID,...如："ITSM_1_23,OVSD_23_34,ITSM_2_34"
	 * 更新：更改为：工单来源_流程OID_流程Ver_工单OID,...如："ITSM_1_12_23,OVSD_23_0_34,ITSM_2_1_34"
	 */
	private String linkedTaskStr = "";
	
	/**
	 *  当前执行的节点
	 */
	private String taskNodeId;
	
	/**
	 * 第三方标识
	 */
	private String thirdID;
	
	public String getLinkedTaskStr() {
		return linkedTaskStr==null?"":linkedTaskStr;
	}

	public void setLinkedTaskStr(String linkedTaskStr) {
		this.linkedTaskStr = linkedTaskStr;
		this.values.put("TASK_LINKED", linkedTaskStr);
	}

	public List getMessages() {
		List l = new ArrayList();
		l = this.xmlDoc.getRootElement().selectNodes("./node/message");
		for (int i = 0; i < l.size(); i++) {
			Element n = (Element)l.get(i);
			TaskMessageInfo info = new TaskMessageInfo();
			info.setOperName(n.attributeValue("operName"));
			info.setDate(n.attributeValue("time"));
			info.setContent(n.getText());
			l.add(info);
		}
		return l;
	}
	
	public String getForceCloseMessage() {
		return forceCloseMessage;
	}

	public void setForceCloseMessage(String forceCloseMessage) {
		this.forceCloseMessage = forceCloseMessage;
		if (xmlDoc != null) {
			Element el = xmlDoc.getRootElement();
			el.addAttribute("forceClose", forceCloseMessage);
		}
	}

	public void setRootData(TaskData rootData) {
		this.rootData = rootData;
	}

	public TaskData getRootData() {
		return rootData;
	}

	public TaskData getTaskData(int dataId) {
		if (dataId == -1)
			return null;
		Stack<TaskData> s = new Stack<TaskData>();
		s.push(rootData);
		while (s.size() > 0) {
			TaskData t = (TaskData)s.pop();
			if (t.getDataId() == dataId)
				return t;
			List<TaskData> l = t.getChilds();
			for (int i = 0; i < l.size(); i++)
				s.push(l.get(i));
		}
		return null;
	}
	
	public TaskInfo(){
		
	}
	
	public TaskInfo(WorkflowData info, String operName) throws DocumentException {
		long t = System.currentTimeMillis();
		setOid(-1);
		setCreateTime(t);
		setStatus(STATUS_OPEN);
		setWfOid(info.getWorkflowOid());
		setCreatedBy(operName);
		setRelations("/" + operName + "/");
		setLastUpdate(t);
		setWfVer(info.getVersionId());
		setParentOid(-1);
		setNewTaskInfo(true);
		setXmlData("<workflow/>");
		this.rootData = null;
	}
	
	public String getValue(String attriName){
		if (attriName.equalsIgnoreCase("STATUS") || attriName.equalsIgnoreCase("TASK_STATUS")) {
			if (status == STATUS_CLOSE) {
				return "关闭";
			} else if (status == STATUS_FORCE_CLOSE) {
				return "强制关闭";
			} else {
				WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.wfOid);
				WorkflowData wfData = wfInfo.getVersion(this.wfVer);
				NodeInfo nodeInfo = wfData.getNode(taskNodeId);
				if (nodeInfo!=null) {
					return nodeInfo.getCaption();
				}
			}
		}
		return (String)this.values.get(attriName);
	}
	
	public Map getValues(){
		if (status == STATUS_CLOSE) {
			this.values.put("TASK_USER", "-");
			this.values.put(Consts.FLD_EXECUTE_USER.toUpperCase(), "-");
			this.values.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(),"-");
			this.values.put("TASK_STATUS", "关闭");
			this.values.put("STATUS", "关闭");
		} else if (status == STATUS_FORCE_CLOSE) {
			this.values.put("TASK_USER", "-");
			this.values.put(Consts.FLD_EXECUTE_USER.toUpperCase(), "-");
			this.values.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(),"-");
			this.values.put("TASK_STATUS", "强制关闭");
			this.values.put("STATUS", "强制关闭");
		} else {
			WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.wfOid);
			WorkflowData wfData = wfInfo.getVersion(this.wfVer);
			NodeInfo nodeInfo = wfData.getNode(taskNodeId);
			if (nodeInfo!=null) {
				this.values.put("TASK_STATUS",nodeInfo.getCaption());
				this.values.put("STATUS", nodeInfo.getCaption());
			}
		}
		return this.values;
	}
	
	public void addValue(String key,String value){
		this.values.put(key, value);
	}
	
	public void addAllValue(Map e){
		this.values.putAll(e);
	}
	
	public void parse() throws ParseException {
		Element el = xmlDoc.getRootElement();
		this.forceCloseMessage = el.attributeValue("forceClose");
		this.thirdID = el.attributeValue("thirdId");
		Element r = (Element)el.selectSingleNode("./node[@parentId='-1']");
		if(r == null)
			return ;
		rootData = new TaskData(this, null, r);
		Stack s = new Stack();
		s.push(rootData);
		while (s.size() > 0) {
			TaskData p = (TaskData)s.pop();
			List l = el.selectNodes("./node[@parentId='" + p.getDataId() + "']");
			if (l == null)
				continue;
			for (int i = 0; i < l.size(); i++) {
				r = (Element)l.get(i);
				TaskData d = new TaskData(this, p, r);
				s.push(d);
			}
		}
	}
	
	/**
	 * 获取taskdata的新ID（调用前不存在）
	 * @return
	 */
	public int getNewId() {
		Element el = xmlDoc.getRootElement();
		String id = el.attributeValue("seqId");
		if (id == null) {
			el.addAttribute("seqId", "1");
			return 0;
		}
		int r = Integer.parseInt(id);
		el.addAttribute("seqId", "" + (r + 1));		
		return r;
	}
	
	/**
	 * 获取最后一个taskDataID（存在的最后一个）
	 * @return
	 */
	public int getLatestTaskDataId() {
		Element el = xmlDoc.getRootElement();
		String id = el.attributeValue("seqId");
		if (id == null)
			return -1;
		int r = Integer.parseInt(id);
		return r-1;
	}
	
	/**
	 * 获取流程节点对应的最后一个数据节点。
	 * @param nodeId 流程节点ID
	 * @return 数据节点,找不到返回NULL
	 */
	public TaskData getLastTaskData(String nodeId){
		TaskData lastTaskData = getTaskData(getLatestTaskDataId());
		while(lastTaskData!=null) {
			if (lastTaskData.getNodeId().equals(nodeId))
				break;
			lastTaskData = lastTaskData.getParent();
		}
		return lastTaskData;
	}
	
	/**
	 * get the taskData by the nodeId
	 * @param nodeId nodeId
	 * @return 
	 */
	public List<TaskData> getTaskData(String nodeId){
		List<TaskData> l = new ArrayList<TaskData>();
		for (int i=0;i<this.getLatestTaskDataId();i++) {
			TaskData td = this.getTaskData(i);
			if (td == null) {
				continue;
			}
			if (td.isRollback())
				continue;
			if (td.getStatus() == TaskData.STATUS_EDIT)
				continue;
			if (td.getNodeId().equals(nodeId)) {
				l.add(td);
			}
		}
		return l;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		this.values.put("TASK_CREATE_BY", createdBy);
		this.values.put("CREATE_BY", createdBy);
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		this.values.put("TASK_CREATE_TIME", sdf.format(new Date(createTime)));
		this.values.put("CREATE_TIME", sdf.format(new Date(createTime)));
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
		this.values.put("TASK_UPDATE_TIME", sdf.format(new Date(lastUpdate)));
		this.values.put("UPDATE_TIME", sdf.format(new Date(lastUpdate)));
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
		this.values.put("TASK_OID", oid+"");
		this.values.put("OID", oid+"");
	}

	public String getRelations() {
		return relations;
	}

	public void setRelations(String relations) {
		this.relations = relations;
		this.values.put("TASK_RELATIONS", relations);
	}

	public void addRelation(String user) {
		if (relations == null || relations.length() == 0)
			relations = "/" + user + "/";
		else {
			if (relations.indexOf("/" + user + "/") == -1)
				relations += user + "/";
		}
		this.values.put("TASK_RELATIONS", relations);
	}

	public int getStatus() {
		if (status == STATUS_CLOSE || status == STATUS_FORCE_CLOSE)
			return status;

		Stack s = new Stack();
		if (rootData != null)
			s.push(rootData);
		else
			return status;
		while (s.size() > 0) {
			TaskData d = (TaskData)s.pop();
			if (d.getStatus() == TaskData.STATUS_OPEN || d.getStatus() == TaskData.STATUS_WAITING)
				return STATUS_OPEN;
			List l = d.getChilds();
			for (int i = 0; i < l.size(); i++)
				s.push(l.get(i));
		}
	
		return STATUS_CLOSE;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getWfOid() {
		return wfOid;
	}

	public void setWfOid(int wfOid) {
		this.wfOid = wfOid;
		this.values.put("TASK_WF_OID", wfOid+"");
		this.values.put("WORKFLOW", WorkflowManager.getWorkflowByOid(wfOid).getName());
		this.values.put("TASK_WF_NAME", WorkflowManager.getWorkflowByOid(wfOid).getName());
	}

	public int getWfVer() {
		return wfVer;
	}

	public void setWfVer(int wfVer) {
		this.wfVer = wfVer;
		this.values.put("TASK_WF_VER", wfVer+"");
	}

	public Document getXmlDoc() {
		return xmlDoc;
	}

	public void setXmlDoc(Document dataD) {
		this.xmlDoc = dataD;
		this.xmlData = dataD.asXML();
	}

	public String getXmlData() {
		return xmlDoc.asXML();
	}

	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(xmlData);
		setXmlDoc(doc);
	}
	
	/**
	 * 返回历史流程字符串
	 * @return 如果要有历史结点图，则showHisGraphics=true,返回xmlDoc的xml串，否则，返回historyDataStr
	 */
	public String getHistoryStr(){
		if (this.showHisGraphics)
			return xmlDoc.asXML();
		else
			return this.historyDataStr;
	}
	
	/**
	 * 获取此分支的执行人
	 * @return
	 */
	public String getUser() {
		return this.user;
	}
	
	public void setUser(String user) {
		if (user.startsWith("/"))
			user = user.substring(1);
		if (user.endsWith("/"))
			user = user.substring(0,user.length()-1);
		this.user = user;
		this.values.put("TASK_USER", user);
		this.values.put(Consts.FLD_EXECUTE_USER.toUpperCase(), user);
		
		String execute_group = "";
		CIInfo info = CIManager.getCIById(user);
		if (info != null) {
			List l = new ArrayList();
			if (info instanceof PersonInfo) {
				execute_group = "/" + user + "/";
			} else{
				if (info instanceof WorkgroupInfo) {
					WorkgroupInfoInterface wginfo = WorkgroupManager.getWorkgroupById(user);
					if (wginfo != null)
						l = wginfo.getPersons(true);
				} else if (info instanceof OrganizationInfo){
					OrganizationInfoInterface oinfo = OrganizationManager.getOrganizationById(user);
					if (oinfo != null)
						l = oinfo.getPersons(true);
				}
				StringBuffer sb = new StringBuffer();
				sb.append("/");
				for (int i = 0; i < l.size(); i++) {
					sb.append(((CIInfo)l.get(i)).getId());
					sb.append("/");
				}
				execute_group = sb.toString();
			}
		}
		this.values.put(Consts.FLD_EXECUTE_GROUP_USER.toUpperCase(), execute_group);
	}

	/**
	 * 获取所有分支的执行人
	 * @return
	 */
	public String getUsers() {
		String ret = "/";
		Stack s = new Stack();
		if (rootData != null)
			s.push(rootData);
		while (s.size() > 0) {
			TaskData d = (TaskData)s.pop();
			if (d.getStatus() == TaskData.STATUS_OPEN
				&& d.getAssignType() == TaskData.ASSIGN_PERSON
				&& ret.indexOf("/" + d.getAssignTo() + "/") == -1)
				ret += d.getAssignTo() + "/";
			List l = d.getChilds();
			for (int i = 0; i < l.size(); i++)
				s.push(l.get(i));
		}
		return ret;
	}
	
	/**
	 * 获取所有可操作的数据节点,打开的/等待的/预关闭的/需要阅知的
	 * @return
	 */
	public List<TaskData> getTaskData() {
		List<TaskData> l = new ArrayList<TaskData>();
		if (rootData != null)
			getTaskDataInternal(l, rootData);
		return l;
	}
	
	public void getTaskDataInternal(List<TaskData> l, TaskData d) {
		List<TaskData> childs = d.getChilds();
		if (d.getStatus() == TaskData.STATUS_OPEN || d.getStatus() == TaskData.STATUS_WAITING ||d.getStatus() == TaskData.STATUS_PRE_CLOSE)
			l.add(d);
		if(d.getStatus() == TaskData.STATUS_CLOSE && d.getReadUser()!=null && !d.getReadUser().equals(""))
			l.add(d);

		for (int j = 0; j < childs.size(); j++) {
			d = (TaskData)childs.get(j);
			getTaskDataInternal(l, d);
		}
	}
	
	public List<TaskData> getNewAdded() {
		List<TaskData> l = new ArrayList<TaskData>();
		if (rootData != null)
			getNewAddData(l, rootData);
		return l;
	}
	
	private void getNewAddData(List<TaskData> l, TaskData d) {
		List<TaskData> childs = d.getChilds();
		if (d.isNewAdded())
			l.add(d);

		for (int j = 0; j < childs.size(); j++) {
			d = (TaskData)childs.get(j);
			getNewAddData(l, d);
		}
	}
	
	/**
	 * @deprecated 参看TaskData.getAllData();
	 * @return
	 */
	public Map getAllFieldData() {
		Map retMap = new HashMap();
		Element el = xmlDoc.getRootElement();
		List fieldDataL = el.selectNodes("./node/fields/field");
		for (int i = 0; i < fieldDataL.size(); i++) {
			Element en = (Element)fieldDataL.get(i);
			retMap.put(en.attributeValue("id"), en.getText());
		}
		return retMap;
	}
	
	public List getAllTaskData() {
		List retList = new ArrayList();
		Stack s = new Stack();
		s.push(rootData);
		while (s.size() > 0) {
			TaskData t = (TaskData)s.pop();
			retList.add(t);
			List l = t.getChilds();
			for (int i = 0; i < l.size(); i++)
				s.push(l.get(i));
		}
		return retList;
	}
	
	public TaskInfo cloneInfo() throws DocumentException, ParseException{
		TaskInfo retInfo = new TaskInfo();
		retInfo.setOid(this.getOid());
		retInfo.setCreateTime(this.getCreateTime());
		retInfo.setWfOid(this.getWfOid());
		retInfo.setWfVer(this.getWfVer());
		retInfo.setParentOid(parentOid);
		retInfo.setStatus(this.getStatus());
		retInfo.setCreatedBy(this.getCreatedBy());
		retInfo.setRelations(this.getRelations());
		retInfo.setLastUpdate(this.getLastUpdate());
		retInfo.setOrigin(this.getOrigin());
		retInfo.setLastUpdateBy(this.getLastUpdateBy());
		retInfo.setLinkedTaskStr(this.getLinkedTaskStr());
		retInfo.setTaskNodeId(this.getTaskNodeId());
		retInfo.setUser(this.getUser());
		retInfo.setXmlData(this.getXmlDoc().asXML());
		retInfo.parse();
		return retInfo;

	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
		this.values.put("TASK_ORIGIN",this.origin);
	}

	public int getParentOid() {
		return parentOid;
	}

	public void setParentOid(int parentOid) {
		this.parentOid = parentOid;
		this.values.put("TASK_PARENT_OID", parentOid+"");
	}
	
	/**
	 * 获取关联的Task
	 * @return List[TaskInfo,....]
	 */
	public List getLinkTask(){
		List retList = new ArrayList();
		if (this.linkedTaskStr == null || this.linkedTaskStr.equals("")) {
			return retList;
		} else {
			retList = TaskManager.getLinkedTask(linkedTaskStr);
		}
		return retList;
	}

	public int getRootParent() {
		return rootParent;
	}

	public void setRootParent(int rootParent) {
		this.rootParent = rootParent;
	}
	
	public List getTaskBranch() throws IOException, DocumentException, ParseException, SQLException{
		List retList = new ArrayList();
		retList = TaskManager.getTaskBranch(this);
		return retList;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
		this.values.put("TASK_UPDATE_BY", lastUpdateBy);
	}
	
	/**
	 * （对打开的工单有用）获取主工单
	 * @return
	 */
	public TaskInfo getParent(){
		return (TaskInfo)Cache.Tasks.get(this.origin+"_"+this.parentOid);
	}

	public String getTaskNodeId() {
		return taskNodeId;
	}

	public void setTaskNodeId(String taskNodeId) {
		this.taskNodeId = taskNodeId;
		this.values.put("TASK_NODE_ID", taskNodeId);
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(this.wfOid);
		WorkflowData wfData = wfInfo.getVersion(this.wfVer);
		NodeInfo nodeInfo = wfData.getNode(taskNodeId);
		if (nodeInfo!=null)
		this.values.put("NODE_DESC",nodeInfo.getCaption());
	}

	public boolean isShowHisGraphics() {
		return showHisGraphics;
	}

	public void setShowHisGraphics(boolean showHisGraphics) {
		this.showHisGraphics = showHisGraphics;
	}

	public String getHistoryDataStr() {
		return historyDataStr;
	}

	public void setHistoryDataStr(String historyDataStr) {
		this.historyDataStr = historyDataStr;
	}

	public int getPwfOid() {
		return pwfOid;
	}

	public void setPwfOid(int pwfOid) {
		this.pwfOid = pwfOid;
	}

	public int getPwfVer() {
		return pwfVer;
	}

	public void setPwfVer(int pwfVer) {
		this.pwfVer = pwfVer;
	}

	public String getThirdID() {
		return thirdID == null?"":thirdID;
	}

	public void setThirdID(String thirdID) {
		this.thirdID = thirdID;
		if (xmlDoc != null) {
			Element el = xmlDoc.getRootElement();
			el.addAttribute("thirdId", thirdID);
		}
	}

	/**
	 * @return the newTaskInfo
	 */
	public boolean isNewTaskInfo() {
		return newTaskInfo;
	}

	/**
	 * @param newTaskInfo the newTaskInfo to set
	 */
	public void setNewTaskInfo(boolean newTaskInfo) {
		this.newTaskInfo = newTaskInfo;
	}
}
