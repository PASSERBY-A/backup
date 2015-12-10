<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript">
function auth_lv2_back(){
	$('#tabs-3-part1').css("display","block");
	$('#tabs-3-part2').html("");
}
function auth_lv2_submit(){
	if(confirm("确定提交表单?")){
		var ids = $('#queryResultTable_user_2').getDataIDs();
		$.ajax("../respool/changeResPoolAccessByUserIds.action", {
			dataType:"json",
			cache:false,
			data : {oid:"${oid}",userIds: ids.length==0?null:ids.toString()},
			success:function(msg){
				alert(msg.resultMessage);
				if(msg.resultCode=='success')
					auth_lv2_back();
			},
			complete:function(){
				$("body").css("cursor","auto");
			}
		})
	}
}
function auth_lv2_users(){
	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
	$("#queryResultTable_user").jqGrid()
		.setGridParam({page:1,rowNum:10,url : result+'/respool/queryAllUserByName.action'})
		.setGridParam({postData : {oid:"${oid}",username : $('#auth_lv2_username').val() } })
		.trigger("reloadGrid");
}
function auth_lv2_add(){
	var ids = $('#queryResultTable_user').jqGrid('getGridParam','selarrrow');
	if(!ids || ids.length==0){
		alert("请选择要添加的用户");
		return;
	}
	for ( var i in ids ){
		var vv = $('#queryResultTable_user_2').getInd(ids[i]);
		if(vv == false){
			$("#queryResultTable_user_2").addRowData(ids[i]
				, {"id":ids[i],"loginName":$('#queryResultTable_user').getCell(ids[i],'loginName')
				,"realName":$('#queryResultTable_user').getCell(ids[i],'realName')}, "first"); 
		}
	}  
	var len = ids.length;  
	for(;len>0;len--) {  //del
		$("#queryResultTable_user").jqGrid("delRowData", ids[0]);  
	}
}
function auth_lv2_rm(){
	var ids = $('#queryResultTable_user_2').jqGrid('getGridParam','selarrrow');
	if(!ids || ids.length==0){
		alert("请选择要移除的用户");
		return;
	}
	for ( var i in ids ){
		var vv = $('#queryResultTable_user').getInd(ids[i]);
		if(vv == false){
			$("#queryResultTable_user").addRowData(ids[i]
				, {"id":ids[i],"loginName":$('#queryResultTable_user_2').getCell(ids[i],'loginName')
				,"realName":$('#queryResultTable_user_2').getCell(ids[i],'realName')}, "first"); 
		}
	} 
	var len = ids.length;  
	for(;len>0;len--) {  //del
		$("#queryResultTable_user_2").jqGrid("delRowData", ids[0]);  
	}
}
$(function() {
	$('#queryResultTable_user').jqGrid({
		url:'../respool/queryAllUserByName.action',
		postData:{ oid:"${oid}" },
		datatype: "json",
		width:340,
		height: 170,
		multiselect:true,
		jsonReader: {
			  repeatitems: false,
			  id: 'loginName',
			  root: 'rows',
			  page:  function(obj) {return obj.page; },
			  total: function(obj) { return obj.total; },
			  records: function(obj) { return obj.records; }
	    },
		colNames:['id','帐户','用户名'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'loginName',index:'loginName'},
	   		{name:'realName',index:'realName'}
		],
		rowNum: 10,
		altRows:true,
		forceFit:true,
		pager: '#queryResultPage_user',
		sortname: 'id',
		viewrecords: true,
		sortorder: "desc"
	});
	
	$('#queryResultTable_user_2').jqGrid({
		url:'../respool/queryPoolUsers.action',
		postData:{ oid:"${oid}" },
		datatype: "json",
		width:340,
		height: 170,
		multiselect:true,
		jsonReader: {
			  repeatitems: false,
			  id: 'loginName',
			  root: 'rows',
			  page:  function(obj) {return obj.page; },
			  total: function(obj) { return obj.total; },
			  records: function(obj) { return obj.records; }
	    },
		colNames:['id','帐户','用户名'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'loginName',index:'loginName'},
	   		{name:'realName',index:'realName'}
		],
		rowNum: 1000,
		altRows:true,
		forceFit:true,
		pager: '#queryResultPage_user_2',
		sortname: 'id',
		viewrecords: true,
		sortorder: "desc"
	});
})
</script>

<div>管理用户设置</div>

<table cellspacing="0" class="topTable" width="100%" style="margin:0 30px">
	<colgroup><col width="40%"/><col width="20%"/><col width="40%"/></colgroup>
	<tr><td>用户列表 : <input class="inputBlock textInput" type="text" id="auth_lv2_username"/><input class="specialBtn" type="button" onclick="auth_lv2_users()" value="查询" /></td>
		<td>&nbsp;</td>
		<td>现有管理员:</td>
	</tr>
	<tr>
		<td><table id="queryResultTable_user"></table>
	        <div id="queryResultPage_user"></div></td>
		<td align="center"><input class="specialBtn" type="button" onclick="auth_lv2_add()" value="&gt;&gt;" />
			<br/><br/><input class="specialBtn" type="button" onclick="auth_lv2_rm()" value="&lt;&lt;" /></td>
		<td><table id="queryResultTable_user_2"></table>
	        <div id="queryResultPage_user_2"></div></td>
	</tr>
	<tr><td colspan="3"><input class="specialBtn" type="button" onclick="auth_lv2_submit()" value="提交" style="margin:10px 120px 0 320px" />
			<input class="specialBtn" type="button" onclick="auth_lv2_back()" value="返回" /></td>
	</tr>
</table>
