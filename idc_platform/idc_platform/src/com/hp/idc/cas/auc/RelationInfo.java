package com.hp.idc.cas.auc;

/**
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class RelationInfo {

	/**
	 * 对象ID，可能是工作组的，可能是组织的，可能是人员的。
	 */
	private String moId = "";
	
	/**
	 * 角色ID。
	 */
	private String roleId = "";

	public String getMoId() {
		return moId;
	}

	public void setMoId(String moId) {
		this.moId = moId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
