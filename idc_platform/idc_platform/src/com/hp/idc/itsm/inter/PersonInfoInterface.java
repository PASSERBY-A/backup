package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.itsm.ci.CIInfo;

public abstract class PersonInfoInterface  extends CIInfo {

	public abstract void setPassword(String password);
	public abstract void setEmail(String email);
	public abstract void setMobile(String mobile);
	public abstract void setPersonStatus(String personStatus);

	
	public abstract String getPassword();
	public abstract String getEmail();
	public abstract String getMobile();
	public abstract String getPersonStatus();
	
	public abstract OrganizationInfoInterface getOrganization();
	public abstract List getWorkgroups();
	
	public abstract boolean isInWorkgroup(String groupId,boolean includeParent);
	public abstract boolean isInOrganization(String orgaId,boolean includeParent);

}
