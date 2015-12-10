<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script language="JavaScript" type="text/javascript">
var chartDatas = {};
//
$(function() {
	$('#inforTabs').tabs({selected:0});
	//
	$('#queryResultTable').jqGrid({
		url:'respool/thirdQueryVm.action',
		postData:{ oid : "${oid}" },
		datatype: "json",
		width:920,
		height: "100%",
		jsonReader: {
		    repeatitems: false,
		    id: 'id',
		    root:  function(obj) {return obj.result; },
		    page:  function(obj) {return obj.currentPage; },
		    total: function(obj) { return obj.pageCount; },
		    records: function(obj) { return obj.totalCount; }
	    },
		colNames:['id','虚机名称','所属主机','CPU数量','内存','存储','运行状态'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'displayName',index:'displayName'},
	   		{name:'profile',index:'hostResourceName',formatter:function(val){
	   			if(val[0] && val[0].hostResourceName){
	   				return val[0].hostResourceName;
	   			} else {
	   				return '';
	   			}
	   		}},
	   		{name:'profile',index:'cpuCount',formatter:function(val){
	   			if(val[0] && val[0].cpuCount){
	   				return val[0].cpuCount;
	   			} else {
	   				return '';
	   			}
	   		}},
	   		{name:'profile',index:'ram',formatter:function(val){
	   			if(val[0] && val[0].ram){
					return val[0].ram+(val[0].ramUnit=='0'?' GB':(val[0].ramUnit=='1'?' MB':(val[0].ramUnit=='2'?' KB':'')));
	   			}else{
	   				return '';
	   			}
	   		}},
	   		{name:'diskInfo',index:'diskInfo',formatter:function(val){
	   			if(!val)
	   				return '';
	   			if(val[0]){
	   				return val.length + ' 块';
	   			}else{
	   				return '';
	   			}
	   		}},
	   		{name:'startupstatus',index:'startupstatus',formatter:function(val){
	   			switch(val){
	   				case '0':return'运行';
	   				case '1':return'关机';
	   				case '2':return'暂停';
	   				case '3':return'保存中';
	   				case '4':return'处理失败';
	   				case '5':return'丢失';
	   				case '6':return'状态变更中';
	   				case '7':return'已移除';
	   				default:return'';
	   			}
	   		}}
		],
		rowNum: 10,
		altRows:true,
		altclass:'r1',
		forceFit:true,
		pager: '#queryResultPage',
		sortname: 'creationDate',
		viewrecords: true,
		sortorder: "desc",
		caption:"",
		gridComplete: function() {
			//
		}
	});

});
</script>
<div id="inforTabs">
    <ul>
    <li><a href="#baseInfor">基本信息</a></li>
    <li><a href="#virtualInfor">虚机列表</a></li>
    </ul>
    <div id="baseInfor">
    	<table width="100%" class="layoutTable">
        <tr>
        <td>资源池名称：${cbmsServiceInstance.name}</td>
        <td>资源池级别：三级</td>
        </tr>
        <tr>
        <td colspan="4">资源池说明：${perforConfig}</td>
        </tr>
        </table>
    </div>
    <div id="virtualInfor">
        <table id="queryResultTable"></table>
        <div id="queryResultPage"></div>
    </div>
</div>
