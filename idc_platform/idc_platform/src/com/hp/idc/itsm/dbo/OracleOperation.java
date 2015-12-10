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
 * Oracle �����ķ�װ�࣬�����Զ���¼������־
 * 
 * @author ÷԰
 */
public class OracleOperation {

	/**
	 * JDBC url, ����Ҫʹ�� JDBC ģʽʱ���� setJdbcMode ����
	 */
	protected String jdbcUrl = null;

	/**
	 * JDBC �û���, ����Ҫʹ�� JDBC ģʽʱ���� setJdbcMode ����
	 */
	protected String jdbcLogin = null;

	/**
	 * JDBC ����, ����Ҫʹ�� JDBC ģʽʱ���� setJdbcMode ����
	 */
	protected String jdbcPassword = null;

	/**
	 * �����ı���
	 */
	protected String tableName;

	/**
	 * Ҫ���µ����ݣ�����->ColumnData
	 */
	protected Map dataMap = new HashMap();

	/**
	 * ʹ�õ����ݿ�����
	 */
	protected Connection currentConnection = null;

	/**
	 * ��ǰִ�е����
	 */
	PreparedStatement currentStatement = null;

	/**
	 * ����Ա
	 */
	protected String operName;

	/**
	 * �Ķ����ֶ���Ϣ�б�
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
	 * ��ʹ�� select ʱ�ô˹��캯��
	 */
	public OracleOperation() {
		this.tableName = "?";
		this.operName = "system";
	}

	/**
	 * ʹ��ָ���ı����Ͳ���Ա���ƹ��� OracleOperation ����
	 * 
	 * @param tableName
	 *            �����ı���
	 * @param operName
	 *            ����Ա����
	 */
	public OracleOperation(String tableName, String operName) {
		this.tableName = tableName;
		this.operName = operName;
		if (this.operName == null || this.operName.length() == 0)
			this.operName = "system";
	}

	/**
	 * ���ò����ı���
	 * 
	 * @param tableName
	 *            �����ı���
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Connection getCurrentConnection() {
		return currentConnection;
	}

	/**
	 * ����ָ���ж�Ӧ�ĸ��»���������
	 * 
	 * @param columnName
	 *            ����
	 * @param data
	 *            ���»���������
	 */
	public void setColumnData(String columnName, ColumnData data) {
		dataMap.put(columnName.toLowerCase(), data);
	}

	/**
	 * ��ӸĶ�����Ϣ����Ҫ���ô˺���
	 * 
	 * @param operType
	 *            �������ͣ�delete/insert/update
	 * @param columnName
	 *            ����
	 * @param fromValue
	 *            ԭʼֵ
	 * @param toValue
	 *            ��ֵ
	 * @param content
	 *            ����ֵ
	 */
	protected void addModify(String operType, String columnName,
			String fromValue, String toValue, String content) {
		modifyList.add(new ModifyInfo(operName, operType, tableName,
				columnName, fromValue, toValue, content));
	}

	/**
	 * ִ�и��²���
	 * 
	 * @param rs
	 *            ���������ݼ�
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
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
	 * ִ��ɾ������
	 * 
	 * @param rs
	 *            ���������ݼ�
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
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
	 * ִ�в������
	 * 
	 * @param rs
	 *            ���������ݼ�
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public void executeInsert(ResultSet rs) throws SQLException {
		rs.moveToInsertRow();
		Map map = getColumnTypes(rs);
		updateResultSet(rs, "insert", map, dataMap.keySet().toArray());
		rs.insertRow();
		rs.moveToCurrentRow();
	}

	/**
	 * ��ȡ�ֶ���->�ֶ����͵Ķ�Ӧ��
	 * 
	 * @param rs
	 *            ���������ݼ�
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
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
	 *            ���������ݼ�
	 * @param operType
	 *            �������ͣ�delete/insert/update
	 * @param map
	 *            �ֶ���->�ֶ����͵Ķ�Ӧ��
	 * @param keys
	 *            ��Ҫ������������
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
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
	 * ��ȡ���е���һ��ֵ
	 * 
	 * @param seqName
	 *            ������
	 * @return �������е���һ��ֵ
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
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
	 * ����ָ���������� SQL ���
	 * 
	 * @param where
	 *            ��ѯ������null ��ʾ�������κ����ݣ�"" ��ʾ������������
	 * @return ���ذ���ָ�������� SQL ���
	 */
	protected String getSql(String where) {
		if (where == null)
			return "select t.* from " + tableName + " t where 2 < 1";
		else if (where.length() == 0)
			return "select t.* from " + tableName + " t";
		return "select t.* from " + tableName + " t where " + where;
	}

	/**
	 * ��ȡҪ�����Ľ����
	 * 
	 * @param where
	 *            Ϊnullʱȡ�ռ��ϣ�""Ϊ�������ݣ�����Ϊ��ѯ����
	 * @return ��ѯ�����
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public ResultSet getResultSet(String where) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(getSql(where),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return currentStatement.executeQuery();
	}

	/**
	 * ��ȡҪ���������ȡ�ú�ɰ󶨲�����Ȼ��ͨ��executeQuery()����ȡ�����
	 * 
	 * @param where
	 *            Ϊnullʱȡ�ռ��ϣ�""Ϊ�������ݣ�����Ϊ��ѯ����
	 * @return PreparedStatement ����
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public PreparedStatement getStatement(String where) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(getSql(where),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return currentStatement;
	}

	/**
	 * ��ȡҪ�����Ľ����
	 * 
	 * @param sql
	 *            SQL ���
	 * @return ��ѯ�����
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public ResultSet getSelectResultSet(String sql) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		return currentStatement.executeQuery();
	}

	/**
	 * ��ȡҪ���������ȡ�ú�ɰ󶨲�����Ȼ��ͨ��executeQuery()����ȡ�����
	 * 
	 * @param sql
	 *            SQL ���
	 * @return PreparedStatement ����
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public PreparedStatement getSelectStatement(String sql) throws SQLException {
		if (currentConnection == null)
			currentConnection = getConnection();
		currentStatement = currentConnection.prepareStatement(sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		return currentStatement;
	}


	/**
	 * ������������
	 */
	public void clear() {
		dataMap.clear();
	}

	/**
	 * �رս�����;��
	 * 
	 * @param rs
	 *            Ҫ�رյĽ����
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
	 * �ع����ݿ������һ���ڷ����쳣ʱ����
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
	 * �ύ���ݿ�������ڲ�����ɺ���ã�ͬʱд�������־
	 * 
	 * @param rs
	 *            �����Ľ����
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ���� SQLException �쳣
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
	 * ���ô˶���ʹ�� jdbc ���ӣ�������Ĭ�ϵ����ӳ�
	 * 
	 * @param url
	 *            JDBC url
	 * @param login
	 *            �û���
	 * @param password
	 *            ����
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
	 * ��ȡ���ݿ�����
	 * 
	 * @return �������ݿ�����
	 * @throws SQLException
	 *             �������ݿ�ʧ��ʱ���� SQLException �쳣
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
