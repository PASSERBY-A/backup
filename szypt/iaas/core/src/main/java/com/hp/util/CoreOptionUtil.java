package com.hp.util;

import com.hp.core.Constants;
import com.hp.core.configuration.impl.AppConfigMgr;

public class CoreOptionUtil {
	private static final String EDITLOCK_ENABLED = "editlock.enabled";
	private static final String DB_CONFIG_RELOAD_SPACETIME = "dbconfig.reload.spacetime";
	private static final String EDITLOCK_MAX_LOCK_DURATION = "editlock.max.lock.duration";
	private static final String APPLICATION_ID = "application.id";
	private static final String CHANGE_PASSWORD_PAGEPATH = "changepassword.pagepath";
	private static final String USER_LOGIN_ATTEMPT_TIMES = "user.login.attempt.times";
	private static final String DEFAULT_AUDIT_TRAIL_ENABLE = "default.audit.trail.enable";
	
	private static final AppConfigMgr appConfig = AppConfigMgr.getInstance();	
	
	private CoreOptionUtil() {
		//private
	}
	
	public static Long obtainReloadSpacetimeForDbConfig() {
		Long spacetime = appConfig.getLongValue(DB_CONFIG_RELOAD_SPACETIME);
		if (null == spacetime) {
			spacetime = Constants.DEFAULT_DB_CONFIG_RELOAD_SPACETIME;
		}
		
		return spacetime;
	}
	
	public static boolean obtainEditLockIsEnabled() {
		Boolean lockIsEnabled = appConfig.getBooleanValue(EDITLOCK_ENABLED);
		if (null == lockIsEnabled || false == lockIsEnabled.booleanValue()) {
			return false;
		}
		return true;
	}
	
	public static Integer obtainEditLockMaxLockDuration() {
		Integer max = appConfig.getIntegerValue(EDITLOCK_MAX_LOCK_DURATION);
		if (null == max) {
			max = Constants.DEFAULT_EDITLOCK_MAX_LOCK_DURATION;
		}
		return max;
		
	}

	public static Long obtainApplictionId() {
		Long appId = appConfig.getLongValue(APPLICATION_ID);
		if (null == appId) {
			appId = Constants.DEFAULT_APPLICATION_ID;
		}
		return appId;
	}
	
	public static String obtainChangePasswordPage() {
		String pagePath = appConfig.getValue(CHANGE_PASSWORD_PAGEPATH);
		if (false == StringUtil.isNotEmpty(pagePath)) {
			pagePath = Constants.DEFAULT_CHANGE_PASSWORD_PATH;
		}
		return pagePath;
	}
	
	public static Integer obtainUserLoginAttemptTimes() {
		Integer times = appConfig.getIntegerValue(USER_LOGIN_ATTEMPT_TIMES);
		if (null == times) {
			times = Constants.DEFAULT_USER_LOGIN_ATTEMPT_TIMES;
		}
		return times;
	}
	
	public static Boolean obtainDefaultAuditTrailEnable() {
		Boolean value = appConfig.getBooleanValue(DEFAULT_AUDIT_TRAIL_ENABLE);
		if (null == value) {
			value = Constants.DEFAULT_AUDIT_TRAIL_ENABLE;
		}
		return value;
	}
}
