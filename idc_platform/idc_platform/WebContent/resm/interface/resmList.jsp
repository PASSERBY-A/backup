<%@ page contentType="text/html; charset=GBK" language="java"%>
<html>
<head>
<title>接口示例代码</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
</head>

<body>
<script type="text/javascript">

var view = new Ext.tree.TreePanel({
	region:'west',
	id:'modelTree',
	animate:false,
	title:'导航栏',
	collapsible:true,
	height : document.body.clientHeight,
	width:200,
	autoScroll: true,
	rootVisible: false,
	loader: new Ext.tree.TreeLoader({
		baseParams: {  },
		dataUrl:'model.jsp',
		listeners: {
			load: function(l,n,r){
				view.fireEvent('click',view.getRootNode().firstChild);
			}
		}
	}),
	root:new Ext.tree.AsyncTreeNode({
		text: 'Ext JS',
		id:'_',
		draggable:false
	}),
	containerScroll: true
});

view.on('click',function(node,e){
	window.frames['centerFrm'].location = 'resourceList1.jsp?model='+node.attributes.id;
});

Ext.onReady(function(){

    Ext.QuickTips.init();

	new Ext.Viewport({
		layout: 'border',
		items: [view,{
			region:'center',
			border:true,
			xtype:'panel',
			title:'资源信息查询',
			html:'<IFRAME width="100%" height="100%" frameborder="0" name="centerFrm"></IFRAME>'
		}]
  	});
});

</script>
</body>
</html>