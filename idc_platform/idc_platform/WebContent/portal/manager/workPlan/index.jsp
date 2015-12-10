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
	       {name: 'userId', mapping: 'userId'},
	       {name: 'title', mapping: 'title'},
	       {name: 'type', mapping: 'type'},
	       {name: 'status', mapping: 'status'},
	       {name: 'planTime', mapping: 'planTime'},
	       {name: 'finishTime', mapping: 'finishTime'},
	       {name: 'createTime', mapping: 'createTime'},
	       {name: 'description', mapping: 'description'}
	]),
	sortInfo: {field: 'oid',direction: 'DESC'}
});

var sm = new Ext.grid.CheckboxSelectionModel();

function getType(value, p, record){
	if(value=='2'){
		return '重要';
	}else if(value=='3'){
		return '里程碑';
	}else{
		return '普通';
	}
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
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '编辑',
			tooltip : '编辑工作计划',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要编辑的工作计划！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要编辑的工作计划！");
					return;
				}
				if(sm.getSelections()[0].get('status')=='完成'){
					alert("该工作计划已完成，不能进行编辑！");
					return;
				}
				Ext.loadRemoteScript("edit.jsp",{oid:sm.getSelected().get("oid")}); 
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除工作计划',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的工作计划！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要删除的工作计划！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要删除选中的工作计划作吗?', function(btn) {
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
			text : '完成计划',
			tooltip : '完成工作计划',
			iconCls : 'icon-finish',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要完成的工作计划！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要完成的工作计划！");
					return;
				}
				if(sm.getSelections()[0].get('status')=='完成'){
					alert("该工作计划已完成！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要完成选中的工作计划作吗?', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request( {
					        url : 'action.jsp',
					        method : 'post',
					        params : {
					            action : 'update',   
					            oid : sm.getSelected().get("oid"),
					            isFinish:'1'
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
		},'-','查询： ',{
            xtype: 'combo',
            id:'type',
            value: '全部',
            width : 70,
            valueField : 'id',
			displayField : 'value',
			typeAhead : true,
			mode : 'local',
			maxHeight : 150,
			triggerAction : 'all',
			emptyText : '请选择...',
			selectOnFocus : true,
			editable : false,
			forceSelection : true,
			allowBlank : false,
			value:'-1',
			store : typeData
        },new Ext.form.TextField({
			id:'key',
			width : 150
		}), {
			id : 'search',
			tooltip : '点击搜索',
			iconCls : 'icon-search',
			// enableToggle : true,
			handler : function() {
				store.baseParams.mode = 'filter';
				store.baseParams.type = Ext.getCmp('type').getValue();
				store.baseParams.keyWords = Ext.getCmp('key').getValue();
    			store.load();
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
            {header: "状态", width: 75, sortable: true, dataIndex: 'status'},
            {header: "预计完成时间", width: 85, sortable: true, dataIndex: 'planTime'},
            {header: "实际完成时间", width: 75, sortable: true, dataIndex: 'finishTime'},
            {header: "创建时间", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'工作计划',
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