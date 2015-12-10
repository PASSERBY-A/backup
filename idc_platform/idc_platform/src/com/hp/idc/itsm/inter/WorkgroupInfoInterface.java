package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;

public abstract class WorkgroupInfoInterface extends CIInfo  {

	
	/**
	 * ��ȡ�������µ�������Ա
	 * 
	 * @return ���ع������µ�������ԱList<PersonInfo>
	 */
	public abstract List getPersons();
	/**
	 * ��ȡ�������µ�������Ա
	 * 
	 * @param includeChild
	 *            �����ӹ������µ���Ա
	 * @return ���ع������µ�������ԱList<PersonInfo>
	 */
	public abstract List getPersons(boolean includeChild);
	
	public abstract WorkgroupInfoInterface getParentInfo();
}
