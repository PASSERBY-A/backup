<%@page contentType="text/html; charset=gb2312" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.resm.service.*"%>
<%@ page import="com.hp.idc.resm.model.*"%>
<%@ page import="com.hp.idc.resm.resource.*"%>
<%@ page import="com.hp.idc.resm.util.*"%>
<%@ page import="com.hp.idc.resm.ui.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
int pageCount = 20;

String modelId = request.getParameter("model");
Model model = ServiceManager.getModelService().getModelById(modelId);
List<AttributeDefine> headers = model.getHeader();
%>

<html>
<head>
  <title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
	<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/extjs/ext-all.js"></script>
	<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="/extjs/ext-ext.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/common/css/style.css" />
</head>
<body>
<script type="text/javascript">

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

var store = new Ext.data.GroupingStore({
	proxy: new Ext.data.HttpProxy({
		url: '/idc_platform/resm/interface/resQuery1.jsp'
	}),
	baseParams: { "modelId": "<%=modelId%>", limit: <%=pageCount%>,oper:"0" },
	autoLoad:true,
	reader: new Ext.data.JsonReader({
		root: 'items',
		totalProperty: 'totalCount',
		id: 'id'
	}, [
<%
	for (int i = 0; i < headers.size(); i++)
	{
		if (i > 0)
			out.print(",");
		out.print("{name: \"" + headers.get(i).getId() + "\"}");
	}
%>	
	]),
	sortInfo:{field: 'name', direction: 'ASC'},
	remoteSort: true
});
store.on('load',function(s,options){
	Ext.apply(s.baseParams,{
		oper:"0"
	});
});
var pt = new Ext.PagingToolbar({
			pageSize: <%=pageCount%>,
			store: store,
			displayInfo: true,
			displayMsg: '第 {0} - {1} 条，共 {2} 条记录',
			emptyMsg: "无记录",
			//plugins: filters,
			items: ['-']
		});

var mainPanel;
var sf = new Ext.form.SearchField({
	paramName:'filter',
	store: store,
	emptyText : '<%=model.createObject().getMatchDescription()%>',
	width: 560
});
	
mainPanel = new Ext.grid.GridPanel({
	region:'center',
	store: store,
	margins:'1 1 1 1',
	columns: [
<%
	for (int i = 0; i < headers.size(); i++)
	{
		if (i > 0)
			out.print(",");
		out.print("{header: \"" + headers.get(i).getName() + "\", dataIndex: '" + headers.get(i).getId()
			+ "', sortable: true, renderer:redKeywords}");
	}
%>	
	],
	sm: new Ext.grid.RowSelectionModel({
			singleSelect: true
		}),
	tbar: [
			'模糊查找: ', sf,
			{
				text:'高级查询',
				iconCls:'search',
				handler:function(){
					showSearch();
				}
			}
			],
	bbar: pt,
	viewConfig: {
		forceFit: true
	},
	width:'100%',
	split:true,
	height: 200,
	loadMask: true,
   	view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
        })
});

Ext.onReady(function(){
	var viewport = new Ext.Viewport({
		layout:'border',
		items:[mainPanel]
	});
});


function redKeywords(val){
	var str=sf.getValue();
	var strArr=str.split(',');
	
	for(var i=0;i<strArr.length;i++){
		s=strArr[i].trim();
		if(val.indexOf(s)>-1){
			var text="";
			text+=val.substring(0,val.indexOf(s));
			text+="<font color='red'>"+val.substr(val.indexOf(s),s.length)+"</font>";
			text+=val.substring(val.indexOf(s)+s.length,val.length);
			val=text;
		}
	}
	
	return val;
}
String.prototype.trim = function() { 
	return this.replace(/(^\s*)|(\s*$)/g, "");
} 
var searchWin;
function showSearch(){
	if(!searchWin){
		searchWin=new Ext.Window({
			id : 'searchWin',
			title : '高级查询',
			layout : 'fit',
			closable : true,
			closeAction : 'hide',
			constrain:true,
			plain : true,
			modal : true,
			width : 500,
			height : 240,
			resizable : false,
			items : [ new Ext.form.FormPanel({
					id : 'productForm',
					layout : 'form',
					frame : true,
					labelAlign : 'right',
					labelWidth : 65,
					items:[
						{
							xtype:'textfield',
							id:'id',
							width:380,
							fieldLabel:'资源编号'
						},
						{
							xtype:'textfield',
							id:'name',
							width:380,
							fieldLabel:'名称'
						},
						{
							xtype:'textfield',
							id:'customer_id',
							width:380,
							fieldLabel:'客户编号'
						},
						{
							xtype:'textfield',
							id:'order_id',
							width:380,
							fieldLabel:'订单编号'
						},
						new Ext.form.ComboBox({
							id : 'status',
							labelWidth : 80,
							width : 380,
							fieldLabel : '状态',
							mode : 'local',
							// readOnly : true,
							triggerAction : 'all',
							store : new Ext.data.SimpleStore({
								fields : [ "value", "text" ],
								data : [ [ "","---请选择---"],[ "空闲", "空闲" ], [ "已安装", "已安装" ],["预占","预占"],["实占","实占"],["使用中","使用中"],["备用中","备用中"] ]
							}),
							valueField : "value",
							displayField : "text",
							editable: false,
							forceSelection: true,
							value : ""
						})
					]
				})
			],
			buttonAlign : 'center',
			buttons : [ {
					text : '查询',
					handler : function() {
						store.baseParams.oper='1';
						store.baseParams.id=Ext.getCmp('id').getValue();
						store.baseParams.name=Ext.getCmp('name').getValue();
						store.baseParams.customer_id=Ext.getCmp('customer_id').getValue();
						store.baseParams.order_id=Ext.getCmp('order_id').getValue();
						store.baseParams.status=Ext.getCmp('status').getValue();
						sf.setValue('');
						store.reload();
						searchWin.hide();
					}
				}
			]
		});
	}
	searchWin.show();
}
</script>
 </body>
</html>
