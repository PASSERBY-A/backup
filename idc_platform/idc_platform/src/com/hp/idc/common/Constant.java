/*
 * @(#)Constant.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common;

/**
 * 常量定义
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
	 * 代码类型：模块类别
	 */
	public static int CODETYPE_MODULE = 2;
	
	/**
	 * 每页记录数
	 */
	public static int ITEMS_PER_PAGE = 20;
	
	/**
	 * 本页记录描述
	 */
	public static String MSG_PAGE_DISPLAY = "第 {0} - {1} 条，共 {2} 条记录";

	/**
	 * 无记录的供述
	 */
	public static String MSG_PAGE_EMPTY = "无记录";

	/**
	 * 正在处理的描述
	 */
	public static String MSG_WAIT = "正在处理，请稍候...";

	/**
	 * 成功时的提示框标题
	 */
	public static String MSG_BOXTITLE_SUCCESS = "信息";

	/**
	 * 失败时的提示框标题
	 */
	public static String MSG_BOXTITLE_FAILED = "失败";

	/**
	 * 成功时的提示框内容
	 */
	public static String MSG_SUCCESS = "您的请求已被成功的处理!";
	
	/**
	 * 生成表单时的名称前缀 如FieldInfo.getId() == "user_name"时 生成的html为<input
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
