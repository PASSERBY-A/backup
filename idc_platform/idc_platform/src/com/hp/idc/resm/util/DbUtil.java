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
 * 数据库操作
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DbUtil {

	/**
	 * jdbc地址，在resm.properties文件中进行配置
	 */
	private String jdbcUrl;

	/**
	 * jdbc用户名，在resm.properties文件中进行配置
	 */
	private String jdbcUser;

	/**
	 * jdbc密码，在resm.properties文件中进行配置
	 */
	private String jdbcPassword;

	/**
	 * 连接池大小，在resm.properties文件中进行配置
	 */
	private int maxPoolSize;

	/**
	 * 连接池最小连接数，在resm.properties文件中进行配置
	 */
	private int minPoolSize;

	/**
	 * 连接扩展增量，在resm.properties文件中进行配置
	 */
	private int acquireIncrement;

	/**
	 * 连接池对象
	 */
	private ComboPooledDataSource dataSource = null;

	/**
	 * 初始化函数，在bean实例化时由系统自动调用
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
	 * 获取连接扩展增量
	 * 
	 * @return 连接扩展增量
	 */
	public int getAcquireIncrement() {
		return this.acquireIncrement;
	}

	/**
	 * 设置连接扩展增量
	 * 
	 * @param acquireIncrement
	 *            连接扩展增量
	 */
	public void setAcquireIncrement(int acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	/**
	 * 获取jdbc地址
	 * 
	 * @return jdbc地址
	 */
	public String getJdbcUrl() {
		return this.jdbcUrl;
	}

	/**
	 * 设置jdbc地址
	 * 
	 * @param jdbcUrl
	 *            jdbc地址
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * 获取jdbc用户名
	 * 
	 * @return jdbc用户名
	 */
	public String getJdbcUser() {
		return this.jdbcUser;
	}

	/**
	 * 设置jdbc用户名
	 * 
	 * @param jdbcUser
	 *            jdbc用户名
	 */
	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	/**
	 * 获取jdbc密码
	 * 
	 * @return jdbc密码
	 */
	public String getJdbcPassword() {
		return this.jdbcPassword;
	}

	/**
	 * 设置jdbc密码
	 * 
	 * @param jdbcPassword
	 *            jdbc密码
	 */
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * 获取连接池大小
	 * 
	 * @return 连接池大小
	 */
	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	/**
	 * 设置连接池大小
	 * 
	 * @param maxPoolSize
	 *            连接池大小
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * 获取连接池最小连接数
	 * 
	 * @return 连接池最小连接数
	 */
	public int getMinPoolSize() {
		return this.minPoolSize;
	}

	/**
	 * 设置连接池最小连接数
	 * 
	 * @param minPoolSize
	 *            连接池最小连接数
	 */
	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return 数据库连接
	 * @throws SQLException
	 *             连接失败时发生
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
	 * 获取数据库连接池
	 * @return 数据库连接池
	 */
	public static ComboPooledDataSource getDataSource() {
		DbUtil db = (DbUtil) ContextUtil.getBean("databaseBean");
		return db.dataSource;
	}
	
	public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 释放资源
	 * 
	 * @param conn
	 *            数据库连接
	 * @param stmt
	 *            数据库语句
	 * @param rs
	 *            数据集
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
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            要执行的sql语句
	 * @return INSERT/UPDATE/DELETE操作影响的行数
	 * @throws Exception
	 *             操作异常时发生
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
	 * 创建LOBS的参数
	 */
	final static Class<?>[] CREATE_TEMP_ARGS = new Class[] { Connection.class,
			boolean.class, int.class };

	/**
	 * 存储创建的临时的LOB与连接的对应关系，在连接关闭的时候进行释放
	 */
	private static ConcurrentHashMap<Connection, List<Object>> lobsMap = new ConcurrentHashMap<Connection, List<Object>>();

	/**
	 * 添加lob对象到hash表中
	 * 
	 * @param conn
	 *            数据库连接
	 * @param lob
	 *            lob对象
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
	 * 释放连接中创建的LOB对象
	 * 
	 * @param conn
	 *            数据库连接
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
	 * 使用Oracle的API创建一个临时的BLOB对象。<br/>
	 * 需要在连接关闭之时调用freeTemporary来释放这个对象。
	 * 
	 * @param c3p0ProxyCon
	 *            c3p0的数据库连接
	 * @param cache
	 *            cache 一般为true
	 * @param duration
	 *            一般为 oracle.sql.BLOB.DURATION_SESSION
	 * @return 临时的BLOB对象
	 * @throws SQLException
	 *             SQL操作异常时发生
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
	 * 使用Oracle的API创建一个临时的CLOB对象。<br/>
	 * 需要在连接关闭之时调用freeTemporary来释放这个对象。
	 * 
	 * @param c3p0ProxyCon
	 *            c3p0的数据库连接
	 * @param cache
	 *            cache 一般为true
	 * @param duration
	 *            一般为 oracle.sql.CLOB.DURATION_SESSION
	 * @return 临时的CLOB对象
	 * @throws SQLException
	 *             SQL操作异常时发生
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
	 * 转化结果集里面的clob字段数据为String型
	 * 
	 * @param clob
	 *            clob
	 * @return clob内容
	 * @throws IOException
	 *             IO异常时发生
	 * @throws SQLException
	 *             SQL异常时发生
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
	 * 获取序列值，用于自动生成递增的id。<br/>
	 * 序列值存储在resm_sequence表中，本函数返回value值+1。<br/>
	 * 如value值为4时，函数返回5。id未找到时会新建一条记录，value设置为1，同时本函数也返回1。
	 * 
	 * @param id
	 *            序列id
	 * @return 序列值
	 * @throws SQLException
	 *             数据库操作异常
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
