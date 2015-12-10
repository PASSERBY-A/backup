<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>中国移动通信 - 深圳云计算IAAS管理平台 - 三级视图</title>
<link href="${ctx}/jsp/iaaspool/visuals/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/jsp/iaaspool/visuals/components.css" rel="stylesheet" type="text/css">
<style>
	.alert{
		background: url("${ctx}/jsp/iaaspool/visuals/alert.png") no-repeat 0 50%;
		color: #c8741e;
		padding-top: 20px;
		font-size: 2.2em;
		font-weight: normal;
		padding-left: 50px;
		background-position: 0 15px;
		display: none;
	}
	.pool_name{
		word-break: break-all;
		display: inline-block;
	}
	#endNodes .barNode .status{
		margin-left: 0px;
	}
	#endNodes .barNode table.nodeCell{
		display: inline-block;
		width: 140px;
		word-break: break-all;
	}
#endNodes .barNode .status .textField{
		margin-left: 70px;
		margin-top: 20px;
		display: inline-block;
	}
	.barNode li{
		width: 240px;
	}
	#endNodes .barNode table.nodeCell{
		margin-left: 60px;
	}
	#endNodes .barNode li{
		background-position: 35px 10px;
	}
	.status th .textField label{
		font-size: 14px;
	}
</style>
<script type="text/javascript">
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
</script>
<script type="text/javascript" src="${ctx}/jsp/iaaspool/libs/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/jsp/iaaspool/libs/NodesViews.js"></script>
<script type="text/javascript" src="${ctx}/scripts/cloud.core.callbacks.js"></script>
</head>
<div id="page-body">

<div id="header">
	<div class="wrapper relaContainer">
    	<div id="logoBar"><img src="${ctx}/jsp/iaaspool/visuals/logoIAAS.png" width="360" height="60"></div>
        <div id="topBar">
        	<div><script>document.write(new Date().format('yyyy-MM-dd'));</script></div>
        	<div> <label>用户:${sessionScope.activeUser.userName} </label></div>
        </div>
    </div>
</div>

<div id="bodyer" class="wrapper">
	<div id="pathBar" class="relaContainer">
    	<a href="javascript:void(history.go(-1))" class="returnBtn"><span class="icon floatl"></span>返回</a>
    	<a href="${ctx}" style="font-size:1.25em; text-decoration:none;float:right;padding:0 10px;">管理平台</a>
    	<h1 class="pathText"><label>IAAS资源池 /主机：${otitle} /</label> 主机：${title}</h1>
    </div>
	<div id="pageContents">
		<div id="endNodes" class="blockBoard relaContainer radiusAll">
			<h2 class="radiusTopL radiusTopR">${title}</h2>
			<div id="nodeDetails">
				<ul class="nodeViewContainer bigNode floatl" frame="static">
				<li><table class="nodeCell"><tr><td><div class="subTitle">主机</div><a class="pool_name">${title}</a></td></tr></table></li>
				</ul>
				<div class="status">
					<table>
						<tr>
						<th colspan="3" valign="bottom">
							<h3 class="alert">资源池容量不足</h3>
							<span class="textField">虚拟机数量：<label>${hostResource.hostVmAndResourceCount.resourceCount}</label></span>
						</th>
						</tr>
						<tr>
						<td><span class="textField">CPU量：<label class="cpu_number"></label> 颗</span></td>
						<td><span class="textField">内存量：<label class="memory_number"></label>G</span></td>
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
						</tr> 
						
						
					</table>
				</div>
			</div>
			<div class="separation"></div>
			<div id="subLevel" class="relaContainer">
				<h3>虚拟机列表</h3>
				<ul class="nodeViewContainer barNode" frame="bar">
				<c:forEach var="item" items="${list}">
				<li>
					<table class="nodeCell floatl"><tr><td><a href="####">
						<c:if test="${item.instancename ne null}">
							${item.instancename}
						</c:if>
						<c:if test="${item.instancename  eq null}">
							${item.name}
						</c:if>
						</a></td></tr></table>
					<div class="status">
						<table>
							<tr>
							<th colspan="3" valign="top">
								<span class="textField">IP:<label>${item.nic[0].ipaddress}

</label></span>
							</th>
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
<script language="JavaScript" type="text/javascript">
$(function() {
	nodesViews.init('.nodeViewContainer',2);
});

$(document).ready(function(){
	$.ajax({
		type : "POST",
		url: clientApiUrl+"?command=listHosts&response=json&id=${hostid}",
		dataType:'json',
		async:false,
		timeout:20000,
		success : function(data) {
			if(data&&data.listhostsresponse&&data.listhostsresponse.host&&data.listhostsresponse.host[0]){
				var d=data.listhostsresponse.host[0];
				$(".cpu_number").text(d.cpunumber);
				$(".memory_number").text((d.memorytotal/1024/1024/1024).toFixed(0));
				$(".storage_number").text(d.cpunumber);
				$("td.cpu .process .fp").css("width",d.cpuallocated);
				$("td.cpu .counts .cpu_fp").text(d.cpuallocated);
				$("td.cpu .counts .cpu_wfp").text(parseFloat(100-d.cpuallocated.substring(0,d.cpuallocated.indexOf("%"))));
				$("td.cpu .process .use").css("width",d.cpuused);
				$("td.cpu .counts .cpu_use").text(d.cpuused);
				$("td.cpu .counts .cpu_unuse").text(parseFloat(100-d.cpuused.substring(0,d.cpuused.indexOf("%"))));
				$("td.memory .process .fp").css("width",(100*(d.memoryallocated/d.memorytotal)).toFixed(0)+"%");
				$("td.memory .counts .memory_fp").text((100*(d.memoryallocated/d.memorytotal)).toFixed(0)+"%");
				$("td.memory .counts .memory_wfp").text(parseFloat(100*((d.memorytotal-d.memoryallocated)/d.memorytotal)).toFixed(0)+"%");
				
				$("td.memory .process .use").css("width",(100*(d.memoryused/d.memorytotal)).toFixed(0)+"%");
				$("td.memory .counts .memory_use").text((100*(d.memoryused/d.memorytotal)).toFixed(0)+"%");
				$("td.memory .counts .memory_unuse").text(parseFloat(100*((d.memorytotal-d.memoryused)/d.memorytotal)).toFixed(0)+"%");
				//$("td.memory .counts .memory_use").text("90%");
			}
		}
	})
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
</html>
