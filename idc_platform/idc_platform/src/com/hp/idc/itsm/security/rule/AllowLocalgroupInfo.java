package com.hp.idc.itsm.security.rule;

import java.util.List;

import com.hp.idc.itsm.security.LocalgroupInfo;
import com.hp.idc.itsm.security.LocalgroupManager;

public class AllowLocalgroupInfo  extends RuleInfo  {
	/**
	 * 默认构造函数
	 */
	public AllowLocalgroupInfo() {
		this.desc = "允许本地工作组 ";
	}

	/**
	 * 返回是否允许操作,如果没限制，表示拒绝<br>
	 * <i>v1:改变规则，如果没限制，从原来的返回0改为返回-1</i><br>
	 * <i>v2:改变规则，如果没限制，从原来的返回-1改为返回0</i><br>
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:表示没做限制；1:表示允许；-1:表示拒绝；
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
