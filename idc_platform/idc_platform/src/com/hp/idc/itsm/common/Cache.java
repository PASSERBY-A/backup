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
 * �������Ļ�������࣬
 * 
 * @author ÷԰
 * 
 */
public class Cache {
	/**
	 * ��ʼ��״̬��δ��ʼ��
	 */
	static public int INIT_NONE = 0;

	/**
	 * ��ʼ��״̬��������
	 */
	static public int INIT_INPROGRESS = 1;

	/**
	 * ��ʼ��״̬���ɹ�
	 */
	static public int INIT_OK = 2;

	/**
	 * ��ʼ��״̬��ʧ��
	 */
	static public int INIT_FAIL = 3;

	/**
	 * ��ʾ�����Ƿ��ѱ���ʼ��
	 */
	static public int initStatus = INIT_NONE;

	/**
	 * GBK��ӦUNICODE��
	 */
	public static int CodeMapping[] = new int[65536];

	/**
	 * ��ʼ�������
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
	 * ��ʼ����������� �˺����ڷ���������ʱ�Զ�����
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
	 * ���³�ʼ������
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
		
		//���ش������б�
		PersonManager.loadFactors();
		LocalgroupManager.initLocalgroup();
		
		WorkflowManager.initCache();
		ITSMTaskManagerImpl itm = new ITSMTaskManagerImpl();
		itm.initCache();
		
	}

	/**
	 * ���̻��� key = new Integer(WorkflowInfo.getOid()) value = WorkflowInfo
	 */
	public static Map<Integer,WorkflowInfo> Workflows;

	/**
	 * ������ϵ��𻺴� key = new Integer(RelationTypeInfo.getOid()) value =
	 * RelationTypeInfo
	 */
	public static Map RelationTypes;

	/**
	 * ������ϵ���� key = RelationInfo.getId() value = RelationInfo
	 */
	public static Map Relations;

	/**
	 * �������ͻ��� key = new Integer(CodeTypeInfo.getOid()) value = CodeTypeInfo
	 */
	public static Map CodeTypes;

	/**
	 * ���뻺�棬����������OID���Ҵ��� key = new Integer(CodeTypeInfo.getOid()) value = List<CodeInfo>,�����������µ�����CodeInfo
	 */
	public static Map Codes;
	
	/**
	 * ���뻺�棬����������OID���Ҵ��� key = new Integer(CodeTypeInfo.getOid()) value = List<CodeInfo>��ֻ������һ���Ĵ��룬�Ӽ�����ͨ��CodeInfo�������
	 */
	public static Map CodesTree;

	/**
	 * ���뻺�� key = new Integer(CodeInfo.getOid()) value = CodeInfo
	 */
	public static Map AllCodes;
	
	/**
	 * ���뻺�� key =CodeInfo.getCodeId() value = CodeInfo
	 */
	public static Map AllCodes2;
	
	/**
	 * ��������� key = new Integer(CodeInfo.getOid()) value = CodeInfo
	 */
	public static Map CICategory;
	
	/**
	 * ��ʽ����������������� List[CodeInfo]
	 */
	public static List CICategoryTree;
	
	public static Map CategoryToCI;

	/**
	 * ������� key = CIInfo.getId() value = CIInfo
	 */
	public static Map CIs;

	/**
	 * ������� key = new Integer(CIInfo.getOid()) value = CIInfo
	 */
	public static Map CIs2;

	/**
	 * �ֶλ��� key = FieldInfo.getId() value = FieldInfo
	 */
	public static Map Fields;

	/**
	 * �ֶλ��� key = new Integer(FieldInfo.getOid()) value = FieldInfo
	 */
	public static Map Fields2;

	/**
	 * �ֶ����ͻ��� key = �������硰com.hp.idc.itsm.configure.fields.FileFieldInfo��
	 * value = ˵�����硰������
	 */
	public static Map FieldTypes;

	/**
	 * ������ key = new Integer(FormInfo.getOid()) value = FormInfo
	 */
	public static Map<Integer,FormInfo> Forms;
	
	/**
	 * �򿪵����񻺴� 
	 * key = TaskInfo.getOrigin()+"_"+TaskInfo.getWfOid()+"_"+TaskInfo.getOid() value=TaskInfo
	 */
	public static Map<String,TaskInfo> Tasks;
	
	/**
	 * ��ʷ��������(����������ã��򿪻�������ʷ����)
	 * key = TaskInfo.getOrigin()+"_"+TaskInfo.getWfOid()+"_"+TaskInfo.getOid() value=TaskInfo
	 */
	public static Map<String,TaskInfo> TasksHis;
	
	/**
	 * �����´򿪵Ĺ����б���
	 * key= WorkflowInfo.getOid(); value=Map(key=����id,value=TaskInfo)
	 */
	public static Map<String,Map<String,TaskInfo>> Workflow_Tasks;
	/**
	 * �����¹رյĹ����б���
	 */
	public static Map<String,Map<String,TaskInfo>> Workflow_TasksHis;

	/**
	 * wait taskData
	 */
	public static Map<String, TaskData> WaitData; 
	
	/**
	 * ��ͼ���� key = new Integer(ViewInfo.getOid()) value=ViewInfo
	 */
	public static Map<Integer,ViewInfo> Views;
	
	/**
	 * �������������,key=<String>�������� value=<FactorInfo>
	 */
	public static Map<String,FactorInfo> Factors;
	
	/**
	 * �����˵�����
	 */
	public static Map Menus;

	//ITSM�ڲ�������
	public static Map<String,LocalgroupInfo> Localgroup;
	//ITSM�ڲ�����������Ա��Ӧ
	public static Map<String,List<String>> Relations_L_P;
	//��Ա��ITSM�ڲ��������Ӧ
	public static Map<String,List<String>> Relations_P_L;

	/**-----------�û�������������İ汾ʹ��----------**/
	public static Map<String,WorkgroupInfo> Workgroups;
	public static Map<String,OrganizationInfo> Organizations;
	public static Map<String,PersonInfo> Persons;
	//���ڽ�ɫ��key=��֯/������.getId()+"_"+RoleInfo.getId();
	public static Map<String,RoleInfo> Roles;	
	//��������Ա��ϵ��(key=W_��֯id value=List<PersonInfo>)and(key=P_��ԱID value=List<OrganizationInfo>)
	public static Map<String,List<String>> Relations_W_P;
	//��֯��Ա��ϵ��(key=O_��֯id value=List<PersonInfo>)and(key=P_��ԱID value=List<OrganizationInfo>)
	public static Map<String,List<String>> Relations_O_P;
	// ��Ա-��ɫkey=WorkgroupInfo.getId()+Common.Split_Str+PersonInfo.getId()����key=OrganizationInfo.getId()+Common.Split_Str+PersonInfo.getId()
	// value = RoleInfo.getId();
	public static Map<String,String> Relations_M_R;
	// ��ɫ-��Աkey=WorkgroupInfo.getId()+Common.Split_Str+RoleInfo.getId()����key=OrganizationInfo.getId()+Common.Split_Str+RoleInfo.getId()
	// value = List<PersonInfo.getId()>;
	public static Map<String,List<String>> Relations_M_P;
	
}
