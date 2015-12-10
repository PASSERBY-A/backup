package com.volkswagen.tel.billing.billcall.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.biz.UserInfoBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;

@Service("userInfoService")
@Path("/userInfoService")
public class UserInfoService {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfoService.class);
	@Context
	HttpServletRequest request;
	
	@Autowired
	private UserInfoBizService userInfoBizService;

	@GET
	@Path("getUserInfoBySessionUid")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserInfoBySessionUid() {
		log.info("---------- getUserInfoBySessionUid strart.");
		JSONObject jObj = null;
		
		String userId = TbsSessionUtil.getUidFromSession(request);
		if (userId == null || userId.trim().length() <= 0) {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "Invalid session.");
			return jObj;
		}
		
		try {
			jObj = userInfoBizService.getUserInfoByUserId(userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getUserInfoBySessionUid end.");
		return jObj;
	}
	
	@POST
	@Path("getUserInfoByUserId")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserInfoByUserId(
			@FormParam("targetUserId") String targetUserId) {
		log.info("---------- getUserInfoByUserId strart.");
		JSONObject jObj = null;
		
		try {
			jObj = userInfoBizService.getUserInfoByUserId(targetUserId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getUserInfoByUserId end.");
		return jObj;
	}

	@GET
	@Path("getUserInfoByUserIdViaJsonp")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserInfoByUserIdViaJsonp(
			@QueryParam("uid") String userId,
			@QueryParam("callback") String callbackMethod) {
		log.info("---------- getUserInfoByUserIdViaJsonp strart.");
		log.info(">>> userId="+userId+", callbackMethod="+callbackMethod);
		
		StringBuilder sb = new StringBuilder(callbackMethod);
		sb.append("(");
		JSONObject jObj = null;
		try {
			jObj = userInfoBizService.getUserInfoByUserId(userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}
		sb.append(jObj.toString());
		sb.append(");");

		log.info("---------- getUserInfoByUserIdViaJsonp end.");
		log.info(">>> response: "+sb.toString());
		return sb.toString();
	}
}
