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

var orgUserGrid = new Ext.grid.GridPanel({
	title: '��������Ա',
	region:'center',
	xtype: 'grid',
	layout: 'fit',
	height: 300,
  minSize: 100,
  maxSize: 300,
  split: true,
  margins: '1 1 1 0',
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
		displayMsg: "��ǰ��¼�� {0} - {1}����һ��{2}��",
		emptyMsg: "û�м�¼",
		items:['-']
	}),
	tbar:[{
			id:'addUser',
			text:'����',
			handler:function(){
				var selectRecord = treePanel.getSelectionModel().getSelectedNode();
				if (!selectRecord){
					Ext.MessageBox.alert("��Ϣ","��ѡ��һ����֯��");
    				return;
				}
				else if(selectRecord.attributes.text.indexOf("<del>")>-1){
					Ext.MessageBox.alert("��Ϣ","����֯�Ѿ������ã�");
					return;
				}
				Ext.loadRemoteScript("userSelect.jsp?mutiSelect=true&type=addToOrganization&rType=userOrganization&toId="+selectRecord.id);
			}
		},'-'
		,{
			id:'delUser',
			text:'ɾ��',
			handler:function(){
				var selectRecord = treePanel.getSelectionModel().getSelectedNode();
				if (!selectRecord){
					Ext.MessageBox.alert("��Ϣ","��ѡ��һ����֯��");
    				return;
				}
				else if(selectRecord.attributes.text.indexOf("<del>")>-1){
					Ext.MessageBox.alert("��Ϣ","����֯�Ѿ������ã�");
					return;
				}
				var userSelRecord =orgUserGrid.getSelectionModel().getSelected();
				if (!userSelRecord){
					Ext.MessageBox.alert("��Ϣ","ѡ��Ҫ������ɾ������Ա");
    				return;
				}
				
				if (confirm("ȷ��Ҫ��"+userSelRecord.get("id")+"����("+selectRecord.id+")��ɾ����")){
					postForm({'postType':'deleteRelations','toId':selectRecord.id,'rType':'userOrganization','user':userSelRecord.get("id")},"casAucPost.jsp");
				}
			}
		}
	]
});
function postCallback(){
	userDS.reload();
}

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
	title: '������ְ��',
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
	    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
	    {header: "���ȵȼ�", width: 75, sortable: true, dataIndex: 'level'},
	    {header: "״̬", width: 75, sortable: true, dataIndex: 'status'}
	]),
  stripeRows: true,
  loadMask:true,
  viewConfig: {
		forceFit:true
	},
	tbar:[{
			text:'����',
			handler:function(){
				var selectRecord = treePanel.getSelectionModel().getSelectedNode();
				if (!selectRecord){
					Ext.MessageBox.alert("��Ϣ","��ѡ��һ����֯��");
    			return;
				}
				Ext.loadRemoteScript("roleEdit.jsp?subType=add&moId="+selectRecord.id);
			}
		},'-',{
			text:'�༭',
			handler:function(){
				var moId = treePanel.getSelectionModel().getSelectedNode();
				if (!moId){
					Ext.MessageBox.alert("��Ϣ","��ѡ��һ����֯��");
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
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����֯��");
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
  	dataUrl:'organizationTreeQuery.jsp'
  }),
  tbar:[{
		text:'����',
		handler:function(){
			var orgNode = treePanel.getSelectionModel().getSelectedNode();
			var org = '-1';
			if (orgNode&&orgNode.attributes.text.indexOf("<del>")<0)
				org = orgNode.id;
			Ext.loadRemoteScript("organizationEdit.jsp?subType=add&parentId="+org);
		}
	},'-',{
		text:'�༭',
		handler:function(){
			var org = treePanel.getSelectionModel().getSelectedNode().id;
			Ext.loadRemoteScript("organizationEdit.jsp?subType=modify&orgId="+org);
		}
	}]
});
treePanel.loader.load(treePanel.root);
treePanel.on("click",function (node,e){
	userDS.baseParams.type="org";
	userDS.baseParams.orgId=node.id;
	userDS.reload();
	roleDs.baseParams.moId=node.id;
	roleDs.reload();
})

Ext.onReady(function(){
	new Ext.Viewport({
		layout: 'border',
		items: [treePanel,
		new Ext.Panel({
				layout: 'border',
				border:false,
				region:'center',
				//items:[roleGrid,orgUserGrid]
				items:[orgUserGrid]
			})
			]
  });
});
</script>
</body>
</html>