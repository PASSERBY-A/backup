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
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupManagerInterface;
import com.hp.idc.itsm.security.WorkgroupInfo;
import com.hp.idc.itsm.security.WorkgroupManager;

public class ITSMWorkgroupManagerImpl implements WorkgroupManagerInterface {
	
	public void initCache(){
		loadWorkgroups();
	}
	
	public void registerClass(){
		WorkgroupManager.classInsab.put("ITSM", this.getClass().getName());
		WorkgroupManager.classIns.put("ITSM", this);
	}
	
	private static WorkgroupInfo getWorkgroupInfoFromResultSet(ResultSet rs) throws SQLException{
		WorkgroupInfo wgInfo = new WorkgroupInfo();
		wgInfo.setId(rs.getString("id"));
		wgInfo.setName(rs.getString("name"));
		wgInfo.setParentId(rs.getString("PARENTID"));
		wgInfo.setStatus(rs.getInt("status"));
		return wgInfo;
	}
	
	public void loadWorkgroups() {
		Map m = new HashMap();
		String sql =  "select * from cas_workgroup order by id";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				WorkgroupInfo wgInfo = getWorkgroupInfoFromResultSet(rs);
				m.put(wgInfo.getId(), wgInfo);
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
		Cache.Workgroups = null;
		Cache.Workgroups = m;
		
	}

	/**
	 * 按组织ID查找工作组
	 * @param id 工作组ID
	 * @return 返回找到的工作组对象,找不到时返回null
	 */
	public WorkgroupInfoInterface getWorkgroupById(String id) {
		Object obj = Cache.Workgroups.get(id);
		if (obj != null)
			return (WorkgroupInfo)obj;
		return null;
	}
	
	/**
	 * 获取所有工作组
	 * @return 返回所有工作组List<OrganizationInfo>
	 */
	public List getAllWorkgroups() {
		return new ArrayList(Cache.Workgroups.values());
	}

	public List getWorkgroupsByPersonId(String userId) {
		List ret = new ArrayList();
		Object o = Cache.Relations_W_P.get("P_"+userId);
		if (o!=null){
			List ps = (List)o;
			for (int i = 0; i < ps.size(); i++) {
				Object p = Cache.Workgroups.get(ps.get(i));
				if (p!=null)
					ret.add(p);
			}
		}
		return ret;
	}
	
	public boolean personIsInWorkgroup(String userId,
			WorkgroupInfoInterface workgroup, boolean includeParent){
		Object o = Cache.Relations_W_P.get("P_"+userId);
		if (o!=null && workgroup!=null){
			List ps = (List)o;
			for (int i = 0; i < ps.size(); i++) {
				String wgId = (String)ps.get(i);
				if (wgId.equals(workgroup.getId()))
					return true;
			}
		}

		return false;
	}

	public List<WorkgroupInfoInterface> getWorkgroupsByParentId(String id,boolean includeAll) {
		List<WorkgroupInfoInterface> ret = new ArrayList<WorkgroupInfoInterface>();
		Set s = Cache.Workgroups.keySet();
		for (Iterator ite = s.iterator(); ite.hasNext();){
			String key = (String)ite.next();
			WorkgroupInfo oii = (WorkgroupInfo)Cache.Workgroups.get(key);
			if (!includeAll && oii.getStatus() == oii.STATUS_DELETED)
				continue;
			if (oii.getParentId().equals(id)){
				ret.add(oii);
			}
		}
		return ret;
	}
}
