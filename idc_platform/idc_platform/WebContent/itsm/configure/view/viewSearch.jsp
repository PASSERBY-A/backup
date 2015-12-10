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
	int viewOid = -1;

	if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
		viewOid = Integer.parseInt(request.getParameter("viewOid"));
	ViewInfo viewInfo = ViewManager.getViewByOid(viewOid);
	if (viewInfo==null) {
		out.println("<script defer>Ext.MessageBox.alert('错误', '找不到视图信息(id=" + viewOid + "),<br>请与管理员联系.');</script>");
		return;
	}
	List searchList = viewInfo.getSearchCol();
	//if (searchList==null || searchList.size()==0) {
	//	out.println("<script defer>Ext.MessageBox.alert('错误', '找不到视图检索字段信息(id=" + viewOid + "),<br>请与管理员联系.');</script>");
	//	return;
	//}
	List fieldName = new ArrayList();
	String beforeDay = viewInfo.getAttribute("beforeDayNo");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	String defaultBegin = "";
	if (beforeDay != null && !beforeDay.equals("")) {
		long before = (long)24000*3600*Integer.parseInt(beforeDay);
		defaultBegin = sdf.format(new java.util.Date(System.currentTimeMillis()-before));
	}

	String defaultEnd = sdf.format(new java.util.Date(System.currentTimeMillis()));
%>
<div id='viewSearchPanel'>
</div>
<script type="text/javascript">
	var searchWindow = null;
	if (searchWindow)
		searchWindow.close();
	var fld_system_create_time_b = new Ext.form.DateField({name:'fld_system_create_time_b',fieldLabel:'创建时间',format:'Y-m-d',value:'<%=defaultBegin%>'});
	var fld_system_create_time_e = new Ext.form.DateField({name:'fld_system_create_time_e',fieldLabel:'至',anchor:'-20',format:'Y-m-d',value:'<%=defaultEnd%>'});
	<%
	String[] systemFieldArray = new String[2];
	systemFieldArray[0] = "fld_system_create_time";
	systemFieldArray[1] = "com.hp.idc.itsm.configure.fields.DateFieldInfo";
	fieldName.add(systemFieldArray);
	
	for (int i = 0;i < searchList.size(); i++) {
		ViewSearchInfo col = (ViewSearchInfo)searchList.get(i);
		if (col!=null) {
			FieldInfo fInfo = FieldManager.getFieldById(col.getNameEN());
			fInfo.setName(col.getNameZH());
			if (fInfo==null)
				continue;
			if (fInfo.getType().equals("com.hp.idc.itsm.configure.fields.DateFieldInfo") ||
				fInfo.getType().equals("com.hp.idc.itsm.configure.fields.DateTimeFieldInfo")) {
	%>
	var fld_<%=col.getNameEN()%>_b = new Ext.form.DateField({name:'fld_<%=col.getNameEN()%>_b',fieldLabel:'<%=col.getNameZH()%>',format:'Y-m-d'});
	var fld_<%=col.getNameEN()%>_e = new Ext.form.DateField({name:'fld_<%=col.getNameEN()%>_e',fieldLabel:'至',anchor:'-20',format:'Y-m-d'});
	<%
			} else {
				out.print(fInfo.getFormCode(150));
				out.print("fld_"+col.getNameEN()+".anchor='-20';");
			}
			String[] fieldArray = new String[2];
			fieldArray[0] = "fld_"+col.getNameEN();
			fieldArray[1] = fInfo.getType();
			fieldName.add(fieldArray);
		}
	}
	%>
var searchPanel = new Ext.FormPanel({
	layout:'form',
	labelAlign:'right',
	labelWidth: 60,
	items:[
		
	<%
		boolean hasR = false;
		for (int i = 0;i < fieldName.size(); i++) {
			String[] col = (String[])fieldName.get(i);
			if (hasR)
				out.print(",");
			if (col[1].equals("com.hp.idc.itsm.configure.fields.DateFieldInfo") ||
				col[1].equals("com.hp.idc.itsm.configure.fields.DateTimeFieldInfo")) {
	%>
	{
		layout : 'column',
		border : false,
		anchor : '100%',
		items : [{
			columnWidth:.5,
			layout: 'form',
			border:false,
			items:[<%=col[0]+"_b"%>]
		}, {
			columnWidth:.5,
			layout: 'form',
			border:false,
			labelWidth:30,
			items:[<%=col[0]+"_e"%>]
		}]
	}
	<%
			} else {
				out.print(col[0]);
			}

			hasR = true;
		}
	%>
	]
});
	searchWindow = new Ext.Window({
    title: '检索对话框',
    width: 350,
    height:300,
    minWidth: 350,
    minHeight: 200,
    layout: 'fit',
    plain:true,
    iconCls :'edit',
    buttonAlign:'center',
    modal:true,
    items: searchPanel,
    buttons: [{
      text: '检索',
      handler: function() {

		<%
			for (int i = 0;i < fieldName.size(); i++) {
				String[] col = (String[])fieldName.get(i);
				if (col[1].equals("com.hp.idc.itsm.configure.fields.DateFieldInfo") ||
				col[1].equals("com.hp.idc.itsm.configure.fields.DateTimeFieldInfo")) {
		%>
		viewds.baseParams.<%=col[0]%>_b = searchPanel.form.findField("<%=col[0]%>_b").getEl().dom.value;
		viewds.baseParams.<%=col[0]%>_e = searchPanel.form.findField("<%=col[0]%>_e").getEl().dom.value;
		<%
				} else if (col[1].equals("com.hp.idc.itsm.configure.fields.SelectFieldInfo")||
					col[1].equals("com.hp.idc.itsm.configure.fields.PersonFieldInfo")){
		%>
			viewds.baseParams.<%=col[0]%> = searchPanel.form.findField("<%=col[0]%>").getValue();
		<%
				} else {
		%>
			viewds.baseParams.<%=col[0]%> = searchPanel.form.findField("<%=col[0]%>").getEl().dom.value;
		<%
				}
			}
		%>
		viewds.reload({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});
		searchWindow.hide();
      }
    },{
    	text:'取消',
    	handler: function() {
    		searchWindow.hide();
    	}
    }]
  });
  searchWindow.show();
</script>