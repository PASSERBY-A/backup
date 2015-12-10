package com.hp.idc.itsm.security;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;

/**
 * 表示组织信息
 * @author 梅园
 *
 */
public class OrganizationInfo extends OrganizationInfoInterface {
	


	/**
	 * 获取组织下的所有人员
	 * @return 返回组织下的所有人员List<PersonInfo>
	 */
	public List getPersons() {
		return PersonManager.getPersonsByOrganizationId("ITSM",this.id, false);
		//return getRelationObjects(Consts.RT_ORGANIZATION_PERSON);
	}
	
	/**
	 * 获取组织下的所有人员
	 * 
	 * @param includeChild
	 *            包含子组织下的人员
	 * @return 返回组织下的所有人员List<PersonInfo>
	 */
	public List getPersons(boolean includeChild) {
		List l = PersonManager.getPersonsByOrganizationId("ITSM",this.id, includeChild);
		return l;
	}

	@Override
	public OrganizationInfoInterface getParentInfo() {
		Set s = Cache.Organizations.keySet();
		for (Iterator ite = s.iterator(); ite.hasNext();){
			String key = (String)ite.next();
			OrganizationInfo oii = (OrganizationInfo)Cache.Organizations.get(key);
			if (oii.getId().equals(this.getParentId())){
				return oii;
			}
		}
		return null;
	}
}
