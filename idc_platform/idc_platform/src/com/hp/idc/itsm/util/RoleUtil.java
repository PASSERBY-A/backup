package com.hp.idc.itsm.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.idc.itsm.dbo.OracleOperation;

/**
 * 权限相关的类
 * @author 梅园
 */
public class RoleUtil {

	/**
	 * 判断指定的用户是否在指定的组内
	 * @param userId 用户的ID
	 * @param groupName 组名称
	 * @return 用户在组内时返回 true, 否则返回 false
	 * @throws SQLException 数据库操作异常时引发 SQLException 异常
	 */
	public static boolean isUserInGroup(String userId, String groupName) throws SQLException {
		OracleOperation u = new OracleOperation();
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean ret = false;
		try {
			String sql = "select count(*) from users_usergroups where usergroupid="
				 + "(select usergroupid from usergroup where name=?)"
				 + " and userid=?";
			ps = u.getSelectStatement(sql);
			ps.setString(1, groupName);
			ps.setString(2, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				int n = rs.getInt(1);
				if (n > 0)
					ret = true;
			}
			
		}
		catch (SQLException e) {
			
		}
		finally {
			u.commit(rs);
		}
		return ret;
	}

	/**
	 * 判断指定的用户具有指定的角色
	 * @param userId 用户的ID
	 * @param roleName 角色名称
	 * @return 用户有此角色内时返回 true, 否则返回 false
	 * @throws SQLException 数据库操作异常时引发 SQLException 异常
	 */
	public static boolean isUserHasRole(String userId, String roleName) throws SQLException {
		OracleOperation u = new OracleOperation();
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean ret = false;
		try {
			String sql = "select count(*) from users_roles where roleid="
				 + "(select roleid from role_ where name=?)"
				 + " and userid=?";
			ps = u.getSelectStatement(sql);
			ps.setString(1, roleName);
			ps.setString(2, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				int n = rs.getInt(1);
				if (n > 0)
					ret = true;
			}
			
		}
		catch (SQLException e) {
			
		}
		finally {
			u.commit(rs);
		}
		return ret;
	}
}
