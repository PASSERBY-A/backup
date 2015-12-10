<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.log.*"%>
<%@ include file="getPurview.jsp"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title>登录日志</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>
</head>

<body>

<script defer>

// 生成数据源
var ds = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'loginLogQuery.jsp'
    }),

		baseParams: {'begin':'','end':''},
    // create reader that reads the Topic records
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
        {name: 'userId', mapping: 'userId'},
        {name: 'loginTime', mapping: 'loginTime'},
        {name: 'loginIp', mapping: 'loginIp'},
        {name: 'loginHost', mapping: 'loginHost'}
    ]),
		sortInfo:{field: 'loginTime', direction: 'DESC'}
});


// 生成表格头信息
var cm = new Ext.grid.ColumnModel([{
		header: "登录ID",
		dataIndex: 'userId',
		width: 160,
		sortable: true,
		css: 'white-space:normal;'
	},{
		header: "登录时间",
		sortable: true,
		dataIndex: 'loginTime',
		width: 150
	},{
		header: "登录IP",
		dataIndex: 'loginIp',
		sortable: true,
		width: 100
	},{
		header: "登录主机",
		sortable: true,
		dataIndex: 'loginHost',
		width: 100
  }]
);

Ext.onReady(function() {

	var beginTime = new Ext.form.DateField({name:'begin',format:'Y-m-d',width:90,value:new Date()});
	var endTime = new Ext.form.DateField({name:'end',format:'Y-m-d',width:90,value:new Date()});
	// 生成表格
  grid_fieldList = new Ext.grid.GridPanel({
      ds: ds,
      cm: cm,
      region:'center',
      layout:'fit',
      margins:'1 1 1 1',
			viewConfig: {
				autoFill: true
			},
      selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
      enableColLock:false,
      loadMask: true,
      tbar: ['登录时间:',
      	beginTime,
				'～',
				endTime,
				' ',
				{
					text:'检索',
					handler:function(){
						var begin = beginTime.getEl().dom.value;//ipB
						var end = endTime.getEl().dom.value;//ipE
						ds.baseParams.begin = begin;
						ds.baseParams.end = end;
						ds.reload();
					}
				}
			],
			bbar: new Ext.PagingToolbar({
				store: ds,
				pageSize: 50,
				displayInfo: true,
				displayMsg: "第 {0} - {1} 条，共 {2} 条记录"
			})
    });
  
	var viewport = new Ext.Viewport({
			layout:'border',
      items:[grid_fieldList]
	});

	grid_fieldList.on("rowdblclick", function(grid, rowIndex, e)
		{
			var id = grid.getStore().getAt(rowIndex).get("id");
			var origin = grid.getStore().getAt(rowIndex).get("origin");
			viewField({"id": id,"origin":origin});
		});
    // trigger the data store load
		var begin = beginTime.getEl().dom.value;//ipB
		var end = endTime.getEl().dom.value;//ipE
		ds.baseParams.begin = begin;
		ds.baseParams.end = end;
    ds.load({params:{start:0, limit:50}});

});

</script>

</body>

</html>
