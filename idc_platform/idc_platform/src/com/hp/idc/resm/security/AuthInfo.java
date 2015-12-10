/**
 * 
 */
package com.hp.idc.resm.security;

import java.io.Serializable;

/**
 * ��Ȩ��Ϣ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @see UserRole
 */
public class AuthInfo implements Serializable {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 2382060492796630523L;

	/**
	 * ��ʼ����
	 */
	private int startIndex;

	/**
	 * ��������
	 */
	private int endIndex;

	/**
	 * ��ɫ��Ϣ
	 */
	private Role role;

	/**
	 * ��Ȩ��ʽ
	 * 
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	private int authType;

	/**
	 * ��ȡ��ʼ����
	 * 
	 * @return ��ʼ����
	 * @see #startIndex
	 */
	public int getStartIndex() {
		return this.startIndex;
	}

	/**
	 * ���ÿ�ʼ����
	 * 
	 * @param startIndex
	 *            ��ʼ����
	 * @see #startIndex
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 * @see #endIndex
	 */
	public int getEndIndex() {
		return this.endIndex;
	}

	/**
	 * ���ý�������
	 * 
	 * @param endIndex
	 *            ��������
	 * @see #endIndex
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * ��ȡ��ɫ��Ϣ
	 * 
	 * @return ��ɫ��Ϣ
	 * @see #role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * ���ý�ɫ��Ϣ
	 * 
	 * @param role
	 *            ��ɫ��Ϣ
	 * @see #role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * ��ȡ��Ȩ��ʽ
	 * 
	 * @return ��Ȩ��ʽ
	 * @see #authType
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	public int getAuthType() {
		return this.authType;
	}

	/**
	 * ������Ȩ��ʽ
	 * 
	 * @param authType
	 *            ��Ȩ��ʽ
	 * @see #authType
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	public void setAuthType(int authType) {
		this.authType = authType;
	}

}
