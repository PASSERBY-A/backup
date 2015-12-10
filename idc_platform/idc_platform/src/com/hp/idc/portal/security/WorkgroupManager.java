package com.hp.idc.portal.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.portal.util.DBUtil;

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
		return wgInfo;
	}

	public static void loadAllWorkgroups() throws SQLException {
		String sql = "select * from cas_workgroup order by id";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<WorkgroupInfo>() {
			public WorkgroupInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				WorkgroupInfo wgInfo = getWorkgroupInfoFromResultSet(rs);
				Cache.WorkgroupMap.put(wgInfo.getId(), wgInfo);
				return wgInfo;
			}
		});
	}

	public static void loadRelations() throws SQLException {
		String sql = "select * from CAS_USER_WORKGROUP order by USERID";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<WorkgroupInfo>() {
			public WorkgroupInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				
				String userId = rs.getString("USERID");
				String wgId = rs.getString("WORKGROUPID");
				WorkgroupInfo wgInfo=null;
				try {
					wgInfo = getWorkgroupById(wgId);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
				return wgInfo;
			}
		});
	}

	public static WorkgroupInfo getWorkgroupById(String id) throws Exception {
		if (id == null || id.equals(""))
			return null;
		return Cache.WorkgroupMap.get(id);
	}

	public static List<WorkgroupInfo> getAllWorkgroup()
			throws SQLException {
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		ret.addAll(Cache.WorkgroupMap.values());
		return ret;
	}

	public static List<WorkgroupInfo> getWorkgroupsOfPerson(String userId,boolean includeAll) throws SQLException {
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		List<String> wgs = Cache.Relation_P_W.get(userId);
		if (wgs != null) {
			for (int i = 0; i < wgs.size(); i++) {
				String wgId = wgs.get(i);
				WorkgroupInfo wgInfo = Cache.WorkgroupMap.get(wgId);
				if (wgInfo != null) {
					ret.add(wgInfo);
				}
			}
		}
		return ret;
	}

	public static List<WorkgroupInfo> getWorkgroupsForAddByPerson(String userId) throws SQLException {
		List<WorkgroupInfo> ret = new ArrayList<WorkgroupInfo>();
		ret.addAll(Cache.WorkgroupMap.values());

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
			if (wgInfo.getParentId().equals(parentId)) {
				ret.add(wgInfo);
			}
		}
		return ret;
	}
	
	
	/**
	 * 调用同步缓存数据动作
	 */
	public void syncCache(){
		WorkgroupSync();
	}
	
	/**
	 * 缓存数据同步
	 */
	private void WorkgroupSync() {
		final Map<String, WorkgroupInfo> m = new HashMap<String, WorkgroupInfo>();
		String sql =  "select * from cas_workgroup order by id";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<WorkgroupInfo>() {
			public WorkgroupInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				WorkgroupInfo wgInfo = getWorkgroupInfoFromResultSet(rs);
				m.put(wgInfo.getId(), wgInfo);
				return wgInfo;
			}
		});
		Cache.WorkgroupMap = null;
		Cache.WorkgroupMap = m;
		
	}
	
	/**
	 * 人员是否在组织
	 * @param userId
	 * @param workgroup
	 * @param includeParent
	 * @return
	 */
	public boolean personIsInWorkgroup(String userId, WorkgroupInfo workgroup, boolean includeParent){
		List<String> list = Cache.Relation_P_W.get(userId);
		if (list!=null && workgroup!=null){
			for (int i = 0; i < list.size(); i++) {
				String wgId = list.get(i);
				if (wgId.equals(workgroup.getId()))
					return true;
			}
		}
		return false;
	}
}
