<%@ page language="java" contentType="text/html; charset=gbk"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<html> 
<head>
<title>工作计划</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="colorFiled.js"></script>
<link rel="stylesheet" type="text/css" href="colorFiled.css" />

<script type="text/javascript">
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: 'query.jsp'
	}),	
	baseParams:{},
    reader: new Ext.data.JsonReader({
		root: 'items',
		totalProperty: 'totalCount',
		id: 'oid'
		}, [
			//oid,name,backcolor,forecolor,width,height,advprop,role,creator,createtime,path,type
	       {name: 'oid', mapping: 'oid'},
	       {name: 'name', mapping: 'name'},
	       {name: 'backcolor', mapping: 'backcolor'},
	       {name: 'forecolor', mapping: 'forecolor'},
	       {name: 'width', mapping: 'width'},
	       {name: 'height', mapping: 'height'},
	       {name: 'creator', mapping: 'creator'},
	       {name: 'createTime', mapping: 'createTime'},
	       {name: 'path', mapping: 'path'},
	       {name: 'type', mapping: 'type'}
	]),
	sortInfo: {field: 'oid',direction: 'ASC'}
});

var sm = new Ext.grid.CheckboxSelectionModel();

function getColor(value, p, record){
	return String.format('<font color={0}>██████</font>',value);
}

function getSize(value, p, record){
	return value +"*"+ record.data.height;
}

function getType(value, p, record){
	if(value==1)
		return "文字类";
	if(value==2)
		return "表格类";
	if(value==3)
		return "图表类";
}

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '新增',
			tooltip : '新增视图节点',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '编辑',
			tooltip : '新增视图节点',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要编辑的视图节点！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要编辑的视图节点！");
					return;
				}
				Ext.loadRemoteScript("edit.jsp",{oid:sm.getSelected().get("oid")}); 
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除视图节点',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的视图节点！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要删除的视图节点！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要删除选中的视图节点吗?', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request( {
					        url : 'action.jsp',
					        method : 'post',
					        params : {
					            action : 'delete',   
					            oid : sm.getSelected().get("oid")
					        },
					        success : function(response, options) {   
					            var o = Ext.util.JSON.decode(response.responseText); 
					            alert(o.msg);
					            store.load();
					        },
					        failure : function() { 
					        }
					    });
					}
				});
			}
		},{
			xtype : 'tbbutton',
			text : '节点预览',
			tooltip : '节点预览',
			iconCls : 'icon-review',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要预览的节点！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要预览的节点！");
					return;
				}
				window.open("view.jsp?oid="+sm.getSelected().get("oid")); 
			}
		}, '->', '-', {
			id : 'help',
			tooltip : '查看本页帮助',
			iconCls : 'icon-help',
			// enableToggle : true,
			handler : function() {
			
			}
		}]
});

Ext.onReady(function(){
	
	Ext.QuickTips.init();

    var grid = new Ext.grid.GridPanel({
        store: store,
        sm:sm,
        columns: [
            {header: "名称", width: 160, sortable: true, dataIndex: 'name'},
            {header: "类型", width: 160, sortable: true, dataIndex: 'type',renderer:getType},
            {header: "前景色", width: 75, sortable: true, dataIndex: 'forecolor',renderer:getColor},
            {header: "背景色", width: 85, sortable: true, dataIndex: 'backcolor',renderer:getColor},
            {header: "宽度", width: 85, sortable: true, dataIndex: 'width'},
            {header: "高度", width: 85, sortable: true, dataIndex: 'height'},
            {header: "创建人", width: 75, sortable: true, dataIndex: 'creator'},
            {header: "创建时间", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'视图节点',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        bbar: new Ext.PagingToolbar({
        	pageSize: 20,
        	store: store,
        	displayInfo: true,
        	displayMsg: '第 {0} - {1}条记录 ,共 {2}条记录',
        	emptyMsg: '没有找到已经定义的操作'
        }),
        loadMask: {msg: '正在加载数据，请稍侯……'}
    });
	store.baseParams.mode = 'all';
    store.load();

    var viewport = new Ext.Viewport({
		layout: "border",
		items: [grid]
	}); 
});
</script>
</head>
<body>
</body>
</html>