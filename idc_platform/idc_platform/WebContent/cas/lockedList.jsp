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
</head>
<style>
	.x-tree-selected {
    border:1px dotted #a3bae9;
    background:#DFE8F6;
}
.x-tree-node .x-tree-selected a span{
	background:transparent;
	color:#15428b;
    font-weight:bold;
}
</style>
<body>
<script>

var userDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'lockedUserQuery.jsp'
    }),
		baseParams:{type:'all',includeAll:'true'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount',
        id:'id',
        fields:[
						{name: 'id', type: 'string'},
						{name: 'name', type: 'string'},
						{name: 'role', type: 'string'},
						{name: 'mobile', type: 'string'},
						{name: 'email', type: 'string'},
						{name: 'p_status', type: 'string'},
						{name: 'status', type: 'string'},
						{name: 'department', type: 'string'}
		    ]
    }),
    sortInfo:{field: 'id', direction: 'ASC'}
});

var userCM = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
    {header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
    {header: "ְ��", width: 75, sortable: true, dataIndex: 'role'},
    {header: "�ֻ���", width: 75, sortable: true, dataIndex: 'mobile'},
    {header: "E-MAIL", width: 75, sortable: true, dataIndex: 'email'},
    {header: "��Ա״̬", width: 85, sortable: true, dataIndex: 'p_status',renderer:formatPersonStatus},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
]);

var userGrid = new Ext.grid.GridPanel({
  ds: userDS,
  cm: userCM,
  loadMask: true,
  viewConfig: {
		forceFit:true
	},
	tbar:[
		{
		text:"����",
		handler:function(){
			var row = userGrid.getSelectionModel().getSelected();
			if (!row){
				Ext.MessageBox.alert("��Ϣ","��ѡ��һ����Ա��");
				return;
			}
			if (confirm("ȷ��Ҫ��"+row.get("id")+"������")){
				postForm({'postType':'user','id':row.get("id"),'subType':'unlock'},"casAucPost.jsp",userCallback);
			}
		}
	},'-', '���û�ID����: ',
	new Ext.form.SearchField({
		paramName:'filter',
		store: userDS,
		width:320
	})],
	bbar: new Ext.PagingToolbar({
		store: userDS,
		displayInfo: true,
		pageSize: 25,
		displayMsg: "��ǰ��¼�� {0} - {1}����һ��{2}��",
		emptyMsg: "û�м�¼",
		items:['-']
	})
});

function userCallback(){
}

Ext.onReady(function(){
	new Ext.Viewport({
		layout: 'border',
		items: [{
      region:'center',
      split:true,
      width: 225,
      minSize: 175,
      bodyBorder: false,
			border:false,
      maxSize: 400,
      layout:'fit',
      margins:'1 1 1 1',
      items: userGrid
		}]
  });
  userDS.load({params:{start:0, limit:25}});
});

</script>
</body>
</html>