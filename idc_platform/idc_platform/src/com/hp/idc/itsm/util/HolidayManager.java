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
 * �ڼ��չ�����
 * 
 * @author ���Ľ�
 * 
 */
public class HolidayManager {

	/**
	 * ��̬map,�洢���ݿ���ȡ����ֵ,���Ժ������ֱ�Ӳ�����map,���������ݿ⽻��,���Ч��
	 */
	static protected Map map = null;

	/**
	 * �����ݿ��е�ֵ�ŵ���̬map��
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
	 * �ж��Ƿ�Ϊ�ڼ���
	 * 
	 * @param date
	 *            ����
	 * @return �ڼ��շ���true, ���򷵻�false
	 */
	static public boolean isHoliday(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return isHoliday(date, sdf.format(date));
	}

	/**
	 * �ж��Ƿ�Ϊ�ڼ���
	 * 
	 * @param date
	 *            ����
	 * @param str
	 *            ����
	 * @return �ڼ��շ���true, ���򷵻�false
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
	 * �ж��Ƿ�Ϊ�ڼ���
	 * 
	 * @param date
	 *            ���� yyyymmdd
	 * @return �ڼ��շ���true, ���򷵻�false
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
	 * �ı䵱���״̬��ԭ��Ϊ���ڵĸ�Ϊ�����գ�ԭ��Ϊ�����յĸ�Ϊ����
	 * 
	 * @param date
	 *            ����
	 * @param operName
	 *            ������
	 * @throws SQLException
	 */
	static public void change(Date date, String operName) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		change(sdf.format(date), operName);
	}

	/**
	 * �ı䵱���״̬��ԭ��Ϊ���ڵĸ�Ϊ�����գ�ԭ��Ϊ�����յĸ�Ϊ����
	 * 
	 * @param date
	 *            ����
	 * @param operName
	 *            ������
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
	 * ����в�����
	 * 
	 * @param date
	 *            ����
	 * @param operName
	 *            ������
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
	 * ɾ�����е�����
	 * 
	 * @param strDate
	 *            ����
	 * @param operName
	 *            ������
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
