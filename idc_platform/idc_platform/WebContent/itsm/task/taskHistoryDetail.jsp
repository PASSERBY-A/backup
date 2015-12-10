<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.ci.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ page import="org.apache.velocity.*"%>
<%@ page import="org.apache.velocity.app.*"%>


<div id="task_history_detail">
	<%
	String userId = (String)session.getAttribute("req_user");

	if (userId==null) {
//	userId = ((com.hp.idc.auc.User)com.hp.idc.context.Context.getContext().getAttribute(com.hp.idc.context.Context.USER)).getId();
		userId = "analyis";
	}	
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Expires","0");

	if (request.getParameter("xls") != null)
		response.addHeader("Content-Disposition","attachment;filename=history.xls");

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
		String viewMode = request.getParameter("viewMode");

		String origin = request.getParameter("origin");

		TaskInfo taskInfo = TaskManager.getTaskInfoByOid(origin,taskOid,wfOid,wfVer);
		TaskData taskData = taskInfo.getTaskData(taskDataId);
		boolean xlsMode = (request.getParameter("xls") != null);
		if (taskInfo.getStatus()==TaskInfo.STATUS_FORCE_CLOSE)
			out.println("<div align='right'><font color='red'>强制关闭原因:"+taskInfo.getForceCloseMessage()+"</font></div>");
		WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
		WorkflowData wfData = wfInfo.getVersion(taskInfo.getWfVer());

		if (taskInfo.isShowHisGraphics()){
			if (!"viewAllNode".equals(viewMode)) {
				out.print(printNodeDetail(taskData,wfData, xlsMode,userId));
			} else {
				//int rootFormId = wfData.getNode(rootData.getNodeId()).getFormId();
				TaskData rootData = taskInfo.getRootData();
				//System.out.println("root:"+rootData.getDataId());
				out.print(printNode(rootData,wfData,xlsMode,userId));
			}
		} else {
			NodeInfo nodeInfo = wfData.getNode(taskData.getNodeId());

			String formId = nodeInfo.getFormId();
			FormInfo formInfo = null;
			if (wfData.getForms().containsKey(formId)){
				formInfo = (FormInfo)wfData.getForms().get(formId);
			} else {
				int formOid = 0;
				try {
					formOid = Integer.parseInt(formId);
				} catch(Exception e) {
					e.printStackTrace();
				}
				formInfo = FormManager.getFormByOid(formOid);
			}
			List fieldList = null;
			if (formInfo != null)
				fieldList = formInfo.getFields();
			else {
				fieldList = new ArrayList();
				out.println("<script defer>alert('找不到表单信息,请与管理员联系.');</script>");
			}

			Map context = new HashMap();
			context.put("taskData", taskData);
			context.put("DateTimeUtil", new DateTimeUtil());
			context.put("PersonManager", new PersonManager());
			context.put("taskInfo", taskInfo);
			context.put("StringUtil", new StringUtil());
			context.put("workflowData", wfData);
			context.put("fieldList", fieldList);
			context.put("Integer", new Integer(0));
			context.put("FormManager", new FormManager());
			out.println(TemplateUtil.getHTMLStrFromTemplate("/default/nodeViewForm.html",context));

		}
	%>
</div>
	<%!
	public String printNodeDetail(TaskData taskData,WorkflowData wfData, boolean xlsMode,String loginUser) throws Exception {
		NodeInfo nodeInfo = wfData.getNode(taskData.getNodeId());

		Map context = new HashMap();
		context.put("taskData", taskData);
		context.put("DateTimeUtil", new DateTimeUtil());
		context.put("PersonManager", new PersonManager());
		context.put("FieldManager", new FieldManager());
		context.put("taskInfo", taskData.getOwner());
		context.put("StringUtil", new StringUtil());
		context.put("Integer", new Integer(0));
		context.put("FormManager", new FormManager());
		context.put("nodeInfo", nodeInfo);
		context.put("workflowData", wfData);
		//context.put("request", request);
		context.put("userId", loginUser);
		return TemplateUtil.getHTMLStrFromTemplate("/default/nodeViewForm.html",context);
	}
	public String printNode(TaskData td,WorkflowData wfData, boolean xlsMode,String loginUser) throws Exception {
		StringBuffer ret = new StringBuffer();
		ret.append(printNodeDetail(td,wfData,xlsMode,loginUser));
		List childs = td.getChilds();
		for(int i = 0; i < childs.size(); i++) {
			TaskData td_ = (TaskData)childs.get(i);
			if (td_!=null)
				ret.append(printNode(td_,wfData,xlsMode,loginUser));
		}
		return ret.toString();
	}
%>
