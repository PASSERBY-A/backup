package com.hp.idc.portal.security.rule;

import com.hp.idc.portal.security.OrganizationManager;


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
		for (int i = 0; i < this.count; i++) {
			try {
				if(new OrganizationManager().personIsInOrganization(user, this.ids[i], true)){
					return -1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}

