package com.hp.idc.itsm.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.idc.itsm.dbo.OracleOperation;

/**
 * Ȩ����ص���
 * @author ÷԰
 */
public class RoleUtil {

	/**
	 * �ж�ָ�����û��Ƿ���ָ��������
	 * @param userId �û���ID
	 * @param groupName ������
	 * @return �û�������ʱ���� true, ���򷵻� false
	 * @throws SQLException ���ݿ�����쳣ʱ���� SQLException �쳣
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
	 * �ж�ָ�����û�����ָ���Ľ�ɫ
	 * @param userId �û���ID
	 * @param roleName ��ɫ����
	 * @return �û��д˽�ɫ��ʱ���� true, ���򷵻� false
	 * @throws SQLException ���ݿ�����쳣ʱ���� SQLException �쳣
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
