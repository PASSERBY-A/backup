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
 * 结果集操作
 * @author 梅园
 */
public class ResultSetOperation {

	/**
	 * 日期型的显示格式
	 */
	static protected SimpleDateFormat dateDisplayFormat = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");

	/**
	 * 日期型的文本格式
	 */
	static protected SimpleDateFormat dateTextFormat = new SimpleDateFormat(
	"yyyyMMddHHmmss");

	/**
	 * 获取结果集的记录总数
	 * @param rs 操作的结果集
	 * @return 返回结果集的记录总数
	 * @throws SQLException 数据库操作失败时引发 SQLException 异常
	 */
	public static int getRowCount(ResultSet rs) throws SQLException {
		rs.last();
		return rs.getRow();
	}

	/**
	 * 把指定结果集里的指定记录转化成XML字符串
	 * @param rs 要转化的结果集
	 * @param beginRow 开始处理的记录位置(从1开始)，如果是值为0，从当前条开始处理，正数表示相对于第一条，负数表示相对于最后一条
	 * @param rows 要处理beginRow后的多少行 如果值为小于0的数,表示处理beginRow位置后的所有记录
	 * @return XML格式的字符串
	 * @throws SQLException 数据库操作失败时引发 SQLException 异常
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
	 * 把指定结果集里的指定记录转化成XML字符串(从当前记录开始)
	 * @param rs 要转化的结果集
	 * @return XML格式的字符串
	 * @throws SQLException 数据库操作失败时引发 SQLException 异常
	 */
	public static String resultSetToXMLString(ResultSet rs) throws SQLException {
		return resultSetToXMLString(rs, 0, -1);
	}
	
	/**
	 * 处理结果集为list数组，每一个list元素是由一个map组成，map的key是结果集rs的列名小写，
	 * map的value是对应的每条记录的对应列的值（字符串型），时间格式为'yyyy-MM-dd HH:mm:ss'
	 * @param rs 要转化的结果集
	 * @param beginRow 开始处理的记录位置(从1开始)，如果是值为0，从第当前条开始处理，正数表示相对于第一条，负数表示相对于最后一条
	 * @param rows 要处理beginRow后的多少行，如果值为小于0的数,表示处理beginRow位置后的所有记录
	 * @return 返回生成的 List 数组
	 * @throws SQLException 数据库操作失败时引发 SQLException 异常
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
	 * 处理结果集为list数组(从当前行开始)，每一个list元素是由一个map组成，map的key是结果集rs的列名小写，
	 * map的value是对应的每条记录的对应列的值（字符串型），时间格式为'yyyy-MM-dd HH:mm:ss'
	 * @param rs 要转化的结果集
	 * @return 返回生成的 List 数组
	 * @throws SQLException 数据库操作失败时引发 SQLException 异常
	 */
	public static List resultSetToList(ResultSet rs) throws SQLException {
		return resultSetToList(rs, 0, -1);
	}
	
	/**
	 * 转化结果集里面的clob字段数据为String型
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
