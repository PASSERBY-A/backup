<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

%>
<html>
<head>
  <title>导航列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
</head>

<body>
	<script>

	function addMenu() {
		menuDetail({"id": -1, "action": 'add'});
	}
  function menuDetail(paramList){
		Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/menu/menuAdd.jsp",
		paramList);
  }
 var viewgrid = null;
 
 
	var menuds = new Ext.tree.TreeLoader({
		dataUrl:'<%=Consts.ITSM_HOME%>/configure/menu/menuQuery.jsp',
		uiProviders:{
		   'col': Ext.tree.ColumnNodeUI
		}
  });
var root = new Ext.tree.AsyncTreeNode({
	      text:'Tasks',
	      id:'_'
	  });
  
Ext.onReady(function(){


  var menugrid = new Ext.tree.ColumnTree({
		region:'center',
		margins: '0 1 1 1',
		viewConfig: {
			autoFill: true
		},
    rootVisible:false,
    autoScroll:true,
		width:574,
    columns:[{
			header: "ID",
			width: 180,
			dataIndex: 'id'
		},{
			header: "名称",
			width: 180,
			dataIndex: 'name'
		},{
			header: "地址",
			width: 180,
			dataIndex: 'url'
		},{
			header: "权限",
			width: 180,
			dataIndex: 'rule_str'
		}],
	  loader: menuds,
	  root: root,
	  bbar: [new Ext.Toolbar.Button({
			text: '新增',
			handler: addMenu
		})]
  });

    var viewport = new Ext.Viewport({
			layout:'border',
      items:[menugrid]
	});
	menugrid.on('dblclick', function(node, e) {
			var id = node.attributes['id'];
			menuDetail({"id": id, "action":'edit'});
		}, menugrid);
});
</script>
</body>
</html>