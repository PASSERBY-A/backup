package com.hp.idc.portal.security.rule;


/**
 * ��ʾ�ܾ����������Ȩ��
 * @author �����
 *
 */
public class RejectWorkgroupInfo extends RuleInfo  {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RejectWorkgroupInfo() {
		this.desc = "�ܾ������� ";
	}

	/**
	 * �����Ƿ��������,���û������,Ĭ������<br>
	 * <i>v1:�ı�������û���ƣ���ԭ���ķ���0��Ϊ����1</i><br>
	 * <i>v2:�ı�������û���ƣ���ԭ���ķ���1��Ϊ����0</i><br>
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:��ʾû�����ƣ�1:��ʾ����-1:��ʾ�ܾ���
	 */
	public int valid(String user, String org, String[] group) {
		for (int i = 0; i < group.length; i++)
			if (checkId(group[i]))
				return -1;
		return 0;
	}
}

