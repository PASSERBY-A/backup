package com.hp.idc.itsm.security.rule;


/**
 * �ܾ����е�Ȩ�޿�����
 * 
 * @author ÷԰
 * 
 */
public class RejectAllRuleInfo extends RuleInfo {
	/**
	 * ��ȡ���������
	 * 
	 * @return ���ع��������
	 */
	public String getDesc() {
		return "�ܾ�����";
	}

	/**
	 * �����Ƿ��������
	 * 
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:��ʾû�����ƣ�1:��ʾ����-1:��ʾ�ܾ���
	 */
	public int valid(String user, String org, String[] group) {
		return -1;
	}

}
