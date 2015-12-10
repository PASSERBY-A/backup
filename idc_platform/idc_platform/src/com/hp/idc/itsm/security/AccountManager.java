package com.hp.idc.itsm.security;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;

/**
 * 表示账号管理类(目前未使用)
 * @author 梅园
 *
 */
public class AccountManager {

	/**
	 * 验证结果:成功
	 */
	public static int VALID_SUCCESS = 0;
	/**
	 * 验证结果:无此用户
	 */
	public static int VALID_NO_SUCH_USER = 1;
	/**
	 * 验证结果:密码错误
	 */
	public static int VALID_PASSWORD_ERROR = 2;
	/**
	 * 验证结果:密码过期
	 */
	public static int VALID_PASSWORD_EXPIRED = 3;
	/**
	 * 验证结果:用户已禁用
	 */
	public static int VALID_DISABLED = 4;
	
	/**
	 * 账号ID定义
	 */
	public static String ACCOUNT_ID = "login_user_id"; 
	
	/**
	 * 验证账号登陆过程
	 * @param user 用户名
	 * @param password 密码
	 * @see #VALID_SUCCESS
	 * @see #VALID_NO_SUCH_USER
	 * @see #VALID_PASSWORD_ERROR
	 * @see #VALID_PASSWORD_EXPIRED
	 * @see #VALID_DISABLED
	 * @return 登陆结果
	 * @throws SQLException
	 */
	public static int validAccount(String user, String password) throws SQLException {
		String sql = "select acc_password, acc_lastupdate, acc_flag from itsm_account where acc_user=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			ps.setString(1, user);
			rs = ps.executeQuery();
			if (rs.next()) {
				String pwd = rs.getString(1);
				if (rs.getInt(3) == 1)
					return VALID_DISABLED;
				if (pwd.equals(PasswordUtil.crypt(password)))
					return VALID_SUCCESS;
				return VALID_PASSWORD_ERROR;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return VALID_NO_SUCH_USER;
	}
	
	/**
	 * 添加账号
	 * @param user 用户名
	 * @param password 密码
	 * @param operName 操作人
	 * @throws SQLException
	 */
	public static void addAccount(String user, String password, String operName) throws SQLException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("itsm_account", operName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			u.setColumnData("acc_user", new ColumnData(
					ColumnData.TYPE_STRING, user));
			u.setColumnData("acc_password", new ColumnData(
					ColumnData.TYPE_STRING, PasswordUtil.crypt(password)));
			u.setColumnData("acc_flag", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(0)));
			u.setColumnData("acc_lastupdate", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(System.currentTimeMillis()))));
			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	/**
	 * 变更密码
	 * @param user 用户名
	 * @param password 密码
	 * @param operName 操作人
	 * @throws SQLException
	 */
	public static void updatePassword(String user, String password, String operName) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("itsm_account", operName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			u.setColumnData("acc_password", new ColumnData(
					ColumnData.TYPE_STRING, PasswordUtil.crypt(password)));
			u.setColumnData("acc_lastupdate", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(System.currentTimeMillis()))));
			ps = u.getSelectStatement("acc_user=?");
			ps.setString(1, user);
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	/**
	 * 禁用账号
	 * @param user 用户名
	 * @param operName 操作人
	 * @throws SQLException
	 */
	public static void disableAccount(String user, String operName) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("itsm_account", operName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			u.setColumnData("acc_flag", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(1)));
			u.setColumnData("acc_lastupdate", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(System.currentTimeMillis()))));
			ps = u.getSelectStatement("acc_user=?");
			ps.setString(1, user);
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
}
