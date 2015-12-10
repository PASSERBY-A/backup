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
	//解析附加属性的XML
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
	data : [[1,'文字类'],[2,'表格类'],[3,'图表类']]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	title : '基本属性',
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
                    fieldLabel: '节点名称',
                    name: 'name',
                    value: '<%=StringUtil.escapeJavaScript(node.getName())%>',
                    anchor:'-10'
                }, {
                    xtype:'colorfield',
                    fieldLabel: '前景色',
                    name: 'foreColor',
                    value: '<%=StringUtil.escapeJavaScript(node.getForeColor())%>',
                    anchor:'-10'
                }, {
                    xtype:'textfield',
                    fieldLabel: '宽度',
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
		            fieldLabel : '所属分类',
		            anchor:'-10',
		            valueField : 'id',
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
					value: '<%=node.getType()%>',
					store : typeData
		        },{
                    xtype:'colorfield',
                    fieldLabel: '背景色',
                    name: 'backColor',
                    value: '<%=node.getBackColor()%>',
                    anchor:'-10'
                },{
                    xtype:'textfield',
                    fieldLabel: '高度',
                    name: 'height',
                    value: '<%=node.getHeight()%>',
                    anchor:'-10'
                }]
            }]
        },{
            xtype:'textfield',
            fieldLabel:'路径',
            name: 'path',
            value: '<%=node.getPath()%>',
            anchor:'-10'
        },{
			xtype : 'selDlgfield',
			fieldLabel : '节点授权',
			selectUrl : '../common/viewRuleSelect.jsp',
			name : 'role',
			value : '<%=node.getRole()%>',			
			anchor:'-10'
		}]
});

var cm = new Ext.grid.ColumnModel([{
			header : "属性ID",
			dataIndex : 'id',
			width : 100,
			editor : new Ext.form.TextField()
		}, {
			header : "属性值",
			dataIndex : 'value',
			editor : new Ext.form.TextField(),
			width : 100
		}, {
			header : "属性备注",
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
		text : '新增',
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
		text : '插入',
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
		text : '删除',
		handler : function() {
			var cell = grid_formFieldList
					.getSelectionModel().getSelected();
			if (cell != null)
				ds.remove(cell);
		}
	}), '-', new Ext.Toolbar.Button({
		text : '上移',
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
		text : '下移',
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
				title : '附加属性',
				layout : 'anchor',
				baseCls : 'x-plain',
				defaultType : 'textfield',
				items : grid_formFieldList
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
		title : '编辑节点',
		border : false,
		modal : true,
		width : 600,
		height : 400,
		minWidth : 600,
		minHeight : 400,
		closable : true,
		layout : 'fit', // window的默认布局
		plain : true, // 颜色透明
		items : tabPanel,
		buttons : [{
			text : '保存',
			iconCls :'icon-save',
			handler : function() {
				grid_formFieldList.stopEditing();
				//验证浏览器是否为IE
				if (!Ext.isIE) {
					alert("请使用 IE 进行操作！");
					return;
				}
				var form = win._form.form;
				//判断表单必填字段是否为空
				if (!(form.isValid())) {
					Ext.Msg.alert("提示", "请填写正确的内容！");
					return;
				}
				//调用Windows自带ActiveX
				var doc = new ActiveXObject("Microsoft.XMLDOM");
				if (doc == null) {
					alert("new ActiveXObject('Microsoft.XMLDOM') 失败");
					return;
				}
				//生成附加属性的XML
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
					waitMsg : '正在更新，请稍候...', // form提交时等待的提示信息
					// 提交成功后的处理
					success : function(form, action) {   
			            var o = Ext.util.JSON.decode(action.response.responseText); 
			            alert(o.msg);
			            win.hide();
			            store.load();
			        },
					// 提交失败后的处理
					failure : function(form, action) {
						var o = Ext.util.JSON.decode(action.response.responseText); 
			            alert(o.msg);
			            win.hide();
			            store.load()
					}
				});
			}
			}, {
				text : '取消',
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
