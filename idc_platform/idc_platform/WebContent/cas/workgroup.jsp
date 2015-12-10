<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getPurview.jsp"%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="<%=EXTJS_HOME%>/resources/css/ext-all.css" />
	<script type="text/javascript" src="<%=EXTJS_HOME%>/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-all.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=EXTJS_HOME%>/ext-ext.js"></script>

	<link rel="stylesheet" type="text/css" href="layout-browser.css">
	<script type="text/javascript" src="cas_auc.js"></script>

</head>
<body>
<script>

var wgUserGrid = new Ext.grid.GridPanel({
	title: '关联的人员',
	region:'center',
	layout: 'fit',
	height: 300,
  minSize: 100,
  maxSize: 300,
  split: true,
  margins: '1 1 1 1',
  ds: userDS,
  cm: userCM,
  sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
  stripeRows: true,
  loadMask:true,
  viewConfig: {
		forceFit:true
	},
	bbar: new Ext.PagingToolbar({
		store: userDS,
		displayInfo: true,
		pageSize: 25,
		displayMsg: "当前记录第 {0} - {1}条，一共{2}条",
		emptyMsg: "没有记录",
		items:['-']
	}),
	tbar:[{
			text:'增加',
			handler:function(){
				var selectRecord = treePanel.getSelectionModel().getSelectedNode();
				if (!selectRecord){
					Ext.MessageBox.alert("信息","先选择一个工作组！");
    				return;
				}
				else if(selectRecord.attributes.text.indexOf("<del>")>-1){
					Ext.MessageBox.alert("信息","该工作组已经被禁用！");
					return;
				}
				Ext.loadRemoteScript("userSelect.jsp?mutiSelect=true&type=addToWorkgroup&rType=userWorkgroup&toId="+selectRecord.id);
			}
		},'-',{
			text:'删除',
			handler:function(){
				var selectRecord = treePanel.getSelectionModel().getSelectedNode();
				if (!selectRecord){
					Ext.MessageBox.alert("信息","先选择一个工作组！");
    				return;
				}
				else if(selectRecord.attributes.text.indexOf("<del>")>-1){
					Ext.MessageBox.alert("信息","该工作组已经被禁用！");
					return;
				}
				var userSelRecord =wgUserGrid.getSelectionModel().getSelected();
				if (!userSelRecord){
					Ext.MessageBox.alert("信息","选择要从组中删除的人员");
    			return;
				}
				if (confirm("确定要把"+userSelRecord.get("id")+"从组("+selectRecord.id+")中删除吗？")){
					postForm({'postType':'deleteRelations','toId':selectRecord.id,'rType':'userWorkgroup','user':userSelRecord.get("id")},"casAucPost.jsp");
				}
			}
		}
	]
});


var roleDs = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'roleQuery.jsp'
    }),
		baseParams:{type:'all',moId:''},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
   			{name: 'id', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'level', type: 'string'},
				{name: 'status', type: 'string'}
    ])
	});
var roleGrid = new Ext.grid.GridPanel({
	title: '关联的职务',
	region:'south',
	xtype: 'grid',
	layout: 'fit',
	height: 300,
  minSize: 100,
  maxSize: 300,
  split: true,
  margins: '1 1 1 0',
  ds: roleDs,
  cm: new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(),
	    {header: "ID", width: 70, sortable: true, dataIndex: 'id'},
	    {header: "名称", width: 75, sortable: true, dataIndex: 'name'},
	    {header: "优先等级", width: 75, sortable: true, dataIndex: 'level'},
	    {header: "状态", width: 75, sortable: true, dataIndex: 'status'}
	]),
  stripeRows: true,
  loadMask:true,
  viewConfig: {
		forceFit:true
	},
	tbar:[{
			text:'增加',
			handler:function(){
				var selectRecord = treePanel.getSelectionModel().getSelectedNode();
				if (!selectRecord){
					Ext.MessageBox.alert("信息","先选择一个组织！");
    			return;
				}
				Ext.loadRemoteScript("roleEdit.jsp?subType=add&moId="+selectRecord.id);
			}
		},'-',{
			text:'编辑',
			handler:function(){
				var moId = treePanel.getSelectionModel().getSelectedNode();
				if (!moId){
					Ext.MessageBox.alert("信息","先选择一个组织！");
	  			return;
				}
	  		var roleId = this.store.getAt(rowIndex).get("id");
	  		Ext.loadRemoteScript("roleEdit.jsp?subType=modify&roleId="+roleId+"&moId="+moId.id);
	  	}
		}
	],listeners: {
		rowdblclick:function(g,rowIndex,e){
			var moId = treePanel.getSelectionModel().getSelectedNode();
			if (!moId){
				Ext.MessageBox.alert("信息","先选择一个组织！");
  			return;
			}
  		var roleId = this.store.getAt(rowIndex).get("id");
  		Ext.loadRemoteScript("roleEdit.jsp?subType=modify&roleId="+roleId+"&moId="+moId.id);
  	}
	}
});

var treePanel = new Ext.tree.TreePanel({
  region:'west',
  stripeRows: true,
  split: true,
  width: 200,
  minSize: 150,
  autoScroll: true,
  // tree-specific configs:
  margins: '1 0 1 1',
  rootVisible: false,
  lines: false,
  singleExpand: true,
  useArrows: true,
  root:new Ext.tree.TreeNode({id:'-1'}),
  loader:new Ext.tree.TreeLoader({
  	baseParams:{'includeAll':true},
  	dataUrl:'workgroupTreeQuery.jsp'
  }),
  tbar:[{
		text:'新增',
		handler:function(){
			var orgNode = treePanel.getSelectionModel().getSelectedNode();
			var org = '-1';
			if (orgNode&&orgNode.attributes.text.indexOf("<del>")<0)
				org = orgNode.id;
			Ext.loadRemoteScript("workgroupEdit.jsp?subType=add&parentId="+org);
		}
	},'-',{
		text:'编辑',
		handler:function(){
			var org = treePanel.getSelectionModel().getSelectedNode().id;
			Ext.loadRemoteScript("workgroupEdit.jsp?subType=modify&wgId="+org);
		}
	}]
});
treePanel.loader.load(treePanel.root);
treePanel.on("click",function (node,e){
	userDS.baseParams.type="wog";
	userDS.baseParams.wogId=node.id;
	userDS.reload();
	roleDs.baseParams.moId=node.id;
	roleDs.reload();
});


function postCallback(){
	userDS.reload();
}
Ext.onReady(function(){
	new Ext.Viewport({
		layout: 'border',
		items: [treePanel,
			new Ext.Panel({
				layout: 'border',
				border:false,
				region:'center',
				//items:[roleGrid,wgUserGrid]
				items:[wgUserGrid]
			})
		]
  });
});
</script>
</body>
</html>