<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>中国移动通信 - 深圳云计算IAAS管理平台</title>
<link href="${ctx}/iaaspool/visuals/jqui/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/iaaspool/visuals/ui.jqgrid.css" rel="stylesheet" type="text/css">
<link href="${ctx}/iaaspool/visuals/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/iaaspool/visuals/components.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/iaaspool/libs/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/jquery-ui-1.8.23.custom.min.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/jqgrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/jqgrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/tween/TweenLite.min.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/tween/easing/EasePack.min.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/tween/plugins/CSSPlugin.min.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/amcharts.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/chartsFunction.js"></script>
<script type="text/javascript" src="${ctx}/iaaspool/libs/NodesViews.js"></script>
<script language="JavaScript" type="text/javascript">
Date.prototype.format = function(format)   {   
	var o = {   
		"M+" : this.getMonth()+1,  
		"d+" : this.getDate(),    
		"h+" : this.getHours(),    
		"m+" : this.getMinutes(), 
		"s+" : this.getSeconds(),  
		"q+" : Math.floor((this.getMonth()+3)/3),   
		"S" : this.getMilliseconds() 
	};
	if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	for(var k in o)if(new RegExp("("+ k +")").test(format))   
		format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));   
	return format;   
}
$(function() {
	//
	nodesViews.init('#nodeViewContainer').setupReturn('#pathBar .returnBtn').setupPath('#pathBar .pathText').setupPage('#nodePageContainer');
	nodesViews.loadData('${ctx}/respool/listPoolNodes');
	//nodesViews.loadData('${ctx}/iaaspool/loads/_tempNodes.json');
	$.jgrid.ajaxOptions.type='post';
	//
});
</script>
</head>
<body>
<div id="page-body">

<div id="header">
	<div class="wrapper relaContainer">
    	<div id="logoBar"><img src="${ctx}/iaaspool/visuals/logoIAAS.gif" width="360" height="60"></div>
        <div id="topBar">
        	<div><script>document.write(new Date().format('yyyy年MM月dd日hh时:mm分'));</script></div>
        	<div> <label>用户:${sessionScope.user_name},<a href="${ctx}/userLogout.action" style="color:#FFF">退出</a> </label></div>
        </div>
    </div>
</div>

<div id="bodyer" class="wrapper">
	<div id="pathBar" class="relaContainer">
    	<a href="####" class="returnBtn"><span class="icon floatl"></span>返回</a>
    	<a href="${ctx}/mainpage" style="font-size:1.25em; text-decoration:none;float:right;padding:0 10px;">管理平台</a>
    	<h1 class="pathText"></h1>
    </div>
	<div id="pageContents">
    	<div id="nodeViewContainer" class="relaContainer"></div>
        <div id="nodePageContainer">Loading...</div>
    </div>
</div>

<div id="footer" class="wrapper">
	<p>中国移动通信 - 深圳云计算IAAS管理平台</p>
    <p>- 版权信息 -</p>
</div>

</div>
</body>
</html>
