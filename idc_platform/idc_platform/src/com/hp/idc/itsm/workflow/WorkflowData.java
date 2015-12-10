package com.hp.idc.itsm.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FormInfo;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * 表示流程的某一个具体版本的信息
 * @author 梅园
 *
 */
public class WorkflowData {
	/**
	 * 存储当前流程版本号
	 */
	protected int versionId;
	
	/**
	 * 存储流程的根节点
	 */
	protected NodeInfo rootNode = null;
	
	/**
	 * 存储所属流程对象
	 */
	protected WorkflowInfo owner;
	
	/**
	 * 存储对应此版本流程的所有节点
	 */
	protected List<NodeInfo> nodes = new ArrayList<NodeInfo>();
	
	protected Map forms = new HashMap();
	
	/**
	 * 存储XML配置数据
	 */
	protected String xmlData;
	
	protected Element xmlDoc;
	
	/**
	 * 存储流程描述
	 */
	protected String desc = "";
	
	/**
	 * 数据存储表
	 */
	protected String dataTable = "";

	/**
	 * 获取流程描述 
	 * @return 返回流程描述
	 */
	public String getDesc() {
		return this.desc;
	}
	
	/**
	 * 获取XML配置数据
	 * @return 返回XML配置数据
	 */
	public String getXmlData() {
		return this.xmlData;
	}
	
	/**
	 * 获取对应此版本流程的所有节点
	 * @return 返回对应此版本流程的所有节点
	 */
	public List<NodeInfo> getNodes() {
		return this.nodes;
	}

	/**
	 * 获取当前流程版本号 
	 * @return 返回当前流程版本号 
	 */
	public int getVersionId() {
		return this.versionId;
	}

	/**
	 * 获取流程的oid
	 * @return 返回流程的oid
	 */
	public int getWorkflowOid() {
		return this.owner.getOid();
	}
	
	/**
	 * 根据所属流程和XML数据创建新的WorkflowData对象
	 * @param owner 所属流程
	 * @param str XML配置数据
	 * @return 生成的WorkflowData对象
	 * @throws DocumentException 
	 */
	static public WorkflowData parse(WorkflowInfo owner, String str) throws DocumentException  {
		Document doc = XmlUtil.parseString(str);
		Element el = doc.getRootElement();
		int ver = owner.getCurrentVersionId() + 1;
		el.addAttribute("version", "" + ver);
		WorkflowData d = parse(owner, el);
		
		owner.getXmlDoc().getRootElement().add(doc.getRootElement());
		owner.setCurrentVersionId(ver);
		return d;
	}

	/**
	 * 根据所属流程和XML数据创建新的WorkflowData对象
	 * @param owner 所属流程
	 * @param node XML配置数据
	 * @return 生成的WorkflowData对象
	 */
	static public WorkflowData parse(WorkflowInfo owner, Element node) {
		WorkflowData d = new WorkflowData();
		d.owner = owner;
		d.versionId = Integer.parseInt(node.attributeValue("version"));
		String rootNodeName = node.attributeValue("root");
		d.dataTable = node.attributeValue("dataTable");
		
		//取所有本地表单
		List fl = node.selectNodes("./forms/attribute");
		if(fl!=null && fl.size()>0){
			for (int i = 0; i < fl.size(); i++) {
				Element el = (Element)fl.get(i);
				try {
					FormInfo formInfo = FormInfo.parse(el);
					d.forms.put(formInfo.getId(),formInfo);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
		}
		
		//取所有节点
		List l = node.selectNodes("./node");
		for (int i = 0; i < l.size(); i++) {
			Element el = (Element)l.get(i);
			NodeInfo ni = new NodeInfo();
			ni.parse(d, el);
			if (ni.getId().equals(rootNodeName))
				d.rootNode = ni;
		}
//		for (int i = 0; i < d.nodes.size(); i++) {
//			NodeInfo el = (NodeInfo)d.nodes.get(i);
//			if (el.getId().equals(rootNodeName))
//				d.rootNode = el;
//		}

		//取所有用到的field，以构建表结构,ovsd读取速度太慢，也没必要，屏蔽
		if (owner.getOrigin().equals("ITSM")){
			List f = node.selectNodes("./node/actions/action");
			for (int i = 0; i < f.size(); i++) {
				Element el = (Element)f.get(i);
				String formId = el.attributeValue("formId");
				FormInfo formInfo = null;
				if (d.forms.containsKey(formId))
					formInfo = (FormInfo)d.forms.get(formId);
				else {
					try {
						formInfo = FormManager.getFormByOid(Integer.parseInt(formId));
					}catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
				Map t_ = (Map)getColumnField(formInfo);
				if (t_!=null)
					owner.addColumnFields(t_);
			}
		}
		Element descEl = (Element)node.selectSingleNode("./desc");
		if (descEl != null)
			d.desc = descEl.getText();
		//挪到ower里面进行添加
		//owner.getWorkflows().add(d);
		d.xmlData = node.asXML();
		d.xmlDoc = node;
		return d;
	}
	
	protected static Map<String,ColumnFieldInfo> getColumnField(FormInfo formInfo){
		Map<String,ColumnFieldInfo> tempMap = new HashMap<String,ColumnFieldInfo>();
		if (formInfo == null)
			return null;
		List<FieldInfo> temp_ = formInfo.getFields();
		for (int j = 0; j < temp_.size(); j++) {
			FieldInfo fieldInfo = (FieldInfo)temp_.get(j);
			if (fieldInfo.isSystem())
				continue;
			
			ColumnFieldInfo cfInfo = new ColumnFieldInfo();
			cfInfo.setFieldName(fieldInfo.getId());
			
			String colsName = fieldInfo.getId();
			if (fieldInfo.getId().length()>26)
				colsName = colsName.substring(0,26);
			cfInfo.setColumnName("fld_"+colsName);
			cfInfo.setType("String");
			tempMap.put(fieldInfo.getId(), cfInfo);
		}
		return tempMap;
	}

	/**
	 * 获取所属流程对象
	 * @return 返回所属流程对象
	 */
	public WorkflowInfo getOwner() {
		return this.owner;
	}

	/**
	 * 获取流程的根节点
	 * @return 返回流程的根节点
	 */
	public NodeInfo getRootNode() {
		return this.rootNode;
	}
	
	/**
	 * 根据节点ID查找节点
	 * @param id 节点ID
	 * @return 找到的节点对象
	 */
	public NodeInfo getNode(String id) {
//		for (int i = 0; i < this.nodes.size(); i++) {
//			NodeInfo el = (NodeInfo)this.nodes.get(i);
//			if (el.getId().equals(id))
//				return el;
//		}
//		return null;
		return getNodeByPath(this,id);
	}
	
	/**
	 * 有子流程的时候的获取节点对象
	 * @param path node1/node2/node3....,
	 * 按合理性规则来说，参数中除了最后一个节点的属性不是子流程节点外的其他所有节点都是子流程节点
	 * @return 如果path路径节点中有一个不是子流程节点，则返回null
	 */
	public static NodeInfo getNodeByPath(WorkflowData wfData,String path){
		if(path == null)
			return null;
		String firstNode = "";
		String otherNode = "";
		if (path.indexOf("/")!=-1) {
			firstNode = path.substring(0,path.indexOf("/"));
			otherNode = path.substring(path.indexOf("/")+1);
		} else
			firstNode = path;
		for (int i = 0; i < wfData.nodes.size(); i++) {
			NodeInfo el = (NodeInfo)wfData.nodes.get(i);
			if (el.getId().equals(firstNode)){
				if (otherNode.equals(""))
					return el;
				else {
					if (el.getType() == NodeInfo.TYPE_SUB_WF){
						WorkflowInfo wi_ = WorkflowManager.getWorkflowByOid(el.getSubflow());
						WorkflowData wd_ = wi_.getVersion(el.getSubflowVer());
						return getNodeByPath(wd_,otherNode);
					} else
						return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * 返回指定的动作
	 * @param actId
	 * @return
	 */
	public ActionInfo getAction(String actId){
		ActionInfo act = null;
		if (actId == null || actId.equals(""))
			return act;
		if (ActionInfo.TYPE_ACCEPT.equals(actId)) {
			act = new ActionInfo();
			act.setActionName("接手处理");
			return act;
		} else if (ActionInfo.TYPE_AUTOFLOW.equals(actId)) {
			act = new ActionInfo();
			act.setActionName("自动流转");
			return act;
		}
			
		for (int i = 0; i < nodes.size(); i++) {
			List<ActionInfo> acts = nodes.get(i).getActions();
			for (int j = 0; j< acts.size(); j++) {
				ActionInfo act_ = acts.get(j);
				if (act_.getActionId().equals(actId))
					return act_;
			}
		}
		return act;
	}

	/**
	 * 详见WorkflowInfo.getColumnFields();
	 * @return List[ColumnFieldInfo]
	 */
	public List getColumn() {
		return this.owner.getColumnFieldsList();
	}

	public Element getXmlDoc() {
		return xmlDoc;
	}

	public void setXmlDoc(Element xmlDoc) {
		this.xmlDoc = xmlDoc;
	}
	
	public void remove(){
		this.owner.xmlDoc.getRootElement().remove(this.xmlDoc);
		this.owner.workflows.remove(this);
	}

	public String getDataTable() {
		if (dataTable == null || dataTable.equals(""))
			dataTable = "itsm_task_"+this.owner.getOid();
		return dataTable;
	}
	

	public void setDataTable(String dataTable) {
		this.dataTable = dataTable;
	}

	public Map getForms() {
		return forms;
	}

	public void setForms(Map forms) {
		this.forms = forms;
	}
	
	public boolean equals(Object obj){
		WorkflowData wd = (WorkflowData)obj;
		if (this.versionId == wd.getVersionId())
			return true;
		return false;
	}
}
