package com.hp.idc.itsm.inter;

import java.util.List;

public interface WorkgroupManagerInterface {
	/**
	 * 按组织ID查找工作组
	 * @param id 工作组ID
	 * @return 返回找到的工作组对象,找不到时返回null
	 */
	public WorkgroupInfoInterface getWorkgroupById(String id);
	/**
	 * 获取所有工作组
	 * @return 返回所有工作组List<OrganizationInfo>
	 */
	public List getAllWorkgroups();
	
	public List getWorkgroupsByPersonId(String userId);
	
	/**
	 * 判断人员是否在指定的工作组中
	 * @param workgroup 指定的工作组
	 * @param includeParent 是否递归查找
	 * @return 返回人员是否在指定的工作组中
	 */
	public boolean personIsInWorkgroup(String userId,WorkgroupInfoInterface workgroup, boolean includeParent);
	
	/**
	 * 通过父ID查找组织
	 * @param pId
	 * @param includeAll是否包含已删除
	 * @return
	 */
	public List<WorkgroupInfoInterface> getWorkgroupsByParentId(String pId,boolean includeAll);
}
