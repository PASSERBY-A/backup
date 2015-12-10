package com.hp.idc.itsm.configure;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskManager;
import com.hp.idc.itsm.util.XmlUtil;
import com.hp.idc.itsm.workflow.WorkflowManager;

public class ViewInfo {
	
	/**
	 * 视图类型
	 * TYPE_STANDARD：标准的本系统数据过滤视图
	 * TYPE_LINKED：链接其他页面，或者通过js控制弹出窗口的视图
	 */
	public final static int TYPE_STANDARD=0;
	public final static int TYPE_LINKED=1;

	protected int oid;

	protected String name;

	protected String applyTo;

	protected String configure;
	
	/**
	 * history:已经完成的；current:当前正在处理的
	 */
	protected String dataSource;
	
	protected String workflowOid;
	
	
	protected String createTimeBegin;
	protected String createTimeEnd;
	
	
	
	/**
	 * List[FiltersInfo]
	 */
	protected ViewFilter filter = new ViewFilter();
	
	/**
	 * List[ColumnsInfo]
	 */
	protected List column = new ArrayList();
	
	/**
	 * 需要过滤原始数据
	 * List[Map['s':'32',....],....]
	 */
	protected List oriData;
	
	protected int type = 0;
	
	protected String url = "";
	
	protected String script = "";
	
	/**
	 * 视图表格上对记录操作的动作
	 */
	private Map action = new HashMap();
	
	/**
	 * s视图表格的按钮
	 */
	private List button = new ArrayList();
	
	private String rule = "";
	
	
	
	/**
	 * List[String[colEN,colZH],....]
	 */
	private List searchCol = new ArrayList();
	
	
	public static String defaultConfig() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>");
		sb.append("<attribute>");
		sb.append("</attribute>");

		return sb.toString();
	}
	
	public ViewInfo(){
	}
	
	public ViewInfo(ResultSet rs) throws SQLException, IOException{
		setOid(rs.getInt("VIEW_OID"));
		setName(rs.getString("VIEW_NAME"));
		setApplyTo(rs.getString("VIEW_APPLYTO"));

		Clob clob = rs.getClob("VIEW_CONFIGURE");
		setConfigure(ResultSetOperation.clobToString(clob));
	}
	
	/**
	 * 分析configure配置信息
	 *
	 */
	public void parseXml() {
		try {
			if (configure == null || configure.equals(""))
				configure = defaultConfig();
			Document doc = XmlUtil.parseString(configure);
			Element dataE_ = (Element)doc.getRootElement();
			dataSource = dataE_.attributeValue("dataSource");
			createTimeBegin = dataE_.attributeValue("createTimeBegin");
			createTimeEnd = dataE_.attributeValue("createTimeEnd");
			workflowOid = dataE_.attributeValue("workflow");
			
			if(dataE_.attributeValue("type")!=null && !dataE_.attributeValue("type").equals(""))
				type = Integer.parseInt(dataE_.attributeValue("type"));
			
			//读取规则
			Element ruleStr = (Element)doc.selectSingleNode("/attribute/rule");
			if (ruleStr!=null)
				rule = ruleStr.getText()==null?"":ruleStr.getText();
			//过滤器
			Element com_exp = (Element)doc.selectSingleNode("/attribute/filters");
			if (com_exp!=null)
				filter.parse(com_exp);
			//url
			Element url_ = (Element)doc.selectSingleNode("/attribute/url");
			if (url_!=null)
				url = url_.getText();
			//script
			Element script_ = (Element)doc.selectSingleNode("/attribute/script");
			if (script_!=null)
				script = script_.getText();
			//读取显示列信息
			List columns = doc.selectNodes("/attribute/columns/column");
			for (int i = 0; i < columns.size(); i++) {
				Element column_ele = (Element)columns.get(i);
				ViewColumnInfo column_ = new ViewColumnInfo();
				
				column_.setNameEN(column_ele.attributeValue("nameEN"));
				column_.setNameZH(column_ele.attributeValue("nameZH")==null?column_ele.attributeValue("nameEN"):column_ele.attributeValue("nameZH"));
				
				String sort = column_ele.attributeValue("sort");
				column_.setSort((sort!=null && sort.equals("true"))?true:false);
				
				String group = column_ele.attributeValue("group");
				column_.setGroup((group!=null && group.equals("true"))?true:false);
				
				String dSort = column_ele.attributeValue("defaultSort");
				column_.setDefaultSort((dSort!=null && dSort.equals("true"))?true:false);
				
				String groupLevel = column_ele.attributeValue("groupLevel");
				if (groupLevel!=null && !groupLevel.equals(""))
					column_.setGroupLevel(Integer.parseInt(groupLevel));
				
				String origin = column_ele.attributeValue("origin");
				column_.setOrigin((origin==null || origin.equals(""))?"ITSM":origin);
			
				
				if (column==null) {
					column = new ArrayList();
				}
				column.add(column_);
			}
			
			//读取检索列表
			List searchCols = doc.selectNodes("/attribute/searchs/column");
			for (int i = 0; i < searchCols.size(); i++) {
				Element column_ele = (Element)searchCols.get(i);
				ViewSearchInfo search_ = new ViewSearchInfo();

				search_.setNameEN(column_ele.attributeValue("nameEN"));
				search_.setNameZH(column_ele.attributeValue("nameZH")==null?column_ele.attributeValue("nameEN"):column_ele.attributeValue("nameZH"));
				String origin = column_ele.attributeValue("origin");
				search_.setOrigin(origin==null||origin.equals("")?"ITSM":origin);
				if (searchCol==null) {
					searchCol = new ArrayList();
				}
				searchCol.add(search_);
			}
			
			//读取动作列表
			List actions = doc.selectNodes("/attribute/actions/action");
			for (int i = 0; i < actions.size(); i++) {
				Element action_ele = (Element)actions.get(i);
				action.put(action_ele.attributeValue("name"), action_ele.getText());
			}
			
			//读取按钮列表
			List buttons = doc.selectNodes("/attribute/buttons/button");
			for (int i = 0; i < buttons.size(); i++) {
				Element button_ele = (Element)buttons.get(i);
				String[] s = new String[2];
				s[0] = button_ele.attributeValue("name");
				s[1] = button_ele.getText();
				button.add(s);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public ViewFilter getViewFilter() {
		return filter;
	}
	
	public List getColumns() {
		return column;
	}
	
	/**
	 * 获取itsm task数据，用于视图过滤,
	 * @param operName
	 * @param request
	 * @return List[]
	 */
	public List getTaskData(String operName,HttpServletRequest request) throws Exception{
		List retList = new ArrayList();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String defaultBefore = this.getAttribute("beforeDayNo");
		
		//工单系统来源["ALL","ITSM","OVSD"....]
		String origin = this.getAttribute("systemOrigin");
		if (origin==null || origin.equals(""))
			origin = "ALL";
		
		//开始时间
		String beginTime = "";
		if (request!=null)
			beginTime = request.getParameter("fld_system_create_time_b");
		if (beginTime==null || beginTime.equals("")){
			beginTime = "1980-12-03";
		} else {
			if (beginTime.length()>10)
				beginTime = beginTime.substring(0,10);
		}
		
		//结束时间
		String endTime = "";
		if (request!=null)
			endTime = request.getParameter("fld_system_create_time_e");
		if (endTime==null || endTime.equals("")){
			endTime = sdf.format(new Date(System.currentTimeMillis()));
		} else {
			if (endTime.length()>10)
				endTime = endTime.substring(0,10);
		}
		
		long bt = sdf.parse(beginTime).getTime();
		long et = sdf1.parse(endTime+" 23:59:59").getTime();
		
		List wfOidList = new ArrayList();
		if (workflowOid!=null && !workflowOid.equals("")){
			String[] wfOids= workflowOid.split(",");
			for(int i = 0; i < wfOids.length; i++) {
				if (wfOids[i]==null || wfOids[i].equals(""))
					continue;
				wfOidList.add(wfOids[i]);
			}
		} else {
			wfOidList.add("-1");
		}
		
		System.out.println(sdf1.format(new Date())+":"+this.name+"---开始取数据.....");

		try {
			List taskTemp_ = new ArrayList();
			//取打开的工单
			if (!dataSource.equals("history")) {
				List openTask = new ArrayList();
				for(int i = 0; i < wfOidList.size(); i++) {
					int wfOid = Integer.parseInt((String)wfOidList.get(i));
					if (wfOidList.size()>1 && wfOid==-1)
						continue;
					List ot = TaskManager.getAllOpenedTaskInfos(origin,wfOid);
					openTask.addAll(ot);
				}
				//System.out.println("viewInfo.java:---打开的单子："+openTask.size());
				for (int i = 0; i < openTask.size(); i++) {
					TaskInfo info = (TaskInfo)openTask.get(i);
					long createTime = info.getCreateTime();

					if (createTime>=bt && createTime<=et)
						taskTemp_.add(info);
				}
			}
			//取关闭的工单
			if(!dataSource.equals("current")){
				for(int i = 0; i < wfOidList.size(); i++) {
					int wfOid = Integer.parseInt((String)wfOidList.get(i));
					if (wfOidList.size()>1 && wfOid==-1)
						continue;
					List ct = TaskManager.getAllClosedTaskInfos(origin,beginTime,endTime,wfOid);
					taskTemp_.addAll(ct);
				}
			}
			System.out.println(sdf1.format(new Date())+":"+this.name+"---视图原始工单数量："+taskTemp_.size());
			
			Map<String,Map<String,String>> tempMap_ = new HashMap<String,Map<String,String>>();
			//根据过滤器进行过滤
			
			filter.dealMacro(operName);

			for (int i = 0; i < taskTemp_.size(); i++) {
				TaskInfo info = (TaskInfo)taskTemp_.get(i);
				if (info.getStatus() == TaskInfo.STATUS_OPEN) {
					List taskDataL = info.getTaskData();
					for (int j  = 0; j < taskDataL.size(); j++) {
						TaskData tempTD_ = (TaskData)taskDataL.get(j);
						if (filter.applyFilter(tempTD_,operName))
							retList.add(tempTD_.getAllData());
					}
//					if (applyFilter(info,operName)) {
//						tempMap_.put(info.getOrigin()+"_"+info.getOid(), info.getValues());
//					}
				} else {
					Map<String,String> valuesMap = info.getValues();
					if (filter.applyFilter(valuesMap,operName)) {
						if (info.getParentOid()!=-1) {
							//if (tempMap_.get(info.getOrigin()+"_"+info.getParentOid()) == null) {
							//	TaskInfo parentInfo = TaskManager.getTaskInfoByOid(info.getOrigin(), info.getParentOid(),info.getWfOid());
							valuesMap.put("TASK_OID", info.getParentOid()+"");
							valuesMap.put("OID", info.getParentOid()+"");
							if (info.getPwfOid()!=-1){
								valuesMap.put("TASK_WF_OID", info.getPwfOid()+"");
								valuesMap.put("WORKFLOW", WorkflowManager.getWorkflowByOid(info.getPwfOid()).getName());
								valuesMap.put("TASK_WF_NAME", WorkflowManager.getWorkflowByOid(info.getPwfOid()).getName());
							}
							tempMap_.put(info.getOrigin()+"_"+info.getParentOid(), valuesMap);
							//}
						} else
							tempMap_.put(info.getOrigin()+"_"+info.getOid(), valuesMap);
					}
				}
			}
			retList.addAll(tempMap_.values());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//System.out.println(retList.size());
		//applyFilter(retList, operName);
		return retList;
	}
	
	
	
	public String getDataSource() {
		return dataSource==null?"":dataSource;
	}

	public String getCreateTimeBegin() {
		return createTimeBegin;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setAttribute(ViewInfo info) {
		this.configure = info.configure;
		this.oid = info.oid;
		this.applyTo = info.applyTo;
		this.name = info.name;
		this.filter = info.filter;
		this.column = info.column;
	}
	
	/**
	 * 返回配置数据的根结点的属性值
	 * @param attrName
	 * @return
	 */
	public String getAttribute(String attrName){
		if (configure==null || configure.equals(""))
			return "";
		String retStr = "";
		try {
			Document doc = XmlUtil.parseString(configure);
			Element dataE_ = (Element)doc.getRootElement();
			retStr = dataE_.attributeValue(attrName);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retStr==null?"":retStr;
	}
	
	
	public List getChildValue(String childPath) {
		List retList = new ArrayList();
		if (configure==null || configure.equals(""))
			return retList;
		try {
			Document doc = XmlUtil.parseString(configure);
			List nodes = doc.selectNodes(childPath);
			for (int i = 0; i < nodes.size(); i++) {
				Element node = (Element)nodes.get(i);
				retList.add(node.getText());
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retList;
	}
	
	/**
	 * 返回一个节点的指定属性值,如果有多个path为childPath的节点，则返回第一个
	 * @param childPath 从根开始的节点路径
	 * @param attriName 节点上的属性名称
	 * @return 节点上的属性值
	 */
	public String getChildAttribute(String childPath,String attrName) {
		if (configure==null || configure.equals(""))
			return "";
		String retStr = "";
		try {
			Document doc = XmlUtil.parseString(configure);
			Element dataE_ = (Element)doc.selectSingleNode(childPath);
			retStr = dataE_.attributeValue(attrName);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retStr==null?"":retStr;
	}

	/**
	 * 返回归属模块的名字
	 * 
	 * @return
	 */
	public String getApplyToName() {
		return ModuleName.getModuleName(applyTo);
	}
	
	public String getApplyTo() {
		return applyTo;
	}
	public void setApplyTo(String applyTo) {
		this.applyTo = applyTo;
	}
	public String getConfigure() {
		return configure;
	}
	public void setConfigure(String configure) {
		this.configure = configure;
		this.parseXml();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	public String getGroupField(){
		if (column==null)
			return "";
		for (int i = 0;i < column.size(); i++) {
			ViewColumnInfo col = (ViewColumnInfo)column.get(i);
			if (col.isGroup())
				return col.nameEN;
		}
		return "";
	}
	
	public String getDefaultSortField() {
		if (column==null)
			return "";
		for (int i = 0;i < column.size(); i++) {
			ViewColumnInfo col = (ViewColumnInfo)column.get(i);
			if (col.isDefaultSort())
				return col.nameEN;
		}
		return "";
	}

	public Map getAction() {
		return action;
	}

	public void setAction(Map action) {
		this.action = action;
	}
	
	
	public String getRule() {
		return rule;
	}

	public List getSearchCol() {
		return searchCol;
	}

	public void setSearchCol(List searchCol) {
		this.searchCol = searchCol;
	}

	public String getWorkflowOid() {
		return workflowOid==null?"":workflowOid;
	}
	
	public ViewInfo cloneInfo(){
		ViewInfo retInfo = new ViewInfo();
		retInfo.setOid(this.getOid());
		retInfo.setName(this.getName());
		retInfo.setApplyTo(this.getApplyTo());
		retInfo.setConfigure(this.getConfigure());
		return retInfo;
	}

	public List getButton() {
		return button;
	}

	public void setButton(List button) {
		this.button = button;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url == null ? "": url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getScript() {
		return script == null ? "" : script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	
	

}
