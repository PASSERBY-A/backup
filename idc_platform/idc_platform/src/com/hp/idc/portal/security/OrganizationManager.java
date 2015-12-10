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

import com.hp.idc.portal.security.Cache;
import com.hp.idc.portal.security.Common;
import com.hp.idc.portal.util.DBUtil;

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
		return oInfo;
	}
	
	/**
	 * ������֯���ڴ�
	 * @throws SQLException
	 */
	public static void loadAllOrganization() throws SQLException{
		String sql = "select * from CAS_ORGANIZATION order by id";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<OrganizationInfo>() {
			public OrganizationInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				OrganizationInfo ret = getOrganizationInfoFromResultSet(rs);
				Cache.OrganizationMap.put(ret.getId(), ret);
				return ret;
			}
		});
	}
	
	/**
	 * ������֯��ϵ
	 * @throws SQLException
	 */
	public static void loadRelations() throws SQLException {
		String sql = "select * from CAS_USER_ORGANIZATION order by USERID";
		Cache.Relation_O_P.clear();
		Cache.Relation_P_O.clear();
		Cache.Relation_M_R.clear();
		Cache.Relation_M_P.clear();
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<OrganizationInfo>() {
			public OrganizationInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				
				
				String userId = rs.getString("USERID");
				String orgId = rs.getString("ORGANIZATIONID");
				String roleId = rs.getString("roleId");
				
				OrganizationInfo ret = null;
				if(orgId!=null&&!"".equals(orgId))
					try {
						ret = getOrganizationById(orgId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				List<String> p = Cache.Relation_O_P.get(orgId);
				if (p == null) {
					p = new ArrayList<String>();
				}
				p.add(userId);
				
				Cache.Relation_O_P.put(orgId, p);
				Cache.Relation_P_O.put(userId, orgId);
				
				//������ɫ-��Ա��Ӧ��ϵ
				if (roleId!=null && !roleId.equals("")) {
					Cache.Relation_M_R.put(orgId+Common.Split_Str+userId, roleId);
					
					List<String> m = Cache.Relation_M_P.get(orgId+Common.Split_Str+roleId);
					if (m == null) {
						m = new ArrayList<String>();
						Cache.Relation_M_P.put(orgId+Common.Split_Str+roleId, m);
					}
					m.add(userId);
				}
				return ret;
			}
		});
	}

	/**
	 * ����ID��ȡ��֯
	 * @param id ��֯ID
	 * @return
	 * @throws Exception
	 */
	public static OrganizationInfo getOrganizationById(String id)
			throws Exception {
		if (id == null || id.equals(""))
			return null;
		return Cache.OrganizationMap.get(id);
	}

	/**
	 * ��ȡ������֯
	 * @return
	 * @throws SQLException
	 */
	public static List<OrganizationInfo> getAllOrganization()
			throws SQLException {
		List<OrganizationInfo> ret = new ArrayList<OrganizationInfo>();
		ret.addAll(Cache.OrganizationMap.values());
		return ret;
	}

	/**
	 * ���ֻ���û����ڵ���֯
	 * @param userId �û�ID
	 * @return
	 * @throws SQLException
	 */
	public static OrganizationInfo getOrganizationOfPerson(String userId) throws SQLException {
		String organizationId = Cache.Relation_P_O.get(userId);
		if (organizationId == null || organizationId.equals(""))
			return null;
		return Cache.OrganizationMap.get(organizationId);
	}

	/**
	 * �����֯����������֯
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	public static List<OrganizationInfo> getOrganizationByParentId(String parentId) throws SQLException {
		List<OrganizationInfo> ret = new ArrayList<OrganizationInfo>();
		if (parentId == null || parentId.equals(""))
			parentId = "-1";
		Set<String> keys = Cache.OrganizationMap.keySet();
		for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
			OrganizationInfo wgInfo = Cache.OrganizationMap.get(ite.next());
			if (wgInfo.getParentId().equals(parentId)) {
				ret.add(wgInfo);
			}
		}
		return ret;
	}
	
	/**
	 * ͬ����֯����ӿ�
	 */
	public void syncCache() {
		syncOrganizations();
	}

	/**
	 * ͬ����֯���淽��
	 */
	private void syncOrganizations() {
		final Map<String, OrganizationInfo> m = new HashMap<String, OrganizationInfo>();
		String sql = "select * from CAS_ORGANIZATION order by id";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {}, new RowMapper<OrganizationInfo>() {
			public OrganizationInfo mapRow(ResultSet rs, int arg1) throws SQLException {
				OrganizationInfo ret = getOrganizationInfoFromResultSet(rs);
				m.put(ret.getId(), ret);
				return ret;
			}
		});
		Cache.OrganizationMap = null;
		Cache.OrganizationMap = m;
	}
	
	/**
	 * �ж���Ա�Ƿ���һ����֯��
	 * @param userId	��ԱID
	 * @param organization	��֯
	 * @param includeParent �Ƿ��������֯
	 * @return
	 * @throws Exception
	 */
	public boolean personIsInOrganization(String userId, OrganizationInfo organization, boolean includeParent) throws Exception {
		if (organization == null)
			return false;
		String o = Cache.Relation_P_O.get(userId);
		if (o.equals(organization.getId()))
			return true;
		if (includeParent){
			OrganizationInfo oi_ = getOrganizationOfPerson(userId);
			OrganizationInfo p = oi_;
			while (p!=null) {
				if (p.getId().equals(organization.getId()))
					return true;
				p = getOrganizationById(p.getParentId());
			}
		}
		return false;
	}
	
	/**
	 * �ж���Ա�Ƿ���һ����֯��
	 * @param userId �û�ID
	 * @param orgId	��֯ID
	 * @param includeParent �Ƿ��������֯
	 * @return
	 * @throws Exception 
	 */
	public boolean personIsInOrganization(String userId,String orgId,boolean includeParent) throws Exception{
		OrganizationInfo oi = getOrganizationById(orgId);
		return personIsInOrganization(userId,oi,includeParent);
	}
	
	/**
	 * ��ȡ��֯��·����
	 * @param org ��֯
	 * @return
	 * @throws Exception 
	 */
	public String getOrganizationTreePath(OrganizationInfo org) throws Exception{
		if(org == null)
			return "";
		OrganizationInfo p = org;
		String ret = "/";
		while (p!=null) {
			ret = "/"+p.getId()+ret;
			p = getOrganizationById(p.getParentId());
		}
		return ret;
	}
	
	/**
	 * ��ȡ��֯��·����
	 * @param orgId ��֯ID
	 * @return
	 * @throws Exception 
	 */
	public String getOrganizationTreePath(String orgId) throws Exception{
		OrganizationInfo oi = getOrganizationById(orgId);
		return  getOrganizationTreePath(oi);
	}
}
