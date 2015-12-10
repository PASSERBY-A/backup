<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<%
	String personId = request.getParameter("selectUserId");
	String thirdSystem = request.getParameter("thirdSystem");
	String thirdUserId = request.getParameter("thirdUserId");
%>
<script>

var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
    	'postType':'addMapping'
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, 
		url:'casAucPost.jsp',
		items:[{
        fieldLabel: '�����û�',
        name: 'user',
        value:'<%=personId%>',
        readOnly:true,
        allowBlank:false
    },{
        fieldLabel: '������ϵͳ',
        name: 'thirdSystem',
        allowBlank:false
    },{
        fieldLabel: '�������û�',
        name: 'thirdUserId',
        allowBlank:false
    }]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '�ֶ���Ϣ',
    width: 310,
    height:200,
    minWidth: 300,
    minHeight: 200,
    layout: 'fit',
    plain:true,
    modal:true,
    bodyStyle:'padding:5px;',
    items: userForm,
    buttons:[{
    	text: 'ȷ��',
    	handler: function(){
    		var form = userForm.form;
      	if (!form.isValid()) {
					Ext.MessageBox.alert("��ʾ", "����д����������");
					return;
				}

				form.submit({
						waitMsg: '���ڴ������Ժ�...',
						success: function(form, action)
						{
							Ext.MessageBox.alert("��Ϣ", form.errorReader.xmlData.documentElement.text);

							window_dialog.hide();
							userMappingDS.reload();
						},
						failure: function(form, action)
						{
							form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});
							Ext.MessageBox.alert("ʧ��", form.errorReader.xmlData.documentElement.text);
				    }
				});
    	}
    },{
    	text: 'ȡ��',
  	 	handler: function(){
  	 		window_dialog.hide();
  	 	}
    }]
 });
 window_dialog.show();
</script>
