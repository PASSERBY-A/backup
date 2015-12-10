package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;

public abstract class OrganizationInfoInterface extends CIInfo  {

	
	/**
	 * 获取组织下的所有人员
	 * @return 返回组织下的所有人员List<PersonInfo>
	 */
	public abstract List getPersons();
	
	/**
	 * 获取组织下的所有人员
	 * 
	 * @param includeChild
	 *            包含子组织下的人员
	 * @return 返回组织下的所有人员List<PersonInfo>
	 */
	public abstract List getPersons(boolean includeChild);
	
	public abstract OrganizationInfoInterface getParentInfo();
}
