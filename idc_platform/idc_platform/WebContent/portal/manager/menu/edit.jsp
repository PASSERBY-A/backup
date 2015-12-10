<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.bean.Menu"%>
<%@page import="com.hp.idc.portal.mgr.MenuMgr"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="com.hp.idc.portal.util.StringUtil"%>
<%@include file="../../getUser.jsp" %>

<%
	String oid = request.getParameter("oid");
	MenuMgr mgr = (MenuMgr)ContextUtil.getBean("menuMgr");
	Menu bean = mgr.getBeanById(oid);
%>
<script type="text/javascript">

var typeData = new Ext.data.SimpleStore({
	fields : ['id','value'],
	data : [
	    	<%
	    		List<Menu> list = new ArrayList<Menu>();
	    	list = mgr.getBeanByFilter(userId,"-1");
	    	int i = 0;
	    	for(Menu m : list){
	    		if(i>0)
	    			out.write(",");
	    		out.write("["+m.getOid()+",'"+m.getTitle()+"']");
	    		i++;
	    	}
	    	%>
	    	]
})

var form = new Ext.FormPanel({
	bodyStyle : 'padding:4px;',
	frame : true,
	url : 'action.jsp',
	baseParams: {action:'update',oid:'<%=oid%>'}, 
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
	            xtype: 'combo',
	            hiddenName: 'type',
	            fieldLabel : '��������',
	            anchor:'-20',
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
				value:'<%=bean.getType()%>',
				store : typeData
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
			},{
				xtype : 'numberfield',
				fieldLabel : '�����',
				name : 'orderno',
				value:'<%=bean.getOrderNo() %>',
				anchor:'-20',
				allowBlank:false
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
