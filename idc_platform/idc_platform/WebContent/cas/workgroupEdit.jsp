<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>

<%
	String wgId = request.getParameter("wgId");
	wgId = wgId==null?"":wgId;
 	WorkgroupInfo oInfo = WorkgroupManager.getWorkgroupById(wgId);

 	String subType = request.getParameter("subType");
	String parentId = request.getParameter("parentId");
	parentId = parentId==null?"-1":parentId;
	String title=oInfo==null?"新增工作组":"编辑工作组";
%>
<script>
var userForm = new Ext.form.FormPanel({
    baseCls: 'x-plain',
		method: 'POST',
		autoScroll: true,
    defaultType: 'textfield',
    baseParams:{
    	'postType':'workgroup',
    	'subType':'<%=subType%>'
    	<%
    		if (oInfo!=null)
    			out.print(",'id':'"+wgId+"'");
    	%>
    },
		errorReader: new Ext.form.XmlErrorReader(),
		labelWidth: 75, // label settings here cascade unless overridden
		url:'casAucPost.jsp',
		items:[{
        fieldLabel: 'ID',
        name: 'id',
        regex: /^\w+$/,
        regexText:'只能由字母、数字和下划线组成',
        allowBlank:false
    },{
        fieldLabel: '名称',
        name: 'name',
        allowBlank:false
    },new Ext.form.ComboBox({
    	fieldLabel: '状态',
	    hiddenName:'status',
	    store: new Ext.data.SimpleStore({
	        fields: ['value', 'name'],
	        data : statusStore
	    }),
	    displayField:'name',
	    valueField: 'value',
	    typeAhead: true,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText:'请选择......',
	    selectOnFocus:true,
	    width:100,
	    forceSelection: true,
	    editable:false,
			allowBlank:false
    }),new Ext.form.TreeBox({
			fieldLabel:"父对象",
			singleMode: true,
			hiddenName:'parentId',
			viewLoader: new Ext.tree.FilterTreeLoader({
				dataUrl:'workgroupTreeQuery.jsp?showDel=false'
			}),
			emptyText:'请选择...',
			desc:'',
			width:150
		})]
});

var window_dialog;
if (window_dialog)
	window_dialog.close();
window_dialog = new Ext.Window({
    title: '<%=title%>',
    width: 310,
    height:200,
    minWidth: 300,
    minHeight: 150,
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
							treePanel.loader.load(treePanel.root);
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

 <%
 		if (oInfo!=null) {
 %>
 userForm.form.findField("id").setValue("<%=oInfo.getId()%>");
 userForm.form.findField("id").disable();
 userForm.form.findField("name").setValue("<%=oInfo.getName()%>");
 userForm.form.findField("parentId").setValue("<%=oInfo.getParentId()%>");
 userForm.form.findField("status").setValue("<%=oInfo.getStatus()%>");
 <%
 		}else {
 %>
 userForm.form.findField("parentId").setValue("<%=parentId%>");
<%}%>
</script>
