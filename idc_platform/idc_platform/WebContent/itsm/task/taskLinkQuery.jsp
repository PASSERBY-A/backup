<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.util.StringUtil"%>

<%
	String linkOidStr = "";
	if (request.getParameter("linkOidStr")!=null)
		linkOidStr = request.getParameter("linkOidStr");
		
	List taskData = new ArrayList();
	if (!linkOidStr.equals("")) {
		String[] linkTasks = linkOidStr.split(",");
		for (int i = 0; i < linkTasks.length; i++) {

			//linkTasks[i]="origin_流程OID_流程版本OID_工单OID";
			String[] infs = linkTasks[i].split("_");
			if (infs == null || infs.length<4)
				continue;
			int wfVer = -1;
			if (infs.length == 4)
				wfVer = Integer.parseInt(infs[3]);
			TaskInfo tInfo = TaskManager.getTaskInfoByOid(infs[0], Integer.parseInt(infs[2]), Integer.parseInt(infs[1]),wfVer);
			
			if (tInfo!=null){
				if (tInfo.getStatus() == TaskInfo.STATUS_OPEN) {
					int lastIndex = tInfo.getLatestTaskDataId();
					TaskData lastData = tInfo.getTaskData(lastIndex);
					taskData.add(lastData.getAllData());
				} else
					taskData.add(tInfo.getValues());
			}
		}
	}
%>
{ "items" : [
<%
for (int i = 0; i < taskData.size(); i++) {
	Map<String,String> obj = (Map)taskData.get(i);
	//System.out.println(obj);
	if (i>0)
		out.print(",");
	out.print("{");
	String origin = obj.get("ORIGIN");
	if (origin == null)
		origin = "ITSM";
	out.print("oid:"+obj.get("TASK_OID") +",");
	out.print("origin:'"+origin +"',");
	out.print("wfOid:'"+obj.get("TASK_WF_OID") +"',");
	out.print("wfVer:'"+obj.get("TASK_WF_VER") +"',");
	out.print("title:'"+obj.get("title") +"',");
	out.print("status:'"+obj.get("TASK_STATUS") +"',");
	out.print("create_time:'"+obj.get("TASK_CREATE_TIME") +"',");
	out.print("create_by:'"+obj.get("TASK_CREATE_BY") +"',");
	out.print("wfName:'"+obj.get("TASK_WF_NAME") +"'");
	out.print("}");

}
%>
	],
	"totalCount" : <%=taskData.size()%>
}