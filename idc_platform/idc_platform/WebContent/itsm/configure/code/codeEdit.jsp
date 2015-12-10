<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.datasource.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
%>

<%
try {
int id = Integer.parseInt(request.getParameter("id"));
int type = Integer.parseInt(request.getParameter("type"));
CodeTypeInfo typeInfo = CIManager.getCodeTypeByOid(type);
CodeInfo code = CIManager.getCodeByOid(id);
int width=175;
%>

<script defer>

var form = new Ext.form.FormPanel({
    baseCls: 'x-plain',
	method: 'POST',
	autoScroll: true,
    defaultType: 'textfield',
	baseParams: {"fld_type_oid": <%=type%>},
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 75, // label settings here cascade unless overridden
	url:'<%=Consts.ITSM_HOME%>/configure/code/codePost.jsp'
});

var fld_parent = new Ext.form.TreeBox({
	fieldLabel:"父代码",
	hiddenName:'fld_parent_oid',
	treeData: [
		<%
		CodeDataSource ds4 = new CodeDataSource();
		ds4.load("" + type);
		out.println(ds4.getData("", "","OID"));
	%>
		],
	emptyText:'请选择...',
	width: <%=width%>
});

form.add(
	new Ext.form.TextField({
		fieldLabel: 'OID',
		name: 'fld_oid',
		width: <%=width%>,
		allowBlank:false
	}),	
	new Ext.form.TextField({
		fieldLabel: 'ID',
		name: 'fld_id',
		width: <%=width%>,
		allowBlank:false
	}),

	new Ext.form.TextField({
		fieldLabel: '名称',
		name: 'fld_name',
		width: <%=width%>,
		allowBlank:false
	}),
	new Ext.form.TextField({
		fieldLabel: '排序',
		name: 'fld_order',
		width: <%=width%>,
		allowBlank:false
	})
	,fld_parent,
	new Ext.form.Checkbox({
		fieldLabel: '启用',
		checked: true,
    labelSeparator: '',
    boxLabel: '',
    name: 'fld_enabled'
	}),
	new Ext.form.Checkbox({
		fieldLabel: '可点击',
		checked: true,
    labelSeparator: '',
    boxLabel: '',
    name: 'fld_clickable'
	}),
	new Ext.form.TextField({
		fieldLabel: '提示信息',
		name: 'fld_alertMsg',
		width: <%=width%>,
		allowBlank:true
	}),
	new Ext.form.TextArea({
		fieldLabel: '描述',
		name: 'fld_desc',
		width: <%=width%>,
		allowBlank:true
	})
);

<%
/*List l = new ArrayList();
	if (code != null)
		l = code.getFormElements();
	else
		l = ((CodeInfo)(typeInfo.getObjectClass().newInstance())).getFormElements();
	for (int i = 0; i < l.size(); i++)
	{
		FieldInfo inf = (FieldInfo)l.get(i);
		out.println(inf.getFormCode(width));
		String [] flds = inf.getFormFields();
		for (int j = 0; j < flds.length; j++)
			out.println("form.add(" + flds[j] + ");");
	}
*/
%>

if (window_codeView)
	window_codeView.close();
window_codeView = new Ext.Window({
	title: '代码信息',
	width: 310,
	height:300,
	minWidth: 300,
	minHeight: 200,
	layout: 'fit',
	plain:true,
	bodyStyle:'padding:5px;',
	items: form,

	buttons: [{
		text: '确定',
		handler: function() {
			var form = window_codeView._form.form;
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
					window_codeView.close();
					treeLoader.load(codeRoot,function(){});
					alert(form.errorReader.xmlData.documentElement.text);
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
		   window_codeView.close();
		}
	}]
});

window_codeView._form = form;
window_codeView.show();
<%
/*	for (int i = 0; i < l.size(); i++)
	{
		FieldInfo inf = (FieldInfo)l.get(i);
		out.println(inf.getFormValue());
	}
*/
%>
var fld = form.form.findField("fld_oid");
fld.setValue("<%=id%>");
fld.setReadOnly(true);
<% 
if (code != null) { %>
	fld = form.form.findField("fld_name");
	fld.setValue("<%=StringUtil.escapeJavaScript(code.getName())%>");
	fld = form.form.findField("fld_desc");
	fld.setValue("<%=StringUtil.escapeJavaScript(code.getDesc())%>");
	fld = form.form.findField("fld_order");
	fld.setValue("<%=code.getOrder()%>");
	fld = form.form.findField("fld_parent_oid");
	fld.setValue("<%=code.getParentOid()%>");
	fld = form.form.findField("fld_id");
	fld.setValue("<%=code.getCodeId()%>");

	fld = form.form.findField("fld_enabled");
	fld.setValue(<%=code.isEnabled() ? "true" : "false"%>);
	fld = form.form.findField("fld_clickable");
	fld.setValue(<%=code.isClickable() ? "true" : "false"%>);
	fld = form.form.findField("fld_alertMsg");
	fld.setValue("<%=StringUtil.escapeJavaScript(code.getAlertMsg())%>");
<% }%>


</script>

<%} catch (Exception ex) { ex.printStackTrace(); } %>