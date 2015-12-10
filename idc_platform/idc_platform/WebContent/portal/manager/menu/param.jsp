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
<title>�˵���������</title>
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
	//[1,'�ۺ���Ӫ�Ż�'],[2,'��ع�������'],[3,'ҵ���������'],[4,'��ά��������'],[5,'��Ӫ��������']
	if(value=='1'){
		return 'ҵ����Ӫ';
	}else if(value=='2'){
		return '�ͻ�����';
	}else if(value=='3'){
		return '��Դ����';
	}else if(value=='4'){
		return 'ͳ�Ʊ���';
	}else if(value=='5'){
		return 'ϵͳά��';
	}
}

//fileTypeData		
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[-1,'ȫ��'],[1,'��ͨ'],[2,'��Ҫ'],[3,'	��̱�']]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '�༭����',
			tooltip : '�༭�˵�����',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫ�༭�Ĳ˵���");
					return;
				}
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫ�༭�Ĳ˵���");
					return;
				}
				Ext.loadRemoteScript("paramEdit.jsp",{menuId:sm.getSelected().get("oid")});
			}
		},'->', '-', {
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
            {header: "����", width: 85, sortable: true, dataIndex: 'title'},
            {header: "����", width: 75, sortable: true, dataIndex: 'type',renderer: getType},
            {header: "����", width: 75, sortable: true, dataIndex: 'url'},
            {header: "��ʾ", width: 85, sortable: true, dataIndex: 'alt'},
            {header: "����", width: 150, sortable: true, dataIndex: 'params'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'�˵���������',
        margins:'0 0 0 0',
		autoWidth: true,
		viewConfig: {
            forceFit:true
        },
        tbar:topToolbar,
        loadMask: {msg: '���ڼ������ݣ����Ժ��'},
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