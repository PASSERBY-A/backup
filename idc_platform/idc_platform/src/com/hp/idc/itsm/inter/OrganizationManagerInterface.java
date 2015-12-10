package com.hp.idc.itsm.inter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrganizationManagerInterface {

	static Map<String,List<OrganizationInfoInterface>> orgaTree = new HashMap<String,List<OrganizationInfoInterface>>();
	/**
	 * ����֯ID������֯
	 * @param id ��֯ID
	 * @return �����ҵ�����֯����,�Ҳ���ʱ����null
	 */
	public OrganizationInfoInterface getOrganizationById(String id);
	
	/**
	 * ��ȡ������֯
	 * @return ����������֯List<OrganizationInfo>
	 */
	public List getAllOrganizations();
	
	/**
	 * ��ȡ�������ڵ���֯
	 * 
	 * @return ���ظ������ڵ���֯
	 */
	public OrganizationInfoInterface getOrganizationByPersonId(String userId);
	
	/**
	 * �ж���Ա�Ƿ���ָ������֯��
	 * @param organization ָ������֯
	 * @param includeParent �Ƿ�ݹ����
	 * @return ������Ա�Ƿ���ָ������֯��
	 */
	public boolean personIsInOrganization(String userId,OrganizationInfoInterface organization, boolean includeParent);

	
	/**
	 * ͨ����ID������֯
	 * @param pId
	 * @return
	 */
	public List<OrganizationInfoInterface> getOrganizationsByParentId(String pId,boolean includeAll);
}
