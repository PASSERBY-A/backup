package com.hp.idc.resm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 登录用户相关的处理
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class LoginUtil {

	/**
	 * 获取访问服务器用户的ip地址
	 * 
	 * @param request
	 *            http请求
	 * @return ip地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取用户id
	 * 
	 * @param session
	 *            http会话
	 * @return 用户id
	 * @throws Exception
	 *             用户未登录时发生
	 */
	public static int getUserId(HttpSession session) throws Exception {
		Object obj = session
				.getAttribute("edu.yale.its.tp.cas.client.filter.user");
		if (obj == null)
			throw new Exception("请先登录");
		return StringUtil.parseInt(obj.toString(), 0);
	}

	/**
	 * 获取用户名称
	 * 
	 * @param session
	 *            http会话
	 * @return 用户名称
	 * @throws Exception
	 *             用户未登录时发生
	 */
	public static String getUserName(HttpSession session) throws Exception {
		int id = getUserId(session);
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		String name = obj.getAttributeValue("name");
		return name;
	}

}
