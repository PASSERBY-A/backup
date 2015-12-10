package com.hp.idc.resm.util;

import com.hp.idc.common.CodePage;

/**
 * 配置信息，从 resm.properties 文件中读取
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResmConfig {

	/**
	 * 唯一标识，用于区别不同的服务器
	 */
	public static String id;

	/**
	 * 资源管理服务端的地址
	 */
	public static String server;

	/**
	 * 初始化函数，由bean创建时调用
	 */
	public void init() {
		//			StringUtil.initCodePage();
		//for IDC use the common/codepage
		StringUtil.codeMapping = CodePage.codeMapping; 
	}

	/**
	 * 设置唯一标识参数
	 * 
	 * @param id
	 *            唯一标识参数
	 */
	public void setId(String id) {
		ResmConfig.id = id;
	}

	/**
	 * 设置参数：资源管理服务端的地址
	 * 
	 * @param server
	 *            资源管理服务端的地址
	 */
	public void setServer(String server) {
		ResmConfig.server = server;
	}
}
