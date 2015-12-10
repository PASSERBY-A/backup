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
 * 权限相关服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IRoleService {

	/**
	 * 获取所有基本权限
	 * 
	 * @return 所有基本权限列表
	 */
	public List<BasicPermission> getAllPermissions();
	
	/**
	 * 根据id获取基本权限对象
	 * @param id id
	 * @return 基本权限对象
	 */
	public BasicPermission getPermissionById(String id);

	/**
	 * 获取所有角色定义
	 * 
	 * @return 角色定义
	 */
	public List<Role> getAllRoles();

	/**
	 * 获取用户拥有的角色定义
	 * @param userId 用户id
	 * 
	 * @return 角色定义列表
	 */
	public List<Role> getRolesByUser(int userId);
	
	/**
	 * 获取角色定义
	 * 
	 * @param roleId
	 *            角色id
	 * @return 角色定义
	 */
	public Role getRoleById(String roleId);

	/**
	 * 更新角色定义
	 * 
	 * @param role
	 *            角色定义
	 * @param userId
	 *            操作用户id
	 * @throws Exception
	 *             更新有异常时发生
	 */
	public void updateRole(Role role, int userId) throws Exception;

	/**
	 * 删除一个用户和角色的关联
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @param opUserId 操作人id
	 * @throws Exception 操作有异常时发生
	 */
	public void removeUserRole(int userId, String roleId, int opUserId) throws Exception;
	
	/**
	 * 删除角色定义
	 * 
	 * @param roleId
	 *            角色定义id
	 * @param userId
	 *            操作用户id
	 * @throws Exception
	 *             删除有异常时发生
	 */
	public void deleteRole(String roleId, int userId) throws Exception;

	/**
	 * 获取角色的所有权限
	 * 
	 * @param roleId
	 *            角色id
	 * @return 角色的所有权限
	 */
	public List<RolePermission> getRolePermissions(String roleId);

	/**
	 * 判断用户是否有某个角色
	 * 
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @return Role.AUTH_NONE=没有，Role.AUTH_BY_PERSON=直接通过用户授权,
	 *         Role.AUTH_BY_ORGANIZATION=通过组织授权
	 * @see Role#AUTH_NONE
	 * @see Role#AUTH_BY_PERSON
	 * @see Role#AUTH_BY_ORGANIZATION
	 */
	public int checkRole(int userId, String roleId);
	
	/**
	 * 获取用户权限信息
	 * @param userId 用户id
	 * @return 用户权限信息
	 * @throws Exception 操作有异常时发生
	 */
	public UserRole getUserRole(int userId) throws Exception;

	/**
	 * 判断用户是否有某个角色
	 * 
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @return false=没有，true=有
	 */
	public boolean hasRole(int userId, String roleId);

	/**
	 * 删除角色权限
	 * 
	 * @param id
	 *            角色权限id
	 * @param userId
	 *            用户id
	 * @throws Exception
	 *             更新有异常时发生
	 */
	public void deleteRolePermission(int id, int userId) throws Exception;

	/**
	 * 新增角色权限
	 * 
	 * @param rp
	 *            角色权限对象
	 * @param userId
	 *            用户id
	 * @throws Exception
	 *             更新有异常时发生
	 */
	public void addRolePermission(RolePermission rp, int userId)
			throws Exception;

	/**
	 * 获取角色权限对象
	 * 
	 * @param id
	 *            角色权限id
	 * @return 角色权限对象
	 */
	public RolePermission getRolePermissionById(int id);
	
	/**
	 * 获取用户的权限列表
	 * @param permId 权限id
	 * @param userId 用户id
	 * @return 用户的权限列表
	 */
	public List<RolePermission> getUserPermissions(int userId, String permId);
	
	/**
	 * 检查用户是否有某个权限，本函数需要获取用户权限列表，因此当需要批量检测权限时，请使用
	 * getUserPermissions和带list参数的checkUserPermission函数以提高效率
	 * @param userId 用户id
	 * @param permId 权限id
	 * @param param 权限参数
	 * @return true=有权限，false=无权限
	 */
	public boolean checkUserPermission(int userId, String permId, Object param);
	
	/**
	 * 检查用户是否有某个权限
	 * @param userId 用户id
	 * @param list 用户的权限列表
	 * @param param 权限参数
	 * @return true=有权限，false=无权限
	 */
	public boolean checkUserPermission(int userId, List<RolePermission> list, Object param);

	/**
	 * 更新角色关联的组织
	 * 
	 * @param roleId
	 *            角色id
	 * @param orgIds
	 *            组织id，以","分隔
	 * @param userId
	 *            操作用户id
	 * @throws Exception
	 *             更新有异常时发生
	 */
	public void updateRoleOrganization(String roleId, String orgIds, int userId)
			throws Exception;

	/**
	 * 更新角色关联的人员
	 * 
	 * @param roleId
	 *            角色id
	 * @param personIds
	 *            人员id，以","分隔
	 * @param userId
	 *            操作用户id
	 * @throws Exception
	 *             更新有异常时发生
	 */
	public void updateRolePerson(String roleId, String personIds, int userId)
			throws Exception;

}
