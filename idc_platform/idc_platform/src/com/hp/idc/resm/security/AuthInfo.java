/**
 * 
 */
package com.hp.idc.resm.security;

import java.io.Serializable;

/**
 * 授权信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @see UserRole
 */
public class AuthInfo implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2382060492796630523L;

	/**
	 * 开始索引
	 */
	private int startIndex;

	/**
	 * 结束索引
	 */
	private int endIndex;

	/**
	 * 角色信息
	 */
	private Role role;

	/**
	 * 授权方式
	 * 
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	private int authType;

	/**
	 * 获取开始索引
	 * 
	 * @return 开始索引
	 * @see #startIndex
	 */
	public int getStartIndex() {
		return this.startIndex;
	}

	/**
	 * 设置开始索引
	 * 
	 * @param startIndex
	 *            开始索引
	 * @see #startIndex
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * 获取结束索引
	 * 
	 * @return 结束索引
	 * @see #endIndex
	 */
	public int getEndIndex() {
		return this.endIndex;
	}

	/**
	 * 设置结束索引
	 * 
	 * @param endIndex
	 *            结束索引
	 * @see #endIndex
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * 获取角色信息
	 * 
	 * @return 角色信息
	 * @see #role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * 设置角色信息
	 * 
	 * @param role
	 *            角色信息
	 * @see #role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * 获取授权方式
	 * 
	 * @return 授权方式
	 * @see #authType
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	public int getAuthType() {
		return this.authType;
	}

	/**
	 * 设置授权方式
	 * 
	 * @param authType
	 *            授权方式
	 * @see #authType
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	public void setAuthType(int authType) {
		this.authType = authType;
	}

}
