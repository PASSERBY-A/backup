package com.hp.idc.itsm.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.CodePage;
import com.hp.idc.itsm.authorization.MenuManager;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.configure.FormInfo;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.configure.ViewInfo;
import com.hp.idc.itsm.configure.ViewManager;
import com.hp.idc.itsm.impl.ITSMFieldManagerImpl;
import com.hp.idc.itsm.impl.ITSMTaskManagerImpl;
import com.hp.idc.itsm.security.FactorInfo;
import com.hp.idc.itsm.security.LocalgroupInfo;
import com.hp.idc.itsm.security.LocalgroupManager;
import com.hp.idc.itsm.security.OrganizationInfo;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.security.RoleInfo;
import com.hp.idc.itsm.security.WorkgroupInfo;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * 服务管理的缓存管理类，
 * 
 * @author 梅园
 * 
 */
public class Cache {
	/**
	 * 初始化状态：未初始化
	 */
	static public int INIT_NONE = 0;

	/**
	 * 初始化状态：进行中
	 */
	static public int INIT_INPROGRESS = 1;

	/**
	 * 初始化状态：成功
	 */
	static public int INIT_OK = 2;

	/**
	 * 初始化状态：失败
	 */
	static public int INIT_FAIL = 3;

	/**
	 * 表示缓存是否已被初始化
	 */
	static public int initStatus = INIT_NONE;

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
	}

	/**
	 * 初始化服务管理缓存 此函数在服务器加载时自动调用
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		if (initStatus > 0)
			return;
		initStatus = INIT_INPROGRESS;
		Consts.init();
		System.out.println("itsm init ...");
		//for idc use the common/codepage method
		//initCodePage();
		CodeMapping = CodePage.codeMapping;
		
		try {
			reloadCache();
		} catch (Exception e) {
			initStatus = INIT_FAIL;
			e.printStackTrace();
			throw e;
		}
		System.out.println("itsm init ok.");
		initStatus = INIT_OK;
	}

	/**
	 * 重新初始化缓存
	 * @throws Exception 
	 */
	public void reloadCache() throws Exception {		
		Factors = new HashMap<String,FactorInfo>();
		CICategoryTree = new ArrayList();
		CategoryToCI = new HashMap();
		Roles = new HashMap<String,RoleInfo>();
		Relations_M_R = new HashMap<String,String>();
		Relations_M_P = new HashMap<String,List<String>>();
		
		MenuManager.loadMenus();
		ViewManager.initCache();
		ITSMFieldManagerImpl ifm = new ITSMFieldManagerImpl();
		ifm.initCache();
		FormManager.initCache();
		CIManager ciM = new CIManager();
		ciM.initCache();
		
		//加载代理人列表
		PersonManager.loadFactors();
		LocalgroupManager.initLocalgroup();
		
		WorkflowManager.initCache();
		ITSMTaskManagerImpl itm = new ITSMTaskManagerImpl();
		itm.initCache();
		
	}

	/**
	 * 流程缓存 key = new Integer(WorkflowInfo.getOid()) value = WorkflowInfo
	 */
	public static Map<Integer,WorkflowInfo> Workflows;

	/**
	 * 关联关系类别缓存 key = new Integer(RelationTypeInfo.getOid()) value =
	 * RelationTypeInfo
	 */
	public static Map RelationTypes;

	/**
	 * 关联关系缓存 key = RelationInfo.getId() value = RelationInfo
	 */
	public static Map Relations;

	/**
	 * 代码类型缓存 key = new Integer(CodeTypeInfo.getOid()) value = CodeTypeInfo
	 */
	public static Map CodeTypes;

	/**
	 * 代码缓存，按代码类型OID查找代码 key = new Integer(CodeTypeInfo.getOid()) value = List<CodeInfo>,包含此类型下的所有CodeInfo
	 */
	public static Map Codes;
	
	/**
	 * 代码缓存，按代码类型OID查找代码 key = new Integer(CodeTypeInfo.getOid()) value = List<CodeInfo>，只包含第一级的代码，子级代码通过CodeInfo对象查找
	 */
	public static Map CodesTree;

	/**
	 * 代码缓存 key = new Integer(CodeInfo.getOid()) value = CodeInfo
	 */
	public static Map AllCodes;
	
	/**
	 * 代码缓存 key =CodeInfo.getCodeId() value = CodeInfo
	 */
	public static Map AllCodes2;
	
	/**
	 * 配置项类别 key = new Integer(CodeInfo.getOid()) value = CodeInfo
	 */
	public static Map CICategory;
	
	/**
	 * 格式化成树的配置项类别 List[CodeInfo]
	 */
	public static List CICategoryTree;
	
	public static Map CategoryToCI;

	/**
	 * 配置项缓存 key = CIInfo.getId() value = CIInfo
	 */
	public static Map CIs;

	/**
	 * 配置项缓存 key = new Integer(CIInfo.getOid()) value = CIInfo
	 */
	public static Map CIs2;

	/**
	 * 字段缓存 key = FieldInfo.getId() value = FieldInfo
	 */
	public static Map Fields;

	/**
	 * 字段缓存 key = new Integer(FieldInfo.getOid()) value = FieldInfo
	 */
	public static Map Fields2;

	/**
	 * 字段类型缓存 key = 类名，如“com.hp.idc.itsm.configure.fields.FileFieldInfo”
	 * value = 说明，如“附件”
	 */
	public static Map FieldTypes;

	/**
	 * 表单缓存 key = new Integer(FormInfo.getOid()) value = FormInfo
	 */
	public static Map<Integer,FormInfo> Forms;
	
	/**
	 * 打开的任务缓存 
	 * key = TaskInfo.getOrigin()+"_"+TaskInfo.getWfOid()+"_"+TaskInfo.getOid() value=TaskInfo
	 */
	public static Map<String,TaskInfo> Tasks;
	
	/**
	 * 历史工单缓存(如果流程配置，则开机加载历史数据)
	 * key = TaskInfo.getOrigin()+"_"+TaskInfo.getWfOid()+"_"+TaskInfo.getOid() value=TaskInfo
	 */
	public static Map<String,TaskInfo> TasksHis;
	
	/**
	 * 流程下打开的工单列表缓存
	 * key= WorkflowInfo.getOid(); value=Map(key=工单id,value=TaskInfo)
	 */
	public static Map<String,Map<String,TaskInfo>> Workflow_Tasks;
	/**
	 * 流程下关闭的工单列表缓存
	 */
	public static Map<String,Map<String,TaskInfo>> Workflow_TasksHis;

	/**
	 * wait taskData
	 */
	public static Map<String, TaskData> WaitData; 
	
	/**
	 * 视图缓存 key = new Integer(ViewInfo.getOid()) value=ViewInfo
	 */
	public static Map<Integer,ViewInfo> Views;
	
	/**
	 * 工单处理代理人,key=<String>被代理人 value=<FactorInfo>
	 */
	public static Map<String,FactorInfo> Factors;
	
	/**
	 * 导航菜单缓存
	 */
	public static Map Menus;

	//ITSM内部工作组
	public static Map<String,LocalgroupInfo> Localgroup;
	//ITSM内部工作组与人员对应
	public static Map<String,List<String>> Relations_L_P;
	//人员与ITSM内部工作组对应
	public static Map<String,List<String>> Relations_P_L;

	/**-----------用户管理单独出来后的版本使用----------**/
	public static Map<String,WorkgroupInfo> Workgroups;
	public static Map<String,OrganizationInfo> Organizations;
	public static Map<String,PersonInfo> Persons;
	//组内角色，key=组织/工作组.getId()+"_"+RoleInfo.getId();
	public static Map<String,RoleInfo> Roles;	
	//工作组人员关系表(key=W_组织id value=List<PersonInfo>)and(key=P_人员ID value=List<OrganizationInfo>)
	public static Map<String,List<String>> Relations_W_P;
	//组织人员关系表(key=O_组织id value=List<PersonInfo>)and(key=P_人员ID value=List<OrganizationInfo>)
	public static Map<String,List<String>> Relations_O_P;
	// 人员-角色key=WorkgroupInfo.getId()+Common.Split_Str+PersonInfo.getId()或者key=OrganizationInfo.getId()+Common.Split_Str+PersonInfo.getId()
	// value = RoleInfo.getId();
	public static Map<String,String> Relations_M_R;
	// 角色-人员key=WorkgroupInfo.getId()+Common.Split_Str+RoleInfo.getId()或者key=OrganizationInfo.getId()+Common.Split_Str+RoleInfo.getId()
	// value = List<PersonInfo.getId()>;
	public static Map<String,List<String>> Relations_M_P;
	
}
