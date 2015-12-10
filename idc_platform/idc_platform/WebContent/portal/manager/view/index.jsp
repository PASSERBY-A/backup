<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.List"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<html> 
<head>
<title>视图配置</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="../layout/data-view-plugins.js"></script>
<link rel="stylesheet" type="text/css" href="../layout/data-view.css" />
<link rel="stylesheet" type="text/css" href="chooser.css" />
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
	       {name: 'oid', mapping: 'oid'},
	       {name: 'name', mapping: 'name'},
	       {name: 'layout', mapping: 'layout'},
	       {name: 'createTime', mapping: 'createTime'},
	       {name: 'creator', mapping: 'creator'}
	]),
	sortInfo: {field: 'oid',direction: 'DESC'}
});

var sm = new Ext.grid.CheckboxSelectionModel();

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '新增',
			tooltip : '新增视图',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '编辑',
			tooltip : '编辑视图',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要编辑的视图！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要编辑的视图！");
					return;
				}
				Ext.loadRemoteScript("edit.jsp",{oid:sm.getSelected().get("oid")}); 
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除视图',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的视图！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要删除的视图！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要删除选中的视图作吗?', function(btn) {
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
			text : '视图预览',
			tooltip : '视图预览',
			iconCls : 'icon-review',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要预览的视图！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要预览的视图！");
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
            {header: "视图名称", width: 160, sortable: true, dataIndex: 'name'},
            {header: "布局名称", width: 75, sortable: true, dataIndex: 'layout'},
            {header: "创建人", width: 75, sortable: true, dataIndex: 'creator'},
            {header: "创建时间", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'视图配置',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        bbar: new Ext.PagingToolbar({
        	pageSize: 9,
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