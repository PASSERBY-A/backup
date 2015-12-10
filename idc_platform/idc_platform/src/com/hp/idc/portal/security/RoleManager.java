package com.hp.idc.portal.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.portal.util.DBUtil;

/**
 * 工作组/组织里，所要包含特殊角色，比如科长/副科长/组长，等等 此类就是管理这些信息的管理类
 * 
 * @author FluteD
 * 
 */
public class RoleManager {

	private static RoleInfo getWorkgroupInfoFromResultSet(ResultSet rs) throws SQLException{
		RoleInfo rInfo = new RoleInfo();
		rInfo.setId(rs.getString("id"));
		rInfo.setName(rs.getString("name"));
		rInfo.setLevel(rs.getInt("rlevel"));
		rInfo.setMoId(rs.getString("mo_id"));
		return rInfo;
	}
	
	
	public static void loadAllRoles() throws SQLException{
		String sql = "select * from CAS_ROLE";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<RoleInfo>() {
			public RoleInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				RoleInfo rInfo = getWorkgroupInfoFromResultSet(rs);
				Cache.RoleMap.put(rInfo.getMoId()+Common.Split_Str+rInfo.getId(), rInfo);
				return rInfo;
			}
		});
	}
	
	public static RoleInfo getRole(String moId,String roleId){
		RoleInfo ri = Cache.RoleMap.get(moId+Common.Split_Str+roleId);
		return ri;
	}
	
	/**
	 * 返回一个工作组/组织下，某一角色的人员列表
	 * @param moId 工作组/组织的ID
	 * @param roleId 角色ID
	 * @param includeAll 是否包含已删除的
	 * @return
	 */
	public static List<PersonInfo> getPersonsOfRole(String moId,String roleId,boolean includeAll){
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> rets = Cache.Relation_M_P.get(moId+Common.Split_Str+roleId);
		if (rets==null ||rets.size() == 0) 
			return ret;
		for (int i = 0; i < rets.size(); i++) {
			String pId = rets.get(i);
			PersonInfo pi = Cache.PersonMap.get(pId);
			if (pi != null) {
				ret.add(pi);
			}
		}
		return ret;
	}
	
	/**
	 * 获取一个工作组/组织下所有的角色定义
	 * @param moId
	 * @return
	 */
	public static List<RoleInfo> getRolesOfMo(String moId) {
		List<RoleInfo> ret = new ArrayList<RoleInfo>();
		Set<String> key = Cache.RoleMap.keySet();
		for (Iterator<String> ite = key.iterator(); ite.hasNext();) {
			String moId_ = ite.next();
			RoleInfo rInfo = Cache.RoleMap.get(moId_);
			if (moId_.startsWith(moId+Common.Split_Str))
				ret.add(rInfo);
		}
		Collections.sort(ret, new Comparator<RoleInfo>() {
			public int compare(final RoleInfo a, final RoleInfo b) {
				final String id1 = a.getLevel()+"";
				final String id2 = b.getLevel()+"";
				return id1.compareTo(id2);
			}
		});
		return ret;
	}
	
	/**
	 * 获取人员在一个组织/工作组里所属的角色
	 * @param moId
	 * @param userId
	 * @return
	 */
	public static RoleInfo getPersonRole(String moId,String userId) {
		String roleId = Cache.Relation_M_R.get(moId+Common.Split_Str+userId);
		if (roleId == null)
			return null;
		return Cache.RoleMap.get(moId+Common.Split_Str+roleId);
	}
	
	public static void syncRoles() throws SQLException{
		String sql = "select * from CAS_ROLE";
		final Map<String, RoleInfo> m = new HashMap<String, RoleInfo>();
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<RoleInfo>() {
			public RoleInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				RoleInfo rInfo = getWorkgroupInfoFromResultSet(rs);
				m.put(rInfo.getMoId()+Common.Split_Str+rInfo.getId(), rInfo);
				return rInfo;
			}
		});
		Cache.RoleMap=null;
		Cache.RoleMap=m;
		
	}
}
