package com.hp.idc.portal.security;

import java.util.*;

import com.hp.idc.common.CodePage;
import com.hp.idc.portal.mgr.SystemConfig;

public class Common {

	public static String Split_Str = "_|_";

	public void init() throws Exception {
		/**
		 * ��ʼ����������
		 */
		Cache.reloadAuc();
		
		//for idc use the common/codepage
		//Cache.initCodePage();
		Cache.CodeMapping = CodePage.codeMapping;
		
		SystemConfig.initData();
	}

	/**
	 * �������ص�servlet���°汾���ϴ��������ʹ�á�
	 */
	public static String DOWNLOAD_SERVLET = "/portal/download";

	public static String UPLOAD_SERVLET = "/portal/upload";

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
	 * "������"���߽С���֪�ˡ��ֶ�ID�����ύ������ֶβ�Ϊ�� �������Ա���ܵ�һ�����Ĺ������򿪺�ֻ����д��֪��Ϣ��
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
	public static Map<String, String> SYSTEM_FIELDS = new HashMap<String, String>();

	/**
	 * ������״̬������
	 */
	public static int STATUS_NORMAL = 0;

	/**
	 * ������״̬����ɾ��
	 */
	public static int STATUS_DELETED = 1;
	
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

}
