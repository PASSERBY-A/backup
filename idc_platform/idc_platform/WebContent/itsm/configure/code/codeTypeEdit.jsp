<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<%
int oid = Integer.parseInt(request.getParameter("id"));
String catalogOid = request.getParameter("catalogOid");
CodeTypeInfo codeType = CIManager.getCodeTypeByOid(oid);
int width=175;
%>

<script defer>

var codeTypeForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
	method: 'POST',
	autoScroll: true,
  defaultType: 'textfield',
  baseParams: {"fld_catalog": <%=catalogOid%>},

	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 75, // label settings here cascade unless overridden
	url:'<%=Consts.ITSM_HOME%>/configure/code/codeTypePost.jsp'
});

var fld_enabled = new Ext.form.Checkbox({
		fieldLabel: '启用',
		checked: true,
    labelSeparator: '',
    boxLabel: '',
    name: 'fld_enabled'
	});

codeTypeForm.add(
	new Ext.form.TextField({
		fieldLabel: 'ID',
		name: 'fld_oid',
		width: <%=width%>,
		allowBlank:false
	}),

	new Ext.form.TextField({
		fieldLabel: '名称',
		name: 'fld_name',
		width: <%=width%>,
		allowBlank:false
	}),

	new Ext.form.ComboBox({
		fieldLabel: '类型',
		hiddenName:'fld_type',
		store: new Ext.data.SimpleStore({
			fields: ['id', 'value'],
			data : [
				["0", "列表代码"], 
				["1", "树状代码"]
			]
		}),
		valueField: 'id',
		displayField:'value',
		typeAhead: true,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'请选择...',
		selectOnFocus:true,
		width: <%=width%>,
		editable:false,
		forceSelection: true,
		allowBlank:false
	}),fld_enabled
	
);

	if (window_codeTypeView)
		window_codeTypeView.hide();

  window_codeTypeView = new Ext.Window({
    title: '字段信息',
    width: 310,
    height:300,
    minWidth: 300,
    minHeight: 200,
    layout: 'fit',
    plain:true,
    bodyStyle:'padding:5px;',
    items: codeTypeForm,

    buttons: [{
      text: '确定',
      handler: function() {
				var form = window_codeTypeView._form.form;
				if (!form.isValid()) {
					Ext.MessageBox.alert("提示", "请填写完整的内容");
					return;
				}
				form.items.each(function(f){
				   f.old_status_disabled = f.disabled;
				   f.enable();
				});
				form.submit({
					waitMsg: '<%=Consts.MSG_WAIT%>',
					success: function(form, action) 
					{ 
						form.items.each(function(f){
						   if (f.old_status_disabled)
							   f.disable();
						});
						Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
						window_codeTypeView.hide();
						grid_codeTypeList.getStore().reload();
					},
					failure: function(form, action)
					{
						form.items.each(function(f){
						   if (f.old_status_disabled)
							   f.disable();
						});
						Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_FAILED%>", form.errorReader.xmlData.documentElement.text);
					}
				});
      }
    },{
      text: '取消',
      handler: function() {
         window_codeTypeView.hide();
      }
    }]
  });

	window_codeTypeView._form = codeTypeForm;
  window_codeTypeView.show();

	var fld = codeTypeForm.form.findField("fld_oid");
	fld.setValue("<%=oid%>");
	fld.disable();

<% 
	if (codeType != null) { %>
	fld = codeTypeForm.form.findField("fld_name");
	fld.setValue("<%=StringUtil.escapeJavaScript(codeType.getName())%>");
	fld = codeTypeForm.form.findField("fld_type");
	fld.setValue("<%=codeType.getType()%>");
	fld = codeTypeForm.form.findField("fld_enabled");
	fld.setValue(<%=codeType.isEnabled() ? "true" : "false"%>);
<%}%>


</script>