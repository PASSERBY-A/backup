<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import=" org.dom4j.*"%>
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
try {
String id = request.getParameter("id");
int module = Integer.parseInt(request.getParameter("module"));
String type = request.getParameter("type");
String origin = request.getParameter("origin");
FieldInfo field = FieldManager.getFieldById(origin,id);
int oid = -1;
if (field != null)
	oid = field.getOid();
int width=175;

%>

<script defer>

var form = new Ext.form.FormPanel({
    baseCls: 'x-plain',
	method: 'POST',
	autoScroll: true,
    defaultType: 'textfield',
	baseParams: {"fld_applyto": <%=module%>, "fld_oid":<%=oid%>,"fld_origin":'<%=origin%>'},
	errorReader: new Ext.form.XmlErrorReader(),
	labelWidth: 75, // label settings here cascade unless overridden
	url:'<%=Consts.ITSM_HOME%>/configure/field/fieldPost.jsp'
});

var typeCombo = new Ext.form.ComboBox({
		fieldLabel: '类型',
		hiddenName:'fld_type',
		store: new Ext.data.SimpleStore({
			fields: ['id', 'value'],
			data : [
				<%
				Map map = FieldManager.getFieldTypes();
				Object keys[] = map.keySet().toArray();
				for (int i = 0; i < keys.length; i++)
				{
					if (i > 0)
						out.print(",");
					out.print("[\"" + keys[i].toString().substring(5) + "\",\"" +
						map.get(keys[i]).toString() + "\"]");
				}
				%>
				]
		}),
		valueField: 'id',
		displayField:'value',
		typeAhead: true,
		mode: 'local',
		triggerAction: 'all',
		emptyText:'请选择字段类型...',
		selectOnFocus:true,
		width: <%=width%>,
		editable:false,
		forceSelection: true,
		allowBlank:false
	});
	typeCombo.on('select', function (obj, record, index)
		{
			window_fieldView.hide();
			viewField({"id": id, "module": <%=module%>, "type": record.get("id"),"origin":'<%=origin%>'});
		});

form.add(
	typeCombo
<%if (field != null || type != null) {%>
	,

	new Ext.form.TextField({
		fieldLabel: 'ID',
		name: 'fld_id',
		width: <%=width%>,
		allowBlank:false,
		vtype: 'alphanum'
	}),

	new Ext.form.TextField({
		fieldLabel: '名称',
		name: 'fld_name',
		width: <%=width%>,
		allowBlank:false
	})
<%}%>
);

<%
List l = new ArrayList();
if (field != null || type != null)
{
	if (field != null)
		l = field.getAttributes();
	else
		l = ((FieldInfo)(Class.forName(type).newInstance())).getAttributes();
	for (int i = 0; i < l.size(); i++)
	{
		FieldInfo inf = (FieldInfo)l.get(i);
		out.println(inf.getFormCode(width));
		String [] flds = inf.getFormFields();
		for (int j = 0; j < flds.length; j++)
			out.println("form.add(" + flds[j] + ");");
	}
}
%>

form.add(new Ext.form.TextArea({
		fieldLabel: '描述',
		name: 'fld_desc',
		width: <%=width%>,
		allowBlank:true
	}));

	if (window_fieldView)
		window_fieldView.close();
    window_fieldView = new Ext.Window({
        title: '字段信息',
        width: 310,
        height:300,
        minWidth: 300,
        minHeight: 200,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        items: form,

        buttons: [
        <%if (origin.equals("ITSM") && (field == null || (field!=null && !field.isSystem()))) {%>
        {
            text: '确定',
            handler: function() {
				var form = window_fieldView._form.form;
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
						window_fieldView.close();
						ds.reload();
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
        },
        <%}%>
        {
            text: '取消',
            handler: function() {
               window_fieldView.close();
            }
        }]
    });

  window_fieldView.on("close",function(){
		//document.location.reload();
	});

	window_fieldView._form = form;
    window_fieldView.show();


<%
	for (int i = 0; i < l.size(); i++)
	{
		FieldInfo inf = (FieldInfo)l.get(i);
		out.println(inf.getFormValue());
	}
	if (field != null) { %>
	var fld = form.form.findField("fld_id");
	fld.setValue("<%=id%>");
	fld.setReadOnly(true);
	fld = form.form.findField("fld_name");
	fld.setValue("<%=StringUtil.escapeJavaScript(field.getName())%>");
	fld = form.form.findField("fld_type");
	fld.setValue("<%=field.getType()%>");
	fld.setReadOnly(true);
	fld = form.form.findField("fld_desc");
	fld.setValue("<%=StringUtil.escapeJavaScript(field.getDesc())%>");
<% } else if (type != null) {%>
	fld = form.form.findField("fld_type");
	fld.setValue("<%=type%>");
	fld.setReadOnly(true);
<%}


}catch(Exception e){
	e.printStackTrace();
}%>


</script>