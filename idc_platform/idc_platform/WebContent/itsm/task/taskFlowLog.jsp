<%@ page language="java" pageEncoding="gbk" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="org.dom4j.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.inter.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.common.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>

<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

int taskOid = -1;
if (request.getParameter("taskOid")!=null && !request.getParameter("taskOid").equals(""))
	taskOid = Integer.parseInt(request.getParameter("taskOid"));
int wfOid = -1;
if (request.getParameter("wfOid")!=null && !request.getParameter("wfOid").equals(""))
	wfOid = Integer.parseInt(request.getParameter("wfOid"));
int wfVer = -1;
if (request.getParameter("wfVer")!=null && !request.getParameter("wfVer").equals(""))
	wfVer = Integer.parseInt(request.getParameter("wfVer"));
	
String origin = request.getParameter("origin");

TaskInfo taskInfo = TaskManager.getTaskInfoByOid(origin, taskOid, wfOid, wfVer);
WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
WorkflowData wfData = wfInfo.getVersion(taskInfo.getWfVer());

%>
<br/>
<table id='flow-table' class='embed2' border=0 cellspacing=1 width='99.5%' align='center'>

<thead>
<tr>
<!-- �ڵ����� ������ ����ʱ�� ����ʱ�� ����ʱ�� �Ƿ�ʱ ������־ ��һ�ڵ� ��һ������ -->
<th>�ڵ�����</th><th>������</th><th>����ʱ�� </th><th>����ʱ��</th><th>�Ƿ�ʱ</th><th>������־</th><th>��һ�ڵ�</th><th>��һ������</th><th>����</th>
</tr>
</thead>
<tbody>
<%
StringBuffer sb = new StringBuffer();
for(int i=0; i<taskInfo.getLatestTaskDataId()+1;i++) {
	TaskData td = taskInfo.getTaskData(i);
	String nodeId = td.getNodeId();
	String operLog = "�����ύ��������һ�ڵ�";
	if(td.isRollback())
		operLog = "<font color=red>���˹�������һ�ڵ�</font>";
	
	List<TaskData> childs = td.getChilds();	
	TaskData ntd = null;
	if(childs.size() == 1)
		ntd = childs.get(0);
	String buId = "node-detail-"+i;
	if(td.isBranchEnd() || td.getNodeId().equals("node1"))
		continue;
	sb.append("<tr><td width=110>");
	sb.append(td.getNodeDesc());
	sb.append("</td><td width=75 >");
	sb.append(PersonManager.getPersonNameById(td.getAssignTo()));
	sb.append("</td><td width=160>");
	sb.append(DateTimeUtil.formatDate(td.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
	sb.append("</td><td width=160>");
	sb.append(DateTimeUtil.formatDate(td.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
	sb.append("</td><td width=70>");
	sb.append(td.isOvertime()?"<font color=red>��</font>":"<font color=green>��</font>");
	sb.append("</td><td width=200>");
	sb.append(operLog);
	sb.append("</td><td width=120>");
	sb.append(td.isRollback()?"<font color=red>�ѻ���</font>":ntd==null?(td.isWait()==true?"<font color=red>�ȴ�����</font>":(td.isBranchBegin()?"":"<font color=green>���ڴ���</font>")):ntd.getNodeDesc());
	sb.append("</td><td width=75>");
	sb.append(ntd==null?"":PersonManager.getPersonNameById(ntd.getAssignTo()));
	sb.append("</td><td width=70><input type='button' onClick='javascript: showDetail("+i+")' style='background-color:#ece9d8;border-width:0;' id='"+buId+"' value='�ڵ���ϸ' /></td></tr>");
	
}
out.println(sb.toString());
%>

</tbody>
</table>
