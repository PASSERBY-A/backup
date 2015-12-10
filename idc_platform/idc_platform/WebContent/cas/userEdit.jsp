<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<%
	String personId = request.getParameter("userId");
	personId = personId==null?"":personId;
 	PersonInfo pInfo = PersonManager.getPersonById(personId);

	String subType = request.getParameter("subType");
%>
<script>

var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
    	'postType':'user',
    	'subType':'<%=subType%>'
    	<%
    		if (pInfo!=null)
    			out.print(",'id':'"+personId+"'");
    	%>
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'casAucPost.jsp',
		items:[{
        fieldLabel: 'ID',
        regex: /^[A-Za-z]\d*/,
        regexText:'ֻ������ĸ�����ֺ��»������, ��ֻ������ĸ��ͷ',
        name: 'id',
        allowBlank:false
    },{
        fieldLabel: '����',
        name: 'name',
        allowBlank:false
    },{
        fieldLabel: '�ֻ���',
        name: 'mobile',
        allowBlank:true
    },{
        fieldLabel: 'E-Mail',
        name: 'email',
        allowBlank:true
    },new Ext.form.ComboBox({
    	fieldLabel: '��Ա״̬',
	    hiddenName:'p_status',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : PSStore
	    }),
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'��ѡ��......',
	    selectOnFocus:true,
	    width:100,
	    forceSelection: true,
	    editable:false,
			allowBlank:false
    }),new Ext.form.ComboBox({
    	fieldLabel: '״̬',
	    hiddenName:'status',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : statusStore
	    }),
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'��ѡ��......',
	    selectOnFocus:true,
	    width:100,
	    forceSelection: true,
	    editable:false,
			allowBlank:false
    })]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '�ֶ���Ϣ',
    width: 310,
    height:300,
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
							userDS.reload();
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

  <%
 		if (pInfo!=null) {
 %>
 userForm.form.findField("id").setValue("<%=pInfo.getId()%>");
 userForm.form.findField("id").disable();
 userForm.form.findField("name").setValue("<%=pInfo.getName()%>");
 userForm.form.findField("mobile").setValue("<%=pInfo.getMobile()%>");
 userForm.form.findField("email").setValue("<%=pInfo.getEmail()%>");
 userForm.form.findField("status").setValue("<%=pInfo.getStatus()%>");
 userForm.form.findField("p_status").setValue("<%=pInfo.getPersonStatus()%>");
 <%
 		}
 %>
</script>
