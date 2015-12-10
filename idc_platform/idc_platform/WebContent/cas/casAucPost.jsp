<%@page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="com.hp.idc.cas.auc.*"%>
<%@ page import="com.hp.idc.cas.common.*"%>
<%@ include file="getPurview.jsp"%>


<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Expires","0");
response.setContentType("application/xml;charset=gbk");
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>
<%

	Exception ex = null;
	String operId = userId;
	OperationResult or = new OperationResult();
	try {
		String postType = request.getParameter("postType");

		postType = postType==null?"":postType;
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String status = request.getParameter("status");
		status = (status==null?"0":status);
		String subType = request.getParameter("subType");
		subType = subType == null?"":subType;
		
		if (postType.equals("user")){
			String mobile = request.getParameter("mobile");
			String email = request.getParameter("email");
			String pStatus = request.getParameter("p_status");
			pStatus = (pStatus==null?"0":pStatus);

			PersonInfo info = new PersonInfo();
			info.setId(id);
			info.setName(name);
			info.setMobile(mobile);
			info.setEmail(email);
			info.setStatus(Integer.parseInt(status));
			info.setPersonStatus(Integer.parseInt(pStatus));
			if (subType.equals("resetPassword")){
				or = PersonManager.resetPassword(id,operId,userIp);
			}else if (subType.equals("changePassword")){
				String oldPass =  request.getParameter("oldPassword");
				String newPass =  request.getParameter("newPassword");
				PersonManager.modifyPassword(id,oldPass,newPass,operId,userIp);
			} else if (subType.equals("modify")){
				PersonManager.modifyPerson(info,operId,userIp);
			}else if (subType.equals("unlock")){
				or = PersonManager.unlockAccount(id);
			} else{
				or = PersonManager.addPerson(info,operId,userIp);
			}

		} else if (postType.equals("organization")){
			String parentId = request.getParameter("parentId");
			parentId = (parentId == null || parentId.equals(""))?"-1":parentId;
			OrganizationInfo ogInfo = new OrganizationInfo();
			ogInfo.setId(id);
			ogInfo.setName(name);
			ogInfo.setParentId(parentId);
			ogInfo.setStatus(Integer.parseInt(status));
			if (subType.equals("modify")){
				OrganizationManager.modifyOrganization(ogInfo, operId,userIp);
			} else if (subType.equals("add")){
				OrganizationManager.addOrganization(ogInfo,operId,userIp);
			}
		} else if (postType.equals("workgroup")){
			String parentId = request.getParameter("parentId");
			parentId = (parentId == null || parentId.equals(""))?"-1":parentId;
			WorkgroupInfo wgInfo = new WorkgroupInfo();
			wgInfo.setId(id);
			wgInfo.setName(name);
			wgInfo.setParentId(parentId);
			wgInfo.setStatus(Integer.parseInt(status));
			if (subType.equals("modify")){
				WorkgroupManager.modifyWorkgroup(wgInfo, operId, userIp);
			} else if (subType.equals("add")){
				WorkgroupManager.addWorkgroup(wgInfo, operId, userIp);
			}
		} else if (postType.equals("role")){
			String level = request.getParameter("level");
			if (level == null  ||  level.equals(""))
				level = "-1";
			String moId =  request.getParameter("moId");
			RoleInfo rInfo = new RoleInfo();
			rInfo.setId(id);
			rInfo.setName(name);
			rInfo.setMoId(moId);
			rInfo.setLevel(Integer.parseInt(level));
			rInfo.setStatus(Integer.parseInt(status));
			if (subType.equals("modify")){
				RoleManager.updateRole(rInfo, operId, userIp);
			} else if (subType.equals("add")){
				RoleManager.addRole(rInfo, operId, userIp);
			}
		}else if (postType.equals("addRelations")){

			String users = request.getParameter("users");
			String rType = request.getParameter("rType");
			String objectId = request.getParameter("toId");
			String roleId =  request.getParameter("roleId");
			if ("userWorkgroup".equals(rType)){
				String[] user = users.split(",");
				String[] wgs = objectId.split(",");
				WorkgroupManager.addRelations(user,wgs,roleId,operId, userIp);
			} else if ("userOrganization".equals(rType)){
				String[] user = users.split(",");
				OrganizationManager.addRelations(user,objectId,roleId,false,operId, userIp);
			} else if ("userOrgaUpdate".equals(rType)){
				String[] user = users.split(",");
				OrganizationManager.addRelations(user,objectId,roleId,true,operId, userIp);
			}
		} else if (postType.equals("deleteRelations")){

			String user = request.getParameter("user");
			String rType = request.getParameter("rType");
			String objectId = request.getParameter("toId");
			if ("userWorkgroup".equals(rType)){
				WorkgroupManager.deleteRelations(user,objectId,operId, userIp);
			} else if ("userOrganization".equals(rType)){
				OrganizationManager.deleteRelations(user,operId, userIp);
			}
		} else if (postType.equals("deleteMapping")){
			String user = request.getParameter("user");
			String thirdSystem = request.getParameter("thirdSystem");
			String thirdUserId = request.getParameter("thirdUserId");
			AUCMappingInfo mi = new AUCMappingInfo();
			mi.setUserId(user);
			mi.setThirdSystem(thirdSystem);
			mi.setThirdUserId(thirdUserId);
			AUCMapping.deleteMapping(mi);
		} else if (postType.equals("addMapping")){
			String user = request.getParameter("user");
			String thirdSystem = request.getParameter("thirdSystem");
			String thirdUserId = request.getParameter("thirdUserId");
			AUCMappingInfo mi = new AUCMappingInfo();
			mi.setUserId(user);
			mi.setThirdSystem(thirdSystem);
			mi.setThirdUserId(thirdUserId);
			AUCMapping.addMapping(mi);
		}
	}catch(Exception e){
		e.printStackTrace();
		or.setSuccess(false);
		System.out.println(e.toString());
		or.setMessage(e.toString());
	}

%>

<response success='<%=(or.isSuccess() ? "true" : "false")%>'><%=StringUtil.escapeXml(or.getMessage())%></response>
