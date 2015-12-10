<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getPurview.jsp"%>

<%
String selectUserId = request.getParameter("selectUserId");
%>
<script>
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
	region:'south',
	xtype: 'grid',
	layout: 'fit',
	height: 100,
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
	}
});
var treePanel = new Ext.tree.TreePanel({
  region:'center',
  stripeRows: true,
  split: true,
  width: 200,
  minSize: 150,
  autoScroll: true,
  // tree-specific configs:
  margins: '1 0 1 1',
  rootVisible: false,
  lines: true,
  singleExpand: true,
  root:new Ext.tree.TreeNode({id:'-1'}),
  loader:new Ext.tree.TreeLoader({
  	dataUrl:'organizationTreeQuery.jsp'
  })
});
treePanel.loader.load(treePanel.root);
treePanel.on("click",function (node,e){
	roleDs.baseParams.moId=node.id;
	roleDs.reload();
})


var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '组织选择',
    width: 400,
    height:400,
    minWidth: 300,
    minHeight: 150,
    layout: 'border',
    plain:true,
    modal:true,
    bodyStyle:'padding:5px;',
    items: [treePanel,roleGrid],
    buttons:[{
    	text: '确定',
    	handler: function(){
    		var records = treePanel.getSelectionModel().getSelectedNode();
    		if (!records){
    			Ext.MessageBox.alert("信息","请选择一个组织");
    			return;
    		}
    		var orgs = records.id;
    		var row = roleGrid.getSelectionModel().getSelected();
    		var roleId = "";
    		if (row)
    			roleId = row.get("id");
    		if(orgDS.getCount()>0){
    			if (confirm("确定要变换组织吗？"))
    				postForm({'postType':'addRelations','toId':orgs,'rType':'userOrgaUpdate','users':'<%=selectUserId%>','roleId':roleId},"casAucPost.jsp",orgSelectCallback);
    		} else
    				postForm({'postType':'addRelations','toId':orgs,'rType':'userOrganization','users':'<%=selectUserId%>','roleId':roleId},"casAucPost.jsp",orgSelectCallback);
    	}
    },{
    	text: '取消',
  	 	handler: function(){
  	 		window_dialog.close();
  	 	}
    }]
 });
window_dialog.show();
function orgSelectCallback(){
	orgDS.reload();
	userDS.reload();
	window_dialog.close();
}

</script>