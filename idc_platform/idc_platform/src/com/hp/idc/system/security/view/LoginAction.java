/**   
 * @Title: LoginAction.java 
 * @Description: TODO
 * @date 2011-7-5 下午12:28:40   
 * @version 1.0  
 */
package com.hp.idc.system.security.view;     

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.cas.log.LoginLogManager;
import com.hp.idc.common.Constant;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.common.core.view.NoLogin;
import com.hp.idc.system.security.service.LoginManager;
import com.opensymphony.xwork2.ActionContext;
 
/**
 * 登录管理action
 * @ClassName: LoginAction
 * @Descprition: TODO
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public class LoginAction extends AbstractAction implements NoLogin{

	private static final long serialVersionUID = 1L;
	protected static final Log log = LogFactory.getLog(LoginAction.class);
	private String userId;
	private String password;
	
	/**
	 * 用户登录方法
	 * @Title:login
	 * @Desciption:TODO
	 * @return
	 * @throws IOException 
	 */
	public String login() throws IOException{
		
		HttpServletResponse res = ServletActionContext.getResponse();
		
		res.setContentType("text/plain");
		res.setCharacterEncoding("GBK");
		int ret = new LoginManager().validate(userId, password);
		Map<String, Object> session = ActionContext.getContext().getSession();

		if (ret == 0) {

			session.put(Constant.SESSION_LOGIN, userId);		
			if (session.get(Constant.REIDRECT_URL) != null) {
				setJSONResponse(true , session.get(Constant.REIDRECT_URL).toString());
			} 
			else {
				setJSONResponse(true ,"/");
			}
			
			try {
				
				LoginLogManager.addLoginLog(userId, getRequest().getRemoteAddr(), getRequest().getRemoteHost());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			String message="";
			switch(ret){
				case 1: message="该用户不存在";break;
				case 2: message="用户密码错误";
					if(CommonInfo.SYS_CONFIG.get("login_repeat") != null && PersonManager.isSystemManager(userId) == false)
						message = message+"，总共"+CommonInfo.SYS_CONFIG.get("login_repeat")+"次机会";
				break;
				case 3: message="该用户已禁用";break;
				case 4:message="您的账户密码已过期，请联系管理员重置密码";break;
				case 5: 
					String login_locktime = CommonInfo.SYS_CONFIG.get("login_locktime");
					message="您的账户尝试登录次数过多已被锁定，请在"+login_locktime+"小时之后再试";
					break;
				case 6:
					message="6";
					break;
			}
			setJSONResponse(false ,message);
		}
		
		return SUCCESS;
	}

	/**
	 * 用户退出方法
	 * @Title:logout
	 * @Desciption:TODO
	 * @return
	 */
	public String logout() {
		try {
			ActionContext.getContext().getSession().clear();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * 用户重登录方法
	 * @Title:logout
	 * @Desciption:TODO
	 * @return
	 */
	public String preLogin(){
		return SUCCESS;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
 