<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ include file="getPurview.jsp"%>



<%

List organizations = new ArrayList();
String type = request.getParameter("type");
if (type == null || type.equals(""))
	type = "all";

String personId = request.getParameter("userId");
personId = personId == null?"":personId;
if (type.equals("user")){
	OrganizationInfo ogInfo = OrganizationManager.getOrganizationOfPerson(personId,true);
	if (ogInfo!=null)
		organizations.add(ogInfo);
} else if(type.equals("forAdd")){
	organizations = OrganizationManager.getOrganizationForAddByPerson(personId,true);
} else{
	organizations = OrganizationManager.getAllOrganization(true);
}

String filter = request.getParameter("filter");
filter = (filter==null?"":filter);

if (!filter.equals("")){
	for (int i = 0; i < organizations.size(); i++) {
		OrganizationInfo ogInfo = (OrganizationInfo)organizations.get(i);
			if (ogInfo.getId().indexOf(filter)!=-1)
				continue;
			if (ogInfo.getName().indexOf(filter)!=-1)
				continue;
			
			organizations.remove(i);
			i--;
	}
}

int limit = 0;
int start = 0;
String limit_s = request.getParameter("limit");
if (limit_s!=null && !limit_s.equals(""))
	limit = Integer.parseInt(limit_s);
else
	limit = organizations.size();
String start_s = request.getParameter("start");
if (start_s != null)
	start = Integer.parseInt(start_s);

%>

{"records" : [
<%
int count = 0;
for (int i = start; i < organizations.size() && i < (start+limit); i++){
	OrganizationInfo ogInfo = (OrganizationInfo)organizations.get(i);
	if (count>0)
		out.println(",");
	out.print("{");
	out.print("'parentId':'"+ogInfo.getParentId()+"' ,");
	out.print("'id':'"+ogInfo.getId()+"',");
	out.print("'name':'"+ogInfo.getName()+"',");
	out.print("'status':'"+ogInfo.getStatus()+"'");
	out.print("}");
	count++;
}
%>
],
"totalCount" : <%=organizations.size()%>
}