<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.StringUtil"%>
<%@ include file="../regex.jsp"%>

[
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

try {
	String nodeId = request.getParameter("node");
	if (nodeId!=null && nodeId.equals("_"))
		nodeId = "-1";
		
	boolean includeAll = true;
	if (request.getParameter("includeAll")!=null && !request.getParameter("includeAll").equals(""))
		includeAll = Boolean.parseBoolean(request.getParameter("includeAll"));
	
	String wfOid = request.getParameter("wfOid");
	List lgList = new ArrayList();
	if (wfOid != null && !wfOid.equals(""))
		lgList = LocalgroupManager.getGroupsOfWF(wfOid,false);
	else
		lgList = LocalgroupManager.getAllLocalgroup(true);

	String nodePath = request.getParameter("nodePath");
	String regexId = request.getParameter("regexId");
	String selectGroup = request.getParameter("selectGroup");
	
	String[] filter = null;
	if (regexId==null || regexId.equals("")) {
		regexId = "/-1/";
	}
	filter = regexId.split(";");

Collections.sort(lgList, new Comparator() {
	public int compare(Object a, Object b) {
		String id1 = ((LocalgroupInfo)a).getName();
		String id2 = ((LocalgroupInfo)b).getName();
		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA); 
		return cmp.compare(id1,id2);
	}
});

	int count = 0;
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < lgList.size(); i++){
		LocalgroupInfo ogInfo = (LocalgroupInfo)lgList.get(i);

		boolean[] ret = isWaster(ogInfo.getId(),filter,nodePath);

		if (ret[0])
			continue;
		if (count>0)
			sb.append(",");
		sb.append("{");
		sb.append("'id':'"+ogInfo.getId()+"',");
		if (ogInfo.getStatus() == LocalgroupInfo.STATUS_DELETED)
			sb.append("'text':'<del>"+ogInfo.getName()+"</del>'");
		else
			sb.append("'text':'"+ogInfo.getName()+"'");
		sb.append(",'leaf':true");
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