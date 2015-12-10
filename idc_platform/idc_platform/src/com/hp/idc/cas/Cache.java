package com.hp.idc.cas;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.cas.auc.OrganizationInfo;
import com.hp.idc.cas.auc.OrganizationManager;
import com.hp.idc.cas.auc.PersonInfo;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.cas.auc.RoleInfo;
import com.hp.idc.cas.auc.RoleManager;
import com.hp.idc.cas.auc.WorkgroupInfo;
import com.hp.idc.cas.auc.WorkgroupManager;

public class Cache {

	public static Map<String, PersonInfo> PersonMap;

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

	public static void reloadAuc() {
		PersonMap = new HashMap<String, PersonInfo>();
		WorkgroupMap = new HashMap<String, WorkgroupInfo>();
		OrganizationMap = new HashMap<String, OrganizationInfo>();
		RoleMap = new HashMap<String, RoleInfo>();
		Relation_W_P = new HashMap<String, List<String>>();
		Relation_P_W = new HashMap<String, List<String>>();
		Relation_O_P = new HashMap<String, List<String>>();
		Relation_P_O = new HashMap<String, String>();
		Relation_M_R = new HashMap<String, String>();
		Relation_M_P = new HashMap<String, List<String>>();
		

		System.out.println("load CAS AUC.....");
		try {
			PersonManager.loadAllPersons();
			WorkgroupManager.loadAllWorkgroups();
			OrganizationManager.loadAllOrganization();
			RoleManager.loadAllRoles();
			WorkgroupManager.loadRelations();
			OrganizationManager.loadRelations();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("load CAS AUC.....end");
	}
}
