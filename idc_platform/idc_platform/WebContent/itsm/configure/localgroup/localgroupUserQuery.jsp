<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.StringUtil"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
String lgId = request.getParameter("lgId");
lgId = lgId == null?"":lgId;

List<PersonInfo> lpList = LocalgroupManager.getPersonsOfLocalgroup(lgId, true);

%>
{	"totalCount" : <%=lpList.size()%>,
	"success" : true,
	"lgs" : [
<%
	if (lpList!=null)
	for (int i = 0; i < lpList.size(); i++) {
		if (i > 0)
			out.print(",");
		out.print("{");
		PersonInfo lgInfo = lpList.get(i);
		out.print("'id':'"+lgInfo.getId()+"',");
		out.print("'name':'"+StringUtil.escapeJavaScript(lgInfo.getName())+"',");
		out.print("'status':'"+lgInfo.getStatus()+"'");
		out.print("}");
	}
%>

]}