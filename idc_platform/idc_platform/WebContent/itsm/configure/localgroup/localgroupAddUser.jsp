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
	
%>
<script>
	<%
		PersonFieldInfo pfInfo = new PersonFieldInfo();
		pfInfo.setId("lgUser");
		pfInfo.setName("人员");
		pfInfo.setGroupByOrganization();
		pfInfo.setSingleMode(false);
		pfInfo.setAllowSelectGroup(false);
		pfInfo.parse();
		out.print(pfInfo.getFormCode(150));
	%>
var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
			type:'addRelations'
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'<%=Consts.ITSM_HOME%>/configure/localgroup/localgroupPost.jsp',
		items:[{
		    fieldLabel: 'ID',
		    name: 'id',
		    allowBlank:false
    	},fld_lgUser
    ]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '本地工作组配置',
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
    	text: '确定',
    	handler: function(){
    		var form = userForm.form;
      	if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "请填写完整的内容");
					return;
				}

				form.submit({
						waitMsg: '正在处理，请稍候...',
						success: function(form, action)
						{
							Ext.MessageBox.alert("信息", form.errorReader.xmlData.documentElement.text);
							window_dialog.hide();
							userds.load();
						},
						failure: function(form, action)
						{
							Ext.MessageBox.alert("失败", form.errorReader.xmlData.documentElement.text);
				    }
				});
    	}
    },{
    	text: '取消',
  	 	handler: function(){
  	 		window_dialog.hide();
  	 	}
    }]

 });
 window_dialog.show();
<%
	if (lgId!=null || !lgId.equals("")) {
%>
	userForm.form.findField("id").setValue("<%=lgId%>");
	userForm.form.findField("id").setReadOnly();
<%}%>
</script>
