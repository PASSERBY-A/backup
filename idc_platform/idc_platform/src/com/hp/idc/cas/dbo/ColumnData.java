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
 * ��ʾ�����Ҫ�����е���Ϣ���������ͺ�����
 * 
 * @author Silence
 */
public class ColumnData {

	/**
	 * ��Ŷ��������
	 */
	protected Object obj;

	/**
	 * ��Ŷ��������
	 */
	protected char type;

	/**
	 * ��Ŷ������ʾ�ı�
	 */
	protected String displayText;

	/**
	 * ��ʾ���ա����͵�����
	 */
	public static final char TYPE_NULL = 'n';

	/**
	 * ��ʾ���͵�����
	 */
	public static final char TYPE_INTEGER = 'i';

	/**
	 * ��ʾ�ַ��͵�����
	 */
	public static final char TYPE_STRING = 's';

	/**
	 * ��ʾ�����͵�����
	 */
	public static final char TYPE_DATE = 'd';

	/**
	 * ��ʾ����ʱ���͵�����
	 */
	public static final char TYPE_DATETIME = 't';

	/**
	 * ��ʾclob�͵�����
	 */
	public static final char TYPE_CLOB = 'c';

	/**
	 * ��ʾblob�͵�����
	 */
	public static final char TYPE_BLOB = 'b';

	/**
	 * ʹ��ָ�������ͺ����ݹ��� ColumnData ����
	 * 
	 * @param type
	 *            ��ʾ���������
	 * @param obj
	 *            ��ʾ���������
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
	 * ����������Ϊ��ֵ
	 * 
	 */
	public void setNull() {
		type = TYPE_NULL;
		obj = null;
		displayText = "";
	}

	/**
	 * ����������Ϊָ�����ַ���
	 * 
	 * @param data
	 *            ���õ��ַ�������
	 */
	public void setString(String data) {
		type = TYPE_STRING;
		obj = data;
		displayText = data;
	}

	/**
	 * ����������Ϊָ��������
	 * 
	 * @param data
	 *            ���õ���������, ��ʽyyyyMMdd
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
	 * ����������Ϊָ��������ֵ
	 * 
	 * @param data
	 *            ���õ�����ֵ
	 */
	public void setInt(int data) {
		type = TYPE_INTEGER;
		obj = new Integer(data);
		displayText = obj.toString();
	}

	/**
	 * ����������Ϊָ����ʱ��
	 * 
	 * @param data
	 *            ���õ�ʱ������, ��ʽyyyyMMddHHmmss
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
	 * ����clob����
	 * 
	 * @param obj
	 */
	public void setClob(Object _obj) {
		type = TYPE_CLOB;
		obj = _obj;
	}

	/**
	 * ����blob����
	 * 
	 * @param obj
	 */
	public void setBlob(Object _obj) {
		type = TYPE_BLOB;
		obj = _obj;
	}

	/**
	 * ���������ʾ�����ݸ��µ���Ӧ ResultSet �����ָ������
	 * 
	 * @param columnName
	 *            ָ��������
	 * @param rs
	 *            Ҫ���µĽ��������
	 * @param map
	 *            �������->�����͵Ķ�Ӧ��
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
	 * ��ȡ������Ŀ���ʾ���ı�
	 * 
	 * @return ���ر�����Ŀ���ʾ���ı�
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 * ��ָ���Ľ������Ӧ���н��бȽϣ����رȽϽ�������ڲ�ͬʱ��ӵ������б���
	 * 
	 * @param rs
	 *            Ҫ�ȽϵĽ����
	 * @param columnName
	 *            Ҫ�Ƚϵ�����
	 * @param oo
	 *            ��Ӧ������ OracleOperation ����
	 * @param operType
	 *            ��������
	 * @param keyColumn
	 *            �����е�����
	 * @return ��ͬʱ���� true, ���򷵻� false
	 * @throws SQLException
	 *             �����ݿ����ʧ��ʱ�׳� SQLException �쳣
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
