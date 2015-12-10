package com.hp.idc.itsm.security.rule;

import java.util.List;

import com.hp.idc.itsm.security.LocalgroupInfo;
import com.hp.idc.itsm.security.LocalgroupManager;

public class AllowLocalgroupInfo  extends RuleInfo  {
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public AllowLocalgroupInfo() {
		this.desc = "�����ع����� ";
	}

	/**
	 * �����Ƿ��������,���û���ƣ���ʾ�ܾ�<br>
	 * <i>v1:�ı�������û���ƣ���ԭ���ķ���0��Ϊ����-1</i><br>
	 * <i>v2:�ı�������û���ƣ���ԭ���ķ���-1��Ϊ����0</i><br>
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:��ʾû�����ƣ�1:��ʾ����-1:��ʾ�ܾ���
	 */
	public int valid(String user, String org, String[] group) {
		List<LocalgroupInfo> lgs = LocalgroupManager.getGroupsOfPerson(user, false);
		for (int i = 0; i < lgs.size(); i++) {
			String id = lgs.get(i).getId();
			if (checkId(id))
				return 1;
		}
		return 0;
	}
}
