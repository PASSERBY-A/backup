package com.hp.idc.itsm.inter;

import java.util.List;

public interface WorkgroupManagerInterface {
	/**
	 * ����֯ID���ҹ�����
	 * @param id ������ID
	 * @return �����ҵ��Ĺ��������,�Ҳ���ʱ����null
	 */
	public WorkgroupInfoInterface getWorkgroupById(String id);
	/**
	 * ��ȡ���й�����
	 * @return �������й�����List<OrganizationInfo>
	 */
	public List getAllWorkgroups();
	
	public List getWorkgroupsByPersonId(String userId);
	
	/**
	 * �ж���Ա�Ƿ���ָ���Ĺ�������
	 * @param workgroup ָ���Ĺ�����
	 * @param includeParent �Ƿ�ݹ����
	 * @return ������Ա�Ƿ���ָ���Ĺ�������
	 */
	public boolean personIsInWorkgroup(String userId,WorkgroupInfoInterface workgroup, boolean includeParent);
	
	/**
	 * ͨ����ID������֯
	 * @param pId
	 * @param includeAll�Ƿ������ɾ��
	 * @return
	 */
	public List<WorkgroupInfoInterface> getWorkgroupsByParentId(String pId,boolean includeAll);
}
