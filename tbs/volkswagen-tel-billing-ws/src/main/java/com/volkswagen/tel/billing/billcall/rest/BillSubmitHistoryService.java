package com.volkswagen.tel.billing.billcall.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
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

import com.volkswagen.tel.billing.billcall.biz.BillSubmitHistoryBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;

@Service("billSubmitHistoryService")
@Path("/billSubmitHistoryService")
public class BillSubmitHistoryService {
	private static final Logger log = LoggerFactory
			.getLogger(BillSubmitHistoryService.class);

	@Context
	HttpServletRequest request;

	@Autowired
	BillSubmitHistoryBizService billSubmitHistoryBizService;

	@POST
	@Path("submitToSap")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject submitToSap(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month) {
		log.info("---------- submitToSap strart.");
		JSONObject jObj = null;

		String userId = TbsSessionUtil.getUidFromSession(request);
		try {
			jObj = billSubmitHistoryBizService.submitToSap(userId,
					telephoneNumber, year, month);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}
		log.info("---------- submitToSap end.");
		return jObj;
	}
}