package com.hp.idc.itsm.common;

import java.util.HashMap;
import java.util.Map;

import com.hp.idc.itsm.task.BaseTableStructure;

/**
 * ϵͳʹ�õĳ�������
 * 
 */
public class Consts {
	
	/**
	 * ����������Ŀ¼�����tomcatĿ¼���磺sysmgr/itsm��
	 */
	public static String ITSM_HOME = "";
	
	public static String EXTJS_HOME = "/itsm";


	public static String CMDB_HOME = "";
	
	public static String CAS_HOME = "";
	
	public static boolean showConfPage = false;
	
	public static String ITSM_TS_TAB = "TS_TAB_WG_ITSM_01";//���ݱ�ռ�
	public static String ITSM_TS_IDX = "TS_IDX_WG_ITSM_01";//������ռ�
	
	/**
	 * �������˵�������(����)
	 */
	public static String ROLLBACK_COUNT = "1000";
	
	/**
	 * ϵͳĬ�϶���ģ��
	 */
	public static SMSTemplate SMS_TEMPLATE;
	
	/**
	 * �������ص�servlet���°汾���ϴ��������ʹ�á�
	 */
	public static String DOWNLOAD_SERVLET = "/itsm/download";
	
	public static String UPLOAD_SERVLET = "/itsm/upload";
	
	/**
	 * ֪ͨ��Ա
	 */
	public static String NOTICE_TO = "notice_to";
	
	/**
	 * ֪ͨ��Ϣ
	 */
	public static String NOTICE_MESSAGE = "notice_message";
	
	/**
	 * ����Ա�ĵ�¼��
	 */
	public static String ADMIN_ID = "root";

	/**
	 * CI��CI֮��Ĺ�����ϵ
	 */
	public static int RELATION_CI_CI = 0;

	/**
	 * ÿҳ��¼��
	 */
	public static int ITEMS_PER_PAGE = 20;

	/**
	 * ��ҳ��¼����
	 */
	public static String MSG_PAGE_DISPLAY = "�� {0} - {1} ������ {2} ����¼";

	/**
	 * �޼�¼�Ĺ���
	 */
	public static String MSG_PAGE_EMPTY = "�޼�¼";

	/**
	 * ���ڴ��������
	 */
	public static String MSG_WAIT = "���ڴ������Ժ�...";

	/**
	 * �ɹ�ʱ����ʾ�����
	 */
	public static String MSG_BOXTITLE_SUCCESS = "��Ϣ";

	/**
	 * ʧ��ʱ����ʾ�����
	 */
	public static String MSG_BOXTITLE_FAILED = "ʧ��";

	/**
	 * �ɹ�ʱ����ʾ������
	 */
	public static String MSG_SUCCESS = "���������ѱ��ɹ��Ĵ���";

	/**
	 * ���ɱ�ʱ������ǰ׺ ��FieldInfo.getId() == "user_name"ʱ ���ɵ�htmlΪ<input
	 * name="fld_user_name" ..../>
	 */
	public static String FLD_PREFIX = "fld_";

	/**
	 * ��ִ���ˡ��ֶε�ID
	 */
	public static String FLD_EXECUTE_USER = "execute_user";
	
	/**
	 * "������"���߽С���֪�ˡ��ֶ�ID�����ύ������ֶβ�Ϊ��
	 * �������Ա���ܵ�һ�����Ĺ������򿪺�ֻ����д��֪��Ϣ��
	 */
	public static String FLD_READ_USER = "read_user";
	
	/**
	 * ��ִ���ˡ��ֶε�ID,�������䵽���
	 */
	public static String FLD_EXECUTE_GROUP_USER = "execute_group_user";
	
	/**
	 * "����"�ֶ�
	 */
	public static String FLD_ATTACHMENT = "attachments";

	/**
	 * �����ˡ��ֶε�ID
	 */
	public static String FLD_ROLLBACK = "rollback";

	/**
	 * ����Ϣ���ֶε�ID
	 */
	public static String FLD_MESSAGE = "message";
	
	/**
	 * ϵͳ�ֶ��б�
	 */
	public static Map<String,String> SYSTEM_FIELDS = new HashMap<String,String>();

	/**
	 * Ĭ�ϱ�ID
	 */
	public static int FORM_DEFAULT = 1;

	/**
	 * �������ͣ����������
	 */
	public static int CODETYPE_CICATEGORY = 1;

	/**
	 * �������ͣ�ģ�����
	 */
	public static int CODETYPE_MODULE = 2;

	/**
	 * �����������֯
	 */
	public static int CICATEGORY_ORGANIZATION = 3;

	/**
	 * ��������𣺹�����
	 */
	public static int CICATEGORY_WORKGROUP = 4;

	/**
	 * �����������Ա
	 */
	public static int CICATEGORY_PERSON = 5;

	/**
	 * ��ϵ:������->����
	 */
	public static int RT_WORKGROUP_PERSON = 1;

	/**
	 * ��ϵ:����->������
	 */
	public static int RT_PERSON_WORKGROUP = 2;

	/**
	 * ��ϵ:��֯->����
	 */
	public static int RT_ORGANIZATION_PERSON = 3;

	/**
	 * ��ϵ:����->��֯
	 */
	public static int RT_PERSON_ORGANIZATION = 4;
	
	/**
	 * ���ع�����
	 */
	public static int RT_LOCALGROUP_PERSON = 5;

	
	public static void init(){
		
		BaseTableStructure.init();
	}
	
	public String getITSM_HOME() {
		return ITSM_HOME;
	}

	public void setITSM_HOME(String itsm_home) {
		ITSM_HOME = itsm_home;
		MacroManager.setMacro("$ITSM_HOME", itsm_home);
	}
	
	public String getCMDB_HOME() {
		return CMDB_HOME;
	}

	public void setCMDB_HOME(String cmdb_home) {
		CMDB_HOME = cmdb_home;
	}

	public String getCAS_HOME() {
		return CAS_HOME;
	}

	public void setCAS_HOME(String cas_home) {
		CAS_HOME = cas_home;
	}
	/**
	 * @param iTSMTSTAB the iTSM_TS_TAB to set
	 */
	public void setITSM_TS_TAB(String iTSMTSTAB) {
		ITSM_TS_TAB = iTSMTSTAB;
	}

	/**
	 * @param iTSMTSIDX the iTSM_TS_IDX to set
	 */
	public void setITSM_TS_IDX(String iTSMTSIDX) {
		ITSM_TS_IDX = iTSMTSIDX;
	}

	public String getDOWNLOAD_SERVLET() {
		return DOWNLOAD_SERVLET;
	}

	public void setDOWNLOAD_SERVLET(String down_servlet) {
		DOWNLOAD_SERVLET = down_servlet;
	}

	public String getUPLOAD_SERVLET() {
		return UPLOAD_SERVLET;
	}

	public void setUPLOAD_SERVLET(String upload_servlet) {
		UPLOAD_SERVLET = upload_servlet;
	}


	public String getEXTJS_HOME() {
		return EXTJS_HOME;
	}

	public void setEXTJS_HOME(String extjs_home) {
		EXTJS_HOME = extjs_home;
	}

	public String getROLLBACK_COUNT() {
		return ROLLBACK_COUNT;
	}

	public void setROLLBACK_COUNT(String rOLLBACKCOUNT) {
		ROLLBACK_COUNT = rOLLBACKCOUNT;
	}
	
}
