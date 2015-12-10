<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script language="JavaScript" type="text/javascript">
var chartDatas = {};
//
$(function() {
	$('#inforTabs').tabs({selected:1});
	//
	$.ajax("respool/firstClusterResource.action", {
		dataType:"json",
		cache:false,
		data:{oid : "${oid}" },
		success:function(data,txt,xhr){
			chartDatas = {
				"cpu":{"data":[
					{"title":"未使用", "value":data[0]==null?0:data[0].totalCpu-data[0].usedCpu},
					{"title":"已使用", "value":data[0]==null?0:data[0].usedCpu}
				]},
				"ram":{"data":[
					{"title":"未使用", "value":data[0]==null?0:data[0].totalMemory-data[0].usedMemory},
					{"title":"已使用", "value":data[0]==null?0:data[0].usedMemory}
				]},
				"hd":{"data":[
					{"title":"未使用", "value":data[0]==null?0:data[0].totalStorage-data[0].usedStorage},
					{"title":"已使用", "value":data[0]==null?0:data[0].usedStorage}
				]},
				"pool":{"data":[
					{"title":"总数", "value":data[1]==null?0:data[1]}
				]},
				"vlan":{"total":0,"page":1,"pageSize":8,"data":[],"allData":[]}
			};
			if(data[2]!=null){
				chartDatas.vlan.total = Math.ceil(data[2].length/chartDatas.vlan.pageSize);
				for(i=0;i<data[2].length;i++){
					chartDatas.vlan.allData.push(data[2][i]);
				}
				chartDatas.vlan.data = chartDatas.vlan.allData.slice(0,chartDatas.vlan.pageSize);
			}
			initPieCart('cpuPie',chartDatas.cpu.data);
			initPieCart('ramPie',chartDatas.ram.data);
			initPieCart('hdPie',chartDatas.hd.data);
			initPieCart('poolPie',chartDatas.pool.data);
			initColumn('columnList',chartDatas.vlan,'columnChartPages');
		}
	});
	$('#columnChartControls a').click(function(){
		if( $(this).hasClass("prev") ){
			if(chartDatas.vlan.page==1)return;
			chartDatas.vlan.page--;
		} else if( $(this).hasClass("next") ){
			if(chartDatas.vlan.page  >=  chartDatas.vlan.allData.length/chartDatas.vlan.pageSize )return;
			chartDatas.vlan.page++;
		}
		chartDatas.vlan.data = chartDatas.vlan.allData.slice(chartDatas.vlan.pageSize*(chartDatas.vlan.page-1)
				,chartDatas.vlan.pageSize*chartDatas.vlan.page);
		initColumn('columnList',chartDatas.vlan,'columnChartPages');
		return false;
	});
	//
	$('#queryResultTable01').jqGrid({
		url:'respool/firstQueryResourcePool.action',
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
		colNames:['id','二级资源池名称','CPU容量(个)','内存容量(MB)','存储容量(MB)','描述'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'cpuCap',index:'cpuCap',formatter:function(val){return val==null?0:val}},
	   		{name:'ramCap',index:'ramCap',formatter:function(val){return val==null?0:val}},
	   		{name:'storageCap',index:'storageCap',formatter:function(val){return val==null?0:val}},
	   		{name:'description',index:'description',width:100}
		],
		rowNum: 10,
		altRows:true,
		altclass:'r1',
		forceFit:true,
		pager: '#queryResultPage01',
		sortname: 'creationDate',
		viewrecords: true,
		sortorder: "desc",
		caption:"",
		gridComplete: function() {
			//
		}
	});
	//
	$('#queryResultTable02').jqGrid({
		url:'respool/firstQueryHost.action',
		postData:{
			oid : "${oid}"
		},
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
		colNames:['id','主机名称','CPU数量','内存容量','存储器','运行状态','描述'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'profile',index:'profile',width:60,formatter:function(val){
	   			if (val[0]) return val[0].cpuCount+'个';
	   			else return '---';
	   		}},
	   		{name:'profile',index:'profile', width:100,formatter:function(val){
	   			if (val[0]) return val[0].ram+(val[0].ramUnit=='0'?'GB':(val[0].ramUnit=='1'?'MB':(val[0].ramUnit=='2'?'KB':'')));
	   			else return '---';
	   		}},
	   		{name:'diskInfo',index:'diskInfo',formatter:function(val){
	   			if (val[0]) return val.length + '块';
	   			else return '0块';
	   		}},
	   		{name:'startupstatus',index:'startupstatus', width:60,formatter:function(val){
	   			switch(val){
	   				case '0':return'运行';
	   				case '1':return'关闭';
	   				case '2':return'暂停';
	   				case '3':return'保存中';
	   				case '4':return'处理失败';
	   				case '5':return'丢失';
	   				case '6':return'状态变更中';
	   				case '7':return'已移除';
	   				default:return'';
	   			}
	   		}},
	   		{name:'descr',index:'descr'}
		],
		rowNum: 10,
		altRows:true,
		altclass:'r1',
		forceFit:true,
		pager: '#queryResultPage02',
		sortname: 'creationDate',
		viewrecords: true,
		sortorder: "desc",
		caption:"",
		gridComplete: function() {
			//
		}
	});
	//
	$('#queryResultTable03').jqGrid({
		url:'respool/firstQueryDataStore.action',
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
		colNames:['id','编号','存储名称','状态','总容量','已用量','剩余量'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'name',index:'name'},
	   		{name:'type',index:'type', width:60,formatter:function(val){
	   			switch(val){
	   				case '0':return'运行';
	   				case '1':return'关闭';
	   				case '2':return'暂停';
	   				case '3':return'保存中';
	   				case '4':return'处理失败';
	   				case '5':return'丢失';
	   				case '6':return'状态变更中';
	   				case '7':return'已移除';
	   				default:return'';
	   			}
	   		}},
	   		{name:'maxFileSize',index:'maxFileSize'},
	   		{name:'capacity',index:'capacity'},
	   		{name:'freeSpace',index:'freeSpace'}
		],
		rowNum: 10,
		altRows:true,
		altclass:'r1',
		forceFit:true,
		pager: '#queryResultPage03',
		sortname: 'creationDate',
		viewrecords: true,
		sortorder: "desc",
		caption:"",
		gridComplete: function() {
			//
		}
	});

});
function loadPage_lv2(url,data){
	$("body").css("cursor","wait");
	$.ajax(url, {
		dataType : "html",
		cache : false,
		data : data,
		success : function(data,txt){
			$('#res_lv2_2').html(data);
		},
		complete : function(xhr,txt){
			$("body").css("cursor","auto");
		}
	});
}
function authPool_lv2(oid){
	$('#res_lv2_1').css("display","none");
	loadPage_lv2("respool/secondCbmsResourcePoolAuthUI.action",{oid:oid});
}
function netPool_lv2(oid){
	$('#res_lv2_1').css("display","none");
	loadPage_lv2("respool/secondCbmsResourcePoolNetUI.action",{oid:oid});
}
function addPool_lv2(){
	$('#res_lv2_1').css("display","none");
	loadPage_lv2("respool/secondAddOrUpdateCbmsResourcePoolUI.action",{clusterId:"${oid}"});
}
function upPool_lv2(oid){
	$('#res_lv2_1').css("display","none");
	loadPage_lv2("respool/secondAddOrUpdateCbmsResourcePoolUI.action",{clusterId:"${oid}" ,oid:oid});
}
function delPool_lv2(oid){
	if(confirm("确定删除该2级资源池?")){
		$("body").css("cursor","wait");
		$.ajax("respool/secondRemoveResourcePool.action", {
			dataType:"json",
			cache:false,
			data:{oid : oid },
			success:function(msg){
				$("body").css("cursor","auto");
				if(msg.resultCode=='success'){
					reLoadLv2();
				}else{
					alert(msg.resultMessage);					
				}
				
			},
			error:function(){
				$("body").css("cursor","auto");
			}
		})
	}	
}
function reLoadLv2(){
	$("#queryResultTable01").jqGrid()
		.setGridParam({page:1,rowNum:10,url : 'respool/firstQueryResourcePool.action'})
		.setGridParam({postData : { oid : "${oid}" } })
		.trigger("reloadGrid");
}
</script>
<div id="inforTabs">
    <ul>
    <li><a href="#baseInfor">基本信息</a></li>
    <li><a href="#poolInfor">资源池信息</a></li>
    <li><a href="#subpoolInfor">二级资源池列表</a></li>
    <li><a href="#hostInfor">主机列表</a></li>
    <li><a href="#storageInfor">数据存储</a></li>
    </ul>
    <div id="baseInfor">
    	<table width="100%" class="layoutTable">
    	<tr><td>虚拟化平台名称：${title}</td><td>资源池级别：一级</td></tr>
    	</table>
    </div>
    <div id="poolInfor">
    	<table width="100%" cellspacing="0" class="layoutTable">
        <tr>
        <td width="33%">
            <h3>CPU资源池(个)</h3>
            <div id="cpuPie" class="pieChart"></div>
            <div class="countData"></div>
        </td>
        <td width="33%">
            <h3>内存资源池(MB)</h3>
            <div id="ramPie" class="pieChart"></div>
            <div class="countData"></div>
        </td>
        <td width="34%">
            <h3>存储资源池(MB)</h3>
            <div id="hdPie" class="pieChart"></div>
            <div class="countData"></div>
        </td>
        </tr>
        <tr>
        <td>
            <h3>服务器资源池(个)</h3>
            <div id="poolPie" class="pieChart"></div>
            <div class="countData"></div>
        </td>
        <td colspan="2">
            <h3>VLAN资源池</h3>
        	<div id="columnChartContainer" class="relaContainer">
            	<div id="columnChartControls"><a class="prev floatl" href="####"></a><a class="next floatr" href="####"></a></div>
                <ul id="columnList">
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                <li><label>---</label><div class="chart"><span class="value">?</span><span class="cap top"></span><span class="cap root"></span></div></li>
                </ul>
                <div id="columnChartPages">---</div>
            </div>
        </td>
        </tr>
        </table>
    </div>
    <div id="subpoolInfor">
    	<div id="res_lv2_1">
	        <table id="queryResultTable01"></table>
	        <div id="queryResultPage01"></div>
	    </div>
        <div id="res_lv2_2"></div>
    </div>
    <div id="hostInfor">
        <table id="queryResultTable02"></table>
        <div id="queryResultPage02"></div>
    </div>
    <div id="storageInfor">
        <table id="queryResultTable03"></table>
        <div id="queryResultPage03"></div>
    </div>
</div>
