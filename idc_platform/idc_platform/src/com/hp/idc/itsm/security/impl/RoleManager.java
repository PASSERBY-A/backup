package com.hp.idc.itsm.security.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.RoleInfo;


public class RoleManager {

	public static void loadRoles() throws SQLException{
		Map<String,RoleInfo> m = new HashMap<String,RoleInfo>();
		String sql = "select * from CAS_ROLE";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				RoleInfo ri = new RoleInfo();
				ri.setId(rs.getString("id"));
				ri.setName(rs.getString("name"));
				ri.setMoId(rs.getString("mo_id"));
				ri.setLevel(rs.getInt("rlevel"));
				ri.setStatus(rs.getInt("status"));
				m.put(ri.getMoId()+"_"+ri.getId()+"", ri);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Roles = m;
	}
	
	/**
	 * 返回一个组（组织/工作组）下某一个角色的对象信息
	 * @param moId
	 * @param roleId
	 * @return
	 */
	public static RoleInfo getRoleById(String moId,String roleId){
		return Cache.Roles.get(moId+"_"+roleId);
	}
	
	/**
	 * 获取一个人在一个组下的角色
	 * @param moId
	 * @param pId
	 * @return
	 */
	public static RoleInfo getRoleOfPerson(String moId,String pId) {
		String rId = Cache.Relations_M_R.get(moId+"_"+pId);
		if (rId == null || rId.equals(""))
			return null;
		return Cache.Roles.get(moId+"_"+rId);
	}
	
	/**
	 * 获取一个组下属于一种角色的人员
	 * @param moId
	 * @param rId
	 * @return
	 */
	public static List<PersonInfo> getPersonsOfRole(String moId,String rId){
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> pList = Cache.Relations_M_P.get(moId+"_"+rId);
		if (pList ==null)
			return ret;
		for (int i = 0; i < pList.size(); i++) {
			String pId = pList.get(i);
			PersonInfo pi = Cache.Persons.get(pId);
			if (pi != null)
				ret.add(pi);
		}
		return ret;
	}
}
