package com.hp.idc.portal.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.portal.bean.Menu;
import com.hp.idc.portal.mgr.MenuMgr;
import com.hp.idc.portal.mgr.TopData;
import com.hp.idc.portal.security.OrganizationInfo;
import com.hp.idc.portal.security.OrganizationManager;
import com.hp.idc.portal.security.PersonInfo;
import com.hp.idc.portal.security.PersonManager;
import com.hp.idc.portal.security.RoleInfo;
import com.hp.idc.portal.security.RoleManager;
import com.hp.idc.portal.security.WorkgroupInfo;
import com.hp.idc.portal.security.WorkgroupManager;

public class Cache {
	public static Map<String,Menu> MenuMap;
	
	public static Map<String, PersonInfo> PersonMap;
	
	public static Map<Integer, PersonInfo> PersonMap1;

	public static Map<String, WorkgroupInfo> WorkgroupMap;

	public static Map<String, OrganizationInfo> OrganizationMap;

	// 角色数组，
	// key=WorkgroupInfo.getId()_|_RoleInfo.getId()或者key=OrganizationInfo.getId()_|_RoleInfo.getId()
	public static Map<String, RoleInfo> RoleMap;

	// 工作组-人员 key=WorkgroupInfo.getId(),value=List<PersonInfo.getId()>
	public static Map<String, List<String>> Relation_W_P;
	// 人员-工作组 key=PersonInfo.getId(),value=List<WorkgroupInfo.getId()>
	public static Map<String, List<String>> Relation_P_W;
	// 组织-人员 key=OrganizationInfo.getId(),value=List<PersonInfo.getId()>
	public static Map<String, List<String>> Relation_O_P;
	// 人员-组织 key=PersonInfo.getId(),value=OrganizationInfo.getId()
	public static Map<String, String> Relation_P_O;
	// 人员-角色key=WorkgroupInfo.getId()+Common.Split_Str+PersonInfo.getId()或者key=OrganizationInfo.getId()+Common.Split_Str+PersonInfo.getId()
	// value = RoleInfo.getId();
	public static Map<String, String> Relation_M_R;
	// 角色-人员key=WorkgroupInfo.getId()+Common.Split_Str+RoleInfo.getId()或者key=OrganizationInfo.getId()+Common.Split_Str+RoleInfo.getId()
	
	// value = List<PersonInfo.getId()>;
	public static Map<String, List<String>> Relation_M_P;
	
	public static int todayLoginNum;

	public static void reloadAuc() {
		todayLoginNum = 0;
		MenuMap = new HashMap<String,Menu>();
		PersonMap = new HashMap<String, PersonInfo>();
		PersonMap1 = new HashMap<Integer, PersonInfo>();
		WorkgroupMap = new HashMap<String, WorkgroupInfo>();
		OrganizationMap = new HashMap<String, OrganizationInfo>();
		RoleMap = new HashMap<String, RoleInfo>();
		Relation_W_P = new HashMap<String, List<String>>();
		Relation_P_W = new HashMap<String, List<String>>();
		Relation_O_P = new HashMap<String, List<String>>();
		Relation_P_O = new HashMap<String, String>();
		Relation_M_R = new HashMap<String, String>();
		Relation_M_P = new HashMap<String, List<String>>();
		

		System.out.println("load portal AUC and menu.....");
		try {
			MenuMgr.loadAllMenus();
			System.out.println("load menus OK.");
			PersonManager.loadAllPersons();
			System.out.println("load Person OK.");
			WorkgroupManager.loadAllWorkgroups();
			System.out.println("load Workgroup OK.");
			OrganizationManager.loadAllOrganization();
			System.out.println("load Organization OK.");
			RoleManager.loadAllRoles();
			System.out.println("load Role OK.");
			WorkgroupManager.loadRelations();
			System.out.println("load WorkgroupRelations OK.");
			OrganizationManager.loadRelations();
			System.out.println("load OrganizationRelations OK.");
			TopData.getTodayLoginNum();
			System.out.println("load todayLoginNum OK.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("load AUC error.");
		}
		System.out.println("load portal AUC and menu....OK.");
	}
	
	/**
	 * GBK对应UNICODE表
	 */
	public static int CodeMapping[] = new int[65536];
	
	/**
	 * 初始化代码表
	 * 
	 * @throws IOException
	 */
	public static void initCodePage() throws IOException {
		System.out.println("loading codepage 936 ...");
		for (int i = 0; i < CodeMapping.length; i++)
			CodeMapping[i] = 0;
		InputStream inp = Cache.class
				.getResourceAsStream("/META-INF/CP936.TXT");
		BufferedReader bufferedreader = new BufferedReader(
				new InputStreamReader(inp));
		String s;
		while ((s = bufferedreader.readLine()) != null) {
			String[] ss = s.split("\t");
			if (ss.length >= 2 && ss[0].startsWith("0x")
					&& ss[1].startsWith("0x")) {
				int n1 = Integer.parseInt(ss[0].substring(2), 16);
				int n2 = Integer.parseInt(ss[1].substring(2), 16);
				CodeMapping[n2] = n1;
			}
		}
		System.out.println("loading codepage 936 OK");
	}
}
