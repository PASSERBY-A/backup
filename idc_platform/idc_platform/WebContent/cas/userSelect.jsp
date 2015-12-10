<%@ page language="java" pageEncoding="gbk"%>
<%@ include file="getPurview.jsp"%>
<%
String rType = request.getParameter("rType");
String mutiSelect = request.getParameter("mutiSelect");
if (rType == null)
	rType = "";
String type = request.getParameter("type");
if (type == null)
	type = "";

String toId = request.getParameter("toId");
%>
<script>

Ext.override(Ext.form.TwinTriggerField, {
	initComponent : function(){
        Ext.form.TwinTriggerField.superclass.initComponent.call(this);

        this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class}
        ]};
    }
});

var userSelectGrid = new Ext.grid.GridPanel({
	region:'center',
	layout: 'fit',
  ds: userMutiDS,
  cm: userMutiCM,
<%if ("true".equals(mutiSelect)){%>
  sm: sm,
<%}%>
  stripeRows: true,
  margins: '1 0 1 1',
  viewConfig: {
		forceFit:true
	},
	tbar:['按用户ID过滤: ',
		new Ext.form.SearchField({
		paramName:'filter',
		store: userMutiDS,
		width:320
	})],
	bbar: new Ext.PagingToolbar({
		store: userMutiDS,
		displayInfo: true,
		pageSize: 25,
		displayMsg: "当前记录第 {0} - {1}条，一共{2}条",
		emptyMsg: "没有记录",
		items:['-']
	}),
  listeners: {
  	render: function(){
  		this.store.baseParams.type ='<%=type%>';
  		this.store.baseParams.objectId ='<%=toId%>';
  		this.store.load({params:{start:0, limit:25}});
  	}
  }
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '人员选择',
    width: 500,
    height:400,
    minWidth: 300,
    minHeight: 150,
    layout: 'fit',
    plain:true,
    modal:true,
    bodyStyle:'padding:5px;',
    items: userSelectGrid,
    buttons:[{
    	text: '确定',
    	handler: function(){
    		var records = userSelectGrid.getSelectionModel().getSelections();
    		if (records.length<=0){
    			Ext.MessageBox.alert("信息","请选择人员");
    			return;
    		}
    		var users = "";
    		for (var i = 0; i < records.length; i++){
					if (i>0)
						users +=",";
					users += records[i].get("id");
    		}
    		postForm({'postType':'addRelations','toId':'<%=toId%>','rType':'<%=rType%>','users':users},"casAucPost.jsp",userSelectCallback);
    	}
    },{
    	text: '取消',
  	 	handler: function(){
  	 		window_dialog.close();
  	 	}
    }]
 });
window_dialog.show();
function userSelectCallback(){
	userDS.reload();
	window_dialog.close();
}

</script>