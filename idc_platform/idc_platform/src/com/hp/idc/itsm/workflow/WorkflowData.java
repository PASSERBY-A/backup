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
 * ��ʾ���̵�ĳһ������汾����Ϣ
 * @author ÷԰
 *
 */
public class WorkflowData {
	/**
	 * �洢��ǰ���̰汾��
	 */
	protected int versionId;
	
	/**
	 * �洢���̵ĸ��ڵ�
	 */
	protected NodeInfo rootNode = null;
	
	/**
	 * �洢�������̶���
	 */
	protected WorkflowInfo owner;
	
	/**
	 * �洢��Ӧ�˰汾���̵����нڵ�
	 */
	protected List<NodeInfo> nodes = new ArrayList<NodeInfo>();
	
	protected Map forms = new HashMap();
	
	/**
	 * �洢XML��������
	 */
	protected String xmlData;
	
	protected Element xmlDoc;
	
	/**
	 * �洢��������
	 */
	protected String desc = "";
	
	/**
	 * ���ݴ洢��
	 */
	protected String dataTable = "";

	/**
	 * ��ȡ�������� 
	 * @return ������������
	 */
	public String getDesc() {
		return this.desc;
	}
	
	/**
	 * ��ȡXML��������
	 * @return ����XML��������
	 */
	public String getXmlData() {
		return this.xmlData;
	}
	
	/**
	 * ��ȡ��Ӧ�˰汾���̵����нڵ�
	 * @return ���ض�Ӧ�˰汾���̵����нڵ�
	 */
	public List<NodeInfo> getNodes() {
		return this.nodes;
	}

	/**
	 * ��ȡ��ǰ���̰汾�� 
	 * @return ���ص�ǰ���̰汾�� 
	 */
	public int getVersionId() {
		return this.versionId;
	}

	/**
	 * ��ȡ���̵�oid
	 * @return �������̵�oid
	 */
	public int getWorkflowOid() {
		return this.owner.getOid();
	}
	
	/**
	 * �����������̺�XML���ݴ����µ�WorkflowData����
	 * @param owner ��������
	 * @param str XML��������
	 * @return ���ɵ�WorkflowData����
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
	 * �����������̺�XML���ݴ����µ�WorkflowData����
	 * @param owner ��������
	 * @param node XML��������
	 * @return ���ɵ�WorkflowData����
	 */
	static public WorkflowData parse(WorkflowInfo owner, Element node) {
		WorkflowData d = new WorkflowData();
		d.owner = owner;
		d.versionId = Integer.parseInt(node.attributeValue("version"));
		String rootNodeName = node.attributeValue("root");
		d.dataTable = node.attributeValue("dataTable");
		
		//ȡ���б��ر�
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
		
		//ȡ���нڵ�
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

		//ȡ�����õ���field���Թ�����ṹ,ovsd��ȡ�ٶ�̫����Ҳû��Ҫ������
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
		//Ų��ower����������
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
	 * ��ȡ�������̶���
	 * @return �����������̶���
	 */
	public WorkflowInfo getOwner() {
		return this.owner;
	}

	/**
	 * ��ȡ���̵ĸ��ڵ�
	 * @return �������̵ĸ��ڵ�
	 */
	public NodeInfo getRootNode() {
		return this.rootNode;
	}
	
	/**
	 * ���ݽڵ�ID���ҽڵ�
	 * @param id �ڵ�ID
	 * @return �ҵ��Ľڵ����
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
	 * �������̵�ʱ��Ļ�ȡ�ڵ����
	 * @param path node1/node2/node3....,
	 * �������Թ�����˵�������г������һ���ڵ�����Բ��������̽ڵ�����������нڵ㶼�������̽ڵ�
	 * @return ���path·���ڵ�����һ�����������̽ڵ㣬�򷵻�null
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
	 * ����ָ���Ķ���
	 * @param actId
	 * @return
	 */
	public ActionInfo getAction(String actId){
		ActionInfo act = null;
		if (actId == null || actId.equals(""))
			return act;
		if (ActionInfo.TYPE_ACCEPT.equals(actId)) {
			act = new ActionInfo();
			act.setActionName("���ִ���");
			return act;
		} else if (ActionInfo.TYPE_AUTOFLOW.equals(actId)) {
			act = new ActionInfo();
			act.setActionName("�Զ���ת");
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
	 * ���WorkflowInfo.getColumnFields();
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
