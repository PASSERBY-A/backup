<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%@ page import="com.hp.idc.itsm.util.StringUtil"%>
<%@ include file="../../getUser.jsp"%>

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

List viewList = ViewManager.getViewsOfUser(userId);

%>
{	"totalCount" : <%=viewList.size()%>,
	"success" : true,
	"views" : [
<%
	if (viewList!=null)
	for (int i = start; i < start + limit && i < viewList.size(); i++) {
		if (i > start)
			out.print(",");
		out.print("{");
		ViewInfo viewInfo = (ViewInfo)viewList.get(i);
		out.print("'oid':"+viewInfo.getOid()+",");
		out.print("'name':'"+StringUtil.escapeJavaScript(viewInfo.getName())+"',");
		out.print("'type':'"+viewInfo.getType()+"',");
		out.print("'rule':'"+StringUtil.escapeJavaScript(RuleManager.ruleDesc(viewInfo.getRule()))+"',");
		out.print("'applyto':'"+StringUtil.escapeJavaScript(viewInfo.getApplyToName())+"'");
		out.print("}");
	}
%>

]}