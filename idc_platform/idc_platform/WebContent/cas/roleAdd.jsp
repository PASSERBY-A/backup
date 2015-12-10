<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<%

	String moId = request.getParameter("moId");
	String personId = request.getParameter("personId");
	if (personId == null)
		personId = "";
	RoleInfo rInfo = RoleManager.getPersonRole(moId, personId);

%>
<script>

var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
    	'postType':'addRelations',
    	'toId':'<%=moId%>',
    	'rType':'userOrgaUpdate',
    	'users':'<%=personId%>'
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		labelAlign:'right',
		url:'casAucPost.jsp',
		items:[new Ext.form.ComboBox({
    	fieldLabel: 'ְ��',
	    hiddenName:'roleId',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [<%
	        	List<RoleInfo> rl = RoleManager.getRolesOfMo(moId);
	        	for (int i = 0; i < rl.size(); i++) {
	        		if (i > 0)
	        		out.print(",");
	        		RoleInfo ri = rl.get(i);
	        		out.println("[");
	        		out.println("'"+ri.getId()+"',");
	        		out.println("'"+ri.getName()+"'");	        		
	        		out.println("]");
	        	}

	        %>]
	    }),
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'��ѡ��......',
	    selectOnFocus:true,
	    width:100,
	    forceSelection: true
	 })]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: 'ְ�����',
    width: 250,
    height:50,
    minWidth: 300,
    minHeight: 50,
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
 		if (rInfo!=null) {
 %>
 userForm.form.findField("roleId").setValue("<%=rInfo.getId()%>");
 <%
 		}
 %>
</script>
