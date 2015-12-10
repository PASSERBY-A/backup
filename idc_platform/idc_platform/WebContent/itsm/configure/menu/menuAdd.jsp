<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.authorization.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

String action = request.getParameter("action");
action = (action == null || action.equals(""))?"add":action;
String id = request.getParameter("id");
MenuInfo mInfo = null;
if (action.equals("edit") && id!=null && !id.equals("")){
	mInfo = MenuManager.getMenuInfo(id);
}
%>
<script type="text/javascript">

	//----form begin-----
	var menuForm = new Ext.form.FormPanel({
		method: 'POST',
		labelAlign:'right',
		baseParams: {"operType":"<%=action%>"},
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 95, // label settings here cascade unless overridden
		url:'<%=Consts.ITSM_HOME%>/configure/menu/menuPost.jsp'
	});
	var menu_id = new Ext.form.TextField({
			fieldLabel: 'ID',
			name: 'menu_id',
			width :150,
			allowBlank:false
		});

	var menu_name = new Ext.form.TextField({
			fieldLabel: '名称',
			name: 'menu_name',
			width :150,
			allowBlank:false
		});
		
	var menu_leaf = new Ext.form.Checkbox({
        fieldLabel: '菜单项',
        name: 'menu_leaf',
        allowBlank:true
    });
  var menu_parent = new Ext.form.ComboBox({
	    fieldLabel: '父节点',
	    hiddenName:'menu_parent',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : [
	        <%
					List menus = MenuManager.getAllMenus();
					boolean hasRecord_ = false;
					for (int i = 0; i < menus.size(); i++) {
						MenuInfo mi = (MenuInfo)menus.get(i);
						if (mi.isLeaf())
							continue;
						if (hasRecord_)
							out.print(",");
						out.print("['"+mi.getId()+"','"+mi.getName()+"']");
						hasRecord_ = true;
					}
					%>
	        ]
	    }),
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'请选择......',
	    selectOnFocus:true,
	    width:150,
	    forceSelection: true,
	    editable:false,
			allowBlank:true
		});
	
	var menu_index = new Ext.form.TextField({
      fieldLabel: '优先序号',
      name: 'menu_index',
      width :150,
      value:"-1",
      allowBlank:true
  });

	var menu_rule = new Ext.form.SelectDialogField({
			fieldLabel: '权限',
			name: 'menu_rule',
			width:200,
			allowBlank:true,
			selectUrl:"<%=Consts.ITSM_HOME%>/configure/view/viewRuleSelect.jsp"
		});

	var menuURL = new Ext.form.TextField({
      fieldLabel: '页面地址',
      name: 'menu_url',
      width :300,
      allowBlank:true
  });
  var menuScript = new Ext.form.TextArea({
      fieldLabel: '脚本',
      name: 'menu_script',
      width :300,
      height: 100,
      allowBlank:true
  });

	menuForm.add(menu_id,menu_name,menu_leaf,menu_parent,menu_index,menu_rule,menuURL,menuScript);

	var formWindow = null;
	if (formWindow) {
		formWindow.close();
	}

	formWindow = new Ext.Window({
    title: '编辑视图',
    width: 510,
    height:400,
    minWidth: 300,
    minHeight: 200,
    layout: 'fit',
    plain:true,
    iconCls :'edit',
    buttonAlign:'center',
    modal:true,
    items: menuForm,

    buttons: [{
        text: '确定',
        handler: function() {
					var form = formWindow.menuForm_.form;
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
							Ext.MessageBox.alert("<%=Consts.MSG_BOXTITLE_SUCCESS%>", form.errorReader.xmlData.documentElement.text);
							formWindow.close();
							document.location.reload();
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
          formWindow.close();
        }
    }]
  });
	formWindow.menuForm_ = menuForm;
  formWindow.show();

  <%
  	if (mInfo!=null) {
	%>
		menuForm.form.findField("menu_id").setValue("<%=id%>");
 		menuForm.form.findField("menu_id").disable();

 		menuForm.form.findField("menu_name").setValue("<%=mInfo.getName()%>");
 		menuForm.form.findField("menu_leaf").setValue("<%=mInfo.isLeaf()%>");
 		menuForm.form.findField("menu_parent").setValue("<%=mInfo.getParentId()%>");
 		menuForm.form.findField("menu_rule").setValue("<%=mInfo.getRuleStr()%>");
 		menuForm.form.findField("menu_index").setValue(<%=mInfo.getDisplayIndex()%>);
 		menuForm.form.findField("menu_url").setValue("<%=mInfo.getHref()%>");
 		menuForm.form.findField("menu_script").setValue("<%=StringUtil.escapeJavaScript(mInfo.getScript())%>");
	<%
  	}
  %>
</script>