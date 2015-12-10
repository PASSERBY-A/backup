/*
 * @(#)Constant.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common;

/**
 * ��������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, Mar 17, 2011 2011
 * 
 */

public final class Constant {

	public static final String SESSION_LOGIN = "session_login";
	
	public static final String REIDRECT_URL = "redirect_url";

	public static String JS_PATH;
	
	public static String APP_PATH;
	
	public static String CSS_PATH;
	
	/**
	 * �������ͣ�ģ�����
	 */
	public static int CODETYPE_MODULE = 2;
	
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
	public static String MSG_SUCCESS = "���������ѱ��ɹ��Ĵ���!";
	
	/**
	 * ���ɱ�ʱ������ǰ׺ ��FieldInfo.getId() == "user_name"ʱ ���ɵ�htmlΪ<input
	 * name="fld_user_name" ..../>
	 */
	public static String FLD_PREFIX = "fld_";

	/**
	 * @return the jS_PATH
	 */
	public String getJS_PATH() {
		return JS_PATH;
	}

	/**
	 * @param jSPATH the jS_PATH to set
	 */
	public void setJS_PATH(String jSPATH) {
		JS_PATH = jSPATH;
	}

	/**
	 * @return the aPP_PATH
	 */
	public String getAPP_PATH() {
		return APP_PATH;
	}

	/**
	 * @param aPPPATH the aPP_PATH to set
	 */
	public void setAPP_PATH(String aPPPATH) {
		APP_PATH = aPPPATH;
	}

	/**
	 * @return the cSS_PATH
	 */
	public static String getCSS_PATH() {
		return CSS_PATH;
	}

	/**
	 * @param cSSPATH the cSS_PATH to set
	 */
	public static void setCSS_PATH(String cSSPATH) {
		CSS_PATH = cSSPATH;
	}

}
