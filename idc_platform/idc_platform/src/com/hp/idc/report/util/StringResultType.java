package com.hp.idc.report.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class StringResultType extends StrutsResultSupport {

	protected final Log log = LogFactory.getLog(StringResultType.class);

	private static final long serialVersionUID = -2659052470244868842L;

	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
		str = (String) invocation.getStack().findValue(
				conditionalParse(finalLocation, invocation));
		ActionContext context = invocation.getInvocationContext();
		HttpServletResponse response = (HttpServletResponse) context
				.get(HTTP_RESPONSE);
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		try {
			writer.write(str);
		} finally{
			if(writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}

}
