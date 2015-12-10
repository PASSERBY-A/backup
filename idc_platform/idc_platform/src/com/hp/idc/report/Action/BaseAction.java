package com.hp.idc.report.Action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.hp.idc.context.util.ContextUtil;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public abstract class BaseAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, ServletContextAware {

	private static final long serialVersionUID = 6279754118729869294L;
	protected final Log log = LogFactory.getLog(getClass());
	
	
	private static Map<String, String> stypeMap = new HashMap<String, String>();
	static {
		stypeMap.put("xls", "application/vnd.ms-excel");
		stypeMap.put("exe", "application/x-msdownload");
	}
	
	protected final String TPL = "tpl";
	protected final String JSP = "jsp";
	protected final String HTML = "html";
	protected final String INPUT = "input";
	protected final String STR = "str";
	protected final String INDEX = "index";
	protected final String JSON = "json";
	protected final String ERROR = "error";
	protected final String STREAM = "stream";

	protected HttpServletRequest req;
	protected HttpServletResponse res;

	private InputStream inputStream;
	private String streamType;

	private String target;
	private String str; // for toSTR method

	private String jsonType;
	private Object jsonValue;

	private String prefix;
	private String suffix;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getStr() {
		return str;
	}

	public String getJsonType() {
		return jsonType;
	}

	public Object getJsonValue() {
		return jsonValue;
	}

	// public void setStr(String str) {
	// this.str = str;
	// }

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getStreamType() {
		return streamType;
	}

	/** ******** begin forward ********* */
	protected String toTPL(String _target) {
		setTarget(_target);
		return TPL;
	}

	protected String toJSP(String _target) {
		setTarget(_target);
		return JSP;
	}

	protected String toHTML(String _target) {
		setTarget(_target);
		return HTML;
	}

	protected String toSTR(String str) {
		this.str = str;
		return STR;
	}

	protected String toINDEX() {
		return INDEX;
	}

	public String toINPUT() {
		// ActionContext ctx = ActionContext.getContext();
		// HttpServletRequest req = (HttpServletRequest) ctx
		// .get(ServletActionContext.HTTP_REQUEST);
		// setTarget(req.getHeader("referer"));
		// logger.info("path========>"+req.getContextPath());
		setTarget("init/Index/index");
		return INPUT;
	}

	public String toJSON(String jsonStr) {
		this.jsonType = "jsonStr";
		this.jsonValue = jsonStr;
		return JSON;
	}

	public String toJSON(Object jsonObj) {
		this.jsonType = "jsonObj";
		this.jsonValue = jsonObj;
		return JSON;
	}

	public String toJSON(List jsonArr) {
		this.jsonType = "jsonArr";
		this.jsonValue = jsonArr;
		return JSON;
	}

	public String toJSON(String jsonStr, String prefix, String suffix) {
		this.jsonType = "jsonStr";
		this.jsonValue = jsonStr;
		this.prefix = prefix;
		this.suffix = suffix;
		return JSON;
	}

	public String toJSON(Object jsonObj, String prefix, String suffix) {
		this.jsonType = "jsonObj";
		this.jsonValue = jsonObj;
		this.prefix = prefix;
		this.suffix = suffix;
		return JSON;
	}

	public String toJSON(List jsonArr, String prefix, String suffix) {
		this.jsonType = "jsonArr";
		this.jsonValue = jsonArr;
		this.prefix = prefix;
		this.suffix = suffix;
		return JSON;
	}

	public String toERROR(String str) {
		this.str = "{success:false,info:'" + str + "'}";
		return STR;
	}

	public String toXLS(InputStream is) {
		this.streamType = stypeMap.get("xls");
		this.inputStream = is;
		return STREAM;
	}

	/** ******** end forward ********* */


	// If no such method
	public String doDefault() {
		List<String> errors = new ArrayList<String>();
		errors.add(getText("err.unauthorized_access"));
		this.setActionErrors(errors);
		return toINPUT();
	}

	public void setServletRequest(HttpServletRequest req) {
		this.req = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.res = res;
	}

	public Object getBean(String name) {
		return ContextUtil.getBean(name);
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 *@description 通过request中的参数，构造分页对象
	 */
//	public Page getPage() {
//		String start = req.getParameter("start");
//		String limit = req.getParameter("limit");
//		return new Page(start, limit);
//	}

}
