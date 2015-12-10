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
				fieldLabel : '��������',
				name : 'name',
				allowBlank : false,
				value:'<%=bean.getName()%>',
				anchor:'-10'
			},{
				xtype : 'textfield',
				fieldLabel : 'ģ��·��',
				name : 'path',
				allowBlank : false,
				value:'<%=bean.getPath()%>',
				anchor:'-10'
			},{
				xtype : 'textfield',
				fieldLabel : '�������',
				name : 'areanum',
				value:'<%=bean.getAreaNum()%>',
				anchor:'-10'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '�༭����',
			border : false,
			modal : true,
			width : 300,
			height : 200,
			minWidth : 300,
			minHeight : 200,
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
