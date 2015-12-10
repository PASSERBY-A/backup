/**
 * 
 */
package com.hp.idc.resm.security;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class UserRole implements Serializable{
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5664614277522981207L;

	/**
	 * 用户信息
	 */
	private Person user;
	
	/**
	 * 角色权限信息
	 */
	private List<RolePermission> permissions;
	
	/**
	 * 授权信息
	 */
	private List<AuthInfo> authInfos;

	/**
	 * 获取用户信息
	 * @return 用户信息
	 * @see #user
	 */
	public Person getUser() {
		return this.user;
	}

	/**
	 * 设置用户信息
	 * @param user 用户信息
	 * @see #user
	 */
	public void setUser(Person user) {
		this.user = user;
	}

	/**
	 * 获取角色权限信息
	 * @return 角色权限信息
	 * @see #permissions
	 */
	public List<RolePermission> getPermissions() {
		return this.permissions;
	}

	/**
	 * 设置角色权限信息
	 * @param permissions 角色权限信息
	 * @see #permissions
	 */
	public void setPermissions(List<RolePermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * 获取授权信息
	 * @return 授权信息
	 * @see #authInfos
	 */
	public List<AuthInfo> getAuthInfos() {
		return this.authInfos;
	}

	/**
	 * 设置授权信息
	 * @param authInfos 授权信息
	 * @see #authInfos
	 */
	public void setAuthInfos(List<AuthInfo> authInfos) {
		this.authInfos = authInfos;
	}
	
	
}
