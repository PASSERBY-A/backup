<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.bean.Layout"%>
<%@page import="com.hp.idc.portal.mgr.LayoutMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	String oid = request.getParameter("oid");
	LayoutMgr mgr = (LayoutMgr)ContextUtil.getBean("layoutMgr");
	Layout bean = mgr.getBeanById(oid);
%>
<script type="text/javascript">
var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'update',oid:'<%=oid%>'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 60,
	items : [{
				xtype : 'textfield',
				fieldLabel : '布局名称',
				name : 'name',
				allowBlank : false,
				value:'<%=bean.getName()%>',
				anchor:'-10'
			},{
				xtype : 'textfield',
				fieldLabel : '模板路径',
				name : 'path',
				allowBlank : false,
				value:'<%=bean.getPath()%>',
				anchor:'-10'
			},{
				xtype : 'textfield',
				fieldLabel : '区域个数',
				name : 'areanum',
				value:'<%=bean.getAreaNum()%>',
				anchor:'-10'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '编辑布局',
			border : false,
			modal : true,
			width : 300,
			height : 200,
			minWidth : 300,
			minHeight : 200,
			closable : true,
			layout : 'fit', // window的默认布局
			plain : true, // 颜色透明
			items : form,
			buttons : [{
				text : '保存',
				iconCls :'icon-save',
				handler : function() {
					var form = win._form.form;
					if (!(form.isValid())) {
						Ext.Msg.alert("提示", "请填写正确的内容！");
						return;
					}
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

win._form = form;
win.show();
</script>
