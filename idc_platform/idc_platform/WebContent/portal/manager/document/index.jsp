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
<title>�����ĵ�</title>
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
	data : [['ȫ��']<%for(int i=0;i<list.size();i++){%>,['<%=list.get(i)%>']<%}%>]
})

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : ['��ѯ�� ',{
            xtype: 'combo',
            id:'fileType',
            value: 'ȫ��',
            width : 70,
            valueField : 'value',
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
			store : fileTypeData
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
				store.baseParams.type = Ext.getCmp('fileType').getValue();
				store.baseParams.keyWords = Ext.getCmp('key').getValue();
    			store.load();
			}
		}, '-',{
			xtype : 'tbbutton',
			text : '�ϴ�',
			tooltip : '�ϴ������ļ�',
			iconCls : 'icon-upload',
			handler : function() {
				window.open ("upload.jsp", "newwindow", "height=290, width=390, toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no") //д��һ�� 
			}
		}, {
			xtype : 'tbbutton',
			text : '����',
			tooltip : '����ѡ�е��ļ�',
			iconCls : 'icon-download',
			handler : function() {
				if(sm.getCount() == 0){
            		alert("����ѡ��Ҫ���ص��ĵ���");
            		return;
            	}	
            	if(sm.getCount() > 1){
            		alert("��һ��ֻѡ��һ��Ҫ���ص��ĵ���");
            		return;
            	}
				window.open ("download.jsp?oid="+sm.getSelected().get("oid"), "newwindow", "height=1, width=1, toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no") //д��һ�� 
			}
		}, {
			xtype : 'tbbutton',
			text : '���߱༭',
			tooltip : '��֧��txt,doc,xls���',
			iconCls : 'icon-edit',
			handler : function() {
            	if(sm.getCount() > 1){
            		alert("��ѡ��һ��Ҫ�༭���ĵ���");
            		return;
            	}
            	if(sm.getSelected().get("creator")!=userId){
            		alert("���ܱ༭���˹����ĵ���");
            		return;
            	}
            	if(sm.getSelected().get("filetype")!='doc'&&sm.getSelected().get("filetype")!='xls'&&sm.getSelected().get("filetype")!='ppt'){
            		alert("�������ļ��޷����߱༭��");
            		return;
            	}
				window.open ("docEdit.jsp?oid="+sm.getSelected().get("oid"), "newwindow") //д��һ�� 
			}
		}, {
			xtype : 'tbbutton',
			text : 'ɾ��',
			tooltip : 'ɾ����ѡ�е��ĵ�',
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
		}, {
			xtype : 'tbbutton',
			text : '����',
			tooltip : '����ѡ���ļ�',
			iconCls : 'icon-share',
			handler : function() {
				if(sm.getCount() == 0){
            		alert("����ѡ��Ҫ������ĵ���");
            		return;
            	}	
            	if(sm.getCount() > 1){
            		alert("��һ��ֻѡ��һ��Ҫ������ĵ���");
            		return;
            	}
				Ext.loadRemoteScript("edit.jsp?oid=" + sm.getSelected().get("oid"));
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
            {header: "�ļ���", width: 160, sortable: true, dataIndex: 'name', renderer: getIcon,css:"vertical-align: middle;font-weight: bold;"},
            {header: "�ļ���С", width: 75, sortable: true, dataIndex: 'filesize',css:"vertical-align: middle;font-weight: bold;"},
            {header: "�ļ�����", width: 75, sortable: true, dataIndex: 'filetype',css:"vertical-align: middle;font-weight: bold;"},
            {header: "��������", width: 85, sortable: true, dataIndex: 'uploadTime',css:"font-weight: bold;vertical-align: middle;"},
            {header: "������", width: 75, sortable: true, dataIndex: 'creator',css:"vertical-align: middle;font-weight: bold;"},
            {header: "��������", width: 85, sortable: true, dataIndex: 'createTime',css:"vertical-align: middle;font-weight: bold;"}
        ],
        stripeRows: true,
        region: 'center',
        autoExpandColumn: 'name',	
        title:'�����ĵ�',
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