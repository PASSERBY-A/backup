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

String modelIds = request.getParameter("models");
String[] ids = modelIds.split(";");
String filter = request.getParameter("filter");
if(filter == null){
	filter = "";
} else {
	filter = new String(filter.getBytes("iso-8859-1"), "UTF-8");
}
System.out.println(filter);
List<AttributeDefine> headers = new ArrayList<AttributeDefine>();

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

var treePanel = new Ext.tree.TreePanel( {
	id : 'tree-panel',
	title : '����',
	region : 'west',
	split : true,
	width:200,
	minSize: 175,
	maxSize: 400,
	autoScroll : true,
	collapsible: true,
	rootVisible : false,
	root: new Ext.tree.AsyncTreeNode({
        text:'Ext JS',
        id:'root',
        expanded:true,
        children:[<%
            Model model = null;
			for(int i=0;i<ids.length;i++){
				model = ServiceManager.getModelService().getModelById(ids[i]);
				if(model == null)
					continue;
				if(i>0)
					out.write(",");
				out.write("{id:'"+ids[i]+"',text:'"+model.getName()+"',leaf:true}");
			}
			if(model != null)
				headers = model.getHeader();
        %>]
     }),
	lines : false,
	singleExpand : true,
	expanded : true,
	useArrows : true
});

treePanel.on('click', function(node){
	store.baseParams['modelId'] = node.id;
	store.baseParams['filter'] = '<%= filter %>';
	store.load();
});

var store = new Ext.data.GroupingStore({
	proxy: new Ext.data.HttpProxy({
		url: 'resQuery1.jsp'
	}),
	baseParams: { limit: <%=pageCount%>, oper:'0'},
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
			displayMsg: '�� {0} - {1} ������ {2} ����¼',
			emptyMsg: "�޼�¼",
			//plugins: filters,
			items: ['-']
		});

var mainPanel;
var sf = new Ext.form.SearchField({
		paramName:'filter',
		store: store,
		emptyText : '<%= model == null?"":model.createObject().getMatchDescription()%>',
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
			'ģ������: ', sf
			],
	bbar: pt,
	viewConfig: {
		forceFit: true
	},
	<%if (!mutiModel) { %>
	buttons: [ 
		{ text: 'ȷ��',
		handler : function() {
				var cell = mainPanel.getSelectionModel().getSelected();
				if (cell == null) {
					Ext.MessageBox.alert("��ʾ", "δѡ���κ���Դ,��ѡ��!");
					return;
				}

				//top.window.returnValue = cell.get("id") + "=" + cell.get("name");
				var data = Ext.encode(cell.data);
				data = data.substring(1,data.length-1); //ȥ�����ߵĴ�����, �Է�������grid��, ����appendvalue
				top.window.returnValue = data;
				top.window.close();
				
			}
		},{ text: 'ȡ��',
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
		url: 'resQuery2.jsp'
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
			'��ѡ����Ա: '
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
		{ text: 'ȷ��',
		handler : function() {
				var count = selectedPanel.getStore().getCount();
				if (count == 0) {
					Ext.MessageBox.alert("��ʾ", "δѡ���κ���Դ,��ѡ��!");
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
				//top.window.returnValue = ret;
				top.window.returnValue = Ext.encode(cell.data);
				top.window.close();
			}
		},{ text: 'ȡ��',
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
	//top.window.returnValue = cell.get("id") + "=" + cell.get("name");
	var data = Ext.encode(cell.data);
	data = data.substring(1,data.length-1); //ȥ�����ߵĴ�����, �Է�������grid��, ����appendvalue
	top.window.returnValue = data;
	top.window.close();
});
<% } %>

Ext.onReady(function(){
	var viewport = new Ext.Viewport({
		layout:'border',
		items:[treePanel, mainPanel
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
