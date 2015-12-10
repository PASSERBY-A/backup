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

String limit_s = request.getParameter("limit");
String start_s = request.getParameter("start");
int limit = Consts.ITEMS_PER_PAGE;
if (limit_s != null)
	limit = Integer.parseInt(limit_s);
int start = 0;
if (start_s != null)
	start = Integer.parseInt(start_s);

List lgList = LocalgroupManager.getAllLocalgroup(true);

%>
{	"totalCount" : <%=lgList.size()%>,
	"success" : true,
	"lgs" : [
<%
	if (lgList!=null)
	for (int i = start; i < start + limit && i < lgList.size(); i++) {
		if (i > start)
			out.print(",");
		out.print("{");
		LocalgroupInfo lgInfo = (LocalgroupInfo)lgList.get(i);
		out.print("'id':'"+lgInfo.getId()+"',");
		out.print("'name':'"+StringUtil.escapeJavaScript(lgInfo.getName())+"',");
		out.print("'wfName':'"+lgInfo.getWfOid()+"',");
		out.print("'status':'"+lgInfo.getStatus()+"'");
		out.print("}");
	}
%>

]}