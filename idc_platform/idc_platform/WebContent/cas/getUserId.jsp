<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>

<%
String EXTJS_HOME = "/extjs";
String userId = "root";
String userName = "root";
PersonInfo pi = PersonManager.getPersonById(userId);
if (pi!=null)
	userName = pi.getName();

String clientIp = request.getHeader("x-forwarded-for");
if (clientIp == null || clientIp.length() == 0
		|| "unknown".equalsIgnoreCase(clientIp)) {
	clientIp = request.getHeader("Proxy-Client-IP");
}
if (clientIp == null || clientIp.length() == 0
		|| "unknown".equalsIgnoreCase(clientIp)) {
	clientIp = request.getHeader("WL-Proxy-Client-IP");
}
if (clientIp == null || clientIp.length() == 0
		|| "unknown".equalsIgnoreCase(clientIp)) {
	clientIp = request.getRemoteAddr();
}

String userIp = clientIp;
%>