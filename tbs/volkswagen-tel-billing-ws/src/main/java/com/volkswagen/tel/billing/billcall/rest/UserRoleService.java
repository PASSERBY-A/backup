package com.volkswagen.tel.billing.billcall.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.biz.UserRoleBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;

@Service("userRoleService")
@Path("/userRoleService")
public class UserRoleService {
	private static final Logger log = LoggerFactory
			.getLogger(UserRoleService.class);

	@Context
	HttpServletRequest request;
	
	@Autowired
	UserRoleBizService userRoleBizService;

	@POST
	@Path("getRoleUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getRoleUsers(@FormParam("roleName") String roleName) {
		log.info("---------- getRoleUsers strart.");
		JSONObject jObj = new JSONObject();

		jObj = userRoleBizService.getRoleUsers(roleName);
		if (jObj == null) {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "FAILURE");
		} else {
			jObj.put("roleName", roleName);
		}

		log.info("---------- getRoleUsers end.");
		return jObj;
	}
	
	@GET
	@Path("getAllRoleUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAllRoleUsers() {
		log.info("---------- getAllRoleUsers strart.");
		JSONObject jObj = new JSONObject();

		jObj = userRoleBizService.getAllRoleUsers();
		if (jObj == null) {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "FAILURE");
		}

		log.info("---------- getAllRoleUsers end.");
		return jObj;
	}
	
	@GET
	@Path("getUserRoles")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserRoles() {
		log.info("---------- getUserRoles strart.");
		String userId = TbsSessionUtil.getUidFromSession(request);
		
		JSONObject jObj = new JSONObject();
		jObj = userRoleBizService.getUserRoles(userId);
		if (jObj == null) {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "FAILURE");
		}

		log.info("---------- getUserRoles end.");
		return jObj;
	}

	@POST
	@Path("saveRoleUser")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject saveRoleUser(
			@FormParam("userId") String userId,
			@FormParam("roleName") String roleName,
			@FormParam("uniquePageElemId") String uniquePageElemId
			) {
		log.info("---------- saveRoleUser strart.");
		JSONObject jObj = null;

		try {
			jObj = userRoleBizService.saveRoleUser(userId, roleName);
			jObj.put("uniquePageElemId", uniquePageElemId);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			if (jObj == null) {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put("returnMessage", "FAILURE");
			}
		}

		log.info("---------- saveRoleUser end.");
		return jObj;
	}
	
	@POST
	@Path("deleteRoleUser")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject deleteRoleUser(
			@FormParam("entityId") long entityId,
			@FormParam("uniquePageElemId") String uniquePageElemId
			) {
		log.info("---------- deleteRoleUser strart.");
		JSONObject jObj = null;

		try {
			jObj = userRoleBizService.deleteRoleUser(entityId);
			jObj.put("uniquePageElemId", uniquePageElemId);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			if (jObj == null) {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put("returnMessage", "FAILURE");
			}
		}

		log.info("---------- deleteRoleUser end.");
		return jObj;
	}
}
