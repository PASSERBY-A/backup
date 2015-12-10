package com.cmsz.cloudplatform.web.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * Struts2 interceptor base class
 * 
 * @author Zhefang Chen 
 *
 */
public abstract class BaseInterceptor extends AbstractInterceptor {
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	/**
	 * 
	 */
	public BaseInterceptor() {
		
	}
	protected void debug(Object msg) {
		log.debug(this.getClass()+" - " + msg);
	} 
//	
//	protected void auditTrail(BaseUser user) {
//		AuditTrail at = new AuditTrailImpl();
//		at.setUser(user);		
//		auditLog.log(at);	
//	}	
//	
//	protected String getMethodName(Exception e) { 
//		return e.getStackTrace()[0].getMethodName();
//	}
//	
//	protected void info(Exception e, String msg) {
//		log.info(this.getClass() + "." + this.getMethodName(e) + " - " + msg);
//	}
	
	protected HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest)ActionContext.
		getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		return request;
	}
}
