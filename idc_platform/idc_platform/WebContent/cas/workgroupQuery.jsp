<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ include file="getPurview.jsp"%>



<%
List workgroups = new ArrayList();
String type = request.getParameter("type");
if (type == null || type.equals(""))
	type = "all";
String personId = request.getParameter("userId");
personId = personId == null?"":personId;
if (type.equals("user")){
	workgroups = WorkgroupManager.getWorkgroupsOfPerson(personId,true);
} else if(type.equals("forAdd")){
	workgroups = WorkgroupManager.getWorkgroupsForAddByPerson(personId,true);
}  else if(type.equals("tree")){
	String nodeId = request.getParameter("node");
	workgroups = WorkgroupManager.getWorkgroupByParentId(nodeId);
} else{
	workgroups = WorkgroupManager.getAllWorkgroup(true);
}
%>

{"records" : [
<%
for (int i = 0; i < workgroups.size(); i++){
	WorkgroupInfo wgInfo = (WorkgroupInfo)workgroups.get(i);
	if (i>0)
		out.println(",");
	out.print("{");
	out.print("'parentId':'"+wgInfo.getParentId()+"' ,");
	out.print("'id':'"+wgInfo.getId()+"',");
	out.print("'name':'"+wgInfo.getName()+"',");
	out.print("'status':'"+wgInfo.getStatus()+"'");
	out.print("}");
}
%>
],
"totalCount" : <%=workgroups.size()%>
}