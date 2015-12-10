package com.hp.idc.itsm.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.util.ConnectManager;

public class BaseTableStructure {

	private static String Create_table ;
	private static String Create_key1 ;
//	private static String Create_key2 =   "alter table ITSM_TASK"+
//												"  add constraint FK_ITSM_TASK_WF foreign key (TASK_WF_OID)"+
//												"  references ITSM_CFG_WORKFLOW (WF_OID)";
	private static String Create_index1;
	private static String Create_index2;
	private static String Create_index3;
	
	
	public static void createTable(){
		
	}
	
	public static void init(){
		Create_table =  "create table ITSM_TASK("+
						"  TASK_OID         NUMBER(18) not null,"+
						"  TASK_PARENT_OID  NUMBER(18) not null,"+
						"  TASK_CREATE_BY   VARCHAR2(75) not null,"+
						"  TASK_CREATE_TIME DATE not null,"+
						"  TASK_UPDATE_BY   VARCHAR2(75) not null,"+
						"  TASK_UPDATE_TIME DATE not null,"+
						"  TASK_WF_OID      NUMBER(18) not null,"+
						"  TASK_WF_VER      NUMBER(18) not null,"+
						"  TASK_PWF_OID    	NUMBER(18) default -1,"+
						"  TASK_PWF_VER    	NUMBER(18) default -1,"+
						"  TASK_USER        VARCHAR2(75) not null,"+
						"  TASK_RELATIONS   VARCHAR2(4000) not null,"+
						"  TASK_HISTORY     CLOB,"+
						"  TASK_STATUS      NUMBER not null,"+
						"  TASK_NODE_ID     VARCHAR2(75),"+
						"  TASK_LINKED      VARCHAR2(4000)"+
						")";
		
		if (Consts.ITSM_TS_TAB!=null && !Consts.ITSM_TS_TAB.equals("")) {
			Create_table += " tablespace "+Consts.ITSM_TS_TAB;
		}
		
		Create_key1 =  "alter table ITSM_TASK add constraint PK_ITSM_TASK_OID primary key (TASK_OID) using index ";
		Create_index1 =  "create index IDX_ITSM_TASK_STATUS on ITSM_TASK (TASK_STATUS) ";
		Create_index2 =  "create index IDX_ITSM_TASK_USER on ITSM_TASK (TASK_USER) ";
		Create_index3 =  "create index IDX_ITSM_TASK_CT on ITSM_TASK (TASK_CREATE_TIME) ";
		
		if (Consts.ITSM_TS_IDX!=null && !Consts.ITSM_TS_IDX.equals("")) {
			Create_key1 += " tablespace "+Consts.ITSM_TS_IDX;
			Create_index1 += " tablespace "+Consts.ITSM_TS_IDX;
			Create_index2 += " tablespace "+Consts.ITSM_TS_IDX;
			Create_index3 += " tablespace "+Consts.ITSM_TS_IDX;
		}
		
	}
	

	/**
	 * 更新表结构
	 * 
	 * @param columns
	 *            表列数组{"colA"="vchar2(200)"...}
	 * @throws SQLException
	 */
	public static void updateTableStructure(Map params) throws SQLException {
		String tabName = (String) params.get("SYS_TABLE_NAME");
		tabName = tabName.toUpperCase();

		boolean update = tableIsExist(tabName);

		StringBuffer sqlAdd = new StringBuffer();
		StringBuffer sqlDel = new StringBuffer();
		if (update) {
			String path = getStringOfColumns(tabName);
			Map colRecordCount = getRecordCount(tabName, path);
			sqlAdd.append("ALTER TABLE ITSM_TASK ADD(");
			// 增加字段
			Object[] keys = params.keySet().toArray();
			int indAdd = 0;
			for (int i = 0; i < keys.length; i++) {
				String key = (String) keys[i];
				System.out.println(key + "&&&&&&");
				
				if (key.startsWith("SYS_COL_")) {
					String colsName = key.substring(8).toUpperCase();
					// 已经存在的字段过滤掉
					if (colRecordCount.get(colsName) != null) {
						colRecordCount.put(colsName, "-1");
						continue;
					}
					if (indAdd > 0)
						sqlAdd.append(",");
					sqlAdd.append(colsName + " " + params.get(key));
					indAdd++;
					colRecordCount.put(colsName, "-1");
				}
			} 
			sqlAdd.append(")");
			if (indAdd == 0)
				sqlAdd.delete(0, sqlAdd.length());

			// 删除不用的字段
			sqlDel.append("ALTER TABLE ITSM_TASK DROP(");
			Object[] colRecordKeys = colRecordCount.keySet().toArray();
			int ind = 0;
			for (int i = 0; i < colRecordKeys.length; i++) {
				String colsName = (String)colRecordKeys[i];
				if (colRecordCount.get(colsName).equals("0")&&colsName.startsWith("FLD_")) {
					if (ind > 0)
						sqlDel.append(",");
					sqlDel.append(colsName);
					ind++;
				}
			}
			sqlDel.append(")");
			if (ind == 0)
				sqlDel.delete(0, sqlDel.length());

		} else {

			int pos =  Create_table.lastIndexOf(")");
			sqlAdd.append(Create_table.substring(0, pos));
			Object[] keys = params.keySet().toArray();
			for (int i = 0; i < keys.length; i++) {
				String key = (String) keys[i];
				if (key.startsWith("SYS_COL_")) {
					String colsName = key.substring(8);
					sqlAdd.append(",");
					sqlAdd.append(colsName + " " + params.get(key));
				}
			}
			sqlAdd.append(")");
			sqlAdd.append(Create_table.substring(pos + 1));
		}

		//System.out.println(sqlAdd.toString());
		//System.out.println(sqlDel.toString());

		// 执行更新表结构的语句

		Connection conn = ConnectManager.getConnection();

		try {
			PreparedStatement ps = null;
			conn.setAutoCommit(false);
			System.out.println("更新/创建流程数据表结构....");
			if (sqlAdd.toString().trim().length() != 0) {
				String sql = sqlAdd.toString().replaceAll("ITSM_TASK", tabName);
				System.out.println(sql);
				ps = conn.prepareStatement(sql);
				ps.execute();
				if (!update) {
					ps = conn.prepareStatement(Create_key1.replaceAll("ITSM_TASK", tabName));
					ps.execute();
					//ps = conn.prepareStatement(Create_key2.replaceAll("ITSM_TASK", tabName));
					//ps.execute();
					ps = conn.prepareStatement(Create_index1.replaceAll("ITSM_TASK", tabName));
					ps.execute();
					ps = conn.prepareStatement(Create_index2.replaceAll("ITSM_TASK", tabName));
					ps.execute();
					ps = conn.prepareStatement(Create_index3.replaceAll("ITSM_TASK", tabName));
					ps.execute();
				}
			}
			if (sqlDel.toString().trim().length() != 0) {
				ps = conn.prepareStatement(sqlDel.toString().replaceAll("ITSM_TASK", tabName));
				System.out.println(sqlDel.toString().replaceAll("ITSM_TASK", tabName));
				ps.execute();
			}
			

		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.commit();
				conn.setAutoCommit(true);
				conn.close();
			}
		}
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param tabName
	 * @return
	 * @throws SQLException
	 */
	public static Map getRecordCount(String tabName, String colsPath)
			throws SQLException {
		String[] cols = colsPath.split(",");
		Map retMap = new HashMap();
		colsPath = colsPath.replaceAll(",", "),count(");
		colsPath = "count(" + colsPath + ")";
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
//		u.setJdbcMode("jdbc:oracle:thin:@192.168.11.25:1521:orcl", "utoptea",
//				"utopteaabc");

		try {
			String sql = "select " + colsPath + " from " + tabName;
			rs = u.getSelectResultSet(sql);
			if (rs.next()) {
				for (int i = 0; i < cols.length; i++) {
					retMap.put(cols[i], rs.getString("count(" + cols[i] + ")"));
					System.out.println(cols[i]+"*******"+rs.getString("count(" + cols[i] + ")")+"----------------");
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return retMap;
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param tabName
	 * @return
	 * @throws SQLException
	 */
	public static boolean tableIsExist(String tabName) throws SQLException {

		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
//		u.setJdbcMode("jdbc:oracle:thin:@192.168.11.25:1521:toptea", "utoptea",
//				"utopteaabc");

		try {
			String sql = "select table_name from user_tables where table_name='"
					+ tabName.toUpperCase() + "'";
			rs = u.getSelectResultSet(sql);
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return false;
	}

	/**
	 * 快速获取所有列名
	 * (v: 如果数据库用户没有读取USER_TAB_COLUMNS的权限，此函数将无法执行，已经更改实现)
	 * 
	 * @param tableName 表明
	 * @return columnA, columnB, columnC.....
	 * @throws SQLException
	 */
	public static String getStringOfColumns(String tabName) throws SQLException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
//		u.setJdbcMode("jdbc:oracle:thin:@192.168.11.25:1521:orcl", "utoptea",
//				"utopteaabc");

		String retStr = "";
		try {
//			String sql = "Select  max(SYS_CONNECT_BY_PATH(COLUMN_NAME, ',')) path From (select A.COLUMN_NAME,A.TABLE_NAME, ROWNUM AS ROWNO from USER_TAB_COLUMNS A where TABLE_NAME = '"
//					+ tabName.toUpperCase()
//					+ "' AND A.DATA_TYPE<>'CLOB' ORDER BY A.COLUMN_ID) start with ROWNO = 1 connect by ROWNO = rownum";
//			rs = u.getSelectResultSet(sql);
//			if (rs.next()) {
//				retStr = rs.getString("path");
//			}
			String sql = "select * from "+tabName+" where 1=2";
			rs = u.getSelectResultSet(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			boolean hasR = false;
			for (int i = 1; i <= rsmd.getColumnCount(); i++){
				if (rsmd.getColumnTypeName(i).equals("CLOB")){
					continue;
				}
				
				if(hasR)
					retStr += ",";
				retStr += rsmd.getColumnName(i);
				hasR = true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return retStr;
	}
	
	public static void main(String[] args){
		try {
			String colsPath = BaseTableStructure.getStringOfColumns("ITSM_task");
			System.out.println(colsPath);
			BaseTableStructure.getRecordCount("ITSM_task", colsPath);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
