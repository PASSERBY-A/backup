/**
 * 
 */
package com.hp.idc.resm.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.security.BasicPermission;
import com.hp.idc.resm.security.RolePermission;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 权限相关的工具类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RoleUtil {

	/**
	 * 检查用户的权限
	 * 
	 * @param userId
	 *            用户id
	 * @param permId
	 *            权限id
	 * @param param
	 *            权限参数
	 * @param throwException
	 *            没有权限时是否抛出异常
	 * @return true=有权限
	 * @throws Exception
	 *             没有权限，而且throwException=true时抛出
	 */
	public static boolean checkUserPermission(int userId, String permId,
			Object param, boolean throwException) throws Exception {
		boolean r = ServiceManager.getRoleService().checkUserPermission(userId,
				permId, param);
		if (!r && throwException) {
			BasicPermission b = ServiceManager.getRoleService()
					.getPermissionById(permId);
			if (b == null)
				throw new Exception("系统权限定义错误，非法的权限id: " + permId
						+ "，请联系系统维护人员。");
			throw new Exception("很抱歉，您没有“" + b.getName()
					+ "”的权限，您可以联系系统负责人，为您进行权限的分配。");
		}
		return r;
	}

	/**
	 * 对列表进行过滤，将无权限、有权限的对象从列表中清除
	 * @param <T>  对象类型
	 * @param userId 用户id
	 * @param permIds 权限id集合
	 * @param list 对象列表，本函数操作后，列表中只保留不匹配的对象
	 * @return 有权限的对象
	 * @throws Exception 较验有异常时发生
	 */
	public static <T> List<T> filterByUserPermission(int userId, String[] permIds,
			List<T> list) throws Exception {
		List<T> ret = new ArrayList<T>();
		if (isSystemAdmin(userId)) {
			ret.addAll(list);
			list.clear();
			return ret;
		}
		for (int i = 0; i < permIds.length; i++) {
			if (list.size() > 0) {
				List<RolePermission> pl = ServiceManager.getRoleService()
						.getUserPermissions(userId, permIds[i]);
				for (RolePermission rp : pl) {
					if (list.size() > 0)
						ret.addAll(rp.getPermisson().filter(userId, list));
				}
			}
		}
		return list;
	}
	
	/**
	 * 判断用户是否是系统管理员
	 * @param userId 用户id
	 * @return true=是，false=否
	 */
	public static boolean isSystemAdmin(int userId) {
		// TODO need to do
		//return (userId == 0 || userId == 1);
		return true;
	}
}
