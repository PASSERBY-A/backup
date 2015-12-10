<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%
	String oid = request.getParameter("oid");
	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
	Menu bean = mgr.getBeanById(oid);
	String type = request.getParameter("type");
%>
<script type="text/javascript">
var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [[1,'客户管理'],[2,'业务运营'],[3,'资源与监控'],[4,'统计报表'],[5,'系统管理']]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'update',oid:'<%=oid%>',type:'<%=type%>'}, 
	method : 'POST',
	autoscroll : true,
	labelWidth : 80,
	items : [{
				xtype : 'textfield',
				fieldLabel : '模块名称',
				name : 'title',
				allowBlank : false,
				anchor:'-20',
				value:'<%=bean.getTitle()%>'
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
				value:'<%=bean.getUrl()%>',
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '提示信息',
				name : 'alt',
				value:'<%=bean.getAlt()%>',
				anchor:'-20'
			},{
				xtype : 'selDlgfield',
				fieldLabel : '菜单授权',
				selectUrl : '../common/viewRuleSelect.jsp',
				name : 'role',
				value:'<%=StringUtil.removeNull(bean.getRole())%>',
				anchor:'-10'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '编辑模块',
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
