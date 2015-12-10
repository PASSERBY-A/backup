<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>

<%
	String lgId = request.getParameter("lgId");
	lgId = lgId == null ?"" : lgId;
	LocalgroupInfo lgInfo = LocalgroupManager.getGroupById(lgId);
	
	String type = request.getParameter("action");
	type = (type == null || type.equals(""))?"add":type;
%>
<script>
var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
			type:'<%=type%>'
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'<%=Consts.ITSM_HOME%>/configure/localgroup/localgroupPost.jsp',
		items:[{
		    fieldLabel: 'ID',
		    name: 'id',
		    allowBlank:false
    	},{
		    fieldLabel: '����',
		    name: 'name',
		    allowBlank:false
    	},
    	new Ext.form.TreeBox({
		    fieldLabel: 'ָ������',
		    hiddenName:'lg_wf',
		    singleMode:false,
				width:150,
		    emptyText:'��ѡ��...',
		    allowBlank:false,
		    viewLoader: new Ext.tree.FilterTreeLoader({
		    	baseParams: {'includeAll':'false'},
		    	dataUrl:'<%=Consts.ITSM_HOME%>/configure/workflow/workflowQueryForSelect.jsp'
		    })
		  }),
		  new Ext.form.ComboBox({
		    fieldLabel: '״̬',
		    hiddenName:'status',
		    store: new Ext.data.SimpleStore({
		        fields: ['value', 'name'],
		        data : [
		        	['0','����'],['1','��ͣ��']
		        ]
		    }),
		    displayField:'name',
		    valueField: 'value',
		    mode: 'local',
		    triggerAction: 'all',
		    emptyText:'��ѡ��......',
		    selectOnFocus:true,
		    width:150,
		    forceSelection: true,
		    editable:false,
				allowBlank:false
			})
    ]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '���ع���������',
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
							lgds.load();
						},
						failure: function(form, action)
						{
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
	if (lgInfo!=null) {
%>
	userForm.form.findField("id").setValue("<%=lgInfo.getId()%>");
	userForm.form.findField("id").setReadOnly();
	userForm.form.findField("name").setValue("<%=lgInfo.getName()%>");
	userForm.form.findField("lg_wf").setValue("<%=lgInfo.getWfOid()%>");
	userForm.form.findField("status").setValue("<%=lgInfo.getStatus()%>");
<%} else {%>
	userForm.form.findField("status").setValue("0");
<%}%>
</script>
