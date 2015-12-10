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

import com.volkswagen.tel.billing.billcall.biz.UserPrivateNumberBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;

@Service("userPrivateNumberService")
@Path("/userPrivateNumberService")
public class UserPrivateNumberService {
	private static final Logger log = LoggerFactory
			.getLogger(UserPrivateNumberService.class);

	@Context
	HttpServletRequest request;
	
	@Autowired
	UserPrivateNumberBizService userPrivateNumberBizService;

	@POST
	@Path("savePrivateNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject savePrivateNumber(
			@FormParam("privateNumber") String privateNumber,
			@FormParam("uniquePageElemId") String uniquePageElemId) {
		log.info("---------- savePrivateNumber strart.");
		JSONObject jObj = null;

		try {
			String userId = TbsSessionUtil.getUidFromSession(request);
			
			jObj = userPrivateNumberBizService.savePrivateNumber(userId, privateNumber);
			jObj.put("uniquePageElemId", uniquePageElemId);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			if (jObj == null) {
				jObj = new JSONObject();
			}
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", e.getMessage());
		}

		log.info("---------- savePrivateNumber end.");
		return jObj;
	}
	
	@POST
	@Path("deletePrivateNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject deletePrivateNumber(
			@FormParam("entityId") long entityId,
			@FormParam("uniquePageElemId") String uniquePageElemId
			) {
		log.info("---------- deletePrivateNumber strart.");
		JSONObject jObj = null;

		try {
			jObj = userPrivateNumberBizService.deletePrivateNumberById(entityId);
			jObj.put("uniquePageElemId", uniquePageElemId);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			if (jObj == null) {
				jObj = new JSONObject();
			}
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", e.getMessage());
		}

		log.info("---------- deletePrivateNumber end.");
		return jObj;
	}
	
	@GET
	@Path("getPrivateNumbers")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getPrivateNumbers() {
		log.info("---------- getPrivateNumbers strart.");
		String userId = TbsSessionUtil.getUidFromSession(request);
		
		JSONObject jObj = null;

		try {
			jObj = userPrivateNumberBizService.getPrivateNumbers(userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if (jObj == null) {
				jObj = new JSONObject();
			}
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "FAILURE");
		}

		log.info("---------- getPrivateNumbers end.");
		return jObj;
	}
	
	
}
