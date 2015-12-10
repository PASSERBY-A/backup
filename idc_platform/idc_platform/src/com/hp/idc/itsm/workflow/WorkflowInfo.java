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
 * 流程信息类
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class WorkflowInfo implements IObjectWithAttribute {

	/**
	 * 未使用状态
	 */
	public static int STATUS_UNUSED = 0;

	/**
	 * 使用中状态
	 */
	public static int STATUS_USED = 1;

	/**
	 * 存储OID
	 */
	protected int oid = -1;

	/**
	 * 存储流程名称
	 */
	protected String name;

	/**
	 * 存储最后更新时间
	 */
	protected long lastUpdate;

	/**
	 * 存储流程状态
	 */
	protected int status;

	/**
	 * 存储创建人
	 */
	protected String editBy;

	/**
	 * 存储流程类别
	 */
	protected String category;

	/**
	 * 存储流程配置XML信息
	 */
	protected String xmlData;

	/**
	 * 存储流程配置XML对象
	 */
	protected Document xmlDoc = null;
	
	/**
	 * 所属工单系统（ITSM、OVSD.....）
	 */
	protected String origin = "";
	
	/**
	 * 新建权限控制
	 */
	protected String rule = "";
	
	/**
	 * 允许处理权限列表，不管是不是自己的工单，都可以处理
	 */
	protected String editRule = "";
	
	/**
	 * 启用短信发送
	 */
	protected boolean enableSMS = true;
	
	/**
	 * 是否允许本组（工作组）其他人处理
	 */
	protected boolean allowLocalGroup = false;
	
	/**
	 * 是否把关闭的工单加载到缓存里
	 */
	protected boolean loadHisToCache = false;
	
	/**
	 * 流程单独的短信模板
	 */
	private SMSTemplate smsTemplate;


	/**
	 * 工单处理页面
	 */
	protected String dealPage = "$ITSM_HOME/task/taskInfo.jsp";
	
	//==============工单快照属性开始=====
	//工单快照模式:local/remote
	private String snapMode = "";
	private String snapLocalFormOid = "";
	private String snapLocalFormTemplate = "";
	private String snapRemoteViewPage = "";
	//==============工单快照属性结束=====
	
	//==============申请变更配置==============
	private int changeWfOid = -1;//变更的流程OID


	/**
	 * 存储流程的不同版本
	 */
	protected List<WorkflowData> workflows = new ArrayList<WorkflowData>();

	/**
	 * 指向流程的当前版本
	 */
	protected WorkflowData currentVersion = null;
	
	protected Map<String, ColumnFieldInfo> columnFields = new HashMap<String, ColumnFieldInfo>();

	/**
	 * 存储流程的当前版本号
	 */
	protected int currentVersionId = -1;

	/**
	 * 获取流程的当前版本号
	 * 
	 * @return 返回流程的当前版本号
	 */
	public int getCurrentVersionId() {
		return this.currentVersionId;
	}

	/**
	 * 获取流程描述
	 * 
	 * @return 返回流程描述
	 */
	public String getDesc() {
		if (this.currentVersion != null)
			return this.currentVersion.getDesc();
		return "";
	}

	/**
	 * 获取流程的所有版本
	 * 
	 * @return 返回流程的所有版本 List<WorkflowData>
	 */
	public List<WorkflowData> getWorkflows() {
		return this.workflows;
	}

	/**
	 * 设置当前的版本号
	 * 
	 * @param currentVersionId
	 *            当前的版本号
	 */
	public void setCurrentVersionId(int currentVersionId) {
		this.currentVersionId = currentVersionId;
		this.xmlDoc.getRootElement().addAttribute("currentId",
				"" + currentVersionId);
	}

	/**
	 * 默认构造函数
	 * 
	 */
	public WorkflowInfo() {
		// Nothing to do here
	}

	/**
	 * 对数据库返回的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
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
	 * 用指定的参数对流程数据进行分析
	 * @param oid 流程oid
	 * @param name 流程名称
	 * @param editby 创建人
	 * @param status 流程状态
	 * @param lastUpdate 更新时间
	 * @param category 流程类别
	 * @param configure 流程配置XML数据
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
	 * 分析流程配置XML数据
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
		
		//快照的相关配置
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
		
		//短信模板配置
		Node smsTemplateNode = root.selectSingleNode("./basicConf/sms");
		if (smsTemplateNode!=null) {
			SMSTemplate smst = new SMSTemplate();
			smst.parse(smsTemplateNode);
			this.smsTemplate = smst;
		}
		
		//对应的变更流程的配置
		Node changeConfNode = root.selectSingleNode("./basicConf/change");
		if (changeConfNode!=null){
			this.changeWfOid = Integer.parseInt(((Element)changeConfNode).attributeValue("wfOid"));
		}
		
		if (root.attributeValue("snap")!=null && !root.attributeValue("dealPage").equals("")){
			this.dealPage = root.attributeValue("dealPage");
		}
		
		//权限控制配置
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
	 * 获取流程分类
	 * @return 返回流程分类
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * 获取流程分类名称
	 * @return 返回流程分类名称
	 */
	public String getCategoryName() {
		return ModuleName.getModuleName(this.category);
	}

	/**
	 * 设置流程分类
	 * @param category 流程分类
	 */
	public void setCategory(String category) {
		/* 保证湖南兼容,更新后删除 */
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
		/* 保证湖南兼容,更新后删除 end */
		else
			this.category = category;
	}

	/**
	 * 获取流程创建人
	 * @return 返回流程创建人
	 */
	public String getEditBy() {
		return this.editBy;
	}

	/**
	 * 设置流程创建人
	 * @param editBy 流程创建人
	 */
	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	/**
	 * 获取最后更新时间
	 * @return 返回最后更新时间
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 设置最后更新时间
	 * @param lastUpdate 最后更新时间
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 获取流程名称
	 * @return 返回流程名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置流程名称
	 * @param name 流程名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取流程oid
	 * @return 返回流程oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * 设置流程oid
	 * @param oid 流程oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * 返回流程状态
	 * @see #STATUS_UNUSED
	 * @see #STATUS_USED
	 * @return 返回流程状态
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 设置流程状态
	 * @see #STATUS_UNUSED
	 * @see #STATUS_USED
	 * @param status 流程状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * 更新基本的配置信息，比如权限，短信模板等
	 * @param str
	 * @throws Exception 
	 */
	public void updateBaseConf(String str) throws Exception{
		if (str == null || str.length() == 0)
			return;
		
		Document doc = XmlUtil.parseString(str);
		Element el = doc.getRootElement();
		if (el.asXML().equals(this.currentVersion.getXmlData())) {
			System.out.println("流程数据无变化，无需更新");
			return;
		}
	}

	/**
	 * 增加版本配置信息
	 * 
	 * @param str 配置XML数据
	 * @param operName 操作人
	 * @throws Exception
	 */
	public void addVersion(String str, String operName) throws Exception {
		if (str == null || str.length() == 0)
			return;
		if (this.currentVersion != null) {
			Document doc = XmlUtil.parseString(str);
			Element el = doc.getRootElement();
			if (el.asXML().equals(this.currentVersion.getXmlData())) {
				System.out.println("流程数据无变化，无需更新");
				return;
			}
		}
		System.out.println("流程数据有变化，更新为新版本");
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
				System.out.println("流程数据无变化，无需更新");
				return;
			} else {
				int oldVersionId = this.currentVersion.getVersionId();
				el.addAttribute("version", "" + oldVersionId);
				//删除info的xml对象里面原来的最新版本的<workflowData/>节点
				this.xmlDoc.getRootElement().remove(currentVersion.getXmlDoc());
				//从list数组里面删除
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
	 * 获取流程指定版本
	 * 
	 * @param version
	 *            版本号
	 * @return 返回流程版本信息
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
	 * 获取XML配置文档对象
	 * @return 返回XML配置文档对象
	 */
	public Document getXmlDoc() {
		return this.xmlDoc;
	}

	/**
	 * 获取XML配置数据
	 * @return 返回XML配置数据
	 */
	public String getXmlData() {
		return this.xmlDoc.asXML();
	}

	/**
	 * 设置XML配置数据
	 * @param str XML配置数据
	 * @throws DocumentException
	 */
	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(this.xmlData);
		this.xmlDoc = doc;
	}

	/**
	 * 获取流程的当前版本信息
	 * @return 返回流程的当前版本信息
	 */
	public WorkflowData getCurrentVersion() {
		return this.currentVersion;
	}

	/**
	 * 判断流程是否属于指定模块
	 * @param moduleOid 模块OID
	 * @return 返回流程是否属于指定模块
	 */
	public boolean inModule(int moduleOid) {
		return this.category.equals("" + ModuleName.ALL)
				|| this.category.equals("" + moduleOid)
				|| this.category.startsWith("" + moduleOid + ",")
				|| this.category.endsWith("," + moduleOid)
				|| (this.category.indexOf("," + moduleOid + ",") != -1);
	}

	/**
	 * 根据 id 查询对象的属性<br>
	 * WorkflowInfo 对象的属性包括：<br>
	 * id: 流程ID<br>
	 * name: 流程名称<br>
	 * desc: 流程描述<br>
	 * category: 流程类别<br>
	 * @param id
	 *            查询标识
	 * @return 属性值，找不到时返回 null
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
	 * 获取字段与数据表列的对应
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
	 * 返回分析过的eidtRule,把工作组，组织，转化成人员
	 * @return  /人员1/人员2/....
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
	 * 获取所有流程版本的工单数据存放的表名,重复的，只取一个
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
