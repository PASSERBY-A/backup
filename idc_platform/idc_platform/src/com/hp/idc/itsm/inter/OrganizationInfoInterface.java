package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;

public abstract class OrganizationInfoInterface extends CIInfo  {

	
	/**
	 * ��ȡ��֯�µ�������Ա
	 * @return ������֯�µ�������ԱList<PersonInfo>
	 */
	public abstract List getPersons();
	
	/**
	 * ��ȡ��֯�µ�������Ա
	 * 
	 * @param includeChild
	 *            ��������֯�µ���Ա
	 * @return ������֯�µ�������ԱList<PersonInfo>
	 */
	public abstract List getPersons(boolean includeChild);
	
	public abstract OrganizationInfoInterface getParentInfo();
}
