<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
	String type = request.getParameter("type");
	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
	Menu menu = mgr.getBeanById(type);
%>
<html> 
<head>
<title>菜单管理</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
var store = new Ext.data.GroupingStore({
	proxy: new Ext.data.HttpProxy({
		url: 'query.jsp'
	}),	
	baseParams:{
		mode:'sub',
		type:'<%=type%>'
	},
    reader: new Ext.data.JsonReader({
		root: 'items',
		totalProperty: 'totalCount',
		id: 'oid'
		}, [
	       {name: 'oid', mapping: 'oid'},
	       {name: 'title', mapping: 'title'},
	       {name: 'type', mapping: 'type'},
	       {name: 'url', mapping: 'url'},
	       {name: 'alt', mapping: 'alt'},
	       {name: 'createTime', mapping: 'createTime'},
	       {name: 'creator', mapping: 'creator'}
	]),
	sortInfo: {field: 'oid',direction: 'ASC'}
});

var sm = new Ext.grid.CheckboxSelectionModel();

function getType(value, p, record){
	return '<%=menu.getTitle()%>';
}

//fileTypeData		
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[-1,'全部'],[1,'普通'],[2,'重要'],[3,'里程碑']]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '新增',
			tooltip : '新增工作计划',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("subAdd.jsp",{type:'<%=type%>'}); 
			}
		},{
			xtype : 'tbbutton',
			text : '编辑',
			tooltip : '新增工作计划',
			iconCls : 'icon-update',
			handler : function() {
				Ext.loadRemoteScript("subEdit.jsp",{oid:sm.getSelected().get("oid"),type:'<%=type%>'}); 
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除工作计划',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的文档！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要删除的文档！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要删除选中的文档作吗?', function(btn) {
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
            {header: "标题", width: 160, sortable: true, dataIndex: 'title'},
            {header: "类型", width: 75, sortable: true, dataIndex: 'type',renderer: getType},
            {header: "链接", width: 75, sortable: true, dataIndex: 'url'},
            {header: "提示", width: 85, sortable: true, dataIndex: 'alt'},
            {header: "创建人", width: 75, sortable: true, dataIndex: 'creator'},
            {header: "创建时间", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'‘<%=menu.getTitle()%>’子菜单管理',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        loadMask: {msg: '正在加载数据，请稍侯……'}
    });
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