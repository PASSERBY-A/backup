package com.hp.idc.common.upload;


import org.dom4j.DocumentException;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 8:10:11 AM Aug 23, 2011
 *
 */
public class Common {

	// ����Ŀ¼
	public static String SAVE_PATH = "";

	// ��ʱĿ¼���ļ���ʱ��ʹ��
	public static String TEMP_PATH = "";

	// �ϴ����棬Ĭ��4KB
	public static int UPLOAD_CACHE = 4096;

	// �ļ�����С��Ĭ����40M
	public static long FILE_MAX_SIZE = 41943040;

	public void init() throws DocumentException {

	}

	public String getSAVE_PATH() {
		return SAVE_PATH;
	}

	public void setSAVE_PATH(String save_path) {
		SAVE_PATH = save_path;
	}

	public String getTEMP_PATH() {
		return TEMP_PATH;
	}

	public void setTEMP_PATH(String temp_path) {
		TEMP_PATH = temp_path;
	}

	public static int getUPLOAD_CACHE() {
		return UPLOAD_CACHE;
	}

	public void setUPLOAD_CACHE(int uPLOADCACHE) {
		UPLOAD_CACHE = uPLOADCACHE;
	}

	public static long getFILE_MAX_SIZE() {
		return FILE_MAX_SIZE;
	}

	public void setFILE_MAX_SIZE(long fILEMAXSIZE) {
		FILE_MAX_SIZE = fILEMAXSIZE;
	}

}
