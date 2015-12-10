package com.volkswagen.tel.billing.billcall.rest;

import com.volkswagen.tel.billing.billcall.biz.TelephoneBillBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;
import com.volkswagen.tel.billing.billcall.rest.util.RoleChecker;
import com.volkswagen.tel.billing.billcall.rest.util.TelephoneChecker;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Service("telephoneBillService")
@Path("/telephoneBillService")
public class TelephoneBillService {
	private static final Logger log = LoggerFactory
			.getLogger(TelephoneBillService.class);

	@Context
	HttpServletRequest request;

	@Autowired
	TelephoneBillBizService telephoneBillBizService;

	@Autowired
	TelephoneChecker telephoneChecker;
	
	@Autowired
	RoleChecker roleChecker;

	@POST
	@Path("getAvailableYearsByTel")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAvailableYearsByTel(
			@FormParam("telephoneNumber") String telephoneNumber) {
		log.info("---------- getAvailableYearsByTel strart.");
		JSONObject jObj = null;
		try {
            jObj = telephoneBillBizService.getAvailableYearsByTel(telephoneNumber); //TODO
//            Integer yyyy = 2014;
//            String status = "ok";
//            jObj = new JSONObject();
//            jObj.put("year", yyyy);
//            jObj.put("status", status);
//            Collection<JSONObject> yearArray = new ArrayList<JSONObject>();
//            yearArray.add(jObj);
//            jobj.put("yearList", yearArray);
//            jobj.put("returnCode", "SUCCESS");
//            jobj.put("returnMessage", "SUCCESS");     //TODO used to test
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getAvailableYearsByTel end.");
		return jObj;
	}

	@POST
	@Path("getAvailableMonthsByTelAndYear")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAvailableMonthsByTelAndYear(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year) {
		log.info("---------- getAvailableMonthsByTelAndYear strart.");
		JSONObject jObj = null;
		try {
			String userId = TbsSessionUtil.getUidFromSession(request);
			jObj = telephoneBillBizService.getAvailableMonthsByTelAndYear(userId, telephoneNumber, year); //TODO
//            Integer month = 9;
//            String status = "OK";
//            jObj = new JSONObject();
//            jObj.put("month", month);
//            jObj.put("status", status);
//            Collection<JSONObject> theArray = new ArrayList<JSONObject>();
//            theArray.add(jObj);
//            jObj = new JSONObject();
//            jObj.put("monthList", theArray);
//            jObj.put("returnCode", "SUCCESS");
//            jObj.put("returnMessage", "SUCCESS");    //TODO used to test
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getAvailableMonthsByTelAndYear end.");
		return jObj;
	}
	
	@POST
	@Path("getAvailableMonthsByTelAndYearAndUid")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getAvailableMonthsByTelAndYearAndUid(
			@FormParam("userId") String userId,
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year) {
		log.info("---------- getAvailableMonthsByTelAndYear strart.");
		JSONObject jObj = null;
		try {
			jObj = telephoneBillBizService.getAvailableMonthsByTelAndYear(
					userId, telephoneNumber, year);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getAvailableMonthsByTelAndYear end.");
		return jObj;
	}

	@POST
	@Path("getTelephoneBillInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getTelephoneBillInfo(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month) {
		log.info("---------- getTelephoneBillInfo strart.");
		JSONObject jObj = null;
		try {
			String userId = TbsSessionUtil.getUidFromSession(request);
			if (telephoneChecker.isValidTelephone(userId, telephoneNumber)) {
				jObj = telephoneBillBizService.getTelephoneBillInfo(
						telephoneNumber, year, month);
			} else {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put("returnMessage",
						"Invalid user session. The telephone number is not owned by you.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getTelephoneBillInfo end.");
		return jObj;
	}
	
	@POST
	@Path("getTelephoneBillInfoForAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getTelephoneBillInfoForAdmin(
			@FormParam("targetUserId") String targetUserId,
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month) {
		log.info("---------- getTelephoneBillInfoForAdmin strart.");
		JSONObject jObj = null;
		try {
			String adminUserId = TbsSessionUtil.getUidFromSession(request);
			if (roleChecker.hasRole(adminUserId, RoleChecker.EMERGENCY_USER)
					|| roleChecker.hasRole(adminUserId, RoleChecker.AUDITOR)) {
				if (telephoneChecker.isValidTelephone(targetUserId, telephoneNumber)) {
					jObj = telephoneBillBizService.getTelephoneBillInfo(
							telephoneNumber, year, month);
				} else {
					StringBuilder msgBuilder = new StringBuilder();
					msgBuilder
							.append("The telephone number '")
							.append(telephoneNumber)
							.append("' is not registered under the name of user '")
							.append(targetUserId).append("'.");
					jObj = new JSONObject();
					jObj.put("returnCode", "FAILURE");
					jObj.put("returnMessage", msgBuilder.toString());
				}
			} else {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put(
						"returnMessage",
						"The function can be accessed by administrator only. Please check your account out with administrator.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getTelephoneBillInfoForAdmin end.");
		return jObj;
	}

	@POST
	@Path("inactivateTelephoneBillStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject inactivateTelephoneBillStatus(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month) {
		log.info("---------- inactivateTelephoneBillStatus strart.");
		JSONObject jObj = null;
		try {
			String userId = TbsSessionUtil.getUidFromSession(request);
			if (telephoneChecker.isValidTelephone(userId, telephoneNumber)) {
				jObj = telephoneBillBizService.inactivateTelephoneBillStatus(
						telephoneNumber, year, month);
			} else {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put("returnMessage",
						"Invalid user session. The telephone number is not owned by you.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- inactivateTelephoneBillStatus end.");
		return jObj;
	}
	
	@POST
	@Path("activateTelephoneBillStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject activateTelephoneBillStatus(
			@FormParam("billId") long billId) {
		log.info("---------- activateTelephoneBillStatus strart.");
		JSONObject jObj = null;
		try {
			String adminUserId = TbsSessionUtil.getUidFromSession(request);
			if (roleChecker.hasRole(adminUserId, RoleChecker.HR_ADMIN)
					|| roleChecker.hasRole(adminUserId,
							RoleChecker.EMERGENCY_USER)) {
				jObj = telephoneBillBizService.activateTelephoneBillStatus(billId);
			} else {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put(
						"returnMessage",
						"The function can be accessed by administrator only. Please check your account out with administrator.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- activateTelephoneBillStatus end.");
		return jObj;
	}
	
	@POST
	@Path("getValidTelBillListForAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getValidTelBillListForAdmin(
			@FormParam("targetUserId") String targetUserId,
			@FormParam("telephoneNumber") String telephoneNumber) {
		log.info("---------- getValidTelBillListForAdmin strart.");
		log.info(">>> userId=" + targetUserId + ", telephoneNumber="
				+ telephoneNumber);

		JSONObject jObj = telephoneBillBizService.getValidTelBillListForAdmin(
				targetUserId, telephoneNumber);

		if (jObj == null) {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "Could not find any bill.");
		}

		log.info("---------- getValidTelBillListForAdmin end.");

		return jObj;
	}
}