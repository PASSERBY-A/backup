<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="java.util.ArrayList"%>
<%@include file="../../getUser.jsp" %>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
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
<style>
.group_none_expand{}
</style>
<script type="text/javascript">
Ext.ns("Ext.ux.grid.GroupingViewCustom");
Ext.ux.grid.GroupingViewCustom = function(config) {
                Ext.apply(this, config);
};
Ext.ux.grid.GroupingViewCustom = Ext.extend(Ext.grid.GroupingView, {
   //该字段存放分组标题后的链接文字，如果不想点击分组标题时就展开，也可将分组标题写在此处，而groupTextTpl为空即可。
    groupTextTplNoneExpend : '',

    // private
    initTemplates : function(){
        Ext.grid.GroupingView.superclass.initTemplates.call(this);
        this.state = {};

        var sm = this.grid.getSelectionModel();
        sm.on(sm.selectRow ? 'beforerowselect' : 'beforecellselect',
                this.onBeforeRowSelect, this);

        if(!this.startGroup){
            this.startGroup = new Ext.XTemplate(
                '<div id="{groupId}" class="x-grid-group {cls}">',
                    '<div id="{groupId}-hd" class="x-grid-group-hd" style="{style}"><div class="x-grid-group-title">', this.groupTextTpl ,
                     '<span class="group_none_expand">',this.groupTextTplNoneExpend,'</span>','</div></div>',
                     '<div id="{groupId}-bd" class="x-grid-group-body">'
            );
        }
        this.startGroup.compile();
        if(!this.endGroup){
            this.endGroup = '</div></div>';
        }
        //this.endGroup.compile();
    },

    // private
    interceptMouse : function(e){
        var hd = e.getTarget('.x-grid-group-hd', this.mainBody);
         var noneExpand = e.getTarget('.group_none_expand', this.mainBody);
        if(hd && !noneExpand){
            e.stopEvent();
            this.toggleGroup(hd.parentNode);
        }
    }
});

var store = new Ext.data.GroupingStore({
	proxy: new Ext.data.HttpProxy({
		url: 'query.jsp'
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
	       {name: 'typeOrder', mapping: 'typeOrder'},
	       {name: 'typeName', mapping: 'typeName'},
	       {name: 'url', mapping: 'url'},
	       {name: 'alt', mapping: 'alt'},
	       {name: 'createTime', mapping: 'createTime'},
	       {name: 'creator', mapping: 'creator'},
	       {name: 'orderno', type:'int', mapping: 'orderno'}
	]),
	sortInfo: {field: 'orderno',direction: 'ASC'},
	groupDir: 'ASC',
	groupField:'type'
});

var sm = new Ext.grid.RowSelectionModel();

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '新增',
			tooltip : '新增 菜单',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除菜单',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的菜单！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要删除的菜单！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要删除选中的菜单吗?', function(btn) {
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
		}]
});

Ext.onReady(function(){
	
	Ext.QuickTips.init();

    var grid = new Ext.grid.GridPanel({
        store: store,
        sm:sm,
        columns: [
            {header: "标题", width: 160, sortable: true, dataIndex: 'title'},
            {header: "类型ID", width: 75, sortable: true, dataIndex: 'type',hidden:true},
            {header: "类型", width: 75, sortable: true, dataIndex: 'typeName'},
            {header: "链接", width: 75, sortable: true, dataIndex: 'url'},
            {header: "提示", width: 85, sortable: true, dataIndex: 'alt'},
            {header: "创建人", width: 75, sortable: true, dataIndex: 'creator'},
            {header: "创建时间", width: 85, sortable: true, dataIndex: 'createTime'},
            {header: "排序号", width: 85, sortable: true, dataIndex: 'orderno'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'菜单管理',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        loadMask: {msg: '正在加载数据，请稍侯……'},
        view: new Ext.ux.grid.GroupingViewCustom({
	        forceFit:true,
	        groupTextTpl:'{[ values.rs[0].data["typeName"] ]}  ({[values.rs.length]} 个项目)',
	        groupTextTplNoneExpend:' <a href="javascript:editFirst(\'{[ values.rs[0].data["typeName"] ]}\')" style="float:right;padding-right:10px">编辑</a>'
	    })
    });
	store.baseParams.mode = 'all';
    store.load();
    
	grid.on('rowdblclick',function(grid,rowIndex,e){
			//remove this function
            //var record = grid.getStore().getAt(rowIndex);
            //window.open('subIndex.jsp?type='+record.get('oid'));  
            Ext.loadRemoteScript("edit.jsp",{oid:grid.getStore().getAt(rowIndex).get("oid")});		
    });
	
    var viewport = new Ext.Viewport({
		layout: "border",
		items: [grid]
	}); 
});

function editFirst(title){
	Ext.loadRemoteScript("edit1.jsp",{title:title});
}
</script>
</head>
<body>
</body>
</html>