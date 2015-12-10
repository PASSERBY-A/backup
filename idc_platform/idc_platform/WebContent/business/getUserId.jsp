
<%@page import="com.hp.idc.common.Constant"%><%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>

<%
String EXTJS_HOME = "/extjs";
String userId = (String)session.getAttribute(Constant.SESSION_LOGIN);
String userName = "";
PersonInfo pi = PersonManager.getPersonById(userId);
if (pi!=null)
	userName = pi.getName();

%>