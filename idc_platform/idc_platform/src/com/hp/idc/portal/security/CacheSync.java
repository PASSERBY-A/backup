package com.hp.idc.portal.security;

import com.hp.idc.portal.mgr.TopData;


/**
 * ª∫¥ÊÕ¨≤Ω¿‡
 * 
 * @author silence
 * 
 */

public class CacheSync {

	public void syncCache() {
		System.out.println("load portal CAS's AUC implements...");
		// Cache.Organizations = new HashMap();
		// Cache.Relations_W_P = new HashMap();
		// Cache.Relations_O_P = new HashMap();
		// Cache.Persons = new HashMap();
		// Cache.Workgroups = new HashMap();
		try {
			WorkgroupManager wm = new WorkgroupManager();
			wm.syncCache();
			OrganizationManager om = new OrganizationManager();
			om.syncCache();
			PersonManager pm = new PersonManager();
			pm.syncCache();
			RoleManager.syncRoles();
			TopData.getTodayLoginNum();
//			MenuMgr.syncMenus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("load portal CAS's AUC implements...end");
	}
}
