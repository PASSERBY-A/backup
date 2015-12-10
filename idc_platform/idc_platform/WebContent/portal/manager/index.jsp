<%@ page language="java" contentType="text/html; charset=gbk"%>
<html> 
<head>
<title>œµÕ≥…Ë÷√</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
	Ext.onReady(function(){
	
		Ext.QuickTips.init();
	
		var rootNode = new Ext.tree.AsyncTreeNode();
		var treeLoader = new Ext.tree.TreeLoader({
            			dataUrl:'menu.jsp'
        				});		
		var viewport = new Ext.Viewport({
			layout: 'border',
			items: [{
				region: 'west',
				xtype: 'treepanel',
				split: true,
				collapsible: true,
				id: 'tree',
				title: '',
				iconCls: 'anchor',
				width: 200,
				minSize: 150,
				margins:'5 0 5 5',
      			root: rootNode,
      			rootVisible:false,
      			loader:treeLoader,
      			useArrows:true,
      			autoScroll:true,
				lines:false
			},{
				region: 'center',
				xtype: 'panel',
				id: 'main_panel',
				border: false,
				margins:'5 5 5 0',
				contentEl:'iframe_content'
			}
			]
		}); 
			
    Ext.getCmp('tree').on('click', function(n,e){
    	if(n.leaf)	{
    		e.stopEvent();
    		displayForm.location.href = n.attributes.href;
    		}
    	});		

    displayForm.location.href = 'workPlan/';  		
});
</script>
</head>
<body>
	<div id="iframe_content">
		<iframe id="displayForm" name="displayForm" src="" width="100%" height="100%"  frameborder=0></iframe>
	</div> 
</body> 
</html>
