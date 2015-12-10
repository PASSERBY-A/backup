package com.hp.idc.itsm.inter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrganizationManagerInterface {

	static Map<String,List<OrganizationInfoInterface>> orgaTree = new HashMap<String,List<OrganizationInfoInterface>>();
	/**
	 * 按组织ID查找组织
	 * @param id 组织ID
	 * @return 返回找到的组织对象,找不到时返回null
	 */
	public OrganizationInfoInterface getOrganizationById(String id);
	
	/**
	 * 获取所有组织
	 * @return 返回所有组织List<OrganizationInfo>
	 */
	public List getAllOrganizations();
	
	/**
	 * 获取个人所在的组织
	 * 
	 * @return 返回个人所在的组织
	 */
	public OrganizationInfoInterface getOrganizationByPersonId(String userId);
	
	/**
	 * 判断人员是否在指定的组织中
	 * @param organization 指定的组织
	 * @param includeParent 是否递归查找
	 * @return 返回人员是否在指定的组织中
	 */
	public boolean personIsInOrganization(String userId,OrganizationInfoInterface organization, boolean includeParent);

	
	/**
	 * 通过父ID查找组织
	 * @param pId
	 * @return
	 */
	public List<OrganizationInfoInterface> getOrganizationsByParentId(String pId,boolean includeAll);
}
