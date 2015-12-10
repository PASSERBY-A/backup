<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>中国移动通信 - 深圳云计算IAAS管理平台 - 二级视图</title>
<link href="${ctx}/jsp/iaaspool/visuals/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/jsp/iaaspool/visuals/components.css" rel="stylesheet" type="text/css">
<style>
	.alert{
		background: url("${ctx}/jsp/iaaspool/visuals/alert.png") no-repeat 0 50%;
		color: #c8741e;
		padding-top: 20px;
		font-size: 2.2em;
		font-weight: normal;
		margin-bottom: -20px;
		padding-left: 50px;
		background-position: 0 15px;
		display: none;
	}
	.pool_name{
		word-break: break-all;
		display: inline-block;
	}
</style>
<script type="text/javascript" src="${ctx}/jsp/iaaspool/libs/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/jsp/iaaspool/libs/NodesViews.js"></script>
<script type="text/javascript" src="${ctx}/scripts/ui/core.js"></script>
<script type="text/javascript" src="${ctx}/scripts/sharedFunctions.js"></script>
<script language="JavaScript" type="text/javascript">
var contextPath = '${pageContext.request.contextPath}';
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
	nodesViews.init('.nodeViewContainer',1);
	
	$.ajax({
		type : "POST",
		url: clientApiUrl+"?command=listCapacity&zoneid=${zoneId}&response=json",
		dataType:'json',
		timeout:20000,
		async: false,
		success : function(data) {
				if(data&&data.listcapacityresponse&&data.listcapacityresponse.capacity&&data.listcapacityresponse.capacity){
					var data = data.listcapacityresponse.capacity;
					for(var i=0;i<data.length;i++){
						var type = data[i].type;
						var d=data[i];
						if(type == "1"){//cpu
							$(".totalCpu").text(cloudStack.converters.convertByType(d.type, d.capacitytotal));
							$('td.cpu .process .fp').css("width",parseInt(d.percentused)+"%");
							$('td.cpu .counts .cpu_fp').text(parseInt(d.percentused)+"%");
							$('td.cpu .counts .cpu_wfp').text(d.capacitytotal==0?"0%":(100-parseInt(d.percentused))+"%");
							$("td.cpu .process .use").css("width",parseInt(d.percentused)+"%");
							$("td.cpu .counts .cpu_use").text(parseInt(d.percentused)+"%");
							$("td.cpu .counts .cpu_unuse").text(d.capacitytotal==0?"0%":(100-parseInt(d.percentused))+"%");
							
// 							  $(".totalCpu").text((data[i].capacitytotal/1000).toFixed(0));
// 							  /* $(".usedCpu").text(data[i].capacityused);
// 							 $(".unusedCpu").text(data[i].capacitytotal-data[i].capacityused); */
							 
// 							 	$("td.cpu .process .fp").css("width",d.percentused+"%");
// 								$("td.cpu .counts .cpu_fp").text(d.percentused+"%");
// 								$("td.cpu .counts .cpu_wfp").text(parseFloat(100-d.percentused)+"%");
// 								$("td.cpu .process .use").css("width",d.percentused+"%");
// 								$("td.cpu .counts .cpu_use").text(d.percentused+"%");
// 								$("td.cpu .counts .cpu_unuse").text(parseFloat(100-d.percentused)+"%");
								
						}else if(type == "2"){//storage
							$(".totalStorage").text(cloudStack.converters.convertByType(d.type, d.capacitytotal));
							$('td.storage .process .fp').css("width",parseInt(d.percentused)+"%");
							$('td.storage .counts .storage_fp').text(parseInt(d.percentused)+"%");
							$('td.storage .counts .storage_wfp').text(d.capacitytotal==0?"0%":(100-parseInt(d.percentused))+"%");
							$("td.storage .process .use").css("width",parseInt(d.percentused)+"%");
							$("td.storage .counts .storage_use").text(parseInt(d.percentused)+"%");
							$("td.storage .counts .storage_unuse").text(d.capacitytotal==0?"0%":(100-parseInt(d.percentused))+"%");
							
// 							  $(".totalStorage").text((data[i].capacitytotal/1024/1024/1024).toFixed(0));
// 							  /* $(".usedStorage").text((data[i].capacityused/1024/1024).toFixed(0));
// 							 $(".unusedStorage").text(((data[i].capacitytotal-data[i].capacityused)/1024/1024).toFixed(0)); */
							 
// 							 $("td.storage .process .fp").css("width",d.percentused+"%");
// 								$("td.storage .counts .storage_fp").text(d.percentused+"%");
// 								$("td.storage .counts .storage_wfp").text(parseFloat(100-d.percentused)+"%");
								
// 								$("td.storage .process .use").css("width",d.percentused+"%");
// 								$("td.storage .counts .storage_use").text(d.percentused+"%");
// 								$("td.storage .counts .storage_unuse").text(parseFloat(100-d.percentused)+"%");
						}else if(type == "0"){//memory
							$(".totalMemory").text(cloudStack.converters.convertByType(d.type, d.capacitytotal));
							$('td.memory .process .fp').css("width",parseInt(d.percentused)+"%");
							$('td.memory .counts .memory_fp').text(parseInt(d.percentused)+"%");
							$('td.memory .counts .memory_wfp').text(d.capacitytotal==0?"0%":(100-parseInt(d.percentused))+"%");
							$("td.memory .process .use").css("width",parseInt(d.percentused)+"%");
							$("td.memory .counts .memory_use").text(parseInt(d.percentused)+"%");
							$("td.memory .counts .memory_unuse").text(d.capacitytotal==0?"0%":(100-parseInt(d.percentused))+"%");
							
							
// 							 $(".totalMemory").text((data[i].capacitytotal/1024/1024/1024).toFixed(0));
// 							 /*$(".usedMemory").text((data[i].capacityused/1024/1024).toFixed(0));
// 							$(".unusedMemory").text(((data[i].capacitytotal-data[i].capacityused)/1024/1024).toFixed(0)); */
							
// 							$("td.memory .process .fp").css("width",d.percentused+"%");
// 							$("td.memory .counts .memory_fp").text(d.percentused+"%");
// 							$("td.memory .counts .memory_wfp").text(parseFloat(100-d.percentused)+"%");
							
// 							$("td.memory .process .use").css("width",d.percentused+"%");
// 							$("td.memory .counts .memory_use").text(d.percentused+"%");
// 							$("td.memory .counts .memory_unuse").text(parseFloat(100-d.percentused)+"%");
						}
					}
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
	
	$('.nodeViewContainer li table a.second').live('click',function(){
 		$('#oid').val($(this).parent().parent().parent().parent().parent().attr("id"));
 		$('#title').val($(this).parent().parent().parent().parent().parent().attr("title"));
 		$('#queryForm').attr('action',clientApiUrl+'?command=secondResource&cmsz=yes');
 		$('#queryForm').submit();
 	});
});

$(document).ready(function(){
	  $('.counts').each(function(){
		  var label=$(this).find(".textField:eq(0) label");
		  var label_text=0;
		  if($(label).text().lastIndexOf(')')!=-1){
		  		label_text=$(label).text().substring($(label).text().lastIndexOf(')')+1,$(label).text().length);
	  		}else{
	  			label_text=$(label).text();
	  		}
		  if(parseFloat(label_text)>=parseFloat('90%')){
			  $(this).parent().find(".process div:eq(0)").addClass("bad");
		  }
		  $(this).parent().find(".process div:eq(0)").css("width",label_text);
	  })
	  
	  if($('.status .process div').hasClass('bad')){
			$('.alert').show();
		}
	});
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
    	<a href="javascript:void(history.go(-1))" class="returnBtn"><span class="icon floatl"></span>返回</a>
    	<a href="${ctx}" style="font-size:1.25em; text-decoration:none;float:right;padding:0 10px;">管理平台</a>
    	<h1 class="pathText"><label>IAAS资源池 /</label> Zone：${title}</h1>
    </div>
	<div id="pageContents">
		<form id="queryForm" method="post">
				<input type="hidden" name="hostid" id="oid">
				<input type="hidden" name="otitle" id="otitle"  value="${title}">
				<input type="hidden" name="title" id="title">
			</form>
		<div id="subNodes" class="blockBoard relaContainer radiusAll">
			<h2 class="radiusTopL radiusTopR">Zone</h2>
			<div id="nodeDetails">
				<ul class="nodeViewContainer bigNode floatl" frame="static">
				<li><table class="nodeCell"><tr><td><div class="subTitle">Zone</div><a class="pool_name">${title}</a></td></tr></table></li>
				</ul>
				<div class="status">
					<table>
						<tr colspan="3" valign="bottom"><h3 class="alert">资源池容量不足</h3></tr>
						<tr>
						<th colspan="3" valign="bottom">
							<span class="textField">主机数：<label>${fn:length(hostList)}</label></span>
						</th>
						</tr>
						<tr>
						<td><span class="textField">CPU量：<label class="totalCpu"></label></span></td>
						<td><span class="textField">内存量：<label class="totalMemory"> </label></span></td>
						<td><span class="textField">存储量：<label class="totalStorage"></label></span></td>
						</tr>
						 <tr>
						<td class="cpu">
							<div class="process"><div   title="分配比率: 50%" class="fp"><div class="process"></div><div 

class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="cpu_fp">50%</label></span><span class="textField">未分配：

<label class="cpu_wfp">50%</label></span></div>
						</td>
						<td class="memory">
							<div class="process"><div   title="分配比率: 50%" class="fp"><div class="process"></div><div 

class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="memory_fp">50%</label></span><span class="textField">未分配：

<label class="memory_wfp">50%</label></span></div>
						</td>
						<td class="storage">
							<div class="process"><div   title="分配比率: 50%" class="fp"><div class="process"></div><div 

class="process end"></div></div></div>
							<div class="counts"><span class="textField">已分配：<label class="storage_fp">50%</label></span><span class="textField">未分配：

<label class="storage_wfp">50%</label></span></div>
						</td>
						</tr> 
						<tr>
						<td class="cpu">
							<div class="process "><div   title="使用比率: 50%" class="use"><div class="process"></div><div 

class="process end"></div></div></div>
							<div class="counts"><span class="textField">已使用：<label class="cpu_use">50%</label></span><span class="textField">未使用：

<label class="cpu_unuse">50%</label></span></div>
						</td>
						<td class="memory">
							<div class="process"><div   title="使用比率: 50%" class="use"><div class="process"></div><div 

class="process end"></div></div></div>
							<div class="counts"><span class="textField">已使用：<label class="memory_use">50%</label></span><span class="textField">未使用：

<label class="memory_unuse">50%</label></span></div>
						</td>
						<td class="storage">
							<div class="process"><div   title="使用比率: 50%" class="use"><div class="process"></div><div 

class="process end"></div></div></div>
							<div class="counts"><span class="textField">已使用：<label class="storage_use">50%</label></span><span class="textField">未使用：

<label class="storage_unuse">50%</label></span></div>
						</td>
						</tr> 
						
					</table>
				</div>
			</div>
			<div class="separation"></div>
			<div id="subLevel" class="relaContainer">
				<h3>主机列表</h3>
				<ul class="nodeViewContainer barNode" frame="bar">
					<c:forEach var="item" items="${hostList}">
				<li id="${item.id}" title="${item.name}">
					<table class="nodeCell floatl"><tr><td><a href="javascript:void(0)" class="pool_name second">${item.name}</a></td></tr></table>
					<div class="status">
						<table>
							<tr>
							<th colspan="3" valign="bottom">
								<span class="textField">IP地址：<label class="ip">${item.ipaddress}</label></span>
							</th>
							</tr>
							<tr>
							<td><span class="textField">cpu数量：<label class="cpu_num">${item.cpunumber}</label></span></td>
							</tr>
							
							<tr>
							<td>
								<div class="process"><div class="use" style="${item.cpuused}"><div class="process"></div><div class="process end"></div></div></div>
								<div class="counts">
								<span class="textField">已使用：<label class="use">${item.cpuused}</label></span>
								<span class="textField">未使用：<label class="unuse"><script>document.write(parseFloat(100-parseFloat('${item.cpuused}'))+"%")</script></label></span></div>
							</td>
							</tr>
							 
						</table>
					</div>
				</li>
				</c:forEach>
				</ul>
			</div>
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
