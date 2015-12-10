/**
 * 
 */
package com.hp.idc.resm.util;

import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 一些资源管理的工具类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResmUtil {

	/**
	 * 获取资源名称
	 * 
	 * @param id
	 *            资源id
	 * @return 资源名称
	 * @throws Exception
	 *             找不到资源时发生
	 */
	public static String getResourceName(int id) throws Exception {
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			throw new Exception("找不到资源" + id);
		return obj.getAttributeValue("name");
	}

	/**
	 * 获取带id资源名称
	 * 
	 * @param id
	 *            资源id
	 * @return 资源名称(id)
	 * @throws Exception
	 *             找不到资源时发生
	 */
	public static String getResourceNameWithId(int id) throws Exception {
		return getResourceName(id) + "(" + id + ")";
	}

	/**
	 * 获取用户名
	 * 
	 * @param id
	 *            用户id
	 * @return 用户名,找不到时直接返回id
	 */
	public static String getUserName(int id) {
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			return "" + id;
		return obj.getAttributeValue("name");
	}

	/**
	 * 获取带id的用户名
	 * 
	 * @param id
	 *            用户id
	 * @return 用户名(id)
	 */
	public static String getUserNameWithId(int id) {
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			return "" + id;
		return obj.getAttributeValue("name") + "(" + id + ")";
	}
}
