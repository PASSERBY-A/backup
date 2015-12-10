	package com.hp.idc.itsm.workflow;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.common.IObjectWithAttribute;
import com.hp.idc.itsm.common.SMSTemplate;
import com.hp.idc.itsm.configure.ModuleName;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * ������Ϣ��
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class WorkflowInfo implements IObjectWithAttribute {

	/**
	 * δʹ��״̬
	 */
	public static int STATUS_UNUSED = 0;

	/**
	 * ʹ����״̬
	 */
	public static int STATUS_USED = 1;

	/**
	 * �洢OID
	 */
	protected int oid = -1;

	/**
	 * �洢��������
	 */
	protected String name;

	/**
	 * �洢������ʱ��
	 */
	protected long lastUpdate;

	/**
	 * �洢����״̬
	 */
	protected int status;

	/**
	 * �洢������
	 */
	protected String editBy;

	/**
	 * �洢�������
	 */
	protected String category;

	/**
	 * �洢��������XML��Ϣ
	 */
	protected String xmlData;

	/**
	 * �洢��������XML����
	 */
	protected Document xmlDoc = null;
	
	/**
	 * ��������ϵͳ��ITSM��OVSD.....��
	 */
	protected String origin = "";
	
	/**
	 * �½�Ȩ�޿���
	 */
	protected String rule = "";
	
	/**
	 * ������Ȩ���б������ǲ����Լ��Ĺ����������Դ���
	 */
	protected String editRule = "";
	
	/**
	 * ���ö��ŷ���
	 */
	protected boolean enableSMS = true;
	
	/**
	 * �Ƿ������飨�����飩�����˴���
	 */
	protected boolean allowLocalGroup = false;
	
	/**
	 * �Ƿ�ѹرյĹ������ص�������
	 */
	protected boolean loadHisToCache = false;
	
	/**
	 * ���̵����Ķ���ģ��
	 */
	private SMSTemplate smsTemplate;


	/**
	 * ��������ҳ��
	 */
	protected String dealPage = "$ITSM_HOME/task/taskInfo.jsp";
	
	//==============�����������Կ�ʼ=====
	//��������ģʽ:local/remote
	private String snapMode = "";
	private String snapLocalFormOid = "";
	private String snapLocalFormTemplate = "";
	private String snapRemoteViewPage = "";
	//==============�����������Խ���=====
	
	//==============����������==============
	private int changeWfOid = -1;//���������OID


	/**
	 * �洢���̵Ĳ�ͬ�汾
	 */
	protected List<WorkflowData> workflows = new ArrayList<WorkflowData>();

	/**
	 * ָ�����̵ĵ�ǰ�汾
	 */
	protected WorkflowData currentVersion = null;
	
	protected Map<String, ColumnFieldInfo> columnFields = new HashMap<String, ColumnFieldInfo>();

	/**
	 * �洢���̵ĵ�ǰ�汾��
	 */
	protected int currentVersionId = -1;

	/**
	 * ��ȡ���̵ĵ�ǰ�汾��
	 * 
	 * @return �������̵ĵ�ǰ�汾��
	 */
	public int getCurrentVersionId() {
		return this.currentVersionId;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ������������
	 */
	public String getDesc() {
		if (this.currentVersion != null)
			return this.currentVersion.getDesc();
		return "";
	}

	/**
	 * ��ȡ���̵����а汾
	 * 
	 * @return �������̵����а汾 List<WorkflowData>
	 */
	public List<WorkflowData> getWorkflows() {
		return this.workflows;
	}

	/**
	 * ���õ�ǰ�İ汾��
	 * 
	 * @param currentVersionId
	 *            ��ǰ�İ汾��
	 */
	public void setCurrentVersionId(int currentVersionId) {
		this.currentVersionId = currentVersionId;
		this.xmlDoc.getRootElement().addAttribute("currentId",
				"" + currentVersionId);
	}

	/**
	 * Ĭ�Ϲ��캯��
	 * 
	 */
	public WorkflowInfo() {
		// Nothing to do here
	}

	/**
	 * �����ݿⷵ�صĽ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
	 * @throws DocumentException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void parse(ResultSet rs) throws DocumentException, SQLException,
			IOException {
		parse(rs.getInt("WF_OID"), rs.getString("WF_NAME"), rs
				.getString("WF_EDITBY"), rs.getInt("WF_STATUS"), rs
				.getTimestamp("WF_LASTUPDATE").getTime(), rs
				.getString("WF_CATALOG"), ResultSetOperation.clobToString(rs
				.getClob("WF_CONFIGURE")),rs.getString("WF_ORIGIN"));
	}

	/**
	 * ��ָ���Ĳ������������ݽ��з���
	 * @param oid ����oid
	 * @param name ��������
	 * @param editby ������
	 * @param status ����״̬
	 * @param lastUpdate ����ʱ��
	 * @param category �������
	 * @param configure ��������XML����
	 * @throws DocumentException
	 */
	protected void parse(int oid, String name, String editby, int status,
			long lastUpdate, String category, String configure,String origin)
			throws DocumentException {
		setOid(oid);
		setName(name);
		setEditBy(editby);
		setStatus(status);
		setLastUpdate(lastUpdate);
		setCategory(category);
		setOrigin(origin);
		if (configure == null || configure.length() == 0)
			setXmlData("<configure/>");
		else
			setXmlData(configure);
		parseXml();
	}

	/**
	 * ������������XML����
	 *
	 */
	protected void parseXml() {
		Element root = this.xmlDoc.getRootElement();
		if (root.attributeValue("currentId") != null)
			this.currentVersionId = Integer.parseInt(root
					.attributeValue("currentId"));
		this.enableSMS = "false".equals(root.attributeValue("enableSMS"))?false:true;
		this.allowLocalGroup = "true".equals(root.attributeValue("allowLocalGroup"))?true:false;
		
		this.loadHisToCache = "true".equals(root.attributeValue("loadHisToCache"))?true:false;

		if (root.attributeValue("dealPage")!=null && !root.attributeValue("dealPage").equals("")){
			this.dealPage = root.attributeValue("dealPage");
		}
		
		//���յ��������
		Node snapConfNode = root.selectSingleNode("./basicConf/snap");
		if (snapConfNode!=null) {
			this.snapMode = ((Element)snapConfNode).attributeValue("mode");
			Element localSnap = (Element)snapConfNode.selectSingleNode("./local");
			if (localSnap != null){
				this.snapLocalFormOid = localSnap.attributeValue("formOid");
				this.snapLocalFormTemplate = localSnap.getText();
			}
			
			Element remoteSnap = (Element)snapConfNode.selectSingleNode("./remote");
			if (remoteSnap != null) {
				this.snapRemoteViewPage = remoteSnap.getText();
			}
		}
		
		//����ģ������
		Node smsTemplateNode = root.selectSingleNode("./basicConf/sms");
		if (smsTemplateNode!=null) {
			SMSTemplate smst = new SMSTemplate();
			smst.parse(smsTemplateNode);
			this.smsTemplate = smst;
		}
		
		//��Ӧ�ı�����̵�����
		Node changeConfNode = root.selectSingleNode("./basicConf/change");
		if (changeConfNode!=null){
			this.changeWfOid = Integer.parseInt(((Element)changeConfNode).attributeValue("wfOid"));
		}
		
		if (root.attributeValue("snap")!=null && !root.attributeValue("dealPage").equals("")){
			this.dealPage = root.attributeValue("dealPage");
		}
		
		//Ȩ�޿�������
		Node ruleNode = root.selectSingleNode("./rule/create");
		if (ruleNode!=null)
			this.rule = ruleNode.getText();
		ruleNode = root.selectSingleNode("./rule/edit");
		if (ruleNode!=null)
			this.editRule = ruleNode.getText();
		List l = root.selectNodes("./workflow");
		
		for (int i = 0; i < l.size(); i++) {
			Element el = (Element) l.get(i);
			WorkflowData d = null;
			try {
				d = WorkflowData.parse(this, el);
				this.workflows.add(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (d != null && d.getVersionId() == this.currentVersionId)
				this.currentVersion = d;
		}
	}
	

	/**
	 * ��ȡ���̷���
	 * @return �������̷���
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * ��ȡ���̷�������
	 * @return �������̷�������
	 */
	public String getCategoryName() {
		return ModuleName.getModuleName(this.category);
	}

	/**
	 * �������̷���
	 * @param category ���̷���
	 */
	public void setCategory(String category) {
		/* ��֤���ϼ���,���º�ɾ�� */
		if (category.equals("all"))
			this.category = "" + ModuleName.ALL;
		else if (category.equals("chg"))
			this.category = "" + ModuleName.CHANGE;
		else if (category.equals("cfg"))
			this.category = "" + ModuleName.CONFIGURE;
		else if (category.equals("inc"))
			this.category = "" + ModuleName.INCIDENT;
		else if (category.equals("prb"))
			this.category = "" + ModuleName.PROBLEM;
		else if (category.equals("req"))
			this.category = "" + ModuleName.REQUIRE;
		/* ��֤���ϼ���,���º�ɾ�� end */
		else
			this.category = category;
	}

	/**
	 * ��ȡ���̴�����
	 * @return �������̴�����
	 */
	public String getEditBy() {
		return this.editBy;
	}

	/**
	 * �������̴�����
	 * @param editBy ���̴�����
	 */
	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	/**
	 * ��ȡ������ʱ��
	 * @return ����������ʱ��
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * ����������ʱ��
	 * @param lastUpdate ������ʱ��
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * ��ȡ��������
	 * @return ������������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ������������
	 * @param name ��������
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ����oid
	 * @return ��������oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * ��������oid
	 * @param oid ����oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * ��������״̬
	 * @see #STATUS_UNUSED
	 * @see #STATUS_USED
	 * @return ��������״̬
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * ��������״̬
	 * @see #STATUS_UNUSED
	 * @see #STATUS_USED
	 * @param status ����״̬
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * ���»�����������Ϣ������Ȩ�ޣ�����ģ���
	 * @param str
	 * @throws Exception 
	 */
	public void updateBaseConf(String str) throws Exception{
		if (str == null || str.length() == 0)
			return;
		
		Document doc = XmlUtil.parseString(str);
		Element el = doc.getRootElement();
		if (el.asXML().equals(this.currentVersion.getXmlData())) {
			System.out.println("���������ޱ仯���������");
			return;
		}
	}

	/**
	 * ���Ӱ汾������Ϣ
	 * 
	 * @param str ����XML����
	 * @param operName ������
	 * @throws Exception
	 */
	public void addVersion(String str, String operName) throws Exception {
		if (str == null || str.length() == 0)
			return;
		if (this.currentVersion != null) {
			Document doc = XmlUtil.parseString(str);
			Element el = doc.getRootElement();
			if (el.asXML().equals(this.currentVersion.getXmlData())) {
				System.out.println("���������ޱ仯���������");
				return;
			}
		}
		System.out.println("���������б仯������Ϊ�°汾");
		WorkflowData d = WorkflowData.parse(this, str);
		this.workflows.add(d);
		setStatus(STATUS_USED);
		this.currentVersion = d;
	}
	
	public void updateCurrentVersion(String str, String operName) throws Exception {
		if (str == null || str.length() == 0)
			return;
		if (this.currentVersion != null) {
			Document doc = XmlUtil.parseString(str);
			Element el = doc.getRootElement();
			if (el.asXML().equals(this.currentVersion.getXmlData())) {
				System.out.println("���������ޱ仯���������");
				return;
			} else {
				int oldVersionId = this.currentVersion.getVersionId();
				el.addAttribute("version", "" + oldVersionId);
				//ɾ��info��xml��������ԭ�������°汾��<workflowData/>�ڵ�
				this.xmlDoc.getRootElement().remove(currentVersion.getXmlDoc());
				//��list��������ɾ��
				for (int i = 0;i < workflows.size(); i++) {
					WorkflowData d_ = (WorkflowData)workflows.get(i);
					if (d_.getVersionId() == oldVersionId){
						workflows.remove(i);
						break;
					}
				}
				WorkflowData d = WorkflowData.parse(this, el);
				this.xmlDoc.getRootElement().add(el);
				this.workflows.add(d);
				this.currentVersion = d;
			}
		}
		setStatus(STATUS_USED);
	}

	/**
	 * ��ȡ����ָ���汾
	 * 
	 * @param version
	 *            �汾��
	 * @return �������̰汾��Ϣ
	 */
	public WorkflowData getVersion(int version) {
		if (version == -1)
			return this.currentVersion;
		for (int i = this.workflows.size() - 1; i >= 0; i--) {
			WorkflowData d = (WorkflowData)this.workflows.get(i);
			if (d.getVersionId() == version)
				return d;
		}
		return null;
	}
	
	public void removeVersion(int version) {
		WorkflowData wfData = this.getVersion(version);
		wfData.remove();
	}


	/**
	 * ��ȡXML�����ĵ�����
	 * @return ����XML�����ĵ�����
	 */
	public Document getXmlDoc() {
		return this.xmlDoc;
	}

	/**
	 * ��ȡXML��������
	 * @return ����XML��������
	 */
	public String getXmlData() {
		return this.xmlDoc.asXML();
	}

	/**
	 * ����XML��������
	 * @param str XML��������
	 * @throws DocumentException
	 */
	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(this.xmlData);
		this.xmlDoc = doc;
	}

	/**
	 * ��ȡ���̵ĵ�ǰ�汾��Ϣ
	 * @return �������̵ĵ�ǰ�汾��Ϣ
	 */
	public WorkflowData getCurrentVersion() {
		return this.currentVersion;
	}

	/**
	 * �ж������Ƿ�����ָ��ģ��
	 * @param moduleOid ģ��OID
	 * @return ���������Ƿ�����ָ��ģ��
	 */
	public boolean inModule(int moduleOid) {
		return this.category.equals("" + ModuleName.ALL)
				|| this.category.equals("" + moduleOid)
				|| this.category.startsWith("" + moduleOid + ",")
				|| this.category.endsWith("," + moduleOid)
				|| (this.category.indexOf("," + moduleOid + ",") != -1);
	}

	/**
	 * ���� id ��ѯ���������<br>
	 * WorkflowInfo ��������԰�����<br>
	 * id: ����ID<br>
	 * name: ��������<br>
	 * desc: ��������<br>
	 * category: �������<br>
	 * @param id
	 *            ��ѯ��ʶ
	 * @return ����ֵ���Ҳ���ʱ���� null
	 */
	public String getAttribute(String id) {
		if (id == null)
			return null;
		if (id.equals("id"))
			return "" + getOid();
		if (id.equals("name"))
			return getName();
		if (id.equals("desc"))
			return getDesc();
		if (id.equals("category"))
			return this.getCategoryName();
		return null;
	}

	public String getOrigin() {
		return (origin==null||origin.equals(""))?"ITSM":origin;
	}

	public void setOrigin(String origin) {
		origin = origin==null||origin.equals("")?"ITSM":origin;
		this.origin = origin;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		Element eleRoot = this.xmlDoc.getRootElement();
		Element ruleEle = (Element)eleRoot.selectSingleNode("./rule");
		if (ruleEle==null)
			ruleEle = eleRoot.addElement("rule");
		Element createRule = (Element)ruleEle.selectSingleNode("./create");
		if(createRule == null)
			createRule = ruleEle.addElement("create");
		createRule.setText(rule);
		this.rule = rule;
	}
	
	/**
	 * ��ȡ�ֶ������ݱ��еĶ�Ӧ
	 * @return
	 */
	public List<ColumnFieldInfo> getColumnFieldsList(){
		List<ColumnFieldInfo> retList = new ArrayList<ColumnFieldInfo>();
		retList.addAll(columnFields.values());
		return retList;
	}
	
	public Map<String, ColumnFieldInfo> getColumnFieldsMap(){
		return columnFields;
	}
	
	public List<ColumnFieldInfo> addColumnFields(Map<String, ColumnFieldInfo> m){
		columnFields.putAll(m);
		List<ColumnFieldInfo> retList = new ArrayList<ColumnFieldInfo>();
		retList.addAll(columnFields.values());
		return retList;
	}

	public String getEditRule() {
		return editRule;
	}
	
	/**
	 * ���ط�������eidtRule,�ѹ����飬��֯��ת������Ա
	 * @return  /��Ա1/��Ա2/....
	 */
	public String getAnalyzedEditRule(){
		String[] rs = editRule.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rs.length; i++){
			CIInfo c = CIManager.getCIById(rs[i]);
			if (c == null)
				continue;
			sb.append("/");
			List l = new ArrayList();
			if (c instanceof PersonInfoInterface){
				sb.append(rs[i]);
			}else if (c instanceof WorkgroupInfoInterface){
				WorkgroupInfoInterface wii = (WorkgroupInfoInterface)c;
				l = wii.getPersons();
			}else if (c instanceof OrganizationInfoInterface){
				OrganizationInfoInterface oii = (OrganizationInfoInterface)c;
				l = oii.getPersons();
			}
			for (int j = 0; j < l.size(); j++) {
				sb.append(((CIInfo)l.get(j)).getId());
				sb.append("/");
			}
		}
		sb.append("/");
		return sb.toString();
	}

	public void setEditRule(String editRule) {
		editRule = editRule==null?"":editRule;
		Element eleRoot = this.xmlDoc.getRootElement();
		Element ruleEle = (Element)eleRoot.selectSingleNode("./rule");
		if (ruleEle==null)
			ruleEle = eleRoot.addElement("rule");
		Element editRule_ = (Element)ruleEle.selectSingleNode("./edit");
		if(editRule_ == null)
			editRule_ = ruleEle.addElement("edit");
		editRule_.setText(editRule);
		this.editRule = editRule;
	}

	public boolean isEnableSMS() {
		return enableSMS;
	}

	public void setEnableSMS(boolean enableSMS) {
		Element root = this.xmlDoc.getRootElement();
		root.addAttribute("enableSMS", enableSMS+"");
		this.enableSMS = enableSMS;
	}

	public boolean isAllowLocalGroup() {
		return allowLocalGroup;
	}

	public void setAllowLocalGroup(boolean allowLocalGroup) {
		Element root = this.xmlDoc.getRootElement();
		root.addAttribute("allowLocalGroup", allowLocalGroup+"");
		this.allowLocalGroup = allowLocalGroup;
	}

	public String getDealPage() {
		return (dealPage == null || dealPage.equals(""))?
				"$ITSM_HOME/task/taskInfo.jsp":dealPage;
	}

	public void setDealPage(String dealPage) {
		Element root = this.xmlDoc.getRootElement();
		root.addAttribute("dealPage", dealPage);
		this.dealPage = dealPage;
	}
	
	
	public boolean isLoadHisToCache() {
		return loadHisToCache;
	}

	public void setLoadHisToCache(boolean loadHisToCache) {
		Element root = this.xmlDoc.getRootElement();
		root.addAttribute("loadHisToCache", loadHisToCache+"");
		this.loadHisToCache = loadHisToCache;
	}
	
	public String getSnapMode() {
		return (snapMode == null || snapMode.equals("")) ? "local" : snapMode;
	}

	public void setSnapMode(String snapMode) {
		if (snapMode == null)
			snapMode = "";
		Element eleRoot = this.xmlDoc.getRootElement();
		Element basicConf = (Element)eleRoot.selectSingleNode("./basicConf");
		if (basicConf==null)
			basicConf = eleRoot.addElement("basicConf");
		Element snap = (Element)basicConf.selectSingleNode("./snap");
		if(snap == null)
			snap = basicConf.addElement("snap");
		snap.addAttribute("mode", snapMode);
		this.snapMode = snapMode;
	}

	public String getSnapLocalFormOid() {
		return snapLocalFormOid;
	}

	public void setSnapLocalFormOid(String snapLocalFormOid) {
		Element root = this.xmlDoc.getRootElement();
		Element snap = (Element)root.selectSingleNode("./basicConf/snap");
		Element local = (Element)root.selectSingleNode("./basicConf/snap/local");
		if (local == null)
			local = snap.addElement("local");
		local.addAttribute("formOid", snapLocalFormOid);
		this.snapLocalFormOid = snapLocalFormOid;
	}

	public String getSnapLocalFormTemplate() {
		return snapLocalFormTemplate;
	}

	public void setSnapLocalFormTemplate(String snapLocalFormTemplate) {
		Element root = this.xmlDoc.getRootElement();
		Element snap = (Element)root.selectSingleNode("./basicConf/snap");
		Element local = (Element)root.selectSingleNode("./basicConf/snap/local");
		if (local == null)
			local = snap.addElement("local");
		local.setText(snapLocalFormTemplate);
		this.snapLocalFormTemplate = snapLocalFormTemplate;
	}

	public String getSnapRemoteViewPage() {
		return snapRemoteViewPage;
	}

	public void setSnapRemoteViewPage(String snapRemoteViewPage) {
		Element root = this.xmlDoc.getRootElement();
		Element snap = (Element)root.selectSingleNode("./basicConf/snap");
		Element local = (Element)root.selectSingleNode("./basicConf/snap/remote");
		if (local == null)
			local = snap.addElement("remote");
		local.setText(snapRemoteViewPage);
		this.snapRemoteViewPage = snapRemoteViewPage;
	}

	/**
	 * @return the changeWfOid
	 */
	public int getChangeWfOid() {
		return changeWfOid;
	}

	/**
	 * @param changeWfOid the changeWfOid to set
	 */
	public void setChangeWfOid(int changeWfOid) {
		Element eleRoot = this.xmlDoc.getRootElement();
		Element basicConf = (Element)eleRoot.selectSingleNode("./basicConf");
		if (basicConf==null)
			basicConf = eleRoot.addElement("basicConf");
		Element change = (Element)basicConf.selectSingleNode("./change");
		if(change == null)
			change = basicConf.addElement("change");
		change.addAttribute("wfOid", changeWfOid+"");
		this.changeWfOid = changeWfOid;
	}

	
	/**
	 * ��ȡ�������̰汾�Ĺ������ݴ�ŵı���,�ظ��ģ�ֻȡһ��
	 * @return Map<names,"">
	 */
	public Map<String,String> getDataTablesName(){
		Map<String,String> m = new HashMap<String,String>();
		List<WorkflowData> wdList = this.workflows;
		for (int i = 0; i < wdList.size();i++){
			WorkflowData wfData = wdList.get(i);
			if (wfData.getDataTable()==null || wfData.getDataTable().equals(""))
				m.put("itsm_task_"+this.oid, "");
			else
				m.put(wfData.getDataTable(), "");
		}
//		Element root = this.xmlDoc.getRootElement();
//		List l = root.selectNodes("./workflow");
//		for (int i = 0; i < l.size(); i++) {
//			Element e = (Element)l.get(i);
//			String dt = e.attributeValue("dataTable");
//			if (dt ==null || dt.equals(""))
//				m.put("itsm_task_"+this.oid, "");
//			else
//				m.put(dt, "");
//		}
		return m;
	}

	/**
	 * @return the smsTemplate
	 */
	public SMSTemplate getSmsTemplate() {
		return smsTemplate;
	}

	/**
	 * @param smsTemplate the smsTemplate to set
	 */
	public void setSmsTemplate(SMSTemplate smsTemplate) {
		this.smsTemplate = smsTemplate;
		Element eleRoot = this.xmlDoc.getRootElement();
		Element basicConf = (Element)eleRoot.selectSingleNode("./basicConf");
		if (basicConf==null)
			basicConf = eleRoot.addElement("basicConf");
		Element sms = (Element)basicConf.selectSingleNode("./sms");
		if(sms == null)
			sms = basicConf.addElement("sms");
		Element sms_new  = (Element)sms.selectSingleNode("./new");
		if (sms_new==null)
			sms_new = sms.addElement("new");
		sms_new.setText(smsTemplate.getSmsNew());
		
		Element sms_dealed  = (Element)sms.selectSingleNode("./dealed");
		if (sms_dealed==null)
			sms_dealed = sms.addElement("dealed");
		sms_dealed.setText(smsTemplate.getSmsDealed());
		
		Element sms_rollback  = (Element)sms.selectSingleNode("./rollback");
		if (sms_rollback==null)
			sms_rollback = sms.addElement("rollback");
		sms_rollback.setText(smsTemplate.getSmsRollback());
		
		Element sms_overtime  = (Element)sms.selectSingleNode("./overtime");
		if (sms_overtime==null)
			sms_overtime = sms.addElement("overtime");
		sms_overtime.setText(smsTemplate.getSmsOvertime());
		
		Element sms_overdue  = (Element)sms.selectSingleNode("./overdue");
		if (sms_overdue==null)
			sms_overdue = sms.addElement("overdue");
		sms_overdue.setText(smsTemplate.getSmsOverdue());
		
		Element sms_remind  = (Element)sms.selectSingleNode("./remind");
		if (sms_remind==null)
			sms_remind = sms.addElement("remind");
		sms_remind.setText(smsTemplate.getSmsRemind());
	}

}
