<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getUserId.jsp"%>
<%

if (!PersonManager.isSystemManager(userId)){
	out.print("��û��Ȩ��");
	return;
}
%>