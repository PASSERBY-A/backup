<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.regex.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ include file="../../getUser.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("gbk");
try{

int viewOid = -1;
if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
	viewOid = Integer.parseInt(request.getParameter("viewOid"));
ViewInfo vInfo = ViewManager.getViewByOid(viewOid);

List columns = vInfo.getColumns();
long b = System.currentTimeMillis();

List filterData = vInfo.getTaskData(userId,request);

System.out.println("viewTemplateQueryExport.jsp:第一次过滤后："+filterData.size());


//对结果集利用检索条件进行二次过滤
List searchList = vInfo.getSearchCol();
for (int j = 0; j < searchList.size(); j++) {
	ViewSearchInfo cols = (ViewSearchInfo) searchList.get(j);

	//程序自动生成的查询字段
	//if (cols.getNameEN().startsWith("fld_system_create_time"))
	//	continue;

	String regValue = null;
	String regValue_b = null;
	String regValue_e = null;
	FieldInfo fInfo = FieldManager.getFieldById(cols.getOrigin(),cols.getNameEN());
	if (fInfo==null)
		continue;
	if (fInfo.getType().equals("com.hp.idc.itsm.configure.fields.DateFieldInfo") ||
				fInfo.getType().equals("com.hp.idc.itsm.configure.fields.DateTimeFieldInfo")) {
		regValue_b = request.getParameter("fld_"+cols.getNameEN()+"_b");
		regValue_e = request.getParameter("fld_"+cols.getNameEN()+"_e");
		if ((regValue_b==null || regValue_b.equals("")) && (regValue_e==null || regValue_e.equals("")))
			continue;
	} else {
		regValue = request.getParameter("fld_"+cols.getNameEN());
		if (regValue==null || regValue.equals(""))
			continue;
	}

	for (int i = 0; i < filterData.size(); i++) {
		Map obj = (Map)filterData.get(i);


		String oriValue = (String)obj.get(cols.getNameEN().toUpperCase());
		if (oriValue == null){
			filterData.remove(i);
			i--;
			continue;
		}

		if (fInfo.getType().equals("com.hp.idc.itsm.configure.fields.DateFieldInfo") ||
				fInfo.getType().equals("com.hp.idc.itsm.configure.fields.DateTimeFieldInfo")) {
				if (regValue_b!=null && regValue_b.trim().length()!=0){
					if (oriValue.compareTo(regValue_b)<0) {
						filterData.remove(i);
						i--;
						continue;
					}
				}
				if (regValue_e!=null && regValue_e.trim().length()!=0){
					if (oriValue.length()>10)
						oriValue = oriValue.substring(0,10);
					if (oriValue.compareTo(regValue_e)>0) {
						filterData.remove(i);
						i--;
						continue;
					}
				}
		}else {
			String task_status = (String)obj.get("TASK_STATUS");
			int wfOid = Integer.parseInt((String)obj.get("TASK_WF_OID"));
			String rgV_ = regValue;
			if (task_status.equals("关闭")|| task_status.equals("强制关闭")) {
				WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
				Map columnFields = wfInfo.getColumnFieldsMap();
				if (columnFields.get(fInfo.getId().toUpperCase())!=null)
					rgV_ = fInfo.getHtmlCode(regValue);
			}
			if (fInfo.getType().equals("com.hp.idc.itsm.configure.fields.SelectFieldInfo")||
					fInfo.getType().equals("com.hp.idc.itsm.configure.fields.PersonFieldInfo")){
				rgV_ = "^"+rgV_+"$";
			}
			Pattern pattern = Pattern.compile(rgV_);
			Matcher matcher = pattern.matcher(oriValue);
			if (!matcher.find()){
			  filterData.remove(i);
				i--;
				continue;
			}
		}
	}
}

for (int i = 0 ; i < filterData.size(); i++) {
	for (int j = 1; j < filterData.size()-i; j++) {
		Map obj = (Map)filterData.get(j-1);
		Map obj2 = (Map)filterData.get(j);
		String s1 = (String)obj.get("TASK_OID");
		String s2 = (String)obj2.get("TASK_OID");

		int oid1 = Integer.parseInt(s1);
		int oid2 = Integer.parseInt(s2);
		
		if (oid1 < oid2) {
			filterData.remove(j-1);
			filterData.remove(j-1);
			filterData.add(j-1,obj);
			filterData.add(j-1,obj2);
		}
	}
}

String recordCount = vInfo.getAttribute("recordCountPerPage");
int limit = 1000;
if (recordCount != null && !recordCount.equals(""))
	limit = Integer.parseInt(recordCount);


StringBuffer sb = new StringBuffer();

sb.append("{");

//sb.append("fileName:'"+vInfo.getName()+".xls',");
sb.append("fileName:'"+vInfo.getName()+"',");

sb.append("value:[");

for (int i = 0; i < filterData.size(); i++) {
	Map<String,String> obj = (Map)filterData.get(i);
	if (i>0)
		sb.append(",");
	sb.append("{");
	boolean hasOne = false;
	sb.append("'oid':"+obj.get("TASK_OID"));
	//sb.append("'wfOid':'"+obj.get("TASK_WF_OID") +"',");
	//sb.append("'wfVer':'"+obj.get("TASK_WF_VER") +"',");
	WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(Integer.parseInt((String)obj.get("TASK_WF_OID")));

	for (int j = 0; j < columns.size(); j++) {
		ViewColumnInfo column = (ViewColumnInfo)columns.get(j);
		String key = (String)column.getNameEN();
		key = key == null?"":key;
		String value = (String)obj.get(key.toUpperCase());
		FieldInfo fieldInfo = FieldManager.getFieldById(column.getOrigin(),key);
		if(fieldInfo!=null) {
			if (fieldInfo.isSystem())
				value = obj.get(key.toUpperCase());
			else
				value = obj.get(key);
			if (fieldInfo instanceof PersonFieldInfo) {
				fieldInfo.setOrigin((String)obj.get("TASK_ORIGIN"));
				if (value!=null && value.indexOf("/")!=-1)
					value = ((PersonFieldInfo)fieldInfo).getPathValue(value==null?"":value);
				else
					value = fieldInfo.getHtmlCode(value==null?"":value);
			} else
				value = fieldInfo.getHtmlCode(value==null?"":value);
		}
		sb.append(",");
		sb.append("'"+key+"':'"+StringUtil.escapeJavaScript(value) +"'");
	}
	sb.append("}");

}
sb.append("],");

sb.append("head:[{id:'oid',name:'OID'}");
for (int i = 0; i < columns.size(); i++) {
	ViewColumnInfo column = (ViewColumnInfo)columns.get(i);
	sb.append(",");
	sb.append("{id:'"+column.getNameEN()+"',name:'"+column.getNameZH()+"'}");
}
sb.append("]");
sb.append("}");
out.println(sb.toString());
	}catch (Exception e) {
		e.printStackTrace();
	}
%>