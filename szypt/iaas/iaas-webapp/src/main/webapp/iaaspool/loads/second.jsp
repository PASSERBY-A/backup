<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript">
var chartDatas = {};
//
$(function() {
	$('#inforTabs').tabs({selected:1});
	//
	$.ajax("respool/secondPoolResource.action", {
		dataType:"json",
		cache:false,
		data:{oid : "${oid}"},
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
				"vlan":{"total":0,"page":1,"pageSize":8,"data":[],"allData":[]}
			};
			if(data[1]!=null){
				chartDatas.vlan.total = Math.ceil(data[1].length/chartDatas.vlan.pageSize);
				for(i=0;i<data[1].length;i++){
					chartDatas.vlan.allData.push(data[1][i]);
				}
				chartDatas.vlan.data = chartDatas.vlan.allData.slice(0,chartDatas.vlan.pageSize);
			}
			initPieCart('cpuPie',chartDatas.cpu.data);
			initPieCart('ramPie',chartDatas.ram.data);
			initPieCart('hdPie',chartDatas.hd.data);
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
		url:'respool/secondQueryServiceInstance.action',
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
		colNames:['id','三级资源池名称','域','性能配置'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'domain',index:'domain'},
	   		{name:'perforConfig',index:'perforConfig'}
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
		url:'respool/secondQueryIpPool.action',
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
		colNames:['id','IP池名称','vlanID','网关','掩码','描述'],
		colModel:[
	   		{name:'id',index:'id',hidden: true },
	   		{name:'name',index:'name'},
	   		{name:'vlanID',index:'vlanID'},
			{name:'gateway',index:'gateway'},
			{name:'mask',index:'mask'},
	   		{name:'description',index:'description'}
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

});
</script>
<div id="inforTabs">
    <ul>
    <li><a href="#baseInfor">基本信息</a></li>
    <li><a href="#poolInfor">资源池信息</a></li>
    <li><a href="#subpoolInfor">三级资源池</a></li>
    <li><a href="#ippoolInfor">IP资源池信息</a></li>
    </ul>
    <div id="baseInfor">
    	<table width="100%" class="layoutTable">
        <tr>
        <td>资源池名称：${cbmsResourcePool.name}</td>
        <td>资源池级别：二级</td>
        </tr>
        <tr>
        <td colspan="3">资源池说明：${cbmsResourcePool.description}</td>
        </tr>
        <tr>
        <td colspan="3">管理员列表：<c:forEach items="${resPoolAccessList}" var="item" varStatus="s">
				${item}<c:if test="${!s.last}">,</c:if>
	            </c:forEach>
        </td>
        </tr>
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
        <td colspan="3">
        	<div class="wrapper">
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
            </div>
        </td>
        </tr>
        </table>
    </div>
    <div id="subpoolInfor">
        <table id="queryResultTable01"></table>
        <div id="queryResultPage01"></div>
    </div>
    <div id="ippoolInfor">
        <table id="queryResultTable02"></table>
        <div id="queryResultPage02"></div>
    </div>
</div>
