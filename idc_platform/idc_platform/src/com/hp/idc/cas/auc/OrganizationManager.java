package com.hp.idc.cas.auc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.idc.cas.Cache;
import com.hp.idc.cas.Common;
import com.hp.idc.cas.dbo.ColumnData;
import com.hp.idc.cas.dbo.OracleOperation;
import com.hp.idc.cas.log.LogInfo;
import com.hp.idc.cas.log.LogManager;
import com.hp.idc.common.CacheSync;

/**
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class OrganizationManager {

	public OrganizationManager() {
	}

	private static OrganizationInfo getOrganizationInfoFromResultSet(
			ResultSet rs) throws SQLException {
		OrganizationInfo oInfo = new OrganizationInfo();
		oInfo.setId(rs.getString("id"));
		oInfo.setName(rs.getString("name"));
		String p = rs.getString("PARENTID");
		if (p == null || p.equals(""))
			p = "-1";
		oInfo.setParentId(p);
		oInfo.setStatus(rs.getInt("status"));
		return oInfo;
	}

	public static void loadAllOrganization() throws SQLException {
		String sql = "select * from CAS_ORGANIZATION order by id";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				OrganizationInfo oInfo = getOrganizationInfoFromResultSet(rs);
				Cache.OrganizationMap.put(oInfo.getId(), oInfo);
			}
		} finally {
			u.commit(rs);
		}
	}

	public static void loadRelations() throws SQLException {
		String sql = "select * from CAS_USER_ORGANIZATION order by USERID";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("USERID");
				String orgId = rs.getString("ORGANIZATIONID");
				String roleId = rs.getString("roleId");

				List<String> p = Cache.Relation_O_P.get(orgId);
				if (p == null) {
					p = new ArrayList<String>();
					Cache.Relation_O_P.put(orgId, p);
				}
				p.add(userId);

				Cache.Relation_P_O.put(userId, orgId);

				// 构建角色-人员对应关系
				if (roleId != null && !roleId.equals("")) {
					Cache.Relation_M_R.put(orgId + Common.Split_Str + userId,
							roleId);

					List<String> m = Cache.Relation_M_P.get(orgId
							+ Common.Split_Str + roleId);
					if (m == null) {
						m = new ArrayList<String>();
						Cache.Relation_M_P.put(orgId + Common.Split_Str
								+ roleId, m);
					}
					m.add(userId);
				}

			}
		} finally {
			u.commit(rs);
		}
	}

	public static OrganizationInfo getOrganizationById(String id)
			throws Exception {
		if (id == null || id.equals(""))
			return null;
		return Cache.OrganizationMap.get(id);
	}

	public static List<OrganizationInfo> getAllOrganization(boolean includeAll)
			throws SQLException {
		List<OrganizationInfo> ret = new ArrayList<OrganizationInfo>();
		ret.addAll(Cache.OrganizationMap.values());
		if (!includeAll) {
			for (int i = 0; i < ret.size(); i++) {
				OrganizationInfo oInfo = ret.get(i);
				if (oInfo.getStatus() == OrganizationInfo.STATUS_DELETE) {
					ret.remove(i);
					i--;
				}
			}
		}
		return ret;
	}

	public static void addOrganization(OrganizationInfo oInfo,
			String operaName, String operIp) throws Exception {
		if (oInfo == null)
			return;
		if (oInfo.getId().equalsIgnoreCase(oInfo.getParentId()))
			throw new Exception("父对象不能是自己");
		OrganizationInfo _oInfo = getOrganizationById(oInfo.getId());
		if (_oInfo != null)
			throw new Exception("id对象已存在:" + oInfo.getId());
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("CAS_ORGANIZATION", operaName);
		try {
			u.setColumnData("id", new ColumnData('s', oInfo.getId()
					.toLowerCase()));
			u.setColumnData("name", new ColumnData('s', oInfo.getName()));
			u.setColumnData("parentid",
					new ColumnData('s', oInfo.getParentId()));
			u.setColumnData("status", new ColumnData('i', new Integer(oInfo
					.getStatus())));
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_ADD, oInfo
					.getId(), oInfo.getName(), "对象新建");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Cache.OrganizationMap.put(oInfo.getId(), oInfo);
		
		//sync other org
		CacheSync.loadOrgCache();
		
		return;
	}

	public static void modifyOrganization(OrganizationInfo oInfo,
			String operaName, String operIp) throws Exception {
		if (oInfo == null)
			return;
		if (oInfo.getId().equalsIgnoreCase(oInfo.getParentId()))
			throw new Exception("父对象不能是自己");

		OrganizationInfo oInfo_ = null;
		try {
			oInfo_ = getOrganizationById(oInfo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (oInfo_ == null) {
			throw new SQLException("组织对象不存在id=" + oInfo.getId());
		}
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("CAS_ORGANIZATION", operaName);
		PreparedStatement ps = null;
		try {
			u.setColumnData("name", new ColumnData('s', oInfo.getName()));
			u.setColumnData("parentid",
					new ColumnData('s', oInfo.getParentId()));
			u.setColumnData("status", new ColumnData('i', new Integer(oInfo
					.getStatus())));
			ps = u.getStatement("id=?");
			ps.setString(1, oInfo.getId());
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}

		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_UPDATE,
					oInfo.getId(), oInfo.getName(), oInfo_
							.getDiffrenceStr(oInfo));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Cache.OrganizationMap.put(oInfo.getId(), oInfo);
		
		//sync other org
		CacheSync.loadOrgCache();
		
		return;
	}

	public static void addRelations(String userId, String organizationId,
			String roleId, boolean update, String operName, String operIp)
			throws Exception {
		deleteRelations(userId, operName, operIp);
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("CAS_USER_ORGANIZATION",
				operName);
		try {
			u.setColumnData("userid", new ColumnData(ColumnData.TYPE_STRING,
					userId));
			u.setColumnData("organizationid", new ColumnData(
					ColumnData.TYPE_STRING, organizationId));
			u.setColumnData("roleid", new ColumnData('s', roleId));
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		try {
			LogManager.addLog(operName, operIp, LogInfo.OPER_TYPE_ADD,
					organizationId, organizationId, "增加成员：" + userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List<String> p = Cache.Relation_O_P.get(organizationId);
		if (p == null) {
			p = new ArrayList<String>();
			Cache.Relation_O_P.put(organizationId, p);
		}
		p.add(userId);

		Cache.Relation_P_O.put(userId, organizationId);

		if (roleId != null && !roleId.equals("")) {
			Cache.Relation_M_R.put(organizationId + Common.Split_Str + userId,
					roleId);

			List<String> m = Cache.Relation_M_P.get(organizationId
					+ Common.Split_Str + roleId);
			if (m == null) {
				m = new ArrayList<String>();
				Cache.Relation_M_P.put(organizationId + Common.Split_Str
						+ roleId, m);
			}
			m.add(userId);
		}

		//sync other org person
		CacheSync.loadOrgRelationCache();
		
		return;
	}

	public static void addRelations(String userId[], String organizationId,
			String roleId, boolean update, String operName, String operIp)
			throws Exception {
		if (userId == null || userId.length <= 0)
			return;
		for (int i = 0; i < userId.length; i++)
			if (userId[i] != null && !userId.equals(""))
				try {
					addRelations(userId[i], organizationId, roleId, update,
							operName, operIp);
				} catch (Exception e) {
					throw e;
				}

	}

	public static void deleteRelations(String userId, String operName,
			String operIp) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("CAS_USER_ORGANIZATION",
				operName);
		try {
			ps = u.getStatement("userid=? ");
			ps.setString(1, userId);
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		try {
			LogManager.addLog(operName, operIp, LogInfo.OPER_TYPE_DELETE,
					userId, userId, "从组织中移除");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String organizationId = Cache.Relation_P_O.get(userId);
		List<String> p = Cache.Relation_O_P.get(organizationId);
		if (p != null) {
			p.remove(userId);
		}

		Cache.Relation_P_O.remove(userId);

		String roleId = Cache.Relation_M_R.get(organizationId
				+ Common.Split_Str + userId);
		Cache.Relation_M_R.remove(organizationId + Common.Split_Str + userId);
		Cache.Relation_M_P.remove(organizationId + Common.Split_Str + roleId);
		
		//sync other org person
		CacheSync.loadOrgRelationCache();
		
		return;
	}

	public static OrganizationInfo getOrganizationOfPerson(String userId,
			boolean includeAll) throws SQLException {
		String organizationId = Cache.Relation_P_O.get(userId);
		if (organizationId == null || organizationId.equals(""))
			return null;
		return Cache.OrganizationMap.get(organizationId);
	}

	public static List<OrganizationInfo> getOrganizationForAddByPerson(
			String userId, boolean includeAll) throws SQLException {
		List<OrganizationInfo> ret = new ArrayList<OrganizationInfo>();
		ret.addAll(Cache.OrganizationMap.values());
		if (!includeAll) {
			for (int i = 0; i < ret.size(); i++) {
				OrganizationInfo oInfo = ret.get(i);
				if (oInfo.getStatus() == OrganizationInfo.STATUS_DELETE) {
					ret.remove(i);
					i--;
				}
			}
		}
		String oId = Cache.Relation_P_O.get(userId);
		OrganizationInfo oi = Cache.OrganizationMap.get(oId);
		if (oi != null)
			ret.remove(oi);
		return ret;
	}

	/**
	 * @deprecated
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	public static List<OrganizationInfo> getOrganizationByParentId(
			String parentId) throws SQLException {
		return getOrganizationByParentId(parentId, true);
	}

	public static List<OrganizationInfo> getOrganizationByParentId(
			String parentId, boolean includeAll) throws SQLException {
		List<OrganizationInfo> ret = new ArrayList<OrganizationInfo>();
		if (parentId == null || parentId.equals(""))
			parentId = "-1";
		Set<String> keys = Cache.OrganizationMap.keySet();
		for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
			OrganizationInfo wgInfo = Cache.OrganizationMap.get(ite.next());
			if (!includeAll
					&& wgInfo.getStatus() == OrganizationInfo.STATUS_DELETE) {
				continue;
			}
			if (wgInfo.getParentId().equals(parentId)) {
				ret.add(wgInfo);
			}
		}
		return ret;
	}
}
