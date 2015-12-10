<%
	response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
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
<%@page import="com.hp.idc.bulletin.service.BulletingService"%>
<%@page import="com.hp.idc.bulletin.entity.BulletinInfo"%>
<%@page import="com.hp.idc.common.core.bo.Page"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%@include file="../getUser.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%

	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
	String mid = request.getParameter("mid");
	String url="";
	int num = 1;
	if(mid!=null){
		Menu m = Cache.MenuMap.get(mid);
		num = Integer.parseInt(m.getType());
		if(num>7){
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
	}else if(request.getParameter("url")!=null&&!"".equals((String)request.getParameter("url"))){
		url = request.getParameter("url");
	}else{
		//设置用户默认为第一个节点的url
		url= mgr.getFirstUrl(userId);
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>IDC业务运营管理平台</title>
<link href="style/css/nav.css" rel="stylesheet" type="text/css" charset="utf-8"/>
<link href="css/css.css" type="text/css" rel="stylesheet" />
<link href="css/float.css" type="text/css" rel="stylesheet" />
<script src="style/js/base.js" type="text/javascript"></script>
<script src="iepngfix_tilebg.js" type="text/javascript"></script>
<script src="../style/js/jquery-1.5.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../style/js/jquery.scrollTo-1.4.2.js"></script>
<script src="js/js.js" type="text/javascript"></script>

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
	//getMessage();
	//getMessageCount();
	//document.getElementById("icontent").style.height = (document.body.parentElement.clientHeight-document.getElementById("icontent").getBoundingClientRect().top)+'px';
});
function openUrl(mid,menu){
	location.href="index.jsp?mid="+mid;
}

function openView(mid,menu){
	document.getElementById("icontent").src="../manager/view/view.jsp?oid="+mid;
}
function changeHeight(){
	
	var visolizationHeight;
	if(window.navigator.userAgent.indexOf("MSIE")>=1){
	   visolizationHeight=document.body.parentElement.clientHeight;
	}
	else if(window.navigator.userAgent.indexOf("Firefox")>=1){
		visolizationHeight=window.innerHeight;
	}
	document.getElementById("icontent").style.height = (visolizationHeight-document.getElementById("icontent").getBoundingClientRect().top)+'px';

}

//添加事件响应函数的函数
function addEventSimple(obj,evt,fn){
	if(obj.addEventListener){
		obj.addEventListener(evt,fn,false);
	}else if(obj.attachEvent){
		obj.attachEvent('on'+evt,fn);
	}
}
//addEventSimple(window,'load',initScrolling);
var contentHeight;
var scrollingBox;
var scrollingInterval;
//1.初始化滚动新闻
function initScrolling(){
	scrollingBox = document.getElementById("scrollingBar");
	if(scrollingBox == null)
		return;
	scrollingBox.style.height = "20px";
	scrollingBox.style.overflow = "hidden";
	scrollingBox.style.display = "block";
	scrollingInterval = setInterval("scrolling()",50);
	scrollingBox.onmouseover = over;
	scrollingBox.onmouseout = out;	
}
//3.滚动效果
function scrolling(){
	//开始滚动,origin是原来scrollTop
	var origin = scrollingBox.scrollTop++;
	//如果到底了
	if(origin == scrollingBox.scrollTop){
		//延时固定时间后返回顶部
		setTimeout("scrollingBox.scrollTop=0",1000)
	}
}
function over(){
	clearInterval(scrollingInterval);
}
function out(){
	scrollingInterval = setInterval("scrolling()",50);
}

function openBulletin(id){
	document.getElementById("icontent").src='../../bulletin/getBulletinInfo.action?bulletinId='+id;
}

function modifyPassword(urls){
	//document.location.href=urls;
	var height=250,width=350;
	if(width == -1)
      width = document.body.offsetWidth;
  if(height == -1)
      height = document.body.offsetHeight;

  var xposition = (screen.width - width) / 2;
  var yposition = (screen.height - height) / 2;
	window.open(urls, '_blank','left='+xposition+',top='+yposition+',height='+height+',width='+width+',toolbar=no, menubar=no,resizable=yes, scrollbars=yes,location=no, status=no');
}

</script>
<style type="text/css"> 
img, div, a, input { behavior: url("iepngfix.htc") }
</style>
</head>

<body style="background-color:#E3F3FA">
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
<!---header--->
<div class="header">
	<div class="title">
		
		<div class="rigthTop">
			<span>欢迎您: <%=userName %>!</span>
			<span style="padding: 0 15px; background: none;">|</span>
			<a onclick="modifyPassword('<%=request.getContextPath() %>/cas/modifyPassword.jsp?id=<%=_userId%>')" style="cursor: pointer;">修改资料</a>
			<span style="padding: 0 15px; background: none;">|</span>
			<a href="<%=request.getContextPath() %>/logout.action">注销</a>
			<span style="padding: 0 15px; background: none;">|</span>
			<a href="<%=request.getContextPath() %>/user manual.docx">帮助</a>
		</div>
	</div>
</div>
<!---menu--->
<div class="menu"> 
<%
StringBuffer sb = new StringBuffer();
List<Menu> list = mgr.getBeanByFilter(userId,"-1");
for(int i=0; i<list.size(); i++){
		Menu menu = list.get(i);
		
	sb.append("<em id='hrefa"+i+"'><span class='icomenu0"+(i+1)+"'>"+menu.getTitle()+"</span>\n");
	sb.append("<div id='divMenu0"+(i+1)+"' class='display' style='display: none;'><div class='displaytop'></div>\n");
	sb.append("<div class='displaymid'>\n");
	List<Menu> list1 = mgr.getBeanByFilter(userId,menu.getOid());
	for(int j=0; j<list1.size(); j++){
			Menu menu1 = list1.get(j);
		sb.append("<a href='#' onclick='openUrl(\""+menu1.getOid()+"\",5)' title='"+menu1.getAlt()+"'>"+StringUtil.escapeHtml(menu1.getTitle())+"</a>\n"); 
	}		
	sb.append("</div><div class='displaybtm'></div></div></em>\n");
}
out.write(sb.toString());
%>
	</div>
	
	<iframe id="icontent" name="icontent" src="<%=url %>" width="100%" height="100%" onload="changeHeight();" allowtransparency="true"></iframe>
<!---body--->

<div id="msg_win" class="float_div" style="display: none; top: 584px; visibility: visible; opacity: 1; border-bottom-width: 0pt;">
	<div style="position:relative;" class="float_title">
    	<div class="icos"><a href="javascript:void 0" title="恢复" id="msg_min">::</a><a href="javascript:void 0" title="关闭" id="msg_close">×</a></div>
    </div>
   <ul id="msg_content" class="float_list" style="display:block;">
   <p style="margin: 0px auto">
   <style type="text/css">
		.float_list a:link,.float_list a:hover,.float_list a:visited{ color:#0000ff;}
	</style>
	</p>   
    <ul style="margin: 10px 5px" class="float_list">
    <%
			BulletingService bulletinService = (BulletingService)ContextUtil.getBean("bulletingService");
			Map<String,Object> paramMap=new HashMap<String,Object>();
			//paramMap.put("beginTimeBefore",new java.util.Date());
			paramMap.put("today",new java.util.Date());
			//paramMap.put("endTimeAfter",new java.util.Date());
			LinkedHashMap<String,String> sortMap=new LinkedHashMap<String,String>();
			//sortMap.put("beginTime","desc");
			Page<BulletinInfo> pageInfo =bulletinService.getBulletinList(paramMap,sortMap,0,8);
			if(pageInfo.getResult().size()>0){
				for(BulletinInfo bi:pageInfo.getResult()){
					%><li style="text-align: left"><span style="color: #ff0000"></span><a href='#' onclick='openBulletin(<%=bi.getId()%>)'><%=bi.getTitle()%></a></li><% 
				}
			}
			%>   
    </ul>
</ul>
</div>

<script type="text/javascript">

var Message={
		set: function() {//最小化与恢复状态切换
			var set=this.minbtn.status == 1?[0,1,'block',this.char[0],'最小化']:[1,0,'none',this.char[1],'恢复'];
			this.minbtn.status=set[0];
			this.win.style.borderBottomWidth=set[1];
			this.content.style.display =set[2];
			this.minbtn.innerHTML =set[3]
			this.minbtn.title = set[4];
			this.win.style.top = this.getY().top;
		},
		
		close: function() {//关闭
			this.win.style.display = 'none';
			window.onscroll = null;
		},
		
		setOpacity: function(x) {//设置透明度
			var v = x >= 100 ? '': 'Alpha(opacity=' + x + ')';
			this.win.style.visibility = x<=0?'hidden':'visible';//IE有绝对或相对定位内容不随父透明度变化的bug
			this.win.style.filter = v;
			this.win.style.opacity = x / 100;
		},
		
		show: function() {//渐显
			clearInterval(this.timer2);
			var me = this,fx = this.fx(0, 100, 0.1),t = 0;
			this.timer2 = setInterval(function() {
				t = fx();
				me.setOpacity(t[0]);
				if (t[1] == 0) {clearInterval(me.timer2)}
			},6);//10 to 6
		},
		
		fx: function(a, b, c) {//缓冲计算
			var cMath = Math[(a - b) > 0 ? "floor": "ceil"],c = c || 0.1;
			return function() {return [a += cMath((b - a) * c), a - b]}
		},
		
		getY: function() {//计算移动坐标
			var d = document,b = document.body,	e = document.documentElement;
			var s = Math.max(b.scrollTop, e.scrollTop);
			var h = /BackCompat/i.test(document.compatMode)?b.clientHeight:e.clientHeight;
			var h2 = this.win.offsetHeight;
			return {foot: s + h + h2 + 2+'px',top: s + h - h2 - 2+'px'}
		},
		
		moveTo: function(y) {//移动动画
			clearInterval(this.timer);
			var me = this,a = parseInt(this.win.style.top)||0;
			var fx = this.fx(a, parseInt(y));
			var t = 0 ;
			this.timer = setInterval(function() {
				t = fx();
				me.win.style.top = t[0]+'px';
				if (t[1] == 0) {
					clearInterval(me.timer);
					me.bind();
				}
			},10);//10 to 6
		},
		
		bind:function (){//绑定窗口滚动条与大小变化事件
			var me=this,st,rt;
			window.onscroll = function() {
				clearTimeout(st);
				clearTimeout(me.timer2);
				me.setOpacity(0);
				st = setTimeout(function() {
					me.win.style.top = me.getY().top;
					me.show();
				},100);//600 mod 100
			};
			window.onresize = function (){
				clearTimeout(rt);
				rt = setTimeout(function() {me.win.style.top = me.getY().top},100);
			}
		},
		
		init: function() {//创建HTML
			function $(id) {return document.getElementById(id)};
			this.win=$('msg_win');
			var set={minbtn: 'msg_min',closebtn: 'msg_close',title: 'msg_title',content: 'msg_content'};
			for (var Id in set) {this[Id] = $(set[Id])};
			var me = this;
			this.minbtn.onclick = function() {me.set();this.blur()};
			this.closebtn.onclick = function() {me.close()};
			this.char=navigator.userAgent.toLowerCase().indexOf('firefox')+1?['_','::','×']:['0','2','r'];//FF不支持webdings字体
			this.minbtn.innerHTML=this.char[0];
			this.closebtn.innerHTML=this.char[2];
			setTimeout(function() {//初始化最先位置
				me.win.style.display = 'block';
				me.win.style.top = me.getY().foot;
				me.moveTo(me.getY().top);
			},0);
			return this;
		}
	};

	<%
		boolean msgShow = true;						
		if(msgShow && mid == null){
			if(pageInfo.getResult().size() > 0)
				out.write("Message.init();\nwindow.setTimeout('Message.set()',10000);//20秒后缩进最小化\n");
		}
	%>

</script>

</body>
</html>

