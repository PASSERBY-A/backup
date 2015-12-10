<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getUserId.jsp"%>
<%

if (!PersonManager.isSystemManager(userId)){
	out.print("您没有权限");
	return;
}
%>