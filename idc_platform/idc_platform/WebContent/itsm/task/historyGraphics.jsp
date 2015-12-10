<%@ page language="java" pageEncoding="gbk"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.itsm.configure.*"%>
<%@ page import="com.hp.idc.itsm.configure.fields.*"%>
<%@ page import="com.hp.idc.itsm.task.*"%>
<%@ page import="com.hp.idc.itsm.workflow.*"%>
<%@ page import="com.hp.idc.itsm.util.*"%>
<%@ page import="com.hp.idc.itsm.security.*"%>
<%@ include file="../getUser.jsp"%>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
	<xml:namespace ns="urn:schemas-microsoft-com:vml" prefix="v"/>
 	<LINK href="<%=request.getContextPath()%>/css/sysmgr.css" type=text/css rel=STYLESHEET>
 	 	<LINK href="<%=request.getContextPath()%>/itsm/style.css" type=text/css rel=STYLESHEET>

 	<STYLE>
		v\:* {BEHAVIOR: url(#default#VML)}
@media print {
   .notprint {
       display:none;
       }
}

@media screen {
   .notprint {
       display:inline;
       cursor:hand;
       }
}

	</style>
 	<script type="text/javascript" src="<%=request.getContextPath()%>/webframe/webframe.js"></script>
</head>
<body onload="resizePathDiv()" onresize="resizePathDiv()">
	<%
		int taskOid = -1;
		int wfOid = -1;
		if (request.getParameter("taskOid") != null)
			taskOid = Integer.parseInt(request.getParameter("taskOid"));
		TaskInfo taskInfo = null;
		TaskData taskData = null;
		WorkflowInfo wfInfo = null;
		WorkflowData wfData = null;
		NodeInfo nodeInfo = null;

		int formId = -1;
		if (taskOid != -1)
		{
			taskInfo = TaskManager.getTaskInfoByOid("ITSM",taskOid,wfOid,-1);
			taskData = taskInfo.getRootData();
			wfOid = taskInfo.getWfOid();
			wfInfo = WorkflowManager.getWorkflowByOid(wfOid);
			wfData = wfInfo.getVersion(taskInfo.getWfVer());
			nodeInfo = wfData.getNode(taskData.getNodeId());
			formId = Integer.valueOf(nodeInfo.getFormId());
		}

		FormInfo formInfo = FormManager.getFormByOid(formId);
		List fieldList = new ArrayList();
		if (formInfo!=null)
			fieldList = formInfo.getFields();


	%>
	<table width="100%" border='0' id="PrintA" style="page-break-after: always">
		<tr><td>
			<div id="drawAreaDiv" width="100%" style="overflow-x:scroll">
	<v:group id="drawArea" style="WIDTH: 700px; HEIGHT: 100px; v-text-anchor: top-left" coordsize = "700,100" >
		<%
			List hisList = new ArrayList();
			try {
				getHistoryList(taskData,-1,-1,hisList);
			}catch (Exception e) {
			e.printStackTrace();
			}

			for (int i = 0; i < hisList.size(); i++) {
				List tempList = (List)hisList.get(i);

				for (int j = 0; j < tempList.size(); j++) {
					HistoryNodeInfo hnInfo = (HistoryNodeInfo)tempList.get(j);
					int x = (i)*85;
					int y = j* 40;
					out.print("<v:RoundRect  stroked='false' filled='false' style='position:relative;left:"+x+";top:"+(y+5)+";width:60px;height:15px;'>");
			    out.print("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no'>"+hnInfo.getPersonName()+"</v:TextBox>");
			    out.print("</v:RoundRect>");
					out.print("<v:RoundRect id='node_"+hnInfo.getTaskDataId()+"_e' title='"+hnInfo.getNodeDesc()+(hnInfo.isRollback()?"(已回退)":"")+"' onclick='displayNode("+hnInfo.getTaskOid()+","+hnInfo.getTaskDataId()+")' style='position:relative;left:"+x+";top:"+(y+20)+";width:60px;height:20px;cursor:hand'>");
			    if (hnInfo.isRollback())
			    	out.print("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no' style='color:gray'>"+hnInfo.getNodeDesc()+"</v:TextBox>");
			    else
			    	out.print("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no'>"+hnInfo.getNodeDesc()+"</v:TextBox>");
			    out.print("</v:RoundRect> ");
			    if (hnInfo.getLine_level()>-1) {
			    	out.print("<v:line from='"+(hnInfo.getLine_level()*85+60)+","+(hnInfo.getLine_index()*40+30)+"' to='"+x+","+(y+30)+"'>");
			    	out.print("<v:stroke EndArrow='Classic'/>");
			    	out.print("</v:line>");
			    }
				}
			}
			//out.print(printNode(taskData,0,15,-1,-1));
		%>
	</v:group>
	</div>
	<input type="button" class="form-button" value="查看所有" onclick="displayForm.location.href='taskHisDetail.jsp?taskOid=<%=taskOid%>&viewMode=viewAllNode'">&nbsp;&nbsp;
	<input type="button" class="form-button" value="导出到EXCEL" onclick="AllAreaExcel()">&nbsp;&nbsp;
</td></tr>
<tr>
	<td id="nodeFormDetail">
	</td>
</tr>
<!--<tr><td align="center"><input type='button' class=form-button value='返回' onclick="location.href='taskHistory.jsp'"></td></tr>
-->
</table>
	<iframe id="displayForm" name="displayForm" src="taskHisDetail.jsp?taskOid=<%=taskOid%>&taskDataId=<%=taskData.getDataId()%>&operType=view" onload='nodeFormDetail.innerHTML=displayForm.document.body.innerHTML;' style="display:none"></iframe>
<script>
	var currentRect = "0";
	document.all["node_0_e"].strokecolor="#6E96C7";
	document.all["node_0_e"].fillcolor="#CBDCF7";
	function displayNode(taskOid,taskDataId) {
		if (currentRect!="") {
			document.all["node_"+currentRect+"_e"].strokecolor="black";
			document.all["node_"+currentRect+"_e"].fillcolor="white";
		}
		currentRect = taskDataId+"";
		document.all["node_"+currentRect+"_e"].strokecolor="#6E96C7";
		document.all["node_"+currentRect+"_e"].fillcolor="#d8e7F8";

		var url_ = "taskHisDetail.jsp?taskOid="+taskOid+"&operType=view&taskDataId="+taskDataId;
		displayForm.location.href=url_;
	}

	function resizePathDiv() {
		drawAreaDiv.style.width = document.body.offsetWidth-30;
	}
	function AllAreaExcel()
	{
		var thisSrc = displayForm.location.href;
		thisSrc = thisSrc.substring(thisSrc.indexOf("?"));
		location.href="historyExcel.jsp"+thisSrc;

	}

</script>
</body>
</html>
<%!
	public String printNode(TaskData td,int x,int y,int px,int py) throws Exception {
		StringBuffer ret = new StringBuffer();
		//NodeInfo ni = wfData.getNode(td.getNodeId());
		TaskInfo ti = td.getOwner();
		String personId = td.getAssignTo();
		PersonInfo pi = PersonManager.getPersonById(personId);
		ret.append("<v:RoundRect  stroked='false' filled='false' style='position:relative;left:"+x+";top:"+(y-15)+";width:60;height:15px;'>");
    ret.append("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no'>"+pi.getName()+"</v:TextBox>");
    ret.append("</v:RoundRect>");
		ret.append("<v:RoundRect id='node_"+td.getDataId()+"_e' title='"+td.getNodeDesc()+(td.isRollback()?"(已回退)":"")+"' onclick='displayNode("+ti.getOid()+","+td.getDataId()+")' style='position:relative;left:"+x+";top:"+y+";width:60;height:20px;cursor:hand'>");
    if (td.isRollback())
    	ret.append("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no' style='color:gray'>"+td.getNodeDesc()+"</v:TextBox>");
    else
    	ret.append("<v:TextBox inset='0pt,0pt,0pt,0pt' fill='no'>"+td.getNodeDesc()+"</v:TextBox>");
    ret.append("</v:RoundRect> ");
    if (px!=-1 && py!=-1) {
    	ret.append("<v:line from='"+(px+60)+","+(py+10)+"' to='"+x+","+(y+10)+"'>");
    	ret.append("<v:stroke EndArrow='Classic'/>");
    	ret.append("</v:line>");
    }
		if(td.getChilds().size()!=0) {
			List childs = td.getChilds();
			for(int i = 0; i < childs.size(); i++) {
				TaskData td_ = (TaskData)childs.get(i);
				ret.append(printNode(td_,x+85,y+i*35,x,y));
			}
		}
		return ret.toString();
	}

	public void getHistoryList(TaskData taskData,int level,int index,List nodeList) throws Exception{

		HistoryNodeInfo hnInfo = new HistoryNodeInfo();
		hnInfo.setTaskDataId(taskData.getDataId());
		hnInfo.setNodeDesc(taskData.getNodeDesc());
		hnInfo.setRollback(taskData.isRollback());

		TaskInfo ti = taskData.getOwner();
		hnInfo.setTaskOid(ti.getOid());

		String personId = taskData.getAssignTo();
		PersonInfo pi = PersonManager.getPersonById(personId);
		hnInfo.setPersonName(pi.getName());

		hnInfo.setLine_level(level);
		hnInfo.setLine_index(index);

		List tempList = null;
		if (nodeList.size()<=(level+1) || level==-1) {
			tempList = new ArrayList();
			nodeList.add(tempList);
		} else
			tempList = (List)nodeList.get(level+1);
		tempList.add(hnInfo);

		if(taskData.getChilds().size()!=0) {
			List childs = taskData.getChilds();
			for(int i = 0; i < childs.size(); i++) {
				TaskData td_ = (TaskData)childs.get(i);
				getHistoryList(td_,level+1,tempList.size()-1,nodeList);
			}
		}
	}
%>