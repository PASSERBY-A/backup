package com.hp.idc.itsm.security.rule;


/**
 * 拒绝所有的权限控制类
 * 
 * @author 梅园
 * 
 */
public class RejectAllRuleInfo extends RuleInfo {
	/**
	 * 获取规则的描述
	 * 
	 * @return 返回规则的描述
	 */
	public String getDesc() {
		return "拒绝所有";
	}

	/**
	 * 返回是否允许操作
	 * 
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:表示没做限制；1:表示允许；-1:表示拒绝；
	 */
	public int valid(String user, String org, String[] group) {
		return -1;
	}

}
