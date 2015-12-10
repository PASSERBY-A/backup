package com.hp.idc.resm.util;

import com.hp.idc.common.CodePage;

/**
 * ������Ϣ���� resm.properties �ļ��ж�ȡ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResmConfig {

	/**
	 * Ψһ��ʶ����������ͬ�ķ�����
	 */
	public static String id;

	/**
	 * ��Դ�������˵ĵ�ַ
	 */
	public static String server;

	/**
	 * ��ʼ����������bean����ʱ����
	 */
	public void init() {
		//			StringUtil.initCodePage();
		//for IDC use the common/codepage
		StringUtil.codeMapping = CodePage.codeMapping; 
	}

	/**
	 * ����Ψһ��ʶ����
	 * 
	 * @param id
	 *            Ψһ��ʶ����
	 */
	public void setId(String id) {
		ResmConfig.id = id;
	}

	/**
	 * ���ò�������Դ�������˵ĵ�ַ
	 * 
	 * @param server
	 *            ��Դ�������˵ĵ�ַ
	 */
	public void setServer(String server) {
		ResmConfig.server = server;
	}
}
