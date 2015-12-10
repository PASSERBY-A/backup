<%@page contentType="text/html; charset=gb2312" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
 	<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
  <script type="text/javascript" src="/extjs/ext-all.js"></script>

</head>
<body>
<script type="text/javascript">
	
	var mainPanel = new Ext.Panel({

		id:'view_1',
		region:'center',
		margins:'1 0 0 0',
		contentEl:'iframe_content'
	});
	  
	var westPanel = new Ext.tree.TreePanel({
		id:'navigation',
		region:'west',
		title:'测试列表',
		split:true,
		width: 200,
		minSize: 175,
		maxSize: 400,
		collapsible: true,
		margins:'1 0 0 0',
		rootVisible:false,
		lines:false,
		autoScroll:true,
        loader: new Ext.tree.TreeLoader({
            dataUrl:'navigate.jsp'
        }),
		root: new Ext.tree.AsyncTreeNode('Naviga'),
		collapseFirst:false
	});
	



function addTab(tabId,name,href) {
	mainPanel.setTitle(name);
	mainPanel.id = tabId;
	displayForm.location.href = href;
};
     
Ext.onReady(function(){
	var viewport = new Ext.Viewport({
		layout:'border',
		items:[westPanel,mainPanel]
	});

	document.getElementById("displayForm").height = mainPanel.body.dom.clientHeight;

//	westPanel.selectPath(westPanel.getNodeById("view_1").getPath());
	westPanel.on('click', function(node, e){
		if(node.attributes.href){
			e.stopEvent();
			addTab(node.id,node.text,node.attributes.href);
		}
	});

	var fistLoad = false;
	westPanel.on('load', function(node, e){
		if (fistLoad)
			return;
		westPanel.getRootNode().expand();
		westPanel.getRootNode().expandChildNodes();
		for (var i = 0; i < westPanel.getRootNode().childNodes.length; i++)
			westPanel.getRootNode().childNodes[i].expandChildNodes();
	});
});
    
</script>
	<div id="iframe_content">
		<iframe id="displayForm" name="displayForm" src="" width="100%" height="100%"  frameborder=0></iframe>
	</div>
 </body>
</html>
