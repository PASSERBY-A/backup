/**
 * 
 */
package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.security.BasicPermission;
import com.hp.idc.resm.security.Role;
import com.hp.idc.resm.security.RolePermission;
import com.hp.idc.resm.security.UserRole;

/**
 * Ȩ����ط���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IRoleService {

	/**
	 * ��ȡ���л���Ȩ��
	 * 
	 * @return ���л���Ȩ���б�
	 */
	public List<BasicPermission> getAllPermissions();
	
	/**
	 * ����id��ȡ����Ȩ�޶���
	 * @param id id
	 * @return ����Ȩ�޶���
	 */
	public BasicPermission getPermissionById(String id);

	/**
	 * ��ȡ���н�ɫ����
	 * 
	 * @return ��ɫ����
	 */
	public List<Role> getAllRoles();

	/**
	 * ��ȡ�û�ӵ�еĽ�ɫ����
	 * @param userId �û�id
	 * 
	 * @return ��ɫ�����б�
	 */
	public List<Role> getRolesByUser(int userId);
	
	/**
	 * ��ȡ��ɫ����
	 * 
	 * @param roleId
	 *            ��ɫid
	 * @return ��ɫ����
	 */
	public Role getRoleById(String roleId);

	/**
	 * ���½�ɫ����
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param userId
	 *            �����û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void updateRole(Role role, int userId) throws Exception;

	/**
	 * ɾ��һ���û��ͽ�ɫ�Ĺ���
	 * @param userId �û�id
	 * @param roleId ��ɫid
	 * @param opUserId ������id
	 * @throws Exception �������쳣ʱ����
	 */
	public void removeUserRole(int userId, String roleId, int opUserId) throws Exception;
	
	/**
	 * ɾ����ɫ����
	 * 
	 * @param roleId
	 *            ��ɫ����id
	 * @param userId
	 *            �����û�id
	 * @throws Exception
	 *             ɾ�����쳣ʱ����
	 */
	public void deleteRole(String roleId, int userId) throws Exception;

	/**
	 * ��ȡ��ɫ������Ȩ��
	 * 
	 * @param roleId
	 *            ��ɫid
	 * @return ��ɫ������Ȩ��
	 */
	public List<RolePermission> getRolePermissions(String roleId);

	/**
	 * �ж��û��Ƿ���ĳ����ɫ
	 * 
	 * @param userId
	 *            �û�id
	 * @param roleId
	 *            ��ɫid
	 * @return Role.AUTH_NONE=û�У�Role.AUTH_BY_PERSON=ֱ��ͨ���û���Ȩ,
	 *         Role.AUTH_BY_ORGANIZATION=ͨ����֯��Ȩ
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	public int checkRole(int userId, String roleId);
	
	/**
	 * ��ȡ�û�Ȩ����Ϣ
	 * @param userId �û�id
	 * @return �û�Ȩ����Ϣ
	 * @throws Exception �������쳣ʱ����
	 */
	public UserRole getUserRole(int userId) throws Exception;

	/**
	 * �ж��û��Ƿ���ĳ����ɫ
	 * 
	 * @param userId
	 *            �û�id
	 * @param roleId
	 *            ��ɫid
	 * @return false=û�У�true=��
	 */
	public boolean hasRole(int userId, String roleId);

	/**
	 * ɾ����ɫȨ��
	 * 
	 * @param id
	 *            ��ɫȨ��id
	 * @param userId
	 *            �û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void deleteRolePermission(int id, int userId) throws Exception;

	/**
	 * ������ɫȨ��
	 * 
	 * @param rp
	 *            ��ɫȨ�޶���
	 * @param userId
	 *            �û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void addRolePermission(RolePermission rp, int userId)
			throws Exception;

	/**
	 * ��ȡ��ɫȨ�޶���
	 * 
	 * @param id
	 *            ��ɫȨ��id
	 * @return ��ɫȨ�޶���
	 */
	public RolePermission getRolePermissionById(int id);
	
	/**
	 * ��ȡ�û���Ȩ���б�
	 * @param permId Ȩ��id
	 * @param userId �û�id
	 * @return �û���Ȩ���б�
	 */
	public List<RolePermission> getUserPermissions(int userId, String permId);
	
	/**
	 * ����û��Ƿ���ĳ��Ȩ�ޣ���������Ҫ��ȡ�û�Ȩ���б���˵���Ҫ�������Ȩ��ʱ����ʹ��
	 * getUserPermissions�ʹ�list������checkUserPermission���������Ч��
	 * @param userId �û�id
	 * @param permId Ȩ��id
	 * @param param Ȩ�޲���
	 * @return true=��Ȩ�ޣ�false=��Ȩ��
	 */
	public boolean checkUserPermission(int userId, String permId, Object param);
	
	/**
	 * ����û��Ƿ���ĳ��Ȩ��
	 * @param userId �û�id
	 * @param list �û���Ȩ���б�
	 * @param param Ȩ�޲���
	 * @return true=��Ȩ�ޣ�false=��Ȩ��
	 */
	public boolean checkUserPermission(int userId, List<RolePermission> list, Object param);

	/**
	 * ���½�ɫ��������֯
	 * 
	 * @param roleId
	 *            ��ɫid
	 * @param orgIds
	 *            ��֯id����","�ָ�
	 * @param userId
	 *            �����û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void updateRoleOrganization(String roleId, String orgIds, int userId)
			throws Exception;

	/**
	 * ���½�ɫ��������Ա
	 * 
	 * @param roleId
	 *            ��ɫid
	 * @param personIds
	 *            ��Աid����","�ָ�
	 * @param userId
	 *            �����û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void updateRolePerson(String roleId, String personIds, int userId)
			throws Exception;

}
