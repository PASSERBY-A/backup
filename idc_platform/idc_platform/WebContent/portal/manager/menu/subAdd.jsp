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
				fieldLabel : 'ģ������',
				name : 'title',
				allowBlank : false,
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : 'ģ������',
				name : 'typeShow',
				readOnly :true,
				value:'<%=mgr.getBeanById(type).getTitle()%>',
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : 'ģ��URL',
				name : 'url',
				allowBlank : false,
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '��ʾ��Ϣ',
				name : 'alt',
				anchor:'-20'
			},{
				xtype : 'selDlgfield',
				fieldLabel : '�˵���Ȩ',
				selectUrl : '../common/viewRuleSelect.jsp',
				name : 'role',
				anchor:'-10'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '����ģ��',
			border : false,
			modal : true,
			width : 400,
			height : 300,
			minWidth : 400,
			minHeight : 300,
			closable : true,
			layout : 'fit', // window��Ĭ�ϲ���
			plain : true, // ��ɫ͸��
			items : form,
			buttons : [{
				text : '����',
				iconCls :'icon-save',
				handler : function() {
					var form = win._form.form;
					if (!(form.isValid())) {
						Ext.Msg.alert("��ʾ", "����д��ȷ�����ݣ�");
						return;
					}
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

win._form = form;
win.show();
</script>
