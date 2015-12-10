<%@ page language="java" pageEncoding="GBK"%>
<%@ page import="com.hp.idc.common.Const"%>
<html>
<head>
<title>IDC业务管理</title>
</head>

<%@ include file="/common/inc/header.jsp"%>

<script type="text/javascript">
	var nodeText = "";
	Ext.onReady(function() {
		Ext.QuickTips.init();
		
		var root=new Ext.tree.AsyncTreeNode({  
           	id:"root",  
           	text:"业务管理",  
           	leaf:false,  
           	children:[
           	{  
               	id: 1,  
               	text: '产品目录管理',  
               	leaf: true,
               	href: 'catalog_list.jsp'
           	},{  
               	id: 2,  
               	text: '产品管理',  
              	leaf: true ,
               	href: 'product_list.jsp'
           	},{
           		id: 3,  
               	text: '服务管理',  
               	leaf: true ,
               	href: 'service_list.jsp'
           	}  
           	]  
        });  
		var viewport = new Ext.Viewport({
			layout: 'border',
			items: [{
				region: 'west',
				xtype: 'treepanel',
				title:'导航栏',
				split: true,
				collapsible: true,
				id: 'tree',
				iconCls: 'anchor',
				width: 200,
				minSize: 150,
				margins:'5 0 5 5',
      			root: root,
      			rootVisible:true,
      			loader:new Ext.tree.TreeLoader(),
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
        		if(e)
    				e.stopEvent();
    			displayForm.location.href = n.attributes.href;
    			nodeText = n.text;
    		}
    	});

		//defalut click the first child node
		root.expand(true,true,function(node){
			Ext.getCmp('tree').fireEvent('click',node.firstChild);
		});

	});
</script>

<body>
	<div id="iframe_content">
		<iframe id="displayForm" name="displayForm" src="" width="100%" height="100%"  frameborder=0></iframe>
	</div> 
</body> 
</html>