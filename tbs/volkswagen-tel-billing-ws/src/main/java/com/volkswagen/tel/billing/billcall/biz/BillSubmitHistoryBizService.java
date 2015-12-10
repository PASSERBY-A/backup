package com.volkswagen.tel.billing.billcall.biz;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillSubmitHistoryEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.BillSubmitHistoryDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillStatisticDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.common.GenericConfig;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Component
public class BillSubmitHistoryBizService {
	private static final Logger log = LoggerFactory
			.getLogger(BillSubmitHistoryBizService.class);

	@Autowired
	TelephoneBillStatisticDaoService telephoneBillStatisticDaoService;

	@Autowired
	UserTelephoneDaoService userTelephoneDaoService;
	
	@Autowired
	BillSubmitHistoryDaoService billSubmitHistoryDaoService;

	@Autowired
	UserInfoBizService userInfoBizService;
	
	@Autowired
	TelephoneBillDaoService telephoneBillDaoService;
	
	public JSONObject submitToSap(String userId, String telephoneNumber,
			int billingYear, int billingMonth) {
		log.info("========== submitToSap start.");
		JSONObject jObj = null;
		
		// - telephone number type
		String numberType = "";
		if (telephoneNumber.length()>=11) {
			numberType = "MOBILE";
		} else {
			numberType = "FIXED";
		}

		// - staff code
		String staffCode = "";
		JSONObject usrJsonObj = userInfoBizService.getUserInfoByUserId(userId);
		if (usrJsonObj != null && usrJsonObj.getString("staffCode") != null) {
			staffCode = usrJsonObj.getString("staffCode");
		}

		// - amount
		double billingValue = this.calculateTotalBillingValue(userId,
				telephoneNumber, billingYear, billingMonth);
		BigDecimal bd = new BigDecimal(billingValue);
		billingValue = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		DecimalFormat df3 = new DecimalFormat("0.00");
		String billingValueStr = df3.format(billingValue);
		
		// - count times of submit
		String typeOfSubmit = "INITIAL";
		long countOfSubmit = billSubmitHistoryDaoService.countBillSubmitTimes(
				staffCode, telephoneNumber, billingYear, billingMonth);
		if (countOfSubmit >= 1) {
			typeOfSubmit = "CORRECTION";
		}
		
		log.info(">>> staffCode=" + staffCode + ", telephoneNumber="
				+ telephoneNumber + ", numberType=" + numberType
				+ ", billingYear=" + billingYear
				+ ", billingMonth=" + billingMonth
				+ ", billingValueStr=" + billingValueStr
				+ ", typeOfSubmit=" + typeOfSubmit);
		
		Calendar submitCal = Calendar.getInstance(); 
		
		StringBuilder contentSB = new StringBuilder();
		contentSB.append(staffCode).append(";")
				.append(numberType).append(";")
				.append(telephoneNumber).append(";")
				.append(billingYear).append(";")
				.append(billingMonth).append(";")
				.append(billingValueStr).append(";")
				.append(CommonUtil.formatDate(submitCal.getTime(), "yyyyMMdd HH:mm:ss")).append(";")
				.append(typeOfSubmit);
		
		if (this.sendToRvsDrive(staffCode, submitCal, contentSB.toString())) {
			BillSubmitHistoryEntity bshEntity = this.saveSubmitHistory(userId,
					staffCode, telephoneNumber, billingYear, billingMonth,
					(float) billingValue, typeOfSubmit);
			
			int updateCount = telephoneBillDaoService.updateTelephoneBillStatus(
					telephoneNumber, billingYear, billingMonth, "SENT");
			
			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "This summary information has been sent to SAP system successfully.");
		} else {
			log.error(">>> Failed to send file to RVS drive.");
			
			jObj = new JSONObject();
			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "Failed to send file to RVS drive.");
		}
		log.info("========== submitToSap end.");
		return jObj;
	}

	private BillSubmitHistoryEntity saveSubmitHistory(String userId,
			String staffCode, String telephoneNumber, int billingYear,
			int billingMonth, float billingValue, String typeOfSubmit) {
		BillSubmitHistoryEntity entity = new BillSubmitHistoryEntity();
		entity.setUserId(userId);
		entity.setStaffCode(staffCode);
		entity.setTelephoneNumber(telephoneNumber);
		entity.setBillingYear(billingYear);
		entity.setBillingMonth(billingMonth);
		entity.setBillingValue(billingValue);
		entity.setDateOfSubmit(Calendar.getInstance().getTime());
		entity.setTypeOfSubmit(typeOfSubmit);
		entity = billSubmitHistoryDaoService.saveBillSubmitHistory(entity);
		return entity;
	}
	
	private boolean sendToRvsDrive(String staffCode, Calendar submitCal,
			String content) {
		boolean rtn = false;
		
		String targetDirectory = GenericConfig.sapSubmitDirectory;
		StringBuilder fnBuilder = new StringBuilder();
		fnBuilder.append(targetDirectory).append(File.separator);
				if(staffCode!=null && !staffCode.equals("") && !staffCode.contains("N/A"))
				{
				 
					fnBuilder.append(staffCode).append("_");
				}
				
		fnBuilder.append(CommonUtil.formatDate(submitCal.getTime(), "yyyyMMddHHmmss")).append("_")
				.append(CommonUtil.generateRandomNumber(1000, 9999))
				.append(".txt");

		rtn = CommonUtil.writeToFile(fnBuilder.toString(), content);
		log.info(">>> File written to path: " + fnBuilder.toString());
		return rtn;
	}

	private double calculateTotalBillingValue(String userId,
			String telephoneNumber, int year, int month) {
		double rtn = 0;

		List<Object> statList = this.getStatisticsByType(userId,
				telephoneNumber, year, month);
		log.info(">>>>>> statList=" + statList);
		
		
		if (statList!=null) {
			for (int i=0; i<statList.size(); i++) {
				Object[] oArray = (Object[])statList.get(i);
				rtn += ((Double)oArray[2]).floatValue();
			}
		}

		return rtn;
	}

	private List<Object> getStatisticsByType(String userId,
			String callingNumber, int year, int month) {
		List<Object> resultList = null;

		Date firstCalculationDay = CommonUtil.getCalendar(year, month, 1, 0, 0,
				0).getTime();
		Date lastCalculationDay = CommonUtil.getCalendar(year, month + 1, 1, 0,
				0, 0).getTime();
		log.info(">>>1 firstCalculationDay="
				+ CommonUtil.formatDate(firstCalculationDay,
						"yyyy-MM-dd hh:mm:ss"));
		log.info(">>>1 lastCalculationDay="
				+ CommonUtil.formatDate(lastCalculationDay,
						"yyyy-MM-dd hh:mm:ss"));

		log.info(">>> userId=" + userId + ", telephoneNumber=" + callingNumber);
		List<UserTelephoneEntity> uTelList = userTelephoneDaoService
				.findTelephonesByUserIdAndTelephone(userId, callingNumber);
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

				resultList = telephoneBillStatisticDaoService
						.getCommunicationTypeStatisticByCallingNumerYearMonth(
								callingNumber, year, month,
								firstCalculationDay, lastCalculationDay);
				break;
			}
		}
		return resultList;
	}

}
