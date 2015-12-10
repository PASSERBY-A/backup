<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.regex.*"%>
<%@page import="com.hp.idc.portal.security.*"%>
<%@page import="com.hp.idc.portal.util.*"%>
<%

response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");

/*	Object c[] = request.getParameterMap().keySet().toArray();
	for (int i = 0; i < c.length; i++)
	{
		System.out.println("" + c[i].toString() + ":" + request.getParameter(c[i].toString()).toString());
	}*/
	try{
	String ci_id = request.getParameter("node");
	String nodePath = request.getParameter("nodePath");
	String child = request.getParameter("child");
	String type = request.getParameter("type");
	String origin = request.getParameter("origin");
	String regexId = request.getParameter("regexId");
	
	//如果不显示人员就相当于组织/工作组的选择框
	boolean showPerson = true;
	String _tsp = request.getParameter("showPerson");
	if(_tsp!=null)
		showPerson = "true".equals(_tsp);
	boolean clickAll = "1".equals(request.getParameter("clickAll"));
	out.print("[");
	int count = 0;
/**
	通过nodePath分析出应该取过滤串的哪一段作为过滤表达式
	例如：“系统管理组”点击展开时,nodePath=/_/xtgl,加载此节点下的节点时，应该读regexId的第三个“/”号后的表达式
	如果此时regexId=/_/xtgl/^li  则表示要取“系统管理组”下id以“li”开头的节点
**/
	if (regexId==null || regexId.equals(""))
		regexId = "/_/";
	String[] filter = regexId.split(";");
	if (ci_id!=null && !ci_id.equals("_") && !ci_id.equals("-1")){
		List pList = new ArrayList();
		if (Integer.parseInt(type) == Common.RT_WORKGROUP_PERSON) {
			List<WorkgroupInfo> wgList = WorkgroupManager.getAllWorkgroup();
			//if (wgList!=null)
				//PortalUtil.sort(wgList, "name", false);					
			WorkgroupInfo wgInfoParent = WorkgroupManager.getWorkgroupById(ci_id);
			for (int i = 0; i < wgList.size(); i++) {
				WorkgroupInfo wgInfo = wgList.get(i);
				boolean isChild = false;
				if (wgInfoParent.getOid()!=-1 && wgInfo.getParentOid() == wgInfoParent.getOid())
					isChild = true;
				if(wgInfo.getParentId()!=null && wgInfo.getParentId().equals(wgInfoParent.getId()))
					isChild = true;
				if(!isChild)
					continue;

				//条件过滤
				if (isWaster(wgInfo.getId(),filter,nodePath))
					continue;

				if (count > 0)
					out.print(",");
				count++;
				out.print("{id:'" + wgInfo.getId() + "',");
				out.print("text:'" + StringUtil.escapeJavaScript(wgInfo.getName()) + "',");
				if (showPerson && !clickAll)
					out.print("leaf: false, _click: false}");
				else
					out.print("leaf: false, _click: true}");
			}
			pList = PersonManager.getPersonsOfWorkgroup(wgInfoParent.getId());

		} else if (Integer.parseInt(type) == Common.RT_ORGANIZATION_PERSON) {
			List orList = OrganizationManager.getOrganizationByParentId(ci_id);

			OrganizationInfo orInfoParent = OrganizationManager.getOrganizationById(ci_id);

			for (int i = 0; i < orList.size(); i++) {
				OrganizationInfo orInfo = (OrganizationInfo)orList.get(i);
				boolean isChild = false;
				if (orInfoParent.getOid()!=-1 && orInfo.getParentOid() == orInfoParent.getOid())
					isChild = true;
				if(orInfo.getParentId()!=null && orInfo.getParentId().equals(orInfoParent.getId()))
					isChild = true;
				if(!isChild)
					continue;
				if (isWaster(orInfo.getId(),filter,nodePath))
					continue;

				if (count > 0)
					out.print(",");
				count++;
				out.print("{id:'" + orInfo.getId() + "',");
				out.print("text:'" + StringUtil.escapeJavaScript(orInfo.getName()) + "',");
				if (showPerson && !clickAll)
					out.print("leaf: false, _click: false}");
				else
					out.print("leaf: false, _click: true}");
			}
			pList = PersonManager.getPersonsOfOrganization(orInfoParent.getId());
		}
		if (pList!=null)
			PortalUtil.sort(pList, "name", false);
		if (showPerson) {
			for (int i = 0; i < pList.size(); i++) {
				PersonInfo pInfo = (PersonInfo)pList.get(i);
				if (isWaster(pInfo.getId(),filter,nodePath))
						continue;
				if (count > 0)
					out.print(",");
				count++;
				out.print("{id:'" + pInfo.getId() + "',");
				out.print("text:'" + StringUtil.escapeJavaScript(pInfo.getName()) + "',");
				out.print("leaf: true, _click: true}");
			}
		}
	} else {
			ci_id = "-1";
		if (Integer.parseInt(type) == Common.RT_WORKGROUP_PERSON) {
			List wgList = WorkgroupManager.getAllWorkgroup();
			if (wgList!=null)
				PortalUtil.sort(wgList, "name", false);
			for (int i = 0; i < wgList.size(); i++) {
				WorkgroupInfo wgInfo = (WorkgroupInfo)wgList.get(i);
				if (wgInfo.getParentOid() != -1 || (wgInfo.getParentId()!=null && !wgInfo.getParentId().equals("")&&!wgInfo.getParentId().equals("-1")))
					continue;

				if (isWaster(wgInfo.getId(),filter,nodePath))
					continue;

				if (count > 0)
					out.print(",");
				count++;
				out.print("{id:'" + wgInfo.getId() + "',");
				out.print("text:'" + StringUtil.escapeJavaScript(wgInfo.getName()) + "',");
				if (showPerson && !clickAll)
					out.print("leaf: false, _click: false}");
				else
					out.print("leaf: false, _click: true}");
			}
		} else if (Integer.parseInt(type) == Common.RT_ORGANIZATION_PERSON) {
			List orList = OrganizationManager.getOrganizationByParentId(ci_id);
			
			for (int i = 0; i < orList.size(); i++) {
				OrganizationInfo orInfo = (OrganizationInfo)orList.get(i);
				if (orInfo.getParentOid() != -1 || (orInfo.getParentId()!=null && !orInfo.getParentId().equals("")&&!orInfo.getParentId().equals("-1")))
					continue;

				if (isWaster(orInfo.getId(),filter,nodePath))
					continue;

				if (count > 0)
					out.print(",");
				count++;
				out.print("{id:'" + orInfo.getId() + "',");
				out.print("text:'" + StringUtil.escapeJavaScript(orInfo.getName()) + "',");
				if (showPerson && !clickAll)
					out.print("leaf: false, _click: false}");
				else
					out.print("leaf: false, _click: true}");
			}
		}
	}
	out.print("]");
	}catch(Exception e){
		e.printStackTrace();
	}
%>

<%!
	public boolean isWaster(String id, String[] filter, String nodePath) {
		if (nodePath == null)
			return false;
		int nodeIndex = nodePath.split("/").length;
		boolean waster = true;
		for (int fi = 0; fi < filter.length; fi++) {
			if (filter[fi] == null || filter[fi].equals(""))
				continue;
	
			if (filter[fi].startsWith("_PATH_")) {
	
				String fs = filter[fi].substring(6);
				fs = "/_/" + fs;
				if (fs.indexOf(nodePath + "/" + id + "/") != -1 || fs.indexOf(nodePath + "/*") != -1) {
					waster = false;
					break;
				}
			} else {
				if (filter[fi].length() > 0 && filter[fi].charAt(0) != '/')
					filter[fi] = "/" + filter[fi];
				if (!filter[fi].startsWith("/_/"))
					filter[fi] = "/_" + filter[fi];
	
				String[] fs_ = filter[fi].split("/");
				if (nodeIndex < fs_.length && fs_[nodeIndex] != null && !fs_[nodeIndex].equals("*")
						&& !fs_[nodeIndex].equals("")) {
					Pattern p = Pattern.compile(fs_[nodeIndex]);
					Matcher m = p.matcher(id);
					if (m.find()) {
						waster = false;
						break;
					}
				} else {
					waster = false;
					break;
				}
			}
		}
	
		return waster;
	}
%>