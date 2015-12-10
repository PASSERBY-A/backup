package com.hp.idc.itsm.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectManager {
	
	protected static String jdbcUrl = null;

	protected static String jdbcLogin = null;

	protected static String jdbcPassword = null;
	
	private static DataSource dataSource;

	public static void setJdbcMode(String url, String login, String password) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		} catch (Exception e) {
		}
		jdbcUrl = url;
		if (jdbcUrl != null) {
			if (!jdbcUrl.startsWith("jdbc:oracle:thin:@"))
				jdbcUrl = "jdbc:oracle:thin:@" + jdbcUrl;
		}
		jdbcLogin = login;
		jdbcPassword = password;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * 
	 * @param str connect pool str if it equals NULL, then use the default value
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
