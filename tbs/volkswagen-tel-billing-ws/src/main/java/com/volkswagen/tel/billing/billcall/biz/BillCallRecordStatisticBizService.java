package com.volkswagen.tel.billing.billcall.biz;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillStatisticDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;


@Component
public class BillCallRecordStatisticBizService {
	
	private static final Logger log = LoggerFactory
			.getLogger(BillCallRecordStatisticBizService.class);
	
	@Autowired
	TelephoneBillStatisticDaoService telephoneBillStatistiDaoService;
	
	@Autowired
	UserTelephoneDaoService userTelephoneDaoService;
	
	public JSONObject getCommunicationTypeStatisticByCallingNumerYearMonth(String userId,
			String callingNumber, int year, int month) {
		
		JSONObject jObj = new JSONObject();		
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

		log.info(">>> userId=" + userId + ", telephoneNumber="
				+ callingNumber);
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

				List<Object> resultList = telephoneBillStatistiDaoService.getCommunicationTypeStatisticByCallingNumerYearMonth(callingNumber, year, month, firstCalculationDay, lastCalculationDay);

				jObj.put("resultList", resultList);
				break;
			}
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");

			log.info("get call type statistic for calling number " + callingNumber);
		}
		return jObj;
	}
	
	
	public JSONObject getSavedCallRecordList(String userId,
			String callingNumber, int year, int month) {
		
		JSONObject jObj = new JSONObject();		
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

		log.info(">>> userId=" + userId + ", telephoneNumber="
				+ callingNumber);
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
				
				List<BillCallRecordEntity> resultList = telephoneBillStatistiDaoService.getSavedCallRecordList(callingNumber, year, month, firstCalculationDay, lastCalculationDay);

				jObj.put("resultList", resultList);
				break;
			}
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");

			log.info("get call type statistic for calling number " + callingNumber);
		}
		return jObj;
	}
}
