<%@ page language="java" pageEncoding="gbk"%>
<%@page import="com.hp.idc.portal.security.PersonManager"%>
<%@page import="com.hp.idc.portal.security.PersonInfo"%>
<%@page import="com.hp.idc.common.Constant"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>

<%

response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

String _userId = (String)session.getAttribute(Constant.SESSION_LOGIN);
String userName = "";
int userId = 0;
PersonInfo pi = PersonManager.getPersonById(_userId);

if (pi!=null) {
	userName = pi.getName();
	userId = pi.getOid();
}
out.write(userId+";"+userName);
%>