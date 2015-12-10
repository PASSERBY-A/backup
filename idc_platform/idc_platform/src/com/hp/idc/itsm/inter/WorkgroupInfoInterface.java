package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;

public abstract class WorkgroupInfoInterface extends CIInfo  {

	
	/**
	 * 获取工作组下的所有人员
	 * 
	 * @return 返回工作组下的所有人员List<PersonInfo>
	 */
	public abstract List getPersons();
	/**
	 * 获取工作组下的所有人员
	 * 
	 * @param includeChild
	 *            包含子工作组下的人员
	 * @return 返回工作组下的所有人员List<PersonInfo>
	 */
	public abstract List getPersons(boolean includeChild);
	
	public abstract WorkgroupInfoInterface getParentInfo();
}
