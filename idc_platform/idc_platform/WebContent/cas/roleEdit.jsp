<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<%

	String subType = request.getParameter("subType");
	String moId = request.getParameter("moId");
	String roleId = request.getParameter("roleId");
	if (roleId == null)
		roleId = "";
	RoleInfo rInfo = RoleManager.getRole(moId, roleId);
%>
<script>

var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
    	'postType':'role',
    	'subType':'<%=subType%>',
    	'moId':'<%=moId%>'
    	<%
    		if (rInfo!=null)
    			out.print(",'id':'"+roleId+"'");
    	%>
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'casAucPost.jsp',
		items:[{
        fieldLabel: 'ID',
        name: 'id',
        allowBlank:false
    },{
        fieldLabel: '����',
        name: 'name',
        allowBlank:false
    },{
        fieldLabel: '�ȼ�',
        name: 'level',
        allowBlank:true
    },new Ext.form.ComboBox({
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
    title: '��ɫ����',
    width: 270,
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
							userDS.reload();
							roleDs.reload();
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
 		if (rInfo!=null) {
 %>
 userForm.form.findField("id").setValue("<%=rInfo.getId()%>");
 userForm.form.findField("id").disable();
 userForm.form.findField("name").setValue("<%=rInfo.getName()%>");
 userForm.form.findField("status").setValue("<%=rInfo.getStatus()%>");
 userForm.form.findField("level").setValue("<%=rInfo.getLevel()%>");
 <%
 	} else {
 %>
  userForm.form.findField("status").setValue("0");

 <%
 	}
 %>
</script>
