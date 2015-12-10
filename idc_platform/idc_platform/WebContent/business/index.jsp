<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getUserId.jsp"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link rel="stylesheet" type="text/css"
	href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
<script type="text/javascript"
	src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

<link rel="stylesheet" type="text/css" href="layout-browser.css">

</head>
<body>
<div id="iframe_content"><iframe id="displayForm"
	name="displayForm" src="" width="100%" height="100%"
	frameborder=0></iframe></div>
<script>
	Ext.onReady(function() {

		// This is an inner body element within the Details panel created to provide a "slide in" effect
			// on the panel body without affecting the body's box itself.  This element is created on
			// initial use and cached in this var for subsequent access.
			var detailEl = null;
			var contentPanel = new Ext.Panel( {
				region : 'center',
				title : '产品列表',
				bodyBorder : false,
				margins : '1 0 0 0',
				layout : 'fit',
				contentEl : 'iframe_content'
			});

			// Go ahead and create the TreePanel now so that we can use it below
			var treePanel = new Ext.tree.TreePanel( {
				id : 'tree-panel',
				title : '导航',
				region : 'west',
				split : true,
				width:200,
				minSize: 175,
				maxSize: 400,
				autoScroll : true,
				collapsible: true,
				rootVisible : false,
				lines : false,
				singleExpand : true,
				useArrows : true
			});

			var root = new Ext.tree.TreeNode('业务管理');
			treePanel.setRootNode(root);

			var child = new Ext.tree.TreeNode( {
				id : 'product',
				text : '产品信息',
				leaf : true,
				singleClickExpand : true,
				href : "url"
			});
			treePanel.getRootNode().appendChild(child);
			child = new Ext.tree.TreeNode( {
				id : 'alarm_manager',
				text : '服务信息',
				leaf : true,
				singleClickExpand : true,
				href : "url"
			});
			treePanel.getRootNode().appendChild(child);

			// Assign the changeLayout function to be called on tree node click.
			treePanel.on('click', function(n, e) {
				if (n.leaf) {
					e.stopEvent();
					contentPanel.setTitle(n.text);
					displayForm.location.href = n.attributes.href;
				}
			});

			// Finally, build the main layout once all the pieces are ready.  This is also a good
			// example of putting together a full-screen BorderLayout within a Viewport.
			new Ext.Viewport( {
				layout : 'border',
				title : 'Ext Layout Browser',
				items : [ treePanel, contentPanel ],
				renderTo : Ext.getBody()
			});
		});
</script>
</body>
</html>