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
	String userId = request.getParameter("userId");
	userId = (userId==null?"":userId);
	FactorInfo fi = Cache.Factors.get(userId);
%>
<script>

var fld_factors = new Ext.form.TreeBox({
	fieldLabel:"������",
	singleMode: false,
	hiddenName:'fld_factors',
	viewLoader: new Ext.tree.FilterTreeLoader( {
		baseParams: {child:1, type:3,origin:'',showPerson:true},
		dataUrl:'<%=Consts.ITSM_HOME%>/ci/ciPersonTree.jsp'
	}),
	emptyText:'��ѡ��...',desc:'',width:150
});
var fld_sendSMS = new Ext.form.Checkbox({
	name: 'fld_sendSMS',
	fieldLabel: '�Ƿ񷢶���',
	allowBlank: false
});
var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
	method: 'POST',
	autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
    	<%
    		if (!userId.equals(""))
    			out.print("'userId':'"+userId+"'");
    	%>
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'<%=Consts.ITSM_HOME%>/configure/factorPost.jsp',
		items:[{
        fieldLabel: 'ID',
        name: 'userId',
        readOnly:true,
        allowBlank:false
    },fld_factors,
    fld_sendSMS,
    new Ext.form.Label({
    	html:"<b>˵����</b>ͨ�����ô����ˣ������������޷���½ϵͳ������������£��ô�����ԱΪ�����죬�����ͬʱϵͳ���Զ�������֪ͨ����<br>ѡ�з��Ͷ���ѡ�������˿��յ������������"
    })
    ]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '���ô�����',
    width: 310,
    height:250,
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
				success: function(form, action) {
					Ext.MessageBox.alert("��Ϣ", form.errorReader.xmlData.documentElement.text);
					window_dialog.hide();
				},
				failure: function(form, action) {
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
userForm.form.findField("userId").setValue("<%=userId%>");
<%
if (fi != null) {
	out.println("fld_factors.setValue(\""+fi.getFactors()+"\");");
	out.println("fld_sendSMS.setValue("+fi.isSendSMS()+");");
}
%>

</script>
