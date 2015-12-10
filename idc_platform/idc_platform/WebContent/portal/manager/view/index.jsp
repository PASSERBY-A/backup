<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="java.util.List"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<html> 
<head>
<title>��ͼ����</title>
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
			text : '����',
			tooltip : '������ͼ',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '�༭',
			tooltip : '�༭��ͼ',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫ�༭����ͼ��");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫ�༭����ͼ��");
					return;
				}
				Ext.loadRemoteScript("edit.jsp",{oid:sm.getSelected().get("oid")}); 
			}
		},{
			xtype : 'tbbutton',
			text : 'ɾ��',
			tooltip : 'ɾ����ͼ',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫɾ������ͼ��");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫɾ������ͼ��");
					return;
				}
				Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫɾ��ѡ�е���ͼ����?', function(btn) {
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
			text : '��ͼԤ��',
			tooltip : '��ͼԤ��',
			iconCls : 'icon-review',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��ҪԤ������ͼ��");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��ҪԤ������ͼ��");
					return;
				}
				window.open("view.jsp?oid="+sm.getSelected().get("oid")); 
			}
		}, '->', '-', {
			id : 'help',
			tooltip : '�鿴��ҳ����',
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
            {header: "��ͼ����", width: 160, sortable: true, dataIndex: 'name'},
            {header: "��������", width: 75, sortable: true, dataIndex: 'layout'},
            {header: "������", width: 75, sortable: true, dataIndex: 'creator'},
            {header: "����ʱ��", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'��ͼ����',
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
        	displayMsg: '�� {0} - {1}����¼ ,�� {2}����¼',
        	emptyMsg: 'û���ҵ��Ѿ�����Ĳ���'
        }),
        loadMask: {msg: '���ڼ������ݣ����Ժ��'}
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