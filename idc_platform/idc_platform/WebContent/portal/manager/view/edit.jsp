<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.bean.View"%>
<%@page import="com.hp.idc.portal.mgr.ViewMgr"%>
<%@page import="org.dom4j.*"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	String oid = request.getParameter("oid");
	ViewMgr mgr = (ViewMgr)ContextUtil.getBean("viewMgr");
	View view = mgr.getBeanById(oid);
	//�����������Ե�XML
	Document d = DocumentHelper.parseText(view.getNodes());
	Element e = d.getRootElement();
	List list = e.selectNodes("node");
	StringBuffer data = new StringBuffer();
	for(int i=0;i<list.size();i++){
		Element e1 = (Element)list.get(i);
		if(i>0){
			data.append(",");
		}
		data.append("['"+e1.attributeValue("areaId")+"','"+e1.attributeValue("nodeId")+"']");
	}
%>
<script type="text/javascript">
//����ģ��store
var jstore = new Ext.data.JsonStore({
    url: 'get-images.jsp',
    root: 'images',
    fields: ['oid','name', 'url','areaNum'],
   	listeners: {
		    	'load': {fn:function(){ dv.select(<%=view.getLayoutId()%>); }, scope:this, single:true}
		    }
});

var nodeDs = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
				url : '../node/query.jsp?type=view'
			}),
	reader : new Ext.data.JsonReader({
					root : 'items',
					totalProperty : 'totalCount',
					id : 'oid'
			}, [{
					name : 'oid',
					mapping : 'oid'
				}, {
					name : 'name',
					mapping : 'name'
				}])
});

var tpl = new Ext.XTemplate(
	'<tpl for=".">',
		'<div class="thumb-wrap" id="{name}">',
	    '<div class="thumb"><img src="{url}" title="{name}"></div>',
	    '<span class="x-editable">{name}</span></div>',
	'</tpl>',
	'<div class="x-clear"></div>'
);
var dtpl = new Ext.XTemplate(
	'<div class="details">',
		'<tpl for=".">',
			'<img src="{url}"><div class="details-info">',
			'<b>��������:</b>',
			'<span>{name}</span><br>',
			'<b>�������:</b>',
			'<span>{areaNum}</span>',
		'</tpl>',
	'</div>'
);

var topToolbar = new Ext.Toolbar({
	cls : 'search-toolbar',
	items : ['��ͼ���ƣ�',{
				xtype : 'textfield',
				id:'name',
				value:'<%=view.getName()%>'
	}]
});

var dv = new Ext.DataView({
	store: jstore,
	tpl: tpl,
	autoHeight:true,
	multiSelect: true,
	overClass:'x-view-over',
	itemSelector:'div.thumb-wrap',
	emptyText: 'û���κβ���ģ����Ϣ',
	listeners: {
		selectionchange: {
			fn: function(dv,nodes){
				var selNode = dv.getSelectedRecords();
			    var detailEl = Ext.getCmp('img-detail-panel').body;
				if(selNode && selNode.length > 0){
					selNode = selNode[0];
					var data = {oid:selNode.get("oid"),name:selNode.get("name"), url:selNode.get("url"),areaNum:selNode.get("areaNum")};
			        detailEl.hide();
			        dtpl.overwrite(detailEl, data);
			        detailEl.slideIn('l', {stopFx:true,duration:.2});
				}else{
				    detailEl.update('');
				}
			}
       	}
	}
});

var panel = new Ext.Panel({
	id:'images-view',
	margins:'0 0 0 0',
	width : 350,
	region: 'center',
	border:false,
	tbar:topToolbar,
	items:dv
});

var cm = new Ext.grid.ColumnModel([{
			header : "������",
			dataIndex : 'areaId',
			width : 100,
			editor : new Ext.form.TextField()
		}, {
			header : "��ͼ�ڵ�",
			dataIndex : 'nodeId',
			editor : new Ext.grid.GridEditor(new Ext.form.ComboBox({
						store : nodeDs,
						valueField : 'oid',
						displayField : 'name',
						typeAhead : true,
						mode : 'local',
						maxHeight : 150,
						triggerAction : 'all',
						emptyText : '��ѡ��...',
						selectOnFocus : true,
						editable : true,
						forceSelection : true,
						allowBlank : false
					})),
			width : 100
		}]);

var dsData = [<%=data.toString()%>];
var ds = new Ext.data.SimpleStore({
	fields : ['areaId', 'nodeId'],
	data : []
});

var Plant = Ext.data.Record.create([
	{name : 'areaId',type : 'string'}, 
	{name : 'nodeId',type : 'string'}])

var toolbar = new Ext.Toolbar({
	items : [new Ext.Toolbar.Button({
		text : '����',
		handler : function() {
			var p = new Plant({
						id : '',
						name : '',
						delay : 0,
						executeAction : ''
					});
			grid_formFieldList.stopEditing();
			ds.add(p);
		}
	}), '-', new Ext.Toolbar.Button({
		text : '����',
		handler : function() {
			var cell = grid_formFieldList
					.getSelectionModel().getSelected();
			var p = new Plant({
						id : '',
						name : '',
						delay : 0,
						executeAction : ''
					});
			grid_formFieldList.stopEditing();
			if (cell != null)
				ds.insert(ds.indexOf(cell), p);
			else
				ds.add(p);
		}
	}), '-', new Ext.Toolbar.Button({
		text : 'ɾ��',
		handler : function() {
			var cell = grid_formFieldList
					.getSelectionModel().getSelected();
			if (cell != null)
				ds.remove(cell);
		}
	}), '-', new Ext.Toolbar.Button({
		text : '����',
		handler : function() {
			var cell = grid_formFieldList
					.getSelectionModel().getSelected();
			if (cell == null)
				return;
			var n = ds.indexOf(cell);
			if (n == 0)
				return;
			var r = ds.getAt(n);
			ds.remove(r);
			ds.insert(n - 1, r);
			grid_formFieldList.getSelectionModel()
					.selectRow(n - 1, cell);
		}
	}), '-', new Ext.Toolbar.Button({
		text : '����',
		handler : function() {
			var cell = grid_formFieldList
					.getSelectionModel().getSelected();
			if (cell == null)
				return;
			var n = ds.indexOf(cell);
			if (n == ds.getCount() - 1)
				return;
			var r = ds.getAt(n);
			ds.remove(r);
			ds.insert(n + 1, r);
			grid_formFieldList.getSelectionModel().selectRow(n + 1, cell);
		}
	})]
});

var grid_formFieldList = new Ext.grid.EditorGridPanel({
	ds : ds,
	cm : cm,
	selModel : new Ext.grid.RowSelectionModel({
				singleSelect : true
			}),
	enableColLock : false,
	loadMask : true,
	anchor : '100% 100%',
	tbar : toolbar
});

var tabPanel = new Ext.TabPanel({
	baseCls : 'x-plain',
	activeTab : 0,
	border : false,
	bodyBorder : false,
	height : 340,
	layoutOnTabChange : true,
	items : [{
				title : '������Ϣ',
				layout : 'border',
				border:false,
				items : [panel,new Ext.Panel({
					id: 'img-detail-panel',
					region: 'east',
					split: true,
					width: 150,
					minWidth: 150,
					maxWidth: 250
				})]
			}, {
				title : '��ϸ����',
				layout : 'anchor',
				baseCls : 'x-plain',
				defaultType : 'textfield',
				items : grid_formFieldList
			}]
});

Ext.onReady(function(){
    
	if (win)
		win.hide();
	var win = new Ext.Window({
		id: 'img-chooser-dlg',
		title : '�༭��ͼ',
		border : false,
		modal : true,
		width : 500,
		height : 400,
		minWidth : 500,
		minHeight : 400,
		closable : true,
		layout : 'fit', // window��Ĭ�ϲ���
		plain : true, // ��ɫ͸��
		items : tabPanel,
		buttons : [{
					text : '����',
					iconCls :'icon-save',
					handler : function() {
						var selNode = dv.getSelectedRecords();
						if(selNode && selNode.length > 0){
							selNode = selNode[0];
						}else{
							alert("��ѡ��һ������");
							return;
						}
						if (!Ext.isIE) {
							alert("��ʹ�� IE ���в�����");
							return;
						}
						//����Windows�Դ�ActiveX
						var doc = new ActiveXObject("Microsoft.XMLDOM");
						if (doc == null) {
							alert("new ActiveXObject('Microsoft.XMLDOM') ʧ��");
							return;
						}
						//���ɸ������Ե�XML
						doc.loadXML("<nodes/>");
						var ds = grid_formFieldList.getStore();
						var count = ds.getCount();
						for (var i = 0; i < count; i++) {
							var node = doc.createElement("node");
							node.setAttribute("nodeId", ds.getAt(i).get("nodeId"));
							node.setAttribute("areaId", ds.getAt(i).get("areaId"));
							doc.documentElement.appendChild(node);
						}
						Ext.Ajax.request( {   
					        url : 'action.jsp',   
					        method : 'post',   
					        params : {
								oid:'<%=oid%>',
					        	name : Ext.getCmp('name').getValue(),
					        	layoutId:selNode.get("oid"),
					        	nodes: doc.xml,
					        	action:'update'
					        },   
					        success : function(response, options) {   
					        	var o = Ext.util.JSON.decode(response.responseText); 
					            alert(o.msg);
					            win.close();
					            store.load();
					        },   
					        failure : function() {   
					        	
					        }
					    });
					}
				}, {
					text : 'ȡ��',
					iconCls :'icon-cancel',
					handler : function() {
						win.close();
					}
				}]
	});
	jstore.load();
	nodeDs.load();
	ds.loadData(dsData);
	win.show();
});
</script>