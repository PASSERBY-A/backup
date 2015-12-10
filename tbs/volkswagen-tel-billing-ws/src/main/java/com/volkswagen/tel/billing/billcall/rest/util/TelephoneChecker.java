package com.volkswagen.tel.billing.billcall.rest.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.UserTelephoneDaoService;

@Component
public class TelephoneChecker {
	private static final Logger log = LoggerFactory
			.getLogger(TelephoneChecker.class);

	@Autowired
	UserTelephoneDaoService userTelephoneDaoService;

	/**
	 * judge if the user is the owner of the telephone.
	 * @param userId
	 * @param telephoneNumber
	 * @return
	 */
	public boolean isValidTelephone(String userId, String telephoneNumber) {
		boolean rtn = false;

		List<UserTelephoneEntity> userTelList = userTelephoneDaoService
				.findTelephonesByUserId(userId);
		if (userTelList != null) {
			for (UserTelephoneEntity ent : userTelList) {
				if (telephoneNumber.equals(ent.getTelephoneNumber())) {
					rtn = true;
					break;
				}
			}
		}

		log.info(">>> isValidTelephone()=" + rtn);
		return rtn;
	}
}
