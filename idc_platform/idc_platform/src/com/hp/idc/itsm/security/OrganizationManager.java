package com.hp.idc.itsm.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.OrganizationManagerInterface;

/**
 * 表示组织的管理类
 * @author 梅园
 *
 */
public class OrganizationManager{
	
	/**
	 * 继承类的类名["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * 所有实现TaskManagerInterface接口的类注册一下，就可以自动实现方法的执行
	 */
	public static Map classInsab = new HashMap();
	
	/**
	 * 继承类的map数组["ITSM"=TaskManagerInterface..]
	 * 所有实现TaskManagerInterface接口的类注册一下，就可以自动实现方法的执行
	 */
	public static Map classIns = new HashMap();
	
	protected static OrganizationManagerInterface omi = null;

	private static OrganizationManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		OrganizationManagerInterface ret = (OrganizationManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("找不到注册的类OrganizationManagerInterface:"+origin);
		return ret;
	}
	/**
	 * 按组织ID查找组织
	 * @param id 组织ID
	 * @return 返回找到的组织对象,找不到时返回null
	 */
	public static OrganizationInfo getOrganizationById(String id) {
		OrganizationManagerInterface om = (OrganizationManagerInterface)classIns.get("ITSM");
		OrganizationInfoInterface ret = om.getOrganizationById(id);
		if (ret!=null)
			return (OrganizationInfo)ret;
		return null;
	}
	
	public static OrganizationInfoInterface getOrganizationById(String origin,String id) {
		omi = getClassInstance(origin);
		return omi.getOrganizationById(id);
	}
	
	/**
	 * 获取所有组织
	 * @return 返回所有组织List<OrganizationInfo>
	 */
	public static List getAllOrganizations() {
		omi = getClassInstance("ITSM");
		return omi.getAllOrganizations();
	}
	
	public static List getAllOrganizations(String origin) {
		omi = getClassInstance(origin);
		return omi.getAllOrganizations();
	}
	
	public static OrganizationInfoInterface getOrganizationByPersonId(String origin,String userId){
		omi = getClassInstance(origin);
		return omi.getOrganizationByPersonId(userId);
	}

	
	public static boolean personIsInOrganization(String origin,String userId,OrganizationInfoInterface organization, boolean includeParent){
		omi = getClassInstance(origin);
		return omi.personIsInOrganization(userId, organization, includeParent);
	}
	
	public static List<OrganizationInfoInterface> getOrganizationsByParentId(String id,boolean includeAll){
		omi = getClassInstance("ITSM");
		return omi.getOrganizationsByParentId(id,includeAll);
	}
	
	public static List<OrganizationInfoInterface> getOrganizationsByParentId(String origin, String id,boolean includeAll){
		omi = getClassInstance(origin);
		return omi.getOrganizationsByParentId(id,includeAll);
	}
}
