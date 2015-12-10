/**
 * 
 */
package com.hp.idc.resm.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.BasicPermissionCache;
import com.hp.idc.resm.cache.RoleCache;
import com.hp.idc.resm.cache.RolePermissionCache;
import com.hp.idc.resm.cache.RolePermissionCacheStore;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.security.AuthInfo;
import com.hp.idc.resm.security.BasicPermission;
import com.hp.idc.resm.security.Organization;
import com.hp.idc.resm.security.Person;
import com.hp.idc.resm.security.Role;
import com.hp.idc.resm.security.RolePermission;
import com.hp.idc.resm.security.UserRole;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.LogUtil;
import com.hp.idc.resm.util.RoleUtil;
import com.hp.idc.resm.util.StringUtil;
import com.hp.idc.unitvelog.Log;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CachedRoleService implements IRoleService {

	/**
	 * ����Ȩ�޻���
	 */
	public static BasicPermissionCache basicPermissionCache = null;

	/**
	 * ��ɫ���建��
	 */
	public static RoleCache roleCache = null;

	/**
	 * ��ɫȨ�޻���
	 */
	public static RolePermissionCache rolePermissionCache = null;

	/**
	 * ��ʼ��
	 * 
	 * @throws Exception
	 *             ��ʼ�����쳣ʱ����
	 */
	public void init() throws Exception {
		basicPermissionCache = new BasicPermissionCache();
		basicPermissionCache.initCache();

		roleCache = new RoleCache();
		roleCache.initCache();

		rolePermissionCache = new RolePermissionCache();
		rolePermissionCache.initCache();
	}

	public List<BasicPermission> getAllPermissions() {
		return basicPermissionCache.getAll();
	}
	
	public BasicPermission getPermissionById(String id) {
		return basicPermissionCache.get(id);
	}

	public RolePermission getRolePermissionById(int id) {
		return rolePermissionCache.get("" + id);
	}

	public List<Role> getAllRoles() {
		return roleCache.getAll();
	}

	public void updateRolePerson(String roleId, String personIds, int userId)
			throws Exception {
		String[] ids = personIds.split(",");
		Role r = this.getRoleById(roleId);
		if (r == null)
			throw new Exception("�Ҳ�����ɫ" + roleId);
		r = (Role) r.clone();
		List<Integer> idList = new ArrayList<Integer>();
		for (String id : ids) {
			int n = StringUtil.parseInt(id, -1);
			if (n <= 0)
				continue;
			ResourceObject res = ServiceManager.getResourceService()
					.getResourceById(n);
			if (!(res instanceof Person))
				continue;
			idList.add(n);
		}
		int[] persons = new int[idList.size()];
		for (int i = 0; i < idList.size(); i++)
			persons[i] = idList.get(i).intValue();
		r.setPersons(persons);
		updateRole(r, userId);
	}

	public void updateRoleOrganization(String roleId, String orgIds, int userId)
			throws Exception {
		String[] ids = orgIds.split(",");
		Role r = this.getRoleById(roleId);
		if (r == null)
			throw new Exception("�Ҳ�����ɫ" + roleId);
		r = (Role) r.clone();
		List<Integer> idList = new ArrayList<Integer>();
		for (String id : ids) {
			int n = StringUtil.parseInt(id, -1);
			if (n <= 0)
				continue;
			ResourceObject res = ServiceManager.getResourceService()
					.getResourceById(n);
			if (!(res instanceof Organization))
				continue;
			idList.add(n);
		}
		int[] orgs = new int[idList.size()];
		for (int i = 0; i < idList.size(); i++)
			orgs[i] = idList.get(i).intValue();
		r.setOrganizations(orgs);
		updateRole(r, userId);
	}

	public void updateRole(Role role, int userId) throws Exception {
		if (!hasRole(userId, Role.ROLE_ADMIN))
			throw new Exception("ֻ���ڹ���Ա��Ľ�ɫ���в���Ȩ��");

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ROLE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(role.getId());
		uniLog.setObjectName(role.getName());
		uniLog.setOperator(userId);
		uniLog.setExtendInfo(role.getLogExtendInfo());
		if (roleCache.get(role.getId()) == null) {
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("������ɫ");
		} else {
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("���½�ɫ");
		}
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			role.syncToDatabase(conn);
			conn.commit();
			roleCache.add(role);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		LogUtil.log(uniLog);
	}

	public void deleteRole(String roleId, int userId) throws Exception {
		if (!hasRole(userId, Role.ROLE_ADMIN))
			throw new Exception("ֻ���ڹ���Ա��Ľ�ɫ���в���Ȩ��");
		if (roleId.equals(Role.ROLE_ADMIN))
			throw new Exception("����Ա��ɫ����ɾ��");
		if (roleId.equals(Role.ROLE_USER))
			throw new Exception("��ͨ�û���ɫ����ɾ��");
		Role role = roleCache.get(roleId);
		if (role == null)
			throw new Exception("û�������ɫ");
		if (getRolePermissions(roleId).size() > 0)
			throw new Exception("����ɾ����ɫ���е�Ȩ�ޣ��ٽ��в���");

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ROLE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(role.getId());
		uniLog.setObjectName(role.getName());
		uniLog.setOperator(userId);
		uniLog.setExtendInfo(role.getLogExtendInfo());
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("ɾ����ɫ");
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			role.removeFromDatabase(conn);
			conn.commit();
			roleCache.remove(role);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		LogUtil.log(uniLog);
	}

	public List<RolePermission> getRolePermissions(String roleId) {
		Role role = roleCache.get(roleId);
		if (role == null)
			return new ArrayList<RolePermission>();
		RolePermissionCacheStore store = (RolePermissionCacheStore) rolePermissionCache
				.getCacheStore();
		int p = role.getPriority() << 16;
		return store.priorityIndex.getGLE(p, p | 0xFFFF);
	}

	public void addRolePermission(RolePermission rp, int userId)
			throws Exception {
		if (!hasRole(userId, Role.ROLE_ADMIN))
			throw new Exception("ֻ���ڹ���Ա��Ľ�ɫ���в���Ȩ��");

		Role role = roleCache.get(rp.getRoleId());
		if (role == null)
			throw new Exception("û�������ɫ");

		List<RolePermission> oldList = getRolePermissions(rp.getRoleId());
		for (RolePermission ro : oldList) {
			if (ro.getPermId().equals(rp.getPermId())
					&& ro.getId() != rp.getId()) {
				throw new Exception("��ɫȨ���ظ������Ҫ�޸����ݣ���ʹ�ñ༭����");
			}
		}

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ROLEPERMISSION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + rp.getId());
		uniLog.setObjectName(role.getName());
		uniLog.setOperator(userId);
		if (rp.getId() <= 0) {
			rp.setId(DbUtil.getSequence(RolePermission.class.getName()));
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("������ɫȨ��");
		} else {
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("���½�ɫȨ��");
		}
		uniLog.setExtendInfo(rp.getLogExtendInfo());

		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			rp.syncToDatabase(conn);
			conn.commit();
			rolePermissionCache.add(rp);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		LogUtil.log(uniLog);
	}

	public void deleteRolePermission(int id, int userId) throws Exception {
		if (!hasRole(userId, Role.ROLE_ADMIN))
			throw new Exception("ֻ���ڹ���Ա��Ľ�ɫ���в���Ȩ��");

		RolePermission rp = getRolePermissionById(id);
		if (rp == null)
			throw new Exception("û�������ɫȨ��");

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ROLEPERMISSION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + rp.getId());
		uniLog.setObjectName(rp.getRoleId());
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("ɾ����ɫȨ��");
		uniLog.setExtendInfo(rp.getLogExtendInfo());

		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			rp.removeFromDatabase(conn);
			conn.commit();
			rolePermissionCache.remove(rp);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		LogUtil.log(uniLog);
	}

	public boolean hasRole(int userId, String roleId) {
		int type = checkRole(userId, roleId);
		// System.out.println("checkRole(" + userId + "," + roleId + ")=" +
		// type);
		return type != Role.AUTH_NONE;
	}

	public int checkRole(int userId, String roleId) {
		if (Role.ROLE_ADMIN.equals(roleId)) {
			if (RoleUtil.isSystemAdmin(userId))
				return Role.AUTH_BY_PERSON;
		}

		Role role = roleCache.get(roleId);
		if (role == null)
			return Role.AUTH_NONE;
		ResourceObject r = ServiceManager.getResourceService().getResourceById(
				userId);
		if (r == null || !(r instanceof Person))
			return Role.AUTH_NONE;
		Person p = (Person) r;
		if (role.hasUser(userId))
			return Role.AUTH_BY_PERSON;

		Organization org = p.getOrganization();
		if (role.hasOrganization(org))
			return Role.AUTH_BY_ORGANIZATION;
		return Role.AUTH_NONE;
	}

	public Role getRoleById(String roleId) {
		return roleCache.get(roleId);
	}

	public void removeUserRole(int userId, String roleId, int opUserId)
			throws Exception {
		Role role = this.getRoleById(roleId);
		if (role == null)
			throw new Exception("�Ƿ��Ľ�ɫid:" + roleId);
		if (!role.hasUser(userId)) // �������
			return;
		role = (Role) role.clone();
		role.removePerson(userId);
		this.updateRole(role, opUserId);
	}

	public UserRole getUserRole(int userId) throws Exception {
		ResourceObject r = ServiceManager.getResourceService().getResourceById(
				userId);
		if (r == null || !(r instanceof Person))
			throw new Exception("�Ƿ����û�id:" + userId);
		UserRole ur = new UserRole();
		ur.setUser((Person) r);
		List<RolePermission> rpList = new ArrayList<RolePermission>();
		List<AuthInfo> authInfos = new ArrayList<AuthInfo>();
		ur.setAuthInfos(authInfos);
		ur.setPermissions(rpList);

		RolePermissionCacheStore store = (RolePermissionCacheStore) rolePermissionCache
				.getCacheStore();
		List<RolePermission> all = store.priorityIndex.values();
		if (all.size() == 0)
			return ur;
		String curId = "";
		AuthInfo info = null;
		for (int i = 0; i < all.size(); i++) {
			RolePermission rp = all.get(i);
			if (!rp.getRoleId().equals(curId)) {
				curId = rp.getRoleId();
				int type = checkRole(userId, curId);
				// System.out.println("checkRole(" + userId + "," + curId + ")="
				// + type);
				if (type == Role.AUTH_NONE) {
					int j = i + 1;
					for (; j < all.size(); j++)
						if (!all.get(j).getRoleId().equals(curId))
							break;
					i = j - 1;
					continue;
				}
				if (info != null)
					info.setEndIndex(rpList.size() - 1);
				info = new AuthInfo();
				info.setStartIndex(rpList.size());
				info.setRole(getRoleById(curId));
				info.setAuthType(type);
				authInfos.add(info);
			}
			rpList.add(rp);
		}
		if (info != null)
			info.setEndIndex(rpList.size() - 1);
		return ur;
	}

	public List<RolePermission> getUserPermissions(int userId, String permId) {
		List<RolePermission> rpList = new ArrayList<RolePermission>();
		RolePermissionCacheStore store = (RolePermissionCacheStore) rolePermissionCache
				.getCacheStore();
		List<RolePermission> all = store.priorityIndex.values();
		String curId = "";
		for (int i = 0; i < all.size(); i++) {
			RolePermission rp = all.get(i);
			if (!rp.getRoleId().equals(curId)) {
				curId = rp.getRoleId();
				int type = checkRole(userId, curId);
				if (type == Role.AUTH_NONE) {
					int j = i + 1;
					for (; j < all.size(); j++)
						if (!all.get(j).getRoleId().equals(curId))
							break;
					i = j - 1;
					continue;
				}
			}
			if (rp.getPermId().equals(permId))
				rpList.add(rp);
		}
		return rpList;
	}

	public boolean checkUserPermission(int userId, String permId, Object param) {
		if (RoleUtil.isSystemAdmin(userId)) // ϵͳ����Ա��һ��Ȩ��
			return true;
		List<RolePermission> list = getUserPermissions(userId, permId);
		return checkUserPermission(userId, list, param);
	}
	
	public boolean checkUserPermission(int userId, List<RolePermission> list, Object param) {
		if (RoleUtil.isSystemAdmin(userId)) // ϵͳ����Ա��һ��Ȩ��
			return true;
		for (RolePermission rp : list) {
			int r = BasicPermission.PERM_DENY;
			try {
				r = rp.getPermisson().valid(userId, param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (r == BasicPermission.PERM_DENY)
				return false;
			if (r == BasicPermission.PERM_ALLOW)
				return true;
		}
		return false;
	}

	public List<Role> getRolesByUser(int userId) {
		List<Role> list = getAllRoles();
		for (int i = 0; i < list.size(); i++) {
			if (!hasRole(userId, list.get(i).getId())) {
				list.remove(i);
				i--;
			}
		}
		return list;
	}
}
