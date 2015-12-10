package com.hp.idc.itsm.dbo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.util.StringUtil;

/**
 * ���������
 * @author ÷԰
 */
public class ResultSetOperation {

	/**
	 * �����͵���ʾ��ʽ
	 */
	static protected SimpleDateFormat dateDisplayFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");

	/**
	 * �����͵��ı���ʽ
	 */
	static protected SimpleDateFormat dateTextFormat = new SimpleDateFormat(
	"yyyyMMddHHmmss");

	/**
	 * ��ȡ������ļ�¼����
	 * @param rs �����Ľ����
	 * @return ���ؽ�����ļ�¼����
	 * @throws SQLException ���ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public static int getRowCount(ResultSet rs) throws SQLException {
		rs.last();
		return rs.getRow();
	}

	/**
	 * ��ָ����������ָ����¼ת����XML�ַ���
	 * @param rs Ҫת���Ľ����
	 * @param beginRow ��ʼ����ļ�¼λ��(��1��ʼ)�������ֵΪ0���ӵ�ǰ����ʼ����������ʾ����ڵ�һ����������ʾ��������һ��
	 * @param rows Ҫ����beginRow��Ķ����� ���ֵΪС��0����,��ʾ����beginRowλ�ú�����м�¼
	 * @return XML��ʽ���ַ���
	 * @throws SQLException ���ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public static String resultSetToXMLString(ResultSet rs, int beginRow,
			int rows) throws SQLException {
		StringBuffer sb = new StringBuffer();
		ResultSetMetaData md = rs.getMetaData();
		int cols = md.getColumnCount();
		String[] colNames = new String[cols];
		String[] colClassNames = new String[cols];
		for (int i = 0; i < cols; i++) {
			colNames[i] = md.getColumnName(i + 1).toLowerCase();
			colClassNames[i] = md.getColumnClassName(i + 1);
		}
		sb.append("<?xml version=\"1.0\"?>\n");
		sb.append("<data>\n");
		if (beginRow != 0)
			rs.absolute(beginRow);
		else
			rs.next();
		if (rs.getRow() != 0) {
			int rowIndex = 0;
			do {
				if (rows > -1 && rowIndex >= rows)
					break;
				if (rs.isAfterLast())
					break;
				sb.append("\t<row>\n");
				for (int i = 0; i < cols; i++) {
					if (colClassNames[i].equals("java.sql.Timestamp")) {
						sb.append("\t\t<col name=\"" + colNames[i] + "\"");
						Timestamp ts = rs.getTimestamp(i + 1);
						if (ts == null) {
							Calendar cal = new GregorianCalendar();
							sb.append(" year=\"-1\"");
							sb.append(" month=\"" + cal.get(Calendar.MONTH) + "\"");
							sb.append(" day=\"" + cal.get(Calendar.DAY_OF_MONTH)
									+ "\"");
							sb.append(" hour=\"" + cal.get(Calendar.HOUR_OF_DAY) + "\"");
							sb.append(" minute=\"" + cal.get(Calendar.MINUTE)
									+ "\"");
							sb.append(" second=\"" + cal.get(Calendar.SECOND)
									+ "\"");
							sb.append(" display=\"\">");
						} else {
							Calendar cal = new GregorianCalendar();
							cal.setTime(new Date(ts.getTime()));
							sb.append(" year=\"" + cal.get(Calendar.YEAR) + "\"");
							sb.append(" month=\"" + cal.get(Calendar.MONTH) + "\"");
							sb.append(" day=\"" + cal.get(Calendar.DAY_OF_MONTH)
									+ "\"");
							sb.append(" hour=\"" + cal.get(Calendar.HOUR_OF_DAY) + "\"");
							sb.append(" minute=\"" + cal.get(Calendar.MINUTE)
									+ "\"");
							sb.append(" second=\"" + cal.get(Calendar.SECOND)
									+ "\"");
							sb.append(" display=\""
									+ dateDisplayFormat.format(cal.getTime())
									+ "\">");
							sb.append(dateTextFormat.format(cal.getTime()));
						}
					} else {
						sb.append("\t\t<col name=\"" + colNames[i] + "\">");
						String str = rs.getString(i + 1);
						if (str != null)
							sb.append(StringUtil.escapeXml(str));
					}
					sb.append("</col>\n");
				}
				sb.append("\t</row>\n");
				rowIndex++;
			} while (rs.next());
		}

		sb.append("</data>\n");
		return sb.toString();
	}

	/**
	 * ��ָ����������ָ����¼ת����XML�ַ���(�ӵ�ǰ��¼��ʼ)
	 * @param rs Ҫת���Ľ����
	 * @return XML��ʽ���ַ���
	 * @throws SQLException ���ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public static String resultSetToXMLString(ResultSet rs) throws SQLException {
		return resultSetToXMLString(rs, 0, -1);
	}
	
	/**
	 * ��������Ϊlist���飬ÿһ��listԪ������һ��map��ɣ�map��key�ǽ����rs������Сд��
	 * map��value�Ƕ�Ӧ��ÿ����¼�Ķ�Ӧ�е�ֵ���ַ����ͣ���ʱ���ʽΪ'yyyy-MM-dd HH:mm:ss'
	 * @param rs Ҫת���Ľ����
	 * @param beginRow ��ʼ����ļ�¼λ��(��1��ʼ)�������ֵΪ0���ӵڵ�ǰ����ʼ����������ʾ����ڵ�һ����������ʾ��������һ��
	 * @param rows Ҫ����beginRow��Ķ����У����ֵΪС��0����,��ʾ����beginRowλ�ú�����м�¼
	 * @return �������ɵ� List ����
	 * @throws SQLException ���ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public static List resultSetToList(ResultSet rs, int beginRow, int rows)
			throws SQLException {
		List returnList = new ArrayList();
		ResultSetMetaData md = rs.getMetaData();
		int cols = md.getColumnCount();
		String[] colNames = new String[cols];
		String[] colClassNames = new String[cols];
		for (int i = 0; i < cols; i++) {
			colNames[i] = md.getColumnName(i + 1).toLowerCase();
			colClassNames[i] = md.getColumnClassName(i + 1);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (beginRow != 0)
			rs.absolute(beginRow);
		else
			rs.next();
		if (rs.getRow() != 0) {
			int rowIndex = 0;
			do {
				if (rows > -1 && rowIndex >= rows)
					break;
				if (rs.isAfterLast())
					break;
				rowIndex++;
	
				Map rowMap = new HashMap();
				for (int i = 0; i < cols; i++) {
					if (colClassNames[i].equals("java.sql.Timestamp")) {
						Timestamp ts = rs.getTimestamp(i + 1);
						if (ts == null)
							rowMap.put(colNames[i], "");
						else
							rowMap.put(colNames[i], sdf.format(new Date(ts
									.getTime())));
					} else {
						String str = rs.getString(i + 1);
						rowMap.put(colNames[i], str);
					}
				}
				returnList.add(rowMap);
			} while (rs.next());
		}
		return returnList;
	}

	/**
	 * ��������Ϊlist����(�ӵ�ǰ�п�ʼ)��ÿһ��listԪ������һ��map��ɣ�map��key�ǽ����rs������Сд��
	 * map��value�Ƕ�Ӧ��ÿ����¼�Ķ�Ӧ�е�ֵ���ַ����ͣ���ʱ���ʽΪ'yyyy-MM-dd HH:mm:ss'
	 * @param rs Ҫת���Ľ����
	 * @return �������ɵ� List ����
	 * @throws SQLException ���ݿ����ʧ��ʱ���� SQLException �쳣
	 */
	public static List resultSetToList(ResultSet rs) throws SQLException {
		return resultSetToList(rs, 0, -1);
	}
	
	/**
	 * ת������������clob�ֶ�����ΪString��
	 * @param clob
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static String clobToString(Clob clob) throws IOException,SQLException {
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
}
