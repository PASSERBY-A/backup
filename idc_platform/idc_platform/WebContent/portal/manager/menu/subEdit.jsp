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
	data : [[1,'�ͻ�����'],[2,'ҵ����Ӫ'],[3,'��Դ����'],[4,'ͳ�Ʊ���'],[5,'ϵͳ����']]
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
				fieldLabel : 'ģ������',
				name : 'title',
				allowBlank : false,
				anchor:'-20',
				value:'<%=bean.getTitle()%>'
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
				value:'<%=bean.getUrl()%>',
				anchor:'-20'
			},{
				xtype : 'textfield',
				fieldLabel : '��ʾ��Ϣ',
				name : 'alt',
				value:'<%=bean.getAlt()%>',
				anchor:'-20'
			},{
				xtype : 'selDlgfield',
				fieldLabel : '�˵���Ȩ',
				selectUrl : '../common/viewRuleSelect.jsp',
				name : 'role',
				value:'<%=StringUtil.removeNull(bean.getRole())%>',
				anchor:'-10'
			}]
});

if (win)
	win.hide();
var win = new Ext.Window({
			title : '�༭ģ��',
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
