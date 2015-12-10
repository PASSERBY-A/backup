<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.impl.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.task.listener.*"%>
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
		int taskDataId = Integer.parseInt((String)map.get("taskDataId"));
		int taskOid = Integer.parseInt((String)map.get("taskOid"));
		int formOid = Integer.parseInt((String)map.get("formOid"));
		int masterWfOid = Integer.parseInt((String)map.get("masterWfOid"));
		int masterWfVer = Integer.parseInt((String)map.get("masterWfVer"));
		int masterTaskOid = Integer.parseInt((String)map.get("masterTaskOid"));
		
		String toNodePath = (String)map.get("toNodeId");
		String fromNodeId = (String)map.get("fromNodeId");
		String subWFOid = (String)map.get("subWFOid");
		String executeUser = request.getParameter(Consts.FLD_PREFIX + Consts.FLD_EXECUTE_USER);
		executeUser = executeUser == null ? userId : executeUser;
		executeUser = executeUser.trim().equals("") ? userId : executeUser;
		Map m1 = new HashMap();
		List l = new ArrayList();

		FormInfo fm = FormManager.getFormByOid(formOid);
		l = fm.getFields();

		for (int i = 0; i < l.size(); i++)
		{
			FieldInfo field = (FieldInfo)l.get(i);
			m1.put(field.getId(), field.getRequestValue(map));
		}
		m1.put("masterTaskOid",masterTaskOid+"");
		m1.put("masterWfOid",masterWfOid+"");
		m1.put("masterWfVer",masterWfVer+"");
		m1.put("_fromNode",fromNodeId);
		m1.put("_subWFOid",subWFOid);
		ITSMTaskManagerImpl taskManager = new ITSMTaskManagerImpl();
		m1.put("_sys_linkTaskStr", "ITSM_"+masterWfOid+"_"+masterWfVer+"_"+masterTaskOid);
	
		int referTaskOid = taskManager.updateTask(-1,wfOid,m1,-1,"","",executeUser,userId,0);
		
		//开始双向关联工单
		TaskInfo ti = taskManager.getTaskInfoByOid(masterTaskOid,masterWfOid,masterWfVer);
		String linktask = ti.getLinkedTaskStr();
		if (linktask!=null && !linktask.equals(""))
			linktask += ",";
		WorkflowInfo wfi_ = WorkflowManager.getWorkflowByOid(wfOid);
		linktask += "ITSM_"+wfOid+"_"+wfi_.getCurrentVersionId()+"_"+referTaskOid;
		ti.setLinkedTaskStr(linktask);
		
		DBListener dbListener = new DBListener();
		dbListener.updateTaskInfo(ti,userId);
		
	} catch (Exception e) {
		e.printStackTrace();
		or.setSuccess(false);
		if (e.toString().indexOf("\n")!=-1)
			or.setMessage(StringUtil.escapeJavaScript(e.toString()));
		else
			or.setMessage(e.toString());
	}
%>
<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>
