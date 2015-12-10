package com.hp.idc.itsm.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;

/**
 * ITSM���ع����������
 * 
 * @author FluteD
 * 
 */
public class LocalgroupManager {

	public static void initLocalgroup() {
		Cache.Localgroup = new HashMap<String,LocalgroupInfo>();
		Cache.Relations_L_P = new HashMap<String,List<String>>();
		Cache.Relations_P_L = new HashMap<String,List<String>>();
		try {
			loadLocalgroup();
			loadLU();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void loadLocalgroup() throws SQLException {
		String sql = "select * from ITSM_CFG_LOCALGROUP";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		Map<String, LocalgroupInfo> m = new HashMap<String, LocalgroupInfo>();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				LocalgroupInfo lgInfo = new LocalgroupInfo();
				lgInfo.setId(rs.getString("ID"));
				lgInfo.setName(rs.getString("NAME"));
				lgInfo.setStatus(rs.getInt("STATUS"));
				lgInfo.setWfOid(rs.getString("WF_OID"));

				m.put(lgInfo.getId(), lgInfo);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Localgroup = m;
	}
	
	public static void reloadLocalgroup(String id) throws SQLException {
		String sql = "select * from ITSM_CFG_LOCALGROUP where ID=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				LocalgroupInfo lgInfo = new LocalgroupInfo();
				lgInfo.setId(rs.getString("ID"));
				lgInfo.setName(rs.getString("NAME"));
				lgInfo.setStatus(rs.getInt("STATUS"));
				lgInfo.setWfOid(rs.getString("WF_OID"));
				Cache.Localgroup.put(lgInfo.getId(), lgInfo);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	public static void loadLU() throws SQLException {
		String sql = "select * from ITSM_CFG_LOCALGROUP_USERS";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		Map<String, List<String>> m = new HashMap<String, List<String>>();
		Map<String, List<String>> m2 = new HashMap<String, List<String>>();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("USER_ID");
				String lgId = rs.getString("LG_ID");

				List<String> l = m.get(lgId);
				if (l == null) {
					l = new ArrayList<String>();
					m.put(lgId, l);
				}
				l.add(userId);
				
				List<String> l2 = m2.get(userId);
				if (l2 == null) {
					l2 = new ArrayList<String>();
					m2.put(userId, l2);
				}
				l2.add(lgId);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Relations_L_P = m;
		Cache.Relations_P_L = m2;
	}
	
	public static void reloadLU(String lgId) throws SQLException {
		String sql = "select * from ITSM_CFG_LOCALGROUP_USERS where LG_ID=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			ps.setString(1, lgId);
			rs = ps.executeQuery();
			List<String> pl = new ArrayList<String>();

			while (rs.next()) {
				String userId = rs.getString("USER_ID");
				pl.add(userId);
				
				List<String> l2 = Cache.Relations_P_L.get(userId);
				if (l2 == null) {
					l2 = new ArrayList<String>();
					Cache.Relations_P_L.put(userId, l2);
				}
				l2.add(lgId);
			}
			Cache.Relations_L_P.put(lgId, pl);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	/**
	 * 
	 * @param includeAll
	 *            �Ƿ����ɾ���
	 * @return
	 */
	public static List<LocalgroupInfo> getAllLocalgroup(boolean includeAll) {
		List<LocalgroupInfo> ret = new ArrayList<LocalgroupInfo>();
		if (Cache.Localgroup.values() != null) {
			ret.addAll(Cache.Localgroup.values());
			if (!includeAll) {
				for (int i = 0; i < ret.size(); i++) {
					LocalgroupInfo lgInfo = ret.get(i);
					if (lgInfo.getStatus() == LocalgroupInfo.STATUS_DELETED) {
						ret.remove(i);
						i--;
					}
				}
			}
		}
		return ret;
	}

	public static LocalgroupInfo getGroupById(String id) {
		if (id == null || id.equals(""))
			return null;
		return Cache.Localgroup.get(id);
	}

	public static void updateLocalgroup(LocalgroupInfo lgInfo, String operName)
			throws SQLException {
		if (lgInfo == null)
			return;
		LocalgroupInfo wgInfo_ = getGroupById(lgInfo.getId());

		if (wgInfo_ == null) {
			throw new SQLException("�����鲻����id=" + lgInfo.getId());
		}

		ResultSet rs = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_LOCALGROUP", operName);
		PreparedStatement ps = null;
		try {
			u.setColumnData("name", new ColumnData('s', lgInfo.getName()));
			u.setColumnData("WF_OID", new ColumnData('s', lgInfo.getWfOid()));
			u.setColumnData("status", new ColumnData('i', new Integer(lgInfo
					.getStatus())));
			ps = u.getStatement("id=?");
			ps.setString(1, lgInfo.getId());
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Localgroup.put(lgInfo.getId(), lgInfo);
		
		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("LOCALGROUP", lgInfo.getId());
	}

	public static void addLocalgroup(LocalgroupInfo lgInfo, String operName)
			throws Exception {
		if (lgInfo == null)
			return;

		LocalgroupInfo _wInfo = getGroupById(lgInfo.getId());
		if (_wInfo != null)
			throw new Exception("id�����Ѵ���:" + lgInfo.getId());
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_LOCALGROUP", operName);
		try {
			u.setColumnData("id", new ColumnData('s', lgInfo.getId()
					.toLowerCase()));
			u.setColumnData("name", new ColumnData('s', lgInfo.getName()));
			u.setColumnData("WF_OID", new ColumnData('s', lgInfo.getWfOid()));
			u.setColumnData("status", new ColumnData('i', new Integer(lgInfo
					.getStatus())));
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.Localgroup.put(lgInfo.getId(), lgInfo);
		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("LOCALGROUP", lgInfo.getId());
	}

	/**
	 * ��ȡһ�����µ���Ա
	 * 
	 * @param lgId
	 * @return
	 */
	public static List<PersonInfo> getPersonsOfLocalgroup(String lgId,
			boolean includeAll) {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> pList = Cache.Relations_L_P.get(lgId);
		if (pList == null)
			return ret;
		for (int i = 0; i < pList.size(); i++) {
			String pId = pList.get(i);
			PersonInfo pi = Cache.Persons.get(pId);
			if (!includeAll && pi.getStatus() == PersonInfo.STATUS_DELETED)
				continue;
			if (pi != null)
				ret.add(pi);
		}
		return ret;
	}

	/**
	 * ���ӹ�����ϵ
	 * 
	 * @param lgId
	 * @param userId
	 * @param operName
	 * @throws SQLException
	 */
	public static void addRelation(String lgId, String userId, String operName)
			throws SQLException {

		ResultSet rs = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_LOCALGROUP_USERS",
				operName);
		try {
			u.setColumnData("LG_ID", new ColumnData('s', lgId));
			u.setColumnData("USER_ID", new ColumnData('s', userId));

			rs = u.getResultSet(null);
			u.executeInsert(rs);

		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}

		List<String> persons = Cache.Relations_L_P.get(lgId);
		if (persons == null) {
			persons = new ArrayList<String>();
			Cache.Relations_L_P.put(lgId, persons);
		}
		persons.add(userId);
		
		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("LOCALGROUP_R", lgId);
	}

	/**
	 * ɾ�������ϵ
	 * 
	 * @param lgId
	 * @param userId
	 * @param operName
	 * @throws SQLException
	 */
	public static void deleteRelation(String lgId, String userId,
			String operName) throws SQLException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_LOCALGROUP_USERS",
				operName);
		PreparedStatement ps = null;
		try {
			ps = u.getStatement("LG_ID=? and USER_ID=?");
			ps.setString(1, lgId);
			ps.setString(2, userId);
			rs = ps.executeQuery();
			u.executeDelete(rs);

		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		List<String> persons = Cache.Relations_L_P.get(lgId);
		if (persons != null) {
			for (int i = 0; i < persons.size(); i++) {
				String id = persons.get(i);
				if (id != null && id.equals(userId)) {
					persons.remove(i);
					break;
				}
			}
		}
		DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
		dsmc.publishEvent("LOCALGROUP_R", lgId);
	}

	/**
	 * ��ȡ�����±��ع�����
	 * 
	 * @param wfOid
	 * @return
	 */
	public static List<LocalgroupInfo> getGroupsOfWF(String wfOid,
			boolean includeAll) {
		List<LocalgroupInfo> ret = new ArrayList<LocalgroupInfo>();

		if (wfOid == null || wfOid.equals(""))
			return ret;
		Set<String> keys = Cache.Localgroup.keySet();
		for (Iterator<String> ite = keys.iterator(); ite.hasNext();) {
			String key = ite.next();
			LocalgroupInfo lgInfo = Cache.Localgroup.get(key);
			if (!includeAll
					&& lgInfo.getStatus() == LocalgroupInfo.STATUS_DELETED)
				continue;
			if ("-1".equals(wfOid) || lgInfo.getWfOid().equals("-1")
					|| lgInfo.getWfOid().equals(wfOid)
					|| lgInfo.getWfOid().startsWith(wfOid + ",")
					|| lgInfo.getWfOid().endsWith("," + wfOid)
					|| lgInfo.getWfOid().indexOf("," + wfOid + ",") != -1) {
				ret.add(lgInfo);
			}
		}
		return ret;
	}

	/**
	 * ��ȡ��Ա�µı��ع�����
	 * 
	 * @param personId
	 * @param includeAll �Ƿ����ɾ���
	 * @return
	 */
	public static List<LocalgroupInfo> getGroupsOfPerson(String personId,
			boolean includeAll) {
		List<LocalgroupInfo> ret = new ArrayList<LocalgroupInfo>();
		if (personId == null || personId.equals(""))
			return ret;
		List<String> lgs_ = new ArrayList<String>();
		if (Cache.Relations_P_L.get(personId)!=null)
			lgs_ = Cache.Relations_P_L.get(personId);
		for (int i = 0; i < lgs_.size(); i++) {
			String lg = lgs_.get(i);
			LocalgroupInfo lgInfo = Cache.Localgroup.get(lg);
			if (lgInfo!=null) {
				if (!includeAll && lgInfo.getStatus() == LocalgroupInfo.STATUS_DELETED)
					continue;
				ret.add(lgInfo);
			}
		}
		return ret;
	}

}
