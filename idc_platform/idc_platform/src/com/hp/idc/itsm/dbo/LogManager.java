package com.hp.idc.itsm.dbo;


/**
 * ������־����
 * @author ÷԰
 */
public class LogManager {
	/**
	 * ��ȡָ��������������Ϣ
	 * @param tableName ָ���ı���
	 * @return ����ָ��������������Ϣ
	 */
	static public LogInfo getLogInfo(String tableName) {
		return null;
	}
	
	/**
	 * �����ڶ�ָ��������ָ���н���ָ������ʱ�Ƿ���Ҫ�ǲ�����־
	 * @param tableName Ҫ�����ı���
	 * @param columnName Ҫ����������
	 * @param operType ��������, "update", "insert", "delete"
	 * @return ��Ҫ��¼ʱ���� true, ���򷵻� false
	 */
	public static boolean isLogable(String tableName, String columnName, String operType) {
		return false;
	}
}
