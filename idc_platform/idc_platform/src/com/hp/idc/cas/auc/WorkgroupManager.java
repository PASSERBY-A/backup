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
public class WorkgroupManager {

	public WorkgroupManager() {
	}

	private static WorkgroupInfo getWorkgroupInfoFromResultSet(ResultSet rs)
			throws SQLException {
		WorkgroupInfo wgInfo = new WorkgroupInfo();
		wgInfo.setId(rs.getString("id"));
		wgInfo.setName(rs.getString("name"));
		String p = rs.getString("PARENTID");
		if (p == null || p.equals(""))
			p = "-1";
		wgInfo.setParentId(p);
		wgInfo.setStatus(rs.getInt("status"));
		return wgInfo;
	}

	public static void loadAllWorkgroups() throws SQLException {
		String sql = "select * from cas_workgroup order by id";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				WorkgroupInfo wgInfo = getWorkgroupInfoFromResultSet(rs);
				Cache.WorkgroupMap.put(wgInfo.getId(), wgInfo);
			}
		} finally {
			u.commit(rs);
		}
	}

	public static void loadRelations() throws SQLException {
		String sql = "select * from CAS_USER_WORKGROUP order by USERID";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("USERID");
				String wgId = rs.getString("WORKGROUPID");
				String roleId = rs.getString("roleId");

				List<String> p = Cache.Relation_W_P.get(wgId);
				if (p == null) {
					p = new ArrayList<String>();
					Cache.Relation_W_P.put(wgId, p);
				}
				p.add(userId);

				List<String> w = Cache.Relation_P_W.get(userId);
				if (w == null) {
					w = new ArrayList<String>();
					Cache.Relation_P_W.put(userId, w);
				}
				w.add(wgId);
				
				//构建角色-人员对应关系
				if (roleId!=null && !roleId.equals("")) {
					Cache.Relation_M_R.put(wgId+Common.Split_Str+userId, roleId);
					
					List<String> m = Cache.Relation_M_P.get(wgId+Common.Split_Str+roleId);
					if (m == null) {
						m = new ArrayList<String>();
						Cache.Relation_M_P.put(wgId+Common.Split_Str+roleId, m);
					}
					m.add(userId);
				}

			}
		} finally {
			u.commit(rs);
		}
	}

	public static WorkgroupInfo getWorkgroupById(String id) throws Exception {
		if (id == null || id.equals(""))
			return null;
		return Cache.WorkgroupMap.get(id);
	}

	public static List<WorkgroupInfo> getAllWorkgroup(boolean includeAll)
			throws SQLException {
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		ret.addAll(Cache.WorkgroupMap.values());
		if (!includeAll) {
			for (int i = 0; i < ret.size(); i++) {
				WorkgroupInfo wi = ret.get(i);
				if (wi.getStatus() == WorkgroupInfo.STATUS_DELETE) {
					ret.remove(i);
					i--;
				}
			}
		}
		return ret;
	}

	public static void addWorkgroup(WorkgroupInfo wInfo, String operaName,
			String operIp) throws Exception {
		if (wInfo == null)
			return;
		if (wInfo.getId().equalsIgnoreCase(wInfo.getParentId()))
			throw new Exception("父对象不能是自己");
		WorkgroupInfo _wInfo = getWorkgroupById(wInfo.getId());
		if (_wInfo != null)
			throw new Exception("id对象已存在:" + wInfo.getId());
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("cas_workgroup", operaName);
		try {
			u.setColumnData("id", new ColumnData('s', wInfo.getId()
					.toLowerCase()));
			u.setColumnData("name", new ColumnData('s', wInfo.getName()));
			u.setColumnData("parentid",
					new ColumnData('s', wInfo.getParentId()));
			u.setColumnData("status", new ColumnData('i', new Integer(wInfo
					.getStatus())));
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_ADD, wInfo
					.getId(), wInfo.getName(), "对象新建");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Cache.WorkgroupMap.put(wInfo.getId(), wInfo);
		
		//sync other group
		CacheSync.loadGroupCache();
		
		return;
	}

	public static void modifyWorkgroup(WorkgroupInfo wInfo, String operaName,
			String operIp) throws Exception {
		WorkgroupInfo wgInfo_ = null;
		if (wInfo == null)
			return;
		if (wInfo.getId().equalsIgnoreCase(wInfo.getParentId()))
			throw new Exception("父对象不能是自己");
		try {
			wgInfo_ = getWorkgroupById(wInfo.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (wgInfo_ == null) {
			throw new SQLException("工作组不存在id=" + wInfo.getId());
		}

		ResultSet rs = null;
		OracleOperation u = new OracleOperation("cas_workgroup", operaName);
		PreparedStatement ps = null;
		try {
			u.setColumnData("name", new ColumnData('s', wInfo.getName()));
			u.setColumnData("parentid",
					new ColumnData('s', wInfo.getParentId()));
			u.setColumnData("status", new ColumnData('i', new Integer(wInfo
					.getStatus())));
			ps = u.getStatement("id=?");
			ps.setString(1, wInfo.getId());
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}

		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_UPDATE,
					wInfo.getId(), wInfo.getName(), wgInfo_
							.getDiffrenceStr(wInfo));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Cache.WorkgroupMap.put(wInfo.getId(), wInfo);
		
		//sync other group
		CacheSync.loadGroupCache();
		
		return;
	}

	public static void addRelations(String userId, String workgroupId,String roleId,
			String operName, String operIp) throws Exception {
		if (userId == null || userId.equals(""))
			return;
		if (workgroupId == null || workgroupId.equals(""))
			return;

		WorkgroupInfo wgInfo_ = null;
		PersonInfo pInfo_ = null;
		try {
			wgInfo_ = getWorkgroupById(workgroupId);
			pInfo_ = PersonManager.getPersonById(userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (wgInfo_ == null) {
			throw new Exception("工作组不存在id=" + workgroupId);
		}
		if (pInfo_ == null) {
			throw new Exception("人员对象不存在id=" + userId);
		}

		ResultSet rs = null;
		OracleOperation u = new OracleOperation("CAS_USER_WORKGROUP", operName);
		try {
			u.setColumnData("userid", new ColumnData('s', userId));
			u.setColumnData("workgroupid", new ColumnData('s', workgroupId));
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
					workgroupId, wgInfo_.getName(), "增加成员：" + userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List<String> p = Cache.Relation_W_P.get(workgroupId);
		if (p == null) {
			p = new ArrayList<String>();
			Cache.Relation_W_P.put(workgroupId, p);
		}
		p.add(userId);

		List<String> w = Cache.Relation_P_W.get(userId);
		if (w == null) {
			w = new ArrayList<String>();
			Cache.Relation_P_W.put(userId, w);
		}
		w.add(workgroupId);
		
		if (roleId!=null && !roleId.equals("")) {
			Cache.Relation_M_R.put(workgroupId+Common.Split_Str+userId, roleId);
			
			List<String> m = Cache.Relation_M_P.get(workgroupId+Common.Split_Str+roleId);
			if (m == null) {
				m = new ArrayList<String>();
				Cache.Relation_M_P.put(workgroupId+Common.Split_Str+roleId, m);
			}
			m.add(userId);
		}
		
		//sync other group person
		CacheSync.loadGroupRelationCache();
		
		return;
	}

	public static void addRelations(String userId[], String workgroupId,String roleId,
			String operName, String operIp) {
		if (userId == null || userId.length <= 0)
			return;
		if (workgroupId == null || workgroupId.equals(""))
			return;
		for (int i = 0; i < userId.length; i++) {
			if (userId[i] != null && !userId.equals("")) {
				try {
					addRelations(userId[i], workgroupId,roleId, operName, operIp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void addRelations(String userId[], String workgroupId[],String roleId,
			String operName, String operIp) {
		if (workgroupId == null || workgroupId.length <= 0)
			return;
		if (userId == null || userId.length <= 0)
			return;
		for (int i = 0; i < workgroupId.length; i++)
			if (workgroupId[i] != null && !workgroupId.equals(""))
				try {
					addRelations(userId, workgroupId[i],roleId, operName, operIp);
				} catch (Exception e) {
					e.printStackTrace();
				}

	}

	public static void deleteRelations(String userId, String workgroupId,
			String operName, String operIp) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("CAS_USER_WORKGROUP", operName);
		try {
			ps = u.getStatement("lower(userid)=? and lower(workgroupid)=?");
			ps.setString(1, userId.toLowerCase());
			ps.setString(2, workgroupId.toLowerCase());
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		try {
			LogManager.addLog(operName, operIp, LogInfo.OPER_TYPE_DELETE,
					workgroupId, workgroupId, "删除关联关系：" + workgroupId + "与"
							+ userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		List<String> p = Cache.Relation_W_P.get(workgroupId);
		if (p != null) {
			p.remove(userId);
		}

		List<String> w = Cache.Relation_P_W.get(userId);
		if (w != null) {
			w.remove(workgroupId);
		}
		
		
		String roleId = Cache.Relation_M_R.get(workgroupId+Common.Split_Str+userId);
		Cache.Relation_M_R.remove(workgroupId+Common.Split_Str+userId);
		Cache.Relation_M_P.remove(workgroupId+Common.Split_Str+roleId);

		//sync other group person
		CacheSync.loadGroupRelationCache();
		
		return;
	}

	public static List<WorkgroupInfo> getWorkgroupsOfPerson(String userId,
			boolean includeAll) throws SQLException {
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		List<String> wgs = Cache.Relation_P_W.get(userId);
		if (wgs != null) {
			for (int i = 0; i < wgs.size(); i++) {
				String wgId = wgs.get(i);
				WorkgroupInfo wgInfo = Cache.WorkgroupMap.get(wgId);
				if (wgInfo != null) {
					if (!includeAll
							&& wgInfo.getStatus() == WorkgroupInfo.STATUS_DELETE)
						continue;
					ret.add(wgInfo);
				}
			}
		}
		return ret;
	}

	public static List<WorkgroupInfo> getWorkgroupsForAddByPerson(
			String userId, boolean includeAll) throws SQLException {
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		ret.addAll(Cache.WorkgroupMap.values());
		if (!includeAll) {
			for (int i = 0; i < ret.size(); i++) {
				WorkgroupInfo wi = ret.get(i);
				if (wi.getStatus() == WorkgroupInfo.STATUS_DELETE) {
					ret.remove(i);
					i--;
				}
			}
		}

		List<String> wgs = Cache.Relation_P_W.get(userId);
		if (wgs != null) {
			for (int i = 0; i < wgs.size(); i++) {
				WorkgroupInfo wgInfo = Cache.WorkgroupMap.get(wgs.get(i));
				if (wgInfo != null)
					ret.remove(wgInfo);
			}
		}
		return ret;
	}

	/**
	 * @deprecated
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	public static List<WorkgroupInfo> getWorkgroupByParentId(String parentId)
			throws SQLException {
		return getWorkgroupByParentId(parentId, true);
	}

	public static List<WorkgroupInfo> getWorkgroupByParentId(String parentId,
			boolean includeAll) throws SQLException {
		if (parentId == null || parentId.equals(""))
			parentId = "-1";
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		Set<String> keys = Cache.WorkgroupMap.keySet();
		for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
			WorkgroupInfo wgInfo = Cache.WorkgroupMap.get(ite.next());
			if (!includeAll
					&& wgInfo.getStatus() == WorkgroupInfo.STATUS_DELETE) {
				continue;
			}
			if (wgInfo.getParentId().equals(parentId)) {
				ret.add(wgInfo);
			}
		}
		return ret;
	}
}
