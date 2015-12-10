<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.common.Consts"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title>TEST</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=Consts.EXTJS_HOME%>/resources/css/ext-ext.css" />
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/ext-ext.js"></script>
	<script type="text/javascript" src="<%=Consts.EXTJS_HOME%>/source/locale/ext-lang-zh_CN.js"></script>
</head>

<body>

<script defer>
var window_codeView = null;
var currentCatalogOid = 1;

function newCode()
{
	viewCode({"id": -1});
}

function viewCode(paramList)
{
	paramList.type = currentCatalogOid;
	Ext.loadRemoteScript("<%=Consts.ITSM_HOME%>/configure/code/codeEdit.jsp",
		paramList);
}

var codeTypeTree = new Ext.tree.TreePanel({
	title:'代码类型列表',
	region:'west',
	split:true,
  width: 150,
  minSize: 75,
  maxSize: 175,
  animate:false,
	lines:false,
	margins:'1 0 1 1',
  autoScroll:true,
  collapsible: true,
  rootVisible:false
});

var root = new Ext.tree.TreeNode({
	text: '代码类型',
	draggable:false
});

<%
	List modules = CIManager.getCodesByTypeOid(Consts.CODETYPE_MODULE);
	for (int i = 0; i < modules.size(); i++) {
		CodeInfo cc = (CodeInfo)modules.get(i);
		List codeTypes = CIManager.getCodeTypesOfCatalog(cc.getOid());
		if (codeTypes == null || codeTypes.size()<=0)
			continue;
		cc.output("root_" + i, out, "javascript:");
		out.println("root.appendChild(root_" + i + ");");
		
		ItsmUtil.sort(codeTypes, "name", false);
		for (int j = 0; j < codeTypes.size(); j++) {
			CodeTypeInfo ct = (CodeTypeInfo)codeTypes.get(j);
			out.println("root_"+i+".appendChild(new Ext.tree.TreeNode({\n"
				+ "text: '" + ct.getName() + "',\n");
		     out.println("href: 'javascript:loadCode(" + ct.getOid() + ")',\n");
		 out.println("draggable:false" + "}));\n");
		}
	}
%>
	
codeTypeTree.setRootNode(root);

var treeLoader = new Ext.tree.TreeLoader({
    dataUrl:'<%=Consts.ITSM_HOME%>/configure/code/codeQuery.jsp',
    baseParams:{type:currentCatalogOid},
    uiProviders:{
        'col': Ext.tree.ColumnNodeUI
    }
});

var codeTree = new Ext.tree.ColumnTree({
	border:false,
	viewConfig: {
		autoFill: true
	},
  rootVisible:false,
  autoScroll:true,
	width:574,
	columns:[{
	   header: "名称",
	   dataIndex: 'codeName',
	   width: 150
	},{
	   header: "描述",
	   dataIndex: 'codeDesc',
	   width: 200
	},{
	   header: "OID",
	   dataIndex: 'codeOId',
	   width: 50,
	   css: 'white-space:normal;'
	},{
	   header: "ID",
	   dataIndex: 'codeId',
	   width: 80,
	   css: 'white-space:normal;'
	},{
	   header: "排序",
	   dataIndex: 'codeOrder',
	   width: 50
	},{
	   header: "启用",
	   dataIndex: 'enabled',
	   width: 50
	},{
	   header: "可点",
	   dataIndex: 'clickable',
	   width: 50
	}],
	loader: treeLoader,
	bbar: [new Ext.Toolbar.Button({
		text: '新增',
		handler: newCode
	})]

  
});
var codeRoot = new Ext.tree.AsyncTreeNode({
      text:'Tasks'
  });
codeTree.setRootNode(codeRoot);

Ext.onReady(function() {

   // tree.render();
	var viewport = new Ext.Viewport({
			layout:'border',
      items:[codeTypeTree,{
            region:'center',
            split:true,
            width: 225,
            minSize: 175,
            maxSize: 400,
            layout:'fit',
            margins:'1 1 1 1',
            items: codeTree
			}]
	});
	codeTree.on('dblclick', function(node, e) {
			var id = node.attributes['codeOId'];
			viewCode({"id": id});
		}, codeTree);
});

function loadCode(catalogOid)
{
	currentCatalogOid = catalogOid;
	treeLoader.baseParams.type=currentCatalogOid;
	treeLoader.load(codeRoot,function(){});
}

</script>

</body>

</html>
