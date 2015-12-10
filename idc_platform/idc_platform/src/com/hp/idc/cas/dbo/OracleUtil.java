package com.hp.idc.cas.dbo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.driver.OracleConnection;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.sql.SqlUtils;

public class OracleUtil {
    final static Class[] CREATE_TEMP_ARGS = new Class[]{Connection.class, boolean.class, int.class};
    
    /**
     *  Uses Oracle-specific API on the raw, underlying Connection to create a temporary BLOB.
     *  <b>Users are responsible for calling freeTemporary on the returned BLOB prior to Connection close() / check-in!
     *  c3p0 will <i>not</i> automatically clean up temporary BLOBs.</b>
     *
     * @param c3p0ProxyCon may be a c3p0 proxy for an <tt>oracle.jdbc.driver.OracleConnection</tt>, or an 
     *        <tt>oracle.jdbc.driver.OracleConnection</tt> directly.
     */
    public static BLOB createTemporaryBLOB(Connection c3p0ProxyCon, boolean cache, int duration) throws SQLException
    { 
	if (c3p0ProxyCon instanceof C3P0ProxyConnection)
	    {
		try
		    {
			C3P0ProxyConnection castCon = (C3P0ProxyConnection) c3p0ProxyCon;
			Method m = BLOB.class.getMethod("createTemporary", CREATE_TEMP_ARGS);
			Object[] args = new Object[] {C3P0ProxyConnection.RAW_CONNECTION, Boolean.valueOf( cache ), new Integer( duration )};
			return (BLOB) castCon.rawConnectionOperation(m, null, args);			
		    }
		catch (InvocationTargetException e)
		    {
			throw SqlUtils.toSQLException( e.getTargetException() );
		    }
		catch (Exception e)
		    {
			throw SqlUtils.toSQLException( e );
		    }
	    }
	else if (c3p0ProxyCon instanceof OracleConnection)
	    return BLOB.createTemporary( c3p0ProxyCon, cache, duration );
	else
	    throw new SQLException("Cannot create an oracle BLOB from a Connection that is neither an oracle.jdbc.driver.Connection, " +
				   "nor a C3P0ProxyConnection wrapped around an oracle.jdbc.driver.Connection.");
    }
	
    /**
     *  Uses Oracle-specific API on the raw, underlying Connection to create a temporary CLOB.
     *  <b>Users are responsible for calling freeTemporary on the returned BLOB prior to Connection close() / check-in!
     *  c3p0 will <i>not</i> automatically clean up temporary CLOBs.</b>
     *
     * @param c3p0ProxyCon may be a c3p0 proxy for an <tt>oracle.jdbc.driver.OracleConnection</tt>, or an 
     *        <tt>oracle.jdbc.driver.OracleConnection</tt> directly.
     */
    public static CLOB createTemporaryCLOB(Connection c3p0ProxyCon, boolean cache, int duration) throws SQLException
    { 
	if (c3p0ProxyCon instanceof C3P0ProxyConnection)
	    {
		try
		    {
			C3P0ProxyConnection castCon = (C3P0ProxyConnection) c3p0ProxyCon;
			Method m = CLOB.class.getMethod("createTemporary", CREATE_TEMP_ARGS);
			Object[] args = new Object[] {C3P0ProxyConnection.RAW_CONNECTION, Boolean.valueOf( cache ), new Integer( duration )};
			return (CLOB) castCon.rawConnectionOperation(m, null, args);			
		    }
		catch (InvocationTargetException e)
		    {
			throw SqlUtils.toSQLException( e.getTargetException() );
		    }
		catch (Exception e)
		    {
			throw SqlUtils.toSQLException( e );
		    }
	    }
	else if (c3p0ProxyCon instanceof OracleConnection)
	    return CLOB.createTemporary( c3p0ProxyCon, cache, duration );
	else
	    throw new SQLException("Cannot create an oracle CLOB from a Connection that is neither an oracle.jdbc.driver.Connection, " +
				   "nor a C3P0ProxyConnection wrapped around an oracle.jdbc.driver.Connection.");
    }

	static public List getUsers() throws SQLException {
		List returnList = new ArrayList();
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select username from dba_users";
			rs = u.getSelectResultSet(sql);
			while (rs.next()) {
				returnList.add(rs.getString(1).toLowerCase());
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}
		return returnList;
	}
	
	static public List getColumnsOfTable(String user, String table) throws SQLException {
		List returnList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select column_name from dba_tab_cols where owner=? and table_name=?";
			ps = u.getSelectStatement(sql);
			ps.setString(1, user.toUpperCase());
			ps.setString(2, table.toUpperCase());
			rs = ps.executeQuery();
			while (rs.next()) {
				returnList.add(rs.getString(1).toLowerCase());
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}
		return returnList;
	}

	static public List getTablesOfUser(String user) throws SQLException {
		List returnList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select table_name from dba_tables where owner=?";
			ps = u.getSelectStatement(sql);
			ps.setString(1, user.toUpperCase());
			rs = ps.executeQuery();
			while (rs.next()) {
				returnList.add(rs.getString(1).toLowerCase());
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}
		return returnList;
	}
}
