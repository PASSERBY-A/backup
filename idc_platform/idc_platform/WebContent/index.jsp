<%@page import="com.hp.idc.common.Constant"%><%
if(session.getAttribute(Constant.SESSION_LOGIN) != null){
	response.sendRedirect("portal/web/index.jsp");	
} else {
	response.sendRedirect("portal/login.html");
}
%>
