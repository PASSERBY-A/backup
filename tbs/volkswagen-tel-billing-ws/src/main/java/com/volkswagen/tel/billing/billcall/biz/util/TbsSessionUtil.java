package com.volkswagen.tel.billing.billcall.biz.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TbsSessionUtil {
	private static final Logger log = LoggerFactory
			.getLogger(TbsSessionUtil.class);

	private static final String USER_ID_KEY = "iv-user";

	/**
	 * Get user id from session header of the logged in user.
	 * 
	 * @param request
	 * @return
	 */
	public static String getUidFromSession(HttpServletRequest request) {
		String uid = request.getHeader(USER_ID_KEY);
		//uid="ELIQIAG";
		//uid ="ELIYUA0";
		//uid="ELIFENG";
		//uid="EWEBAR0";
		log.info(">>> User session: " + uid);
		return uid;
	}

	/**
	 * Save user id to session header of the logged in user.
	 * 
	 * @param request
	 * @return
	 */
	public static void saveUidToSession(HttpServletResponse response, String uid) {
		response.setHeader(USER_ID_KEY, uid);

		log.info(">>> Set user session - " + uid);
	}
}
