<%@page contentType="text/html; charset=gb2312" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%@ page import="com.hp.idc.resm.ui.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
int pageCount = 20;

String customerId = request.getParameter("customerId");
String orderNo = request.getParameter("orderNo");
Model model = ServiceManager.getModelService().getModelById("device");
List<AttributeDefine> headers = model.getHeader();
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
	<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/extjs/ext-all.js"></script>
	<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="/extjs/ext-ext.js"></script>
	<script type="text/javascript" src="/cmdb/js/cmdb.js"></script>
</head>
<body>
<script type="text/javascript">

var store = new Ext.data.GroupingStore({
	proxy: new Ext.data.HttpProxy({
		url: '/idc_platform/resm/interface/resQueryOfCustomer.jsp'
	}),
	baseParams: { "customerId": "<%=customerId%>", "orderNo":"<%= orderNo %>",limit: <%=pageCount%> },
	autoLoad:true,
	reader: new Ext.data.JsonReader({
		root: 'items',
		totalProperty: 'totalCount',
		id: 'id'
	}, [
<%
	for (int i = 0; i < headers.size(); i++)
	{
		if (i > 0)
			out.print(",");
		out.print("{name: \"" + headers.get(i).getId() + "\"}");
	}
%>	
	]),
	sortInfo:{field: 'name', direction: 'ASC'},

	remoteSort: true
});

var pt = new Ext.PagingToolbar({
			pageSize: <%=pageCount%>,
			store: store,
			displayInfo: true,
			displayMsg: '第 {0} - {1} 条，共 {2} 条记录',
			emptyMsg: "无记录",
			//plugins: filters,
			items: ['-']
		});

var mainPanel;
var sf = new Ext.form.SearchField({
		paramName:'filter',
		store: store,
		emptyText : '<%=model.createObject().getMatchDescription()%>',
		width: 560
	});
	
mainPanel = new Ext.grid.GridPanel({
	region:'center',
	store: store,
	margins:'1 1 1 1',
	columns: [
<%
	for (int i = 0; i < headers.size(); i++)
	{
		if (i > 0)
			out.print(",");
		out.print("{header: \"" + headers.get(i).getName() + "\", dataIndex: '" + headers.get(i).getId()
			+ "', sortable: true}");
	}
%>	
	],
	sm: new Ext.grid.RowSelectionModel({
			singleSelect: true
		}),
	tbar: [
			'模糊查找: ', sf
			],
	bbar: pt,
	viewConfig: {
		forceFit: true
	},
	width:'100%',
	split:true,
	height: 200,
	loadMask: true,
   	view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
        })
});

mainPanel.on("rowdblclick", function(g, rowIndex, e) {
	var cell = g.getStore().getAt(rowIndex);
	//top.window.returnValue = cell.get("id") + "=" + cell.get("name");
	var data = Ext.encode(cell.data);
	data = data.substring(1,data.length-1); //去除两边的大括号, 以方便与在grid中, 重新appendvalue
	top.window.returnValue = data;
	top.window.close();
});

Ext.onReady(function(){
	var viewport = new Ext.Viewport({
		layout:'border',
		items:[mainPanel]
	});
});

</script>
 </body>
</html>
