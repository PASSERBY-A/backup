<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ include file="getPurview.jsp"%>
<%@ include file="regex.jsp"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
request.setCharacterEncoding("UTF-8");

try{
	String singleMode = request.getParameter("singleMode");
	if (singleMode == null || singleMode.equals(""))
		singleMode = "true";
	String pathMode = request.getParameter("pathMode");
	if (pathMode == null || pathMode.equals(""))
		pathMode = "false";
	String personStr = request.getParameter("s_value");
	if (personStr == null)
		personStr = "";
		
	String filter = request.getParameter("filter");
	if (filter == null)
		filter = "";
		
	String groupType = request.getParameter("groupType");
	if (groupType == null)
		groupType = "1";
		
	String selectGroup = request.getParameter("selectGroup");
	if (selectGroup == null)
		selectGroup = "0";

String id = request.getParameter("id");
if (id == null)
	id  = "";

List persons = PersonManager.getAllPersons(false);
for (int i = 0; i < persons.size(); i++) {
	PersonInfo pInfo = (PersonInfo)persons.get(i);
	if (pInfo.getId().startsWith(id) && isNeededPerson(pInfo.getId(),filter,groupType))
		continue;
	persons.remove(i);
	i--;
}

%>
{
"totalCount" : <%=persons.size()%>,
"records" : [
<%
int count = 0;
for (int i = 0; i < persons.size(); i++){
	PersonInfo pInfo = (PersonInfo)persons.get(i);
	if (count>0)
		out.println(",");
	out.print("{");
	out.print("'id':'"+pInfo.getId()+"',");
	out.print("'name':'"+pInfo.getName()+"',");
	out.print("'mobile':'"+pInfo.getMobile()+"',");
	out.print("'email':'"+pInfo.getEmail()+"',");
	out.print("'status':'"+pInfo.getStatus()+"',");
	out.print("'p_status':'"+pInfo.getPersonStatus()+"'");
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