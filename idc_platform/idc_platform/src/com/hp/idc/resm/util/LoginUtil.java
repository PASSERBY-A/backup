package com.hp.idc.resm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * ��¼�û���صĴ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class LoginUtil {

	/**
	 * ��ȡ���ʷ������û���ip��ַ
	 * 
	 * @param request
	 *            http����
	 * @return ip��ַ
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * ��ȡ�û�id
	 * 
	 * @param session
	 *            http�Ự
	 * @return �û�id
	 * @throws Exception
	 *             �û�δ��¼ʱ����
	 */
	public static int getUserId(HttpSession session) throws Exception {
		Object obj = session
				.getAttribute("edu.yale.its.tp.cas.client.filter.user");
		if (obj == null)
			throw new Exception("���ȵ�¼");
		return StringUtil.parseInt(obj.toString(), 0);
	}

	/**
	 * ��ȡ�û�����
	 * 
	 * @param session
	 *            http�Ự
	 * @return �û�����
	 * @throws Exception
	 *             �û�δ��¼ʱ����
	 */
	public static String getUserName(HttpSession session) throws Exception {
		int id = getUserId(session);
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		String name = obj.getAttributeValue("name");
		return name;
	}

}
