package com.hp.idc.itsm.security;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;

/**
 * ��ʾ��������Ϣ
 * 
 * @author ÷԰
 * 
 */
public class WorkgroupInfo extends WorkgroupInfoInterface {


	/**
	 * ��ȡ�������µ�������Ա
	 * 
	 * @return ���ع������µ�������ԱList<PersonInfo>
	 */
	public List getPersons() {
		return PersonManager.getPersonsByWorkgoupId("ITSM",this.id, false);
		//return getRelationObjects(Consts.RT_WORKGROUP_PERSON);
	}

	/**
	 * ��ȡ�������µ�������Ա
	 * 
	 * @param includeChild
	 *            �����ӹ������µ���Ա
	 * @return ���ع������µ�������ԱList<PersonInfo>
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
