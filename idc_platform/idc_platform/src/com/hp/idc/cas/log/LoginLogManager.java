package com.hp.idc.cas.log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.cas.dbo.OracleOperation;

public class LoginLogManager {
	
	public static String currentUser;
	
	public static List<LoginLogInfo> getLoginLog() throws SQLException{
		List<LoginLogInfo> ret = new ArrayList<LoginLogInfo>();
		String sql = "select * from cas_login_log order by LOGIN_TIME desc";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				LoginLogInfo llInfo = new LoginLogInfo();
				llInfo.setUser_id(rs.getString("USER_ID"));
				llInfo.setLogin_time(sdf.format(rs.getTimestamp("LOGIN_TIME")));
				llInfo.setLogin_ip(rs.getString("CLIENT_IP"));
				llInfo.setLogin_host(rs.getString("CLIENT_HOST"));
				ret.add(llInfo);
			}
		} finally {
			u.commit(rs);
		}
		return ret;
	}
	
	public static List<LoginLogInfo> getLoginLog(String begin,String end) throws SQLException{
		List<LoginLogInfo> ret = new ArrayList<LoginLogInfo>();
		String sql = "select * from cas_login_log where LOGIN_TIME>=to_date(?,'yyyy-MM-dd HH24:mi:ss') and LOGIN_TIME<=to_date(?,'yyyy-MM-dd HH24:mi:ss') order by LOGIN_TIME desc";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			ps = u.getSelectStatement(sql);
			ps.setString(1, begin);
			ps.setString(2, end);
			rs = ps.executeQuery();
			while (rs.next()){
				LoginLogInfo llInfo = new LoginLogInfo();
				llInfo.setUser_id(rs.getString("USER_ID"));
				llInfo.setLogin_time(sdf.format(rs.getTimestamp("LOGIN_TIME")));
				llInfo.setLogin_ip(rs.getString("CLIENT_IP"));
				llInfo.setLogin_host(rs.getString("CLIENT_HOST"));
				ret.add(llInfo);
			}
		} finally {
			u.commit(rs);
		}
		return ret;
	}
	
	public static void addLoginLog(String userId, String ip, String host) throws SQLException{
		LoginLogInfo llInfo = new LoginLogInfo();
		llInfo.setUser_id(userId);
		llInfo.setLogin_ip(ip);
		llInfo.setLogin_host(host);
		
		LoginLogManager.currentUser = userId;
		
		String sql = "insert into cas_login_log (user_id, login_time, client_ip, client_host) values (?, ?, ?, ?)";
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			ps.setString(1, llInfo.getUser_id());
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setString(3, llInfo.getLogin_ip());
			ps.setString(4, llInfo.getLogin_host());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			u.commit(null);
		}
	}

}
