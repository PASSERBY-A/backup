<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>
<%

Exception ex = null;
String operId = userId;
OperationResult or = new OperationResult();
	try {
		Map map = new HashMap();
		String login_repeat = request.getParameter("login_repeat");
		String login_locktime = request.getParameter("login_locktime");
		String pass_period = request.getParameter("pass_period");
		String pass_regulation = request.getParameter("pass_regulation");
		String pass_regulation_desc = request.getParameter("pass_regulation_desc");
		String pass_repeatTime = request.getParameter("pass_repeatTime");
		String pass_first = request.getParameter("pass_first");
		if (pass_first!=null && !pass_first.equals(""))
			pass_first = "true";
		String systemManager = request.getParameter("systemManager");
		map.put("login_repeat", login_repeat==null?"":login_repeat);
		map.put("login_locktime", login_locktime==null?"":login_locktime);
		map.put("pass_period", pass_period==null?"":pass_period);
		map.put("pass_regulation", pass_regulation==null?"":pass_regulation);
		map.put("pass_repeatTime", pass_repeatTime==null?"":pass_repeatTime);
		map.put("pass_first", pass_first==null?"":pass_first);
		map.put("systemManager", systemManager==null?"":systemManager);
		map.put("pass_regulation_desc", pass_regulation_desc==null?"":pass_regulation_desc);
		SystemManager.updateConfig(map);
	}catch(Exception e){
		e.printStackTrace();
		or.setSuccess(false);
		or.setMessage(e.toString());
	}

%>

<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>
