package com.volkswagen.tel.billing.billcall.biz;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillSumEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.*;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BillCallRecordBizService {
	private static final Logger log = LoggerFactory
			.getLogger(BillCallRecordBizService.class);

	@Autowired
	TelephoneBillDaoService telephoneBillDaoService;
	
	@Autowired
	BillCallRecordDaoService billCallRecordDaoService;
	
	@Autowired
	UserTelephoneDaoService userTelephoneDaoService;
	
	@Autowired
	UserPrivateNumberDaoService userPrivateNumberDaoService;

    @Autowired
    TelephoneBillSumDaoService telephoneBillSumDaoService;
	
	/**
	 * fetch call record from db by query criteria.
	 * @param userId - the owner of the telephone.
	 * @param telephoneNumber
	 * @param year
	 * @param month
	 * @param pageNumber - the page number to display.
	 * @param displayLength - the record count for display.
	 * @return
	 */
	public JSONObject getBillCallRecordsByTelAndMonth(String userId,
			String telephoneNumber, int year, int month, int pageNumber,
			int displayLength) {

		log.info("---------- getBillCallRecordsByTelAndMonth strart.");
		JSONObject jObj = new JSONObject();
        Date firstCalculationDay = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
		Date lastCalculationDay = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
		log.info(">>>1 firstCalculationDay=" + CommonUtil.formatDate(firstCalculationDay, "yyyy-MM-dd hh:mm:ss"));
		log.info(">>>1 lastCalculationDay=" + CommonUtil.formatDate(lastCalculationDay, "yyyy-MM-dd hh:mm:ss"));
		
		JSONArray resultList = new JSONArray();
		boolean isEditable = true;
		long iTotalRecords = 0;
		long iTotalDisplayRecords = 0;
		
		try {
			log.info(">>> userId="+userId+", telephoneNumber="+telephoneNumber);
			List<UserTelephoneEntity> uTelList = userTelephoneDaoService
					.findTelephonesByUserIdAndTelephone(userId, telephoneNumber);
			if (uTelList == null || uTelList.size() <= 0) {
				log.info(">>> Available telephones is null.");
			} else {
				for (UserTelephoneEntity tmpEnt : uTelList) {
					if (tmpEnt.getStatus().equals("ACTIVE")) {
						if (tmpEnt.getValidStartingTime().before(
								firstCalculationDay)) {
						} else {
							firstCalculationDay = tmpEnt.getValidStartingTime();
						}
					} else {
						lastCalculationDay = tmpEnt.getValidEndTime();
					}

					log.info(">>> firstCalculationDay=" + firstCalculationDay);
					log.info(">>> lastCalculationDay=" + lastCalculationDay);

					TelephoneBillEntity tbEntity = telephoneBillDaoService
							.findTelephoneBillByTelNumberAndMonth(
									telephoneNumber, year, month);
					
					if (tbEntity!=null && !"ACTIVE".equals(tbEntity.getStatus())) {
						isEditable = false;
					}

					List<BillCallRecordEntity> callList = billCallRecordDaoService
							.findBillCallRecordsByTelNumberAndMonthAndDatePeriod(
									telephoneNumber, year, month,
									firstCalculationDay, lastCalculationDay,
									pageNumber, displayLength,
									Sort.Direction.ASC, "dateOfCall");
					
					resultList = this.convertToJSONArrayStructure(callList, isEditable);
					iTotalRecords = billCallRecordDaoService
							.countBillCallRecordsByTelNumberAndMonthAndDatePeriod(
									telephoneNumber, year, month,
									firstCalculationDay, lastCalculationDay);
					iTotalDisplayRecords = iTotalRecords;
					break;
				}
			}
			
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}
		
		jObj.put("resultList", resultList);
		jObj.put("isEditable", isEditable);
		jObj.put("iTotalRecords", iTotalRecords);
		jObj.put("iTotalDisplayRecords", iTotalDisplayRecords);

		log.info("---------- getBillCallRecordsByTelAndMonth end.");
		return jObj;
	}

	/**
	 * update call record as private by recordId.
	 * @param recordId - call record primary key.
	 * @param isPrivate - 1: private call, 0: company call.
	 * @return
	 */
	public JSONObject updatePrivatePurposeByRecordId(long recordId,
			int isPrivate) {
		log.info("---------- updatePrivatePurposeByRecordId strart.");
		log.info(">>> recordId=" + recordId);
		log.info(">>> isPrivate=" + isPrivate);
		JSONObject jObj = new JSONObject();
		
		int updateCount = billCallRecordDaoService
				.updatePrivatePurposeByRecordId(recordId, isPrivate);

		log.info(">>> updateCount=" + updateCount);
		jObj.put("updateCount", updateCount);

		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		log.info("---------- updatePrivatePurposeByRecordId end.");
		return jObj;
	}
	
	public JSONObject getNumbersToExecuteInBatch(String userId,
			String callingNumber, int year, int month) {
		log.info("---------- getNumbersToExecuteInBatch strart.");
		JSONObject jObj = new JSONObject();

		List<String> storedPrivateNumberList = userPrivateNumberDaoService
				.findPrivateNumberStringListByUserId(userId);

		List<String> calledNumberList = billCallRecordDaoService
				.findCalledNumbersByTelNumberAndMonth(callingNumber, year,
						month);

		List<String> numbersToExecuteInBatch = composeListToExecute(storedPrivateNumberList,
				calledNumberList);
		if (numbersToExecuteInBatch != null && numbersToExecuteInBatch.size() > 0) {
			jObj.put("numbersToExecuteInBatch", numbersToExecuteInBatch);
		}
		jObj.put("returnCode", "SUCCESS");
		
		log.info("---------- getNumbersToExecuteInBatch end.");
		return jObj;
	}
	
	public JSONObject updatePrivatePurposeInBatch(String callingNumber,
			int year, int month, String calledNumbers, int isPrivate) {
		log.info("---------- updatePrivatePurposeInBatch strart.");
		log.info(">>> callingNumber=" + callingNumber + ", year=" + year
				+ ", month=" + month + ", calledNumbers=" + calledNumbers
				+ ", isPrivate=" + isPrivate);
		JSONObject jObj = new JSONObject();

		List<String> numbersToExecute = null;
		if (calledNumbers != null && calledNumbers.trim().length() > 0) {
			numbersToExecute = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(calledNumbers.trim(), ",");
			while (st.hasMoreTokens()) {
				numbersToExecute.add(st.nextToken());
			}
		}
		
		int updateCount = 0;
		if (numbersToExecute != null) {
			updateCount = billCallRecordDaoService
					.updatePrivatePurposeByPrivateNumberStringList(isPrivate,
							callingNumber, numbersToExecute, year, month);
			jObj.put("privateNumberList", numbersToExecute);
		}
		log.info(">>> updateCount=" + updateCount);
		jObj.put("updateCount", updateCount);

		jObj.put("returnCode", "SUCCESS");
		if (isPrivate == 1) {
			jObj.put("returnMessage", "Mark successfully!");
		} else {
			jObj.put("returnMessage", "Unmark successfully!");
		}

		log.info("---------- updatePrivatePurposeInBatch end.");
		return jObj;
	}
	
	/**
	 * Iterate the called numbers to get those ending with any private number
	 * only.
	 * 
	 * @param privateNumberList
	 * @param calledNumberList
	 * @return
	 */
	private List<String> composeListToExecute(List<String> privateNumberList,
			List<String> calledNumberList) {
		List<String> rtn = null;
		if (privateNumberList == null || privateNumberList.size() <= 0
				|| calledNumberList == null || calledNumberList.size() <= 0) {
			return null;
		}

		Set<String> calledPrivateSet = new HashSet<String>();
		for (String privateNum : privateNumberList) {
			for (String calledNum : calledNumberList) {
				if (calledNum != null) {
					if (calledNum.endsWith(privateNum)) {
						calledPrivateSet.add(calledNum);
					}
				}
			}
		}
		log.info(">>> Numbers to mark: " + calledPrivateSet);

		// - sort number from right side.
		if (calledPrivateSet != null && calledPrivateSet.size() > 0) {
			List<String> sList = new ArrayList<String>();
			for (String str : calledPrivateSet) {
				sList.add(new StringBuilder(str).reverse().toString());
			}

			Collections.sort(sList);
			rtn = new ArrayList<String>();
			for (String tmp : sList) {
				rtn.add(new StringBuilder(tmp).reverse().toString());
			}
		}
		
		return rtn;
	}
	
	/**
	 * Convert call record list to json array for front-end display.
	 * @param list - call record list fetched from db.
	 * @param isEditable - flag for identifying if the call record can be editable on front-end.
	 * @return
	 */
	private JSONArray convertToJSONArrayStructure(
			List<BillCallRecordEntity> list, boolean isEditable) {
		JSONArray data = null;

		if (list != null) {
			data = new JSONArray();
			for (BillCallRecordEntity ent : list) {
				JSONArray row = new JSONArray();
				
				row.add(ent.getDateOfCall()); 				// - 0
				row.add(ent.getStartingTime()); 			// - 1
				row.add(ent.getDuration()); 				// - 2
				row.add(ent.getCalledNumber());  			// - 3
				row.add(ent.getCommunicationType()); 		// - 4
				row.add(ent.getLocation()); 				// - 5
				row.add(ent.getCost()); 					// - 6
				row.add(Integer.toString(ent.getPrivatePurpose())); // - 7
				row.add(isEditable);						// - 8
				row.add(ent.getCallRecordId().toString());	// - 9

				data.add(row);
			}
		}

		return data;
	}

    public JSONObject getBillCallSumByTelAndMonth(String userId, String telephoneNumber, int year, int month) {

        log.info("---------- getBillCallSumByTelAndMonth strart.");
        JSONObject jObj = new JSONObject();
        Date firstCalculationDay = CommonUtil.getCalendar(year, month, 1, 0, 0, 0).getTime();
        Date lastCalculationDay = CommonUtil.getCalendar(year, month + 1, 1, 0, 0, 0).getTime();
        log.info(">>>1 firstCalculationDay=" + CommonUtil.formatDate(firstCalculationDay, "yyyy-MM-dd hh:mm:ss"));
        log.info(">>>1 lastCalculationDay=" + CommonUtil.formatDate(lastCalculationDay, "yyyy-MM-dd hh:mm:ss"));
        try {
            log.info(">>> userId="+userId+", telephoneNumber="+telephoneNumber);
			float monthPkg = 0.0f;
			float dataBoPkg = 0.0f;
			float smsBoPkg = 0.0f;
			float roamingBoPkg = 0.0f;
			TelephoneBillSumEntity billSumEntity = telephoneBillSumDaoService.findBillSumByUserIdAndTelephone(telephoneNumber, year, month);
			if (billSumEntity != null) {
				monthPkg = billSumEntity.getMonthPkg();
				dataBoPkg = billSumEntity.getDataBoPkg();
				roamingBoPkg = billSumEntity.getRoamingBoPkg();
				smsBoPkg = billSumEntity.getSmsBoPkg();
			}
			jObj.put("monthPkg", monthPkg);
			jObj.put("dataBoPkg", dataBoPkg);
			jObj.put("smsBoPkg", smsBoPkg);
			jObj.put("roamingBoPkg", roamingBoPkg);

            jObj.put("returnCode", "SUCCESS");
            jObj.put("returnMessage", "SUCCESS");
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            jObj.put("returnCode", "FAILURE");
            jObj.put("returnMessage", "System error: " + e.getMessage());
        }

        log.info("---------- getBillCallSumByTelAndMonth end.");
        return jObj;
    }
}
