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
			fieldLabel : "Ȩ�޷�Χ",
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
			emptyText : '��ѡ�����Ա',
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
				fieldLabel : '�ļ�����',
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
			title : '�����ĵ�',
			border : false,
			modal : true,
			width : 400,
			height : 160,
			minWidth : 400,
			minHeight : 160,
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
