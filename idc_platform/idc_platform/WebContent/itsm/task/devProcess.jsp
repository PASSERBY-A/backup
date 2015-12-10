<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.message.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.impl.ITSMTaskManagerImpl"%>
<%@ include file="../getUser.jsp"%>

<%
int taskOid = -1;
if (request.getParameter("taskOid")!=null && !request.getParameter("taskOid").equals(""))
	taskOid = Integer.parseInt(request.getParameter("taskOid"));
int taskDataId = -1;
if (request.getParameter("taskDataId")!=null && !request.getParameter("taskDataId").equals(""))
	taskDataId = Integer.parseInt(request.getParameter("taskDataId"));

int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
int wfVer = -1;
if (request.getParameter("wfVer")!=null && !request.getParameter("wfVer").equals(""))
	wfVer = Integer.parseInt(request.getParameter("wfVer"));


TaskInfo taskInfo = TaskManager.getTaskInfoByOid("ITSM",taskOid,wfOid,wfVer);
List l = new ArrayList();
TaskData taskData = null;
if(taskDataId == -1){
	taskData = taskInfo.getLastTaskData("node8");	
} else {
	taskData = taskInfo.getTaskData(taskDataId);	
}

if(taskData != null)
	l = taskData.getActivateChilds();

int i = 0;
%>

	(function() {
		var tabs = new Ext.TabPanel({
		    activeTab: 0,
			autoHeight: true,
			frame: false,
			border: false

<% 
if(l.size() > 0){
out.println(", activeTab: 0, items: [");
for (Iterator iterator = l.iterator(); iterator.hasNext();) {
	i++;
	TaskData td = (TaskData) iterator.next();
	if(i>1)
		out.println(",");
%>
	{
				xtype: 'panel',
				frame: true,
				title: '开发过程 - <%=i %>',
				autoHeight: true,
				layout: 'form',
				items :[{
					xtype: 'panel',
					layout : 'column',
					border : false,
					anchor : '100%',
					items : [{
						xtype: 'panel',
						columnWidth : .5,
						layout : 'form',
						border : false,
						bodyStyle : 'padding: 0 30 0 0',
						items : [{
							xtype: 'datefield',
							fieldLabel:"实际开始时间",
							anchor : '100%',
							style : 'border-top:1px solid #99BBE8;border-left:1px solid #99BBE8;border-bottom:1px solid #99BBE8;border-right:1px solid #99BBE8;',
							format : 'Y-m-d',
							allowBlank : false,
							maxValue : new Date(),
							readOnly: true,
							cls: "text-disable",
							value: '<%=td.getValue("req_handler_begin_time") %>'
						}]
					}, {
						xtype: 'panel',
						columnWidth : .5,
						layout : 'form',
						border : false,
						bodyStyle : 'padding: 0 0 0 30',
						items : [{
							xtype: 'datefield',
							fieldLabel:"实际完成时间",
							anchor : '100%',
							style : 'border-top:1px solid #99BBE8;border-left:1px solid #99BBE8;border-bottom:1px solid #99BBE8;border-right:1px solid #99BBE8;',
							format : 'Y-m-d',
							allowBlank : false,
							minValue : new Date(),
							readOnly: true,
							cls: "text-disable",
							value: '<%=td.getValue("req_handler_end_time") %>'
						}]
					}]
				}, 
				{xtype: 'textarea', fieldLabel:"\u5F00\u53D1\u8FC7\u7A0B",anchor : '100%',allowBlank:false,height:48, grow: true, readOnly: true, cls: "text-disable", value: '<%=td.getValue("req_dev_process") %>'}, 
				{xtype: 'textarea', fieldLabel:"\u6D4B\u8BD5\u65B9\u6848",anchor : '100%',height:48,grow: true, readOnly: true, cls: "text-disable", value: '<%=td.getValue("req_test_scheme") %>'},
				{xtype: 'textarea', fieldLabel:"备注",anchor : '100%',height:48,grow: true}]
		    }
<% 
 	
	}
out.println("]");
} %>	

		});
	    return tabs;
	})();

