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
	fieldLabel:"代理人",
	singleMode: false,
	hiddenName:'fld_factors',
	viewLoader: new Ext.tree.FilterTreeLoader( {
		baseParams: {child:1, type:3,origin:'',showPerson:true},
		dataUrl:'<%=Consts.ITSM_HOME%>/ci/ciPersonTree.jsp'
	}),
	emptyText:'请选择...',desc:'',width:150
});
var fld_sendSMS = new Ext.form.Checkbox({
	name: 'fld_sendSMS',
	fieldLabel: '是否发短信',
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
    	html:"<b>说明：</b>通过设置代理人，您可以在您无法登陆系统处理工单的情况下，让代理人员为您代办，代办的同时系统会自动发短信通知您。<br>选中发送短信选项，则代理人可收到工单到达短信"
    })
    ]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '设置代理人',
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
    	text: '确定',
    	handler: function(){
    		var form = userForm.form;
			if (!form.isValid()) {
				Ext.MessageBox.alert("提示", "请填写完整的内容");
				return;
			}

			form.submit({
				waitMsg: '正在处理，请稍候...',
				success: function(form, action) {
					Ext.MessageBox.alert("信息", form.errorReader.xmlData.documentElement.text);
					window_dialog.hide();
				},
				failure: function(form, action) {
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
userForm.form.findField("userId").setValue("<%=userId%>");
<%
if (fi != null) {
	out.println("fld_factors.setValue(\""+fi.getFactors()+"\");");
	out.println("fld_sendSMS.setValue("+fi.isSendSMS()+");");
}
%>

</script>
