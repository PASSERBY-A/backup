package com.hp.idc.itsm.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.OrganizationManagerInterface;

/**
 * ��ʾ��֯�Ĺ�����
 * @author ÷԰
 *
 */
public class OrganizationManager{
	
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
	
	protected static OrganizationManagerInterface omi = null;

	private static OrganizationManagerInterface getClassInstance(String origin){
		if (origin == null || origin.equals(""))
			origin = "ITSM";
		OrganizationManagerInterface ret = (OrganizationManagerInterface)classIns.get(origin);
		if (ret==null)
			throw new NullPointerException("�Ҳ���ע�����OrganizationManagerInterface:"+origin);
		return ret;
	}
	/**
	 * ����֯ID������֯
	 * @param id ��֯ID
	 * @return �����ҵ�����֯����,�Ҳ���ʱ����null
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
	 * ��ȡ������֯
	 * @return ����������֯List<OrganizationInfo>
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
