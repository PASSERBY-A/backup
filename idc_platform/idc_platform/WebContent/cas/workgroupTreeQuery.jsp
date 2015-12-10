<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ include file="getPurview.jsp"%>
<%@ include file="regex.jsp"%>

[
<%
try {
	String nodeId = request.getParameter("node");
	String showDel = request.getParameter("showDel");
	if (nodeId!=null && nodeId.equals("_"))
		nodeId = "-1";
		
	boolean includeAll = true;
	if (request.getParameter("includeAll")!=null && !request.getParameter("includeAll").equals(""))
		includeAll = Boolean.parseBoolean(request.getParameter("includeAll"));
	List organizations = WorkgroupManager.getWorkgroupByParentId(nodeId,includeAll);
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
		String id1 = ((WorkgroupInfo)a).getName();
		String id2 = ((WorkgroupInfo)b).getName();
		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA); 
		return cmp.compare(id1,id2);
	}
});

	int count = 0;
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < organizations.size(); i++){
		WorkgroupInfo ogInfo = (WorkgroupInfo)organizations.get(i);
		if(showDel!=null&&"false".equals(showDel)
			&&ogInfo.getStatus() == WorkgroupInfo.STATUS_DELETE){
			continue;
		}
		boolean[] ret = isWaster(ogInfo.getId(),filter,nodePath);

		if (ret[0])
			continue;
		if (count>0)
			sb.append(",");
		sb.append("{");
		sb.append("'id':'"+ogInfo.getId()+"',");
		if (ogInfo.getStatus() == WorkgroupInfo.STATUS_DELETE)
			sb.append("'text':'<del>"+ogInfo.getName()+"</del>'");
		else
			sb.append("'text':'"+ogInfo.getName()+"'");

		//List temp_ = WorkgroupManager.getWorkgroupByParentId(ogInfo.getId());
		//这样判断速度太慢，去掉了
		//if (temp_==null || temp_.size() == 0)
		//	sb.append(",'leaf':true");
		if (ret[1])
			sb.append(",'_click':true");
		else
			sb.append(",'_click':false");
		if ("1".equals(selectGroup))
			sb.append(",'checked':false");
		sb.append("}");
		count++;
	}
	out.print(sb.toString());

}catch (Exception e) {
	e.printStackTrace();
}
%>
]