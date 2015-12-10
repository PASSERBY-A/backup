package com.volkswagen.tel.billing.billcall.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.biz.UserTelephoneBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;
import com.volkswagen.tel.billing.common.datatable.DataTableParam;
import com.volkswagen.tel.billing.common.datatable.DataTableUtil;

@Service("userTelephoneService")
@Path("/userTelephoneService")
public class UserTelephoneService {
	private static final Logger log = LoggerFactory
			.getLogger(UserTelephoneService.class);
	@Context
	HttpServletRequest request;

	@Autowired
	private UserTelephoneBizService userTelephoneBizService;

	@GET
	@Path("getTelephonesBySessionUid")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getTelephonesBySessionUid() {
		log.info("---------- getTelephonesBySessionUid strart.");
		JSONObject jObj = null;

		String userId = TbsSessionUtil.getUidFromSession(request);
		if (userId == null || userId.trim().length() <= 0) {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "Invalid session.");
			return jObj;
		}

		try {
			jObj = userTelephoneBizService.getTelephonesByUserId(userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getTelephonesBySessionUid end.");
		return jObj;
	}

	@POST
	@Path("getTelephonesByUserId")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getTelephonesByUserId(
			@FormParam("targetUserId") String targetUserId) {
		log.info("---------- getTelephonesByUserId strart.");
		log.info(">>> targetUserId=" + targetUserId);
		JSONObject jObj = null;

		try {
			jObj = userTelephoneBizService.getTelephonesByUserId(targetUserId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getTelephonesByUserId end.");
		return jObj;
	}
	
	@POST
	@Path("getListByUserId")
	@Produces(MediaType.APPLICATION_JSON)
	public String getListByUserId(
			@DefaultValue("true") @FormParam("isQuery") String isQuery,
			@DefaultValue("") @FormParam("voList") String voListStr)
			throws Exception {
		log.info("---------- getListByUserId start.");

		DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
		Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());
		
		String sEcho = paramMap.get("sEcho");
		int pageNumber = (Integer.parseInt(paramMap.get("iDisplayStart")) / Integer
				.parseInt(paramMap.get("iDisplayLength"))) + 1;
		int displayLength = Integer.parseInt(paramMap.get("iDisplayLength"));
		
		// - get user info from session;
		String userId = paramMap.get("userId");

		log.info(">>> pageNumber=" + pageNumber + ", displayLength=" + displayLength);
		log.info(">>> userId=" + userId);

		JSONObject jObj = null;
		jObj = userTelephoneBizService.getListByUserId(userId, pageNumber,
				displayLength);
		
		if (jObj != null) {
			jObj.put("sEcho", sEcho);
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage",
					"Invalid user session. The telephone number is not owned by you.");
			this.wrapEmptyResult(jObj);
		}
		
		log.info("---------- getListByUserId end.");
		return jObj.toString();
	}
	
	private void wrapEmptyResult(JSONObject jsonObj) {
		jsonObj.put("resultList", new JSONArray());
		jsonObj.put("iTotalRecords", 0);
		jsonObj.put("iTotalDisplayRecords", 0);
	}
}
