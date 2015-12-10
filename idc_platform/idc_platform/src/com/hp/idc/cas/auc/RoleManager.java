package com.hp.idc.cas.auc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.idc.cas.Cache;
import com.hp.idc.cas.Common;
import com.hp.idc.cas.dbo.ColumnData;
import com.hp.idc.cas.dbo.OracleOperation;
import com.hp.idc.cas.log.LogInfo;
import com.hp.idc.cas.log.LogManager;

/**
 * 工作组/组织里，所要包含特殊角色，比如科长/副科长/组长，等等 此类就是管理这些信息的管理类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RoleManager {

	private static RoleInfo getWorkgroupInfoFromResultSet(ResultSet rs) throws SQLException{
		RoleInfo rInfo = new RoleInfo();
		rInfo.setId(rs.getString("id"));
		rInfo.setName(rs.getString("name"));
		rInfo.setLevel(rs.getInt("rlevel"));
		rInfo.setMoId(rs.getString("mo_id"));
		rInfo.setStatus(rs.getInt("status"));
		return rInfo;
	}
	
	
	public static void loadAllRoles() throws SQLException{
		String sql = "select * from CAS_ROLE";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				RoleInfo rInfo = getWorkgroupInfoFromResultSet(rs);
				Cache.RoleMap.put(rInfo.getMoId()+Common.Split_Str+rInfo.getId(), rInfo);
			}
		} finally {
			u.commit(rs);
		}
	}
	
	public static RoleInfo getRole(String moId,String roleId){
		RoleInfo ri = Cache.RoleMap.get(moId+Common.Split_Str+roleId);
		return ri;
	}
	
	public static void addRole(RoleInfo ri,String operaName, String operIp) throws Exception{

		RoleInfo ri_ = null;
		try {
			ri_ = getRole(ri.getMoId(),ri.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (ri_ != null)
			throw new Exception("角色已存在id=" + ri.getId());

		ResultSet rs = null;
		OracleOperation u = new OracleOperation("CAS_ROLE", operaName);
		try {
			u.setColumnData("id", new ColumnData('s', ri.getId()
					.toLowerCase()));
			u.setColumnData("name", new ColumnData('s', ri.getName()));
			u.setColumnData("MO_ID", new ColumnData('s', ri.getMoId()));
			u.setColumnData("RLEVEL", new ColumnData('i', ri.getLevel()));
			u.setColumnData("status", new ColumnData('i', new Integer(ri
					.getStatus())));
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}

		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_UPDATE,
					ri.getId(), ri.getName(), "对象新建");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Cache.RoleMap.put(ri.getMoId()+Common.Split_Str+ri.getId(), ri);
	}
	public static void updateRole(RoleInfo ri,String operaName, String operIp) throws Exception {

		RoleInfo ri_ = null;
		try {
			ri_ = getRole(ri.getMoId(),ri.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (ri_ == null)
			throw new Exception("角色对象不存在id=" + ri.getId());

		ResultSet rs = null;
		OracleOperation u = new OracleOperation("CAS_ROLE", operaName);
		PreparedStatement ps = null;
		try {
			u.setColumnData("name", new ColumnData('s', ri.getName()));
			u.setColumnData("MO_ID", new ColumnData('s', ri.getMoId()));
			u.setColumnData("RLEVEL", new ColumnData('i', ri.getLevel()));
			u.setColumnData("status", new ColumnData('i', new Integer(ri
					.getStatus())));
			ps = u.getStatement("id=?");
			ps.setString(1, ri.getId());
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}

		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_UPDATE,
					ri.getId(), ri.getName(),ri_
					.getDiffrenceStr(ri));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Cache.RoleMap.put(ri.getMoId()+Common.Split_Str+ri.getId(), ri);
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
				if (!includeAll
						&& pi.getStatus() == PersonInfo.STATUS_DELETE)
					continue;
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
}
