package com.hp.idc.portal.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.portal.util.DBUtil;

/**
 * 人员管理类
 * 
 * @author FluteD <a mailto:lihz@lianchuang.com>FluteD</a>
 * 
 */
public class PersonManager {

	public PersonManager() {
	}

	private static PersonInfo getPersonInfoFromResultSet(ResultSet rs) throws SQLException {
		PersonInfo pi = new PersonInfo();
		pi.setOid(rs.getInt("OID"));
		pi.setId(rs.getString("id"));
		pi.setName(rs.getString("name"));
		pi.setMobile(rs.getString("mobile"));
		pi.setEmail(rs.getString("email"));
		return pi;
	}

	/**
	 * 加载所有人员进缓存
	 * 
	 * @throws SQLException
	 */
	public static void loadAllPersons() throws SQLException {
		String sql = "select * from cas_user order by id";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<PersonInfo>() {
			public PersonInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				PersonInfo ret = getPersonInfoFromResultSet(rs);
				Cache.PersonMap.put(ret.getId().toUpperCase(), ret);
				Cache.PersonMap1.put(ret.getOid(), ret);
				return ret;
			}

		});
	}

	public static PersonInfo getPersonById(String id) {
		if (id == null || id.equals(""))
			return null;
		PersonInfo pi = null;
		pi = Cache.PersonMap.get(id.toUpperCase());
		
		if (pi == null && id.matches("\\d*")) {
			pi = Cache.PersonMap1.get(Integer.valueOf(id));
		}
		
		return pi;
	}
	
	public static String getPersonNameById(String id) {
		
		if (id == null || id.equals(""))
			return null;
		PersonInfo pi = null;
		pi = Cache.PersonMap.get(id.toUpperCase());
		
		if (pi == null && id.matches("\\d*")) {
			pi = Cache.PersonMap1.get(Integer.valueOf(id));
		}
		if(pi != null)
			return pi.getName();
		return id;
	}

	/**
	 * 获取所有人员列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getAllPersons() throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		ret.addAll(Cache.PersonMap.values());
		return ret;
	}

	/**
	 * 获取工作组下的所有人员
	 * 
	 * @param workgroupId
	 *            工作组ID
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsOfWorkgroup(String workgroupId) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> ps = Cache.Relation_W_P.get(workgroupId);
		if (ps != null) {
			for (int i = 0; i < ps.size(); i++) {
				String piId = ps.get(i).toUpperCase();
				PersonInfo pi = Cache.PersonMap.get(piId);
				ret.add(pi);
			}
		}
		return ret;
	}

	/**
	 * 获取组织下的所有人员
	 * 
	 * @param organizationId
	 *            组织ID
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsOfOrganization(String organizationId) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> ps = Cache.Relation_O_P.get(organizationId);
		if (ps != null) {
			for (int i = 0; i < ps.size(); i++) {
				String piId = ps.get(i).toUpperCase();
				PersonInfo pi = Cache.PersonMap.get(piId);
				ret.add(pi);
			}
		}
		return ret;
	}

	/**
	 * 获取待加入工作组的人员
	 * 
	 * @param workgroupId
	 *            工作组ID
	 * @param includeAll
	 *            是否包含状态为禁止的人员
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsForAddToWorkgroup(String workgroupId) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		ret.addAll(Cache.PersonMap.values());
		List<String> wgPersons = Cache.Relation_W_P.get(workgroupId);
		if (wgPersons != null) {
			for (int j = 0; j < wgPersons.size(); j++) {
				PersonInfo pi = Cache.PersonMap.get(wgPersons.get(j));
				if (pi != null) {
					ret.remove(pi);
				}
			}
		}
		return ret;
	}

	/**
	 * 返回待加入组织的人员列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsForAddToOrganization() throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		ret.addAll(Cache.PersonMap.values());

		for (int i = 0; i < ret.size(); i++) {
			PersonInfo pi = ret.get(i);
			if (Cache.Relation_P_O.get(pi.getId()) != null) {
				ret.remove(i);
				i--;
			}
		}
		return ret;
	}

	public static List<PersonInfo> getPersonsBySQL(String sql) throws SQLException {
		List<PersonInfo> ret = DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<PersonInfo>() {
			public PersonInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				PersonInfo ret = getPersonInfoFromResultSet(rs);
				Cache.PersonMap.put(ret.getId().toUpperCase(), ret);
				return ret;
			}

		});
		return ret;
	}
	
	public void syncCache() throws Exception{
		syncPersons();
		//同步工作组人员关系
		syncWP();
		//同步组织人员关系
		syncOP();
	}
	
	/**
	 * 同步缓存中的
	 * 
	 * @throws SQLException
	 */
	public void syncPersons() throws SQLException {
		loadAllPersons();
	}
	
	private void syncWP() {
		final Map<String, List<String>> m = new HashMap<String, List<String>>();
		final Map<String, List<String>> m1 = new HashMap<String, List<String>>();
		final Map<String,String> m2 = new HashMap<String,String>();
		final Map<String,List<String>> m3 = new HashMap<String,List<String>>();
		String sql =  "select uw.* from cas_user_workgroup uw";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<WorkgroupInfo>() {
			public WorkgroupInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				
				String userId = rs.getString("USERID");
				String wgId = rs.getString("WORKGROUPID");
				String roleId = rs.getString("roleId");

				List<String> p = m.get(wgId);
				if (p == null) {
					p = new ArrayList<String>();
					m.put(wgId, p);
				}
				p.add(userId);

				List<String> w = m1.get(userId);
				if (w == null) {
					w = new ArrayList<String>();
					m1.put(userId, w);
				}
				w.add(wgId);
				
				//构建角色-人员对应关系
				if (roleId!=null && !roleId.equals("")) {
					m2.put(wgId+Common.Split_Str+userId, roleId);
					
					List<String> m = m3.get(wgId+Common.Split_Str+roleId);
					if (m == null) {
						m = new ArrayList<String>();
						m3.put(wgId+Common.Split_Str+roleId, m);
					}
					m.add(userId);
				}
				return null;
			}
		});
		Cache.Relation_W_P = null;
		Cache.Relation_W_P = m;
		Cache.Relation_P_W = null;
		Cache.Relation_P_W = m1;

		Cache.Relation_M_R = null;
		Cache.Relation_M_R = m2;
		Cache.Relation_M_P = null;
		Cache.Relation_M_P = m3;
	}
	
	private void syncOP() {
		final Map<String, List<String>> m = new HashMap<String, List<String>>();
		final Map<String, String> m1 = new HashMap<String, String>();
		final Map<String,String> m2 = new HashMap<String,String>();
		if (Cache.Relation_M_R.size()>0)
			m2.putAll(Cache.Relation_M_R);
		final Map<String,List<String>> m3 = new HashMap<String,List<String>>();
		if (Cache.Relation_M_P.size()>0)
			m3.putAll(Cache.Relation_M_P);
		String sql =  "select uo.* from cas_user_organization uo";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<OrganizationInfo>() {
			public OrganizationInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				String userId = rs.getString("USERID");
				String orgId = rs.getString("ORGANIZATIONID");
				String roleId = rs.getString("roleId");
				
				List<String> p = m.get(orgId);
				if (p == null) {
					p = new ArrayList<String>();
				}
				p.add(userId);
				m.put(orgId, p);
				m1.put(userId, orgId);
				
				//构建角色-人员对应关系
				if (roleId!=null && !roleId.equals("")) {
					m2.put(orgId+Common.Split_Str+userId, roleId);
					
					List<String> m = m3.get(orgId+Common.Split_Str+roleId);
					if (m == null) {
						m = new ArrayList<String>();
						m3.put(orgId+Common.Split_Str+roleId, m);
					}
					m.add(userId);
				}
				return null;
			}
		});
		Cache.Relation_O_P= null;
		Cache.Relation_O_P = m;
		Cache.Relation_P_O= null;
		Cache.Relation_P_O = m1;
		Cache.Relation_M_R = null;
		Cache.Relation_M_R = m2;
		Cache.Relation_M_P = null;
		Cache.Relation_M_P = m3;
	}
}
