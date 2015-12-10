<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.mgr.ViewMgr"%>
<%@page import="com.hp.idc.portal.bean.View"%>
<%@page import="com.hp.idc.portal.security.Cache"%>
<%@page import="java.util.regex.*"%>
<%@page import="com.hp.idc.portal.bean.MenuParams"%>
<%@page import="com.hp.idc.portal.mgr.MenuParamsMgr"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%@include file="../getUser.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String mid = request.getParameter("mid");
	String url="";
	int num = 1;
	if(mid!=null){
		Menu m = Cache.MenuMap.get(mid);
		num = Integer.parseInt(m.getType());
		if(num>5){
			num = Integer.parseInt(Cache.MenuMap.get(m.getType()).getType());
		}
		num +=4;
		Pattern p = Pattern.compile("#\\d+");
		Matcher matcher = p.matcher(m.getUrl());
		StringBuffer dsData = new StringBuffer();
		
		MenuParams mp = MenuParamsMgr.getBeanById(userId,Integer.parseInt(m.getOid()));
		Map<String,String> params = new HashMap<String,String>();
		url = m.getUrl();
		if(mp!=null){
			//处理参数记录，放入map中
			String[] paramArr = mp.getParams().split(",");
			for(String param:paramArr){
				String[] temp=param.split("=");
				url = url.replaceFirst(temp[0],temp[1]);
			}
		}
	}else{
		url = request.getParameter("url");
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>IDC业务运营管理平台</title>
<link href="style/css/nav.css" rel="stylesheet" type="text/css" charset="utf-8"/>
<script src="style/js/base.js" type="text/javascript"></script>
<script src="iepngfix_tilebg.js" type="text/javascript"></script>
<script src="../style/js/jquery-1.5.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../style/js/jquery.scrollTo-1.4.2.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$(".index_line1Span6").click(function(){
		$(".cover_divBig").show("slow");
		$(".cover_divBig").css("background-color","transparent");
		$(".btn1").fadeIn();
		$(".cover_divSmall").fadeIn();
	});			   
	$("#index_line1Span7").click(function(){
		$(".cover_divBig").show("slow");
		$(".cover_divBig").css("background-color","transparent");
		$(".btn1").fadeIn();
		$(".cover_divSmall").fadeIn();
	});			   
	
	$("#close_message_win").click(function(){
		$(".cover_divBig").hide("slow");
		$(this).fadeOut();
		$(".cover_divSmall").fadeOut();
	});
	getMessage();
	getMessageCount();
	document.getElementById("icontent").style.height = (document.body.parentElement.clientHeight-document.getElementById("icontent").getBoundingClientRect().top)+'px';
});

function openUrl(mid,menu){
	location.href="index.jsp?mid="+mid;
}

function openView(mid,menu){
	document.getElementById("icontent").src="../manager/view/view.jsp?oid="+mid;
}

function changeHeight(){
	document.getElementById("li<%=num%>").className = "nav_liSelect";
	var visolizationHeight;
	if(window.navigator.userAgent.indexOf("MSIE")>=1){
	   visolizationHeight=document.body.parentElement.clientHeight;
	}
	else if(window.navigator.userAgent.indexOf("Firefox")>=1){
		visolizationHeight=window.innerHeight;
	}
	document.getElementById("icontent").style.height = (visolizationHeight-document.getElementById("icontent").getBoundingClientRect().top)+'px';
	document.getElementById("cover_divBig").style.height = visolizationHeight;
	document.getElementById("cover_divSmall").style.height = visolizationHeight;
}
</script>
<style type="text/css"> 
img, div, a, input { behavior: url("iepngfix.htc") }
</style>
</head>
	
<body>
	<!--出发弹出层  layout-->
    <div class="cover_divBig" style="display:none;" id="cover_divBig"></div>
    <div class="cover_divSmall"  style="display:none;" id="cover_divSmall">
    	<!-- Widget XHTML Starts Here -->
		<div id="posts-container">
			<!-- Posts go inside this DIV -->
			<div id="posts"></div>
			<!-- Load More "Link" -->
			<div id="load-more" class="btn1">加载更多未读消息</div>
			<div class="btn1" id="close_message_win">关闭</div>
		</div>
		<!-- Widget XHTML Ends Here -->
   	</div>
    <!--出发弹出层  end-->
	<div class="nav_title">
    	<!--head  layout-->
            <!--line1  layout-->
            <div class="index_line1">
                <div class="index_line1Con">
                    <div class="index_line1Rit">
                        <div class="index_line1Rit1" >
                        	<span class="index_line1Span6" title="系统消息"></span>
                        	<span class="index_line1Span7" id="index_line1Span7">您有未读消息<span style="color: red;" id="messageCount"></span>条</span>
                            <span class="index_line1Span1">欢迎您：<%=userName %></span>
                            <span class="index_line1Span2"><a href="<%=request.getContextPath() %>/logout.action">注销</a></span>
                            <span class="index_line1Span3"></span>
                        </div>
                        <br class="clearfloat"/>
                    </div>
                    <br class="clearfloat"/>
                </div>
            </div>
            <!--line1  end-->
            <!--index_nav  layout-->
            <div class="ul_nav">
                <ul class="index_nav">
                	 
                    <li id="li5" onmouseover="action1(5)" onmouseout="disappear(5,<%=num %>)">
                    	<a href="../" class="index_nav1">客户管理</a>
                    	<div class="index_line2" id="ul_tatle5" style="display:none;" >
			                 <ul class="index_SubNav Sub1" id="ul_block5" style="display:none;">
			                    <%
			                    	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
			                 		List<Menu> list = mgr.getBeanByFilter(userId,"1");
			                 		for(int i=0; i<list.size(); i++){
			                 			Menu menu = list.get(i);
			                 	%>
			                    <li><a href="#" onclick="openUrl('<%=menu.getOid() %>',5)" title="<%=menu.getAlt() %>"><%=StringUtil.escapeHtml(menu.getTitle()) %></a></li>
			                    <%
			                 		}
			                    %>
			                    <br class="clearfloat"/>
			                </ul>
			            </div>
                    </li>
                    <li id="li6" onmouseover="action1(6)" onmouseout="disappear(6,<%=num %>)">
                    	<a href="#" class="index_nav2">业务运营</a>
                    	<div class="index_line2" id="ul_tatle6" style="display:none;" >
			                 <ul class="index_SubNav Sub2" id="ul_block6" style="display:none;">
			                    <%
			                 		list = mgr.getBeanByFilter(userId,"2");
			                 		for(int i=0; i<list.size(); i++){
			                 			Menu menu = list.get(i);
			                 	%>
			                    <li><a href="#" onclick="openUrl('<%=menu.getOid() %>',6)" title="<%=menu.getAlt() %>"><%=StringUtil.escapeHtml(menu.getTitle()) %></a></li>
			                    <%} %>
			                    <br class="clearfloat"/>
			                </ul>
			            </div>
                    </li>
                    <li id="li7" onmouseover="action1(7)" onmouseout="disappear(7,<%=num %>)"><a href="#" class="index_nav3">资源与监控</a>
                    	<div class="index_line2" id="ul_tatle7" style="display:none;" >
			                <ul class="index_SubNav Sub3" id="ul_block7" style="display:none;">
			                    <%
			                 		list = mgr.getBeanByFilter(userId,"3");
			                 		for(int i=0; i<list.size(); i++){
			                 			Menu menu = list.get(i);
			                 	%>
			                    <li><a href="#" onclick="openUrl('<%=menu.getOid() %>',7)" title="<%=menu.getAlt() %>"><%=StringUtil.escapeHtml(menu.getTitle()) %></a></li>
			                    <%} %>
			                    <br class="clearfloat"/>
			                </ul>
		                </div>
                    </li>
                    <li id="li8" onmouseover="action1(8)" onmouseout="disappear(8,<%=num %>)"><a href="#" class="index_nav4">统计报表</a>
	                    <div class="index_line2" id="ul_tatle8" style="display:none;" >
			                 <ul class="index_SubNav Sub4" id="ul_block8" style="display:none;">
			                    <%
			                 		list = mgr.getBeanByFilter(userId,"4");
			                 		for(int i=0; i<list.size(); i++){
			                 			Menu menu = list.get(i);
			                 	%>
			                    <li><a href="#" onclick="openUrl('<%=menu.getOid() %>',8)" title="<%=menu.getAlt() %>"><%=StringUtil.escapeHtml(menu.getTitle()) %></a></li>
			                    <%} %>
			                    <br class="clearfloat"/>
			                </ul>
			            </div>	
                    </li>
                    <li id="li9" onmouseover="action1(9)" onmouseout="disappear(9,<%=num %>)"><a href="#" class="index_nav5">系统维护</a>
                    	 <div class="index_line2" id="ul_tatle9" style="display:none;" >
			                 <ul class="index_SubNav Sub5" id="ul_block9" style="display:none;">
			                    <%
			                 		list = mgr.getBeanByFilter(userId,"5");
			                 		for(int i=0; i<list.size(); i++){
			                 			Menu menu = list.get(i);
			                 	%>
			                    <li><a href="#" onclick="openUrl('<%=menu.getOid() %>',9)" title="<%=menu.getAlt() %>"><%=StringUtil.escapeHtml(menu.getTitle()) %></a></li>
			                    <%} %>
			                    <br class="clearfloat"/>
			                </ul>
			            </div>
                    </li>
                    <br class="clearfloat"/>
                </ul>
            </div>
    <!--index_nav  end-->
    	<!--head  end-->
    </div>
    <iframe id="icontent" name="icontent" src="<%=url %>" width="100%" height="100%" onload="changeHeight();"></iframe>
</body>
</html>
