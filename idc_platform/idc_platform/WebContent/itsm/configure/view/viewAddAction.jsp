<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
	String name = "";
	if (request.getParameter("name")!=null)
		name = request.getParameter("name");
	String code = "";
	if (request.getParameter("code")!=null)
		code = request.getParameter("code");
%>
<div id='editActionPanel'>
</div>
<script type="text/javascript">
	var actionWindow = null;
	if (actionWindow)
		actionWindow.close();
	var actionPanel = new Ext.Panel({
		layout:'form',
		labelAlign:'right',
		labelWidth:70,
		items:[new Ext.form.TextField({fieldLabel :"动作名称",id:'name',hiddenName:'name'}),
					 new Ext.form.TextArea({fieldLabel :"代码",id:'code',hiddenName:'code',width:300,height:200})]
	});
	actionWindow = new Ext.Window({
      title: '编辑动作',
      width: 450,
      height:350,
      minWidth: 300,
      minHeight: 200,
      layout: 'fit',
      plain:true,
      iconCls :'edit',
      buttonAlign:'center',
      modal:true,
      items: actionPanel,
      buttons: [{
          text: '确定',
          handler: function() {
          	var actionPlant = Ext.data.Record.create([
						   {name: 'name', type: 'string'},
						   {name: 'code', type: 'string'}
					  ]);
					  var name = actionPanel.items.get("name").getEl().dom.value;
					  
					  var code = actionPanel.items.get("code").getEl().dom.value;
					  if (name=="") {
					  	alert("动作名不能为空");
					  	return;
					  }
					  var ap = new actionPlant({
							name: name,
							code: code
						});
						var count = action_ds.getCount();
						var hasExist = false;
						for (var i = 0; i < count; i++) {
							var currName = action_ds.getAt(i).get("name");
							if (currName == name) {
								action_ds.getAt(i).set("code",code);
								hasExist = true;
								break;
							}
						}
						if (!hasExist)
							action_ds.add(ap);
						actionWindow.close();
          }
      },{
          text: '取消',
          handler: function() {
          	actionWindow.close();
          }
      }]
  });
  actionWindow.show();
  
  actionPanel.items.get("name").getEl().dom.value = '<%=name%>';
	actionPanel.items.get("code").getEl().dom.value = '<%=StringUtil.escapeJavaScript(code)%>';
</script>