package com.hp.idc.itsm.security;


/**
 * Ȩ�޿��ƽӿ�
 * @author �����
 *
 */
public interface IRuleInfo {
	/**
	 * �����Ƿ��������
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:��ʾû�����ƣ�1:��ʾ����-1:��ʾ�ܾ���
	 */
	public int valid(String user, String org, String[] group);
	
	/**
	 * ��ȡ���������
	 * @return ���ع��������
	 */
	public String getDesc();
}
