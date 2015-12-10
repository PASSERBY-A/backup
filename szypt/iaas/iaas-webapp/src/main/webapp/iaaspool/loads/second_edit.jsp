<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript">
function res_lv2_submit(){
	if($.trim($('#lv2_res_form_name').val())==''){
		alert("资源池名称不能为空");
		return;
	}
	if($('#lv2_res_form_cpu').val()==''){
		alert("CPU大小不能为空");
		return;
	}
	if($('#lv2_res_form_ram').val()==''){
		alert("内存大小不能为空");
		return;
	}
	if($('#lv2_res_form_storage').val()==''){
		alert("存储大小不能为空");
		return;
	}
	if($('#lv2_res_form_name').val().length > 64){
		alert("资源池名称长度最多64个字符");
		return;
	}
	var poolId = $('#lv2_poolId').val();
	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);

	var url;
	if($.trim(poolId)==''){
		url = result+"/respool/secondAddCbmsResourcePool.action";
	}else{
		url = result+"/respool/secondUpdateCbmsResourcePool.action";
	}
	if(confirm("确定提交表单?")){
		$("body").css("cursor","wait");
		$(".res_lv2_submit").attr("disabled",true);
		$.ajax(url, {
			dataType:"json",
			cache:false,
			data:$('#lv2_res_form').serialize(), 
			type : "POST",
			success:function(msg){
				$("body").css("cursor","auto");
				$(".res_lv2_submit").attr("disabled",false);
				alert(msg.resultMessage);
				if(msg.resultCode=='success'){
					res_lv2_back();
					reLoadLv2();
				}
			},
			error:function(){
				$("body").css("cursor","auto");
				$(".res_lv2_submit").attr("disabled",false);
			}
		})
	}
}
function res_lv2_back(){
	$('#tabs-3-part1').css("display","block");
	$('#tabs-3-part2').html("");
}
</script>

<h3>
	<c:if test="${cbmsResourcePool==null}">新增2级资源池</c:if>
	<c:if test="${cbmsResourcePool!=null}">修改2级资源池</c:if>
</h3>

<form id="lv2_res_form" action="" method="post" class="form_content">
<input type="hidden" name="clusterId" value="${clusterId}" />
<input type="hidden" id="lv2_poolId" name="poolId" value="${cbmsResourcePool.id}" />
<table cellspacing="20" class="table" width="100%">
	<tr><td width="25%" align="right" class="td_title">池名称:</td>
		<td><input class="w140" type="text" id="lv2_res_form_name" name="name" value="${cbmsResourcePool.name}" /></td>
	</tr>
	<tr><td align="right" class="td_title">cpu个数 :</td>
		<td><input class="w140" type="text" id="lv2_res_form_cpu" name="cpuCap" value="${cbmsResourcePool.cpuCap}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
	</tr>
	<tr><td align="right" class="td_title">内存大小(MB):</td>
		<td><input class="w140" type="text" id="lv2_res_form_ram" name="ramCap" value="${cbmsResourcePool.ramCap}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
	</tr>
	<tr><td align="right" class="td_title">存储大小(MB):</td>
		<td><input class="w140" type="text" id="lv2_res_form_storage" name="storageCap" value="${cbmsResourcePool.storageCap}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" /></td>
	</tr>
	<tr><td align="right" class="td_title">描述:</td>
		<td><input class="w140" type="text" name="description" value="${cbmsResourcePool.description}" /></td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	<td>
		<input class="specialBtn res_lv2_submit" type="button" onclick="res_lv2_submit()" value="提交" />
		<input class="specialBtn" type="button" onclick="res_lv2_back()" value="返回" />
	</td>
	</tr>
</table>
</form>
