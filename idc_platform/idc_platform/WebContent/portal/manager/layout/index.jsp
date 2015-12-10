<%@ page language="java" contentType="text/html; charset=gbk"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>²¼¾Ö¹ÜÀí</title>

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
        emptyText: 'Ã»ÓÐÈÎºÎ²¼¾ÖÄ£°åÐÅÏ¢'

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
			text : 'ÐÂÔö',
			tooltip : 'ÐÂÔö²¼¾ÖÄ£°å',
			iconCls : 'icon-add',
			handler : function() {
				Ext.loadRemoteScript("add.jsp"); 
			}
		},{
			xtype : 'tbbutton',
			text : '±à¼­',
			tooltip : '±à¼­²¼¾ÖÄ£°å',
			iconCls : 'icon-update',
			handler : function() {
				var selNode = dv.getSelectedRecords();
				if(selNode && selNode.length > 0){
					selNode = selNode[0];
					Ext.loadRemoteScript("edit.jsp",{oid:selNode.get("oid")}); 
				}else if(selNode && selNode.length > 1){
				    alert("Çë²»ÒªÑ¡Ôñ¶àÌõÄ£°æ¼ÇÂ¼");
				    return;
				}else{
				    alert("ÇëÑ¡ÔñÒ»ÌõÄ£°æ¼ÇÂ¼");
				    return;
				}
			}
		},{
			xtype : 'tbbutton',
			text : 'É¾³ý',
			tooltip : 'É¾³ý²¼¾ÖÄ£°å',
			iconCls : 'icon-delete',
			handler : function() {
				var selNode = dv.getSelectedRecords();
				if(!selNode || selNode.length < 1){
					alert("ÇëÑ¡ÔñÒ»ÌõÄ£°æ¼ÇÂ¼");
				    return;
				}else if(selNode && selNode.length > 1){
				    alert("Çë²»ÒªÑ¡Ôñ¶àÌõÄ£°æ¼ÇÂ¼");
				    return;
				}
				selNode = selNode[0];
				var oid = selNode.get("oid");
				Ext.MessageBox.confirm('ÌáÊ¾', 'È·¶¨ÒªÉ¾³ýÑ¡ÖÐµÄÎÄµµ×÷Âð?', function(btn) {
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
			tooltip : '²é¿´±¾Ò³°ïÖú',
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
        title:'²¼¾ÖÄ£°å',
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
