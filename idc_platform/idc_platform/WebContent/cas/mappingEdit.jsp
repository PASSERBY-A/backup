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
        fieldLabel: '本地用户',
        name: 'user',
        value:'<%=personId%>',
        readOnly:true,
        allowBlank:false
    },{
        fieldLabel: '第三方系统',
        name: 'thirdSystem',
        allowBlank:false
    },{
        fieldLabel: '第三方用户',
        name: 'thirdUserId',
        allowBlank:false
    }]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '字段信息',
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
							userMappingDS.reload();
						},
						failure: function(form, action)
						{
							form.items.each(function(f){
								   if (f.old_status_disabled)
									   f.disable();
								});
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
</script>
