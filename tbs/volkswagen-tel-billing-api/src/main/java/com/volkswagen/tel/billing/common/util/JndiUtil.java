package com.volkswagen.tel.billing.common.util;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JndiUtil {

	private static Log log = LogFactory.getLog(JndiUtil.class);

	public static Object lookupJndi(String name) {
		try {
			InitialContext ic = new InitialContext();
			Context envCtx = (Context) ic.lookup("java:comp/env");
			try {
				Object o = envCtx.lookup(name);
				log.info("successful lookup of " + name + " (" + o + ")");
				return o;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error("error looking up jndi stuff: ", e);
		}
		return null;
	}

	public static String lookupJndiParameter(String paramName) {
		try {
			String s = (String) lookupJndi(paramName);
			log.info("found jndi env at: " + paramName + " /// returning: " + s);
			return s;
		} catch (Exception e) {
			log.error("error looking up jndi stuff: ", e);
		}
		return null;
	}
}
