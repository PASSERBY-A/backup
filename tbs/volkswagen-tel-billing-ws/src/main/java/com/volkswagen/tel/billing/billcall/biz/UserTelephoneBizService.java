package com.volkswagen.tel.billing.billcall.biz;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Component
public class UserTelephoneBizService {
	private static final Logger log = LoggerFactory
			.getLogger(UserTelephoneBizService.class);

	@Autowired
	UserTelephoneDaoService userTelephoneDaoService;

	/**
	 * get telephones of a particular user.
	 * @param userId
	 * @return
	 */
	public JSONObject getTelephonesByUserId(String userId) {
		JSONObject jObj = null;

		List<UserTelephoneEntity> userTelList = userTelephoneDaoService
				.findTelephonesByUserId(userId);
		
		Set<String> telephoneSet = null;
		if (userTelList != null && userTelList.size() >0) {
			telephoneSet = new HashSet<String>();
			for (UserTelephoneEntity utEnt: userTelList) {
				telephoneSet.add(utEnt.getTelephoneNumber());
			}
			log.info(">>> telephoneSet=" + telephoneSet);
			
			jObj = new JSONObject();
			jObj.put("userTelList", telephoneSet);

			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "No available telephone number is found.");
		}

		return jObj;
	}
	
	public JSONObject getListByUserId(String userId, int pageNumber,
			int displayLength) {
		log.info("========== getListByUserId strart.");
		JSONObject jObj = new JSONObject();

		JSONArray resultList = new JSONArray();
		long iTotalRecords = 0;
		long iTotalDisplayRecords = 0;
		
		try {
			log.info(">>> userId=" + userId);
			List<UserTelephoneEntity> uTelList = null;
			if (userId != null && userId.length() > 0) {
				uTelList = userTelephoneDaoService
						.findByUserId(userId, pageNumber, displayLength,
								Sort.Direction.ASC, "userId");
				iTotalRecords = userTelephoneDaoService.countByUserId(userId);
			} else {
				uTelList = userTelephoneDaoService.findAllEntities(pageNumber,
						displayLength, Sort.Direction.ASC, "userId");
				iTotalRecords = userTelephoneDaoService.countAllEntities();
			}
			iTotalDisplayRecords = iTotalRecords;
			
			if (uTelList == null || uTelList.size() <= 0) {
				log.info(">>> User list is null.");
			} else {
				resultList = this.convertToJSONArrayStructure(uTelList);
			}
			
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			jObj.put("returnCode", "FAILURE");
			jObj.put("returnMessage", "System error: " + e.getMessage());
		}
		
		jObj.put("resultList", resultList);
		jObj.put("iTotalRecords", iTotalRecords);
		jObj.put("iTotalDisplayRecords", iTotalDisplayRecords);

		log.info("========== getListByUserId end.");
		return jObj;
	}

	private JSONArray convertToJSONArrayStructure(List<UserTelephoneEntity> list) {
		JSONArray data = null;

		if (list != null) {
			data = new JSONArray();
			for (UserTelephoneEntity ent : list) {
				JSONArray row = new JSONArray();
				
				row.add(ent.getUserId()); 				// - 0
				row.add(ent.getTelephoneNumber()); 		// - 1
				row.add(ent.getStatus()); 				// - 2
				
				StringBuilder validPeriodBuilder = new StringBuilder();
				validPeriodBuilder.append(CommonUtil.formatDate(
						ent.getValidStartingTime(), "MM/dd/yyyy"));
				if ("ACTIVE".equals(ent.getStatus())) {
					validPeriodBuilder.append(" - Now");
				} else {
					validPeriodBuilder.append(" - ");
					validPeriodBuilder.append(CommonUtil.formatDate(
							ent.getValidEndTime(), "MM/dd/yyyy"));
				}
				row.add(validPeriodBuilder.toString());	// - 3

				data.add(row);
			}
		}

		return data;
	}
}
