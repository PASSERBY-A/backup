package com.hp.idc.portal.mgr;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.portal.security.*;
import com.hp.idc.portal.util.DBUtil;

public class TopData {

	/**
	 * 根据用户名获取当前用户具有浏览权限并且未过期的已发公告
	 * 
	 * @param userId
	 *            用户名
	 * @return
	 */
	public List<Map<String, String>> getNoticeByUserId(String userId) {
		StringBuffer sql = new StringBuffer("select * from portal_notice where rownum<6 ");
		// 判断权限
		String per = userId;
		OrganizationInfo orInfo = null;
		try {
			orInfo = OrganizationManager.getOrganizationOfPerson(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (orInfo != null)
			sql.append(" and (receiver = '" + per + "' " + "or receiver like '" + per + ",%' " + "or receiver like '%,"
					+ per + ",%' " + "or receiver like '%," + per + "' " + "or receiver = '" + orInfo.getId() + "' "
					+ "or receiver like '" + orInfo.getId() + ",%' " + "or receiver like '%," + orInfo.getId() + ",%' "
					+ "or receiver like '%," + orInfo.getId() + "')");
		else
			sql.append(" and (receiver = '" + per + "' " + "or receiver like '" + per + ",%' " + "or receiver like '%,"
					+ per + "')");
		sql.append(" order by pubtime desc");
		List<Map<String, String>> list = DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] {},
				new NoticeRowMapper());
		return list;
	}

	private class NoticeRowMapper implements RowMapper<Map<String, String>> {
		public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> temp = new HashMap<String, String>();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			//t.oid,t.title,t.content,t.receiver,t.validtime,t.pubtime
			temp.put("oid", rs.getString("oid"));
			temp.put("title", rs.getString("title"));
			temp.put("content", rs.getString("content"));
			temp.put("receiver", rs.getString("receiver"));
			temp.put("pubtime", sdf.format(rs.getTimestamp("pubtime")));
			temp.put("creator", rs.getString("creator"));
			return temp;
		}
	}
	
	/**
	 * 根据关键字模糊检索用户
	 * 
	 * @param keyWord
	 *            关键字
	 * @return
	 */
	public List<Map<String, String>> seachUserByKeyWord(String keyWord) {
		StringBuffer sql = new StringBuffer("select * from cas_user t where rownum<4 and t.id=? or t.name like '%"+keyWord+"%' or t.mobile=? ");
		List<Map<String, String>> list = DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] {keyWord,keyWord},
				new UserRowMapper());
		return list;
	}
	
	public static void getTodayLoginNum(){
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from cas_login_log t where t.LOGIN_TIME>sysdate-1");
		Cache.todayLoginNum = DBUtil.getJdbcTemplate().queryForInt(sb.toString(), new Object[]{});
	}
	
	/**
	 * 首页短信发送方法
	 * @param user	短信接收人
	 * @param content	短信内容
	 * @param module	模块名
	 * @return
	 */
	public int sendSms(String user,String content,String module){
		final String sql = "{call p_send_sms(?,?,?,?,?,?)}"; 
        Object[] params = new Object[] { user, content,module,null,null,null };
        return DBUtil.getJdbcTemplate().update(sql,params);
	}
	
	private class UserRowMapper implements RowMapper<Map<String, String>> {
		public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> temp = new HashMap<String, String>();
			temp.put("oid", rs.getString("oid"));
			temp.put("id", rs.getString("id"));
			temp.put("name", rs.getString("name"));
			temp.put("mobile", rs.getString("mobile"));
			temp.put("email", rs.getString("email"));
			return temp;
		}
	}
}
