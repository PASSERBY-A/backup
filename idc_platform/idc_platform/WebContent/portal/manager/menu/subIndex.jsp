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
<title>�˵�����</title>
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
	data : [[-1,'ȫ��'],[1,'��ͨ'],[2,'��Ҫ'],[3,'��̱�']]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '����',
			tooltip : '���������ƻ�',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("subAdd.jsp",{type:'<%=type%>'}); 
			}
		},{
			xtype : 'tbbutton',
			text : '�༭',
			tooltip : '���������ƻ�',
			iconCls : 'icon-update',
			handler : function() {
				Ext.loadRemoteScript("subEdit.jsp",{oid:sm.getSelected().get("oid"),type:'<%=type%>'}); 
			}
		},{
			xtype : 'tbbutton',
			text : 'ɾ��',
			tooltip : 'ɾ�������ƻ�',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫɾ�����ĵ���");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫɾ�����ĵ���");
					return;
				}
				Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫɾ��ѡ�е��ĵ�����?', function(btn) {
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
            {header: "����", width: 160, sortable: true, dataIndex: 'title'},
            {header: "����", width: 75, sortable: true, dataIndex: 'type',renderer: getType},
            {header: "����", width: 75, sortable: true, dataIndex: 'url'},
            {header: "��ʾ", width: 85, sortable: true, dataIndex: 'alt'},
            {header: "������", width: 75, sortable: true, dataIndex: 'creator'},
            {header: "����ʱ��", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'��<%=menu.getTitle()%>���Ӳ˵�����',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        loadMask: {msg: '���ڼ������ݣ����Ժ��'}
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