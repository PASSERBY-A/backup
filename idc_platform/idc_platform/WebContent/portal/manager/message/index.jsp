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

<script type="text/javascript" src="colorFiled.js"></script>
<link rel="stylesheet" type="text/css" href="colorFiled.css" />

<script type="text/javascript">
function openWin(url,width,height){
	 //��ô��ڵĴ�ֱλ��
    var iTop = (window.screen.availHeight-30-height)/2;        
    //��ô��ڵ�ˮƽλ��
    var iLeft = (window.screen.availWidth-10-width)/2;
	window.open (url, 'newwindow', 'height='+height+', width='+width+',top='+iTop+',left='+iLeft+', toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no') 
}

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
	       {name: 'title', mapping: 'title'},
	       {name: 'url', mapping: 'url'},
	       {name: 'userId', mapping: 'userId'},
	       {name: 'status', mapping: 'status'},
	       {name: 'module', mapping: 'module'}
	]),
	sortInfo: {field: 'oid',direction: 'desc'}
});

var sm = new Ext.grid.CheckboxSelectionModel();

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '�鿴',
			tooltip : '�鿴��Ϣ',
			iconCls : 'icon-review',
			handler : function() {
				if (sm.getCount() >1) {
					alert("����ѡ��Ҫһ���鿴����Ϣ��");
					return;
				}
				openWin('show.jsp?oid='+sm.getSelected().get("oid"),400,100);
			}
		},{
			xtype : 'tbbutton',
			text : 'ɾ��',
			tooltip : 'ɾ����Ϣ',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("����ѡ��Ҫɾ������Ϣ��");
					return;
				} 
				var messages = sm.getSelections();
				var oids = "";
				for (var i = 0; i < messages.length; i++) {
					var messageId = messages[i].get("oid");
					if (i == 0)
						oids = oids + messageId;
					else
						oids = oids + "," + messageId;
				}
				Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫɾ��ѡ�е���Ϣ��?', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request( {
					        url : 'action.jsp',
					        method : 'post',
					        params : {
					            action : 'delete',   
					            oids : oids
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

function getStatus(value, p, record){
	if(record.get('status')==1){
		return 'δ��';
	}else{
		return '�Ѷ�';
	}
}

Ext.onReady(function(){
	
	Ext.QuickTips.init();

    var grid = new Ext.grid.GridPanel({
        store: store,
        sm:sm,
        columns: [sm,
            {header: "����", width: 160, sortable: true, dataIndex: 'title'},
            {header: "����", width: 160, sortable: true, dataIndex: 'url'},
            {header: "״̬", width: 85, sortable: true, dataIndex: 'status',renderer: getStatus},
            {header: "ģ��", width: 85, sortable: true, dataIndex: 'module'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'��ͼ�ڵ�',
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

    grid.on('rowdblclick',function(grid,rowIndex,e){
            var record = grid.getStore().getAt(rowIndex);
            openWin('show.jsp?oid='+record.get('oid'),400,100);
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