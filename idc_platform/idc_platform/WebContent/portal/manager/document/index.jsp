<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@include file="../../getUser.jsp" %>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>
<%
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	List<String> list = mgr.getFileTypeByUserId(userId);
%>
<html> 
<head>
<title>个人文档</title>
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/extjs/resources/css/ext-ext.css" />
<link rel="stylesheet" type="text/css" href="../style/css/index.css" />
<script type="text/javascript" src="/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/extjs/ext-all.js"></script>
<script type="text/javascript" src="/extjs/ext-ext.js"></script>
<script type="text/javascript" charset="utf-8" src="/extjs/source/locale/ext-lang-zh_CN.js"></script>
<script type="text/javascript">
var userId = <%=userId%>;
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
	       {name: 'filesize', mapping: 'filesize'},
	       {name: 'filetype', mapping: 'filetype'},
	       {name: 'filepath', mapping: 'filepath'},
	       {name: 'filename', mapping: 'filename'},
	       {name: 'creator', mapping: 'creator'},
	       {name: 'uploadTime', mapping: 'uploadTime'},
	       {name: 'createTime', mapping: 'createTime'}
	]),
	sortInfo: {field: 'oid',direction: 'DESC'}
});

var sm = new Ext.grid.CheckboxSelectionModel();

function getIcon(value, p, record){
	var icon = "unknown";
	if(record.get('filetype')=="html"||record.get('filetype')=="htm"){
		icon = "html"
	}
	if(record.get('filetype')=="doc"||record.get('filetype')=="docx"){
		icon = "doc"
	}
	if(record.get('filetype')=="ppt"||record.get('filetype')=="pptx"){
		icon = "ppt"
	}
	if(record.get('filetype')=="ini"){
		icon = "ini"
	}
	if(record.get('filetype')=="inf"){
		icon = "inf"
	}
	if(record.get('filetype')=="gif"){
		icon = "gif"
	}
	if(record.get('filetype')=="zip"){
		icon = "zip"
	}
	if(record.get('filetype')=="bmp"){
		icon = "bmp"
	}
	if(record.get('filetype')=="xls"||record.get('filetype')=="xlsx"){
		icon = "xls"
	}
	if(record.get('filetype')=="jpg"||record.get('filetype')=="jpeg"){
		icon = "jpg"
	}
	if(record.get('filetype')=="cab"){
		icon = "cab"
	}
	if(record.get('filetype')=="dll"){
		icon = "dll"
	}
	if(record.get('filetype')=="txt"){
		icon = "txt"
	}
	if(record.get('filetype')=="png"){
		icon = "png"
	}
	if(record.get('filetype')=="pdf"){
		icon = "pdf"
	}
	if(record.get('filetype')=="mp3"){
		icon = "mp3"
	}
    return String.format('<img src="typeIcon/{0}.png" alt="* " align="absmiddle"  style="width: 44"/>&nbsp;{1}', icon, value );
}

//fileTypeData		
var fileTypeData = new Ext.data.SimpleStore({
	fields : ['value'],
	data : [['全部']<%for(int i=0;i<list.size();i++){%>,['<%=list.get(i)%>']<%}%>]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : ['查询： ',{
            xtype: 'combo',
            id:'fileType',
            value: '全部',
            width : 70,
            valueField : 'value',
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
			store : fileTypeData
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
				store.baseParams.type = Ext.getCmp('fileType').getValue();
				store.baseParams.keyWords = Ext.getCmp('key').getValue();
    			store.load();
			}
		}, '-',{
			xtype : 'tbbutton',
			text : '上传',
			tooltip : '上传本地文件',
			iconCls : 'icon-upload',
			handler : function() {
				window.open ("upload.jsp", "newwindow", "height=290, width=390, toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no") //写成一行 
			}
		}, {
			xtype : 'tbbutton',
			text : '下载',
			tooltip : '下载选中的文件',
			iconCls : 'icon-download',
			handler : function() {
				if(sm.getCount() == 0){
            		alert("请先选择要下载的文档！");
            		return;
            	}	
            	if(sm.getCount() > 1){
            		alert("请一次只选择一个要下载的文档！");
            		return;
            	}
				window.open ("download.jsp?oid="+sm.getSelected().get("oid"), "newwindow", "height=1, width=1, toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no") //写成一行 
			}
		}, {
			xtype : 'tbbutton',
			text : '在线编辑',
			tooltip : '仅支持txt,doc,xls类别',
			iconCls : 'icon-edit',
			handler : function() {
            	if(sm.getCount() > 1){
            		alert("请选择一个要编辑的文档！");
            		return;
            	}
            	if(sm.getSelected().get("creator")!=userId){
            		alert("不能编辑他人共享文档！");
            		return;
            	}
            	if(sm.getSelected().get("filetype")!='doc'&&sm.getSelected().get("filetype")!='xls'&&sm.getSelected().get("filetype")!='ppt'){
            		alert("该类型文件无法在线编辑！");
            		return;
            	}
				window.open ("docEdit.jsp?oid="+sm.getSelected().get("oid"), "newwindow") //写成一行 
			}
		}, {
			xtype : 'tbbutton',
			text : '删除',
			tooltip : '删除你选中的文档',
			iconCls : 'icon-delete',
			handler : function() {
				if (sm.getCount() == 0) {
					alert("请先选择要删除的文档！");
					return;
				} 
				if (sm.getCount() > 1) {
					alert("请一次只选择一个要删除的文档！");
					return;
				}
				Ext.MessageBox.confirm('提示', '确定要删除选中的文档作吗?', function(btn) {
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
		}, {
			xtype : 'tbbutton',
			text : '共享',
			tooltip : '共享选定文件',
			iconCls : 'icon-share',
			handler : function() {
				if(sm.getCount() == 0){
            		alert("请先选择要共享的文档！");
            		return;
            	}	
            	if(sm.getCount() > 1){
            		alert("请一次只选择一个要共享的文档！");
            		return;
            	}
				Ext.loadRemoteScript("edit.jsp?oid=" + sm.getSelected().get("oid"));
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
	
	Ext.QuickTips.init();

    var grid = new Ext.grid.GridPanel({
        store: store,
        sm:sm,
        columns: [
            {header: "文件名", width: 160, sortable: true, dataIndex: 'name', renderer: getIcon,css:"vertical-align: middle;font-weight: bold;"},
            {header: "文件大小", width: 75, sortable: true, dataIndex: 'filesize',css:"vertical-align: middle;font-weight: bold;"},
            {header: "文件类型", width: 75, sortable: true, dataIndex: 'filetype',css:"vertical-align: middle;font-weight: bold;"},
            {header: "更新日期", width: 85, sortable: true, dataIndex: 'uploadTime',css:"font-weight: bold;vertical-align: middle;"},
            {header: "创建人", width: 75, sortable: true, dataIndex: 'creator',css:"vertical-align: middle;font-weight: bold;"},
            {header: "创建日期", width: 85, sortable: true, dataIndex: 'createTime',css:"vertical-align: middle;font-weight: bold;"}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'个人文档',
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