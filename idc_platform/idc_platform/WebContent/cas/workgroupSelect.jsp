<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getPurview.jsp"%>

<%
String selectUserId = request.getParameter("selectUserId");
%>
<script>


var wgSelectDS = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: 'workgroupQuery.jsp'
    }),
		baseParams:{type:'forAdd'},
    reader: new Ext.data.JsonReader({
        root: 'records',
        totalProperty: 'totalCount'
    }, [
				{name: 'id', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'parentId', type: 'string'},
				{name: 'status', type: 'string'}
    ])
});
var wgSelectCM = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer(),
		sm,
		{header: "ID", width: 70, sortable: true, dataIndex: 'id'},
    {header: "����", width: 75, sortable: true, dataIndex: 'name'},
    {header: "��ID", width: 75, sortable: true, dataIndex: 'parentId'},
		{header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer:formatStatus}
]);

var wgSelectGrid = new Ext.grid.GridPanel({
	region:'center',
	layout: 'fit',
  ds: wgSelectDS,
  cm: wgSelectCM,
  sm:sm,
  stripeRows: true,
  margins: '1 0 1 1',
  viewConfig: {
		forceFit:true
	},
  listeners: {
  	render: function(){
  		this.store.baseParams.userId='<%=selectUserId%>';
  		this.store.load();
  	}
  }
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '��֯ѡ��',
    width: 400,
    height:400,
    minWidth: 300,
    minHeight: 150,
    layout: 'fit',
    plain:true,
    modal:true,
    bodyStyle:'padding:5px;',
    items: wgSelectGrid,
    buttons:[{
    	text: 'ȷ��',
    	handler: function(){
    		var records = wgSelectGrid.getSelectionModel().getSelections();
    		if (records.length<=0){
    			Ext.MessageBox.alert("��Ϣ","��ѡ��Ҫ��ӵĹ�����");
    			return;
    		}
    		var wgs = "";
    		for (var i = 0; i < records.length; i++){
					if (i>0)
						wgs +=",";
					wgs += records[i].get("id");
    		}
    		postForm({'postType':'addRelations','toId':wgs,'rType':'userWorkgroup','users':'<%=selectUserId%>'},"casAucPost.jsp",wgSelectCallback);
    	}
    },{
    	text: 'ȡ��',
  	 	handler: function(){
  	 		window_dialog.close();
  	 	}
    }]
 });
window_dialog.show();
function wgSelectCallback(){
	wgDS.reload();
	window_dialog.close();
}

</script>