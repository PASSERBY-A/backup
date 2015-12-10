<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>中国移动通信 - 深圳云计算IAAS管理平台 - 顶级视图</title>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/jsp/iaaspool/visuals/style.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/jsp/iaaspool/visuals/components.css" rel="stylesheet" type="text/css"/>
<style>
	.hidearea{
		width: 650px;
		height: 290px;
		position: absolute;
		top: 100px;
		left: 35px;
		background: url("${ctx}/jsp/iaaspool/wt_loading.gif") no-repeat;
		background-color:rgba(0,0,0,0.2);
		overflow: hidden;
		background-position: 290px 140px;
	}
	.resource_status{
		height: 200px;
		top: 400px;
		background-position: 290px 90px;
	}
	.pool_name{
		word-break: break-all;
		display: inline-block;
	}
	.selectCluster{
		margin: 10px 25px;
	}
</style>
<script type="text/javascript" src="${ctx}/jsp/iaaspool/libs/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/jsp/iaaspool/libs/NodesViews.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ui/core.js"></script>
<script type="text/javascript" src="${ctx}/scripts/sharedFunctions.js"></script>
<script language="JavaScript" type="text/javascript">
var contextPath = '${pageContext.request.contextPath}';
var clusterId = "";
Date.prototype.format = function(format)   {  
	var date=new Date();
	var year=date.getFullYear();
	var month=date.getMonth()+1<10?"0"+(date.getMonth()+1):date.getMonth()+1;
	var day=date.getDate();
	var hours=date.getHours();
	var minutes=date.getMinutes();
	var seconds=date.getSeconds();
	if(parseInt(day)<10){
		day="0"+day;
	}
	if(parseInt(hours)<10){
		hours="0"+hours;
	}
	if(parseInt(minutes)<10){
		minutes="0"+minutes;
	}
	if(parseInt(seconds)<10){
		seconds="0"+seconds;
	}
	return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
}
$(function() {
	$("#cluster").change(function(){
		clusterId = $("#cluster").val();
		$.ajax({
			type : "POST",
			//url: "${ctx}/respool/listPoolNodes.action",
			//url: "${ctx}/?command=listPoolNodes&cmsz=yes",
			url: clientApiUrl+"?command=listsubresource&cmsz=yes&response=json&resourcePoolId="+clusterId,
			dataType:'json',
			async: false,
			timeout:20000,
			success : function(data) {
				$(".firstResource").empty();
				if(data&&data.listzonesresponse&&data.listzonesresponse.count>0){
					var data=data.listzonesresponse.zone;
					for(var i=0;i<data.length;i++){
						var li="<li id="+data[i].id+"><table class='nodeCell'><tr><td><a href='javascript:void(0)' class='pool_name'>"+data[i].name+"</a></td></tr></table></li>";
						var alert_li="<li id="+data[i].id+" class='alert'><table class='nodeCell'><tr><td><a href='javascript:void(0)' class='pool_name'>"+data[i].name+"</a></td></tr></table></li>";
//	 					if((100*(data[i].hostResource.usedCpu)/data[i].hostResource.totalCpu).toFixed(0)>=90){
//	 						li=alert_li;
//	 					}else if((100*(data[i].hostResource.usedMemory)/data[i].hostResource.totalMemory).toFixed(0)>=90){
//	 						li=alert_li;
//	 					}else if((100*(data[i].hostResource.usedStorage)/data[i].hostResource.totalStorage).toFixed(0)>=90){
//	 						li=alert_li;
//	 					}
						$(li).appendTo("#vmwareBoard .nodeViewContainer.firstResource");
					}
					 /* for(var i=0;i<data.items.length;i++){
						var li="<li><table class='nodeCell'><tr><td><a href='####'>"+data.items[i].title+"</a></td></tr></table></li>";
						$(li).appendTo("#vmwareBoard .nodeViewContainer.firstResource");
					}
					for(var i=0;i<data.items.length;i++){
						var li="<li><table class='nodeCell'><tr><td><a href='####'>"+data.items[i].title+"</a></td></tr></table></li>";
						$(li).appendTo("#vmwareBoard .nodeViewContainer.firstResource");
					} */
					$(".resource").hide();
					$(".zoneCount").text(data.length);
					nodesViews.init('.nodeViewContainer',0);
				}else{
					alert('暂时没有任何数据');
					$(".resource").hide();
					$(".zoneCount").text(0);
					return;
				}
			},
			error : function(){
				alert('系统错误，请稍后重试');
				$(".resource").hide();
				return;
			}
		});

		$.ajax({
			type : "POST",
			//url: "${ctx}/?command=rootClusterResource&cmsz=yes",
			url: clientApiUrl+"?command=computeresource&cmsz=yes&response=json&resourcePoolId="+clusterId,
			//url: "${ctx}/respool/rootClusterResource.action?oid=0",
			dataType:'json',
			timeout:20000,
			success : function(data) {

				if(data&&data.listcapacityresponse&&data.listcapacityresponse.capacity&&data.listcapacityresponse.capacity){
					var data = data.listcapacityresponse.capacity;
					for(var i=0;i<data.length;i++){
						var type = data[i].type;
						var capacity = data[i];
						if(type == "1"){//cpu
							$("#total_status .total_cpu").text(cloudStack.converters.convertByType(capacity.type, capacity.capacitytotal));
							$('#total_status .cpu_use').text(cloudStack.converters.convertByType(capacity.type, capacity.capacityused));
							$('#total_status .cpu_unuse').text(cloudStack.converters.convertByType2(capacity.type, capacity.capacitytotal, capacity.capacityused));
							$('#total_status .cpu_per').css("width",parseInt(capacity.percentused)+"%");
							
// 							$("#total_status .total_cpu").text(data[i].capacitytotal);
// 							$('#total_status .cpu_use').text(data[i].capacityused);
// 							$('#total_status .cpu_unuse').text((data[i].capacitytotal-data[i].capacityused));
// 							$('#total_status .cpu_per').css("width",data[i].percentused+"%");
						}else if(type == "0"){//storage
							$("#total_status .total_memo").text(cloudStack.converters.convertByType(capacity.type, capacity.capacitytotal));
							$('#total_status .memo_use').text(cloudStack.converters.convertByType(capacity.type, capacity.capacityused));
							$('#total_status .memo_unuse').text(cloudStack.converters.convertByType2(capacity.type, capacity.capacitytotal, capacity.capacityused));
							$('#total_status .mem_per').css("width",parseInt(capacity.percentused)+"%");
							
// 							$("#total_status .total_memo").text((data[i].capacitytotal/1024/1024).toFixed(0));
// 							$('#total_status .memo_use').text((data[i].capacityused/1024/1024).toFixed(0));
// 							$('#total_status .memo_unuse').text(((data[i].capacitytotal-data[i].capacityused)/1024/1024).toFixed(0));
// 							$('#total_status .mem_per').css("width",data[i].percentused+"%");
						}else if(type == "2"){//memory
							$("#total_status .total_storage").text(cloudStack.converters.convertByType(capacity.type, capacity.capacitytotal));
							$('#total_status .storage_use').text(cloudStack.converters.convertByType(capacity.type, capacity.capacityused));
							$('#total_status .storage_unuse').text(cloudStack.converters.convertByType2(capacity.type, capacity.capacitytotal, capacity.capacityused));
							$('#total_status .sto_per').css("width",parseInt(capacity.percentused)+"%");
							
// 							$("#total_status .total_storage").text((data[i].capacitytotal/1024/1024).toFixed(0));
// 							$('#total_status .storage_use').text((data[i].capacityused/1024/1024).toFixed(0));
// 							$('#total_status .storage_unuse').text(((data[i].capacitytotal-data[i].capacityused)/1024/1024).toFixed(0));
// 							$('#total_status .sto_per').css("width",data[i].percentused+"%");
						}
					}
					
					/* if((100*(data[0].totalStorage-data[0].usedStorage)/data[0].totalStorage).toFixed(0)>=90){
						$('#total_status table tr:eq(2) td:eq(2)').find(".process div").addClass("bad");
					}
					$('#total_status table tr:eq(2) td:eq(2)').find(".process div").css("width",(100*(data[0].totalStorage-data[0].usedStorage)/data[0].totalStorage).toFixed(0)+'%'); */
					
					
//	 				  $('#total_status .counts').each(function(){
//	 					  var label=$(this).find(".textField:eq(0) label");
//	 					  var label_text=0;
//	 					  if($(label).text().lastIndexOf(')')!=-1){
//	 					  		label_text=$(label).text().substring($(label).text().lastIndexOf(')')+1,$(label).text().length);
//	 				  		}else{
//	 				  			label_text=$(label).text();
//	 				  		}
//	 					  if(parseFloat(label_text)>=parseFloat('90%')){
//	 						  $(this).parent().find(".process div:eq(0)").addClass("bad");
//	 					  }
//	 					  $(this).parent().find(".process div:eq(0)").css("width",label_text);
//	 				  })
					$(".resource_status").hide();
				}else{
					alert('暂时没有任何数据');
					$(".resource_status").hide();
					return;
				}
			},
			error : function(){
				alert('系统错误，请稍后重试');
				$(".resource_status").hide();
				return;
			}
		});
		
		loadX86AndHPVM();
	});
	
	$.ajax({
		type : "POST",
		//url: "${ctx}/respool/listPoolNodes.action",
		//url: "${ctx}/?command=listPoolNodes&cmsz=yes",
		url: clientApiUrl+"?command=listPoolNodes&cmsz=yes&response=json",
		dataType:'json',
		timeout:20000,
		async: false,
		success : function(data) {
			if(data&&data.poolNodes&&data.poolNodes.length>0){
				var data=data.poolNodes;
				for(var i=0;i<data.length;i++){
					if(i==0){
						clusterId= data[i].resourceId;
					}
					var option="<option value="+data[i].resourceId+">"+data[i].name+"</option>";
					$("#cluster").append(option);
				}
			}
		}
	});
	$.ajax({
		type : "POST",
		//url: "${ctx}/respool/listPoolNodes.action",
		//url: "${ctx}/?command=listPoolNodes&cmsz=yes",
		url: clientApiUrl+"?command=listsubresource&cmsz=yes&response=json&resourcePoolId="+clusterId,
		dataType:'json',
		async: false,
		timeout:20000,
		success : function(data) {
			$(".firstResource").empty();
			if(data&&data.listzonesresponse&&data.listzonesresponse.count>0){
				var data=data.listzonesresponse.zone;
				for(var i=0;i<data.length;i++){
					var li="<li id="+data[i].id+"><table class='nodeCell'><tr><td><a href='javascript:void(0)' class='pool_name'>"+data[i].name+"</a></td></tr></table></li>";
					var alert_li="<li id="+data[i].id+" class='alert'><table class='nodeCell'><tr><td><a href='javascript:void(0)' class='pool_name'>"+data[i].name+"</a></td></tr></table></li>";
// 					if((100*(data[i].hostResource.usedCpu)/data[i].hostResource.totalCpu).toFixed(0)>=90){
// 						li=alert_li;
// 					}else if((100*(data[i].hostResource.usedMemory)/data[i].hostResource.totalMemory).toFixed(0)>=90){
// 						li=alert_li;
// 					}else if((100*(data[i].hostResource.usedStorage)/data[i].hostResource.totalStorage).toFixed(0)>=90){
// 						li=alert_li;
// 					}
					$(li).appendTo("#vmwareBoard .nodeViewContainer.firstResource");
				}
				 /* for(var i=0;i<data.items.length;i++){
					var li="<li><table class='nodeCell'><tr><td><a href='####'>"+data.items[i].title+"</a></td></tr></table></li>";
					$(li).appendTo("#vmwareBoard .nodeViewContainer.firstResource");
				}
				for(var i=0;i<data.items.length;i++){
					var li="<li><table class='nodeCell'><tr><td><a href='####'>"+data.items[i].title+"</a></td></tr></table></li>";
					$(li).appendTo("#vmwareBoard .nodeViewContainer.firstResource");
				} */
				$(".zoneCount").text(data.length);
				$(".resource").hide();
				nodesViews.init('.nodeViewContainer',0);
			}else{
				alert('暂时没有任何数据');
				$(".resource").hide();
				return;
			}
		},
		error : function(){
			alert('系统错误，请稍后重试');
			$(".resource").hide();
			return;
		}
	});
	
	$.ajax({
		type : "POST",
		//url: "${ctx}/?command=rootClusterResource&cmsz=yes",
		url: clientApiUrl+"?command=computeresource&cmsz=yes&response=json&resourcePoolId="+clusterId,
		//url: "${ctx}/respool/rootClusterResource.action?oid=0",
		dataType:'json',
		timeout:20000,
		success : function(data) {

			if(data&&data.listcapacityresponse&&data.listcapacityresponse.capacity&&data.listcapacityresponse.capacity){
				var data = data.listcapacityresponse.capacity;
				for(var i=0;i<data.length;i++){
					var type = data[i].type;
					var capacity = data[i];
					if(type == "1"){//cpu
						$("#total_status .total_cpu").text(cloudStack.converters.convertByType(capacity.type, capacity.capacitytotal));
						$('#total_status .cpu_use').text(cloudStack.converters.convertByType(capacity.type, capacity.capacityused));
						$('#total_status .cpu_unuse').text(cloudStack.converters.convertByType2(capacity.type, capacity.capacitytotal, capacity.capacityused));
						$('#total_status .cpu_per').css("width",parseInt(capacity.percentused)+"%");
						
// 						$("#total_status .total_cpu").text(data[i].capacitytotal);
// 						$('#total_status .cpu_use').text(data[i].capacityused);
// 						$('#total_status .cpu_unuse').text((data[i].capacitytotal-data[i].capacityused));
// 						$('#total_status .cpu_per').css("width",data[i].percentused+"%");
					}else if(type == "0"){//storage
						$("#total_status .total_memo").text(cloudStack.converters.convertByType(capacity.type, capacity.capacitytotal));
						$('#total_status .memo_use').text(cloudStack.converters.convertByType(capacity.type, capacity.capacityused));
						$('#total_status .memo_unuse').text(cloudStack.converters.convertByType2(capacity.type, capacity.capacitytotal, capacity.capacityused));
						$('#total_status .mem_per').css("width",parseInt(capacity.percentused)+"%");
						
// 						$("#total_status .total_memo").text((data[i].capacitytotal/1024/1024/1024).toFixed(0));
// 						$('#total_status .memo_use').text((data[i].capacityused/1024/1024/1024).toFixed(0));
// 						$('#total_status .memo_unuse').text(((data[i].capacitytotal-data[i].capacityused)/1024/1024/1024).toFixed(0));
// 						$('#total_status .mem_per').css("width",data[i].percentused+"%");
					}else if(type == "2"){//memory
						$("#total_status .total_storage").text(cloudStack.converters.convertByType(capacity.type, capacity.capacitytotal));
						$('#total_status .storage_use').text(cloudStack.converters.convertByType(capacity.type, capacity.capacityused));
						$('#total_status .storage_unuse').text(cloudStack.converters.convertByType2(capacity.type, capacity.capacitytotal, capacity.capacityused));
						$('#total_status .sto_per').css("width",parseInt(capacity.percentused)+"%");
						
// 						$("#total_status .total_storage").text((data[i].capacitytotal/1024/1024/1024).toFixed(0));
// 						$('#total_status .storage_use').text((data[i].capacityused/1024/1024/1024).toFixed(0));
// 						$('#total_status .storage_unuse').text(((data[i].capacitytotal-data[i].capacityused)/1024/1024/1024).toFixed(0));
// 						$('#total_status .sto_per').css("width",data[i].percentused+"%");
					}
				}
				
				/* if((100*(data[0].totalStorage-data[0].usedStorage)/data[0].totalStorage).toFixed(0)>=90){
					$('#total_status table tr:eq(2) td:eq(2)').find(".process div").addClass("bad");
				}
				$('#total_status table tr:eq(2) td:eq(2)').find(".process div").css("width",(100*(data[0].totalStorage-data[0].usedStorage)/data[0].totalStorage).toFixed(0)+'%'); */
				
				
// 				  $('#total_status .counts').each(function(){
// 					  var label=$(this).find(".textField:eq(0) label");
// 					  var label_text=0;
// 					  if($(label).text().lastIndexOf(')')!=-1){
// 					  		label_text=$(label).text().substring($(label).text().lastIndexOf(')')+1,$(label).text().length);
// 				  		}else{
// 				  			label_text=$(label).text();
// 				  		}
// 					  if(parseFloat(label_text)>=parseFloat('90%')){
// 						  $(this).parent().find(".process div:eq(0)").addClass("bad");
// 					  }
// 					  $(this).parent().find(".process div:eq(0)").css("width",label_text);
// 				  })
				$(".resource_status").hide();
			}else{
				alert('暂时没有任何数据');
				$(".resource_status").hide();
				return;
			}
		},
		error : function(){
			alert('系统错误，请稍后重试');
			$(".resource_status").hide();
			return;
		}
	});
	
	loadX86AndHPVM();
	
	 $('.status .view_resource').live('click',function(){
		$('#oid').val($(this).attr("id"));
		$('#title').val($(this).attr("title"));
		$('#queryForm').attr('action','${ctx}/respool/firstResource.action');
		$('#queryForm').submit();
		//window.location.href="${ctx}/respool/firstResource.action?oid="+$(this).attr("id")+"&title="+$(this).find("a").text();
	}); 
});

function loadX86AndHPVM() {
	$.ajax({
		type : "POST",
		url: clientApiUrl+"?command=computeresource&type=workorder_type.X86PhysicalResourcesApplication&cmsz=yes&response=json&resourcePoolId="+clusterId,
		dataType:'json',
		timeout:20000,
		success : function(data) {
			if(data) {
				for(var i=0; i<data.length; i++) {
					if (data[i].resourcePool == 'unit') {
						$(".total_x86_unit").text(data[i].total);
						$(".x86_unit_used").text(data[i].used);
						$(".x86_unit_unused").text(data[i].unused);
					} else if (data[i].resourcePool == 'cpu') {
						$(".total_x86_cpu").text(data[i].total);
						$(".x86_cpu_used").text(data[i].used);
						$(".x86_cpu_unused").text(data[i].unused);
					} else if (data[i].resourcePool == 'memory') {
						$(".total_x86_memory").text(data[i].total);
						$(".x86_memory_used").text(data[i].used);
						$(".x86_memory_unused").text(data[i].unused);
					}
				}
			}else{
				alert('暂时没有任何数据');
				return;
			}
		},
		error : function(){
			alert('系统错误，请稍后重试');
			return;
		}
	});
	
	$.ajax({
		type : "POST",
		url: clientApiUrl+"?command=computeresource&type=workorder_type.HPMinicomputerResourcesApplication&cmsz=yes&response=json&resourcePoolId="+clusterId,
		dataType:'json',
		timeout:20000,
		success : function(data) {
			if(data) {
				for(var i=0; i<data.length; i++) {
					if (data[i].resourcePool == 'unit') {
						$(".total_hpvm_host").text(data[i].totalHost);
						$(".total_hpvm_unit").text(data[i].total);
						$(".hpvm_unit_used").text(data[i].used);
						$(".hpvm_unit_unused").text(data[i].unused);
					} else if (data[i].resourcePool == 'cpu') {
						$(".total_hpvm_cpu").text(data[i].total);
						$(".hpvm_cpu_used").text(data[i].used);
						$(".hpvm_cpu_unused").text(data[i].unused);
					} else if (data[i].resourcePool == 'memory') {
						$(".total_hpvm_memory").text(data[i].total);
						$(".hpvm_memory_used").text(data[i].used);
						$(".hpvm_memory_unused").text(data[i].unused);
					}
				}
			}else{
				alert('暂时没有任何数据');
				return;
			}
		},
		error : function(){
			alert('系统错误，请稍后重试');
			return;
		}
	});
}
</script>
<script type="text/javascript" src="${ctx}/scripts/cloud.core.callbacks.js"></script>
</head>
<body>
<div id="page-body">

<div id="header">
	<div class="wrapper relaContainer">
    	<div id="logoBar"><img src="${ctx}/jsp/iaaspool/visuals/logoIAAS.png" width="360" height="60"></div>
        <div id="topBar">
        	<div><script>document.write(new Date().format('yyyy年MM月dd日hh时:mm分'));</script></div>
        	<div> <label>用户:${sessionScope.activeUser.userName} </label></div>
        </div>
    </div>
</div>

<div id="bodyer" class="wrapper">
	<div id="pathBar" class="relaContainer">
    	
    	<a href="${ctx}" style="font-size:1.25em; text-decoration:none;float:right;padding:0 10px;">管理平台</a>
    	<h1 class="pathText">IAAS资源池</h1>
    </div>
	<div id="pageContents">
		<form id="queryForm" method="post">
				<input type="hidden" name="oid" id="oid">
				<input type="hidden" name="title" id="title">
			</form>
		<div id="vmwareBoard" class="blockBoard relaContainer floatl radiusAll">
			<h2 class="radiusTopL radiusTopR">资源池信息动态展示</h2>
			<div class="counts"><span class="textField">zone数量：<label class="zoneCount"></label></span></div>
			<div class="separation"></div>
			<div class="selectCluster">
				<label>请选择一级资源池</label><select id="cluster"></select>
			</div>
			<ul class="nodeViewContainer firstResource" frame="main">
			<!-- <li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li class="alert"><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li>
			<li><table class="nodeCell"><tr><td><a href="####">信息资源池</a></td></tr></table></li> -->
			</ul>
			<div class="hidearea resource">
				
			</div>
			<div class="nodeViewTips radiusAll">
				<a class="close floatr" href="####"></a>
				<h3>资源池容量不足</h3>
				<div class="separation"></div>
				<div class="status">
					<table>
						<tr>
						<td rowspan="4" valign="top" align="center">
							<ul class="nodeViewContainer">
							<li><table class="nodeCell"><tr><td><a href="javascript:void(0)" class='pool_name'>信息资源池</a></td></tr></table></li>
							</ul>
							<a href="javascript:void(0)" class="view_resource">查看资源池</a>
						</td>
						<th><span class="textField">主机数：<label class="host_total"></label></span></th>
						<th><span class="textField">二级资源池：<label class="second_total"></label></span></th>
						<th><span class="textField">虚机数：<label class="guest_total"></label></span></th>
						</tr>
						<tr>
						<td><span class="textField">CPU量：<label class="cpu_total"></label> 颗</span></td>
						<td>
							<div class="process"><div class="cpu_per" style="width: 50%;"><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="cpu_fp"></label></span><span class="textField">未分配：<label class="cpu_wfp"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已使用：<label class="cpu_use"></label></span><span class="textField">未使用：<label class="cpu_unuse"></label></span></div>
						</td>
						</tr>
						<tr>
						<td><span class="textField">内存量：<label class="memo_total"></label> G</span></td>
						<td>
							<div class="process"><div class="mem_per" style="width: 50%;"><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="memo_fp"></label></span><span class="textField">未分配：<label class="memo_wfp"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已使用：<label class="memo_use"></label></span><span class="textField">未使用：<label class="memo_unuse"></label></span></div>
						</td>
						</tr>
						<tr>
						<td><span class="textField">存储量：<label class="storage_total"></label> G</span></td>
						<td>
							<div class="process"><div class="sto_per" style="width: 50%;"><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="storage_fp"></label></span><span class="textField">未分配：<label class="storage_wfp"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="" style="width: 50%;"><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已使用：<label class="storage_use"></label></span><span class="textField">未使用：<label class="storage_unuse"></label></span></div>
						</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div class="hidearea resource_status">
				
			</div>
			<div class="separation"></div>
			<div class="status" id="total_status">
				<table>
					<tr>
					<td><span class="textField">CPU量：<label class="total_cpu"></label></span></td>
					<td><span class="textField">内存量：<label class="total_memo"></label></span></td>
					<td><span class="textField">存储量：<label class="total_storage"></label></span></td>
					</tr>
					<tr>
					<td>
						<div class="process"><div class="cpu_per" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
						<div class="counts"><span class="textField">已使用：<label class="cpu_use"></label></span><span class="textField">未使用：<label class="cpu_unuse"></label></span></div>
					</td>
					<td>
						<div class="process"><div class="mem_per" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
						<div class="counts"><span class="textField">已使用：<label class="memo_use"></label></span><span class="textField">未使用：<label class="memo_unuse"></label></span></div>
					</td>
					<td>
						<div class="process"><div class="sto_per" style="width: 70%;" ><div class="process"></div><div class="process end"></div></div></div>
						<div class="counts"><span class="textField">已使用：<label class="storage_use"></label></span><span class="textField">未使用：<label class="storage_unuse"></label></span></div>
					</td>
					</tr>
				</table>
			</div>
			
			<div class="separation"></div>
			<h2 class="radiusTopL radiusTopR">x86物理机信息</h2>
			<div class="status" id="total_status" style="padding-top: 10px; padding-bottom: 10px;">
				<table>
					<tr>
						<td><span class="textField">台数：<label class="total_x86_unit"></label> 台</span></td>
						<td><span class="textField">cpu量：<label class="total_x86_cpu"></label> 核</span></td>
						<td><span class="textField">内存量：<label class="total_x86_memory"></label> MB</span></td>
					</tr>
					<tr>
						<td>
							<div class="process"><div class="cpu_per" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="x86_unit_used"></label></span><span class="textField">未分配：<label class="x86_unit_unused"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="mem_per" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="x86_cpu_used"></label></span><span class="textField">未分配：<label class="x86_cpu_unused"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="sto_per" style="width: 70%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="x86_memory_used"></label></span><span class="textField">未分配：<label class="x86_memory_unused"></label></span></div>
						</td>
					</tr>
				</table>
			</div>
			
			<div class="separation"></div>
			<h2 class="radiusTopL radiusTopR">HP小型机信息</h2>
			<div class="status" id="total_status" style="padding-top: 10px; padding-bottom: 10px;">
				<table>
					<tr>
					<td colspan="3">
						<span class="textField">小型机台数：<label class="total_hpvm_host"></label> 台</span>
					</td>
					</tr>
					<tr>
						<td><span class="textField">虚机台数：<label class="total_hpvm_unit"></label> 台</span></td>
						<td><span class="textField">cpu量：<label class="total_hpvm_cpu"></label> 核</span></td>
						<td><span class="textField">内存量：<label class="total_hpvm_memory"></label> MB</span></td>
					</tr>
					<tr>
						<td>
							<div class="process"><div class="cpu_per" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="hpvm_unit_used"></label></span><span class="textField">未分配：<label class="hpvm_unit_unused"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="mem_per" style="width: 50%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="hpvm_cpu_used"></label></span><span class="textField">未分配：<label class="hpvm_cpu_unused"></label></span></div>
						</td>
						<td>
							<div class="process"><div class="sto_per" style="width: 70%;" ><div class="process"></div><div class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="hpvm_memory_used"></label></span><span class="textField">未分配：<label class="hpvm_memory_unused"></label></span></div>
						</td>
					</tr>
				</table>
			</div>
			
		</div>
		 <div id="sideBoard">
			<%-- <div id="kvmBoard" class="blockBoard radiusAll">
				<h2 class="radiusTopL radiusTopR">KVM资源池</h2>
				<div class="counts"><span class="textField">主机数：<label>${hostCount}</label></span><span class="textField">虚机数：<label>${vmCount}</label></span></div>
				<div class="separation"></div>
				<ul class="nodeViewContainer" frame="static">
				<li><table class="nodeCell"><tr><td></td></tr></table></li>
				</ul>
			</div>
			<div id="miniBoard" class="blockBoard radiusAll">
				<h2 class="radiusTopL radiusTopR">小型机资源池</h2>
				<div class="counts"><span class="textField">主机数：<label>${list[2].hostCount}</label></span><span class="textField">虚机数：<label>${list[2].vmCount}</label></span></div>
				<div class="separation"></div>
				<ul class="nodeViewContainer" frame="static">
				<li><table class="nodeCell"><tr><td></td></tr></table></li>
				</ul>
			</div> --%>
		</div> 
    </div>
</div>

<div id="footer" class="wrapper">
	<p>中国移动通信 - 深圳云计算IAAS管理平台</p>
    <p>- 版权信息 -</p>
</div>

</div>
</body>
</html>
