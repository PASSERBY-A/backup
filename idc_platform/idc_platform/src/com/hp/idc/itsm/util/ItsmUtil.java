package com.hp.idc.itsm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.IObjectWithAttribute;

/**
 * ITSM库中用到的一此工具函数类
 * 
 * @author Administrator
 * 
 */
public class ItsmUtil {
	/**
	 * 输出调试信息
	 * @param str 调试信息
	 */
	public static void debugPrint(String str) {
		System.out.println(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " " + str);
	}
	
	/**
	 * 获取数组指定位置的值，velocity模板用,例如获取String[3]第三个值，getObjectFromArray(strA,2)
	 * @param objA object数组
	 * @param num 位置
	 * @return
	 */
	public static Object getObjectFromArray(Object[] objA,int num){
		return objA[num];
	}
	
	/**
	 * 进行中文字符串比较大小
	 * @param str1
	 * @param str2
	 * @return 比较结果，-1/0/1
	 */
	public static int compareChinese(String str1, String str2) {
		int ret = 0, pos = 0;
		int p1, p2;
		int n1 = str1.length();
		int n2 = str2.length();
		for ( ; ; pos++) {
			p1 = (pos < n1) ? Cache.CodeMapping[str1.charAt(pos)] : 0;
			p2 = (pos < n2) ? Cache.CodeMapping[str2.charAt(pos)] : 0;
			ret = p1 - p2;
			if (ret != 0 || pos == n2)
				break;
		}
		if (ret < 0)
			ret = -1;
		else if (ret > 0)
			ret = 1;
		return ret;
	}

	/**
	 * 变量替换<br>
	 * 变量包括:<br>
	 * ${user}=userId参数<br>
	 * ${date}=当前日期yyyy-MM-dd<br>
	 * ${datetime}=当前时间yyyy-MM-dd HH:mm:ss<br>
	 * ${month}=当前月yyyy-MM<br>
	 * ${year}=当前年yyyy<br>
	 * 
	 * @param val
	 *            包含变量的字符串
	 * @param userId
	 *            输入当前用户
	 * @return 替换变量后的字符串
	 */
	public static String replaceVariant(String val, String userId) {
		if (val == null || val.equals(""))
			return "";
		String valClone = val;
		
		//匹配${date}-1或者${date}这种形式的串
		String regx = "(\\$\\{[a-z]+\\})([-\\+]?)(\\d*)";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(valClone);
		while (matcher.find()) {
			String ret = matcher.group(1);
			String rpValue = "";
			if (ret.equalsIgnoreCase("${user}")) {
				rpValue = userId;
			}
			if (ret.equalsIgnoreCase("${date}")||ret.equalsIgnoreCase("${datetime}") ||ret.equalsIgnoreCase("${month}")||ret.equalsIgnoreCase("${year}")) {
				int subValue = 0;
				if (!matcher.group(3).equals(""))
					subValue = Integer.parseInt(matcher.group(3));
				String opS = matcher.group(2);
				
				//获取当前时间，如果是${date}-3这种形式，在对-3进行运算
				Calendar cal = Calendar.getInstance();
				
				if (!opS.equals("")) {
					int subObject = Calendar.DATE;
					if (ret.equalsIgnoreCase("${month}"))
						subObject = Calendar.MONTH;
					else if (ret.equalsIgnoreCase("${year}"))
						subObject = Calendar.YEAR;
					
					int date = cal.get(subObject);
					if (opS.equals("-"))
						cal.set(subObject, date-subValue);
					if (opS.equals("+"))
						cal.set(subObject, date+subValue);
				}
				SimpleDateFormat sdf = null;
				if (ret.equalsIgnoreCase("${date}"))
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				else if (ret.equalsIgnoreCase("${datetime}"))
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				else if (ret.equalsIgnoreCase("${month}"))
					sdf = new SimpleDateFormat("yyyy-MM-01");
				else if (ret.equalsIgnoreCase("${year}"))
					sdf = new SimpleDateFormat("yyyy-01-01");
				else // default
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					rpValue = sdf.format(cal.getTime());
				} catch (Exception e) {
					return "";
				}
			}
			String matchStr = matcher.group(0);
			matchStr = matchStr.replaceAll("\\$", "\\\\\\$");
			matchStr = matchStr.replaceAll("\\}", "\\\\\\}");
			matchStr = matchStr.replaceAll("\\{", "\\\\\\{");
			matchStr = matchStr.replaceAll("\\+", "\\\\\\+");
			
			valClone = valClone.replaceAll(matchStr, rpValue);
		}
		return valClone;
	}

	/**
	 * 根据表达式对列表进行过滤,列表对象必须继承IObjectWithAttribute接口
	 * 
	 * @param list
	 *            要过滤的列表List<IObjectWithAttribute>
	 * @param id
	 *            过滤条件:根据此属性进行过滤
	 * @param filter
	 *            过滤条件:过滤的正则表达式,不满足表达式的将被过滤掉
	 */
	public static void filter(List list, String id, String filter) {
		Pattern p = null;
		if (filter != null && filter.length() > 0) {
			try {
				p = Pattern.compile(filter);
			} catch (Exception e) {
				System.out.println("filter error: " + filter);
				e.printStackTrace();
				return;
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (!(list.get(i) instanceof IObjectWithAttribute)) {
				list.remove(i);
				i--;
				continue;
			}
			IObjectWithAttribute obj = (IObjectWithAttribute) list.get(i);
			String v = obj.getAttribute(id);
			if (v == null)
				v = "";
			if (p != null && !p.matcher(v).find()) {
				list.remove(i);
				i--;
				continue;
			}
		}
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param s
	 *            输入的字符串
	 * @return 是数字时返回true,否则返回false
	 */
	public static boolean isNumber(String s) {
		int n = 0;
		if (s == null || (n = s.length()) == 0)
			return false;
		int pos = 0, count = 0, dot = 0;
		if (s.charAt(0) == '-')
			pos++;
		while (pos < n) {
			char c = s.charAt(pos);
			if (c == '.') {
				dot++;
				if (dot > 1)
					return false;
			} else if (c >= '0' && c <= '9') {
				count++;
			} else {
				return false;
			}
			pos++;
		}
		return (count > 0);
	}

	/**
	 * 根据指定的属性对列表进行排序,列表对象必须继承IObjectWithAttribute接口
	 * 
	 * @param list
	 *            要排序的列表List<IObjectWithAttribute>
	 * @param id
	 *            根据此属性进行排序
	 * @param desc
	 *            是否为从大到小排序
	 */
	public static void sort(List list, String id, boolean desc) {
		for (int i = 0; i < list.size(); i++) {
			if (!(list.get(i) instanceof IObjectWithAttribute))
				return;
		}
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				IObjectWithAttribute o1 = (IObjectWithAttribute) list.get(i);
				IObjectWithAttribute o2 = (IObjectWithAttribute) list.get(j);
				String s1 = o1.getAttribute(id);
				String s2 = o2.getAttribute(id);
				if (s1 == null || s2 == null)
					continue;
				boolean swap = false;
				if (isNumber(s1) && isNumber(s2)) {
					double d1 = Double.parseDouble(s1);
					double d2 = Double.parseDouble(s2);
					if (desc && d1 < d2)
						swap = true;
					else if (!desc && d1 > d2)
						swap = true;
				} else {
					if (desc && s1.compareTo(s2) < 0)
						swap = true;
					else if (!desc && compareChinese(s1, s2) > 0)
						swap = true;
				}
				if (swap) {
					list.remove(j);
					list.remove(i);
					list.add(i, o2);
					list.add(j, o1);
				}
			}
		}
	}

	/**
	 * 根据指定的ID获取下一个序列值, 序列值最小从1001开始
	 * 
	 * @param id
	 *            指定的ID
	 * @return 返回得到的序列值
	 * @throws SQLException
	 */
	public static synchronized int getSequence(String id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = ConnectManager.getConnection();
		SQLException se = null;
		int startCount = 1001;
		int ret = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("update itsm_sequence set value=value+1 where id=?");
			ps.setString(1, id);
			int n = ps.executeUpdate();
			if (n == 0) {
				ps.close();
				ps = conn
						.prepareStatement("insert into itsm_sequence(id, value) values(?, ?)");
				ps.setString(1, id);
				ps.setInt(2, startCount);
				ps.executeUpdate();
				ret = startCount;
			} else {
				ps.close();
				ps = conn
						.prepareStatement("select value from itsm_sequence where id=?");
				ps.setString(1, id);
				rs = ps.executeQuery();
				rs.next();
				ret = rs.getInt(1);
			}
			conn.commit();
			conn.setAutoCommit(true);
			if (rs != null) {
				rs.close();
				rs = null;
			}
			ps.close();
			ps = null;
			conn.close();
			conn = null;
		} catch (SQLException e) {
			se = e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				rs = null;
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ps = null;
			}
			if (conn != null) {
				try {
					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				conn = null;
			}
		}
		if (se != null)
			throw se;
		return ret;
	}
	
	/**
	 * 取sql字符串里的:p参数
	 * @param sql
	 * @return
	 */
	public static List getSQLParameters(String sql) {
		List retList = new ArrayList();
		// String temp = "";
		//String sqlTemp = sql;
		String paraRegex = ":([\\w]*)";
		Pattern pattern = Pattern.compile(paraRegex);
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String paramName = matcher.group(1);
			retList.add(paramName);
		}
		return retList;
	}
}
