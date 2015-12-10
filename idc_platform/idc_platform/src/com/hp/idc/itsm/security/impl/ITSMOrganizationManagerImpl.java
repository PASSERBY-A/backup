package com.hp.idc.itsm.security.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.OrganizationManagerInterface;
import com.hp.idc.itsm.security.OrganizationInfo;
import com.hp.idc.itsm.security.OrganizationManager;

public class ITSMOrganizationManagerImpl implements
		OrganizationManagerInterface {

	public void initCache() {
		loadOrganizations();
	}

	public void registerClass() {
		OrganizationManager.classInsab.put("ITSM", this.getClass().getName());
		OrganizationManager.classIns.put("ITSM", this);
	}

	private OrganizationInfo getOrganizationInfoFromResultSet(ResultSet rs)
			throws SQLException {
		OrganizationInfo oInfo = new OrganizationInfo();
		oInfo.setId(rs.getString("id"));
		oInfo.setName(rs.getString("name"));
		String pId = "";
		if (rs.getString("PARENTID") == null
				|| rs.getString("PARENTID").equals(""))
			pId = "-1";
		else
			pId = rs.getString("PARENTID");
		oInfo.setParentId(pId);
		oInfo.setStatus(rs.getInt("status"));
		return oInfo;
	}

	public void loadOrganizations() {
		Map m = new HashMap();
		String sql = "select * from CAS_ORGANIZATION order by id";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				OrganizationInfo oInfo = getOrganizationInfoFromResultSet(rs);
				m.put(oInfo.getId(), oInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				u.commit(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Cache.Organizations = null;
		Cache.Organizations = m;

	}

	/**
	 * 按组织ID查找组织
	 * 
	 * @param id
	 *            组织ID
	 * @return 返回找到的组织对象,找不到时返回null
	 */
	public OrganizationInfoInterface getOrganizationById(String id) {
		Object o = Cache.Organizations.get(id);
		if (o != null)
			return (OrganizationInfo) o;
		return null;
	}

	/**
	 * 获取所有组织
	 * 
	 * @return 返回所有组织List<OrganizationInfo>
	 */
	public List getAllOrganizations() {
		return new ArrayList(Cache.Organizations.values());
	}

	public OrganizationInfoInterface getOrganizationByPersonId(String userId) {
		Object o = Cache.Relations_O_P.get("P_" + userId);
		OrganizationInfo ret = null;
		if (o != null) {
			List ps = (List) o;
			if (ps.size() > 0)
				ret = (OrganizationInfo) Cache.Organizations.get(ps.get(0));
		}
		return ret;
	}
	
	/**
	 * 获取组织的路径树
	 * @param orgId
	 * @return
	 */
	public String getOrganizationTreePath(String orgId){
		OrganizationInfoInterface oi = getOrganizationById(orgId);
		return  getOrganizationTreePath(oi);
	}
	
	public String getOrganizationTreePath(OrganizationInfoInterface org){
		if(org == null)
			return "";
		OrganizationInfoInterface p = org;
		String ret = "/";
		while (p!=null) {
			ret = "/"+p.getId()+ret;
			p = p.getParentInfo();
		}
		return ret;
	}

	public boolean personIsInOrganization(String userId,
			OrganizationInfoInterface organization, boolean includeParent) {
		if (organization == null)
			return false;
		Object o = Cache.Relations_O_P.get("P_" + userId);
		if (o != null && organization != null) {
			List ps = (List) o;
			if (ps.size() > 0) {
				String oId = (String) ps.get(0);
				if (oId.equals(organization.getId()))
					return true;
			}
		}
		if (includeParent){
			OrganizationInfoInterface oi_ = getOrganizationByPersonId(userId);
			OrganizationInfoInterface p = oi_;
			while (p!=null) {
				if (p.getId().equals(organization.getId()))
					return true;
				p = p.getParentInfo();
			}
		}
		return false;
	}
	
	public boolean personIsInOrganization(String userId,String orgId,boolean includeParent){
		OrganizationInfoInterface oi = this.getOrganizationById(orgId);
		return personIsInOrganization(userId,oi,includeParent);
	}
	
	/**
	 *	
	 * @return
	 */
	public String getOrganizationTreePathOfPerson(String userId){
		OrganizationInfoInterface oi = this.getOrganizationByPersonId(userId);
		return getOrganizationTreePath(oi);
	}

	/**
	 * 通过父ID查找组织
	 */
	public List<OrganizationInfoInterface> getOrganizationsByParentId(String id,boolean includeAll) {
		//if (orgaTree.get(id) != null)
		//	return orgaTree.get(id);
		List<OrganizationInfoInterface> ret = new ArrayList<OrganizationInfoInterface>();
		Set s = Cache.Organizations.keySet();
		for (Iterator ite = s.iterator(); ite.hasNext();) {
			String key = (String) ite.next();
			OrganizationInfo oii = (OrganizationInfo) Cache.Organizations
					.get(key);
			if (!includeAll && oii.getStatus() == oii.STATUS_DELETED)
				continue;
			if (oii.getParentId().equals(id)) {
				ret.add(oii);
			}
		}
		orgaTree.put(id, ret);
		return ret;
	}
}
