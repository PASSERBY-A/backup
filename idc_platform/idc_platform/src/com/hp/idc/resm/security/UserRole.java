/**
 * 
 */
package com.hp.idc.resm.security;

import java.io.Serializable;
import java.util.List;

/**
 * �û�Ȩ����Ϣ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class UserRole implements Serializable{
	
	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 5664614277522981207L;

	/**
	 * �û���Ϣ
	 */
	private Person user;
	
	/**
	 * ��ɫȨ����Ϣ
	 */
	private List<RolePermission> permissions;
	
	/**
	 * ��Ȩ��Ϣ
	 */
	private List<AuthInfo> authInfos;

	/**
	 * ��ȡ�û���Ϣ
	 * @return �û���Ϣ
	 * @see #user
	 */
	public Person getUser() {
		return this.user;
	}

	/**
	 * �����û���Ϣ
	 * @param user �û���Ϣ
	 * @see #user
	 */
	public void setUser(Person user) {
		this.user = user;
	}

	/**
	 * ��ȡ��ɫȨ����Ϣ
	 * @return ��ɫȨ����Ϣ
	 * @see #permissions
	 */
	public List<RolePermission> getPermissions() {
		return this.permissions;
	}

	/**
	 * ���ý�ɫȨ����Ϣ
	 * @param permissions ��ɫȨ����Ϣ
	 * @see #permissions
	 */
	public void setPermissions(List<RolePermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * ��ȡ��Ȩ��Ϣ
	 * @return ��Ȩ��Ϣ
	 * @see #authInfos
	 */
	public List<AuthInfo> getAuthInfos() {
		return this.authInfos;
	}

	/**
	 * ������Ȩ��Ϣ
	 * @param authInfos ��Ȩ��Ϣ
	 * @see #authInfos
	 */
	public void setAuthInfos(List<AuthInfo> authInfos) {
		this.authInfos = authInfos;
	}
	
	
}
