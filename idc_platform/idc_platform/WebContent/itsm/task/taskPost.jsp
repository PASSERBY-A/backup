<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.dbo.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.impl.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.message.*"%>
<%@ include file="../getUser.jsp"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>
<%
	Object c[] = request.getParameterMap().keySet().toArray();
	//System.out.println("-");
	String origin = request.getParameter("origin");
	Map map = new HashMap();
	for (int i = 0; i < c.length; i++)
	{

		String value = request.getParameter(c[i].toString());
		if (value==null)
			value = "";
		//System.out.println("" + c[i].toString() + ":" + value.toString());
		map.put(c[i].toString(), XmlUtil.escapeUnformChar(value.toString(), "?"));
	}
	//System.out.println("-");
	OperationResult or = new OperationResult();
	int wfOid = Integer.parseInt((String)map.get("wfOid"));
	String operType = (String)map.get("operType");
	try {
		if ("acceptTask".equals(operType))
		{
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			TaskManager.acceptTask(origin,wfOid,taskOid,taskDataId,userId);
		}
		else if ("message".equals(operType))
		{
			ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			String msg = (String)map.get("msg");
			itmi.addTaskMessage(wfOid,taskOid, taskDataId, msg, userId);
			
		}
		else if ("readApply".equals(operType))
		{
			ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			String msg = (String)map.get("msg");
			itmi.readApply(wfOid,taskOid, taskDataId, msg, userId);
		}
		else if ("rollback".equals(operType))
		{
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			String msg = (String)map.get("msg");
			TaskManager.rollbackTask(origin,wfOid,taskOid, taskDataId, msg, userId);
		}
		else if ("forceClose".equals(operType))
		{
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			String msg = (String)map.get("msg");
			TaskManager.forceCloseTask(origin,wfOid,taskOid, msg, userId);
		} 
		else if ("forceCloseBranch".equals(operType))
		{
			ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			int branchTaskDataId = Integer.parseInt((String)map.get("branchTaskDataId"));
			String msg = (String)map.get("msg");
			itmi.forceCloseTaskData(wfOid,taskOid,taskDataId,branchTaskDataId,msg,userId);
		}
		else if ("removeChild".equals(operType))
		{
			ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			int branchTaskDataId = Integer.parseInt((String)map.get("branchTaskDataId"));
			itmi.removeTaskData(wfOid,taskOid,taskDataId,branchTaskDataId,userId);
		}
		else if ("sendSms".equals(operType))
		{
			String sendTo = (String)map.get("fld_notice_to");
			String msg = (String)map.get("fld_notice_message");
			String[] sts = sendTo.split(",");
			for (int i = 0; i < sts.length; i++) {
				MessageManager.sendSms(msg,sts[i], userId ,new Date(),userId);
			}
		}
		else if ("edit".equals(operType))
		{
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			String actionId = (String)map.get("actionId");
			int formOid = Integer.parseInt((String)map.get("formOid"));
			String toNodePath = (String)map.get("toNodeId");
			String fromNodeId = (String)map.get("fromNodeId");
			String subWFOid = (String)map.get("subWFOid");
			String executeUser = request.getParameter(Consts.FLD_PREFIX + Consts.FLD_EXECUTE_USER);
			executeUser = executeUser == null ? userId : executeUser;
			executeUser = executeUser.trim().equals("") ? userId : executeUser;
			Map m1 = new HashMap();
			List l = new ArrayList();
			/*//为了重建转发表单，现在流程配置里增加了转发表单的配置
			if (fromNodeId.equals(toNodeId)) {
				FieldInfo p_ = FieldManager.getFieldById(Consts.FLD_EXECUTE_USER);
				l.add(p_);
				FieldInfo r_ = FieldManager.getFieldById(Consts.FLD_MESSAGE);
				l.add(r_);
			} else {*/
				FormInfo fm = FormManager.getFormByOid(formOid);
				l = fm.getFields();
			//}
			for (int i = 0; i < l.size(); i++)
			{
				FieldInfo field = (FieldInfo)l.get(i);
				m1.put(field.getId(), field.getRequestValue(map));
			}
			m1.put("_fromNode",fromNodeId);
			m1.put("_subWFOid",subWFOid);
			m1.put("_sys_linkTaskStr",map.get("sys_linkTaskStr"));

			//如果保存为草稿，不派发，则closeBranchNode=TaskUpdateInfo.TYPE_SAVE_FOR_EDIT
			int closeBranchNode = Integer.parseInt((String)map.get("closeBranchNode"));
			if (closeBranchNode == 1) {
				operType = "closeBranch";
				closeBranchNode = 0;//0是正常流转，1是回退标识
			}
			
			TaskManager.updateTask(origin,taskOid, wfOid, m1, taskDataId, toNodePath,actionId, executeUser, userId,closeBranchNode);

			//可以增加自定义的返回信息
			if(m1.get("$$response.text") != null)
				or.setMessage((String)m1.get("$$response.text"));
		} else if ("closeEdit".equals(operType)) {
			
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			int wfVer = Integer.parseInt((String)map.get("wfVer"));
			String toNodePath = (String)map.get("toNodeId");
			
			TaskInfo taskInfo = TaskManager.getTaskInfoByOid(origin, taskOid, wfOid, wfVer);
			TaskData taskData = taskInfo.getTaskData(taskDataId);
			taskInfo.setStatus(0);
			taskData.setStatus(0);
			
			TaskEvent event = new TaskEvent(this, taskInfo, taskData.getDataId(), false, userId, TaskUpdateInfo.TYPE_NORMAL);
			new ITSMTaskManagerImpl().getTaskFactory().publishEvent(event);
			TaskManager.updateTask(origin,taskOid, wfOid, new HashMap(), taskDataId, toNodePath, "", userId, userId,TaskUpdateInfo.TYPE_NORMAL);
		}

		if ("closeBranch".equals(operType))
		{
			int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
			int taskOid = Integer.parseInt((String)map.get("taskOid"));
			TaskManager.closeTaskData(origin,wfOid,taskOid,taskDataId,userId);
		}
	}
	catch (Exception e)
	{
		e.printStackTrace();

		or.setSuccess(false);
		if (e.toString().indexOf("\n")!=-1)
			or.setMessage(StringUtil.escapeJavaScript(e.toString()));
		else
			or.setMessage(e.toString());
		System.out.println("taskPost.jsp--"+or.getMessage());
	}
%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>
