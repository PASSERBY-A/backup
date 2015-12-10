package com.hp.idc.common.core.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.tool.hbm2x.StringUtils;

import com.hp.idc.common.Constant;
import com.hp.idc.system.security.entity.SysUser;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 抽象action类
 * struts2 action应继承该抽象类
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 *
 */
public class AbstractAction extends ActionSupport {

	/**
	 * rand serial uid
	 */
	private static final long serialVersionUID = -1575326270138198214L;
	
	public static final String ACT_MODIFY = "modify";
	
	/**
	 * Json Support define
	 */
	public static final String JSON_TOTAL_COUNT = "totalCount";
	public static final String JSON_RESULT = "result";
	public static final String JSON_RET_SUCCESS = "success";
	public static final String JSON_RET_MESSAGE = "message";
	
	/**
	 * log4j.xml
	 */
	private Log logger = LogFactory.getLog(this.getClass());
	
	public AbstractAction()
	{
		try {
	        freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
	    } catch (ClassNotFoundException e) {
	        logError("couldn't find freemaker classes",e);
	    }
	}
	
	//***************************//
	//***  action indicator  ******//
	//***************************//
	private String forward;
	
	//***************************//
	//***  paging setting  ******//
	//***************************//
    protected int start;
	
    protected int limit = 10;
	
    protected String sort;
    
    protected String dir;
    
    //***************************//
    //*Default JSON data setting*//
    //***************************//
    
	protected JSONObject jsonObject;
	
	protected JSONArray jsonArray;
	
	protected void setJSONResponse(boolean successFlg)
	{
		setJSONResponse(successFlg,null);
	}

	protected void setJSONResponse(boolean successFlg,String msg)
	{
		jsonObject = new JSONObject();
		
		jsonObject.put(JSON_RET_SUCCESS, successFlg);
		if(!StringUtils.isEmpty(msg))
		{
		   jsonObject.put(JSON_RET_MESSAGE, msg);
		}
	}
	
	//***************************//
	//***    log4j log     ******//
	//***************************//
	protected void logDebug(Object obj,Throwable arg1)
	{
		logger.debug(obj, arg1);
	}
	protected void logDebug(Object obj)
	{
		logger.debug(obj);
	}
	protected void logInfo(Object obj,Throwable arg1)
	{
		logger.info(obj, arg1);
	}
	protected void logInfo(Object obj)
	{
		logger.info(obj);
	}
	protected void logError(Object obj,Throwable arg1)
	{
		logger.error(obj, arg1);
	}
	protected void logError(Object obj)
	{
		logger.error(obj);
	}
	
	/**
	 * set the current login userId into session
	 */
	protected void setLoginUser(SysUser sysUser)
	{
		if(sysUser == null)
			return ;
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put(Constant.SESSION_LOGIN,sysUser);
	}
	
	
	/**
	 * get the current login userId from session
	 * @return userId
	 */
	protected String getLoginUserId()
	{
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		String _userId = (String)session.get(Constant.SESSION_LOGIN);
		if (_userId != null) {
			return _userId;
		}
		return null;
	}

	public void sendMsg(String content) throws IOException{       
	    HttpServletResponse response = ServletActionContext.getResponse();    
	    response.setContentType("text/html");
	    response.setCharacterEncoding("UTF-8");       
	    response.getWriter().write(content);   
	}

	public String getSort() {
			return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public int getPageNo()
	{
		return (start/limit)+1;
	}
	
    public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
		
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	} 
	
	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	
	
}
