<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%
int viewOid = -1;

if (request.getParameter("viewOid")!=null && !request.getParameter("viewOid").equals(""))
	viewOid = Integer.parseInt(request.getParameter("viewOid"));

%>

<div id='viewSearchPanel'>
</div>
<script type="text/javascript">
var wfCombox = new Ext.form.ComboBox({
    fieldLabel: '流程',
    hiddenName:'fld_task_wf_name',
    store: new Ext.data.SimpleStore({
        fields: ['value', 'name'],
        data : [
        	<%
	        	List wfList = WorkflowManager.getWorkflows(true);
	        	int wfCount = wfList.size();
	        	for (int i = 0; i < wfCount; i++) {
	        		WorkflowInfo wfInfo  = (WorkflowInfo)wfList.get(i);
	        		if (i>0)
	        			out.print(",");
	        		out.print("['"+wfInfo.getOid()+"','"+wfInfo.getName()+"']");

	        	}
	        %>
        ]
    }),
    width:180,
    mode: 'local',
    triggerAction: 'all',
    editable:false,
    displayField:'name',
    valueField: 'value',
    allowBlank: false
});
wfCombox.on("change",function(f,nv,ov){
	wfStatusDs.baseParams.wfOid=nv;
	wfStatusDs.reload();
});

var wfStatusDs = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: '<%=Consts.ITSM_HOME%>/configure/workflow/workflowStatusQuery.jsp'
    }),
		baseParams:{'wfOid':'-1','includeAll':'false'},
    reader: new Ext.data.JsonReader({
        root: 'items',
        totalProperty: 'totalCount'
    }, [
    		{name: 'id', mapping: 'id'},
    		{name: 'caption', mapping: 'caption'}
    ])
  });
var statusCombox = new Ext.form.ComboBox({
    fieldLabel: '状态',
    hiddenName:'fld_task_status',
    store: wfStatusDs,
    width:180,
    triggerAction: 'all',
    editable:false,
    displayField:'caption',
    allowBlank:false,
    valueField: 'id'
});

var searchPanel = new Ext.form.FormPanel({
frame: true,
autoHeight: true,
width: 270,
border: false,
layout: 'form',
labelWidth: 50,
labelAlign: 'left',
items: [wfCombox,
		//remove the status, as the history query getting the close tasks
    	<% if(viewOid != 3) {%>
        statusCombox,
        <% } %>
        {xtype: 'textfield', fieldLabel: '<b>OID</b>', width:180, name: 'fld_task_oid'},
        {xtype: 'textfield', fieldLabel: '<b>标题</b>', width:180, name: 'fld_title'},
        {xtype: 'textfield', fieldLabel: '<b>创建人</b>', width:180, name: 'fld_task_create_by'},            
        {html: '创建的时间范围'},
		{xtype: 'datefield', fieldLabel: '<b>由</b>',width:180, name: 'fld_system_create_time_b', format: 'Y-m-d'},
		{xtype: 'datefield', fieldLabel: '<b>至</b>', width:180, name: 'fld_system_create_time_e', format: 'Y-m-d', value: new Date()}
        ]
});


/***************************************/
//Link task Window
/**************************************/
var SearchWin = null;
if(SearchWin) {SearchWin.destroy()}

var SearchWin = new Ext.Window({
title: '检索对话框',
width: 280,
height: 300,
plain:true,
iconCls :'edit',
modal:true,
items: [searchPanel],
buttons: [{text: '检索', handler: search}, {text: '取消', handler: function(){SearchWin.close()}}]
});
SearchWin.show();

function search() {
	viewds.baseParams.fld_system_create_time_b = searchPanel.form.findField("fld_system_create_time_b").getEl().dom.value;
	viewds.baseParams.fld_system_create_time_e = searchPanel.form.findField("fld_system_create_time_e").getEl().dom.value;
	viewds.baseParams.fld_task_wf_name = searchPanel.form.findField("fld_task_wf_name").getEl().dom.value;
	viewds.baseParams.fld_task_oid = searchPanel.form.findField("fld_task_oid").getEl().dom.value;
	viewds.baseParams.fld_title = searchPanel.form.findField("fld_title").getEl().dom.value;
	<% if(viewOid != 3) {%>
	viewds.baseParams.fld_task_status = searchPanel.form.findField("fld_task_status").getEl().dom.value;
    <% } %>
	viewds.baseParams.fld_task_create_by = searchPanel.form.findField("fld_task_create_by").getValue();
	viewds.reload({params:{start:0, limit:<%=Consts.ITEMS_PER_PAGE%>}});
	SearchWin.close();
}
</script>