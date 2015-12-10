package com.hp.idc.itsm.security;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;

/**
 * 表示工作组信息
 * 
 * @author 梅园
 * 
 */
public class WorkgroupInfo extends WorkgroupInfoInterface {


	/**
	 * 获取工作组下的所有人员
	 * 
	 * @return 返回工作组下的所有人员List<PersonInfo>
	 */
	public List getPersons() {
		return PersonManager.getPersonsByWorkgoupId("ITSM",this.id, false);
		//return getRelationObjects(Consts.RT_WORKGROUP_PERSON);
	}

	/**
	 * 获取工作组下的所有人员
	 * 
	 * @param includeChild
	 *            包含子工作组下的人员
	 * @return 返回工作组下的所有人员List<PersonInfo>
	 */
	public List getPersons(boolean includeChild) {
		return PersonManager.getPersonsByWorkgoupId("ITSM",this.id, includeChild);
		
	}

	@Override
	public WorkgroupInfoInterface getParentInfo() {
		Set s = Cache.Workgroups.keySet();
		for (Iterator ite = s.iterator(); ite.hasNext();){
			String key = (String)ite.next();
			WorkgroupInfo oii = (WorkgroupInfo)Cache.Workgroups.get(key);
			if (oii.getId().equals(this.getParentId())){
				return oii;
			}
		}
		return null;
	}

}
