<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	String type = request.getParameter("type");
	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
%>
<script type="text/javascript">
var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'add',type:'<%=type%>'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 80,
	items : [{
				xtype : 'textfield',
				fieldLabel : '模块名称',
				name : 'title',
				allowBlank : false,
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '模块名称',
				name : 'typeShow',
				readOnly :true,
				value:'<%=mgr.getBeanById(type).getTitle()%>',
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '模块URL',
				name : 'url',
				allowBlank : false,
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '提示信息',
				name : 'alt',
				anchor:'-20'
			},{
				xtype : 'selDlgfield',
				fieldLabel : '菜单授权',
				selectUrl : '../common/viewRuleSelect.jsp',
				name : 'role',
				anchor:'-10'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '新增模块',
			border : false,
			modal : true,
			width : 400,
			height : 300,
			minWidth : 400,
			minHeight : 300,
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
