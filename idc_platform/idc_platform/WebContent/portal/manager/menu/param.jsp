<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="java.util.List"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<html> 
<head>
<title>菜单参数管理</title>
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
		url: 'paramsQuery.jsp'
	}),	
	baseParams:{mode:'all'},
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
	       {name: 'params', mapping: 'params'}
	]),
	sortInfo: {field: 'oid',direction: 'ASC'},
	groupField:'type'
});

var sm = new Ext.grid.CheckboxSelectionModel();

function getType(value, p, record){
	//[1,'综合运营门户'],[2,'监控管理中心'],[3,'业务管理中心'],[4,'运维管理中心'],[5,'运营分析中心']
	if(value=='1'){
		return '业务运营';
	}else if(value=='2'){
		return '客户管理';
	}else if(value=='3'){
		return '资源与监控';
	}else if(value=='4'){
		return '统计报表';
	}else if(value=='5'){
		return '系统维护';
	}
}

//fileTypeData		
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[-1,'全部'],[1,'普通'],[2,'重要'],[3,'	里程碑']]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '编辑参数',
			tooltip : '编辑菜单参数',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要编辑的菜单！");
					return;
				}
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要编辑的菜单！");
					return;
				}
				Ext.loadRemoteScript("paramEdit.jsp",{menuId:sm.getSelected().get("oid")});
			}
		},'->', '-', {
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
            {header: "标题", width: 85, sortable: true, dataIndex: 'title'},
            {header: "类型", width: 75, sortable: true, dataIndex: 'type',renderer: getType},
            {header: "链接", width: 75, sortable: true, dataIndex: 'url'},
            {header: "提示", width: 85, sortable: true, dataIndex: 'alt'},
            {header: "参数", width: 150, sortable: true, dataIndex: 'params'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'菜单参数管理',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        loadMask: {msg: '正在加载数据，请稍侯……'},
        view: new Ext.grid.GroupingView({
	        forceFit:true,
	        groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
	    })
    });
	store.baseParams.mode = 'all';
    store.load();
    
	grid.on('rowdblclick',function(grid,rowIndex,e){
            var record = grid.getStore().getAt(rowIndex);
            window.open('subIndex.jsp?type='+record.get('oid'));  		
    });
	
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