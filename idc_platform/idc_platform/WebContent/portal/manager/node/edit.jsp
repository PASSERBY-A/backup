<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="org.dom4j.*"%>
<%@page import="java.util.List"%>
<%@page import="com.hp.idc.portal.mgr.ViewNodeMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.bean.ViewNode"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%
	String  oid = request.getParameter("oid");
	ViewNodeMgr mgr = (ViewNodeMgr)ContextUtil.getBean("viewNodeMgr");
	ViewNode node = mgr.getBeanById(oid);
	//�����������Ե�XML
	Document d = DocumentHelper.parseText(node.getAdvProp());
	Element e = d.getRootElement();
	List list = e.selectNodes("propertie");
	StringBuffer data = new StringBuffer();
	for(int i=0;i<list.size();i++){
		Element e1 = (Element)list.get(i);
		if(i>0){
			data.append(",");
		}
		data.append("['"+StringUtil.escapeJavaScript(e1.attributeValue("id"))+"','"+StringUtil.escapeJavaScript(e1.attributeValue("value"))+"','"+StringUtil.escapeJavaScript(e1.attributeValue("bak"))+"']");
	}
%>
<script type="text/javascript">
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[1,'������'],[2,'�����'],[3,'ͼ����']]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	title : '��������',
	baseParams: {action:'update',oid:'<%=oid%>'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 60,
	items: [{
            layout:'column',
            items:[{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '�ڵ�����',
                    name: 'name',
                    value: '<%=StringUtil.escapeJavaScript(node.getName())%>',
                    anchor:'-10'
                }, {
                    xtype:'colorfield',
                    fieldLabel: 'ǰ��ɫ',
                    name: 'foreColor',
                    value: '<%=StringUtil.escapeJavaScript(node.getForeColor())%>',
                    anchor:'-10'
                }, {
                    xtype:'textfield',
                    fieldLabel: '���',
                    name: 'width',
                    value: '<%=StringUtil.escapeJavaScript(node.getWidth())%>',
                    anchor:'-10'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
		            xtype: 'combo',
		            hiddenName: 'type',
		            fieldLabel : '��������',
		            anchor:'-10',
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
					value: '<%=node.getType()%>',
					store : typeData
		        },{
                    xtype:'colorfield',
                    fieldLabel: '����ɫ',
                    name: 'backColor',
                    value: '<%=node.getBackColor()%>',
                    anchor:'-10'
                },{
                    xtype:'textfield',
                    fieldLabel: '�߶�',
                    name: 'height',
                    value: '<%=node.getHeight()%>',
                    anchor:'-10'
                }]
            }]
        },{
            xtype:'textfield',
            fieldLabel:'·��',
            name: 'path',
            value: '<%=node.getPath()%>',
            anchor:'-10'
        },{
			xtype : 'selDlgfield',
			fieldLabel : '�ڵ���Ȩ',
			selectUrl : '../common/viewRuleSelect.jsp',
			name : 'role',
			value : '<%=node.getRole()%>',			
			anchor:'-10'
		}]
});

var cm = new Ext.grid.ColumnModel([{
			header : "����ID",
			dataIndex : 'id',
			width : 100,
			editor : new Ext.form.TextField()
		}, {
			header : "����ֵ",
			dataIndex : 'value',
			editor : new Ext.form.TextField(),
			width : 100
		}, {
			header : "���Ա�ע",
			dataIndex : 'bak',
			editor : new Ext.form.TextField(),
			width : 300
		}]);

var dsData = [<%=data.toString()%>];
var ds = new Ext.data.SimpleStore({
	fields : ['id', 'value', 'bak'],
	data : []
});
var Plant = Ext.data.Record.create([
	{name : 'id',type : 'string'}, 
	{name : 'value',type : 'string'}, 
	{name : 'bak',type : 'string'}]);

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
	bodyStyle:'padding:10px;',
	bodyBorder : false,
	height : 340,
	layoutOnTabChange : true,
	items : [form, {
				title : '��������',
				layout : 'anchor',
				baseCls : 'x-plain',
				defaultType : 'textfield',
				items : grid_formFieldList
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
		title : '�༭�ڵ�',
		border : false,
		modal : true,
		width : 600,
		height : 400,
		minWidth : 600,
		minHeight : 400,
		closable : true,
		layout : 'fit', // window��Ĭ�ϲ���
		plain : true, // ��ɫ͸��
		items : tabPanel,
		buttons : [{
			text : '����',
			iconCls :'icon-save',
			handler : function() {
				grid_formFieldList.stopEditing();
				//��֤������Ƿ�ΪIE
				if (!Ext.isIE) {
					alert("��ʹ�� IE ���в�����");
					return;
				}
				var form = win._form.form;
				//�жϱ������ֶ��Ƿ�Ϊ��
				if (!(form.isValid())) {
					Ext.Msg.alert("��ʾ", "����д��ȷ�����ݣ�");
					return;
				}
				//����Windows�Դ�ActiveX
				var doc = new ActiveXObject("Microsoft.XMLDOM");
				if (doc == null) {
					alert("new ActiveXObject('Microsoft.XMLDOM') ʧ��");
					return;
				}
				//���ɸ������Ե�XML
				doc.loadXML("<properties/>");
				var ds = grid_formFieldList.getStore();
				var count = ds.getCount();
				for (var i = 0; i < count; i++) {
					var node = doc.createElement("propertie");
					if(ds.getAt(i).get("id")==""||ds.getAt(i).get("value")=="")
						continue;
					node.setAttribute("id", ds.getAt(i).get("id"));
					node.setAttribute("value", ds.getAt(i).get("value"));
					node.setAttribute("bak", ds.getAt(i).get("bak"));
					doc.documentElement.appendChild(node);
				}
	
				form.baseParams.advProp = doc.xml;
				doc = null;
	
				form.submit({
					waitMsg : '���ڸ��£����Ժ�...', // form�ύʱ�ȴ�����ʾ��Ϣ
					// �ύ�ɹ���Ĵ���
					success : function(form, action) {   
			            var o = Ext.util.JSON.decode(action.response.responseText); 
			            alert(o.msg);
			            win.hide();
			            store.load();
			        },
					// �ύʧ�ܺ�Ĵ���
					failure : function(form, action) {
						var o = Ext.util.JSON.decode(action.response.responseText); 
			            alert(o.msg);
			            win.hide();
			            store.load()
					}
				});
			}
			}, {
				text : 'ȡ��',
				iconCls :'icon-cancel',
				handler : function() {
					win.hide();
				}
			}]
	});
ds.loadData(dsData);
win._form = form;
win.show();
</script>
