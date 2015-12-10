package com.volkswagen.tel.billing.billcall.biz;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Component
public class TelephoneBillBizService {
	private static final Logger log = LoggerFactory
	.getLogger(TelephoneBillBizService.class);
	
	@Context
	HttpServletRequest request;
	
	@Autowired
	TelephoneBillDaoService telephoneBillDaoService;
	
	@Autowired
	UserTelephoneDaoService userTelephoneDaoService;
	
	/**
	 * Save a telephone bill.
	 * @param telephoneNumber
	 * @param year
	 * @param month
	 * @return
	 */
	public JSONObject saveTelephoneBill(
			@FormParam("telephoneNumber") String telephoneNumber,
			@FormParam("year") int year, @FormParam("month") int month) {
		
		TelephoneBillEntity billEntity = new TelephoneBillEntity();
		billEntity.setTelephoneNumber(telephoneNumber);
		billEntity.setYear(year);
		billEntity.setMonth(month);
		billEntity.setVendorName("");
		billEntity.setStatus("READY_FOR_PROCESS");
		billEntity.setLastUpdateTime(Calendar.getInstance().getTime());
		telephoneBillDaoService.saveTelephoneBill(billEntity);
		
		JSONObject jobj = new JSONObject();
		log.info(">>> billEntity=" + billEntity);
		jobj.put("billEntity", billEntity);
		
		jobj.put("returnCode", "SUCCESS");
		jobj.put("returnMessage", "SUCCESS");
		
		return jobj;
	}
	
	/**
	 * get valid starting month and end month by specified year for searching billings. 
	 * @param userId
	 * @param telephoneNumber
	 * @param targetYear
	 * @return
	 */
	private synchronized Map<String, Integer> getMonthLimitStartAndEnd(String userId,
			String telephoneNumber, int targetYear) {
		
		boolean newbegin = false;
		
		Map<String, Integer> rtn = null;

		List<UserTelephoneEntity> uTelList = userTelephoneDaoService
				.findTelephonesByUserIdAndTelephone(userId, telephoneNumber);
		if (uTelList == null || uTelList.size() <= 0) {
			log.info(">>> Available telephones is null.");
			return null;
		} else {
			for (UserTelephoneEntity tmpEnt : uTelList) {
				
				Calendar endCal = Calendar.getInstance();
				if (tmpEnt.getStatus().equals("INACTIVE")) {
					if(tmpEnt.getValidEndTime()!=null)
					{
						endCal.setTime(tmpEnt.getValidEndTime());
					}
					else{
						newbegin = true;
						endCal.setTime(new Date());
					}
					
				}
				
				Calendar tmpCal = Calendar.getInstance();
				tmpCal.setTime(tmpEnt.getValidStartingTime());
				int activeStartYear = tmpCal.get(Calendar.YEAR);
				int activeStartMonth = tmpCal.get(Calendar.MONTH) + 1;
				

				int activeEndYear = endCal.get(Calendar.YEAR);
				int activeEndMonth = endCal.get(Calendar.MONTH) + 1;
				
				if (targetYear < activeStartYear || targetYear > activeEndYear) {
					return null;
				} else if (targetYear == activeStartYear) {
					rtn = new HashMap<String, Integer>();
					rtn.put("START", new Integer(activeStartMonth));
					int end = (activeStartYear == activeEndYear) ? activeEndMonth : 12;
					rtn.put("END", new Integer(end));
				} else if (targetYear == activeEndYear) {
					rtn = new HashMap<String, Integer>();
					int start = (activeStartYear == activeEndYear) ? activeStartMonth : 1;
					rtn.put("START", new Integer(start));
					rtn.put("END", new Integer(activeEndMonth));
				} else {
					rtn = new HashMap<String, Integer>();
					rtn.put("START", new Integer(1));
					rtn.put("END", new Integer(12));
				}
				
				if ("ACTIVE".equals(tmpEnt.getStatus())) {
					return rtn;
				}
			}
		}
		
		return rtn;
	}

	private Map<String, Calendar> getValidPeriod(String userId,
			String telephoneNumber) {
		Map<String, Calendar> rtn = null;

		List<UserTelephoneEntity> uTelList = userTelephoneDaoService
				.findTelephonesByUserIdAndTelephone(userId, telephoneNumber);
		if (uTelList == null || uTelList.size() <= 0) {
			log.info(">>> Available telephones is null.");
			return null;
		} else {
			for (UserTelephoneEntity tmpEnt : uTelList) {
				Calendar startCal = Calendar.getInstance();
				if (tmpEnt.getValidStartingTime()!=null) {
					startCal.setTime(tmpEnt.getValidStartingTime());
				} else {
					startCal = null;
				}
				
				Calendar endCal = Calendar.getInstance();
				if (tmpEnt.getStatus().equals("INACTIVE")) {
					endCal.setTime(tmpEnt.getValidEndTime());
				}
				
				rtn = new HashMap<String, Calendar>();
				rtn.put("START-CALENDAR", startCal);
				rtn.put("END-CALENDAR", endCal);
				
				if ("ACTIVE".equals(tmpEnt.getStatus())) {
					return rtn;
				}
			}
		}
		
		return rtn;
	}
	
	/**
	 * get available month by telephone number and year.
	 * @param telNumber
	 * @param year
	 * @return
	 */
	public JSONObject getAvailableMonthsByTelAndYear(String userId,
			String telNumber, int year) {
		JSONObject jobj = new JSONObject();
		JSONArray theArray = new JSONArray();
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		Map<String, Integer> monthLimitMap = this.getMonthLimitStartAndEnd(
				userId, telNumber, year);
		
		List<TelephoneBillEntity> tbList = null;
		if (monthLimitMap != null && monthLimitMap.get("START") != null
				&& monthLimitMap.get("END") != null) {
			tbList = telephoneBillDaoService.findAvailableMonthsByTelAndYear(
					telNumber, year, monthLimitMap.get("START"),
					monthLimitMap.get("END"));
		}
		log.info(">>> " + ((tbList != null) ? tbList.size() : 0)
				+ " records were found.");
		
		if (tbList != null && tbList.size() > 0) {
			for (TelephoneBillEntity ent : tbList) {
				Integer entMonth = ent.getMonth();
				String entStatus = ent.getStatus();
				
				String mStatus = map.get(entMonth);
				if (mStatus == null) {
					map.put(entMonth, entStatus);
				} else {
					if ("INACTIVE".equals(mStatus) && "ACTIVE".equals(entStatus)) {
						map.put(entMonth, entStatus);
					}
				}
			}
		}
		
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			Integer month = entry.getKey();
			String status = entry.getValue();
			
			JSONObject tmpObj = new JSONObject();
			tmpObj.put("month", month);
			tmpObj.put("status", status);
			theArray.add(tmpObj);
		}  
		log.info(">>> theArray=" + theArray);
		
		jobj.put("monthList", theArray);
		jobj.put("returnCode", "SUCCESS");
		jobj.put("returnMessage", "SUCCESS");
		return jobj;
	}
	
	/**
	 * get available years by telephone number.
	 * @param telephoneNumber
	 * @return
	 */
	public JSONObject getAvailableYearsByTel(String telephoneNumber) {
		JSONObject jobj = new JSONObject();
		JSONArray yearArray = new JSONArray();
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		List<TelephoneBillEntity> tbList = telephoneBillDaoService
				.findAvailableYearsByTel(telephoneNumber);
		log.info(">>> "+((tbList!=null)?tbList.size():0)+" records were found.");
		
		if (tbList != null && tbList.size() > 0) {
			for (TelephoneBillEntity ent : tbList) {
				Integer entYear = ent.getYear();
				String entStatus = ent.getStatus();
				
				String mStatus = map.get(entYear);
				if (mStatus == null) {
					map.put(entYear, entStatus);
				} else {
					if ("INACTIVE".equals(mStatus) && "ACTIVE".equals(entStatus)) {
						map.put(entYear, entStatus);
					}
				}
			}
		}
		
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			Integer yyyy = entry.getKey();
			String status = entry.getValue();
			
			JSONObject tmpObj = new JSONObject();
			tmpObj.put("year", yyyy);
			tmpObj.put("status", status);
			yearArray.add(tmpObj);
		}  

		jobj.put("yearList", yearArray);
		jobj.put("returnCode", "SUCCESS");
		jobj.put("returnMessage", "SUCCESS");
		return jobj;
	}
	
	/**
	 * get particular telephone bill info.
	 * @param telephoneNumber
	 * @param year
	 * @param month
	 * @return
	 */
	public JSONObject getTelephoneBillInfo(String telephoneNumber,
			int year, int month) {
		JSONObject jobj = new JSONObject();
		try {
			TelephoneBillEntity tbEntity = telephoneBillDaoService.findTelephoneBillByTelNumberAndMonth(telephoneNumber, year, month);
			jobj.put("telephoneBillEntity", tbEntity);
			jobj.put("returnCode", "SUCCESS");
			jobj.put("returnMessage", "SUCCESS");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			jobj.put("returnCode", "FAILURE");
			jobj.put("returnMessage",
					"Fail to locate telephone bill information. System error: "
							+ e.getMessage());
		}

		return jobj;
	}
	
	/**
	 * inactivate telephone bill by telephone number, year and month.
	 * @param telephoneNumber
	 * @param year
	 * @param month
	 * @return
	 */
	public JSONObject inactivateTelephoneBillStatus(String telephoneNumber,
			int year, int month) {
		JSONObject jobj = new JSONObject();

//		int updateCount = telephoneBillDaoService.updateTelephoneBillStatus(
//				telephoneNumber, year, month, "INACTIVE");
		int updateCount = telephoneBillDaoService.updateTelephoneBillStatus(
				telephoneNumber, year, month, "SAVED");
		log.info(">>> updateCount=" + updateCount);
		
		jobj.put("updateCount", updateCount);

		jobj.put("returnCode", "SUCCESS");
		jobj.put("returnMessage", "SUCCESS");

		return jobj;
	}
	
	/**
	 * activate telephone bill by billId primary key.
	 * @param billId
	 * @return
	 */
	public JSONObject activateTelephoneBillStatus(long billId) {
		JSONObject jobj = new JSONObject();

		int updateCount = telephoneBillDaoService.updateTelephoneBillStatusByBillId(billId, "ACTIVE");
		log.info(">>> updateCount=" + updateCount);
		
		jobj.put("updateCount", updateCount);

		jobj.put("returnCode", "SUCCESS");
		jobj.put("returnMessage", "SUCCESS");

		return jobj;
	}
	
	public JSONObject getValidTelBillListForAdmin(String targetUserId,
			String telephoneNumber) {
		JSONObject jobj = new JSONObject();

		Map<String, Calendar> calLimitMap = this.getValidPeriod(targetUserId,
				telephoneNumber);
		if (calLimitMap == null) {
			return null;
		}
		
		Calendar startCal = calLimitMap.get("START-CALENDAR");
		if (startCal == null) {
			jobj.put("returnCode", "FAILURE");
			jobj.put("returnMessage", "Invalid starting date for this telephone.");
			return jobj;
		}
		Calendar endCal = calLimitMap.get("END-CALENDAR");
		
		log.info(">>> Query parameters: " + "telephoneNumber="
				+ telephoneNumber + ", timePeriod: "
				+ CommonUtil.formatDate(startCal.getTime(), "MM/dd/yyyy")
				+ "-" + CommonUtil.formatDate(endCal.getTime(), "MM/dd/yyyy"));
		List<TelephoneBillEntity> billList = telephoneBillDaoService
				.findBillsByTelTimePeriod(telephoneNumber, startCal, endCal);
		
		jobj.put("targetUserId", targetUserId);
		jobj.put("telephoneNumber", telephoneNumber);
		
		jobj.put("billList", billList);
		jobj.put("returnCode", "SUCCESS");
		jobj.put("returnMessage", "SUCCESS");
		
		return jobj;
	}
}
