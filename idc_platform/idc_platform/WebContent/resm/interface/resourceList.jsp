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

String id = request.getParameter("id");
boolean mutiModel = "1".equals(request.getParameter("muti"));
if (id == null)
	id = "";
String modelId = request.getParameter("model");
Model model = ServiceManager.getModelService().getModelById(modelId);
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
		url: '/idc_platform/resm/interface/resQuery.jsp'
	}),
	baseParams: { "modelId": "<%=modelId%>", limit: <%=pageCount%> },
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
	<%if (!mutiModel) { %>
	buttons: [ 
		{ text: '确定',
		handler : function() {
				var cell = mainPanel.getSelectionModel().getSelected();
				if (cell == null) {
					Ext.MessageBox.alert("提示", "请选择人员");
					return;
				}
				top.window.returnValue = cell.get("id") + "=" + cell.get("name");
				top.window.close();
			}
		},{ text: '取消',
			handler: function() { top.window.close(); }
		}
		],
	<%}%>
	width:'100%',
	split:true,
	height: 200,
	loadMask: true,
   	view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
        })
});

<%if (mutiModel) { %>
var store2 = new Ext.data.GroupingStore({
	proxy: new Ext.data.HttpProxy({
		url: '/idc_platform/resm/interface/resQuery2.jsp'
	}),
	baseParams: { "modelId": "person", id: "<%=id%>" },
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

var selectedPanel = new Ext.grid.GridPanel({
	region:'south',
	store: store2,
	margins:'1 1 1 1',
	tbar: [
			'已选择人员: '
			],
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
	viewConfig: {
		forceFit: true
	},
	width:'100%',
	split:true,
	height: 200,
		buttons: [ 
		{ text: '确定',
		handler : function() {
				var count = selectedPanel.getStore().getCount();
				if (count == 0) {
					Ext.MessageBox.alert("提示", "请选择人员");
					return;
				}
				var i;
				var ret = "";
				for (i = 0; i < count; i++)
				{
					var r = selectedPanel.getStore().getAt(i);
					if (i > 0)
						ret += ",";
					ret += r.get("id") + "=" + r.get("name");
				}
				top.window.returnValue = ret;
				top.window.close();
			}
		},{ text: '取消',
			handler: function() { top.window.close(); }
		}
		],
	loadMask: true,
   	view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
        })
});
<%}%>


<%if (mutiModel) { %>
mainPanel.on("rowdblclick", function(g, rowIndex, e) {
	var cell = g.getStore().getAt(rowIndex);
	if (store2.indexOf(cell) == -1)
		store2.add([cell]);
	selectedPanel.getSelectionModel().selectRecords([cell]);
});
selectedPanel.on("rowdblclick", function(g, rowIndex, e) {
	store2.removeAt(rowIndex);
});
<% } else { %>
mainPanel.on("rowdblclick", function(g, rowIndex, e) {
	var cell = g.getStore().getAt(rowIndex);
	top.window.returnValue = cell.get("id") + "=" + cell.get("name");
	top.window.close();
});
<% } %>

Ext.onReady(function(){
	var viewport = new Ext.Viewport({
		layout:'border',
		items:[mainPanel
		<%if (mutiModel) { %>
			,selectedPanel
		<%}%>
		]
	});
<%if (mutiModel) { %>
	store2.load();
	sf.setValue('');
<% } else { %>
	sf.setValue('<%=id%>');
<% } %>
	sf.onTrigger2Click();
});

</script>
 </body>
</html>
