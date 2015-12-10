package com.volkswagen.tel.billing.billcall.biz;

import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserPrivateNumberEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.UserPrivateNumberDaoService;

@Component
public class UserPrivateNumberBizService {
	private static final Logger log = LoggerFactory
			.getLogger(UserPrivateNumberBizService.class);

	@Autowired
	UserPrivateNumberDaoService userPrivateNumberDaoService;
	
	public JSONObject savePrivateNumber(String userId, String privateNumber) {
		JSONObject jObj = new JSONObject();

		UserPrivateNumberEntity entity = new UserPrivateNumberEntity();
		entity.setUserId(userId);
		entity.setPrivateNumber(privateNumber);
		entity.setStatus("ACTIVE");
		entity.setLastUpdateTime(Calendar.getInstance().getTime());
		entity = userPrivateNumberDaoService.savePrivateNumber(entity);
		log.info(">>> Saved entity info: " + entity);
		
		jObj.put("userPrivateEntityEntity", entity);
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		return jObj;
	}
	
	public JSONObject deletePrivateNumberById(long entityId) {
		JSONObject jObj = new JSONObject();

		userPrivateNumberDaoService.deletePrivateNumberById(entityId);
		
		jObj.put("returnCode", "SUCCESS");
		jObj.put("returnMessage", "SUCCESS");

		return jObj;
	}
	
	public JSONObject getPrivateNumbers(String userId) {
		JSONObject jObj = null;

		List<UserPrivateNumberEntity> upnList = userPrivateNumberDaoService
				.findPrivateNumbersByUserId(userId);

		if (upnList != null && upnList.size() > 0) {
			log.info(">>> User private number list: " + upnList);
			
			jObj = new JSONObject();
			jObj.put("userPrivateNumberList", upnList);

			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "SUCCESS");
		} else {
			jObj = new JSONObject();
			jObj.put("returnCode", "SUCCESS");
			jObj.put("returnMessage", "No available users is found.");
		}

		return jObj;
	}
}
