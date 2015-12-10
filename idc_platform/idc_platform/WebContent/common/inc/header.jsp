<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    String ctx = request.getContextPath();
    
	String extPath="http://"+request.getHeader("Host")+"/extjs";
	//other path define here
%>
<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- import extjs scripts -->
<script type="text/javascript" src="<%=extPath%>/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=extPath%>/ext-all.js"></script>
<script type="text/javascript" src="<%=extPath%>/extends/gridextend.js"></script>
<script type="text/javascript" src="<%=extPath%>/extends/gridextendsforremotesort.js"></script>
<script type="text/javascript" src="<%=extPath%>/extends/datetimeextend.js"></script>
<script type="text/javascript" src="<%=extPath%>/source/locale/ext-lang-zh_CN.js"></script>
<!-- other scripts import here  -->

<!-- import extjs css -->
<link rel="stylesheet" type="text/css" href="<%=extPath%>/resources/css/ext-all.css" />

<!-- orther css import here -->
<link rel="stylesheet" type="text/css" href="<%=ctx%>/common/css/style.css" />
<style>
body{
	backgroud-color:#E3F3FA
}
</style>
<script type="text/javascript">
    Ext.BLANK_IMAGE_URL = '<%=extPath%>/resources/images/default/s.gif';
    Ext.QuickTips.init();
	var PWD_MINLENGTH = 6;
	document.onkeydown = function(e){
		var e1 = e||event;
		var keyCode=e1.keyCode||e1.which;
		//获取目标按下键以后的焦点对象
		var src = e1.target||e1.srcElement;
		if (keyCode==116 || (keyCode==8 && src.readOnly)){
			if(!e)e1.keyCode = 0;
			return false;
		}
	};
	document.oncontextmenu = function(){
		//return false;
	}
</script>