<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.*"%>

<%@ include file="getPurview.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");

try{

List roles = new ArrayList();
String moId = request.getParameter("moId");
roles = RoleManager.getRolesOfMo(moId);

Collections.sort(roles, new Comparator() {
	public int compare(Object a, Object b) {
		String id1 = ""+((RoleInfo)a).getLevel();
		String id2 = ""+((RoleInfo)b).getLevel();
		return id1.compareTo(id2);
	}
});
%>
{
"totalCount" : <%=roles.size()%>,
"records" : [
<%
int count = 0;
for (int i = 0; i < roles.size(); i++){
	RoleInfo rInfo = (RoleInfo)roles.get(i);
	if (count>0)
		out.println(",");
	out.print("{");
	out.print("'id':'"+rInfo.getId()+"',");
	out.print("'name':'"+rInfo.getName()+"',");
	out.print("'status':'"+rInfo.getStatus()+"',");
	out.print("'level':'"+rInfo.getLevel()+"'");
	out.print("}");
	count++;
}
%>

]}

<%
} catch(Exception e){
	e.printStackTrace();
}
%>