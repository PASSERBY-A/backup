package com.hp.idc.itsm.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupManagerInterface;

/**
 * ��ʾ������Ĺ�����
 * @author ÷԰
 *
 */
public class WorkgroupManager{
	
	/**
	 * �̳��������["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map classInsab = new HashMap();
	
	/**
	 * �̳����map����["ITSM"=TaskManagerInterface..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map classIns = new HashMap();
	
	protected static WorkgroupManagerInterface wmi = null;
	
	private static WorkgroupManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		WorkgroupManagerInterface ret = (WorkgroupManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("�Ҳ���ע�����WorkgroupManagerInterface:"+origin);
		return ret;
	}

	/**
	 * ����֯ID���ҹ�����
	 * @param id ������ID
	 * @return �����ҵ��Ĺ��������,�Ҳ���ʱ����null
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
	 * ��ȡ���й�����
	 * @return �������й�����List<OrganizationInfo>
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
	 * ��ȡ�������ڵĹ������б�
	 * @return ���ظ������ڵĹ������б�List<WorkgroupInfo>
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
