package com.hp.idc.cas.dbo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

/**
 * 表示存放需要更新列的信息，包括类型和数据
 * 
 * @author Silence
 */
public class ColumnData {

	/**
	 * 存放对象的数据
	 */
	protected Object obj;

	/**
	 * 存放对象的类型
	 */
	protected char type;

	/**
	 * 存放对象的显示文本
	 */
	protected String displayText;

	/**
	 * 表示“空”类型的数据
	 */
	public static final char TYPE_NULL = 'n';

	/**
	 * 表示整型的数据
	 */
	public static final char TYPE_INTEGER = 'i';

	/**
	 * 表示字符型的数据
	 */
	public static final char TYPE_STRING = 's';

	/**
	 * 表示日期型的数据
	 */
	public static final char TYPE_DATE = 'd';

	/**
	 * 表示日期时间型的数据
	 */
	public static final char TYPE_DATETIME = 't';

	/**
	 * 表示clob型的数据
	 */
	public static final char TYPE_CLOB = 'c';

	/**
	 * 表示blob型的数据
	 */
	public static final char TYPE_BLOB = 'b';

	/**
	 * 使用指定的类型和数据构造 ColumnData 对象
	 * 
	 * @param type
	 *            表示对象的类型
	 * @param obj
	 *            表示对象的数据
	 */
	public ColumnData(char type, Object obj) {
		if (type == TYPE_DATE)
			setDate((String) obj);
		else if (type == TYPE_DATETIME)
			setDateTime((String) obj);
		else if (type == TYPE_NULL)
			setNull();
		else if (type == TYPE_STRING)
			setString((String) obj);
		else if (type == TYPE_INTEGER)
			setInt(((Integer) obj).intValue());
		else if (type == TYPE_CLOB)
			setClob(obj);
		else if (type == TYPE_BLOB)
			setBlob(obj);
	}

	public CLOB setClob(Connection conn, ResultSet rs, String columnName,
			Object value) throws SQLException {
		if (value == null) {
			rs.updateNull(columnName);
			return null;
		}
		oracle.sql.CLOB tempClob = null;
		try {
			tempClob = OracleUtil.createTemporaryCLOB
				(conn, true, oracle.sql.CLOB.DURATION_SESSION);
			tempClob.open(oracle.sql.CLOB.MODE_READWRITE);
			Writer tempClobWriter = tempClob.getCharacterOutputStream();
			tempClobWriter.write(value.toString());
			tempClobWriter.flush();
			tempClobWriter.close();
			tempClob.close();
			rs.updateClob(columnName, (Clob) tempClob);
		} catch (IOException e) {
			throw new SQLException(e.getMessage());
		}
		return tempClob;
	}

	public BLOB setBlob(Connection conn, ResultSet rs, String columnName,
			Object value) throws SQLException {
		if (value == null) {
			rs.updateNull(columnName);
			return null;
		}
		oracle.sql.BLOB tempBlob = null;
		try {
			tempBlob = OracleUtil.createTemporaryBLOB
				(conn, true, oracle.sql.BLOB.DURATION_SESSION);
			tempBlob.open(oracle.sql.BLOB.MODE_READWRITE);
			OutputStream tempBlobWriter = tempBlob.getBinaryOutputStream();
			tempBlobWriter.write(value.toString().getBytes());
			tempBlobWriter.flush();
			tempBlobWriter.close();
			tempBlob.close();
			rs.updateBlob(columnName, (Blob) tempBlob);
		} catch (IOException e) {
			throw new SQLException(e.getMessage());
		}
		return tempBlob;
	}

	/**
	 * 将对象设置为空值
	 * 
	 */
	public void setNull() {
		type = TYPE_NULL;
		obj = null;
		displayText = "";
	}

	/**
	 * 将对象设置为指定的字符串
	 * 
	 * @param data
	 *            设置的字符串内容
	 */
	public void setString(String data) {
		type = TYPE_STRING;
		obj = data;
		displayText = data;
	}

	/**
	 * 将对象设置为指定的日期
	 * 
	 * @param data
	 *            设置的日期内容, 格式yyyyMMdd
	 */
	public void setDate(String data) {
		type = TYPE_DATE;
		int n = data.length();
		if (n == 8 || n == 14)
			obj = Date.valueOf(data.substring(0, 4) + "-"
					+ data.substring(4, 6) + "-" + data.substring(6, 8));
		else if (n == 10 || n == 19)
			obj = Date.valueOf(data.substring(0, 4) + "-"
					+ data.substring(5, 7) + "-" + data.substring(8, 10));
		else {
			setNull();
			return;
		}

		displayText = data + "000000";
	}

	/**
	 * 将对象设置为指定的整数值
	 * 
	 * @param data
	 *            设置的整数值
	 */
	public void setInt(int data) {
		type = TYPE_INTEGER;
		obj = new Integer(data);
		displayText = obj.toString();
	}

	/**
	 * 将对象设置为指定的时间
	 * 
	 * @param data
	 *            设置的时间内容, 格式yyyyMMddHHmmss
	 */
	public void setDateTime(String data) {
		type = TYPE_DATETIME;
		int n = data.length();
		if (n == 14)
			obj = Timestamp.valueOf(data.substring(0, 4) + "-"
					+ data.substring(4, 6) + "-" + data.substring(6, 8) + " "
					+ data.substring(8, 10) + ":" + data.substring(10, 12)
					+ ":" + data.substring(12, 14));
		else if (n == 19)
			obj = Timestamp.valueOf(data.substring(0, 4) + "-"
					+ data.substring(5, 7) + "-" + data.substring(8, 10) + " "
					+ data.substring(11, 13) + ":" + data.substring(14, 16)
					+ ":" + data.substring(17, 19));
		else {
			setNull();
			return;
		}
		displayText = data;
	}

	/**
	 * 设置clob类型
	 * 
	 * @param obj
	 */
	public void setClob(Object _obj) {
		type = TYPE_CLOB;
		obj = _obj;
	}

	/**
	 * 设置blob类型
	 * 
	 * @param obj
	 */
	public void setBlob(Object _obj) {
		type = TYPE_BLOB;
		obj = _obj;
	}

	/**
	 * 将本对象表示的数据更新到对应 ResultSet 对象的指定列中
	 * 
	 * @param columnName
	 *            指定的列名
	 * @param rs
	 *            要更新的结果集对象
	 * @param map
	 *            存放列名->列类型的对应表
	 * @throws SQLException
	 */
	public void update(OracleOperation oo, Connection conn, String columnName, ResultSet rs, Map map)
			throws SQLException {
		String colClassName = (String) map.get(columnName);
		if (colClassName == null) {
			System.out.println("columnName=" + columnName);
			Object obs[] = map.keySet().toArray();
			for (int i = 0; i < obs.length; i++) {
				System.out.println("column(" + i + ")=" + obs[i]);
			}
			throw new SQLException("sql error? column not in table.");
		}
		if (colClassName.equals("java.sql.Timestamp")) {
			if (type == TYPE_STRING) {
				String str = (String) obj;
				switch (str.length()) {
				case 19:
					rs.updateTimestamp(columnName, Timestamp.valueOf(str
							.substring(0, 4)
							+ "-"
							+ str.substring(5, 7)
							+ "-"
							+ str.substring(8, 10)
							+ " "
							+ str.substring(11, 13)
							+ ":"
							+ str.substring(14, 16)
							+ ":"
							+ str.substring(17, 19)));
					break;
				case 14:
					rs.updateTimestamp(columnName, Timestamp.valueOf(str
							.substring(0, 4)
							+ "-"
							+ str.substring(4, 6)
							+ "-"
							+ str.substring(6, 8)
							+ " "
							+ str.substring(8, 10)
							+ ":"
							+ str.substring(10, 12)
							+ ":"
							+ str.substring(12, 14)));
					break;
				case 10:
					rs.updateDate(columnName, Date.valueOf(str.substring(0, 4)
							+ "-" + str.substring(5, 7) + "-"
							+ str.substring(8, 10)));
					break;
				case 8:
					rs.updateDate(columnName, Date.valueOf(str.substring(0, 4)
							+ "-" + str.substring(4, 6) + "-"
							+ str.substring(6, 8)));
					break;
				default:
					rs.updateNull(columnName);
				}
				return;
			}
		}
		if (type == TYPE_STRING) {
			if (obj == null || obj.toString().length() == 0)
				rs.updateNull(columnName);
			else
				rs.updateString(columnName, (String) obj);
		} else if (type == TYPE_DATETIME)
			rs.updateTimestamp(columnName, (Timestamp) obj);
		else if (type == TYPE_DATE)
			rs.updateDate(columnName, (Date) obj);
		else if (type == TYPE_INTEGER)
			rs.updateInt(columnName, ((Integer) obj).intValue());
		else if (type == TYPE_NULL)
			rs.updateNull(columnName);
		else if (type == TYPE_CLOB) {
			CLOB clob = this.setClob(conn, rs, columnName, obj);
			if (clob != null)
				oo.addTempClob(clob);
		} else if (type == TYPE_BLOB) {
			BLOB blob = this.setBlob(conn, rs, columnName, obj);
			if (blob != null)
				oo.addTempBlob(blob);
		}
	}

	/**
	 * 获取本对象的可显示的文本
	 * 
	 * @return 返回本对象的可显示的文本
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 * 与指定的结果集对应的列进行比较，返回比较结果，并在不同时添加到差异列表中
	 * 
	 * @param rs
	 *            要比较的结果集
	 * @param columnName
	 *            要比较的列名
	 * @param oo
	 *            相应操作的 OracleOperation 对象
	 * @param operType
	 *            操作类型
	 * @param keyColumn
	 *            主键列的列名
	 * @return 相同时返回 true, 否则返回 false
	 * @throws SQLException
	 *             当数据库操作失败时抛出 SQLException 异常
	 */
	public boolean compareWith(ResultSet rs, String columnName,
			OracleOperation oo, String operType, String keyColumn)
			throws SQLException {
		Object d = rs.getObject(columnName);
		String keyValue = "";
		if (!keyColumn.equals("-")) {
			try {
				keyValue = rs.getString(keyColumn);
			} catch (SQLException e) {
			}
		}
		if (d == null) { // check null value
			if (type == TYPE_NULL)
				return true;
			if (type == TYPE_STRING && ((String) obj).length() == 0)
				return true;
			oo.addModify(operType, columnName, "", displayText, keyValue);
		} else if (type == TYPE_STRING) { // compare string
			if (d.toString().equals(obj))
				return true;
			oo.addModify(operType, columnName, d.toString(), displayText,
					keyValue);
		} else if (type == TYPE_INTEGER) {
			int val = rs.getInt(columnName);
			if (val == ((Integer) obj).intValue())
				return true;
			oo.addModify(operType, columnName, d.toString(), displayText,
					keyValue);
		} else if (type == TYPE_DATE || type == TYPE_DATETIME) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String str = sdf.format(rs.getTimestamp(columnName));
			if (str.equals(displayText))
				return true;
			oo.addModify(operType, columnName, str, displayText, keyValue);
		}
		return false;
	}
}
