<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.bean.Document"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	String oid = request.getParameter("oid");
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	Document d = mgr.getBeanById(oid);
%>
<script type="text/javascript">
var role = new Ext.form.SelectDialogField({
			fieldLabel : "权限范围",
			hiddenName : 'role',
			selectUrl : '../common/viewRuleSelect.jsp',
			value :'<%=d.getRole()%>',
			params : {
				singleMode : false,
				child : 1,
				groupType : 3,
				pathMode : true,
				selectGroup : 0
			},
			emptyText : '请选择管理员',
			width : 280
		})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {oid:'<%=oid%>',action:'update'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 80,
	items : [{
				xtype : 'textfield',
				fieldLabel : '文件名称',
				name : 'title',
				allowBlank : false,
				readOnly:true,
				value : '<%=d.getName()%>',
				width : 280
			}, role]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '共享文档',
			border : false,
			modal : true,
			width : 400,
			height : 160,
			minWidth : 400,
			minHeight : 160,
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
