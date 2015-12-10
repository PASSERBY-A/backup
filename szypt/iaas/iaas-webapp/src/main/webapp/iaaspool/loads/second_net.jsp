<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript">
function pools_lv2_back(){
	$('#tabs-3-part1').css("display","block");
	$('#tabs-3-part2').html("");
}
function pools_lv2_submit(){
	if(confirm("确定提交表单?")){
		var ids = $('#queryResultTable_pools_2').getDataIDs();
		$.ajax("../respool/changeResPoolsOwner.action", {
			dataType:"json",
			cache:false,
			data : {oid:"${oid}",poolIds: ids.length==0?null:ids.toString()},
			success:function(msg){
				alert(msg.resultMessage);
				if(msg.resultCode=='success')
					pools_lv2_back();
			},
			complete:function(){
				$("body").css("cursor","auto");
			}
		})
	}
}
function pools_lv2_L(){
	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    
	$("#queryResultTable_pools").jqGrid()
		.setGridParam({page:1,rowNum:10,url : result+'/respool/queryFreeIpPool.action'})
		.setGridParam({postData : { name : $("#pools_lv2_name").val() } })
		.trigger("reloadGrid");
}
function pools_lv2_add(){
	var ids = $('#queryResultTable_pools').jqGrid('getGridParam','selarrrow');
	if(!ids || ids.length==0){
		alert("请选择要添加的IP池");
		return;
	}
	for ( var i in ids ){
		var vv = $('#queryResultTable_pools_2').getInd(ids[i]);
		if(vv == false){
			$("#queryResultTable_pools_2").addRowData(ids[i]
				, {"id":ids[i],"name":$('#queryResultTable_pools').getCell(ids[i],'name')
				,"ipAddressList":$('#queryResultTable_pools').getCell(ids[i],'ipAddressList')}, "first"); 
		}
	}  
	var len = ids.length;  
	for(;len>0;len--) {  //del
		$("#queryResultTable_pools").jqGrid("delRowData", ids[0]);  
	}
}
function pools_lv2_rm(){
	var ids = $('#queryResultTable_pools_2').jqGrid('getGridParam','selarrrow');
	if(!ids || ids.length==0){
		alert("请选择要移除的IP池");
		return;
	}
	var ipFree=0,ipTotal=0;
	for ( var i in ids ){
		ipFree = $('#queryResultTable_pools_2').getCell(ids[i],'ipFree');
		ipTotal = $('#queryResultTable_pools_2').getCell(ids[i],'ipTotal');
		//if(ipFree < ipTotal){
		//	alert("IP池中有已使用的IP,池名称:"+$('#queryResultTable_pools_2').getCell(ids[i],'name'));
		//	return ;
		//}
	}  
	for ( var i in ids ){
		var vv = $('#queryResultTable_pools').getInd(ids[i]);
		if(vv == false){
			$("#queryResultTable_pools").addRowData(ids[i]
				, {"id":ids[i],"name":$('#queryResultTable_pools_2').getCell(ids[i],'name')
				,"ipAddressList":$('#queryResultTable_pools_2').getCell(ids[i],'ipAddressList')}, "first"); 
		}
	} 
	var len = ids.length;  
	for(;len>0;len--) {  //del
		$("#queryResultTable_pools_2").jqGrid("delRowData", ids[0]);  
	}
}
$(function() {
	$('#queryResultTable_pools').jqGrid({
		url:'../respool/queryFreeIpPool.action',
		datatype: "json",
		width:340,
		height: 170,
		multiselect:true,
		jsonReader: {
			  repeatitems: false,
			  root:  function (obj) { return obj.result; },
			  id: 'id',
			  page:  function(obj) {return obj.page; },
			  total: function(obj) { return obj.total; },
			  records: function(obj) { return obj.records; },
	    },
		colNames:['id','IP池名称','可用ip数','ip总数','ip列表'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'ipFree',index:'ipFree'},
	   		{name:'ipTotal',index:'ipTotal'},
	   		{name:'ipAddressList',index:'ipAddressList',hidden:true,formatter:function(val){
	   			if(typeof(val)=='string')
	   				return val;
	   			var ipFree=0,ipTotal=0;
	   			if(val!=null){
					ipTotal = val.length;
					for(i=0;i<val.length;i++){
	   					if(val[i].status==0)
	   						ipFree++;
	   				}
				}
	   			return ipFree+"#"+ipTotal;
	   		}}
		],
		rowNum: 10,
		altRows:true,
		forceFit:true,
		pager: '#queryResultPage_pools',
		sortname: 'id',
		viewrecords: true,
		sortorder: "desc",
	    gridComplete: function() {
			var ids = $('#queryResultTable_pools').getDataIDs();  
			var ipFree,ipTotal;
			for ( var i in ids ){
				var vals = $('#queryResultTable_pools').getCell( ids[i] , 'ipAddressList').split("#");
				$('#queryResultTable_pools').setRowData( ids[i], { "ipFree": vals[0],"ipTotal": vals[1]});
			}
	    }
	});
	
	$('#queryResultTable_pools_2').jqGrid({
		url:'../respool/secondQueryAllIpPools.action',
		datatype: "json",
		postData:{ oid:"${oid}" },
		width:340,
		height: 170,
		multiselect:true,
		jsonReader: {
			  repeatitems: false,
			  root:  function (obj) { return obj.result; },
			  id: 'id',
			  page:  function(obj) {return obj.page; },
			  total: function(obj) { return obj.total; },
			  records: function(obj) { return obj.records; },
	    },
	    colNames:['id','IP池名称','可用ip数','ip总数','ip列表'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'ipFree',index:'ipFree'},
	   		{name:'ipTotal',index:'ipTotal'},
	   		{name:'ipAddressList',index:'ipAddressList',hidden:true,formatter:function(val){
	   			if(typeof(val)=='string')
	   				return val;
	   			var ipFree=0,ipTotal=0;
	   			if(val!=null){
					ipTotal = val.length;
					for(i=0;i<val.length;i++){
	   					if(val[i].status==0)
	   						ipFree++;
	   				}
				}
	   			return ipFree+"#"+ipTotal;
	   		}}
		],
		loadonce: true,
		rowNum: 1000,
		altRows:true,
		forceFit:true,
		pager: '#queryResultPage_pools_2',
		sortname: 'id',
		viewrecords: true,
		sortorder: "desc",
	    gridComplete: function() {
			var ids = $('#queryResultTable_pools_2').getDataIDs();  
			var ipFree,ipTotal;
			for ( var i in ids ){
				var vals = $('#queryResultTable_pools_2').getCell( ids[i] , 'ipAddressList').split("#");
				$('#queryResultTable_pools_2').setRowData( ids[i], { "ipFree": vals[0],"ipTotal": vals[1]});
			}
	    }
	});
})
</script>

<div>二级资源池网络设置</div>

<table cellspacing="0" class="topTable" width="100%" style="margin:0 30px">
	<colgroup><col width="40%"/><col width="20%"/><col width="40%"/></colgroup>
	<tr><td>IP池列表:<input class="inputBlock textInput" type="text" id="pools_lv2_name"/><input class="specialBtn" type="button" onclick="pools_lv2_L()" value="查询" /></td>
		<td>&nbsp;</td>
		<td>现有IP池:</td>
	</tr>
	<tr>
		<td><table id="queryResultTable_pools"></table>
	        <div id="queryResultPage_pools"></div></td>
		<td align="center"><input class="specialBtn" type="button" onclick="pools_lv2_add()" value="&gt;&gt;" />
			<br/><br/><input class="specialBtn" type="button" onclick="pools_lv2_rm()" value="&lt;&lt;" /></td>
		<td><table id="queryResultTable_pools_2"></table>
	        <div id="queryResultPage_pools_2"></div></td>
	</tr>
	<tr><td colspan="3"><input class="specialBtn" type="button" onclick="pools_lv2_submit()" value="提交" style="margin:10px 120px 0 320px" />
			<input class="specialBtn" type="button" onclick="pools_lv2_back()" value="返回" style="margin:10px 0px" /></td>
	</tr>
</table>
