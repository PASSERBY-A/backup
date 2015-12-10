<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript">
function cluster_lv1_submit(){
	if($.trim($('#clouster_res_form_displayName').val())==''){
		alert("资源池名称不能为空");
		return;
	}
	if($('#clouster_res_form_desc').val()==''){
		alert("资源池描述不能为空");
		return;
	}
	 
	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);

	var  url = result+"/respool/firstUpdateCluster.action";
	
	if(confirm("确定提交表单?")){
		$("body").css("cursor","wait");
		$(".cluster_lv1_submit").attr("disabled",true);
		
		var clouster_res_form_name = document.getElementById('clouster_res_form_name').value;
		var clouster_res_form_displayName = document.getElementById('clouster_res_form_displayName').value;
		
		var clouster_res_form_hypervisorType = document.getElementById('clouster_res_form_hypervisorType').value;
		var typeStr = "";
		if(clouster_res_form_hypervisorType){
				switch(clouster_res_form_hypervisorType){
					case '0':typeStr =  'VSPHERE';break;
					case '1':typeStr = 'HYPERV';break;
					case '2':typeStr = 'KVM';break;
					case '3':typeStr = 'POWERVM';break;
					default:typeStr = '';
				}
			}
		var clouster_res_form_desc = document.getElementById('clouster_res_form_desc').value;
		var clusterIdforEdit = document.getElementById('clusterIdforEdit').value;
		
		
		$.ajax(url, {
			dataType:"json",
			cache:false,
			data:$('#cluster_res_form').serialize(), 
			type : "POST",
			success:function(msg){
				$("body").css("cursor","auto");
				$(".cluster_lv1_submit").attr("disabled",false);
				alert(msg.resultMessage);
				if(msg.resultCode=='success'){
					$(".dTreeNode a.nodeSel").text(clouster_res_form_displayName);
					cluster_lv1_back(clouster_res_form_name,clouster_res_form_displayName,typeStr,clouster_res_form_desc,clusterIdforEdit);
					reLoadLv1();
				}
			},
			error:function(){
				$("body").css("cursor","auto");
				$(".cluster_lv1_submit").attr("disabled",false);
			}
		})
	}
}
function cluster_lv1_back(clouster_res_form_name,clouster_res_form_displayName,typeStr,clouster_res_form_desc,clusterIdforEdit){
	if(!(clouster_res_form_name&&clouster_res_form_name)){
		  clouster_res_form_name = document.getElementById('clouster_res_form_name').value;
		  clouster_res_form_displayName = document.getElementById('clouster_res_form_displayName').value;
		  clouster_res_form_hypervisorType = document.getElementById('clouster_res_form_hypervisorType').value;
		  
 		if(clouster_res_form_hypervisorType){
 				switch(clouster_res_form_hypervisorType){
	   				case '0':typeStr =  'VSPHERE';break;
	   				case '2':typeStr = 'KVM';break;
	   				case '4':typeStr = '小型机';break;
	   				default:typeStr = '';
 				}
 		}
			
		  clouster_res_form_desc = document.getElementById('clouster_res_form_desc').value;
		  clusterIdforEdit = document.getElementById('clusterIdforEdit').value;
	}
	$('#tabs-1-info-part2').html("");
	
	$('#tabs-1-info').html("<table width='100%' class='table'><tr><td>虚拟化平台名称："+clouster_res_form_name+"</td><td>资源池级别：一级</td></tr>"+
			"<tr><td>资源池名称:"+clouster_res_form_displayName+"</td><td>虚拟化平台:"+typeStr+"</td></tr>"+
			"<tr><td>资源池描述:"+clouster_res_form_desc+"</td></tr>"+
			"<tr><td align='center' colspan='3'></td><td align='right'>"+"<input class='specialBtn res_lv2_submitx' type='button' onclick=\"editcluster(\'"+clusterIdforEdit+"\')\""+ " value='编辑' />"+"</td></tr>"+
	"</table>");
	$('#tabs-1-info').css("display","block");
	
}
</script>

<h3>
	修改一级资源池
</h3>

<form id="cluster_res_form" action="" method="post" class="form_content">
<input type="hidden" id="clusterIdforEdit" name="clusterId" value="${clusterId}" />
<table cellspacing="20" class="table" width="100%">
	<tr>
	<td class="td_title" width="25%" align="right">虚拟化平台名称：</td>
	<td>
	<input class="w140" type="text" id="clouster_res_form_name" name="clusterNametxt" value="${cbmsCluster.name}" disabled="disabled"/>
	</td>
	</tr>
	<tr>
	<td class="td_title" width="25%" align="right">资源池级别：</td>
	<td>
	<input class="w140" type="text" id="clouster_res_form_Level" name="clusterLeveltxt" value="一级" disabled="disabled"/>
	</td>
	</tr>
	
	<tr><td width="25%" align="right" class="td_title">资源池名称:</td>
		<td><input class="w140" type="text" id="clouster_res_form_displayName" name="displayName" value="${cbmsCluster.displayName}"  /></td>
	</tr>
	<tr>
		<!-- <td>
		<td align="right">资源池类型 :</td>
		 <input class="inputBlock textInput" type="text" id="host_res_form_clusterType" name="clusterTypetxt" value="${cbmsCluster.clusterType}" disabled="disabled"/>
		<input  class="inputBlock textInput" type="hidden" name="clusterType"  value="${cbmsCluster.clusterType}"/>
		 -->
		     <td width="25%" align="right" class="td_title">虚拟化平台：</td>
        <td ><select id="clouster_res_form_hypervisorType" name="hypervisorType" class="queryForm w140">
        	<c:choose>
        		<c:when test="${cbmsCluster.hypervisorType==0}">
        			<option value="0" selected>VSPHERE</option>   
        		</c:when>
        		<c:when test="${cbmsCluster.hypervisorType==2}">
        			<option value="2" selected>KVM</option>   
        		</c:when>
        		<c:when test="${cbmsCluster.hypervisorType==4}">
        			<option value="4" selected>小型机</option>   
        		</c:when>
        	</c:choose>
            </select>
        </td> 
	</tr>
	<tr><td align="right" class="td_title">资源池描述 :</td>
		<td><input class="w140" type="text" id="clouster_res_form_desc" name="desc" value="${cbmsCluster.desc}"  /></td>
	</tr>

	
	
	<tr>
	<td>&nbsp;</td>
	<td>
		<input class="specialBtn cluster_lv1_submit" type="button" onclick="cluster_lv1_submit()" value="提交" />
		<input class="specialBtn" type="button" onclick="cluster_lv1_back(null,null,null,null,null)" value="返回" />
	</td>
	</tr>
</table>
</form>
