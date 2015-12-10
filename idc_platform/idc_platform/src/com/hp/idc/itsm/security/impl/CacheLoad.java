package com.hp.idc.itsm.security.impl;



public class CacheLoad {

	public void loadAucCache(){
		System.out.println("load itsm AUC implements...");
//		Cache.Organizations = new HashMap();
//		Cache.Relations_W_P = new HashMap();
//		Cache.Relations_O_P = new HashMap();
//		Cache.Persons = new HashMap();
//		Cache.Workgroups = new HashMap();
		
		ITSMWorkgroupManagerImpl iwm = new ITSMWorkgroupManagerImpl();
		iwm.initCache();
		ITSMOrganizationManagerImpl iom = new ITSMOrganizationManagerImpl();
		iom.initCache();
		ITSMPersonManagerImpl ipm = new ITSMPersonManagerImpl();
		ipm.initCache();
		try {
			RoleManager.loadRoles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("load itsm AUC implements...end");
	}
	
	public void registerClass() {
		ITSMWorkgroupManagerImpl iwm = new ITSMWorkgroupManagerImpl();
		iwm.registerClass();
		ITSMOrganizationManagerImpl iom = new ITSMOrganizationManagerImpl();
		iom.registerClass();
		ITSMPersonManagerImpl ipm = new ITSMPersonManagerImpl();
		ipm.registerClass();
		
		//sync cache
		loadAucCache();
	}
}
