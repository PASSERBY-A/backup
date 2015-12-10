package com.hp.idc.itsm.security;


/**
 * 权限控制接口
 * @author 李会争
 *
 */
public interface IRuleInfo {
	/**
	 * 返回是否允许操作
	 * @param user
	 * @param org
	 * @param group
	 * @return 0:表示没做限制；1:表示允许；-1:表示拒绝；
	 */
	public int valid(String user, String org, String[] group);
	
	/**
	 * 获取规则的描述
	 * @return 返回规则的描述
	 */
	public String getDesc();
}
