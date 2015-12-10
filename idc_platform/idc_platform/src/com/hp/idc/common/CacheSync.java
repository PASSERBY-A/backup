/*
 * @(#)CacheSync.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hp.idc.itsm.security.impl.ITSMOrganizationManagerImpl;
import com.hp.idc.itsm.security.impl.ITSMPersonManagerImpl;
import com.hp.idc.itsm.security.impl.ITSMWorkgroupManagerImpl;
import com.hp.idc.portal.security.OrganizationManager;
import com.hp.idc.portal.security.WorkgroupManager;

/**
 * 通过spring,quartuz来同步加载内存中的对象, 例如portal和itsm中的cas对象等
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 8:58:23 AM Jul 12, 2011
 * 
 */

public class CacheSync {
	
	/**
	 * 定义日志变量
	 */
	private Logger logger = Logger.getLogger(CacheSync.class);

	public void reLoadCache(){
		
		logger.info("load Cas Auc.....");
		
		new com.hp.idc.itsm.security.impl.CacheLoad().loadAucCache();
		
		new com.hp.idc.portal.security.CacheSync().syncCache();
		
		logger.info("load Cas Auc.....End");
	}
	
	public static void loadPersonCache(){
		//sync portal person
		try {
			com.hp.idc.portal.security.PersonManager.loadAllPersons();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//sync itsm person
		new ITSMPersonManagerImpl().loadPersons();
	}
	
	public static void loadGroupCache(){
		try {
			WorkgroupManager.loadAllWorkgroups();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ITSMWorkgroupManagerImpl().loadWorkgroups();
	}
	
	public static void loadOrgCache(){
		try {
			OrganizationManager.loadAllOrganization();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new ITSMOrganizationManagerImpl().loadOrganizations();
		
	}
	
	public static void loadOrgRelationCache(){
		try {
			OrganizationManager.loadRelations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new ITSMPersonManagerImpl().loadOP();
	}
	
	public static void loadGroupRelationCache(){
		try {
			WorkgroupManager.loadRelations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new ITSMPersonManagerImpl().loadWP();
	}
}
