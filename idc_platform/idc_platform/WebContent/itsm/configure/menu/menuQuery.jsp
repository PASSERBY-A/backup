<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.authorization.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.util.StringUtil"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

String nid = request.getParameter("node");
if (nid.equals("_"))
	nid = "";
List<MenuInfo> menuList = MenuManager.getSubMenus(nid);

%>
[
<%
	if (menuList!=null)
	for (int i = 0; i < menuList.size(); i++) {
		if (i > 0)
			out.print(",");
		out.print("{");
		MenuInfo mInfo = menuList.get(i);
		out.print("'id':'"+mInfo.getId()+"',");
		out.print("'name':'"+StringUtil.escapeJavaScript(mInfo.getName())+"',");
		out.print("'url':'"+mInfo.getHref()+"',");
		out.print("uiProvider:'col',");
		if (mInfo.isLeaf())
			out.print("leaf: true,");
		out.print("'rule_str':'"+mInfo.getRuleStr()+"',");
		out.print("'rule_name':'"+StringUtil.escapeJavaScript(RuleManager.ruleDesc(mInfo.getRuleStr()))+"'");
		out.print("}");
	}
%>

]