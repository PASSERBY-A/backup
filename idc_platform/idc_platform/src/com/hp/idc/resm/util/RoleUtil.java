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
 * Ȩ����صĹ�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RoleUtil {

	/**
	 * ����û���Ȩ��
	 * 
	 * @param userId
	 *            �û�id
	 * @param permId
	 *            Ȩ��id
	 * @param param
	 *            Ȩ�޲���
	 * @param throwException
	 *            û��Ȩ��ʱ�Ƿ��׳��쳣
	 * @return true=��Ȩ��
	 * @throws Exception
	 *             û��Ȩ�ޣ�����throwException=trueʱ�׳�
	 */
	public static boolean checkUserPermission(int userId, String permId,
			Object param, boolean throwException) throws Exception {
		boolean r = ServiceManager.getRoleService().checkUserPermission(userId,
				permId, param);
		if (!r && throwException) {
			BasicPermission b = ServiceManager.getRoleService()
					.getPermissionById(permId);
			if (b == null)
				throw new Exception("ϵͳȨ�޶�����󣬷Ƿ���Ȩ��id: " + permId
						+ "������ϵϵͳά����Ա��");
			throw new Exception("�ܱ�Ǹ����û�С�" + b.getName()
					+ "����Ȩ�ޣ���������ϵϵͳ�����ˣ�Ϊ������Ȩ�޵ķ��䡣");
		}
		return r;
	}

	/**
	 * ���б���й��ˣ�����Ȩ�ޡ���Ȩ�޵Ķ�����б������
	 * @param <T>  ��������
	 * @param userId �û�id
	 * @param permIds Ȩ��id����
	 * @param list �����б��������������б���ֻ������ƥ��Ķ���
	 * @return ��Ȩ�޵Ķ���
	 * @throws Exception �������쳣ʱ����
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
	 * �ж��û��Ƿ���ϵͳ����Ա
	 * @param userId �û�id
	 * @return true=�ǣ�false=��
	 */
	public static boolean isSystemAdmin(int userId) {
		// TODO need to do
		//return (userId == 0 || userId == 1);
		return true;
	}
}
