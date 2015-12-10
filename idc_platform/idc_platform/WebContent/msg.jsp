<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
#msg_win {
    display: none;
    font-size: 12px;
    margin: 0;
    position: absolute;
    right: 0;
    z-index: 99;
}
.float_div {
    background: none repeat scroll 0 0 #FFFFFB;
    border: 2px solid #3988C3;
    width: 260px;
}
.float_title {
    background: url("bulletin/images/float_title.gif") no-repeat scroll 0 0 transparent;
    height: 26px;
    line-height: 26px;
}
.icos {
    position: absolute;
    right: 2px;
    top: 2px;
    z-index: 9;
}
.icos a {
    color: #FFFFFF;
    float: left;
    font-family: webdings;
    font-weight: bold;
    height: 22px;
    line-height: 22px;
    margin: 1px;
    padding: 1px;
    text-align: center;
    text-decoration: none;
    width: 14px;
}
.icos a:link, .icos a:visited {
    color: #1768BA;
    text-decoration: none;
}
.float_list {
    margin: 10px 10px;
    padding: 0;
}
.float_list li {
    line-height: 20px;
	list-style: none outside none;
}
</style>
</head>
<body>

<div id="msg_win" class="float_div" style="display: block; top: 584px; visibility: visible; opacity: 1; border-bottom-width: 0pt;">
	<div style="position:relative;" class="float_title">
    	<div class="icos"><a href="javascript:void 0" title="恢复" id="msg_min">::</a><a href="javascript:void 0" title="关闭" id="msg_close">×</a></div>
    </div>
   <ul id="msg_content" class="float_list" style="display:block;">
   <p style="margin: 0px auto">
   <style type="text/css">
		.float_list a:link,.float_list a:hover,.float_list a:visited{ color:#0000ff;}
	</style></p>
   <p style="margin:0px; padding:0px; background-color:#F00;"><a href="#" target="_blank"><img src="/images/pic_index_tcad.gif" alt="" style="border:0px;width:240px;"></a></p>
    <ul style="margin: 10px 5px" class="float_list">
    <li style="text-align: left"><span style="color: #ff0000">【活动】</span> <a href="http://www.gs.chinamobile.com/phoneCardSupermarket!queryPhoneCards.action">网上选号入网，号更靓	</a></li>

    <li style="text-align: left"><span style="color: #ff0000">【活动】</span> <a href="/whatsnew/event/201107/t20110704_6684.htm">e充值，更优惠，预存50送10</a></li>
    <li style="text-align: left"><span style="color: #ff0000">【活动】</span> <a href="/whatsnew/event/201107/t20110704_6682.htm">3G手机双重大礼智能终端合约计划</a></li>
	
	 <li style="text-align: left"><span style="color: #ff0000">【活动】</span> <a href="/whatsnew/event/201110/t20111010_6730.htm">手机支付缴话费百万大酬宾</a></li>
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
	Message.init();
	window.setTimeout("Message.set()",20000);//20秒后缩进最小化

</script>
</body>
</html>