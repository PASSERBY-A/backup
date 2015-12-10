package com.volkswagen.tel.billing.billcall.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;

@Service("sessionMockupService")
@Path("/sessionMockupService")
public class SessionMockupService {
	private static final Logger log = LoggerFactory
			.getLogger(SessionMockupService.class);

	@Context
	HttpServletRequest request;
	
	@Context
	HttpServletResponse response;

	@POST
	@Path("mockup")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject mockup(@FormParam("sessionUid") String sessionUid) {
		log.info("---------- mockup strart.");

		JSONObject jObj = null;
		try {
			TbsSessionUtil.saveUidToSession(response, sessionUid);

			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- mockup end.");
		return jObj;
	}
}
