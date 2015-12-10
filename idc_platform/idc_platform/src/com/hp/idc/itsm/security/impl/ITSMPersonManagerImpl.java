package com.hp.idc.itsm.security.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.PersonManagerInterface;
import com.hp.idc.itsm.security.PersonInfo;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONObject;

public class ITSMPersonManagerImpl implements PersonManagerInterface {

	
	public void initCache(){

		loadPersons();
		//加载工作组人员关系
		loadWP();
		//加载组织人员关系
		loadOP();
	}
	
	public void registerClass(){
		PersonManager.classInsab.put("ITSM", this.getClass().getName());
		PersonManager.classIns.put("ITSM", this);
	}
	
	private static PersonInfo getPersonInfoFromResultSet(ResultSet rs) throws SQLException{
		PersonInfo pi = new PersonInfo();
		pi.setId(rs.getString("id"));
		pi.setName(rs.getString("name"));
		pi.setMobile(rs.getString("mobile"));
		pi.setEmail(rs.getString("email"));
		pi.setPassword(rs.getString("password"));
		pi.setStatus(rs.getInt("status"));
		pi.setPersonStatus(rs.getInt("p_status")+"");
		return pi;
	}
	
	public void loadWP() {
		Map m = new HashMap();
		Map<String,String> m2 = new HashMap<String,String>();
		Map<String,List<String>> m3 = new HashMap<String,List<String>>();
		String sql =  "select uw.* from cas_user_workgroup uw";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				String userid = rs.getString("userid");
				String workgroupid = rs.getString("workgroupid");
				String roleId = rs.getString("roleid");
				List<String> p = null;
				if (m.get("P_"+userid)!=null){
					p = (List)m.get("P_"+userid);
				} else {
					p = new ArrayList<String>();
					m.put("P_"+userid, p);
				}
				p.add(workgroupid);
				
				List<String> w = (List)m.get("W_"+workgroupid);
				if (w == null){
					w = new ArrayList<String>();
					m.put("W_"+workgroupid, w);
				}
				w.add(userid);
				
				if (roleId!=null && !roleId.equals("")) {
					m2.put(workgroupid+"_"+userid, roleId);
					List<String> pList = m3.get(workgroupid+"_"+roleId);
					if (pList == null) {
						pList = new ArrayList<String>();
						m3.put(workgroupid+"_"+roleId, pList);
					}
					pList.add(userid);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				u.commit(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Cache.Relations_W_P = null;
		Cache.Relations_W_P = m;

		Cache.Relations_M_R = m2;
		Cache.Relations_M_P = m3;
	}
	
	public void loadOP() {
		Map m = new HashMap();
		Map<String,String> m2 = new HashMap<String,String>();
		if (Cache.Relations_M_R.size()>0)
			m2.putAll(Cache.Relations_M_R);
		Map<String,List<String>> m3 = new HashMap<String,List<String>>();
		if (Cache.Relations_M_P.size()>0)
			m3.putAll(Cache.Relations_M_P);
		String sql =  "select uo.* from cas_user_organization uo";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				String userid = rs.getString("userid");
				String organizationid = rs.getString("organizationid");
				String roleId = rs.getString("roleid");
				List<String> p = null;
				if (m.get("P_"+userid)!=null){
					p = (List)m.get("P_"+userid);
				} else {
					p = new ArrayList<String>();
					m.put("P_"+userid, p);
				}
				p.add(organizationid);
				
				List<String> w = null;
				if (m.get("O_"+organizationid)!=null){
					w = (List)m.get("O_"+organizationid);
				} else {
					w = new ArrayList<String>();
					m.put("O_"+organizationid, w);
				}
				w.add(userid);
				
				if (roleId!=null && !roleId.equals("")) {
					m2.put(organizationid+"_"+userid, roleId);
					List<String> pList = m3.get(organizationid+"_"+roleId);
					if (pList == null) {
						pList = new ArrayList<String>();
						m3.put(organizationid+"_"+roleId, pList);
					}
					pList.add(userid);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				u.commit(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Cache.Relations_O_P= null;
		Cache.Relations_O_P = m;
		Cache.Relations_M_R = m2;
		Cache.Relations_M_P = m3;
	}
	
	public void loadPersons() {
		Map m = new HashMap();
		String sql =  "select * from cas_user order by name";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				PersonInfo pInfo = getPersonInfoFromResultSet(rs);
				m.put(pInfo.getId(), pInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				u.commit(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Cache.Persons = null;
		Cache.Persons = m;
	}
	/**
	 * 根据id获取人员信息
	 * @param id 人员id
	 * @return 返回找到的人员对象
	 */
	public PersonInfoInterface getPersonById(String id) {
		Object obj = Cache.Persons.get(id);
		if (obj != null)
			return (PersonInfo)obj;
		return null;
	}
	/**
	 * 根据id获取人员名称
	 * @param id 人员id
	 * @return 返回找到的人员名称,找不到的返回原id参数
	 */
	public String getPersonNameById(String id) {
		PersonInfoInterface info = getPersonById(id);
		if (info == null)
			return id;
		return info.getName();
	}
	
	/**
	 * 获取所有人员信息
	 * @return 返回所有人员信息List<PersonInfo>
	 */
	public List getAllPersons() {
		return new ArrayList(Cache.Persons.values());
	}
	

	/**
	 * 重置密码
	 * @param userId 用户id
	 * @param newPasswd 新密码
	 * @return 成功时返回null, 失败返回错误信息
	 */
	public String resetPassword(String userId, 
			String newPasswd) {
		//TODO
		return "";
	}

	/**
	 * 更新用户密码
	 * @param userId 用户密码
	 * @param oldPasswd 原密码
	 * @param newPasswd 新密码
	 * @return 成功时返回null, 失败返回错误信息
	 */
	public String changePassword(String userId, String oldPasswd,
			String newPasswd) {
		//TODO
		return "";
	}

	/**
	 * 用户登录
	 * @param userId 用户id
	 * @param passwd 密码
	 * @return 成功返回null, 失败返回错误信息
	 */
	public String login(String userId, String passwd) {
		//TODO
		return null;
	}
	
	public List getPersonsByWorkgoupId(String workgroupId, boolean includeChildren) {
		List ret = new ArrayList();
		Object o = Cache.Relations_W_P.get("W_"+workgroupId);
		if (o!=null){
			List ps = (List)o;
			for (int i = 0; i < ps.size(); i++) {
				Object p = Cache.Persons.get(ps.get(i));
				if (p!=null)
					ret.add(p);
			}
		}
		return ret;
	}
	
	public List getPersonsByOrganizationId(String organizationId, boolean includeChildren) {
		List ret = new ArrayList();
		Object o = Cache.Relations_O_P.get("O_"+organizationId);
		if (o!=null){
			List ps = (List)o;
			for (int i = 0; i < ps.size(); i++) {
				Object p = Cache.Persons.get(ps.get(i));
				if (p!=null)
					ret.add(p);
			}
		}
		return ret;
	}

	public String getLocalId(String id) {
		return id;
	}

	public String getRemoteId(String id) {
		return id;
	}

	public JSONArray getPersonsBySQL(String sql) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		JSONArray retJA = new JSONArray();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				JSONObject jo = new JSONObject();
				ResultSetMetaData rmd = rs.getMetaData();
				int colCount = rmd.getColumnCount();
				for (int i = 1; i <= colCount; i++) {
					String colName = rmd.getColumnName(i);
					jo.put(colName.toUpperCase(), rs.getString(colName));
				}
				retJA.put(jo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				u.commit(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retJA;
	}
}
