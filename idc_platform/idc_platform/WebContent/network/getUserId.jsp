<%@ page language="java" pageEncoding="gbk"%>
<%@page import="com.hp.idc.common.Constant"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import = "com.mercury.sitescope.platform.security.passwords.tool.loginEncrypt" %>

<%
String NNMBaseURL = "gscm.idc.com";
String SitescopeBaseURL = "111.11.181.100:8080";
String EXTJS_HOME = "/extjs";
String userId = (String)session.getAttribute(Constant.SESSION_LOGIN);
String userName = "system";
String password = "gsidc";
String userCode = "";
String passwordCode = "";
String sisUserName = "Administrator";
String sisPassword = "gsidc";
PersonInfo pi = PersonManager.getPersonById(userId);
if (pi!=null){
	
	String code = loginEncrypt.getEncryp(sisUserName);
	userCode = java.net.URLEncoder.encode(code);
	code = loginEncrypt.getEncryp(sisPassword);
	passwordCode = java.net.URLEncoder.encode(code);
}
%>

