package com.hp.idc.portal.security.rule;

import com.hp.idc.portal.security.OrganizationManager;


/**
 * 表示拒绝组织访问权限
 * @author 李会争
 *
 */
public class RejectOrganizationInfo extends RuleInfo  {
	/**
	 * 默认构造函数
	 */
	public RejectOrganizationInfo() {
		this.desc = "拒绝组织 ";
	}

	/**
	 * 返回是否允许操作,如果没做限制,默认允许<br>
	 * <i>v1:改变规则，如果没限制，从原来的返回0改为返回1</i><br>
	 * <i>v2:改变规则，如果没限制，从原来的返回1改为返回0</i><br>
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:表示没做限制；1:表示允许；-1:表示拒绝；
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

