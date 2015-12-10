<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@include file="../getUser.jsp" %>
<%
	String url = request.getParameter("url");
	String num = request.getParameter("menu");
	num=num==null?"1":num;
%>
function writeHead (){
	document.write("<div class=\"nav_title\">");
	document.write("<div class=\"index_line1\">");
	document.write("<div class=\"index_line1Con\">");
	document.write("<div class=\"index_line1Rit\">");
	document.write("<div class=\"index_line1Rit1\" >");
	document.write("<span class=\"index_line1Span1\">欢迎您：管理员</span>");
	document.write("<span class=\"index_line1Span2\">注销</span>");
	document.write("<span class=\"index_line1Span3\"></span>");
	document.write("</div>");
	document.write("<br class=\"clearfloat\"/>");
	document.write("</div>");
	document.write("<br class=\"clearfloat\"/>");
	document.write("</div>");
 	document.write("</div>")
 	document.write("<div class=\"ul_nav\">");
 	document.write('<ul class="index_nav">');
 	
 	document.write('<li id="li5" onmouseover="action1(5)" onmouseout="disappear(5,<%=num %>)">');
 	document.write('<a href="/portal/" class="index_nav1">综合运营门户</a>');
 	document.write('<div class="index_line2" id="ul_tatle5" style="display:none;" >');
 	document.write('<ul class="index_SubNav Sub1" id="ul_block5" style="display:none;">');
	<%
    	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
 		List<Menu> list = mgr.getBeanByFilter(userId,"1");
 		for(int i=0; i<list.size(); i++){
 			Menu menu = list.get(i);
 	%>
 	document.write('<li><a href="#" onclick="openUrl(\'<%=menu.getUrl() %>\',5)" title="<%=menu.getAlt() %>"><%=menu.getTitle() %></a></li>');
 	<%} %>
 	document.write('<br class="clearfloat"/></ul></div></li>');
 	
 	document.write('<li id="li6" onmouseover="action1(6)" onmouseout="disappear(6,<%=num %>)">');
 	document.write('<a href="#" class="index_nav2">监控管理中心</a>');
 	document.write('<div class="index_line2" id="ul_tatle6" style="display:none;" >');
 	document.write('<ul class="index_SubNav Sub2" id="ul_block6" style="display:none;">');
 	<%
		list = mgr.getBeanByFilter(userId,"2");
		for(int i=0; i<list.size(); i++){
			Menu menu = list.get(i);
	%>
 	document.write('<li><a href="#" onclick="openUrl(\'<%=menu.getUrl() %>\',6)" title="<%=menu.getAlt() %>"><%=menu.getTitle() %></a></li>');
 	<%} %>
 	document.write('<br class="clearfloat"/></ul></div></li>');
 	
 	document.write('<li id="li7" onmouseover="action1(7)" onmouseout="disappear(7,<%=num %>)">');
 	document.write('<a href="#" class="index_nav2">业务管理中心</a>');
 	document.write('<div class="index_line2" id="ul_tatle7" style="display:none;" >');
 	document.write('<ul class="index_SubNav Sub2" id="ul_block7" style="display:none;">');
 	<%
		list = mgr.getBeanByFilter(userId,"3");
		for(int i=0; i<list.size(); i++){
			Menu menu = list.get(i);
	%>
 	document.write('<li><a href="#" onclick="openUrl(\'<%=menu.getUrl() %>\',7)" title="<%=menu.getAlt() %>"><%=menu.getTitle() %></a></li>');
 	<%} %>
 	document.write('<br class="clearfloat"/></ul></div></li>');
 	
 	document.write('<li id="li8" onmouseover="action1(8)" onmouseout="disappear(8,<%=num %>)">');
 	document.write('<a href="#" class="index_nav2">运维管理中心</a>');
 	document.write('<div class="index_line2" id="ul_tatle8" style="display:none;" >');
 	document.write('<ul class="index_SubNav Sub2" id="ul_block8" style="display:none;">');
 	<%
		list = mgr.getBeanByFilter(userId,"4");
		for(int i=0; i<list.size(); i++){
			Menu menu = list.get(i);
	%>
 	document.write('<li><a href="#" onclick="openUrl(\'<%=menu.getUrl() %>\',8)" title="<%=menu.getAlt() %>"><%=menu.getTitle() %></a></li>');
 	<%} %>
 	document.write('<br class="clearfloat"/></ul></div></li>');
 	
 	document.write('<li id="li9" onmouseover="action1(9)" onmouseout="disappear(9,<%=num %>)">');
 	document.write('<a href="#" class="index_nav2">运营分析中心</a>');
 	document.write('<div class="index_line2" id="ul_tatle9" style="display:none;" >');
 	document.write('<ul class="index_SubNav Sub2" id="ul_block9" style="display:none;">');
 	<%
		list = mgr.getBeanByFilter(userId,"5");
		for(int i=0; i<list.size(); i++){
			Menu menu = list.get(i);
	%>
 	document.write('<li><a href="#" onclick="openUrl(\'<%=menu.getUrl() %>\',9)" title="<%=menu.getAlt() %>"><%=menu.getTitle() %></a></li>');
 	<%} %>
 	document.write('<br class="clearfloat"/></ul></div></li></ul></div>');
}

/* 导航 js */
/* 导航滑动 */
function action1(num) {
	var i;
	for (i = 5; i <= 9; i++) {
		if(document.getElementById("li" + i).className=="nav_liSelect")
			continue;
		document.getElementById("ul_block" + i).style.display = "none";
		document.getElementById("li" + i).className = "";
	}
	document.getElementById("ul_tatle"+num).style.display = "block";
	document.getElementById("ul_block" + num).style.display = "block";
	document.getElementById("li" + num).className = "nav_liOn";

}

function disappear(num,menu) {
	document.getElementById("ul_tatle"+num).style.display = "none";
	document.getElementById("li" + num).className="";
	document.getElementById("li" + menu).className="nav_liSelect";
}
/* 导航滑动 end */
/* 导航 end */