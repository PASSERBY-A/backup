package com.hp.idc.itsm.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;

/**
 * 节假日管理类
 * 
 * @author 梁文剑
 * 
 */
public class HolidayManager {

	/**
	 * 静态map,存储数据库中取出的值,在以后操作中直接操作该map,而不与数据库交互,提高效率
	 */
	static protected Map map = null;

	/**
	 * 将数据库中的值放到静态map中
	 * 
	 * @throws SQLException
	 */
	static protected synchronized void init() throws SQLException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
		PreparedStatement ps = null;
		Map m = new HashMap();
		try {
			String sql = "select * from itsm_holiday_info";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				m.put(rs.getString("str_date"), "1");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		map = m;
	}

	/**
	 * 判断是否为节假日
	 * 
	 * @param date
	 *            日期
	 * @return 节假日返回true, 否则返回false
	 */
	static public boolean isHoliday(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return isHoliday(date, sdf.format(date));
	}

	/**
	 * 判断是否为节假日
	 * 
	 * @param date
	 *            日期
	 * @param str
	 *            日期
	 * @return 节假日返回true, 否则返回false
	 */
	static private boolean isHoliday(Date date, String str) {
		if (map == null) {
			try {
				init();
			} catch (SQLException e) {
				return false;
			}
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int n = calendar.get(Calendar.DAY_OF_WEEK);
		boolean ret = false;
		if (n == Calendar.SATURDAY || n == Calendar.SUNDAY)
			ret = true;
		if (map.get(str) != null)
			ret = !ret;
		return ret;
	}

	/**
	 * 判断是否为节假日
	 * 
	 * @param date
	 *            日期 yyyymmdd
	 * @return 节假日返回true, 否则返回false
	 */
	static public boolean isHoliday(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return isHoliday(sdf.parse(date), date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 改变当天的状态：原来为假期的改为工作日；原来为工作日的改为假期
	 * 
	 * @param date
	 *            日期
	 * @param operName
	 *            操作人
	 * @throws SQLException
	 */
	static public void change(Date date, String operName) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		change(sdf.format(date), operName);
	}

	/**
	 * 改变当天的状态：原来为假期的改为工作日；原来为工作日的改为假期
	 * 
	 * @param date
	 *            日期
	 * @param operName
	 *            操作人
	 * @throws SQLException
	 */
	static public synchronized void change(String date, String operName)
			throws SQLException {
		if (map == null) {
			try {
				init();
			} catch (SQLException e) {
				return;
			}
		}

		if (map.get(date) != null) {
			delete(date, operName);
		} else {
			add(date, operName);
		}
	}

	/**
	 * 向表中插数据
	 * 
	 * @param date
	 *            日期
	 * @param operName
	 *            操作者
	 * @throws SQLException
	 */
	static private void add(String date, String operName) throws SQLException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("itsm_holiday_info", operName);

		try {
			u.setColumnData("STR_DATE", new ColumnData(ColumnData.TYPE_STRING,
					date));

			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		map.put(date, "1");
	}

	/**
	 * 删除表中的数据
	 * 
	 * @param strDate
	 *            日期
	 * @param operName
	 *            操作者
	 * @throws SQLException
	 */
	static private void delete(String strDate, String operName)
			throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("itsm_holiday_info", operName);
		try {
			ps = u.getStatement("str_date=?");
			ps.setString(1, strDate);
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		map.remove(strDate);
	}

}
