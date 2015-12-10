package com.hp.idc.resm.util;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import oracle.jdbc.driver.OracleConnection;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import com.hp.idc.context.util.ContextUtil;
import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.sql.SqlUtils;

/**
 * ���ݿ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DbUtil {

	/**
	 * jdbc��ַ����resm.properties�ļ��н�������
	 */
	private String jdbcUrl;

	/**
	 * jdbc�û�������resm.properties�ļ��н�������
	 */
	private String jdbcUser;

	/**
	 * jdbc���룬��resm.properties�ļ��н�������
	 */
	private String jdbcPassword;

	/**
	 * ���ӳش�С����resm.properties�ļ��н�������
	 */
	private int maxPoolSize;

	/**
	 * ���ӳ���С����������resm.properties�ļ��н�������
	 */
	private int minPoolSize;

	/**
	 * ������չ��������resm.properties�ļ��н�������
	 */
	private int acquireIncrement;

	/**
	 * ���ӳض���
	 */
	private ComboPooledDataSource dataSource = null;

	/**
	 * ��ʼ����������beanʵ����ʱ��ϵͳ�Զ�����
	 */
	public void init() {
		this.dataSource = new ComboPooledDataSource();
		try {
			this.dataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		this.dataSource.setJdbcUrl("jdbc:oracle:thin:@" + this.jdbcUrl);
		this.dataSource.setUser(this.jdbcUser);
		this.dataSource.setPassword(this.jdbcPassword);
		this.dataSource.setMaxPoolSize(this.maxPoolSize);
		this.dataSource.setMinPoolSize(this.minPoolSize);
		this.dataSource.setAcquireIncrement(this.acquireIncrement);
		this.dataSource.setDebugUnreturnedConnectionStackTraces(true);
		this.dataSource.setUnreturnedConnectionTimeout(300);
	}

	/**
	 * ��ȡ������չ����
	 * 
	 * @return ������չ����
	 */
	public int getAcquireIncrement() {
		return this.acquireIncrement;
	}

	/**
	 * ����������չ����
	 * 
	 * @param acquireIncrement
	 *            ������չ����
	 */
	public void setAcquireIncrement(int acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	/**
	 * ��ȡjdbc��ַ
	 * 
	 * @return jdbc��ַ
	 */
	public String getJdbcUrl() {
		return this.jdbcUrl;
	}

	/**
	 * ����jdbc��ַ
	 * 
	 * @param jdbcUrl
	 *            jdbc��ַ
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * ��ȡjdbc�û���
	 * 
	 * @return jdbc�û���
	 */
	public String getJdbcUser() {
		return this.jdbcUser;
	}

	/**
	 * ����jdbc�û���
	 * 
	 * @param jdbcUser
	 *            jdbc�û���
	 */
	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	/**
	 * ��ȡjdbc����
	 * 
	 * @return jdbc����
	 */
	public String getJdbcPassword() {
		return this.jdbcPassword;
	}

	/**
	 * ����jdbc����
	 * 
	 * @param jdbcPassword
	 *            jdbc����
	 */
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * ��ȡ���ӳش�С
	 * 
	 * @return ���ӳش�С
	 */
	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	/**
	 * �������ӳش�С
	 * 
	 * @param maxPoolSize
	 *            ���ӳش�С
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * ��ȡ���ӳ���С������
	 * 
	 * @return ���ӳ���С������
	 */
	public int getMinPoolSize() {
		return this.minPoolSize;
	}

	/**
	 * �������ӳ���С������
	 * 
	 * @param minPoolSize
	 *            ���ӳ���С������
	 */
	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	/**
	 * ��ȡ���ݿ�����
	 * 
	 * @return ���ݿ�����
	 * @throws SQLException
	 *             ����ʧ��ʱ����
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			/*
			 * Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			 * conn = DriverManager.getConnection(
			 * "jdbc:oracle:thin:@192.168.11.25:1521:bomc", "uresm",
			 * "utopteaabc");
			 */
			DbUtil db = (DbUtil) ContextUtil.getBean("databaseBean");
			conn = db.dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return conn;
	}

	/**
	 * ��ȡ���ݿ����ӳ�
	 * @return ���ݿ����ӳ�
	 */
	public static ComboPooledDataSource getDataSource() {
		DbUtil db = (DbUtil) ContextUtil.getBean("databaseBean");
		return db.dataSource;
	}
	
	public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * �ͷ���Դ
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param stmt
	 *            ���ݿ����
	 * @param rs
	 *            ���ݼ�
	 */
	public static void free(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				freeLobs(conn);
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ִ��SQL���
	 * 
	 * @param sql
	 *            Ҫִ�е�sql���
	 * @return INSERT/UPDATE/DELETE����Ӱ�������
	 * @throws Exception
	 *             �����쳣ʱ����
	 */
	public static int execute(String sql) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			String s = sql.trim();
			if (s.endsWith(";"))
				s = s.substring(0, s.length() - 1);
			stmt = conn.prepareStatement(s);
			if (stmt.execute())
				return -1;
			return stmt.getUpdateCount();
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
			throw e;
		} finally {
			DbUtil.free(conn, stmt, null);
		}
	}

	/**
	 * ����LOBS�Ĳ���
	 */
	final static Class<?>[] CREATE_TEMP_ARGS = new Class[] { Connection.class,
			boolean.class, int.class };

	/**
	 * �洢��������ʱ��LOB�����ӵĶ�Ӧ��ϵ�������ӹرյ�ʱ������ͷ�
	 */
	private static ConcurrentHashMap<Connection, List<Object>> lobsMap = new ConcurrentHashMap<Connection, List<Object>>();

	/**
	 * ���lob����hash����
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param lob
	 *            lob����
	 * @see #lobsMap
	 */
	private static void addLobToMap(Connection conn, Object lob) {
		List<Object> list = lobsMap.get(conn);
		if (list == null) {
			list = new ArrayList<Object>();
			lobsMap.put(conn, list);
		}
		list.add(lob);
	}

	/**
	 * �ͷ������д�����LOB����
	 * 
	 * @param conn
	 *            ���ݿ�����
	 */
	private static void freeLobs(Connection conn) {
		List<Object> list = lobsMap.remove(conn);
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof BLOB) {
				try {
					((BLOB) obj).freeTemporary();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (obj instanceof CLOB) {
				try {
					((CLOB) obj).freeTemporary();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ʹ��Oracle��API����һ����ʱ��BLOB����<br/>
	 * ��Ҫ�����ӹر�֮ʱ����freeTemporary���ͷ��������
	 * 
	 * @param c3p0ProxyCon
	 *            c3p0�����ݿ�����
	 * @param cache
	 *            cache һ��Ϊtrue
	 * @param duration
	 *            һ��Ϊ oracle.sql.BLOB.DURATION_SESSION
	 * @return ��ʱ��BLOB����
	 * @throws SQLException
	 *             SQL�����쳣ʱ����
	 */
	public static BLOB createTemporaryBLOB(Connection c3p0ProxyCon,
			boolean cache, int duration) throws SQLException {
		if (c3p0ProxyCon instanceof C3P0ProxyConnection) {
			try {
				C3P0ProxyConnection castCon = (C3P0ProxyConnection) c3p0ProxyCon;
				Method m = BLOB.class.getMethod("createTemporary",
						CREATE_TEMP_ARGS);
				Object[] args = new Object[] {
						C3P0ProxyConnection.RAW_CONNECTION,
						Boolean.valueOf(cache), new Integer(duration) };
				BLOB b = (BLOB) castCon.rawConnectionOperation(m, null, args);
				addLobToMap(c3p0ProxyCon, b);
				return b;
			} catch (InvocationTargetException e) {
				throw SqlUtils.toSQLException(e.getTargetException());
			} catch (Exception e) {
				throw SqlUtils.toSQLException(e);
			}
		} else if (c3p0ProxyCon instanceof OracleConnection) {
			BLOB b = BLOB.createTemporary(c3p0ProxyCon, cache, duration);
			addLobToMap(c3p0ProxyCon, b);
			return b;
		} else
			throw new SQLException(
					"Cannot create an oracle BLOB from a Connection that is neither an oracle.jdbc.driver.Connection, "
							+ "nor a C3P0ProxyConnection wrapped around an oracle.jdbc.driver.Connection.");
	}

	/**
	 * ʹ��Oracle��API����һ����ʱ��CLOB����<br/>
	 * ��Ҫ�����ӹر�֮ʱ����freeTemporary���ͷ��������
	 * 
	 * @param c3p0ProxyCon
	 *            c3p0�����ݿ�����
	 * @param cache
	 *            cache һ��Ϊtrue
	 * @param duration
	 *            һ��Ϊ oracle.sql.CLOB.DURATION_SESSION
	 * @return ��ʱ��CLOB����
	 * @throws SQLException
	 *             SQL�����쳣ʱ����
	 */
	public static CLOB createTemporaryCLOB(Connection c3p0ProxyCon,
			boolean cache, int duration) throws SQLException {
		if (c3p0ProxyCon instanceof C3P0ProxyConnection) {
			try {
				C3P0ProxyConnection castCon = (C3P0ProxyConnection) c3p0ProxyCon;
				Method m = CLOB.class.getMethod("createTemporary",
						CREATE_TEMP_ARGS);
				Object[] args = new Object[] {
						C3P0ProxyConnection.RAW_CONNECTION,
						Boolean.valueOf(cache), new Integer(duration) };
				CLOB b = (CLOB) castCon.rawConnectionOperation(m, null, args);
				addLobToMap(c3p0ProxyCon, b);
				return b;
			} catch (InvocationTargetException e) {
				throw SqlUtils.toSQLException(e.getTargetException());
			} catch (Exception e) {
				throw SqlUtils.toSQLException(e);
			}
		} else if (c3p0ProxyCon instanceof OracleConnection) {
			CLOB b = CLOB.createTemporary(c3p0ProxyCon, cache, duration);
			addLobToMap(c3p0ProxyCon, b);
			return b;
		} else
			throw new SQLException(
					"Cannot create an oracle CLOB from a Connection that is neither an oracle.jdbc.driver.Connection, "
							+ "nor a C3P0ProxyConnection wrapped around an oracle.jdbc.driver.Connection.");
	}

	/**
	 * ת������������clob�ֶ�����ΪString��
	 * 
	 * @param clob
	 *            clob
	 * @return clob����
	 * @throws IOException
	 *             IO�쳣ʱ����
	 * @throws SQLException
	 *             SQL�쳣ʱ����
	 */
	public static String clobToString(Clob clob) throws IOException,
			SQLException {
		String returnStr = "";
		if (clob == null)
			return returnStr;
		Reader reader = clob.getCharacterStream();
		BufferedReader br = new BufferedReader(reader);
		String temp = br.readLine();
		while (temp != null) {
			returnStr += temp + "\n";
			temp = br.readLine();
		}
		return returnStr;
	}

	/**
	 * ��ȡ����ֵ�������Զ����ɵ�����id��<br/>
	 * ����ֵ�洢��resm_sequence���У�����������valueֵ+1��<br/>
	 * ��valueֵΪ4ʱ����������5��idδ�ҵ�ʱ���½�һ����¼��value����Ϊ1��ͬʱ������Ҳ����1��
	 * 
	 * @param id
	 *            ����id
	 * @return ����ֵ
	 * @throws SQLException
	 *             ���ݿ�����쳣
	 */
	public static int getSequence(String id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = getConnection();
		SQLException se = null;
		int startCount = 1;
		int ret = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("update resm_sequence set value=value+1 where id=?");
			ps.setString(1, id);
			int n = ps.executeUpdate();
			if (n == 0) {
				ps.close();
				ps = conn
						.prepareStatement("insert into resm_sequence(id, value) values(?, ?)");
				ps.setString(1, id);
				ps.setInt(2, startCount);
				ps.executeUpdate();
				ret = startCount;
			} else {
				ps.close();
				ps = conn
						.prepareStatement("select value from resm_sequence where id=?");
				ps.setString(1, id);
				rs = ps.executeQuery();
				rs.next();
				ret = rs.getInt(1);
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			se = e;
		} finally {
			free(conn, ps, rs);
		}
		if (se != null)
			throw se;
		return ret;
	}
}
