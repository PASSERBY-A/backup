package com.hp.idc.itsm.security.rule;


/**
 * ��ʾ�ܾ���֯����Ȩ��
 * @author �����
 *
 */
public class RejectOrganizationInfo extends RuleInfo  {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RejectOrganizationInfo() {
		this.desc = "�ܾ���֯ ";
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
		if (checkId(org))
			return -1;
		return 0;
	}
}

