<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.mgr.MessageMgr"%>
<%@page import="com.hp.idc.portal.message.Message"%>
<%
	String oid = request.getParameter("oid");
	Message msg = MessageMgr.getMessageById(Integer.parseInt(oid));
%>
<html>
<head>
<style type="text/css">
#posts-container			{ width:400px; border:1px solid #ccc; -webkit-border-radius:10px; -moz-border-radius:10px;background-color: #fff; }
.post						{ padding:5px 10px 5px 100px; min-height:65px; border-bottom:1px solid #ccc; background:url(/portal/style/img/dwloadmore.png) 5px 5px no-repeat; cursor:pointer;  }
.post:hover					{ background-color:lightblue; }
a.post-title 				{ font-weight:bold; font-size:12px; text-decoration:none; }
a.post-title:hover			{ text-decoration:underline; color:#900; }
a.post-more					{ color:#900; }
p.post-content				{ font-size:12px; line-height:17px; padding-bottom:0; }
.activate					{ background:url(/portal/style/img/loadmorespinner.gif) 140px 9px no-repeat #eee; }
</style>
</head>
<body>
<div id="posts">
<div class="post" id="post-undefined" >
<a href="<%=msg.getUrl() %>" class="post-title"><%=msg.getTitle() %></a><p class="post-content"><%=msg.getContent() %><br /><a href="<%=msg.getUrl() %>" class="post-more">Read more...</a></p>
</div>
</div>
</body>
</html>
