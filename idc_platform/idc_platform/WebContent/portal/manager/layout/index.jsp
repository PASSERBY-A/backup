<%@ page language="java" contentType="text/html; charset=gbk"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>布局管理</title>

<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="data-view-plugins.js"></script>
<link rel="stylesheet" type="text/css" href="data-view.css" />
<script type="text/javascript">
var store = new Ext.data.JsonStore({
        url: 'get-images.jsp',
        root: 'images',
        fields: ['oid','name', 'url']
    });

    var tpl = new Ext.XTemplate(
		'<tpl for=".">',
            '<div class="thumb-wrap" id="{name}">',
		    '<div class="thumb"><img src="{url}" title="{name}"></div>',
		    '<span class="x-editable">{name}</span></div>',
        '</tpl>',
        '<div class="x-clear"></div>'
	);
	var dv = new Ext.DataView({
        store: store,
        tpl: tpl,
        autoHeight:true,
        multiSelect: true,
        overClass:'x-view-over',
        itemSelector:'div.thumb-wrap',
        emptyText: '没有任何布局模板信息'

        /*plugins: [
            new Ext.DataView.DragSelector(),
            new Ext.DataView.LabelEditor({dataIndex: 'name'})
        ],
        prepareData: function(data){
            data.shortName = Ext.util.Format.ellipsis(data.name, 15);
            data.sizeString = Ext.util.Format.fileSize(data.size);
            data.dateString = data.lastmod.format("m/d/Y g:i a");
            return data;
        },
        
        listeners: {
        	selectionchange: {
        		fn: function(dv,nodes){
        			var l = nodes.length;
        			var s = l != 1 ? 's' : '';
        			panel.setTitle('Simple DataView ('+l+' item'+s+' selected)');
        		}
        	}
        }*/
    });

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : [{
			xtype : 'tbbutton',
			text : '新增',
			tooltip : '新增布局模板',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '编辑',
			tooltip : '编辑布局模板',
			iconCls : 'icon-update',
			handler : function() {
				var selNode = dv.getSelectedRecords();
				if(selNode && selNode.length > 0){
					selNode = selNode[0];
					Ext.loadRemoteScript("edit.jsp",{oid:selNode.get("oid")}); 
				}else if(selNode && selNode.length > 1){
				    alert("请不要选择多条模版记录");
				    return;
				}else{
				    alert("请选择一条模版记录");
				    return;
				}
			}
		},{
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除布局模板',
			iconCls : 'icon-delete',
			handler : function() {
				var selNode = dv.getSelectedRecords();
				if(!selNode || selNode.length < 1){
					alert("请选择一条模版记录");
				    return;
				}else if(selNode && selNode.length > 1){
				    alert("请不要选择多条模版记录");
				    return;
				}
				selNode = selNode[0];
				var oid = selNode.get("oid");
				Ext.MessageBox.confirm('提示', '确定要删除选中的文档作吗?', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request( {
					        url : 'action.jsp',
					        method : 'post',
					        params : {
					            action : 'delete',   
					            oid : oid
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
    var xd = Ext.data;

    var panel = new Ext.Panel({
        id:'images-view',
        margins:'0 0 0 0',
		autoWidth: true,
        layout:'fit',
        title:'布局模板',
        region: 'center',
		tbar:topToolbar,
        items:dv 
    });
    store.load();
    var viewport = new Ext.Viewport({
		layout: "border",
		items: [panel]
	});
});
</script>
</head>
<body>
</body>
</html>
