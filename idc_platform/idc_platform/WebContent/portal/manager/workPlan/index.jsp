<%@ page language="java" contentType="text/html; charset=gbk"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<html> 
<head>
<title>�����ƻ�</title>
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
		return '��Ҫ';
	}else if(value=='3'){
		return '��̱�';
	}else{
		return '��ͨ';
	}
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
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '�༭',
			tooltip : '�༭�����ƻ�',
			iconCls : 'icon-update',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫ�༭�Ĺ����ƻ���");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫ�༭�Ĺ����ƻ���");
					return;
				}
				if(sm.getSelections()[0].get('status')=='���'){
					alert("�ù����ƻ�����ɣ����ܽ��б༭��");
					return;
				}
				Ext.loadRemoteScript("edit.jsp",{oid:sm.getSelected().get("oid")}); 
			}
		},{
			xtype : 'tbbutton',
			text : 'ɾ��',
			tooltip : 'ɾ�������ƻ�',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫɾ���Ĺ����ƻ���");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫɾ���Ĺ����ƻ���");
					return;
				}
				Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫɾ��ѡ�еĹ����ƻ�����?', function(btn) {
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
			text : '��ɼƻ�',
			tooltip : '��ɹ����ƻ�',
			iconCls : 'icon-finish',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫ��ɵĹ����ƻ���");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("��һ��ֻѡ��һ��Ҫ��ɵĹ����ƻ���");
					return;
				}
				if(sm.getSelections()[0].get('status')=='���'){
					alert("�ù����ƻ�����ɣ�");
					return;
				}
				Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫ���ѡ�еĹ����ƻ�����?', function(btn) {
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
		},'-','��ѯ�� ',{
            xtype: 'combo',
            id:'type',
            value: 'ȫ��',
            width : 70,
            valueField : 'id',
			displayField : 'value',
			typeAhead : true,
			mode : 'local',
			maxHeight : 150,
			triggerAction : 'all',
			emptyText : '��ѡ��...',
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
			tooltip : '�������',
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
            {header: "����", width: 160, sortable: true, dataIndex: 'title'},
            {header: "����", width: 75, sortable: true, dataIndex: 'type',renderer: getType},
            {header: "״̬", width: 75, sortable: true, dataIndex: 'status'},
            {header: "Ԥ�����ʱ��", width: 85, sortable: true, dataIndex: 'planTime'},
            {header: "ʵ�����ʱ��", width: 75, sortable: true, dataIndex: 'finishTime'},
            {header: "����ʱ��", width: 85, sortable: true, dataIndex: 'createTime'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'�����ƻ�',
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