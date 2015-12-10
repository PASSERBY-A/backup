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
 * ITSM�����õ���һ�˹��ߺ�����
 * 
 * @author Administrator
 * 
 */
public class ItsmUtil {
	/**
	 * ���������Ϣ
	 * @param str ������Ϣ
	 */
	public static void debugPrint(String str) {
		System.out.println(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " " + str);
	}
	
	/**
	 * ��ȡ����ָ��λ�õ�ֵ��velocityģ����,�����ȡString[3]������ֵ��getObjectFromArray(strA,2)
	 * @param objA object����
	 * @param num λ��
	 * @return
	 */
	public static Object getObjectFromArray(Object[] objA,int num){
		return objA[num];
	}
	
	/**
	 * ���������ַ����Ƚϴ�С
	 * @param str1
	 * @param str2
	 * @return �ȽϽ����-1/0/1
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
	 * �����滻<br>
	 * ��������:<br>
	 * ${user}=userId����<br>
	 * ${date}=��ǰ����yyyy-MM-dd<br>
	 * ${datetime}=��ǰʱ��yyyy-MM-dd HH:mm:ss<br>
	 * ${month}=��ǰ��yyyy-MM<br>
	 * ${year}=��ǰ��yyyy<br>
	 * 
	 * @param val
	 *            �����������ַ���
	 * @param userId
	 *            ���뵱ǰ�û�
	 * @return �滻��������ַ���
	 */
	public static String replaceVariant(String val, String userId) {
		if (val == null || val.equals(""))
			return "";
		String valClone = val;
		
		//ƥ��${date}-1����${date}������ʽ�Ĵ�
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
				
				//��ȡ��ǰʱ�䣬�����${date}-3������ʽ���ڶ�-3��������
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
	 * ���ݱ��ʽ���б���й���,�б�������̳�IObjectWithAttribute�ӿ�
	 * 
	 * @param list
	 *            Ҫ���˵��б�List<IObjectWithAttribute>
	 * @param id
	 *            ��������:���ݴ����Խ��й���
	 * @param filter
	 *            ��������:���˵�������ʽ,��������ʽ�Ľ������˵�
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
	 * �ж��ַ����Ƿ�Ϊ����
	 * 
	 * @param s
	 *            ������ַ���
	 * @return ������ʱ����true,���򷵻�false
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
	 * ����ָ�������Զ��б��������,�б�������̳�IObjectWithAttribute�ӿ�
	 * 
	 * @param list
	 *            Ҫ������б�List<IObjectWithAttribute>
	 * @param id
	 *            ���ݴ����Խ�������
	 * @param desc
	 *            �Ƿ�Ϊ�Ӵ�С����
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
	 * ����ָ����ID��ȡ��һ������ֵ, ����ֵ��С��1001��ʼ
	 * 
	 * @param id
	 *            ָ����ID
	 * @return ���صõ�������ֵ
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
	 * ȡsql�ַ������:p����
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
