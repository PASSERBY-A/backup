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

<script type="text/javascript" src="colorFiled.js"></script>
<link rel="stylesheet" type="text/css" href="colorFiled.css" />

<script type="text/javascript">
function openWin(url,width,height){
	 //获得窗口的垂直位置
    var iTop = (window.screen.availHeight-30-height)/2;        
    //获得窗口的水平位置
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
			text : '查看',
			tooltip : '查看消息',
			iconCls : 'icon-review',
			handler : function() {
				if (sm.getCount() >1) {
					alert("请先选择要一条查看的消息！");
					return;
				}
				openWin('show.jsp?oid='+sm.getSelected().get("oid"),400,100);
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除消息',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的消息！");
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
				Ext.MessageBox.confirm('提示', '确定要删除选中的消息吗?', function(btn) {
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
			tooltip : '查看本页帮助',
			iconCls : 'icon-help',
			// enableToggle : true,
			handler : function() {
			
			}
		}]
});

function getStatus(value, p, record){
	if(record.get('status')==1){
		return '未读';
	}else{
		return '已读';
	}
}

Ext.onReady(function(){
	
	Ext.QuickTips.init();

    var grid = new Ext.grid.GridPanel({
        store: store,
        sm:sm,
        columns: [sm,
            {header: "标题", width: 160, sortable: true, dataIndex: 'title'},
            {header: "链接", width: 160, sortable: true, dataIndex: 'url'},
            {header: "状态", width: 85, sortable: true, dataIndex: 'status',renderer: getStatus},
            {header: "模块", width: 85, sortable: true, dataIndex: 'module'}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'视图节点',
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