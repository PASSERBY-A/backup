package com.volkswagen.tel.billing.billcall.rest;

import com.volkswagen.tel.billing.billcall.biz.BillCallRecordBizService;
import com.volkswagen.tel.billing.billcall.biz.ReportBizService;
import com.volkswagen.tel.billing.billcall.biz.ReportLocalBizService;
import com.volkswagen.tel.billing.billcall.biz.util.TbsSessionUtil;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterType;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoServiceImpl;
import com.volkswagen.tel.billing.billcall.rest.util.RoleChecker;
import com.volkswagen.tel.billing.billcall.rest.util.TelephoneChecker;
import com.volkswagen.tel.billing.common.datatable.DataTableParam;
import com.volkswagen.tel.billing.common.datatable.DataTableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.Map;

@Service("reportService")
@Path("/reportService")
public class ReportService {
	private static final Logger log = LoggerFactory
			.getLogger(ReportService.class);
	
	@Context
	private HttpServletRequest request;

	@Autowired
	private BillCallRecordBizService billCallRecordBizService;
	
	@Autowired
	private TelephoneBillDaoService telephoneBillDaoService;
	
	@Autowired
	private TelephoneChecker telephoneChecker;
	
	@Autowired
	private RoleChecker roleChecker;
	
	@Autowired
	private ReportBizService reportBizService;
	
	@Autowired
	private ReportLocalBizService  reportLocalBizService;
 

    @POST
	@Path("getAllOpenBillsByYearAndMonth")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllOpenBillsByYearAndMonth(@DefaultValue("true") @FormParam("isQuery") String isQuery,@DefaultValue("") @FormParam("voList") String voListStr)
			throws Exception {
		log.info("---------- getBillCallRecordsByTelAndMonthAndUserId strart.");

		DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
		Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());
		
		String sEcho = paramMap.get("sEcho");
		int pageNumber = (Integer.parseInt(paramMap.get("iDisplayStart")) / Integer.parseInt(paramMap.get("iDisplayLength"))) + 1;
		int displayLength = Integer.parseInt(paramMap.get("iDisplayLength"));
		
		// - get user info from session;
		String targetUserId = "ELIQIAG";
		//String telephoneNumber = "13801010101";
		
		String type = paramMap.get("telephoneNumber");
		
		System.out.println(type);
		
		int year = Integer.parseInt(paramMap.get("year"));
		int month = Integer.parseInt(paramMap.get("month"));
		
		
		// - valid date period can be calculated from table user_telephone
		
		log.info(">>> pageNumber=" + pageNumber + ", displayLength=" + displayLength);
		log.info(">>> telephoneNumber=" + type + ", year="+ year + ", month=" + month);

		JSONObject jObj = new JSONObject();
		
		String auditorUid = TbsSessionUtil.getUidFromSession(request);
		
		if (roleChecker.hasRole(auditorUid, RoleChecker.EMERGENCY_USER) || roleChecker.hasRole(auditorUid, RoleChecker.AUDITOR)||roleChecker.hasRole(auditorUid, RoleChecker.ITASSERT))
		{
				
				jObj = reportBizService.findAllOpenBills(type, year, month, pageNumber, displayLength);
				
				jObj.put("sEcho", sEcho);
		}
		else 
		{
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage","The function can be accessed by auditor only. Please check your account out with system administrator.");
			this.wrapEmptyResult(jObj);
		}
		
		
		
		
		log.info("---------- getAllOpenBillsByYearAndMonth end.");
		return jObj.toString();
	}
 
    
    @POST
  	@Path("costCenterMonthlyReport")
  	@Produces(MediaType.APPLICATION_JSON)
  	public String costCenterMonthlyReport(@DefaultValue("true") @FormParam("isQuery") String isQuery,@DefaultValue("") @FormParam("voList") String voListStr)
  			throws Exception {
  		log.info("---------- getAllCostCenterInfo strart.");

  		DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
  		Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());
  		
  		String sEcho = paramMap.get("sEcho");
  		int pageNumber = (Integer.parseInt(paramMap.get("iDisplayStart")) / Integer.parseInt(paramMap.get("iDisplayLength"))) + 1;
  		int displayLength = Integer.parseInt(paramMap.get("iDisplayLength"));
  		
  		// - get user info from session;
  		
  		String costCenter = paramMap.get("targetUserId")!=null?(StringUtils.trim(paramMap.get("targetUserId"))):"";
  		
  		System.out.println(costCenter);
  		
  		int year = Integer.parseInt(paramMap.get("year"));
  		int month = Integer.parseInt(paramMap.get("month"));
  		
  		
  		// - valid date period can be calculated from table user_telephone
  		
  		log.info(">>> pageNumber=" + pageNumber + ", displayLength=" + displayLength);
  		log.info(">>> costCenter=" + costCenter + ", year="+ year + ", month=" + month);

  		JSONObject jObj = new JSONObject();
  		
  		String auditorUid = TbsSessionUtil.getUidFromSession(request);
  		
  		if (roleChecker.hasRole(auditorUid, RoleChecker.EMERGENCY_USER) || roleChecker.hasRole(auditorUid, RoleChecker.AUDITOR) 
  				||roleChecker.hasRole(auditorUid, RoleChecker.VCIC) || roleChecker.hasRole(auditorUid, RoleChecker.VGIC) ||roleChecker.hasRole(auditorUid, RoleChecker.VCRA) ||roleChecker.hasRole(auditorUid, RoleChecker.AUDI))
  		{
  				
  				//jObj = reportBizService.findCostCenter(costCenter, year, month, pageNumber, displayLength);
  				jObj = reportLocalBizService.findCostCenter(costCenter, year, month, pageNumber, displayLength);
  				jObj.put("sEcho", sEcho);
  		}
  		else 
  		{
  			jObj = new JSONObject();
  			jObj.put("returnCode", "FAILURE");
  			jObj.put("returnMessage","The function can be accessed by auditor only. Please check your account out with system administrator.");
  			this.wrapEmptyResult(jObj);
  		}
  		
  		
  		
  		
  		log.info("---------- getAllCostCenterInfo end.");
  		return jObj.toString();
  	}    
    

    @POST
  	@Path("getAllCostCenterInfo")
  	@Produces(MediaType.APPLICATION_JSON)
  	public String getAllCostCenterInfo(@DefaultValue("true") @FormParam("isQuery") String isQuery,@DefaultValue("") @FormParam("voList") String voListStr)
  			throws Exception {
  		log.info("---------- getAllCostCenterInfo strart.");

  		DataTableParam params = DataTableUtil.collectParameters(isQuery, voListStr);
  		Map<String, String> paramMap = DataTableUtil.convertVoSetToMap(params.getVoList());
  		
  		String sEcho = paramMap.get("sEcho");
  		int pageNumber = (Integer.parseInt(paramMap.get("iDisplayStart")) / Integer.parseInt(paramMap.get("iDisplayLength"))) + 1;
  		int displayLength = Integer.parseInt(paramMap.get("iDisplayLength"));
  		
  		// - get user info from session;
  		
  		//String costCenter = paramMap.get("targetUserId");
  		
  		//System.out.println(costCenter);
  		
  		int year = Integer.parseInt(paramMap.get("year"));
  		int month = Integer.parseInt(paramMap.get("month"));
  		String type= paramMap.get("type") ;
  		
  		// - valid date period can be calculated from table user_telephone
  		
  		log.info(">>> pageNumber=" + pageNumber + ", displayLength=" + displayLength);
  		//log.info(">>> costCenter=" + costCenter + ", year="+ year + ", month=" + month);

  		JSONObject jObj = new JSONObject();
  		
  		String auditorUid = TbsSessionUtil.getUidFromSession(request);
  		
  		if (roleChecker.hasRole(auditorUid, RoleChecker.EMERGENCY_USER) || roleChecker.hasRole(auditorUid, RoleChecker.AUDITOR))
  		{
  			jObj = reportLocalBizService.findAllCostCenter(null,year, month, pageNumber, displayLength);
  				//jObj = reportBizService.findAllCostCenter(year, month, pageNumber, displayLength);
  				
  				jObj.put("sEcho", sEcho);
  		}else if(roleChecker.hasRole(auditorUid, RoleChecker.VGIC)){
  			
  			jObj = reportLocalBizService.findAllCostCenter(CostCenterType.VGIC.toString(),year, month, pageNumber, displayLength);
  			
  			jObj.put("sEcho", sEcho);
  		}
  		else if(roleChecker.hasRole(auditorUid, RoleChecker.AUDI)){
  			
  			jObj = reportLocalBizService.findAllCostCenter(CostCenterType.AUDI.toString(),year, month, pageNumber, displayLength);
  			jObj.put("sEcho", sEcho);
  		}
  		else if(roleChecker.hasRole(auditorUid, RoleChecker.VCRA)){
  			jObj = reportLocalBizService.findAllCostCenter(CostCenterType.VCRA.toString(),year, month, pageNumber, displayLength);
  			jObj.put("sEcho", sEcho);
  		}
  		else if(roleChecker.hasRole(auditorUid, RoleChecker.VCIC)){
  			
  			jObj = reportLocalBizService.findAllCostCenter(CostCenterType.VCIC.toString(),year, month, pageNumber, displayLength);
  			jObj.put("sEcho", sEcho);
  		}
  		else 
  		{
  			jObj = new JSONObject();
  			jObj.put("returnCode", "FAILURE");
  			jObj.put("returnMessage","The function can be accessed by auditor only. Please check your account out with system administrator.");
  			this.wrapEmptyResult(jObj);
  		}
  		
  		
  		
  		
  		log.info("---------- getAllCostCenterInfo end.");
  		return jObj.toString();
  	} 
    
    
    
    
    
	
	private void wrapEmptyResult(JSONObject jsonObj) {
		jsonObj.put("resultList", new JSONArray());
		jsonObj.put("iTotalRecords", 0);
		jsonObj.put("iTotalDisplayRecords", 0);
	}
}