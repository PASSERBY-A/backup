package com.volkswagen.tel.billing.billcall.rest;

import com.volkswagen.tel.billing.billcall.biz.BillCallRecordBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;
import com.volkswagen.tel.billing.billcall.rest.util.RoleChecker;
import com.volkswagen.tel.billing.billcall.rest.util.TelephoneChecker;
import com.volkswagen.tel.billing.common.datatable.DataTableParam;
import com.volkswagen.tel.billing.common.datatable.DataTableUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Service("billCallRecordService")
@Path("/billCallRecordService")
public class BillCallRecordService {
	private static final Logger log = LoggerFactory
			.getLogger(BillCallRecordService.class);
	
	@Context
	HttpServletRequest request;

	@Autowired
	BillCallRecordBizService billCallRecordBizService;
	
	@Autowired
	TelephoneChecker telephoneChecker;
	
	@Autowired
	RoleChecker roleChecker;
	
	/**
	 * Update for particular record id.
	 * Note: all the same called number will be updated as well.
	 * @param recordId
	 * @param isPrivate
	 * @return
	 */
	@POST
	@Path("updatePrivatePurposeByRecordId")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject updatePrivatePurposeByRecordId(
			@FormParam("recordId") long recordId,
			@FormParam("isPrivate") int isPrivate) {
		log.info("---------- updatePrivateInfoByRecordId strart.");
		
		JSONObject jObj = null;
		try {
			jObj = billCallRecordBizService.updatePrivatePurposeByRecordId(
					recordId, isPrivate);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}
		
		log.info("---------- updatePrivateInfoByRecordId end.");
		return jObj;
	}

	@POST
	@Path("getNumbersToExecuteInBatch")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getNumbersToExecuteInBatch(
			@FormParam("callingNumber") String callingNumber,
			@FormParam("year") int year,
			@FormParam("month") int month) {
		log.info("---------- getNumbersToExecuteInBatch strart.");
		JSONObject jObj = null;

		try {
			String userId = TbsSessionUtil.getUidFromSession(request);
			jObj = billCallRecordBizService.getNumbersToExecuteInBatch(userId,
					callingNumber, year, month);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}

		log.info("---------- getNumbersToExecuteInBatch end.");
		return jObj;
	}
	
	@POST
	@Path("updatePrivatePurposeInBatch")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject updatePrivatePurposeInBatch(
			@FormParam("callingNumber") String callingNumber,
			@FormParam("year") int year,
			@FormParam("month") int month,
			@FormParam("calledNumbers") String calledNumbers,
			@FormParam("isPrivate") int isPrivate) {
		log.info("---------- updatePrivatePurposeInBatch strart.");
		JSONObject jObj = null;
		try {
			String userId = TbsSessionUtil.getUidFromSession(request);
			jObj = billCallRecordBizService.updatePrivatePurposeInBatch(
					callingNumber, year, month, calledNumbers, isPrivate);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}
		
		log.info("---------- updatePrivatePurposeInBatch end.");
		return jObj;
	}
	
	/**
	 * Query for call records by parameters.
	 * @param isQuery - flag for marking the query.
	 * @param voListStr - query parameter, including telephoneNumber, year, month, and pagination param etc.
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("getBillCallRecordsByTelAndMonth")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBillCallRecordsByTelAndMonth(
			@DefaultValue("true") @FormParam("isQuery") String isQuery,
			@DefaultValue("") @FormParam("voList") String voListStr)
			throws Exception {
		log.info("---------- getBillCallRecordsByTelAndMonth strart.");

		DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
		Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());
		
		String sEcho = paramMap.get("sEcho");
		int pageNumber = (Integer.parseInt(paramMap.get("iDisplayStart")) / Integer
				.parseInt(paramMap.get("iDisplayLength"))) + 1;
		int displayLength = Integer.parseInt(paramMap.get("iDisplayLength"));
		
		// - get user info from session;
		String telephoneNumber = paramMap.get("telephoneNumber");
		int year = Integer.parseInt(paramMap.get("year"));
		int month = Integer.parseInt(paramMap.get("month"));
		// - valid date period can be calculated from table user_telephone
		
		log.info(">>> pageNumber=" + pageNumber + ", displayLength=" + displayLength);
		log.info(">>> telephoneNumber=" + telephoneNumber + ", year="
				+ year + ", month=" + month);

		JSONObject jObj = new JSONObject();
		String userId = TbsSessionUtil.getUidFromSession(request);
		if (telephoneChecker.isValidTelephone(userId, telephoneNumber)) {
			jObj = billCallRecordBizService.getBillCallRecordsByTelAndMonth(userId,
					telephoneNumber, year, month, pageNumber, displayLength);
			jObj.put("sEcho", sEcho);
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage",
					"Invalid user session. The telephone number is not owned by you.");
			this.wrapEmptyResult(jObj);
		}                     //TODO
		
		log.info("---------- getBillCallRecordsByTelAndMonth end.");
		return jObj.toString();
	}

    /**
     * Query for call summary by parameters.
     * @param isQuery - flag for marking the query.
     * @param voListStr - query parameter, including telephoneNumber, year, month, and pagination param etc.
     * @return
     * @throws Exception
     */
    @POST
    @Path("getBillCallSumByTelAndMonth")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBillCallSumByTelAndMonth(
            @DefaultValue("true") @FormParam("isQuery") String isQuery,
            @DefaultValue("") @FormParam("voList") String voListStr)
            throws Exception {
        log.info("---------- getBillCallSumByTelAndMonth strart.");

        DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
        Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());

        // - get user info from session;
        String telephoneNumber = paramMap.get("telephoneNumber");
        int year = Integer.parseInt(paramMap.get("year"));
        int month = Integer.parseInt(paramMap.get("month"));
        // - valid date period can be calculated from table user_telephone

        log.info(">>> telephoneNumber=" + telephoneNumber + ", year="
                + year + ", month=" + month);

        JSONObject jObj = new JSONObject();
        String userId = TbsSessionUtil.getUidFromSession(request);  //TODO "123"; to test without login service
        if (telephoneChecker.isValidTelephone(userId, telephoneNumber)) {
            jObj = billCallRecordBizService.getBillCallSumByTelAndMonth(userId, telephoneNumber, year, month);
        } else {
            jObj = new JSONObject();
            jObj.put("returnCode", "FAILURE");
            jObj.put("returnMessage",
                    "Invalid user session. The telephone number is not owned by you.");
            this.wrapEmptyResult(jObj);
        }//TODO

        log.info("---------- getBillCallSumByTelAndMonth end.");
        return jObj.toString();
    }

    @POST
	@Path("getBillCallRecordsByTelAndMonthAndUserId")
	@Produces(MediaType.APPLICATION_JSON)
	public String getBillCallRecordsByTelAndMonthAndUserId(
			@DefaultValue("true") @FormParam("isQuery") String isQuery,
			@DefaultValue("") @FormParam("voList") String voListStr)
			throws Exception {
		log.info("---------- getBillCallRecordsByTelAndMonthAndUserId strart.");

		DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
		Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());
		
		String sEcho = paramMap.get("sEcho");
		int pageNumber = (Integer.parseInt(paramMap.get("iDisplayStart")) / Integer
				.parseInt(paramMap.get("iDisplayLength"))) + 1;
		int displayLength = Integer.parseInt(paramMap.get("iDisplayLength"));
		
		// - get user info from session;
		String targetUserId = paramMap.get("targetUserId");
		String telephoneNumber = paramMap.get("telephoneNumber");
		int year = Integer.parseInt(paramMap.get("year"));
		int month = Integer.parseInt(paramMap.get("month"));
		// - valid date period can be calculated from table user_telephone
		
		log.info(">>> pageNumber=" + pageNumber + ", displayLength=" + displayLength);
		log.info(">>> telephoneNumber=" + telephoneNumber + ", year="
				+ year + ", month=" + month);

		JSONObject jObj = new JSONObject();
		String auditorUid = TbsSessionUtil.getUidFromSession(request);
		if (roleChecker.hasRole(auditorUid, RoleChecker.EMERGENCY_USER)
				|| roleChecker.hasRole(auditorUid, RoleChecker.AUDITOR)) {
			if (telephoneChecker
					.isValidTelephone(targetUserId, telephoneNumber)) {
				jObj = billCallRecordBizService
						.getBillCallRecordsByTelAndMonth(targetUserId,
								telephoneNumber, year, month, pageNumber,
								displayLength);
				jObj.put("sEcho", sEcho);
			} else {
				jObj = new JSONObject();
				jObj.put("returnCode", "FAILURE");
				jObj.put("returnMessage",
						"Invalid user session. The telephone number is not owned by you.");
				this.wrapEmptyResult(jObj);
			}
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put(
					"returnMessage",
					"The function can be accessed by auditor only. Please check your account out with system administrator.");
			this.wrapEmptyResult(jObj);
		}
		
		
		log.info("---------- getBillCallRecordsByTelAndMonthAndUserId end.");
		return jObj.toString();
	}
	
	private void wrapEmptyResult(JSONObject jsonObj) {
		jsonObj.put("resultList", new JSONArray());
		jsonObj.put("iTotalRecords", 0);
		jsonObj.put("iTotalDisplayRecords", 0);
	}
}