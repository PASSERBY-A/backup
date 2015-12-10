/*
 * Copyright (c) 2002-2006 by OpenSymphony
 * All rights reserved.
 */

package com.hp.idc.common.core.interceptor;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.idc.common.Constant;
import com.hp.idc.common.core.view.NoLogin;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
/**
 * 用户超时认证拦截器
 * @ClassName: UserLoginInterceptor
 * @Descprition: TODO
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public class UserLoginInterceptor implements Interceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Log log = LogFactory.getLog(UserLoginInterceptor.class);

	public UserLoginInterceptor() {
		if (log.isDebugEnabled()) {
			log.debug("new UserLoginInterceptor()");
		}
	}

	public void init() {
		if (log.isDebugEnabled())
			log.debug("init()");
	}

	public void destroy() {
		if (log.isDebugEnabled())
			log.debug("destroy()");
	}
	
	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation invocation) throws Exception {
		Map session = invocation.getInvocationContext().getSession();
		if (session.get(Constant.SESSION_LOGIN)==null) {
			if (!(invocation.getAction() instanceof NoLogin)) {
				return "prelogin";
			}
		}
		return invocation.invoke();
	}

}
