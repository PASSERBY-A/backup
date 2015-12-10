<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.regex.*"%>
<%@ page import="java.sql.*"%>
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
request.setCharacterEncoding("UTF-8");
try{

String start_s = request.getParameter("start");
int start = 0;
if (start_s != null)
	start = Integer.parseInt(start_s);
int viewOid = -1;
if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
	viewOid = Integer.parseInt(request.getParameter("viewOid"));
ViewInfo vInfo = ViewManager.getViewByOid(viewOid);

if (vInfo == null)
	System.out.println("err:viewoid="+viewOid+"");
List columns = vInfo.getColumns();
long b = System.currentTimeMillis();

SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

List filterData = vInfo.getTaskData(userId,request);
System.out.println(sdf1.format(new java.util.Date())+":"+vInfo.getName()+"---第一次过滤后："+filterData.size());
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

		String searchId = cols.getNameEN();
		if (fInfo.isSystem())
			searchId = cols.getNameEN().toUpperCase();
		String oriValue = (String)obj.get(searchId);
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
System.out.println(sdf1.format(new java.util.Date())+":"+vInfo.getName()+"---第2次过滤后："+filterData.size());

Collections.sort(filterData, new Comparator() { 
	public int compare(Object a, Object b) {
		int orderA = Integer.parseInt((String)((Map)a).get("TASK_OID"));
		int orderB = Integer.parseInt((String)((Map)b).get("TASK_OID"));
		return orderB - orderA;
	}
});

String recordCount = vInfo.getAttribute("recordCountPerPage");
int limit = 1000;
if (recordCount != null && !recordCount.equals(""))
	limit = Integer.parseInt(recordCount);

%>
{ "items" : [
<%
for (int i = start; i < filterData.size() && i<(start+limit); i++) {
	Map<String,String> obj = (Map)filterData.get(i);
	//System.out.println(obj);
	if (i>start)
		out.print(",");
	out.print("{");
	boolean hasOne = false;
	out.print("'oid':"+obj.get("TASK_OID") +",");
	out.print("'wait':'"+("true".equals(obj.get("TASK_WAIT"))?"等待处理":"<font color=green>正在处理</font>") +"',");
	out.print("'dataId':"+(obj.get("TASK_DATA_ID")==null?"0":obj.get("TASK_DATA_ID")) +",");
	out.print("'origin':'"+obj.get("TASK_ORIGIN") +"',");
	out.print("'overtime':'"+obj.get("OVERTIME") +"',");
	out.print("'wfOid':'"+obj.get("TASK_WF_OID") +"',");
	out.print("'wfVer':'"+obj.get("TASK_WF_VER") +"',");
	WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(Integer.parseInt((String)obj.get("TASK_WF_OID")));
	String dealPage = Consts.ITSM_HOME+"/task/taskInfo.jsp";
	if (wfInfo!=null)
			dealPage = wfInfo.getDealPage();
	dealPage = dealPage.replaceAll("\\$ITSM_HOME",Consts.ITSM_HOME);
	out.print("'dealPage':'"+StringUtil.escapeJavaScript(dealPage)+"'");

	for (int j = 0; j < columns.size(); j++) {
		ViewColumnInfo column = (ViewColumnInfo)columns.get(j);
		String key = (String)column.getNameEN();
		key = key == null?"":key;
		FieldInfo fieldInfo = FieldManager.getFieldById(column.getOrigin(),key,false);
		String value = "";
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
		out.print(",");
		out.print("'"+key+"':'"+StringUtil.escapeJavaScript(value) +"'");
	}
	out.print("}");

}

%>
	],
	"totalCount" : <%=filterData.size()%>
}

<%
	}catch (Exception e) {
		e.printStackTrace();
	}
%>