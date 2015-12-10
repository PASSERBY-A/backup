<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ include file="getPurview.jsp"%>
<%@ include file="regex.jsp"%>
[
<%
String nodeId = request.getParameter("node");
String showDel = request.getParameter("showDel");
if (nodeId!=null && nodeId.equals("_"))
		nodeId = "-1";
		
boolean includeAll = true;
if (request.getParameter("includeAll")!=null && !request.getParameter("includeAll").equals(""))
	includeAll = Boolean.parseBoolean(request.getParameter("includeAll"));
		
List organizations = OrganizationManager.getOrganizationByParentId(nodeId,includeAll);
String nodePath = request.getParameter("nodePath");
String regexId = request.getParameter("regexId");
String selectGroup = request.getParameter("selectGroup");

	String[] filter = null;
	if (regexId==null || regexId.equals("")) {
		regexId = "/-1/";
	}
		filter = regexId.split(";");

Collections.sort(organizations, new Comparator() {
	public int compare(Object a, Object b) {
		String id1 = ((OrganizationInfo)a).getName();
		String id2 = ((OrganizationInfo)b).getName();
		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA); 
		return cmp.compare(id1,id2);
	}
});

int count = 0;
for (int i = 0; i < organizations.size(); i++){
	OrganizationInfo ogInfo = (OrganizationInfo)organizations.get(i);
	if(showDel!=null&&"false".equals(showDel)
		&&ogInfo.getStatus() == OrganizationInfo.STATUS_DELETE){
		continue;
	}
	boolean[] ret = isWaster(ogInfo.getId(),filter,nodePath);
	if (ret[0])
			continue;
	if (count>0)
		out.println(",");
	out.print("{");
	out.print("'id':'"+ogInfo.getId()+"',");
	if (ogInfo.getStatus() == OrganizationInfo.STATUS_DELETE){
		out.print("'text':'<del>"+ogInfo.getName()+"</del>'");
	}else
		out.print("'text':'"+ogInfo.getName()+"'");
	//List temp_ = OrganizationManager.getOrganizationByParentId(ogInfo.getId());
	//if (temp_==null || temp_.size() == 0)
	//	out.print(",'leaf':true");
	if (ret[1])
		out.print(",'_click':true");
	else
		out.print(",'_click':false");
	if ("1".equals(selectGroup))
		out.print(",'checked':false");
	out.print("}");
	count++;
}

%>
]