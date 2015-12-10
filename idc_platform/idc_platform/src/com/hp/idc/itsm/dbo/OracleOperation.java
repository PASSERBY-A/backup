// meiy

package com.hp.idc.itsm.dbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import com.hp.idc.itsm.util.ConnectManager;

/**
 * Oracle 操作的封装类，可以自动记录操作日志
 * 
 * @author 梅园
 */
public class OracleOperation {

	/**
	 * JDBC url, 当需要使用 JDBC 模式时调用 setJdbcMode 设置
	 */
	protected String jdbcUrl = null;

	/**
	 * JDBC 用户名, 当需要使用 JDBC 模式时调用 setJdbcMode 设置
	 */
	protected String jdbcLogin = null;

	/**
	 * JDBC 密码, 当需要使用 JDBC 模式时调用 setJdbcMode 设置
	 */
	protected String jdbcPassword = null;

	/**
	 * 操作的表名
	 */
	protected String tableName;

	/**
	 * 要更新的数据，列名->ColumnData
	 */
	protected Map dataMap = new HashMap();

	/**
	 * 使用的数据库连接
	 */
	protected Connection currentConnection = null;

	/**
	 * 当前执行的语句
	 */
	PreparedStatement currentStatement = null;

	/**
	 * 操作员
	 */
	protected String operName;

	/**
	 * 改动的字段信息列表
	 */
	protected List modifyList = new ArrayList();

	protected List tempClobs = new ArrayList();
	protected List tempBlobs = new ArrayList();

	public void addTempClob(CLOB clob) {
		tempClobs.add(clob);
	}

	public void addTempBlob(BLOB blob) {
		tempClobs.add(blob);
	}

	public void freeTempLobs() {
		while (tempClobs.size() > 0) {
			CLOB clob = (CLOB) tempClobs.remove(0);
			try {
				CLOB.freeTemporary(clob);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		while (tempBlobs.size() > 0) {
			BLOB blob = (BLOB) tempBlobs.remove(0);
			try {
				BLOB.freeTemporary(blob);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 当使用 select 时用此构造函数
	 */
	public OracleOperation() {
		this.tableName = "?";
		this.operName = "system";
	}

	/**
	 * 使用指定的表名和操作员名称构造 OracleOperation 对象
	 * 
	 * @param tableName
	 *            操作的表名
	 * @param operName
	 *            操作员名称
	 */
	public OracleOperation(String tableName, String operName) {
		this.tableName = tableName;
		this.operName = operName;
		if (this.operName == null || this.operName.length() == 0)
			this.operName = "system";
	}

	/**
	 * 设置操作的表名
	 * 
	 * @param tableName
	 *            操作的表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Connection getCurrentConnection() {
		return currentConnection;
	}

	/**
	 * 设置指定列对应的更新或插入的数据
	 * 
	 * @param columnName
	 *            列名
	 * @param data
	 *            更新或插入的数据
	 */
	public void setColumnData(String columnName, ColumnData data) {
		dataMap.put(columnName.toLowerCase(), data);
	}

	/**
	 * 添加改动的信息，不要调用此函数
	 * 
	 * @param operType
	 *            操作类型，delete/insert/update
	 * @param columnName
	 *            列名
	 * @param fromValue
	 *            原始值
	 * @param toValue
	 *            新值
	 * @param content
	 *            主键值
	 */
	protected void addModify(String operType, String columnName,
			String fromValue, String toValue, String content) {
		modifyList.add(new ModifyInfo(operName, operType, tableName,
				columnName, fromValue, toValue, content));
	}

	/**
	 * 执行更新操作
	 * 
	 * @param rs
	 *            操作的数据集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public void executeUpdate(ResultSet rs) throws SQLException {
		Map map = getColumnTypes(rs);
		Object[] obs = dataMap.keySet().toArray();
		while (rs.next()) {
			updateResultSet(rs, "update", map, obs);
			rs.updateRow();
		}
	}

	/**
	 * 执行删除操作
	 * 
	 * @param rs
	 *            操作的数据集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public void executeDelete(ResultSet rs) throws SQLException {
		LogInfo info = LogManager.getLogInfo(tableName);
		if (info == null) {
			while (rs.next())
				rs.deleteRow();
			return;
		}
		List l = info.getDeleteList();
		String keyColumn = info.getKeyColumn();
		while (rs.next()) {
			String keyValue = "";
			if (!keyColumn.equals("-")) {
				try {
					keyValue = rs.getString(keyColumn);
				} catch (SQLException e) {
				}
			}
			for (int i = 0; i < l.size(); i++) {
				String col = (String) l.get(i);
				addModify("delete", col, rs.getString(col), "", keyValue);
			}
			rs.deleteRow();
		}
	}

	/**
	 * 执行插入操作
	 * 
	 * @param rs
	 *            操作的数据集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public void executeInsert(ResultSet rs) throws SQLException {
		rs.moveToInsertRow();
		Map map = getColumnTypes(rs);
		updateResultSet(rs, "insert", map, dataMap.keySet().toArray());
		rs.insertRow();
		rs.moveToCurrentRow();
	}

	/**
	 * 获取字段名->字段类型的对应表
	 * 
	 * @param rs
	 *            操作的数据集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	protected Map getColumnTypes(ResultSet rs) throws SQLException {
		Map map = new HashMap();
		ResultSetMetaData md = rs.getMetaData();
		int cols = md.getColumnCount();
		for (int i = 0; i < cols; i++) {
			map.put(md.getColumnName(i + 1).toLowerCase(), md
					.getColumnClassName(i + 1));
		}
		return map;
	}

	/**
	 * 
	 * @param rs
	 *            操作的数据集
	 * @param operType
	 *            操作类型，delete/insert/update
	 * @param map
	 *            字段名->字段类型的对应表
	 * @param keys
	 *            需要操作列名数组
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	protected void updateResultSet(ResultSet rs, String operType, Map map,
			Object[] keys) throws SQLException {
		LogInfo info = LogManager.getLogInfo(tableName);
		ColumnData keyData = null;
		String keyColumn = "";
		if (info != null) {
			keyColumn = info.getKeyColumn();
			keyData = (ColumnData) dataMap.get(keyColumn);
		}
		String keyValue = "";
		if (keyData != null)
			keyValue = keyData.getDisplayText();

		for (int i = 0; i < keys.length; i++) {
			ColumnData data = (ColumnData) dataMap.get(keys[i]);
			String columnName = (String) keys[i];
			if (info != null) {
				if (info.isLogable(columnName, operType)) {
					if (operType.equals("update")) {
						data.compareWith(rs, columnName, this, operType,
								keyColumn);
					} else if (operType.equals("insert")) {
						addModify(operType, columnName, "", data
								.getDisplayText(), keyValue);
					}
				}
			}
			data.update(this, currentConnection, columnName, rs, map);
		}
	}

	/**
	 * 获取序列的下一个值
	 * 
	 * @param seqName
	 *            序列名
	 * @return 返回序列的下一个值
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public static int getSequence(String seqName) throws SQLException {
		int returnId = -1;
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select " + seqName + ".nextval id from dual";
			rs = u.getSelectResultSet(sql);
			if (rs.next()) {
				returnId = rs.getInt("id");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return returnId;
	}

	/**
	 * 根据指定条件生成 SQL 语句
	 * 
	 * @param where
	 *            查询条件，null 表示不查找任何数据，"" 表示查找所有数据
	 * @return 返回包含指定条件的 SQL 语句
	 */
	protected String getSql(String where) {
		if (where == null)
			return "select t.* from " + tableName + " t where 2 < 1";
		else if (where.length() == 0)
			return "select t.* from " + tableName + " t";
		return "select t.* from " + tableName + " t where " + where;
	}

	/**
	 * 获取要操作的结果集
	 * 
	 * @param where
	 *            为null时取空集合，""为所有数据，否则为查询条件
	 * @return 查询结果集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public ResultSet getResultSet(String where) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(getSql(where),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return currentStatement.executeQuery();
	}

	/**
	 * 获取要操作的命令，取得后可绑定参数，然后通过executeQuery()方法取结果集
	 * 
	 * @param where
	 *            为null时取空集合，""为所有数据，否则为查询条件
	 * @return PreparedStatement 对象
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public PreparedStatement getStatement(String where) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(getSql(where),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return currentStatement;
	}

	/**
	 * 获取要操作的结果集
	 * 
	 * @param sql
	 *            SQL 语句
	 * @return 查询结果集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public ResultSet getSelectResultSet(String sql) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		return currentStatement.executeQuery();
	}

	/**
	 * 获取要操作的命令，取得后可绑定参数，然后通过executeQuery()方法取结果集
	 * 
	 * @param sql
	 *            SQL 语句
	 * @return PreparedStatement 对象
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public PreparedStatement getSelectStatement(String sql) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		return currentStatement;
	}


	/**
	 * 清除输入的数据
	 */
	public void clear() {
		dataMap.clear();
	}

	/**
	 * 关闭结果集和句柄
	 * 
	 * @param rs
	 *            要关闭的结果集
	 */
	public void closeResultSetAndStatement(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			rs = null;
		}

		if (currentStatement != null) {
			try {
				currentStatement.close();
			} catch (SQLException e) {
			}
			currentStatement = null;
		}
	}

	/**
	 * 回滚数据库操作，一般在发生异常时调用
	 */
	public void rollback() {
		if (currentConnection == null)
			return;
		try {
			currentConnection.rollback();
		} catch (SQLException e) {
		}
	}

	/**
	 * 提交数据库操作，在操作完成后调用，同时写入操作日志
	 * 
	 * @param rs
	 *            操作的结果集
	 * @throws SQLException
	 *             对数据库操作失败时引发 SQLException 异常
	 */
	public void commit(ResultSet rs) throws SQLException {
		if (currentConnection == null)
			return;
		SQLException se = null;

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			rs = null;
		}

		if (currentStatement != null) {
			try {
				currentStatement.close();
			} catch (SQLException e) {
			}
			currentStatement = null;
		}

		if (modifyList.size() > 0) {
			try {
				currentStatement = currentConnection
						.prepareStatement("insert into swp_log"
								+ "(log_oid, log_oper, log_table, log_column, log_type, log_original, log_new, log_content) "
								+ "values(seq_log.nextval, ?, ?, ?, ?, ?, ?, ?)");
				for (int i = 0; i < modifyList.size(); i++) {
					ModifyInfo info = (ModifyInfo) modifyList.get(i);
					currentStatement.setString(1, info.getOperName());
					currentStatement.setString(2, info.getTableName());
					currentStatement.setString(3, info.getColumnName());
					currentStatement.setString(4, info.getOperType());
					currentStatement.setString(5, info.getFromValue());
					currentStatement.setString(6, info.getToValue());
					currentStatement.setString(7, info.getContent());
					currentStatement.executeUpdate();
				}
				currentStatement.close();
				currentStatement = null;
			} catch (SQLException e) {
			} finally {
				if (currentStatement != null) {
					try {
						currentStatement.close();
					} catch (SQLException e) {
					}
					currentStatement = null;
				}
			}
			modifyList.clear();
		}

		try {
			currentConnection.commit();
		} catch (SQLException e) {
			se = e;
		}

		if (se != null) {
			try {
				currentConnection.rollback();
			} catch (SQLException e) {
			}
		}

		currentConnection.setAutoCommit(true);
		try {
			this.freeTempLobs();
			currentConnection.close();
		} catch (SQLException e) {
		}
		currentConnection = null;

		if (se != null)
			throw se;
	}

	/**
	 * 设置此对象使用 jdbc 连接，而不是默认的连接池
	 * 
	 * @param url
	 *            JDBC url
	 * @param login
	 *            用户名
	 * @param password
	 *            密码
	 */
	public void setJdbcMode(String url, String login, String password) {
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

	/**
	 * 获取数据库连接
	 * 
	 * @return 返回数据库连接
	 * @throws SQLException
	 *             连接数据库失败时引发 SQLException 异常
	 */
	protected Connection getConnection() throws SQLException {
		Connection conn = null;
		if (jdbcUrl != null) {
			conn = DriverManager
					.getConnection(jdbcUrl, jdbcLogin, jdbcPassword);
		} else {
			conn = ConnectManager.getConnection();
		}
		if (conn == null)
			throw new SQLException("connect to database failed.");
		conn.setAutoCommit(false);
		return conn;
	}
}
