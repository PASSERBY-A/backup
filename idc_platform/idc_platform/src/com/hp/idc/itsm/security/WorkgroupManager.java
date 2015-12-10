package com.hp.idc.itsm.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupManagerInterface;

/**
 * 表示工作组的管理类
 * @author 梅园
 *
 */
public class WorkgroupManager{
	
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
	
	protected static WorkgroupManagerInterface wmi = null;
	
	private static WorkgroupManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		WorkgroupManagerInterface ret = (WorkgroupManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("找不到注册的类WorkgroupManagerInterface:"+origin);
		return ret;
	}

	/**
	 * 按组织ID查找工作组
	 * @param id 工作组ID
	 * @return 返回找到的工作组对象,找不到时返回null
	 */
	public static WorkgroupInfo getWorkgroupById(String id) {
		WorkgroupManagerInterface wm = (WorkgroupManagerInterface)classIns.get("ITSM");
		WorkgroupInfoInterface ret = wm.getWorkgroupById(id);
		if (ret!=null)
			return (WorkgroupInfo)ret;
		return null;
	}
	
	public static WorkgroupInfoInterface getWorkgroupById(String origin,String id) {
		wmi = getClassInstance(origin);
		return wmi.getWorkgroupById(id);
	}
	
	
	/**
	 * 获取所有工作组
	 * @return 返回所有工作组List<OrganizationInfo>
	 */
	public static List getAllWorkgroups() {
		wmi = getClassInstance("ITSM");
		return wmi.getAllWorkgroups();
	}
	public static List getAllWorkgroups(String origin) {
		wmi = getClassInstance(origin);
		return wmi.getAllWorkgroups();
	}
	
	/**
	 * 获取个人所在的工作组列表
	 * @return 返回个人所在的工作组列表List<WorkgroupInfo>
	 */
	public static List getWorkgroupsByPersonId(String origin,String peronId){
		wmi = getClassInstance(origin);
		return wmi.getWorkgroupsByPersonId(peronId);
	}
	
	public static boolean personIsInWorkgroup(String origin,String userId,WorkgroupInfoInterface workgroup, boolean includeParent){
		wmi = getClassInstance(origin);
		return wmi.personIsInWorkgroup(userId, workgroup, includeParent);
	}
	
	public static List getWorkgroupsByParentId(String origin,String pId,boolean includeAll){
		wmi = getClassInstance(origin);
		return wmi.getWorkgroupsByParentId(pId,includeAll);

	}

}
